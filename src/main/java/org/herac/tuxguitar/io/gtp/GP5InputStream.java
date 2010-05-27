package org.herac.tuxguitar.io.gtp;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;
import org.herac.tuxguitar.gui.editors.tab.TGLyricImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTextImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.Direction;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGStroke;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVelocities;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;
import org.herac.tuxguitar.song.models.effects.EffectPoint;
import org.herac.tuxguitar.song.models.effects.HarmonicEffect;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;
import org.herac.tuxguitar.song.models.effects.Transition;

public class GP5InputStream extends GTPInputStream {
  private static final float GP_BEND_POSITION = 60f;
  private static final float GP_BEND_SEMITONE = 25f;
  private static final String supportedVersions[] = {
      "FICHIER GUITAR PRO v5.00", "FICHIER GUITAR PRO v5.10" };

  public GP5InputStream(GTPSettings settings) {
    super(settings, supportedVersions);
  }

  private TGBeat getBeat(TGMeasure measure, long start) {
    int count = measure.countBeats();
    for (int i = 0; i < count; i++) {
      TGBeat beat = measure.getBeat(i);
      if (beat.getStart() == start) {
        return beat;
      }
    }
    TGBeat beat = new TGBeatImpl();
    beat.setStart(start);
    measure.addBeat(beat);
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
    return new TGFileFormat("Guitar Pro 5", "*.gp5");
  }

  private int getTiedNoteValue(int string, TGTrack track) {
    int measureCount = track.countMeasures();
    if (measureCount > 0) {
      for (int m = measureCount - 1; m >= 0; m--) {
        TGMeasure measure = track.getMeasure(m);
        for (int b = measure.countBeats() - 1; b >= 0; b--) {
          TGBeat beat = measure.getBeat(b);
          for (int v = 0; v < beat.countVoices(); v++) {
            TGVoice voice = beat.getVoice(v);
            if (!voice.isEmpty()) {
              for (int n = 0; n < voice.getNotes().size(); n++) {
                TGNote note = voice.getNote(n);
                if (note.getString() == string) {
                  return note.getValue();
                }
              }
            }
          }
        }
      }
    }
    return -1;
  }

  private void readArtificialHarmonic(TGNoteEffect effect) throws IOException {
    int type = readByte();

    HarmonicEffect harmonic = null;

    switch (type) {
    case 1:
      harmonic = HarmonicEffect.NATURAL;
      break;
    case 2:
      skip(3);
      harmonic = HarmonicEffect.ARTIFICIAL;
      break;
    case 3:
      skip(1);
      harmonic = HarmonicEffect.TAPPED;
      break;
    case 4:
      harmonic = HarmonicEffect.PINCH;
      break;
    case 5:
      harmonic = HarmonicEffect.SEMI;
      break;
    }

    harmonic.setData(0);
    effect.setHarmonic(harmonic);
  }

  private long readBeat(long start, TGMeasure measure, TGTrack track,
      TGTempo tempo, int voiceIndex) throws IOException {
    int flags = readUnsignedByte();

    TGBeat beat = getBeat(measure, start);
    TGVoice voice = beat.getVoice(voiceIndex);
    if ((flags & 0x40) != 0) {
      int beatType = readUnsignedByte();
      voice.setEmpty((beatType & 0x02) == 0);
    }
    TGDuration duration = readDuration(flags);
    TGNoteEffect effect = new TGNoteEffect();
    if ((flags & 0x02) != 0) {
      readChord(track.stringCount(), beat);
    }
    if ((flags & 0x04) != 0) {
      readText(beat);
    }
    if ((flags & 0x08) != 0) {
      readBeatEffects(beat, effect);
    }
    if ((flags & 0x10) != 0) {
      readMixChange(tempo);
    }
    int stringFlags = readUnsignedByte();
    for (int i = 6; i >= 0; i--) {
      if ((stringFlags & (1 << i)) != 0 && (6 - i) < track.stringCount()) {
        TGString string = track.getString((6 - i) + 1).clone();
        TGNote note = readNote(string, track, effect.clone());
        voice.addNote(note);
      }
      voice.setDuration(duration.clone());
    }

    skip(1);

    int read = readByte();
    // if (read == 8 || read == 10 || read == 24 ) {
    if ((read & 0x08) != 0) {
      skip(1);
    }

    return (!voice.isEmpty() ? duration.getTime() : 0);
  }

