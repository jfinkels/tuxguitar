package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.factory.TGFactory;

public abstract class TGColor {
  public static final TGColor BLACK = newColor(0, 0, 0);
  public static final TGColor BLUE = newColor(0, 0, 255);
  public static final TGColor GREEN = newColor(0, 255, 0);
  public static final TGColor RED = newColor(255, 0, 0);
  public static final TGColor WHITE = newColor(255, 255, 255);

  public static TGColor newColor(int r, int g, int b) {
    TGColor color = new TGFactory().newColor();
    color.setR(r);
    color.setG(g);
    color.setB(b);
    return color;
  }

  private int b;
  private int g;

  private int r;

  public TGColor() {
    this.r = 0;
    this.g = 0;
    this.b = 0;
  }

  public TGColor clone(TGFactory factory) {
    TGColor color = factory.newColor();
    copy(color);
    return color;
  }

  public void copy(TGColor color) {
    color.setR(getR());
    color.setG(getG());
    color.setB(getB());
  }

  public int getB() {
    return this.b;
  }

  public int getG() {
    return this.g;
  }

  public int getR() {
    return this.r;
  }

  public boolean isEqual(TGColor color) {
    return (this.getR() == color.getR() && this.getG() == color.getG() && this
        .getB() == color.getB());
  }

  public void setB(int b) {
    this.b = b;
  }

  public void setG(int g) {
    this.g = g;
  }

  public void setR(int r) {
    this.r = r;
  }

}
