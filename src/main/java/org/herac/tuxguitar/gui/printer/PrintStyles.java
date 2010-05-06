package org.herac.tuxguitar.gui.printer;

import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;

public class PrintStyles {

  private boolean blackAndWhite;

  private int fromMeasure;

  private int style;

  private int toMeasure;

  private int trackNumber;

  public PrintStyles() {
    this(-1, -1, -1, ViewLayout.DISPLAY_TABLATURE, true);
  }

  public PrintStyles(int trackNumber, int fromMeasure, int toMeasure,
      int style, boolean blackAndWhite) {
    this.trackNumber = trackNumber;
    this.fromMeasure = fromMeasure;
    this.toMeasure = toMeasure;
    this.style = style;
    this.blackAndWhite = blackAndWhite;
  }

  public int getFromMeasure() {
    return this.fromMeasure;
  }

  public int getStyle() {
    return this.style;
  }

  public int getToMeasure() {
    return this.toMeasure;
  }

  public int getTrackNumber() {
    return this.trackNumber;
  }

  public boolean isBlackAndWhite() {
    return this.blackAndWhite;
  }

  public void setBlackAndWhite(boolean blackAndWhite) {
    this.blackAndWhite = blackAndWhite;
  }

  public void setFromMeasure(int fromMeasure) {
    this.fromMeasure = fromMeasure;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public void setToMeasure(int toMeasure) {
    this.toMeasure = toMeasure;
  }

  public void setTrackNumber(int trackNumber) {
    this.trackNumber = trackNumber;
  }
}
