/*
 * Created on 05-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

/**
 * Immutable class representing a division?
 * 
 * @author julian
 * @author Jeffrey Finkelstein
 */
@Immutable
public class TGDivisionType {
  public static final TGDivisionType NORMAL = new TGDivisionType(1, 1);
  public static final TGDivisionType DEFAULT = new TGDivisionType(3, 2);

  public TGDivisionType(final int enters, final int times) {
    this.enters = enters;
    this.times = times;
  }

  public static final int DEFAULT_ENTERS = 1;
  public static final int DEFAULT_TIMES = 1;

  /**
   * Cantidad de Duraciones que entran en los tiempos
   */
  private int enters = DEFAULT_ENTERS;

  /**
   * Tiempos
   */
  private int times = DEFAULT_TIMES;

  public long convertTime(long time) {
    return time * this.times / this.enters;
  }

  public int getEnters() {
    return this.enters;
  }

  public int getTimes() {
    return this.times;
  }

  public boolean isEqual(final TGDivisionType other) {
    return (other.enters == this.enters && other.times == this.times);
  }

}
