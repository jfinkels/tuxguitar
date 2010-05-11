/*
 * Created on 29-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.gui.editors.tab.TGFactoryImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGTimeSignature {
  private TGDuration denominator = TGFactoryImpl.newDuration();
  private int numerator = 4;

  @Override
  public TGTimeSignature clone() {
    TGTimeSignature timeSignature = new TGTimeSignature();
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