  private void readBeatEffects(TGBeat beat, TGNoteEffect noteEffect)
      throws IOException {
    int flags1 = readUnsignedByte();
    int flags2 = readUnsignedByte();
    noteEffect.setFadeIn(((flags1 & 0x10) != 0));
    noteEffect.setVibrato(((flags1 & 0x02) != 0));
    if ((flags1 & 0x20) != 0) {
      int effect = readUnsignedByte();
      noteEffect.setTapping(effect == 1);
      noteEffect.setSlapping(effect == 2);
      noteEffect.setPopping(effect == 3);
    }
    if ((flags2 & 0x04) != 0) {
      readTremoloBar(noteEffect);
    }
    if ((flags1 & 0x40) != 0) {
      int strokeUp = readByte();
      int strokeDown = readByte();
      if (strokeUp > 0) {
        beat.setStroke(new TGStroke(Direction.UP, toStrokeValue(strokeUp)));
      } else if (strokeDown > 0) {
        beat.setStroke(new TGStroke(Direction.DOWN, toStrokeValue(strokeDown)));
      }
    }
    if ((flags2 & 0x02) != 0) {
      readByte();
    }
  }

  private void readBend(TGNoteEffect effect) throws IOException {
    skip(5);
    BendingEffect bend = new BendingEffect();
    int numPoints = readInt();
    for (int i = 0; i < numPoints; i++) {
      int bendPosition = readInt();
      int bendValue = readInt();
      readByte();

      int pointPosition = Math.round(bendPosition
          * EffectPoint.MAX_POSITION_LENGTH / GP_BEND_POSITION);
      int pointValue = Math.round(bendValue * EffectPoint.SEMITONE_LENGTH
          / GP_BEND_SEMITONE);
      bend.addPoint(pointPosition, pointValue);
    }
    if (!bend.getPoints().isEmpty()) {
      effect.setBend(bend);
    }
  }

  private TGChannel readChannel(List<TGChannel> channels) throws IOException {
    TGChannel result = null;

    int index = (readInt() - 1);
    int effectChannel = (readInt() - 1);
    if (index >= 0 && index < channels.size()) {
      result = channels.get(index).clone();
      if (result.getInstrument() < 0) {
        result.setInstrument((short) 0);
      }
      if (!result.isPercussionChannel()) {
        result.setEffectChannel((short) effectChannel);
      }
    }

    return result;
  }

  private List<TGChannel> readChannels() throws IOException {
    List<TGChannel> channels = new ArrayList<TGChannel>();
    for (int i = 0; i < 64; i++) {
      TGChannel channel = new TGChannel();
      channel.setChannel((short) i);
      channel.setEffectChannel((short) i);
      channel.setInstrument((short) readInt());
      channel.setVolume(toChannelShort(readByte()));
      channel.setBalance(toChannelShort(readByte()));
      channel.setChorus(toChannelShort(readByte()));
      channel.setReverb(toChannelShort(readByte()));
      channel.setPhaser(toChannelShort(readByte()));
      channel.setTremolo(toChannelShort(readByte()));
      channels.add(channel);
      skip(2);
    }
    return channels;
  }

  private void readChord(int strings, TGBeat beat) throws IOException {
    TGChord chord = new TGChordImpl(strings);
    this.skip(17);
    chord.setName(readStringByte(21));
    this.skip(4);
    chord.setFirstFret(readInt());
    for (int i = 0; i < 7; i++) {
      int fret = readInt();
      if (i < chord.countStrings()) {
        chord.addFretValue(i, fret);
      }
    }
    this.skip(32);
    if (chord.countNotes() > 0) {
      beat.setChord(chord);
    }
  }

