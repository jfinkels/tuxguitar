package org.herac.tuxguitar.song.models;

/**
 * A vertical direction: down, up, or none.
 * 
 * @author Jeffrey Finkelstein
 */
public enum Direction {
  /** The down direction. */
  DOWN(-1),
  /** No direction. */
  NONE(0),
  /** The up direction. */
  UP(1);

  /** The identifying number of this direction. */
  private final int id;

  /**
   * Instantiates this direction with the specified identifying number.
   * 
   * @param id
   *          The identifying number of this direction.
   */
  private Direction(final int id) {
    this.id = id;
  }

  /**
   * Gets the unique identifying number of this direction.
   * 
   * @return The unique identifying number of this direction.
   */
  public int getId() {
    return this.id;
  }
}
