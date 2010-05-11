package org.herac.tuxguitar.song.models;

public class TGScale {
  private int key = 0;

  private final boolean[] notes = new boolean[12];

  public void clear() {
    this.key = 0;
    for (int i = 0; i < this.notes.length; i++) {
      this.notes[i] = false;
    }
  }

  public int getKey() {
    return this.key;
  }

  public boolean getNote(int note) {
    return this.notes[((note + (12 - this.key)) % 12)];
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setNote(int note, boolean on) {
    this.notes[note] = on;
  }

}
