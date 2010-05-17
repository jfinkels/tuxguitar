/*
 * Created on 16-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.io.tg.v07;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.editors.chord.ChordSelector;
import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGInputStreamBase;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGInputStream implements TGInputStreamBase {
  private static final String TG_VERSION = "TG_DEVEL-0.01";

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
    this.dataInputStream = new DataInputStream(stream);
    this.version = null;
  }

  public boolean isSupportedVersion() {
    try {
      readVersion();
      return isSupportedVersion(this.version);
    } catch (Exception e) {
      return false;
    } catch (Error e) {
      return false;
    }
  }

  public boolean isSupportedVersion(String version) {
    return (version.equals(TG_VERSION));
  }

  private TGSong read() {
    TGSong song = new TGSong();

    // leo el nombre
    song.setName(readString());

    // leo el artista
    song.setArtist(readString());

    // leo el album
    song.setAlbum(readString());

    // leo el autor
    song.setAuthor(readString());

    // leo la cantidad de pistas
    int trackCount = readInt();

    // leo las pistas
    for (int i = 0; i < trackCount; i++) {
      song.addTrack(readTrack(song));
    }

    return song;
  }

  private BendingEffect readBendEffect() {
    BendingEffect bend = new BendingEffect();

    // leo la cantidad de puntos
    int count = readInt();

    for (int i = 0; i < count; i++) {
      // leo la posicion
      int position = readInt();

      // leo el valor
      int value = readInt();

      // agrego el punto
      bend.addPoint(position, ((value > 0) ? value / 2 : value));
    }
    return bend;
  }

  private boolean readBoolean() {
    try {
      return this.dataInputStream.readBoolean();
    } catch (IOException e) {
      LOG.error(e);
    }
    return false;
  }

  private void readChannel(TGTrack track) {
    // leo el canal
    track.getChannel().setChannel(readShort());

    // leo el canal de efectos
    track.getChannel().setEffectChannel(readShort());

    // leo el instrumento
    track.getChannel().setInstrument(readShort());

    // leo el volumen
    track.getChannel().setVolume(readShort());

    // leo el balance
    track.getChannel().setBalance(readShort());

    // leo el chorus
    track.getChannel().setChorus(readShort());

    // leo el reverb
    track.getChannel().setReverb(readShort());

    // leo el phaser
    track.getChannel().setPhaser(readShort());

    // leo el tremolo
    track.getChannel().setTremolo(readShort());

    // leo el solo
    track.setSolo(readBoolean());

    // leo el mute
    track.setMute(readBoolean());
  }

  private Color readColor() {
    final int red = readInt();
    final int green = readInt();
    final int blue = readInt();
    return new Color(red, green, blue);
  }

  private void readDivisionType(TGDivisionType divisionType) {
    // leo los enters
    divisionType.setEnters(readInt());

    // leo los tiempos
    divisionType.setTimes(readInt());
  }

  private void readDuration(TGDuration duration) {
    // leo el valor
    duration.setValue(readInt());

    // leo el puntillo
    duration.setDotted(readBoolean());

    // leo el doble puntillo
    duration.setDoubleDotted(readBoolean());

    // leo el tipo de divisiones
    readDivisionType(duration.getDivision());
  }

  private TGString readInstrumentString() {
    TGString string = new TGString();

    // leo el numero
    string.setNumber(readInt());

    // leo el valor
    string.setValue(readInt());

    return string;
  }

  private int readInt() {
    try {
      return this.dataInputStream.readInt();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  private long readLong() {
    try {
      return this.dataInputStream.readLong();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  private TGMeasure readMeasure(TGMeasureHeader header) {
    TGMeasure measure = new TGMeasureImpl(header);

    // leo el number
    header.setNumber(readInt());

    // leo el start
    header.setStart((TGDuration.QUARTER_TIME * readLong() / 1000));

    // leo la cantidad de notas
    int noteCount = readInt();

    // leo las notas
    TGBeat previous = null;
    for (int i = 0; i < noteCount; i++) {
      previous = readNote(measure, previous);
    }

    // leo la cantidad de silencios
    int silenceCount = readInt();

    // leo los silencios
    previous = null;
    for (int i = 0; i < silenceCount; i++) {
      previous = readSilence(measure, previous);
    }

    // leo el time signature
    readTimeSignature(header.getTimeSignature());

    // leo el tempo
    readTempo(header.getTempo());

    // leo la clave
    final int clefCode = readInt();
    
    Clef clef = null;
    switch(clefCode) {
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

    // leo el key signature
    measure.setKeySignature(readInt());

    // leo el comienzo de la repeticion
    header.setRepeatOpen(readBoolean());

    // leo el numero de repeticiones
    header.setRepeatClose(readInt());

    return measure;

  }

  private TGBeat readNote(TGMeasure measure, TGBeat previous) {
    TGBeat beat = previous;

    // leo el valor
    int value = readInt();

    // leo el start
    long start = (TGDuration.QUARTER_TIME * readLong() / 1000);
    if (beat == null || beat.getStart() != start) {
      beat = new TGBeatImpl();
      beat.setStart(start);
      measure.addBeat(beat);
    }
    TGVoice voice = beat.getVoice(0);
    voice.setEmpty(false);

    // leo la duracion
    readDuration(voice.getDuration());

    TGNote note = new TGNoteImpl();

    note.setValue(value);

    // leo el velocity
    note.setVelocity(readInt());

    // leo la cuerda
    note.setString(readInt());

    // leo la ligadura
    note.setTiedNote(readBoolean());

    // leo los efectos
    readNoteEffect(note.getEffect());

    voice.addNote(note);
    return beat;
  }

  private void readNoteEffect(TGNoteEffect effect) {
    // leo el vibrato
    effect.setVibrato(readBoolean());

    // leo el bend
    if (readBoolean()) {
      effect.setBend(readBendEffect());
    }

    // leo la nota muerta
    effect.setDeadNote(readBoolean());

    // leo el slide
    effect.setSlide(readBoolean());

    // leo el hammer
    effect.setHammer(readBoolean());
  }

  private short readShort() {
    try {
      return this.dataInputStream.readShort();
    } catch (IOException e) {
      LOG.error(e);
    }
    return 0;
  }

  private TGBeat readSilence(TGMeasure measure, TGBeat previous) {
    TGBeat beat = previous;

    // leo el start
    long start = (TGDuration.QUARTER_TIME * readLong() / 1000);
    if (beat == null || beat.getStart() != start) {
      beat = new TGBeatImpl();
      beat.setStart(start);
      measure.addBeat(beat);
    }
    TGVoice voice = beat.getVoice(0);
    voice.setEmpty(false);

    // leo la duracion
    readDuration(voice.getDuration());

    return beat;
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

  private String readString() {
    try {
      int length = this.dataInputStream.read();
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
    tempo.setValue(readInt());
  }

  private void readTimeSignature(TGTimeSignature timeSignature) {
    // leo el numerador
    timeSignature.setNumerator(readInt());

    // leo el denominador
    readDuration(timeSignature.getDenominator());
  }

  private TGTrack readTrack(TGSong song) {
    TGTrack track = new TGTrackImpl();

    // leo el numero
    track.setNumber((int) readLong());

    // leo el nombre
    track.setName(readString());

    // leo el canal
    readChannel(track);

    // leo la cantidad de compases
    int measureCount = readInt();

    if (song.countMeasureHeaders() == 0) {
      for (int i = 0; i < measureCount; i++) {
        TGMeasureHeader header = new TGMeasureHeaderImpl();
        song.addMeasureHeader(header);
      }
    }

    // leo los compases
    for (int i = 0; i < measureCount; i++) {
      track.addMeasure(readMeasure(song.getMeasureHeader(i)));
    }

    // leo la cantidad de cuerdas
    int stringCount = readInt();

    // leo las cuerdas
    for (int i = 0; i < stringCount; i++) {
      track.getStrings().add(readInstrumentString());
    }

    // leo el color
    track.setColor(readColor());

    return track;
  }

  private void readVersion() {
    if (this.version == null) {
      this.version = readString();
    }
  }
}
