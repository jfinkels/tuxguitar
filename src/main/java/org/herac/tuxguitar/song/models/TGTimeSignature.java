/*
 * Created on 29-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGTimeSignature {
  private TGDuration denominator;
  private int numerator;

  public TGTimeSignature(TGFactory factory) {
    this.numerator = 4;
    this.denominator = factory.newDuration();
  }

  public TGTimeSignature clone(TGFactory factory) {
    TGTimeSignature timeSignature = factory.newTimeSignature();
    copy(timeSignature);
    return timeSignature;
  }

  public void copy(TGTimeSignature timeSignature) {
    timeSignature.setNumerator(getNumerator());
    getDenominator().copy(timeSignature.getDenominator());
  }

  public TGDuration getDenominator() {
    return this.denominator;
  }

  public int getNumerator() {
    return this.numerator;
  }

  public boolean isEqual(TGTimeSignature ts) {
    return (getNumerator() == ts.getNumerator() && getDenominator().isEqual(
        ts.getDenominator()));
  }

  public void setDenominator(TGDuration denominator) {
    this.denominator = denominator;
  }

  public void setNumerator(int numerator) {
    this.numerator = numerator;
  }
}
