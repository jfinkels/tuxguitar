package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGVelocities;

public abstract class TGEffectGrace {

  public static final int TRANSITION_BEND = 2;

  public static final int TRANSITION_HAMMER = 3;

  public static final int TRANSITION_NONE = 0;

  public static final int TRANSITION_SLIDE = 1;

  private boolean dead;
  private int duration;
  private int dynamic;
  private int fret;
  private boolean onBeat;
  private int transition;

  public TGEffectGrace() {
    this.fret = 0;
    this.duration = 1;
    this.dynamic = TGVelocities.DEFAULT;
    this.transition = TRANSITION_NONE;
    this.onBeat = false;
    this.dead = false;
  }

  public TGEffectGrace clone(TGFactory factory) {
    TGEffectGrace effect = factory.newEffectGrace();
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
    return (int) ((TGDuration.QUARTER_TIME / 16.00) * getDuration());
  }

  public int getDynamic() {
    return this.dynamic;
  }

  public int getFret() {
    return this.fret;
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

  public void setFret(int fret) {
    this.fret = fret;
  }

  public void setOnBeat(boolean onBeat) {
    this.onBeat = onBeat;
  }

  public void setTransition(int transition) {
    this.transition = transition;
  }

}
