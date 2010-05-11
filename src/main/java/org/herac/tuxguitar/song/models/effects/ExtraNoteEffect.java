package org.herac.tuxguitar.song.models.effects;

public abstract class ExtraNoteEffect {

  private int fret = 0;

  public int getFret() {
    return this.fret;
  }

  public void setFret(int fret) {
    this.fret = fret;
  }

}
