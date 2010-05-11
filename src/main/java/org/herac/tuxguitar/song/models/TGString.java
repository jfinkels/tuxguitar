/*
 * Created on 30-nov-2005
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
public class TGString {
  private int number = 0;
  private int value = 0;

  @Override
  public TGString clone() {
    TGString string = new TGString();
    copy(string);
    return string;
  }

  public void copy(TGString string) {
    string.setNumber(getNumber());
    string.setValue(getValue());
  }

  public int getNumber() {
    return this.number;
  }

  public int getValue() {
    return this.value;
  }

  public boolean isEqual(TGString string) {
    return (this.getNumber() == string.getNumber() && this.getValue() == string
        .getValue());
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
