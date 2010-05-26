package org.herac.tuxguitar.song.models;

public enum StrokeDirection {
  DOWN(-1), NONE(0), UP(1);
  
  private final int id;
  
  private StrokeDirection(final int id) {
    this.id = id;
  }
  public int getId() {
    return this.id;
  }
}
