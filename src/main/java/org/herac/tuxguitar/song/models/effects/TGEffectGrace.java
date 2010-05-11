package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGVelocities;

public class TGEffectGrace extends ExtraNoteEffect {

  public static final int TRANSITION_BEND = 2;

  public static final int TRANSITION_HAMMER = 3;

  public static final int TRANSITION_NONE = 0;

  public static final int TRANSITION_SLIDE = 1;

  private boolean dead = false;
  private int duration = 1;
  private int dynamic = TGVelocities.DEFAULT;
  private boolean onBeat = false;
  // private Transition transition = Transition.NONE;
  private int transition = 0;

  @Override
  public TGEffectGrace clone() {
    TGEffectGrace effect = new TGEffectGrace();
    effect.setFret(getFret());
    effect.setDuration(getDuration());
    effect.setDynamic(getDynamic());
    effect.setTransition(getTransition());
    effect.setOnBeat(isOnBeat());
    effect.setDead(isDead());
    return effect;
  }

  public int getDuration() {
    return this.duration;
  }

  public int getDurationTime() {
    // return (int)(((float)TGDuration.QUARTER_TIME / 16.00 ) *
    // (float)getDuration());
    return (int) ((TGDuration.QUARTER_TIME / 16.00) * this.duration);
  }

  public int getDynamic() {
    return this.dynamic;
  }

  public int getTransition() {
    return this.transition;
  }

  public boolean isDead() {
    return this.dead;
  }

  public boolean isOnBeat() {
    return this.onBeat;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public void setDynamic(int dynamic) {
    this.dynamic = dynamic;
  }

  public void setOnBeat(boolean onBeat) {
    this.onBeat = onBeat;
  }

  public void setTransition(int transition) {
    this.transition = transition;
  }

}