  private Color readColor() throws IOException {
    final int red = readUnsignedByte();
    final int green = readUnsignedByte();
    final int blue = readUnsignedByte();
    skip(1);
    return new Color(red, green, blue);
  }

  private TGDuration readDuration(int flags) throws IOException {
    TGDuration duration = new TGDuration();
    duration.setValue((int) (Math.pow(2, (readByte() + 4)) / 4));
    duration.setDotted(((flags & 0x01) != 0));
    if ((flags & 0x20) != 0) {
      int divisionType = readInt();
      switch (divisionType) {
      case 3:
        duration.setDivision(TGDivisionType.DEFAULT);
        break;
      case 5:
        duration.setDivision(new TGDivisionType(5, 4));
        break;
      case 6:
        duration.setDivision(new TGDivisionType(6, 4));
        break;
      case 7:
        duration.setDivision(new TGDivisionType(7, 4));
        break;
      case 9:
        duration.setDivision(new TGDivisionType(9, 8));
        break;
      case 10:
        duration.setDivision(new TGDivisionType(10, 8));
        break;
      case 11:
        duration.setDivision(new TGDivisionType(11, 8));
        break;
      case 12:
        duration.setDivision(new TGDivisionType(12, 8));
        break;
      }
    }
    return duration;
  }

  private void readGrace(TGNoteEffect effect) throws IOException {
    int fret = readUnsignedByte();
    int dynamic = readUnsignedByte();
    int transition = readByte();
    int duration = readUnsignedByte();
    int flags = readUnsignedByte();
    TGEffectGrace grace = new TGEffectGrace();
    grace.setFret(fret);
    grace
        .setDynamic((TGVelocities.MIN_VELOCITY + (TGVelocities.VELOCITY_INCREMENT * dynamic))
            - TGVelocities.VELOCITY_INCREMENT);
    grace.setDuration(duration);
    grace.setDead((flags & 0x01) != 0);
    grace.setOnBeat((flags & 0x02) != 0);
    if (transition == 0) {
      grace.setTransition(Transition.NONE);
    } else if (transition == 1) {
      grace.setTransition(Transition.SLIDE);
    } else if (transition == 2) {
      grace.setTransition(Transition.BEND);
    } else if (transition == 3) {
      grace.setTransition(Transition.HAMMER);
    }
    effect.setGrace(grace);
  }

  private void readInfo(TGSong song) throws IOException {
    song.setName(readStringByteSizeOfInteger());
    readStringByteSizeOfInteger();
    song.setArtist(readStringByteSizeOfInteger());
    song.setAlbum(readStringByteSizeOfInteger());
    song.setAuthor(readStringByteSizeOfInteger());
    readStringByteSizeOfInteger();
    song.setCopyright(readStringByteSizeOfInteger());
    song.setWriter(readStringByteSizeOfInteger());
    readStringByteSizeOfInteger();
    int comments = readInt();
    for (int i = 0; i < comments; i++) {
      song.setComments(song.getComments() + readStringByteSizeOfInteger());
    }
  }

  private TGLyric readLyrics() throws IOException {
    TGLyric lyric = new TGLyricImpl();
    lyric.setFrom(readInt());
    lyric.setLyrics(readStringInteger());
    for (int i = 0; i < 4; i++) {
      readInt();
      readStringInteger();
    }
    return lyric;
  }

  private TGMarker readMarker(int measure) throws IOException {
    TGMarker marker = new TGMarker();
    marker.setMeasure(measure);
    marker.setTitle(readStringByteSizeOfInteger());
    marker.setColor(readColor());
    return marker;
  }

  private void readMeasure(TGMeasure measure, TGTrack track, TGTempo tempo)
      throws IOException {
    for (int voice = 0; voice < 2; voice++) {
      long start = measure.getStart();
      int beats = readInt();
      for (int i = 0; i < beats; i++) {
        start += readBeat(start, measure, track, tempo, voice);
      }
    }

    List<TGBeat> emptyBeats = new ArrayList<TGBeat>();
    for (int i = 0; i < measure.countBeats(); i++) {
      TGBeat beat = measure.getBeat(i);
      boolean empty = true;
      for (int v = 0; v < beat.countVoices(); v++) {
        if (!beat.getVoice(v).isEmpty()) {
          empty = false;
        }
      }
      if (empty) {
        emptyBeats.add(beat);
      }
    }

    for (final TGBeat beat : emptyBeats) {
      measure.removeBeat(beat);
    }
    measure.setClef(getClef(track));
  }

