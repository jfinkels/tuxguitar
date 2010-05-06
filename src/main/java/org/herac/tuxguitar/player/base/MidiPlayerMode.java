package org.herac.tuxguitar.player.base;

public class MidiPlayerMode {

  public static final int DEFAULT_TEMPO_PERCENT = 100;

  public static final int TYPE_CUSTOM = 2;
  public static final int TYPE_SIMPLE = 1;

  private int currentPercent;
  private int customPercentFrom;
  private int customPercentIncrement;
  private int customPercentTo;
  private boolean loop;
  private int loopEHeader;
  private int loopSHeader;
  private int simplePercent;
  private int type;

  public MidiPlayerMode() {
    this.clear();
  }

  public void clear() {
    this.loop = false;
    this.type = TYPE_SIMPLE;
    this.simplePercent = DEFAULT_TEMPO_PERCENT;
    this.customPercentFrom = DEFAULT_TEMPO_PERCENT;
    this.customPercentTo = DEFAULT_TEMPO_PERCENT;
    this.customPercentIncrement = 0;
    this.loopSHeader = -1;
    this.loopEHeader = -1;
    this.reset();
  }

  public int getCurrentPercent() {
    return this.currentPercent;
  }

  public int getCustomPercentFrom() {
    return this.customPercentFrom;
  }

  public int getCustomPercentIncrement() {
    return this.customPercentIncrement;
  }

  public int getCustomPercentTo() {
    return this.customPercentTo;
  }

  public int getLoopEHeader() {
    return this.loopEHeader;
  }

  public int getLoopSHeader() {
    return this.loopSHeader;
  }

  public int getSimplePercent() {
    return this.simplePercent;
  }

  public int getType() {
    return this.type;
  }

  public boolean isLoop() {
    return this.loop;
  }

  public void notifyLoop() {
    if (getType() == TYPE_SIMPLE) {
      this.setCurrentPercent(getSimplePercent());
    } else if (getType() == TYPE_CUSTOM) {
      this.setCurrentPercent(Math.min(getCustomPercentTo(),
          (getCurrentPercent() + getCustomPercentIncrement())));
    }
  }

  public void reset() {
    if (getType() == TYPE_SIMPLE) {
      this.setCurrentPercent(getSimplePercent());
    } else if (getType() == TYPE_CUSTOM) {
      this.setCurrentPercent(getCustomPercentFrom());
    }
  }

  public void setCurrentPercent(int currentPercent) {
    this.currentPercent = currentPercent;
  }

  public void setCustomPercentFrom(int customPercentFrom) {
    this.customPercentFrom = customPercentFrom;
  }

  public void setCustomPercentIncrement(int customPercentIncrement) {
    this.customPercentIncrement = customPercentIncrement;
  }

  public void setCustomPercentTo(int customPercentTo) {
    this.customPercentTo = customPercentTo;
  }

  public void setLoop(boolean loop) {
    this.loop = loop;
  }

  public void setLoopEHeader(int loopEHeader) {
    this.loopEHeader = loopEHeader;
  }

  public void setLoopSHeader(int loopSHeader) {
    this.loopSHeader = loopSHeader;
  }

  public void setSimplePercent(int simplePercent) {
    this.simplePercent = simplePercent;
  }

  public void setType(int type) {
    this.type = type;
  }
}
