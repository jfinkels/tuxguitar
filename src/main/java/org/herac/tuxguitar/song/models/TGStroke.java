package org.herac.tuxguitar.song.models;

public class TGStroke extends Pair<StrokeDirection, Integer> {

  public static final TGStroke DEFAULT = new TGStroke(StrokeDirection.NONE, 0);
  
  public TGStroke(final StrokeDirection direction, final int value) {
    super(direction, value);
  }

  @Override
  public TGStroke clone() {
    return new TGStroke(this.left, this.right);
  }

  public StrokeDirection getDirection() {
    return this.left;
  }
  
  public void setDirection(final StrokeDirection newDirection) {
    this.left = newDirection;
  }

  public int getIncrementTime(TGBeat beat) {
    long duration = 0;
    if (this.getValue() > 0) {
      for (int v = 0; v < beat.countVoices(); v++) {
        TGVoice voice = beat.getVoice(v);
        if (!voice.isEmpty()) {
          long currentDuration = voice.getDuration().getTime();
          if (duration == 0 || currentDuration < duration) {
            duration = (currentDuration <= TGDuration.QUARTER_TIME ? currentDuration
                : TGDuration.QUARTER_TIME);
          }
        }
      }
      if (duration > 0) {
        return Math.round(((duration / 8.0f) * (4.0f / this.getValue())));
      }
    }
    return 0;
  }

  public int getValue() {
    return this.right;
  }

  public void setValue(final int value) {
    this.right = value;
  }
}
