/*
 * Created on 05-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

/**
 * Immutable class representing a division?
 * 
 * @author julian
 * @author Jeffrey Finkelstein
 */
public class TGDivisionType extends IntegerPair {
  public static final TGDivisionType NORMAL = new TGDivisionType(1, 1);
  public static final TGDivisionType DEFAULT = new TGDivisionType(3, 2);

  public TGDivisionType(final int enters, final int times) {
    super(enters, times);
  }

  public long convertTime(long time) {
    return time * this.getTimes() / this.getEnters();
  }

  public int getEnters() {
    return this.left;
  }

  public int getTimes() {
    return this.right;
  }

}
