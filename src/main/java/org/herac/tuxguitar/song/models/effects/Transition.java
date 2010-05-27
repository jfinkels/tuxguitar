package org.herac.tuxguitar.song.models.effects;

public enum Transition {
  BEND(2),
  HAMMER(3),
  NONE(0),
  SLIDE(1);
  
  private final int id;
  
  private Transition(final int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
}
