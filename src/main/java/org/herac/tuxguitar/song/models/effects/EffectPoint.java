package org.herac.tuxguitar.song.models.effects;

public class EffectPoint {
  private final int position;
  private final int value;

  public static final int MAX_POSITION_LENGTH = 12;

  public static final int SEMITONE_LENGTH = 1;

  public static final int MAX_VALUE_LENGTH = (SEMITONE_LENGTH * 12);
  
  public EffectPoint(int position, int value) {
    this.position = position;
    this.value = value;
  }

  @Override
  public Object clone() {
    return new EffectPoint(this.position, this.value);
  }

  public int getPosition() {
    return this.position;
  }

  public long getTime(long duration) {
    return (duration * this.position / MAX_POSITION_LENGTH);
  }

  public int getValue() {
    return this.value;
  }
}