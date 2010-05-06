package org.herac.tuxguitar.io.ptb.base;

public class PTNote {
  private int bend;
  private boolean dead;
  private boolean hammer;
  private boolean slide;
  private int string;
  private boolean tied;
  private int value;

  public PTNote() {
    super();
  }

  public int getBend() {
    return this.bend;
  }

  public PTNote getClone() {
    PTNote note = new PTNote();
    note.setValue(getValue());
    note.setString(getString());
    note.setTied(isTied());
    note.setDead(isDead());
    note.setBend(getBend());
    note.setHammer(isHammer());
    note.setSlide(isSlide());
    return note;
  }

  public int getString() {
    return this.string;
  }

  public int getValue() {
    return this.value;
  }

  public boolean isDead() {
    return this.dead;
  }

  public boolean isHammer() {
    return this.hammer;
  }

  public boolean isSlide() {
    return this.slide;
  }

  public boolean isTied() {
    return this.tied;
  }

  public void setBend(int bend) {
    this.bend = bend;
  }

  public void setDead(boolean dead) {
    this.dead = dead;
  }

  public void setHammer(boolean hammer) {
    this.hammer = hammer;
  }

  public void setSlide(boolean slide) {
    this.slide = slide;
  }

  public void setString(int string) {
    this.string = string;
  }

  public void setTied(boolean tied) {
    this.tied = tied;
  }

  public void setValue(int value) {
    this.value = value;
  }
}