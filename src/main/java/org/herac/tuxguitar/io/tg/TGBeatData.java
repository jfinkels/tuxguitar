package org.herac.tuxguitar.io.tg;

import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGMeasure;

public class TGBeatData {
  private long currentStart;
  private TGVoiceData[] voices = new TGVoiceData[TGBeat.MAX_VOICES];

  public TGBeatData(TGMeasure measure) {
    this.currentStart = measure.getStart();
    for (int i = 0; i < this.voices.length; i++) {
      this.voices[i] = new TGVoiceData(measure);
    }
  }

  public long getCurrentStart() {
    long minimumStart = -1;
    for (int i = 0; i < this.voices.length; i++) {
      if (this.voices[i].getStart() > this.currentStart) {
        if (minimumStart < 0 || this.voices[i].getStart() < minimumStart) {
          minimumStart = this.voices[i].getStart();
        }
      }
    }
    if (minimumStart > this.currentStart) {
      this.currentStart = minimumStart;
    }
    return this.currentStart;
  }

  public TGVoiceData getVoice(int index) {
    return this.voices[index];
  }
}