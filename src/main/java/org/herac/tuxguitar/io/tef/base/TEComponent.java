package org.herac.tuxguitar.io.tef.base;

public abstract class TEComponent {

  private int measure;

  private int position;

  private int string;

  public TEComponent(int position, int measure, int string) {
    this.position = position;
    this.measure = measure;
    this.string = string;
  }

  public int getMeasure() {
    return this.measure;
  }

  public int getPosition() {
    return this.position;
  }

  public int getString() {
    return this.string;
  }
}
