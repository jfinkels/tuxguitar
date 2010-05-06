package org.herac.tuxguitar.io.ptb.base;

public class PTSymbol implements PTComponent {

  private int endNumber;

  public PTSymbol() {
    super();
  }

  public PTComponent getClone() {
    PTSymbol symbol = new PTSymbol();
    symbol.setEndNumber(getEndNumber());
    return symbol;
  }

  public int getEndNumber() {
    return this.endNumber;
  }

  public void setEndNumber(int endNumber) {
    this.endNumber = endNumber;
  }
}
