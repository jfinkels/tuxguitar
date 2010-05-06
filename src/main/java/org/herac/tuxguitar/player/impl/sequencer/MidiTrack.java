package org.herac.tuxguitar.player.impl.sequencer;

public class MidiTrack {

  private boolean mute;
  private boolean solo;

  public MidiTrack() {
    this.solo = false;
    this.mute = false;
  }

  public boolean isMute() {
    return this.mute;
  }

  public boolean isSolo() {
    return this.solo;
  }

  public void setMute(boolean mute) {
    this.mute = mute;
  }

  public void setSolo(boolean solo) {
    this.solo = solo;
  }
}
