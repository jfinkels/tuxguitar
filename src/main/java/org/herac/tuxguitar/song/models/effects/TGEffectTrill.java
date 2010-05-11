package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.models.TGDuration;

public class TGEffectTrill extends ExtraNoteEffect {

  /** The duration of the trill. */
  private TGDuration duration = new TGDuration();

  @Override
  public TGEffectTrill clone() {
    TGEffectTrill effect = new TGEffectTrill();

    effect.setFret(this.getFret());
    effect.setDuration(this.getDuration().clone());

    return effect;
  }

  public TGDuration getDuration() {
    return this.duration;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

}
