package org.herac.tuxguitar.io.tef.base;

public class TETimeSignature {

  private int denominator;
  private int numerator;

  public TETimeSignature(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public int getDenominator() {
    return this.denominator;
  }

  public int getNumerator() {
    return this.numerator;
  }
}
