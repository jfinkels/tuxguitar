package org.herac.tuxguitar.io.ptb.helper;

import org.herac.tuxguitar.song.models.TGDuration;

public class TrackStartHelper {

  private long barLength;
  private long barStart;
  private boolean measureEmpty;
  private boolean measureRest;

  private int section;
  private long[][] voices;

  public TrackStartHelper() {
    super();
  }

  public void checkBeat(boolean rest) {
    this.measureEmpty = (this.measureEmpty && rest);
    this.measureRest = true;
  }

  public long fixValue(long value) {
    return (((value % (TGDuration.QUARTER_TIME / 2)) + 10 > (TGDuration.QUARTER_TIME / 2)) ? (value + ((TGDuration.QUARTER_TIME / 2) - (value % (TGDuration.QUARTER_TIME / 2))))
        : value);
  }

  public long getBarLength() {
    return this.barLength;
  }

  public long getBarStart() {
    return this.barStart;
  }

  public long getMaxStart() {
    long result = TGDuration.QUARTER_TIME;
    for (int i = 0; i < this.voices.length; i++) {
      for (int j = 0; j < this.voices[i].length; j++) {
        result = Math.max(result, this.voices[i][j]);
      }
    }
    // checkRestMeasures
    if (this.measureRest && this.measureEmpty) {
      result = Math.max(result, (this.barStart + this.barLength));
    }
    return fixValue(result);
  }

  public int getSection() {
    return this.section;
  }

  public long getVoiceStart(int staff, int voice) {
    return this.voices[staff][voice];
  }

  public void init(int section, int staffs) {
    this.section = section;
    this.voices = new long[staffs][2];
    this.measureEmpty = true;
    this.measureRest = false;
    this.barStart = 0;
    this.barLength = 0;
  }

  public void initVoices(long start) {
    for (int i = 0; i < this.voices.length; i++) {
      for (int j = 0; j < this.voices[i].length; j++) {
        this.voices[i][j] = fixValue(start);
      }
    }
    this.measureEmpty = true;
    this.measureRest = false;
  }

  public void setBarLength(long barLength) {
    this.barLength = barLength;
  }

  public void setBarStart(long barStart) {
    this.barStart = barStart;
  }

  public void setVoiceStart(int staff, int voice, long start) {
    this.voices[staff][voice] = fixValue(start);
  }
}
