package org.herac.tuxguitar.io.midi.base;

public class MidiEvent {

  private MidiMessage message;
  private long tick;

  public MidiEvent(MidiMessage message, long tick) {
    this.message = message;
    this.tick = tick;
  }

  public MidiMessage getMessage() {
    return this.message;
  }

  public long getTick() {
    return this.tick;
  }
}
