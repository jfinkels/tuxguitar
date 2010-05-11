package org.herac.tuxguitar.song.models.effects;

public abstract class TGEffectHarmonic {
  public static final int UNKNOWN_ID = -1;
  public static final String UNKNOWN_LABEL = "";

  private int data = 0;

  @Override
  public abstract TGEffectHarmonic clone();

  public int getData() {
    return this.data;
  }

  public void setData(int data) {
    this.data = data;
  }

  public abstract int getId();

  public abstract String getLabel();

}
