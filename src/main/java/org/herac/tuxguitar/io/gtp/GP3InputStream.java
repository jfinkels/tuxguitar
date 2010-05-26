package org.herac.tuxguitar.io.gtp;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTextImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.StrokeDirection;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMarker;
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
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class GP3InputStream extends GTPInputStream {
  private static final float GP_BEND_POSITION = 60f;
  private static final float GP_BEND_SEMITONE = 25f;
  private static final String SUPPORTED_VERSIONS[] = new String[] { "FICHIER GUITAR PRO v3.00" };

  private int tripletFeel;

  public GP3InputStream(GTPSettings settings) {
    super(settings, SUPPORTED_VERSIONS);
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
    return new TGFileFormat("Guitar Pro 3", "*.gp3");
  }

  private int getTiedNoteValue(int string, TGTrack track) {
    int measureCount = track.countMeasures();
    if (measureCount > 0) {
      for (final TGMeasure measure : track.getMeasures()) {
        for (final TGBeat beat : measure.getBeats()) {
          for (final TGNote note : beat.getVoice(0).getNotes()) {
            if (note.getString() == string) {
              return note.getValue();
            }
          }
        }
      }
    }
    return -1;
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

  private long readBeat(long start, TGMeasure measure, TGTrack track,
      TGTempo tempo) throws IOException {
    int flags = readUnsignedByte();
    if ((flags & 0x40) != 0) {
      readUnsignedByte();
    }

    TGBeat beat = new TGBeatImpl();
    TGVoice voice = beat.getVoice(0);
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
    }
    beat.setStart(start);
    voice.setEmpty(false);
    voice.setDuration(duration.clone());
    measure.addBeat(beat);

    return duration.getTime();
  }

  private void readBeatEffects(TGBeat beat, TGNoteEffect effect)
      throws IOException {
    int flags = readUnsignedByte();
    effect.setVibrato(((flags & 0x01) != 0) || ((flags & 0x02) != 0));
    effect.setFadeIn(((flags & 0x10) != 0));
    if ((flags & 0x20) != 0) {
      int type = readUnsignedByte();
      if (type == 0) {
        readTremoloBar(effect);
      } else {
        effect.setTapping(type == 1);
        effect.setSlapping(type == 2);
        effect.setPopping(type == 3);
        readInt();
      }
    }
    if ((flags & 0x40) != 0) {
      int strokeDown = readByte();
      int strokeUp = readByte();
      if (strokeDown > 0) {
        beat.getStroke().setDirection(StrokeDirection.DOWN);
        beat.getStroke().setValue(toStrokeValue(strokeDown));
      } else if (strokeUp > 0) {
        beat.getStroke().setDirection(StrokeDirection.UP);
        beat.getStroke().setValue(toStrokeValue(strokeUp));
      }
    }
    if ((flags & 0x04) != 0) {
      effect.setHarmonic(HarmonicEffect.NATURAL);
    }
    if ((flags & 0x08) != 0) {
      HarmonicEffect harmonic = HarmonicEffect.ARTIFICIAL;
      harmonic.setData(0);
      effect.setHarmonic(harmonic);
    }
  }

  private void readBend(TGNoteEffect effect) throws IOException {
    BendingEffect bend = new BendingEffect();
    skip(5);
    int points = readInt();
    for (int i = 0; i < points; i++) {
      int bendPosition = readInt();
      int bendValue = readInt();
      readByte(); // vibrato

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
    int header = readUnsignedByte();
    if ((header & 0x01) == 0) {
      chord.setName(readStringByteSizeOfInteger());
      chord.setFirstFret(readInt());
      if (chord.getFirstFret() != 0) {
        for (int i = 0; i < 6; i++) {
          int fret = readInt();
          if (i < chord.countStrings()) {
            chord.addFretValue(i, fret);
          }
        }
      }
    } else {
      skip(25);
      chord.setName(readStringByte(34));
      chord.setFirstFret(readInt());
      for (int i = 0; i < 6; i++) {
        int fret = readInt();
        if (i < chord.countStrings()) {
          chord.addFretValue(i, fret);
        }
      }
      skip(36);
    }
    if (chord.countNotes() > 0) {
      beat.setChord(chord);
    }
  }

  private Color readColor() throws IOException {
    final int red = readUnsignedByte();
    final int green = readUnsignedByte();
    final int blue = readUnsignedByte();

    read();
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
    TGEffectGrace grace = new TGEffectGrace();
    grace.setOnBeat(false);
    grace.setDead((fret == 255));
    grace.setFret(((!grace.isDead()) ? fret : 0));
    grace
        .setDynamic((TGVelocities.MIN_VELOCITY + (TGVelocities.VELOCITY_INCREMENT * readUnsignedByte()))
            - TGVelocities.VELOCITY_INCREMENT);
    int transition = readUnsignedByte();
    if (transition == 0) {
      grace.setTransition(TGEffectGrace.TRANSITION_NONE);
    } else if (transition == 1) {
      grace.setTransition(TGEffectGrace.TRANSITION_SLIDE);
    } else if (transition == 2) {
      grace.setTransition(TGEffectGrace.TRANSITION_BEND);
    } else if (transition == 3) {
      grace.setTransition(TGEffectGrace.TRANSITION_HAMMER);
    }
    grace.setDuration(readUnsignedByte());
    effect.setGrace(grace);
  }

  private void readInfo(TGSong song) throws IOException {
    song.setName(readStringByteSizeOfInteger());
    readStringByteSizeOfInteger();
    song.setArtist(readStringByteSizeOfInteger());
    song.setAlbum(readStringByteSizeOfInteger());
    song.setAuthor(readStringByteSizeOfInteger());
    song.setCopyright(readStringByteSizeOfInteger());
    song.setWriter(readStringByteSizeOfInteger());
    readStringByteSizeOfInteger();
    int comments = readInt();
    for (int i = 0; i < comments; i++) {
      song.setComments(song.getComments() + readStringByteSizeOfInteger());
    }
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
    long nextNoteStart = measure.getStart();
    int numberOfBeats = readInt();
    for (int i = 0; i < numberOfBeats; i++) {
      nextNoteStart += readBeat(nextNoteStart, measure, track, tempo);
    }
    measure.setClef(getClef(track));
  }

  private TGMeasureHeader readMeasureHeader(int number, TGSong song,
      TGTimeSignature timeSignature) throws IOException {
    int flags = readUnsignedByte();
    TGMeasureHeader header = new TGMeasureHeaderImpl();
    header.setNumber(number);
    header.setStart(0);
    header.getTempo().setValue(120);
    header.setTripletFeel(this.tripletFeel);
    header.setRepeatOpen(((flags & 0x04) != 0));

    if ((flags & 0x01) != 0) {
      timeSignature.setNumerator(readByte());
    }
    if ((flags & 0x02) != 0) {
      timeSignature.getDenominator().setValue(readByte());
    }
    header.setTimeSignature(timeSignature.clone());
    if ((flags & 0x08) != 0) {
      header.setRepeatClose(readByte());
    }
    if ((flags & 0x10) != 0) {
      header.setRepeatAlternative(parseRepeatAlternative(song, number,
          readUnsignedByte()));
    }
    if ((flags & 0x20) != 0) {
      header.setMarker(readMarker(number));
    }
    if ((flags & 0x40) != 0) {
      readByte();
      readByte();
    }
    return header;
  }

  private void readMeasureHeaders(TGSong song, int count) throws IOException {
    TGTimeSignature timeSignature = new TGTimeSignature();
    for (int i = 0; i < count; i++) {
      song.addMeasureHeader(readMeasureHeader((i + 1), song, timeSignature));
    }
  }

  private void readMeasures(TGSong song, int measures, int tracks,
      int tempoValue) throws IOException {
    TGTempo tempo = new TGTempo();
    tempo.setValue(tempoValue);
    long start = TGDuration.QUARTER_TIME;
    for (final TGMeasureHeader header : song.getMeasureHeaders()) {
      header.setStart(start);
      for (final TGTrack track : song.getTracks()) {
        TGMeasure measure = new TGMeasureImpl(header);
        track.addMeasure(measure);
        readMeasure(measure, track, tempo);
      }
      tempo.copy(header.getTempo());
      start += header.getLength();
    }
  }

  private void readMixChange(TGTempo tempo) throws IOException {
    readByte(); // instrument
    int volume = readByte();
    int pan = readByte();
    int chorus = readByte();
    int reverb = readByte();
    int phaser = readByte();
    int tremolo = readByte();
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
      readByte();
    }
  }

  private TGNote readNote(TGString string, TGTrack track, TGNoteEffect effect)
      throws IOException {
    int flags = readUnsignedByte();
    TGNote note = new TGNoteImpl();
    note.setString(string.getNumber());
    note.setEffect(effect);
    note.getEffect().setGhostNote(((flags & 0x04) != 0));
    if ((flags & 0x20) != 0) {
      int noteType = readUnsignedByte();
      note.setTiedNote((noteType == 0x02));
      note.getEffect().setDeadNote((noteType == 0x03));
    }
    if ((flags & 0x01) != 0) {
      skip(2);
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
    if ((flags & 0x08) != 0) {
      readNoteEffects(note.getEffect());
    }
    return note;
  }

  private void readNoteEffects(TGNoteEffect effect) throws IOException {
    int flags = readUnsignedByte();
    effect.setHammer(((flags & 0x02) != 0));
    effect.setSlide(((flags & 0x04) != 0));
    effect.setLetRing(((flags & 0x08) != 0));
    if ((flags & 0x01) != 0) {
      readBend(effect);
    }
    if ((flags & 0x10) != 0) {
      readGrace(effect);
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

    this.tripletFeel = ((readBoolean()) ? TGMeasureHeader.TRIPLET_FEEL_EIGHTH
        : TGMeasureHeader.TRIPLET_FEEL_NONE);

    int tempoValue = readInt();

    readInt(); // key

    List<TGChannel> channels = readChannels();

    int measures = readInt();
    int tracks = readInt();

    readMeasureHeaders(song, measures);
    readTracks(song, tracks, channels);
    readMeasures(song, measures, tracks, tempoValue);

    this.close();

    return song;
  }

  private void readText(TGBeat beat) throws IOException {
    TGText text = new TGTextImpl();
    text.setValue(readStringByteSizeOfInteger());
    beat.setText(text);
  }

  private TGTrack readTrack(int number, List<TGChannel> channels)
      throws IOException {
    TGTrack track = new TGTrackImpl();
    track.setNumber(number);
    readUnsignedByte();
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
    return track;
  }

  private void readTracks(TGSong song, int count, List<TGChannel> channels)
      throws IOException {
    for (int number = 1; number <= count; number++) {
      song.addTrack(readTrack(number, channels));
    }
  }

  private void readTremoloBar(TGNoteEffect noteEffect) throws IOException {
    int value = readInt();
    BendingEffect effect = new BendingEffect();
    effect.addPoint(0, 0);
    effect.addPoint(Math.round(EffectPoint.MAX_POSITION_LENGTH / 2f), Math
        .round(-(value / (GP_BEND_SEMITONE * 2f))));
    effect.addPoint(EffectPoint.MAX_POSITION_LENGTH, 0);
    noteEffect.setTremoloBar(effect);
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