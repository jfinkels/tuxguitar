package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGDuration;

public abstract class TGEffectTrill {

  private TGDuration duration;
  private int fret;

  public TGEffectTrill(TGFactory factory) {
    this.fret = 0;
    this.duration = factory.newDuration();
  }

  public TGEffectTrill clone(TGFactory factory) {
    TGEffectTrill effect = factory.newEffectTrill();
    effect.setFret(getFret());
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

  public int getFret() {
    return this.fret;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

  public void setFret(int fret) {
    this.fret = fret;
  }

}
