package org.herac.tuxguitar.song.models;

import java.awt.Color;

public class TGMarker {
  private static final Color DEFAULT_COLOR = Color.RED;
  private static final String DEFAULT_TITLE = "Untitled";

  private Color color = DEFAULT_COLOR;
  private int measure = 0;
  private String title = DEFAULT_TITLE;

  @Override
  public TGMarker clone() {
    TGMarker marker = new TGMarker();
    marker.setMeasure(getMeasure());
    marker.setTitle(getTitle());
    marker.setColor(this.color);
    return marker;
  }

  public Color getColor() {
    return this.color;
  }

  public int getMeasure() {
    return this.measure;
  }

  public String getTitle() {
    return this.title;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setMeasure(int measure) {
    this.measure = measure;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
