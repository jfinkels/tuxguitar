package org.herac.tuxguitar.io.ptb.base;

public class PTBar implements PTComponent {
  private int denominator;
  private int numerator;

  private int repeatClose;
  private boolean repeatStart;

  public PTBar() {
    super();
  }

  public PTComponent getClone() {
    PTBar bar = new PTBar();
    bar.setNumerator(getNumerator());
    bar.setDenominator(getDenominator());
    bar.setRepeatStart(isRepeatStart());
    bar.setRepeatClose(getRepeatClose());
    return bar;
  }

  public int getDenominator() {
    return this.denominator;
  }

  public int getNumerator() {
    return this.numerator;
  }

  public int getRepeatClose() {
    return this.repeatClose;
  }

  public boolean isRepeatStart() {
    return this.repeatStart;
  }

  public void setDenominator(int denominator) {
    this.denominator = denominator;
  }

  public void setNumerator(int numerator) {
    this.numerator = numerator;
  }

  public void setRepeatClose(int repeatClose) {
    this.repeatClose = repeatClose;
  }

  public void setRepeatStart(boolean repeatStart) {
    this.repeatStart = repeatStart;
  }
}