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
public class TGString extends IntegerPair {

  public TGString(final int number, final int value) {
    super(number, value);
  }
  
  @Override
  public TGString clone() {
    return new TGString(this.left, this.right);
  }

  public int getNumber() {
    return this.left;
  }

  public int getValue() {
    return this.right;
  }

}
