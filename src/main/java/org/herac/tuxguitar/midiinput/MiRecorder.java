package org.herac.tuxguitar.midiinput;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.transport.TransportPlayAction;
import org.herac.tuxguitar.gui.actions.transport.TransportStopAction;
import org.herac.tuxguitar.gui.editors.TablatureEditor;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGTrack;

class MiRecorder {
  static private MiRecorder s_Instance;

  static public MiRecorder instance() {
    if (s_Instance == null)
      s_Instance = new MiRecorder();

    return s_Instance;
  }

  private MiBuffer f_Buffer = new MiBuffer(); // input notes buffer
  private boolean f_IsRecording;
  private boolean f_SavedMetronomeStatus;
  private long f_StartPosition; // recording start position [ticks]
  private int f_Tempo; // recording tempo [bpm]

  private TGTrack f_TempTrack; // temporary track

  private boolean s_TESTING = true;

  private MiRecorder() {
  }

  public void addNote(byte inString, byte inFret, byte inPitch,
      byte inVelocity, long inTimeStamp) {
    this.f_Buffer.addEvent(inString, inFret, inPitch, inVelocity, inTimeStamp);
  }

  int getTempo() // just for DEBUG
  {
    return (this.f_Tempo);
  }

  public boolean isRecording() {
    return (this.f_IsRecording);
  }

  public void start() {
    TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();

    if (this.s_TESTING) {
      TGTempo tempo = new TGTempo();

      tempo.setValue(80);
      tgSongMgr.changeTempos(TGDuration.QUARTER_TIME, tempo, true);
    }

    this.f_SavedMetronomeStatus = TuxGuitar.instance().getPlayer()
        .isMetronomeEnabled();
    TuxGuitar.instance().getPlayer().setMetronomeEnabled(true);

    TablatureEditor editor = TuxGuitar.instance().getTablatureEditor();
    Caret caret = editor.getTablature().getCaret();

    this.f_Tempo = caret.getMeasure().getTempo().getValue();
    this.f_StartPosition = caret.getMeasure().getStart();

    this.f_TempTrack = tgSongMgr.createTrack();

    this.f_TempTrack.setName("Traccia temporanea input MIDI");
    /*
     * // allocate measures int requestedMeasuresCount = 10, currMeasuresCount =
     * tgSongMgr.getSong().countMeasureHeaders();
     * 
     * for(int m = currMeasuresCount + 1; m < requestedMeasuresCount; m++)
     * tgSongMgr.addNewMeasure(m);
     * 
     * TuxGuitar.instance().fireUpdate();
     * TuxGuitar.instance().getMixer().update();
     */
    TuxGuitar.instance().getAction(TransportPlayAction.NAME).process(null);

    // come si sincronizza il timestamp iniziale con il playback?
    this.f_Buffer.startRecording(MiPort.getNotesPortTimeStamp());
    this.f_IsRecording = true;
  }

  public void stop() {
    TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();

    this.f_Buffer.stopRecording(MiPort.getNotesPortTimeStamp());
    this.f_IsRecording = false;

    TuxGuitar.instance().getAction(TransportStopAction.NAME).process(null);
    TuxGuitar.instance().getPlayer().setMetronomeEnabled(
        this.f_SavedMetronomeStatus);

    // qui deve cancellare la traccia di servizio...
    tgSongMgr.removeTrack(this.f_TempTrack);

    if (this.f_Buffer.finalize((byte) MiConfig.instance().getMinVelocity(),
        (long) MiConfig.instance().getMinDuration() * 1000) > 0) {
      this.f_Buffer.toTrack(this.f_Tempo, this.f_StartPosition,
          "Nuovo input MIDI");
    }

    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
  }
}