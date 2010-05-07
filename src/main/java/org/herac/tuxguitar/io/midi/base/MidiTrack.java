package org.herac.tuxguitar.io.midi.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MidiTrack {

  private List<MidiEvent> events = new ArrayList<MidiEvent>();
  private long ticks;

  public void add(MidiEvent event) {
    this.events.add(event);
    this.ticks = Math.max(this.ticks, event.getTick());
  }

  public MidiEvent get(int index) {
    return (MidiEvent) this.events.get(index);
  }

  public int size() {
    return this.events.size();
  }

  public void sort() {
    final Comparator<MidiEvent> comparator = new Comparator<MidiEvent>() {
      public int compare(MidiEvent e1, MidiEvent e2) {
        return (int) (e1.getTick() - e2.getTick());
      }
    };
    Collections.sort(this.events, comparator);
  }

  public long ticks() {
    return this.ticks;
  }
}
