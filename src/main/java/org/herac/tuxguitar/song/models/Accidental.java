package org.herac.tuxguitar.song.models;

/**
 * An accidental, that is, either a flat, a natural, or a sharp.
 * 
 * @author Jeffrey Finkelstein
 */
public enum Accidental {
  /** A flat, denoting one half step down from the current note. */
  FLAT(3),
  /** A natural, denoting the current note is to be played as is. */
  NATURAL(1),
  /** A sharp, denoting one half step up from the current note. */
  SHARP(2);

  /** The unique identifying number of this type of accidental. */
  private final int id;

  /**
   * Instantiates this type of accidental with the specified unique identifying
   * number.
   * 
   * @param id
   *          The unique identifying number of this type of accidental.
   */
  private Accidental(final int id) {
    this.id = id;
  }
}