  private TGMeasureHeader readMeasureHeader(int index,
      TGTimeSignature timeSignature) throws IOException {
    int flags = readUnsignedByte();
    TGMeasureHeader header = new TGMeasureHeaderImpl();
    header.setNumber((index + 1));
    header.setStart(0);
    header.getTempo().setValue(120);
    header.setRepeatOpen(((flags & 0x04) != 0));
    if ((flags & 0x01) != 0) {
      timeSignature.setNumerator(readByte());
    }
    if ((flags & 0x02) != 0) {
      timeSignature.getDenominator().setValue(readByte());
    }
    header.setTimeSignature(timeSignature.clone());
    if ((flags & 0x08) != 0) {
      header.setRepeatClose(((readByte() & 0xff) - 1));
    }
    if ((flags & 0x20) != 0) {
      header.setMarker(readMarker(header.getNumber()));
    }
    if ((flags & 0x10) != 0) {
      header.setRepeatAlternative(readUnsignedByte());
    }
    if ((flags & 0x40) != 0) {
      readByte();
      readByte();
    }
    if ((flags & 0x01) != 0 || (flags & 0x02) != 0) {
      skip(4);
    }
    if ((flags & 0x10) == 0) {
      skip(1);
    }
    int tripletFeel = readByte();
    if (tripletFeel == 1) {
      header.setTripletFeel(TGMeasureHeader.TRIPLET_FEEL_EIGHTH);
    } else if (tripletFeel == 2) {
      header.setTripletFeel(TGMeasureHeader.TRIPLET_FEEL_SIXTEENTH);
    } else {
      header.setTripletFeel(TGMeasureHeader.TRIPLET_FEEL_NONE);
    }
    return header;
  }

  private void readMeasureHeaders(TGSong song, int count) throws IOException {
    TGTimeSignature timeSignature = new TGTimeSignature();
    for (int i = 0; i < count; i++) {
      if (i > 0) {
        skip(1);
      }
      song.addMeasureHeader(readMeasureHeader(i, timeSignature));
    }
  }

  private void readMeasures(TGSong song, int measures, int tracks,
      int tempoValue) throws IOException {
    TGTempo tempo = new TGTempo();
    tempo.setValue(tempoValue);
    long start = TGDuration.QUARTER_TIME;
    for (int i = 0; i < measures; i++) {
      TGMeasureHeader header = song.getMeasureHeader(i);
      header.setStart(start);
      for (int j = 0; j < tracks; j++) {
        TGTrack track = song.getTrack(j);
        TGMeasure measure = new TGMeasureImpl(header);
        track.addMeasure(measure);
        readMeasure(measure, track, tempo);
        skip(1);
      }
      header.setTempo(tempo.clone());
      start += header.getLength();
    }
  }

  private void readMixChange(TGTempo tempo) throws IOException {
    readByte(); // instrument

    skip(16);
    int volume = readByte();
    int pan = readByte();
    int chorus = readByte();
    int reverb = readByte();
    int phaser = readByte();
    int tremolo = readByte();
    readStringByteSizeOfInteger(); // tempoName
    int tempoValue = readInt();
    if (volume >= 0) {
      readByte();
    }
    if (pan >= 0) {
      readByte();
    }
    if (chorus >= 0) {
      readByte();
    }
    if (reverb >= 0) {
      readByte();
    }
    if (phaser >= 0) {
      readByte();
    }
    if (tremolo >= 0) {
      readByte();
    }
    if (tempoValue >= 0) {
      tempo.setValue(tempoValue);
      skip(1);
      if (getVersionIndex() > 0) {
        skip(1);
      }
    }
    readByte();
    skip(1);
    if (getVersionIndex() > 0) {
      readStringByteSizeOfInteger();
      readStringByteSizeOfInteger();
    }
  }

