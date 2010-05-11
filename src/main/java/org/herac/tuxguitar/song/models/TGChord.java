/*
 * Created on 29-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGChord {
  private TGBeat beat;
  private int firstFret;
  private String name;
  private int[] strings;

  public TGChord(int length) {
    this.strings = new int[length];
    for (int i = 0; i < this.strings.length; i++) {
      this.strings[i] = -1;
    }
  }

  public void addFretValue(int string, int fret) {
    if (string >= 0 && string < this.strings.length) {
      this.strings[string] = fret;
    }
  }

  @Override
  public TGChord clone() {
    TGChord chord = new TGChordImpl(this.strings.length);
    chord.setName(this.name);
    chord.setFirstFret(this.firstFret);

    System.arraycopy(this.strings, 0, chord.strings, 0, this.strings.length);

    return chord;
  }

  public int countNotes() {
    int count = 0;
    
    for (final int string : this.strings) {
      if (string >= 0) {
        count++;
      }
    }
    
    return count;
  }

  public int countStrings() {
    return this.strings.length;
  }

  public TGBeat getBeat() {
    return this.beat;
  }

  public int getFirstFret() {
    return this.firstFret;
  }

  public int getFretValue(int string) {
    if (string >= 0 && string < this.strings.length) {
      return this.strings[string];
    }
    return -1;
  }

  public String getName() {
    return this.name;
  }

  public int[] getStrings() {
    return this.strings;
  }

  public void setBeat(TGBeat beat) {
    this.beat = beat;
  }

  public void setFirstFret(int firstFret) {
    this.firstFret = firstFret;
  }

  public void setName(String name) {
    this.name = name;
  }

}
