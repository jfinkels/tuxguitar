package org.herac.tuxguitar.song.models;

public enum Clef {
  ALTO(3), BASS(1), TENOR(2), TREBLE(0);

  /**
   * Instantiate this Clef with the specified unique ID number.
   * 
   * @param id
   *          The ID of this Clef.
   */
  private Clef(final int id) {
    this.id = id;
  }

  /** The unique ID of this Clef. */
  private int id;

  /**
   * Gets the unique ID of this Clef.
   * 
   * @return The unique ID of this Clef.
   */
  public int getId() {
    return this.id;
  }
}
