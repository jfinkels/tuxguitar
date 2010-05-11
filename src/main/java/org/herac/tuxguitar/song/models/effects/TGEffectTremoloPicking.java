package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.gui.editors.tab.TGFactoryImpl;
import org.herac.tuxguitar.song.models.TGDuration;

public class TGEffectTremoloPicking {

  private TGDuration duration;

  public TGEffectTremoloPicking() {
    this.duration = TGFactoryImpl.newDuration();
  }

  @Override
  public TGEffectTremoloPicking clone() {
    TGEffectTremoloPicking effect = new TGEffectTremoloPicking();
    effect.getDuration().setValue(getDuration().getValue());
    effect.getDuration().setDotted(getDuration().isDotted());
    effect.getDuration().setDoubleDotted(getDuration().isDoubleDotted());
    effect.getDuration().getDivision().setEnters(
        getDuration().getDivision().getEnters());
    effect.getDuration().getDivision().setTimes(
        getDuration().getDivision().getTimes());
    return effect;
  }

  public TGDuration getDuration() {
    return this.duration;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

}
