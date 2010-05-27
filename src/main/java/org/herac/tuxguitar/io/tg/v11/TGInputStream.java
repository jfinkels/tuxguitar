/*
 * Created on 16-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.io.tg.v11;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.io.tg.AbstractTuxGuitarInputStream;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGSong;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGInputStream extends AbstractTuxGuitarInputStream {

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(TGInputStream.class);

  @Override
  protected TGSong read() {
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

  @Override
  protected void readNoteEffect(TGNoteEffect effect) {
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

}
