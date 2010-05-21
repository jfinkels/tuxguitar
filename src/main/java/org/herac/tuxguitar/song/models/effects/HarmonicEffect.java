package org.herac.tuxguitar.song.models.effects;

public enum HarmonicEffect {
  ARTIFICIAL(0, "A.H"), NATURAL(1, "N.H"), PINCH(2, "P.H"), SEMI(3, "S.H"), TAPPED(
      4, "T.H");

  public static final int MIN_ARTIFICIAL_OFFSET = -24;
  public static final int MAX_ARTIFICIAL_OFFSET = +24;
  
  /** The natural frequencies possible for harmonics on a guitar.
   * 
   * The first integer in each pair is the fret at which the harmonic occurs.
   * 
   * TODO What is the second number in each pair?
   */
  public static final int[][] NATURAL_FREQUENCIES = { 
    { 12, 12 }, // AH12 (+12 frets)
    { 9, 28 }, // AH9 (+28 frets)
    { 5, 24 }, // AH5 (+24 frets)
    { 7, 19 }, // AH7 (+19 frets)
    { 4, 28 }, // AH4 (+28 frets)
    { 3, 31 } // AH3 (+31 frets) 
  };
  
  /** The unique ID of each type of HarmonicEffect. */
  private final int id;
  /** The identifying label of each type of HarmonicEffect. */
  private final String label;
  /**
   * I don't know what this field is.
   * 
   * TODO Change this field so that its name represents what it is.
   */
  private int data;

  /**
   * Instantiate this HarmonicEffect with the specified unique ID and
   * identifying label.
   * 
   * @param id
   *          The unique ID of this type of HarmonicEffect.
   * @param label
   *          The identifying label of this type of HarmonicEffect.
   */
  private HarmonicEffect(final int id, final String label) {
    this.id = id;
    this.label = label;
  }

  /**
   * Gets the unique ID of this type of HarmonicEffect.
   * 
   * @return The unique ID of this type of HarmonicEffect.
   */
  public int getId() {
    return this.id;
  }

  /**
   * Gets the identifying label of this type of HarmonicEffect.
   * 
   * @return The identifying label of this type of HarmonicEffect.
   */
  public String getLabel() {
    return this.label;
  }

  public void setData(final int newData) {
    this.data = newData;
  }

  public int getData() {
    return this.data;
  }

}
