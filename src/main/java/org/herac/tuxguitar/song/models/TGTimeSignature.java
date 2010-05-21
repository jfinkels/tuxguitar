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
public class TGTimeSignature {
  private TGDuration denominator = new TGDuration();
  private int numerator = 4;

  @Override
  public TGTimeSignature clone() {
    TGTimeSignature timeSignature = new TGTimeSignature();

    timeSignature.setNumerator(this.numerator);
    timeSignature.setDenominator(this.denominator.clone());

    return timeSignature;
  }

  public TGDuration getDenominator() {
    return this.denominator;
  }

  public int getNumerator() {
    return this.numerator;
  }

  public boolean isEqual(final TGTimeSignature ts) {
    return this.numerator == ts.numerator
        && this.denominator.equals(ts.denominator);
  }

  public void setDenominator(TGDuration denominator) {
    this.denominator = denominator;
  }

  public void setNumerator(int numerator) {
    this.numerator = numerator;
  }
}
