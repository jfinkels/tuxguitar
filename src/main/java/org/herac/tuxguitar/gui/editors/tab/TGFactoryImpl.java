package org.herac.tuxguitar.gui.editors.tab;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGDivisionType;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGLyric;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGNoteEffect;
import org.herac.tuxguitar.song.models.TGStroke;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGText;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.song.models.TGVoice;
import org.herac.tuxguitar.song.models.effects.BendingEffect;
import org.herac.tuxguitar.song.models.effects.TGEffectGrace;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class TGFactoryImpl {

  public static TGBeat newBeat() {
    return new TGBeatImpl();
  }

  public static TGDuration newDuration() {
    return new TGDuration();
  }
  
  public static TGTimeSignature newTimeSignature() {
    return new TGTimeSignature();
  }
  
  public static TGChord newChord(final int length) {
    return new TGChordImpl(length);
  }

  public static TGMeasureHeader newHeader() {
    return new TGMeasureHeaderImpl();
  }

  public static TGLyric newLyric() {
    return new TGLyricImpl();
  }

  public static TGMeasure newMeasure(final TGMeasureHeader header) {
    return new TGMeasureImpl(header);
  }
  
  public static TGStroke newStroke() {
    return new TGStroke();
  }

  public static TGNote newNote() {
    return new TGNoteImpl();
  }

  public static TGText newText() {
    return new TGTextImpl();
  }

  public static TGTrack newTrack() {
    return new TGTrackImpl();
  }

  public static TGVoice newVoice(final int index) {
    return new TGVoiceImpl(index);
  }

  public static TGDivisionType newDivisionType() {
    return new TGDivisionType();
  }

  public static TGChannel newChannel() {
    return new TGChannel();
  }

  public static TGNoteEffect newEffect() {
    return new TGNoteEffect();
  }

  public static TGTempo newTempo() {
    return new TGTempo();
  }

  public BendingEffect newBendingEffect() {
    return new BendingEffect();
  }

  public TGEffectGrace newEffectGrace() {
    return new TGEffectGrace();
  }
}
