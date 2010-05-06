package org.herac.tuxguitar.player.impl.sequencer;

import org.herac.tuxguitar.song.models.TGDuration;

public class MidiTickPlayer {

  private static final int SECOND_IN_MILLIS = 1000;

  private long lastTime;
  private int tempo;
  private long tick;
  private boolean tickChanged;
  private long tickLength;
  private long time;

  public MidiTickPlayer() {
    super();
  }

  public void clearTick() {
    this.tickLength = 0;
  }

  public int getTempo() {
    return this.tempo;
  }

  public long getTick() {
    return this.tick;
  }

  public long getTickLength() {
    return this.tickLength;
  }

  public void notifyTick(long tick) {
    this.tickLength = Math.max(this.tickLength, tick);
  }

  public void process() {
    this.lastTime = this.time;
    this.time = System.currentTimeMillis();
    if (!this.tickChanged) {
      this.tick += (TGDuration.QUARTER_TIME
          * ((float) getTempo() * (float) (this.time - this.lastTime) / 60f) / SECOND_IN_MILLIS);
    }
    this.tickChanged = false;
  }

  public void setTempo(int tempo) {
    this.tempo = tempo;
  }

  public void setTick(long tick) {
    this.tick = tick;
    this.tickChanged = true;
  }
}
