package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.models.TGDuration;

public class TGEffectTremoloPicking {

  private TGDuration duration = new TGDuration();

  @Override
  public TGEffectTremoloPicking clone() {
    TGEffectTremoloPicking effect = new TGEffectTremoloPicking();
    
    effect.setDuration(this.duration.clone());

    return effect;
  }

  public TGDuration getDuration() {
    return this.duration;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

}
