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
public class TGTimeSignature extends Pair<Integer, TGDuration> {

  public TGTimeSignature(final int numerator, final TGDuration denominator) {
    super(numerator, denominator);
  }

  public TGTimeSignature() {
    this(DEFAULT_NUMERATOR, new TGDuration());
  }

  public static final int DEFAULT_NUMERATOR = 4;

  @Override
  public TGTimeSignature clone() {
    return new TGTimeSignature(this.left, this.right.clone());
  }

  public TGDuration getDenominator() {
    return this.right;
  }

  public int getNumerator() {
    return this.left;
  }

  public boolean isEqual(final TGTimeSignature ts) {
    return this.left == ts.left && this.right.equals(ts.right);
  }

  public void setDenominator(final TGDuration denominator) {
    this.right = denominator;
  }

  public void setNumerator(final int numerator) {
    this.left = numerator;
  }
}
