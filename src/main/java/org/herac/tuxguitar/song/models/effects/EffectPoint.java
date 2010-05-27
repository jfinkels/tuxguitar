package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.models.Pair;

public class EffectPoint extends Pair<Integer, Integer> {

  public static final int MAX_POSITION_LENGTH = 12;
  public static final int SEMITONE_LENGTH = 1;
  public static final int MAX_VALUE_LENGTH = SEMITONE_LENGTH * 12;
  
  public EffectPoint(final int position, final int value) {
    super(position, value);
  }

  @Override
  public EffectPoint clone() {
    return new EffectPoint(this.left, this.right);
  }

  public int getPosition() {
    return this.left;
  }

  public long getTime(long duration) {
    return (duration * this.getPosition() / MAX_POSITION_LENGTH);
  }

  public int getValue() {
    return this.right;
  }
}