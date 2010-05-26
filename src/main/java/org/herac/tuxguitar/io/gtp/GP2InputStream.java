package org.herac.tuxguitar.io.gtp;

import java.awt.Color;
import java.io.IOException;

import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTextImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVelocities;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;
import org.herac.tuxguitar.song.models.effects.EffectPoint;
import org.herac.tuxguitar.song.models.effects.HarmonicEffect;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GP2InputStream extends GTPInputStream {

  private static final String SUPPORTED_VERSIONS[] = new String[] {
      "FICHIER GUITAR PRO v2.20", "FICHIER GUITAR PRO v2.21" };

  private static final short TRACK_CHANNELS[][] = new short[][] {
      new short[] { 0, 1 }, new short[] { 2, 3 }, new short[] { 4, 5 },
      new short[] { 6, 7 }, new short[] { 8, 10 }, new short[] { 11, 12 },
      new short[] { 13, 14 }, new short[] { 9, 9 }, };

  private static final int TRACK_COUNT = 8;

  public GP2InputStream(GTPSettings settings) {
    super(settings, SUPPORTED_VERSIONS);
  }

  private TGBeat getBeat(TGMeasure measure, long start) {
    if (start >= measure.getStart()
        && start < (measure.getStart() + measure.getLength())) {
      for (final TGBeat beat : measure.getBeats()) {
        if (beat.getStart() == start) {
          return beat;
        }
      }
    }
    return null;
  }

  private TGBeat getBeat(TGTrack track, TGMeasure measure, long start) {
    TGBeat beat = getBeat(measure, start);
    if (beat == null) {
      for (int i = (track.countMeasures() - 1); i >= 0; i--) {
        beat = getBeat(track.getMeasure(i), start);
        if (beat != null) {
          break;
        }
      }
    }
    return beat;
  }

  private Clef getClef(TGTrack track) {
    if (!track.isPercussionTrack()) {
      for (final TGString string : track.getStrings()) {
        if (string.getValue() <= 34) {
          return Clef.BASS;
        }
      }
    }
    return Clef.TREBLE;
  }

  public TGFileFormat getFileFormat() {
    return new TGFileFormat("Guitar Pro 2", "*.gtp");
  }

  private int parseRepeatAlternative(TGSong song, int measure, int value) {
    int repeatAlternative = 0;
    int existentAlternatives = 0;
    for (final TGMeasureHeader header : song.getMeasureHeaders()) {
      if (header.getNumber() == measure) {
        break;
      }
      if (header.isRepeatOpen()) {
        existentAlternatives = 0;
      }
      existentAlternatives |= header.getRepeatAlternative();
    }

    for (int i = 0; i < 8; i++) {
      if (value > i && (existentAlternatives & (1 << i)) == 0) {
        repeatAlternative |= (1 << i);
      }
    }
    return repeatAlternative;
  }

  private long readBeat(TGTrack track, TGMeasure measure, long start,
      long lastReadedStart) throws IOException {
    readInt();

    TGBeat beat = new TGBeatImpl();
    TGVoice voice = beat.getVoice(0);
    TGDuration duration = readDuration();
    TGNoteEffect effect = new TGNoteEffect();

    int flags1 = readUnsignedByte();
    int flags2 = readUnsignedByte();

    if ((flags2 & 0x02) != 0) {
      readMixChange(measure.getTempo());
    }

    if ((flags2 & 0x01) != 0) {
      readUnsignedByte(); // strokeType
      readUnsignedByte(); // strokeDuration
    }

    duration.setDotted(((flags1 & 0x10) != 0));
    if ((flags1 & 0x20) != 0) {
      duration.setDivision(TGDivisionType.DEFAULT);
      skip(1);
    }

    // beat effects
    if ((flags1 & 0x04) != 0) {
      readBeatEffects(effect);
    }

    // chord diagram
    if ((flags1 & 0x02) != 0) {
      readChord(track.stringCount(), beat);
    }

    // text
    if ((flags1 & 0x01) != 0) {
      readText(beat);
    }

    if ((flags1 & 0x40) != 0) {
      if (lastReadedStart < start) {
        TGBeat previousBeat = getBeat(track, measure, lastReadedStart);
        if (previousBeat != null) {
          TGVoice previousVoice = previousBeat.getVoice(0);
          for (final TGNote previous : previousVoice.getNotes()) {
            TGNote note = new TGNoteImpl();
            note.setValue(previous.getValue());
            note.setString(previous.getString());
            note.setVelocity(previous.getVelocity());
            note.setTiedNote(true);

            voice.addNote(note);
          }
        }
      }
    } else if ((flags1 & 0x08) == 0) {
      int stringsFlags = readUnsignedByte();
      int effectsFlags = readUnsignedByte();
      int graceFlags = readUnsignedByte();

      for (int i = 5; i >= 0; i--) {
        if ((stringsFlags & (1 << i)) != 0) {
          TGNote note = new TGNoteImpl();

          int fret = readUnsignedByte();
          int dynamic = readUnsignedByte();
          if ((effectsFlags & (1 << i)) != 0) {
            readNoteEffects(effect);
          }
          note.setValue((fret >= 0 && fret < 100) ? fret : 0);
          note
              .setVelocity((TGVelocities.MIN_VELOCITY + (TGVelocities.VELOCITY_INCREMENT * dynamic))
                  - TGVelocities.VELOCITY_INCREMENT);
          note.setString(track.stringCount() - i);
          note.setEffect(effect.clone());
          note.getEffect().setDeadNote((fret < 0 || fret >= 100));

          voice.addNote(note);
        }

        // Grace note
        if ((graceFlags & (1 << i)) != 0) {
          readGraceNote();
        }
      }
    }

    beat.setStart(start);
    voice.setEmpty(false);
    voice.setDuration(duration.clone());
    measure.addBeat(beat);

    return duration.getTime();
  }

  private void readBeatEffects(TGNoteEffect effect) throws IOException {
    int flags = readUnsignedByte();
    effect.setVibrato((flags == 1 || flags == 2));
    effect.setFadeIn((flags == 4));
    effect.setTapping((flags == 5));
    effect.setSlapping((flags == 6));
    effect.setPopping((flags == 7));
    if (flags == 3) {
      readBend(effect);
    } else if (flags == 8 || flags == 9) {
      HarmonicEffect harmonic = null;
      if (flags == 8) {
        harmonic = HarmonicEffect.NATURAL;
      } else {
        harmonic = HarmonicEffect.ARTIFICIAL;
        harmonic.setData(0);
      }
      effect.setHarmonic(harmonic);
    }
  }

  private void readBend(TGNoteEffect effect) throws IOException {
    skip(6);
    float value = Math.max(((readUnsignedByte() / 8f) - 26f), 1f);
    BendingEffect bend = new BendingEffect();
    bend.addPoint(0, 0);
    bend.addPoint(Math.round(EffectPoint.MAX_POSITION_LENGTH / 2), Math
        .round(value * EffectPoint.SEMITONE_LENGTH));
    bend.addPoint(Math.round(EffectPoint.MAX_POSITION_LENGTH), Math.round(value
        * EffectPoint.SEMITONE_LENGTH));
    effect.setBend(bend);
    skip(1);
  }

  private void readChord(int strings, TGBeat beat) throws IOException {
    TGChord chord = new TGChordImpl(strings);
    chord.setName(readStringByte(0));

    this.skip(1);
    if (readInt() < 12) {
      skip(32);
    }

    chord.setFirstFret(readInt());
    if (chord.getFirstFret() != 0) {
      for (int i = 0; i < 6; i++) {
        int fret = readInt();
        if (i < chord.countStrings()) {
          chord.addFretValue(i, fret);
        }
      }
    }
    if (chord.countNotes() > 0) {
      beat.setChord(chord);
    }
  }

  private TGDuration readDuration() throws IOException {
    TGDuration duration = new TGDuration();
    duration.setValue((int) (Math.pow(2, (readByte() + 4)) / 4));
    return duration;
  }

  private void readGraceNote() throws IOException {
    byte bytes[] = new byte[3];
    read(bytes);
  }

  private void readInfo(TGSong song) throws IOException {
    song.setName(readStringByteSizeOfByte());
    song.setAuthor(readStringByteSizeOfByte());
    readStringByteSizeOfByte();
  }

  private void readMixChange(TGTempo tempo) throws IOException {
    int flags = readUnsignedByte();
    // Tempo
    if ((flags & 0x20) != 0) {
      tempo.setValue(readInt());
      readUnsignedByte();
    }
    // Reverb
    if ((flags & 0x10) != 0) {
      readUnsignedByte();
      readUnsignedByte();
    }
    // Chorus
    if ((flags & 0x08) != 0) {
      readUnsignedByte();
      readUnsignedByte();
    }
    // Balance
    if ((flags & 0x04) != 0) {
      readUnsignedByte();
      readUnsignedByte();
    }
    // Volume
    if ((flags & 0x02) != 0) {
      readUnsignedByte();
      readUnsignedByte();
    }
    // Instrument
    if ((flags & 0x01) != 0) {
      readUnsignedByte();
    }
  }

  private void readNoteEffects(TGNoteEffect effect) throws IOException {
    int flags = readUnsignedByte();
    effect.setHammer((flags == 1 || flags == 2));
    effect.setSlide((flags == 3 || flags == 4));
    if (flags == 5 || flags == 6) {
      readBend(effect);
    }
  }

  public TGSong readSong() throws GTPFormatException, IOException {
    readVersion();
    if (!isSupportedVersion(getVersion())) {
      this.close();
      throw new GTPFormatException("Unsupported Version");
    }
    TGSong song = new TGSong();

    readInfo(song);

    int tempo = readInt();
    int tripletFeel = ((readInt() == 1) ? TGMeasureHeader.TRIPLET_FEEL_EIGHTH
        : TGMeasureHeader.TRIPLET_FEEL_NONE);

    readInt(); // key

    for (int i = 0; i < TRACK_COUNT; i++) {
      TGTrack track = new TGTrackImpl();
      track.setNumber((i + 1));
      track.getChannel().setChannel(TRACK_CHANNELS[i][0]);
      track.getChannel().setEffectChannel(TRACK_CHANNELS[i][1]);
      track.setColor(Color.RED);

      int strings = readInt();
      for (int j = 0; j < strings; j++) {
        TGString string = new TGString();
        string.setNumber(j + 1);
        string.setValue(readInt());
        track.getStrings().add(string);
      }
      song.addTrack(track);
    }

    int measureCount = readInt();

    for (int i = 0; i < TRACK_COUNT; i++) {
      readTrack(song.getTrack(i));
    }

    skip(10);

    TGMeasureHeader previous = null;
    long[] lastReadedStarts = new long[TRACK_COUNT];
    for (int i = 0; i < measureCount; i++) {
      TGMeasureHeader header = new TGMeasureHeaderImpl();
      header.setStart((previous == null) ? TGDuration.QUARTER_TIME : (previous
          .getStart() + previous.getLength()));
      header.setNumber((previous == null) ? 1 : previous.getNumber() + 1);
      header.getTempo().setValue(
          (previous == null) ? tempo : previous.getTempo().getValue());
      header.setTripletFeel(tripletFeel);
      readTrackMeasures(song, header, lastReadedStarts);
      previous = header;
    }

    TGSongManager manager = new TGSongManager();
    manager.setSong(song);
    manager.autoCompleteSilences();

    this.close();

    return song;
  }

  private void readText(TGBeat beat) throws IOException {
    TGText text = new TGTextImpl();
    text.setValue(readStringByte(0));
    beat.setText(text);
  }

  private void readTimeSignature(TGTimeSignature timeSignature)
      throws IOException {
    timeSignature.setNumerator(readUnsignedByte());
    timeSignature.getDenominator().setValue(readUnsignedByte());
  }

  private void readTrack(TGTrack track) throws IOException {
    track.getChannel().setInstrument((short) readInt());
    readInt(); // Number of frets
    track.setName(readStringByteSizeOfByte());
    track.setSolo(readBoolean());
    track.getChannel().setVolume((short) readInt());
    track.getChannel().setBalance((short) readInt());
    track.getChannel().setChorus((short) readInt());
    track.getChannel().setReverb((short) readInt());
    track.setOffset(readInt());
  }

  private void readTrackMeasures(TGSong song, TGMeasureHeader header,
      long[] lastReadedStarts) throws IOException {
    readTimeSignature(header.getTimeSignature());

    skip(6);

    int[] beats = new int[TRACK_COUNT];
    for (int i = 0; i < TRACK_COUNT; i++) {
      readUnsignedByte();
      readUnsignedByte();
      beats[i] = readUnsignedByte();
      if (beats[i] > 127) {
        beats[i] = 0;
      }
      skip(9);
    }
    skip(2);

    int flags = readUnsignedByte();

    header.setRepeatOpen(((flags & 0x01) != 0));
    if ((flags & 0x02) != 0) {
      header.setRepeatClose(readUnsignedByte());
    }

    if ((flags & 0x04) != 0) {
      header.setRepeatAlternative(parseRepeatAlternative(song, header
          .getNumber(), readUnsignedByte()));
    }

    song.addMeasureHeader(header);
    for (int i = 0; i < TRACK_COUNT; i++) {
      TGTrack track = song.getTrack(i);
      TGMeasure measure = new TGMeasureImpl(header);

      long start = measure.getStart();
      for (int j = 0; j < beats[i]; j++) {
        long length = readBeat(track, measure, start, lastReadedStarts[i]);
        lastReadedStarts[i] = start;
        start += length;
      }
      measure.setClef(getClef(track));
      track.addMeasure(measure);
    }
  }
}