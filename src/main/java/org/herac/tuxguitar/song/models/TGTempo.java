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
public class TGTempo {
  private static final int SECOND_IN_MILLIS = 1000;

  public static TGTempo fromUSQ(int usq) {
    double value = ((60.00 * SECOND_IN_MILLIS) / (usq / 1000.00));
    TGTempo tempo = new TGTempo();
    tempo.setValue((int) value);
    return tempo;
  }

  private int value = 120;
  
  @Override
  public TGTempo clone() {
    TGTempo tempo = new TGTempo();
    copy(tempo);
    return tempo;
  }

  public void copy(TGTempo tempo) {
    tempo.setValue(getValue());
  }

  public long getInMillis() {
    double millis = (60.00 / getValue() * SECOND_IN_MILLIS);
    return (long) millis;
  }

  public long getInUSQ() {
    double usq = ((60.00 / getValue() * SECOND_IN_MILLIS) * 1000.00);
    return (long) usq;
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

}