  private TGNote readNote(TGString string, TGTrack track, TGNoteEffect effect)
      throws IOException {
    int flags = readUnsignedByte();
    TGNote note = new TGNoteImpl();
    note.setString(string.getNumber());
    note.setEffect(effect);
    note.getEffect().setAccentuatedNote(((flags & 0x40) != 0));
    note.getEffect().setHeavyAccentuatedNote(((flags & 0x02) != 0));
    note.getEffect().setGhostNote(((flags & 0x04) != 0));
    if ((flags & 0x20) != 0) {
      int noteType = readUnsignedByte();
      note.setTiedNote((noteType == 0x02));
      note.getEffect().setDeadNote((noteType == 0x03));
    }
    if ((flags & 0x10) != 0) {
      note
          .setVelocity((TGVelocities.MIN_VELOCITY + (TGVelocities.VELOCITY_INCREMENT * readByte()))
              - TGVelocities.VELOCITY_INCREMENT);
    }
    if ((flags & 0x20) != 0) {
      int fret = readByte();
      int value = (note.isTiedNote() ? getTiedNoteValue(string.getNumber(),
          track) : fret);
      note.setValue(value >= 0 && value < 100 ? value : 0);
    }
    if ((flags & 0x80) != 0) {
      skip(2);
    }
    if ((flags & 0x01) != 0) {
      skip(8);
    }
    skip(1);
    if ((flags & 0x08) != 0) {
      readNoteEffects(note.getEffect());
    }
    return note;
  }

  private void readNoteEffects(TGNoteEffect noteEffect) throws IOException {
    int flags1 = readUnsignedByte();
    int flags2 = readUnsignedByte();
    if ((flags1 & 0x01) != 0) {
      readBend(noteEffect);
    }
    if ((flags1 & 0x10) != 0) {
      readGrace(noteEffect);
    }
    if ((flags2 & 0x04) != 0) {
      readTremoloPicking(noteEffect);
    }
    if ((flags2 & 0x08) != 0) {
      noteEffect.setSlide(true);
      readByte();
    }
    if ((flags2 & 0x10) != 0) {
      readArtificialHarmonic(noteEffect);
    }
    if ((flags2 & 0x20) != 0) {
      readTrill(noteEffect);
    }
    noteEffect.setHammer(((flags1 & 0x02) != 0));
    noteEffect.setLetRing(((flags1 & 0x08) != 0));
    noteEffect.setVibrato(((flags2 & 0x40) != 0) || noteEffect.isVibrato());
    noteEffect.setPalmMute(((flags2 & 0x02) != 0));
    noteEffect.setStaccato(((flags2 & 0x01) != 0));
  }

  private void readPageSetup() throws IOException {
    skip((getVersionIndex() > 0 ? 49 : 30));
    for (int i = 0; i < 11; i++) {
      skip(4);
      readStringByte(0);
    }
  }

  public TGSong readSong() throws IOException, GTPFormatException {
    readVersion();
    if (!isSupportedVersion(getVersion())) {
      this.close();
      throw new GTPFormatException("Unsupported Version");
    }
    TGSong song = new TGSong();

    readInfo(song);

    // lyrics
    int lyricTrack = readInt();
    TGLyric lyric = readLyrics();

    readPageSetup();

    int tempoValue = readInt();

    if (getVersionIndex() > 0) {
      skip(1);
    }

    readInt(); // key

    readByte(); // octave

    List<TGChannel> channels = readChannels();

    skip(42);

    int measures = readInt();
    int tracks = readInt();

    readMeasureHeaders(song, measures);
    readTracks(song, tracks, channels, lyric, lyricTrack);
    readMeasures(song, measures, tracks, tempoValue);

    this.close();

    return song;
  }

  private void readText(TGBeat beat) throws IOException {
    TGText text = new TGTextImpl();
    text.setValue(readStringByteSizeOfInteger());
    beat.setText(text);
  }

