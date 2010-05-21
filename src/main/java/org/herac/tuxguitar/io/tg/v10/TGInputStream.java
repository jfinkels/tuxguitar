/*
 * Created on 16-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.io.tg.v10;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTextImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGInputStreamBase;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.TGBeat;
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
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;
import org.herac.tuxguitar.song.models.effects.EffectPoint;
import org.herac.tuxguitar.song.models.effects.HarmonicEffect;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGInputStream extends TGStream implements TGInputStreamBase {

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(TGInputStream.class);

  private DataInputStream dataInputStream;
  // private TGFactory factory;
  private String version;

  public TGFileFormat getFileFormat() {
    return new TGFileFormat("TuxGuitar", "*.tg");
  }

  public void init(InputStream stream) {
    // = factory;
    this.dataInputStream = new DataInputStream(stream);
    this.version = null;
  }

  public boolean isSupportedVersion() {
    try {
      readVersion();
      return isSupportedVersion(this.version);
    } catch (Throwable throwable) {
      return false;
    }
  }

  public boolean isSupportedVersion(String version) {
    return (version.equals(TG_FORMAT_VERSION));
  }

  private TGSong read() {
    TGSong song = new TGSong();

    // leo el nombre
    song.setName(readUnsignedByteString());

    // leo el artista
    song.setArtist(readUnsignedByteString());

    // leo el album
    song.setAlbum(readUnsignedByteString());

    // leo el autor
    song.setAuthor(readUnsignedByteString());

    // leo la cantidad de measure headers
    int headerCount = readShort();

    // leo las pistas
    TGMeasureHeader lastHeader = null;
    long headerStart = TGDuration.QUARTER_TIME;
    for (int i = 0; i < headerCount; i++) {
      TGMeasureHeader header = readMeasureHeader(i + 1, headerStart, lastHeader);
      song.addMeasureHeader(header);
      headerStart += header.getLength();
      lastHeader = header;
    }

    // leo la cantidad de pistas
    int trackCount = readByte();

    // leo las pistas
    for (int i = 0; i < trackCount; i++) {
      song.addTrack(readTrack(i + 1, song));
    }

    return song;
  }

  private void readBeat(int header, TGMeasure measure, TGBeatData data) {
    TGBeat beat = new TGBeatImpl();
    TGVoice voice = beat.getVoice(0);

    beat.setStart(data.getStart());
    voice.setEmpty(false);

    // leo la duracion
    if (((header & BEAT_NEXT_DURATION) != 0)) {
      readDuration(data.getDuration());
    }

    // leo las notas
    if (((header & BEAT_HAS_NOTES) != 0)) {
      readNotes(beat, data);
    }

    // leo el acorde
    if (((header & BEAT_HAS_CHORD) != 0)) {
      readChord(beat);
    }

    // leo el texto
    if (((header & BEAT_HAS_TEXT) != 0)) {
      readText(beat);
    }

    voice.setDuration(data.getDuration().clone());

    measure.addBeat(beat);

    data.setStart(data.getStart() + data.getDuration().getTime());
  }

  private void readBeats(TGMeasure measure, TGBeatData data) {
    int header = BEAT_HAS_NEXT;
    while (((header & BEAT_HAS_NEXT) != 0)) {
      header = readHeader();
      readBeat(header, measure, data);
    }
  }

  private BendingEffect readBendEffect() {
    BendingEffect bend = new BendingEffect();

    // leo la cantidad de puntos
    int count = readByte();

    for (int i = 0; i < count; i++) {
      // leo la posicion
      int position = readByte();

      // leo el valor
      int value = readByte();

      // agrego el punto
      bend.addPoint(position, value);
    }
    return bend;
  }

  private byte readByte() {
    try {
      return (byte) this.dataInputStream.read();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  private void readChannel(TGTrack track) {
    int header = readHeader();

    // leo el canal
    track.getChannel().setChannel(readByte());

    // leo el canal de efectos
    track.getChannel().setEffectChannel(readByte());

    // leo el instrumento
    track.getChannel().setInstrument(readByte());

    // leo el volumen
    track.getChannel().setVolume(readByte());

    // leo el balance
    track.getChannel().setBalance(readByte());

    // leo el chorus
    track.getChannel().setChorus(readByte());

    // leo el reverb
    track.getChannel().setReverb(readByte());

    // leo el phaser
    track.getChannel().setPhaser(readByte());

    // leo el tremolo
    track.getChannel().setTremolo(readByte());

    // leo el solo
    track.setSolo((header & CHANNEL_SOLO) != 0);

    // leo el mute
    track.setMute((header & CHANNEL_MUTE) != 0);
  }

  private void readChord(TGBeat beat) {
    TGChord chord = new TGChordImpl(readByte());

    // leo el nombre
    chord.setName(readUnsignedByteString());

    // leo el primer fret
    chord.setFirstFret(readByte());

    // leo las cuerdas
    for (int string = 0; string < chord.countStrings(); string++) {
      chord.addFretValue(string, readByte());
    }
    beat.setChord(chord);
  }

  private void readDivisionType(TGDivisionType divisionType) {
    // leo los enters
    divisionType.setEnters(readByte());

    // leo los tiempos
    divisionType.setTimes(readByte());
  }

  private void readDuration(TGDuration duration) {
    int header = readHeader();

    // leo el puntillo
    duration.setDotted((header & DURATION_DOTTED) != 0);

    // leo el doble puntillo
    duration.setDoubleDotted((header & DURATION_DOUBLE_DOTTED) != 0);

    // leo el valor
    duration.setValue(readByte());

    // leo el tipo de divisiones
    if (((header & DURATION_NO_TUPLE) != 0)) {
      readDivisionType(duration.getDivision());
    } else {
      TGDivisionType.NORMAL.copy(duration.getDivision());
    }
  }

  private TGEffectGrace readGraceEffect() {
    int header = readHeader();

    TGEffectGrace effect = new TGEffectGrace();

    effect.setDead((header & GRACE_FLAG_DEAD) != 0);

    effect.setOnBeat((header & GRACE_FLAG_ON_BEAT) != 0);

    // leo el fret
    effect.setFret(readByte());

    // leo la duracion
    effect.setDuration(readByte());

    // leo el velocity
    effect.setDynamic(readByte());

    // leo la transicion
    effect.setTransition(readByte());

    return effect;
  }

  private HarmonicEffect readHarmonicEffect() {
    // leo el tipo
    final int id = readByte();

    HarmonicEffect harmonic = null;

    if (id == HarmonicEffect.ARTIFICIAL.getId()) {
      harmonic = HarmonicEffect.ARTIFICIAL;
    } else if (id == HarmonicEffect.NATURAL.getId()) {
      harmonic = HarmonicEffect.NATURAL;
    } else if (id == HarmonicEffect.PINCH.getId()) {
      harmonic = HarmonicEffect.PINCH;
    } else if (id == HarmonicEffect.SEMI.getId()) {
      harmonic = HarmonicEffect.SEMI;
    } else if (id == HarmonicEffect.TAPPED.getId()) {
      harmonic = HarmonicEffect.TAPPED;
    } else {
      LOG.debug("Unknown type of HarmonicEffect, with id " + id);
    }

    // leo la data
    if (harmonic != null && !harmonic.equals(HarmonicEffect.NATURAL)) {
      harmonic.setData(readByte());
    }

    return harmonic;
  }

  private int readHeader() {
    try {
      return this.dataInputStream.read();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  private int readHeader(int bCount) {
    int header = 0;
    for (int i = bCount; i > 0; i--) {
      header += (readHeader() << ((8 * i) - 8));
    }
    return header;
  }

  private TGString readInstrumentString(int number) {
    TGString string = new TGString();

    string.setNumber(number);

    // leo el valor
    string.setValue(readByte());

    return string;
  }

  private String readIntegerString() {
    try {
      return readString(this.dataInputStream.readInt());
    } catch (IOException e) {
      LOG.error(e);
    }
    return null;
  }

  private void readLyrics(TGLyric lyrics) {
    // leo el compas de comienzo
    lyrics.setFrom(readShort());

    // leo el texto
    lyrics.setLyrics(readIntegerString());
  }

  private TGMarker readMarker(int measure) {
    TGMarker marker = new TGMarker();

    marker.setMeasure(measure);

    // leo el titulo
    marker.setTitle(readUnsignedByteString());

    // leo el color
    marker.setColor(readRGBColor());

    return marker;
  }

  private TGMeasure readMeasure(TGMeasureHeader measureHeader,
      TGMeasure lastMeasure) {
    int header = readHeader();

    TGMeasure measure = new TGMeasureImpl(measureHeader);
    TGBeatData data = new TGBeatData(measure);

    // leo la los beats
    readBeats(measure, data);

    // leo la clave
    measure
        .setClef((lastMeasure == null) ? Clef.TREBLE : lastMeasure.getClef());
    if (((header & MEASURE_CLEF) != 0)) {
      final int clefCode = readByte();

      Clef clef = null;
      switch (clefCode) {
      case 1:
        clef = Clef.TREBLE;
        break;
      case 2:
        clef = Clef.BASS;
        break;
      case 3:
        clef = Clef.TENOR;
        break;
      case 4:
        clef = Clef.ALTO;
        break;
      }

      measure.setClef(clef);
    }

    // leo el key signature
    measure.setKeySignature((lastMeasure == null) ? 0 : lastMeasure
        .getKeySignature());
    if (((header & MEASURE_KEYSIGNATURE) != 0)) {
      measure.setKeySignature(readByte());
    }

    return measure;
  }

  private TGMeasureHeader readMeasureHeader(int number, long start,
      TGMeasureHeader lastMeasureHeader) {
    int header = readHeader();

    TGMeasureHeader measureHeader = new TGMeasureHeaderImpl();
    measureHeader.setNumber(number);
    measureHeader.setStart(start);

    // leo el time signature
    if (((header & MEASURE_HEADER_TIMESIGNATURE) != 0)) {
      readTimeSignature(measureHeader.getTimeSignature());
    } else if (lastMeasureHeader != null) {
      measureHeader.setTimeSignature(lastMeasureHeader.getTimeSignature()
          .clone());
    }

    // leo el tempo
    if (((header & MEASURE_HEADER_TEMPO) != 0)) {
      readTempo(measureHeader.getTempo());
    } else if (lastMeasureHeader != null) {
      lastMeasureHeader.getTempo().copy(measureHeader.getTempo());
    }

    // leo el comienzo de la repeticion
    measureHeader.setRepeatOpen((header & MEASURE_HEADER_REPEAT_OPEN) != 0);

    // leo el numero de repeticiones
    if (((header & MEASURE_HEADER_REPEAT_CLOSE) != 0)) {
      measureHeader.setRepeatClose(readShort());
    }

    // leo los finales alternativos
    if (((header & MEASURE_HEADER_REPEAT_ALTERNATIVE) != 0)) {
      measureHeader.setRepeatAlternative(readByte());
    }

    // leo el marker
    if (((header & MEASURE_HEADER_MARKER) != 0)) {
      measureHeader.setMarker(readMarker(number));
    }

    measureHeader
        .setTripletFeel((lastMeasureHeader != null) ? lastMeasureHeader
            .getTripletFeel() : TGMeasureHeader.TRIPLET_FEEL_NONE);
    if (((header & MEASURE_HEADER_TRIPLET_FEEL) != 0)) {
      measureHeader.setTripletFeel(readByte());
    }

    return measureHeader;
  }

  private void readNote(int header, TGBeat beat, TGBeatData data) {
    TGNote note = new TGNoteImpl();
    TGVoice voice = beat.getVoice(0);

    // leo el valor
    note.setValue(readByte());

    // leo la cuerda
    note.setString(readByte());

    // leo la ligadura
    note.setTiedNote((header & NOTE_TIED) != 0);

    // leo el velocity
    if (((header & NOTE_VELOCITY) != 0)) {
      data.setVelocity(readByte());
    }
    note.setVelocity(data.getVelocity());

    // leo los efectos
    if (((header & NOTE_EFFECT) != 0)) {
      readNoteEffect(note.getEffect());
    }

    voice.addNote(note);
  }

  private void readNoteEffect(TGNoteEffect effect) {
    int header = readHeader(3);

    // leo el bend
    if (((header & EFFECT_BEND) != 0)) {
      effect.setBend(readBendEffect());
    }

    // leo el tremolo bar
    if (((header & EFFECT_TREMOLO_BAR) != 0)) {
      effect.setTremoloBar(readTremoloBarEffect());
    }

    // leo el harmonic
    if (((header & EFFECT_HARMONIC) != 0)) {
      effect.setHarmonic(readHarmonicEffect());
    }

    // leo el grace
    if (((header & EFFECT_GRACE) != 0)) {
      effect.setGrace(readGraceEffect());
    }

    // leo el trill
    if (((header & EFFECT_TRILL) != 0)) {
      effect.setTrill(readTrillEffect());
    }

    // leo el tremolo picking
    if (((header & EFFECT_TREMOLO_PICKING) != 0)) {
      effect.setTremoloPicking(readTremoloPickingEffect());
    }

    // vibrato
    effect.setVibrato(((header & EFFECT_VIBRATO) != 0));

    // dead note
    effect.setDeadNote(((header & EFFECT_DEAD) != 0));

    // slide
    effect.setSlide(((header & EFFECT_SLIDE) != 0));

    // hammer-on/pull-off
    effect.setHammer(((header & EFFECT_HAMMER) != 0));

    // ghost note
    effect.setGhostNote(((header & EFFECT_GHOST) != 0));

    // accentuated note
    effect.setAccentuatedNote(((header & EFFECT_ACCENTUATED) != 0));

    // heavy accentuated note
    effect.setHeavyAccentuatedNote(((header & EFFECT_HEAVY_ACCENTUATED) != 0));

    // palm mute
    effect.setPalmMute(((header & EFFECT_PALM_MUTE) != 0));

    // staccato
    effect.setStaccato(((header & EFFECT_STACCATO) != 0));

    // tapping
    effect.setTapping(((header & EFFECT_TAPPING) != 0));

    // slapping
    effect.setSlapping(((header & EFFECT_SLAPPING) != 0));

    // popping
    effect.setPopping(((header & EFFECT_POPPING) != 0));

    // fade in
    effect.setFadeIn(((header & EFFECT_FADE_IN) != 0));
  }

  private void readNotes(TGBeat beat, TGBeatData data) {
    int header = NOTE_HAS_NEXT;
    while (((header & NOTE_HAS_NEXT) != 0)) {
      header = readHeader();
      readNote(header, beat, data);
    }
  }

  private Color readRGBColor() {
    // leo el RGB
    final int red = readByte() & 0xff;
    final int green = readByte() & 0xff;
    final int blue = readByte() & 0xff;
    return new Color(red, green, blue);
  }

  private short readShort() {
    try {
      return this.dataInputStream.readShort();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  public TGSong readSong() throws TGFileFormatException {
    try {
      if (this.isSupportedVersion()) {
        TGSong song = this.read();
        this.dataInputStream.close();
        return song;
      }
      throw new TGFileFormatException("Unsopported Version");
    } catch (Throwable throwable) {
      throw new TGFileFormatException(throwable);
    }
  }

  private String readString(int length) {
    try {
      char[] chars = new char[length];
      for (int i = 0; i < chars.length; i++) {
        chars[i] = this.dataInputStream.readChar();
      }
      return String.copyValueOf(chars);
    } catch (IOException e) {
      LOG.error(e);
    }
    return null;
  }

  private void readTempo(TGTempo tempo) {
    // leo el valor
    tempo.setValue(readShort());
  }

  private void readText(TGBeat beat) {
    TGText text = new TGTextImpl();

    // leo el texto
    text.setValue(readUnsignedByteString());

    beat.setText(text);
  }

  private void readTimeSignature(TGTimeSignature timeSignature) {
    // leo el numerador
    timeSignature.setNumerator(readByte());

    // leo el denominador
    readDuration(timeSignature.getDenominator());
  }

  private TGTrack readTrack(int number, TGSong song) {
    // header
    int header = readHeader();

    TGTrack track = new TGTrackImpl();

    track.setNumber(number);

    // leo el nombre
    track.setName(readUnsignedByteString());

    // leo el canal
    readChannel(track);

    // leo la cantidad de compases
    int measureCount = song.countMeasureHeaders();

    // leo los compases
    TGMeasure lastMeasure = null;
    for (int i = 0; i < measureCount; i++) {
      TGMeasure measure = readMeasure(song.getMeasureHeader(i), lastMeasure);
      track.addMeasure(measure);
      lastMeasure = measure;
    }

    // leo la cantidad de cuerdas
    int stringCount = readByte();

    // leo las cuerdas
    for (int i = 0; i < stringCount; i++) {
      track.getStrings().add(readInstrumentString(i + 1));
    }

    // leo el offset
    track.setOffset(TGTrack.MIN_OFFSET + readByte());

    // leo el color
    track.setColor(readRGBColor());

    // leo el lyrics
    if (((header & TRACK_LYRICS) != 0)) {
      readLyrics(track.getLyrics());
    }

    return track;
  }

  private BendingEffect readTremoloBarEffect() {
    BendingEffect tremoloBar = new BendingEffect();

    // leo la cantidad de puntos
    int count = readByte();

    for (int i = 0; i < count; i++) {
      // leo la posicion
      int position = readByte();

      // leo el valor
      int value = (readByte() - EffectPoint.MAX_VALUE_LENGTH);

      // agrego el punto
      tremoloBar.addPoint(position, value);
    }
    return tremoloBar;
  }

  private TGEffectTremoloPicking readTremoloPickingEffect() {
    TGEffectTremoloPicking effect = new TGEffectTremoloPicking();

    // leo la duracion
    effect.getDuration().setValue(readByte());

    return effect;
  }

  private TGEffectTrill readTrillEffect() {
    TGEffectTrill effect = new TGEffectTrill();

    // leo el fret
    effect.setFret(readByte());

    // leo la duracion
    effect.getDuration().setValue(readByte());

    return effect;
  }

  private String readUnsignedByteString() {
    try {
      return readString((this.dataInputStream.read() & 0xFF));
    } catch (IOException e) {
      LOG.error(e);
    }
    return null;
  }

  private void readVersion() {
    if (this.version == null) {
      this.version = readUnsignedByteString();
    }
  }
}
