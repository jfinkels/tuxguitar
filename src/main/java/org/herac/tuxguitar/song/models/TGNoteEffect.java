/*
 * Created on 26-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.models.effects.BendingEffect;
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
public class TGNoteEffect {
  private boolean accentuatedNote = false;
  private BendingEffect bend = null;
  private boolean deadNote = false;
  private boolean fadeIn = false;
  private boolean ghostNote = false;
  private TGEffectGrace grace = null;
  private boolean hammer = false;
  private HarmonicEffect harmonic = null;
  private boolean heavyAccentuatedNote = false;
  private boolean letRing = false;
  private boolean palmMute = false;
  private boolean popping = false;
  private boolean slapping = false;
  private boolean slide = false;
  private boolean staccato = false;
  private boolean tapping = false;
  private BendingEffect tremoloBar = null;
  private TGEffectTremoloPicking tremoloPicking = null;
  private TGEffectTrill trill = null;
  private boolean vibrato = false;

  @Override
  public TGNoteEffect clone() {
    TGNoteEffect effect = new TGNoteEffect();
    effect.setVibrato(isVibrato());
    effect.setDeadNote(isDeadNote());
    effect.setSlide(isSlide());
    effect.setHammer(isHammer());
    effect.setGhostNote(isGhostNote());
    effect.setAccentuatedNote(isAccentuatedNote());
    effect.setHeavyAccentuatedNote(isHeavyAccentuatedNote());
    effect.setPalmMute(isPalmMute());
    effect.setLetRing(isLetRing());
    effect.setStaccato(isStaccato());
    effect.setTapping(isTapping());
    effect.setSlapping(isSlapping());
    effect.setPopping(isPopping());
    effect.setFadeIn(isFadeIn());
    effect.setBend(isBend() ? this.bend.clone() : null);
    effect.setTremoloBar(isTremoloBar() ? this.tremoloBar.clone() : null);
    effect.setHarmonic(isHarmonic() ? this.harmonic : null);
    effect.setGrace(isGrace() ? this.grace.clone() : null);
    effect.setTrill(isTrill() ? this.trill.clone() : null);
    effect.setTremoloPicking(isTremoloPicking() ? this.tremoloPicking.clone()
        : null);
    return effect;
  }

  public BendingEffect getBend() {
    return this.bend;
  }

  public TGEffectGrace getGrace() {
    return this.grace;
  }

  public HarmonicEffect getHarmonic() {
    return this.harmonic;
  }

  public BendingEffect getTremoloBar() {
    return this.tremoloBar;
  }

  public TGEffectTremoloPicking getTremoloPicking() {
    return this.tremoloPicking;
  }

  public TGEffectTrill getTrill() {
    return this.trill;
  }

  public boolean hasAnyEffect() {
    return (isBend() || isTremoloBar() || isHarmonic() || isGrace()
        || isTrill() || isTremoloPicking() || isVibrato() || isDeadNote()
        || isSlide() || isHammer() || isGhostNote() || isAccentuatedNote()
        || isHeavyAccentuatedNote() || isPalmMute() || isLetRing()
        || isStaccato() || isTapping() || isSlapping() || isPopping() || isFadeIn());
  }

  public boolean isAccentuatedNote() {
    return this.accentuatedNote;
  }

  public boolean isBend() {
    return (this.bend != null && !this.bend.getPoints().isEmpty());
  }

  public boolean isDeadNote() {
    return this.deadNote;
  }

  public boolean isFadeIn() {
    return this.fadeIn;
  }

  public boolean isGhostNote() {
    return this.ghostNote;
  }

  public boolean isGrace() {
    return (this.grace != null);
  }

  public boolean isHammer() {
    return this.hammer;
  }

  public boolean isHarmonic() {
    return (this.harmonic != null);
  }

  public boolean isHeavyAccentuatedNote() {
    return this.heavyAccentuatedNote;
  }

  public boolean isLetRing() {
    return this.letRing;
  }

  public boolean isPalmMute() {
    return this.palmMute;
  }

  public boolean isPopping() {
    return this.popping;
  }

  public boolean isSlapping() {
    return this.slapping;
  }

  public boolean isSlide() {
    return this.slide;
  }

  public boolean isStaccato() {
    return this.staccato;
  }

  public boolean isTapping() {
    return this.tapping;
  }

  public boolean isTremoloBar() {
    return (this.tremoloBar != null);
  }

  public boolean isTremoloPicking() {
    return (this.tremoloPicking != null);
  }

  public boolean isTrill() {
    return (this.trill != null);
  }

  public boolean isVibrato() {
    return this.vibrato;
  }

  public void setAccentuatedNote(boolean accentuatedNote) {
    this.accentuatedNote = accentuatedNote;
    // si es true, quito los efectos incompatibles
    if (this.isAccentuatedNote()) {
      this.ghostNote = false;
      this.heavyAccentuatedNote = false;
    }
  }

  public void setBend(BendingEffect bend) {
    this.bend = bend;
    // si no es null quito los efectos incompatibles
    if (this.isBend()) {
      this.tremoloBar = null;
      this.trill = null;
      this.deadNote = false;
      this.slide = false;
      this.hammer = false;
    }
  }

  public void setDeadNote(boolean deadNote) {
    this.deadNote = deadNote;
    // si es true, quito los efectos incompatibles
    if (this.isDeadNote()) {
      this.tremoloBar = null;
      this.bend = null;
      this.trill = null;
      this.slide = false;
      this.hammer = false;
    }
  }

  public void setFadeIn(boolean fadeIn) {
    this.fadeIn = fadeIn;
  }

  public void setGhostNote(boolean ghostNote) {
    this.ghostNote = ghostNote;
    // si es true, quito los efectos incompatibles
    if (this.isGhostNote()) {
      this.accentuatedNote = false;
      this.heavyAccentuatedNote = false;
    }
  }

  public void setGrace(TGEffectGrace grace) {
    this.grace = grace;
  }

  public void setHammer(boolean hammer) {
    this.hammer = hammer;
    // si es true, quito los efectos incompatibles
    if (this.isHammer()) {
      this.trill = null;
      this.tremoloBar = null;
      this.bend = null;
      this.deadNote = false;
      this.slide = false;
    }
  }

  public void setHarmonic(HarmonicEffect harmonic) {
    this.harmonic = harmonic;
  }

  public void setHeavyAccentuatedNote(boolean heavyAccentuatedNote) {
    this.heavyAccentuatedNote = heavyAccentuatedNote;
    // si es true, quito los efectos incompatibles
    if (this.isHeavyAccentuatedNote()) {
      this.ghostNote = false;
      this.accentuatedNote = false;
    }
  }

  public void setLetRing(boolean letRing) {
    this.letRing = letRing;
    // si es true, quito los efectos incompatibles
    if (this.isLetRing()) {
      this.staccato = false;
      this.palmMute = false;
    }
  }

  public void setPalmMute(boolean palmMute) {
    this.palmMute = palmMute;
    // si es true, quito los efectos incompatibles
    if (this.isPalmMute()) {
      this.staccato = false;
      this.letRing = false;
    }
  }

  public void setPopping(boolean popping) {
    this.popping = popping;
    // si es true, quito los efectos incompatibles
    if (this.isPopping()) {
      this.tapping = false;
      this.slapping = false;
    }
  }

  public void setSlapping(boolean slapping) {
    this.slapping = slapping;
    // si es true, quito los efectos incompatibles
    if (this.isSlapping()) {
      this.tapping = false;
      this.popping = false;
    }
  }

  public void setSlide(boolean slide) {
    this.slide = slide;
    // si es true, quito los efectos incompatibles
    if (this.isSlide()) {
      this.trill = null;
      this.tremoloBar = null;
      this.bend = null;
      this.deadNote = false;
      this.hammer = false;
    }
  }

  public void setStaccato(boolean staccato) {
    this.staccato = staccato;
    // si es true, quito los efectos incompatibles
    if (this.isStaccato()) {
      this.palmMute = false;
      this.letRing = false;
    }
  }

  public void setTapping(boolean tapping) {
    this.tapping = tapping;
    // si es true, quito los efectos incompatibles
    if (this.isTapping()) {
      this.slapping = false;
      this.popping = false;
    }
  }

  public void setTremoloBar(BendingEffect tremoloBar) {
    this.tremoloBar = tremoloBar;
    // si no es null quito los efectos incompatibles
    if (this.isTremoloBar()) {
      this.bend = null;
      this.trill = null;
      this.deadNote = false;
      this.slide = false;
      this.hammer = false;
    }
  }

  public void setTremoloPicking(TGEffectTremoloPicking tremoloPicking) {
    this.tremoloPicking = tremoloPicking;
    // si es true, quito los efectos incompatibles
    if (this.isTremoloPicking()) {
      this.trill = null;
      this.bend = null;
      this.tremoloBar = null;
      this.slide = false;
      this.hammer = false;
      this.deadNote = false;
      this.vibrato = false;
    }
  }

  public void setTrill(TGEffectTrill trill) {
    this.trill = trill;
    // si es true, quito los efectos incompatibles
    if (this.isTrill()) {
      this.bend = null;
      this.tremoloBar = null;
      this.tremoloPicking = null;
      this.slide = false;
      this.hammer = false;
      this.deadNote = false;
      this.vibrato = false;
    }
  }

  public void setVibrato(boolean vibrato) {
    this.vibrato = vibrato;
    // si no es null quito los efectos incompatibles
    if (this.isVibrato()) {
      this.trill = null;
    }
  }

}
