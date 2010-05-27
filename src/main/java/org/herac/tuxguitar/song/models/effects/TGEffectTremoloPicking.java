package org.herac.tuxguitar.song.models.effects;

public class TGEffectTremoloPicking extends DurationEffect {

  @Override
  public TGEffectTremoloPicking clone() {
    final TGEffectTremoloPicking effect = new TGEffectTremoloPicking();

    effect.setDuration(this.getDuration().clone());

    return effect;
  }

}
