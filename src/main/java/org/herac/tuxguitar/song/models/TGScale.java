package org.herac.tuxguitar.song.models;

public abstract class TGScale {
  private int key;

  private final boolean[] notes = new boolean[12];

  public TGScale() {
    this.clear();
  }

  public void clear() {
    this.setKey(0);
    for (int i = 0; i < this.notes.length; i++) {
      this.setNote(i, false);
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
