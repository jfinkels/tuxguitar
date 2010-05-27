package org.herac.tuxguitar.io.tg;

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
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;
import org.herac.tuxguitar.song.models.effects.EffectPoint;
import org.herac.tuxguitar.song.models.effects.HarmonicEffect;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectTremoloPicking;
import org.herac.tuxguitar.song.models.effects.TGEffectTrill;
import org.herac.tuxguitar.song.models.effects.Transition;

public abstract class AbstractTuxGuitarInputStream extends TGStream implements
    TGInputStreamBase {

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(AbstractTuxGuitarInputStream.class);
  private DataInputStream dataInputStream;


private String version;

@Override
  public TGFileFormat getFileFormat() {
    return new TGFileFormat("TuxGuitar", "*.tg");
  }

  @Override
public void init(InputStream stream) {
  this.dataInputStream = new DataInputStream(stream);
  this.version = null;
}
  @Override

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



  protected abstract TGSong read();
  private void readBeat(int header, TGMeasure measure, TGBeatData data) {
    TGBeat beat = new TGBeatImpl();

    beat.setStart(data.getCurrentStart());

    readVoices(header, beat, data);

    // leo el stroke
    if (((header & BEAT_HAS_STROKE) != 0)) {
      beat.setStroke(readStroke());
    }

    // leo el acorde
    if (((header & BEAT_HAS_CHORD) != 0)) {
      readChord(beat);
    }

    // leo el texto
    if (((header & BEAT_HAS_TEXT) != 0)) {
      readText(beat);
    }

    measure.addBeat(beat);
  }
  

  private void readBeats(TGMeasure measure, TGBeatData data) {
    int header = BEAT_HAS_NEXT;
    while (((header & BEAT_HAS_NEXT) != 0)) {
      header = readHeader();
      readBeat(header, measure, data);
    }
  }
  protected BendingEffect readBendEffect() {
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
  
  protected byte readByte() {
    try {
      return (byte) this.dataInputStream.read();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }
  

  private void readChannel(TGChannel channel) {
    // leo el canal
    channel.setChannel(readByte());

    // leo el canal de efectos
    channel.setEffectChannel(readByte());

    // leo el instrumento
    channel.setInstrument(readByte());

    // leo el volumen
    channel.setVolume(readByte());

    // leo el balance
    channel.setBalance(readByte());

    // leo el chorus
    channel.setChorus(readByte());

    // leo el reverb
    channel.setReverb(readByte());

    // leo el phaser
    channel.setPhaser(readByte());

    // leo el tremolo
    channel.setTremolo(readByte());
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
  

  private TGDivisionType readDivisionType() {
    // leo los enters
    final int enters = readByte();

    // leo los tiempos
    final int times = readByte();
    
    return new TGDivisionType(enters, times);
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
    if (((header & DURATION_NO_TUPLET) != 0)) {
      duration.setDivision(readDivisionType());
    } else {
      duration.setDivision(TGDivisionType.NORMAL);
    }
  }
  


  protected TGEffectGrace readGraceEffect() {
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
    final int transitionId = readByte();
    
    Transition transition = null;
    
    if (transitionId == Transition.NONE.getId()) {
      transition = Transition.NONE;
    } else if (transitionId == Transition.BEND.getId()) {
      transition = Transition.BEND;
    } else if (transitionId == Transition.HAMMER.getId()) {
      transition = Transition.HAMMER;
    } else if (transitionId == Transition.SLIDE.getId()) {
      transition = Transition.SLIDE;
    }
    
    effect.setTransition(transition);

    return effect;
  }
  

  protected HarmonicEffect readHarmonicEffect() {
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
  


  protected int readHeader(int bCount) {
    int header = 0;
    for (int i = bCount; i > 0; i--) {
      header += (readHeader() << ((8 * i) - 8));
    }
    return header;
  }
  

  private TGString readInstrumentString(int number) {
    return new TGString(number, readByte());
  }
  
  protected String readIntegerString() {
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
  

  protected TGMeasureHeader readMeasureHeader(int number, long start,
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
      measureHeader.setTempo(lastMeasureHeader.getTempo().clone());
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

  private void readNote(int header, TGVoice voice, TGBeatData data) {
    TGNote note = new TGNoteImpl();

    // leo el valor
    note.setValue(readByte());

    // leo la cuerda
    note.setString(readByte());

    // leo la ligadura
    note.setTiedNote((header & NOTE_TIED) != 0);

    // leo el velocity
    if (((header & NOTE_VELOCITY) != 0)) {
      data.getVoice(voice.getIndex()).setVelocity(readByte());
    }
    note.setVelocity(data.getVoice(voice.getIndex()).getVelocity());

    // leo los efectos
    if (((header & NOTE_EFFECT) != 0)) {
      readNoteEffect(note.getEffect());
    }

    voice.addNote(note);
  }

  protected abstract void readNoteEffect(final TGNoteEffect effect);
  

  private void readNotes(TGVoice voice, TGBeatData data) {
    int header = NOTE_HAS_NEXT;
    while (((header & NOTE_HAS_NEXT) != 0)) {
      header = readHeader();
      readNote(header, voice, data);
    }
  }
  
  private Color readRGBColor() {
    // leo el RGB
    final int red = readByte() & 0xff;
    final int green = readByte() & 0xff;
    final int blue = readByte() & 0xff;

    return new Color(red, green, blue);
  }
  

  protected short readShort() {
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
  
  private TGStroke readStroke() {
    final int directionId = readByte();
    final int value = readByte();
    Direction direction = null;
    if (directionId == Direction.NONE.getId()) {
      direction = Direction.NONE;
    } else if (directionId == Direction.UP.getId()) {
      direction = Direction.UP;
    } else if (directionId == Direction.DOWN.getId()) {
      direction = Direction.DOWN;
    } else {
      LOG.error("Unknown direction ID: " + directionId);
    }
    
    return new TGStroke(direction, value);
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
  

  protected TGTrack readTrack(int number, TGSong song) {
    // header
    int header = readHeader();

    TGTrack track = new TGTrackImpl();

    track.setNumber(number);

    // leo el nombre
    track.setName(readUnsignedByteString());

    // leo el solo
    track.setSolo((header & TRACK_SOLO) != 0);

    // leo el mute
    track.setMute((header & TRACK_MUTE) != 0);

    // leo el canal
    readChannel(track.getChannel());

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
    track.setColor(this.readRGBColor());

    // leo el lyrics
    if (((header & TRACK_LYRICS) != 0)) {
      readLyrics(track.getLyrics());
    }

    return track;
  }

  protected BendingEffect readTremoloBarEffect() {
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
  
  protected TGEffectTremoloPicking readTremoloPickingEffect() {
    TGEffectTremoloPicking effect = new TGEffectTremoloPicking();

    // leo la duracion
    effect.getDuration().setValue(readByte());

    return effect;
  }

  protected TGEffectTrill readTrillEffect() {
    TGEffectTrill effect = new TGEffectTrill();

    // leo el fret
    effect.setFret(readByte());

    // leo la duracion
    effect.getDuration().setValue(readByte());

    return effect;
  }
  protected String readUnsignedByteString() {
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
  private void readVoices(int header, TGBeat beat, TGBeatData data) {
    for (int i = 0; i < TGBeat.MAX_VOICES; i++) {
      int shift = (i * 2);

      beat.getVoice(i).setEmpty(true);

      if (((header & (BEAT_HAS_VOICE << shift)) != 0)) {
        if (((header & (BEAT_HAS_VOICE_CHANGES << shift)) != 0)) {
          data.getVoice(i).setFlags(readHeader());
        }

        int flags = data.getVoice(i).getFlags();

        // leo la duracion
        if (((flags & VOICE_NEXT_DURATION) != 0)) {
          readDuration(data.getVoice(i).getDuration());
        }

        // leo las notas
        if (((flags & VOICE_HAS_NOTES) != 0)) {
          readNotes(beat.getVoice(i), data);
        }

        // leo la direccion
        if (((flags & VOICE_DIRECTION_UP) != 0)) {
          beat.getVoice(i).setDirection(Direction.UP);
        } else if (((flags & VOICE_DIRECTION_DOWN) != 0)) {
          beat.getVoice(i).setDirection(Direction.DOWN);
        }
        beat.getVoice(i).setDuration(data.getVoice(i).getDuration().clone());
        data.getVoice(i).setStart(
            data.getVoice(i).getStart()
                + beat.getVoice(i).getDuration().getTime());

        beat.getVoice(i).setEmpty(false);
      }
    }
  }

  

}
