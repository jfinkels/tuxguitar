package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.factory.TGFactory;

public abstract class TGMarker {
  private static final TGColor DEFAULT_COLOR = TGColor.RED;
  private static final String DEFAULT_TITLE = "Untitled";

  private TGColor color;
  private int measure;
  private String title;

  public TGMarker(TGFactory factory) {
    this.measure = 0;
    this.title = DEFAULT_TITLE;
    this.color = DEFAULT_COLOR.clone(factory);
  }

  public TGMarker clone(TGFactory factory) {
    TGMarker marker = factory.newMarker();
    marker.setMeasure(getMeasure());
    marker.setTitle(getTitle());
    getColor().copy(marker.getColor());
    return marker;
  }

  public TGColor getColor() {
    return this.color;
  }

  public int getMeasure() {
    return this.measure;
  }

  public String getTitle() {
    return this.title;
  }

  public void setColor(TGColor color) {
    this.color = color;
  }

  public void setMeasure(int measure) {
    this.measure = measure;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
