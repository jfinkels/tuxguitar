package org.herac.tuxguitar.io.tg;

import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGVelocities;

public class TGVoiceData {
  private TGDuration duration = new TGDuration();
  private int flags = 0;
  private long start;
  private int velocity = TGVelocities.DEFAULT;

  public TGVoiceData(TGMeasure measure) {
    this.start = measure.getStart();
  }

  public TGDuration getDuration() {
    return this.duration;
  }

  public int getFlags() {
    return this.flags;
  }

  public long getStart() {
    return this.start;
  }

  public int getVelocity() {
    return this.velocity;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public void setVelocity(int velocity) {
    this.velocity = velocity;
  }
}