  private TGTrack readTrack(int number, List<TGChannel> channels, TGLyric lyrics)
      throws IOException {
    readUnsignedByte();
    if (number == 1 || getVersionIndex() == 0) {
      skip(1);
    }
    TGTrack track = new TGTrackImpl();
    track.setNumber(number);
    track.setLyrics(lyrics);
    track.setName(readStringByte(40));
    int stringCount = readInt();
    for (int i = 0; i < 7; i++) {
      int tuning = readInt();
      if (stringCount > i) {
        track.getStrings().add(new TGString(i + 1, tuning));
      }
    }
    readInt();
    final TGChannel newChannel = this.readChannel(channels);
    if (newChannel != null) {
      track.setChannel(newChannel);
    }
    // readChannel(track.getChannel(), channels);
    readInt();
    track.setOffset(readInt());
    track.setColor(readColor());
    skip((getVersionIndex() > 0) ? 49 : 44);
    if (getVersionIndex() > 0) {
      readStringByteSizeOfInteger();
      readStringByteSizeOfInteger();
    }
    return track;
  }

  private void readTracks(TGSong song, int count, List<TGChannel> channels,
      TGLyric lyric, int lyricTrack) throws IOException {
    for (int number = 1; number <= count; number++) {
      song.addTrack(readTrack(number, channels, (number == lyricTrack) ? lyric
          : new TGLyricImpl()));
    }
    skip((getVersionIndex() == 0 ? 2 : 1));
  }

  private void readTremoloBar(TGNoteEffect effect) throws IOException {
    skip(5);
    BendingEffect tremoloBar = new BendingEffect();
    int numPoints = readInt();
    for (int i = 0; i < numPoints; i++) {
      int position = readInt();
      int value = readInt();
      readByte();

      int pointPosition = Math.round(position * EffectPoint.MAX_POSITION_LENGTH
          / GP_BEND_POSITION);
      int pointValue = Math.round(value / (GP_BEND_SEMITONE * 2f));
      tremoloBar.addPoint(pointPosition, pointValue);
    }
    if (!tremoloBar.getPoints().isEmpty()) {
      effect.setTremoloBar(tremoloBar);
    }
  }

  public void readTremoloPicking(TGNoteEffect effect) throws IOException {
    int value = readUnsignedByte();
    TGEffectTremoloPicking tp = new TGEffectTremoloPicking();
    if (value == 1) {
      tp.getDuration().setValue(TGDuration.EIGHTH);
      effect.setTremoloPicking(tp);
    } else if (value == 2) {
      tp.getDuration().setValue(TGDuration.SIXTEENTH);
      effect.setTremoloPicking(tp);
    } else if (value == 3) {
      tp.getDuration().setValue(TGDuration.THIRTY_SECOND);
      effect.setTremoloPicking(tp);
    }
  }

  private void readTrill(TGNoteEffect effect) throws IOException {
    byte fret = readByte();
    byte period = readByte();
    TGEffectTrill trill = new TGEffectTrill();
    trill.setFret(fret);
    if (period == 1) {
      trill.getDuration().setValue(TGDuration.SIXTEENTH);
      effect.setTrill(trill);
    } else if (period == 2) {
      trill.getDuration().setValue(TGDuration.THIRTY_SECOND);
      effect.setTrill(trill);
    } else if (period == 3) {
      trill.getDuration().setValue(TGDuration.SIXTY_FOURTH);
      effect.setTrill(trill);
    }
  }

  private short toChannelShort(byte b) {
    short value = (short) ((b * 8) - 1);
    return (short) Math.max(value, 0);
  }

  private int toStrokeValue(int value) {
    if (value == 1 || value == 2) {
      return TGDuration.SIXTY_FOURTH;
    }
    if (value == 3) {
      return TGDuration.THIRTY_SECOND;
    }
    if (value == 4) {
      return TGDuration.SIXTEENTH;
    }
    if (value == 5) {
      return TGDuration.EIGHTH;
    }
    if (value == 6) {
      return TGDuration.QUARTER;
    }
    return TGDuration.SIXTY_FOURTH;
  }
}
