/*
 * Created on 29-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGTempo {
  private static final int SECOND_IN_MILLIS = 1000;

  public static final int DEFAULT_VALUE = 120;

  public TGTempo() {
    this(DEFAULT_VALUE);
  }

  public TGTempo(final int value) {
    this.value = value;
  }

  public static TGTempo fromUSQ(int usq) {
    return new TGTempo((int) ((60.00 * SECOND_IN_MILLIS) / (usq / 1000.00)));
  }

  private int value = DEFAULT_VALUE;

  @Override
  public TGTempo clone() {
    return new TGTempo(this.value);
  }

  public long getInMillis() {
    return (long) (60.00 / this.value * SECOND_IN_MILLIS);
  }

  public long getInUSQ() {
    return (long) ((60.00 / this.value * SECOND_IN_MILLIS) * 1000.00);
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
