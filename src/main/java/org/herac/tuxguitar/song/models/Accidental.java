package org.herac.tuxguitar.song.models;

public enum Accidental {
  FLAT(3), SHARP(2), NATURAL(1);
  
  private Accidental(final int id) {
    this.id = id;
  }

  private final int id;
}
