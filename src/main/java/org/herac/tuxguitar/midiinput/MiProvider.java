package org.herac.tuxguitar.midiinput;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.SortedSet;

import javax.sound.midi.ShortMessage;
import javax.swing.Timer;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.ActionLock;
import org.herac.tuxguitar.gui.editors.TablatureEditor;
import org.herac.tuxguitar.gui.editors.fretboard.FretBoard;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.gui.tools.scale.ScaleManager;
import org.herac.tuxguitar.gui.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.gui.util.MessageDialog;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.util.TGSynchronizer;

public class MiProvider {
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger.getLogger(MiProvider.class);

  static private MiProvider s_Instance;

  static int getFret(int inPitch, int inString) {
    // returns the 0-based fret index corresponding to the specified note and
    // string
    // or -1 if an error occurred

    int stringFirstPitch = getStringFirstPitch(inString);

    if (stringFirstPitch != -1) {
      int fret = inPitch - stringFirstPitch;

      if (fret >= 0 && fret < FretBoard.MAX_FRETS)
        return (fret);
    }

    return (-1);
  }

  static int getStringFirstPitch(int inString) {
    // returns the note corresponding to the free vibrating string
    // or -1 if the current track does not have such string

    TGTrackImpl track = TuxGuitar.instance().getTablatureEditor()
        .getTablature().getCaret().getTrack();

    if (track != null && track.getStrings().size() >= inString)
      return (track.getString(inString).getValue());

    return (-1);
  }

  static public MiProvider instance() {
    if (s_Instance == null)
      s_Instance = new MiProvider();

    return s_Instance;
  }

  private final int DEVICE_CHANNELS_COUNT = 6; // number of MIDI channels
  // supported by the input device

  private int f_BaseChannel = 0; // 0-based MIDI channel corresponding to the
  // first string
  private MiBuffer f_Buffer = new MiBuffer(); // input notes buffer
  private int f_ChordMode = MiConfig.CHORD_MODE_DIAGRAM; // current chord mode
  private TGBeat f_EchoBeat = null; // beat for echo rendering
  private boolean f_EchoLastWasOn = false; // indicates if last note message was
  // NOTE_ON

  private int[] f_EchoNotes = new int[6]; // list of notes for echo
  private int f_EchoTimeOut = MiConfig.DEF_ECHO_TIMEOUT; // time out for echo
  // rendering [msec]

  private Timer f_EchoTimer = null; // timer for echo rendering

  private int f_InputTimeOut = MiConfig.DEF_INPUT_TIMEOUT; // time out for
  // chord/scale input
  // [msec]

  private Timer f_InputTimer = null; // timer for chord/scale input

  private long f_MinDuration = MiConfig.DEF_DURATION_THRESHOLD; // notes with
  // duration
  // lower than
  // this
  // threshold are
  // considered
  // unwanted
  // noise

  private byte f_MinVelocity = MiConfig.DEF_VELOCITY_THRESHOLD; // notes with
  // velocity
  // lower than
  // this
  // threshold are
  // considered
  // unwanted
  // noise

  private int f_Mode = MiConfig.MODE_FRETBOARD_ECHO; // current mode

  private MiProvider() {
    echo_ResetNotes();
  }

  private void chord_AddNote(byte inString, byte inFret, byte inPitch,
      byte inVelocity, long inTimeStamp) {
    if (this.f_InputTimer == null) {
      ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          f_InputTimer.stop();
          f_InputTimer = null;

          f_Buffer.stopRecording(MiPort.getNotesPortTimeStamp());
          // LOG.debug("Chord ended");

          if (f_Buffer.finalize(f_MinVelocity, f_MinDuration * 1000) > 0) {
            if (!TuxGuitar.instance().getPlayer().isRunning()
                && !TuxGuitar.instance().isLocked() && !ActionLock.isLocked()) {
              TablatureEditor editor = TuxGuitar.instance()
                  .getTablatureEditor();
              Caret caret = editor.getTablature().getCaret();
              TGTrackImpl track = caret.getTrack();
              TGMeasureImpl measure = caret.getMeasure();
              TGBeat beat = caret.getSelectedBeat();
              TGSongManager songMgr = TuxGuitar.instance().getSongManager();

              TGChord chord = f_Buffer
                  .toChord(measure.getTrack().stringCount());
              // TGBeat _beat = this.f_Buffer.toBeat();

              // emulates InsertChordAction
              ActionLock.lock();

              UndoableMeasureGeneric undoable = UndoableMeasureGeneric
                  .startUndo();

              if (f_ChordMode == MiConfig.CHORD_MODE_ALL) {
                songMgr.getMeasureManager().cleanBeat(beat);

                TGVoice voice = beat.getVoice(caret.getVoice());
                Iterator it = track.getStrings().iterator();

                while (it.hasNext()) {
                  TGString string = (TGString) it.next();
                  int value = chord.getFretValue(string.getNumber() - 1);

                  if (value >= 0) {
                    TGNote note = new TGNoteImpl();
                    note.setValue(value);
                    note.setVelocity(editor.getTablature().getCaret()
                        .getVelocity());
                    note.setString(string.getNumber());

                    TGDuration duration = new TGDuration();
                    voice.getDuration().copy(duration);

                    songMgr.getMeasureManager().addNote(beat, note, duration,
                        voice.getIndex());
                  }
                }
              }

              songMgr.getMeasureManager().addChord(beat, chord);
              TuxGuitar.instance().getFileHistory().setUnsavedFile();
              editor.getTablature().getViewLayout().fireUpdate(
                  measure.getNumber());

              TuxGuitar.instance().getUndoableManager().addEdit(
                  undoable.endUndo());

              ActionLock.unlock();
              TuxGuitar.instance().updateCache(true);
            }
          }
        }
      };

