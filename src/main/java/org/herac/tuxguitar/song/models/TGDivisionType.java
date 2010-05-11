/*
 * Created on 05-dic-2005
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
public class TGDivisionType {
  public static final TGDivisionType NORMAL = newDivisionType(1, 1);

  private static TGDivisionType newDivisionType(int enters, int times) {
    TGDivisionType divisionType = new TGDivisionType();
    divisionType.setEnters(enters);
    divisionType.setTimes(times);
    return divisionType;
  }

  /**
   * Cantidad de Duraciones que entran en los tiempos
   */
  private int enters = 1;

  /**
   * Tiempos
   */
  private int times = 1;

  @Override
  public TGDivisionType clone() {
    TGDivisionType divisionType = new TGDivisionType();
    copy(divisionType);
    return divisionType;
  }

  public long convertTime(long time) {
    return time * this.times / this.enters;
  }

  public void copy(TGDivisionType divisionType) {
    divisionType.setEnters(this.enters);
    divisionType.setTimes(this.times);
  }

  public int getEnters() {
    return this.enters;
  }

  public int getTimes() {
    return this.times;
  }

  public boolean isEqual(TGDivisionType divisionType) {
    return (divisionType.getEnters() == getEnters() && divisionType.getTimes() == getTimes());
  }

  public void setEnters(int enters) {
    this.enters = enters;
  }

  public void setTimes(int times) {
    this.times = times;
  }

}