      if (inVelocity > 0) {
        // LOG.debug("New chord");

        this.f_Buffer.startRecording(MiPort.getNotesPortTimeStamp());
        this.f_Buffer.addEvent(inString, inFret, inPitch, inVelocity,
            inTimeStamp);

        this.f_InputTimer = new Timer(f_InputTimeOut, taskPerformer);
        this.f_InputTimer.start();
      }
    } else {
      this.f_Buffer
          .addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

      if (inVelocity > 0)
        this.f_InputTimer.restart();
    }
  }

  private void echo(int inString, int inFret, boolean inIsNoteOn) {
    if (f_EchoTimer == null) {
      ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          if (f_EchoLastWasOn) {
            f_EchoTimer.restart();
          } else {
            f_EchoTimer.stop();
            f_EchoTimer = null;

            if (f_Mode != MiConfig.MODE_SCALES_RECOGNITION)
              echo_UpdateExternalBeat(true);
          }
        }
      };

      echo_ResetNotes();
      echo_BuildAndShowBeat(inString, inFret, inIsNoteOn);
      this.f_EchoTimer = new Timer(this.f_EchoTimeOut, taskPerformer);
      this.f_EchoTimer.start();
    } else {
      echo_BuildAndShowBeat(inString, inFret, inIsNoteOn);
      this.f_EchoTimer.restart();
    }
  }

  private void echo_BuildAndShowBeat(int inString, int inFret, boolean inIsOn) {
    if (this.f_Mode == MiConfig.MODE_SCALES_RECOGNITION
        && this.f_InputTimer == null)
      return;

    this.f_EchoNotes[inString - 1] = (inIsOn ? inFret : -1);
    this.f_EchoLastWasOn = inIsOn;

    TGSongManager songMgr = TuxGuitar.instance().getSongManager();

    this.f_EchoBeat = new TGBeatImpl();

    for (int s = 0; s < this.f_EchoNotes.length; s++) {
      if (this.f_EchoNotes[s] != -1) {
        TGNote note = new TGNoteImpl();

        note.setString(s + 1);
        note.setValue(this.f_EchoNotes[s]);
        this.f_EchoBeat.getVoice(0).addNote(note);
      }
    }

    echo_UpdateExternalBeat(false);
  }

  public void echo_ResetNotes() {
    this.f_EchoLastWasOn = false;

    for (int s = 0; s < this.f_EchoNotes.length; s++)
      this.f_EchoNotes[s] = -1;
  }

  private void echo_UpdateExternalBeat(boolean inIsEmpty) {
    TGSynchronizer.TGRunnable task;

    if (inIsEmpty) {
      task = new TGSynchronizer.TGRunnable() {
        public void run() throws Throwable {
          TuxGuitar.instance().hideExternalBeat();
        }
      };
    } else {
      task = new TGSynchronizer.TGRunnable() {
        public void run() throws Throwable {
          TuxGuitar.instance().showExternalBeat(f_EchoBeat);
        }
      };
    }

    try {
      TGSynchronizer.instance().runLater(task);
    } catch (Throwable t) {
      MessageDialog.errorMessage(t);
    }
  }

  int getMode() {
    return this.f_Mode;
  }

  private int getString(int inChannel) {
    // returns the 1-based string index corresponding to the 0-based specified
    // MIDI channel
    // or -1 if the current track does not have such string

    TGTrackImpl track = TuxGuitar.instance().getTablatureEditor()
        .getTablature().getCaret().getTrack();

    if (track != null) {
      int stringsCount = track.getStrings().size(), stringIndex = inChannel
          - (this.f_BaseChannel + (this.DEVICE_CHANNELS_COUNT - stringsCount))
          + 1;

      if (stringIndex > 0 && stringIndex <= stringsCount)
        return (stringIndex);
    }

    return (-1);
  }

  public void noteReceived(ShortMessage inMessage, long inTimeStamp) {
    byte pitch = (byte) inMessage.getData1(), velocity = (byte) inMessage
        .getData2(), stringIndex = (byte) getString(inMessage.getChannel());

    if (stringIndex != -1) {
      byte fretIndex = (byte) getFret(pitch, stringIndex);

      if (fretIndex != -1) {
        switch (inMessage.getCommand()) {
        case ShortMessage.NOTE_ON: {
          switch (this.f_Mode) {
          case MiConfig.MODE_FRETBOARD_ECHO:
            if (velocity == 0 || velocity > this.f_MinVelocity) // questo VA
              // MODIFICATO!!!
              echo(stringIndex, fretIndex, velocity > 0);
            break;

          case MiConfig.MODE_CHORDS_RECORDING:
            if (velocity == 0 || velocity > this.f_MinVelocity) // questo VA
              // MODIFICATO!!!
              echo(stringIndex, fretIndex, velocity > 0);

            chord_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
            break;

          case MiConfig.MODE_SCALES_RECOGNITION:
            if (velocity == 0 || velocity > this.f_MinVelocity) // questo VA
              // MODIFICATO!!!
              echo(stringIndex, fretIndex, velocity > 0);

            scale_AddNote(stringIndex, fretIndex, pitch, velocity, inTimeStamp);
            break;

          case MiConfig.MODE_SONG_RECORDING:
            if (velocity == 0 || velocity > this.f_MinVelocity) // questo VA
              // MODIFICATO!!!
              echo(stringIndex, fretIndex, velocity > 0);

            MiRecorder.instance().addNote(stringIndex, fretIndex, pitch,
                velocity, inTimeStamp);
            break;
          }
        }
          break;

        case ShortMessage.NOTE_OFF:
          switch (this.f_Mode) {
          case MiConfig.MODE_FRETBOARD_ECHO:
            echo(stringIndex, fretIndex, false);
            break;

          case MiConfig.MODE_CHORDS_RECORDING:
            echo(stringIndex, fretIndex, false);
            chord_AddNote(stringIndex, fretIndex, pitch, (byte) 0, inTimeStamp);
            break;

          case MiConfig.MODE_SCALES_RECOGNITION:
            echo(stringIndex, fretIndex, false);
            scale_AddNote(stringIndex, fretIndex, pitch, (byte) 0, inTimeStamp);
            break;

          case MiConfig.MODE_SONG_RECORDING:
            echo(stringIndex, fretIndex, false);
            MiRecorder.instance().addNote(stringIndex, fretIndex, pitch,
                (byte) 0, inTimeStamp);
            break;
          }
          break;
        }
      }
    }
  }

  private void scale_AddNote(byte inString, byte inFret, byte inPitch,
      byte inVelocity, long inTimeStamp) {
    if (f_InputTimer == null) {
      ActionListener taskPerformer = new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
          f_InputTimer.stop();
          f_InputTimer = null;

          f_Buffer.stopRecording(MiPort.getNotesPortTimeStamp());
          // LOG.debug("Scale ended");

          if (f_Buffer.finalize(f_MinVelocity, f_MinDuration * 1000) > 0) {
            TGBeat beat = f_Buffer.toBeat();
            SortedSet<Byte> pitches = f_Buffer.toPitchesSet();

            MiScaleFinder.findMatchingScale(pitches);
            TuxGuitar.instance().showExternalBeat(beat);
          } else {
            TuxGuitar.instance().hideExternalBeat();
            MiScaleFinder.selectScale(ScaleManager.NONE_SELECTION, 0);
          }

          TuxGuitar.instance().updateCache(true);
        }
      };

      if (inVelocity > 0) {
        // LOG.debug("New scale");

        this.f_Buffer.startRecording(MiPort.getNotesPortTimeStamp());
        this.f_Buffer.addEvent(inString, inFret, inPitch, inVelocity,
            inTimeStamp);

        this.f_InputTimer = new Timer(f_InputTimeOut, taskPerformer);
        this.f_InputTimer.start();
      }
    } else {
      this.f_Buffer
          .addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);

      if (inVelocity > 0)
        this.f_InputTimer.restart();
    }
  }

  public void setBaseChannel(int inValue) {
    this.f_BaseChannel = inValue;
  }

  public void setChordMode(int inValue) {
    this.f_ChordMode = inValue;
  }

  public void setEchoTimeOut(int inValue) {
    this.f_EchoTimeOut = inValue;
  }

  public void setInputTimeOut(int inValue) {
    this.f_InputTimeOut = inValue;
  }

  public void setMinDuration(long inValue) {
    this.f_MinDuration = inValue;
  }

  public void setMinVelocity(byte inValue) {
    this.f_MinVelocity = inValue;
  }

  public void setMode(int inValue) {
    this.f_Mode = inValue;
    echo_ResetNotes();
  }

}
