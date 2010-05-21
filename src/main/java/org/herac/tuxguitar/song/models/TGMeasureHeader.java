/*
 * Created on 26-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGMeasureHeader {
  public static final int TRIPLET_FEEL_EIGHTH = 2;
  public static final int TRIPLET_FEEL_NONE = 1;
  public static final int TRIPLET_FEEL_SIXTEENTH = 3;

  private TGMarker marker = null;
  private int number = 0;
  private int repeatAlternative = 0;
  private int repeatClose = 0;
  private boolean repeatOpen = false;
  private TGSong song = null;
  private long start = TGDuration.QUARTER_TIME;
  private TGTempo tempo = new TGTempo();
  private TGTimeSignature timeSignature = new TGTimeSignature();
  private int tripletFeel = TRIPLET_FEEL_NONE;

  public TGMeasureHeader() {
    this.checkMarker();
  }

  private void checkMarker() {
    if (hasMarker()) {
      this.marker.setMeasure(getNumber());
    }
  }

  @Override
  public TGMeasureHeader clone() {
    TGMeasureHeader header = new TGMeasureHeaderImpl();
    header.setNumber(this.number);
    header.setStart(this.start);
    header.setRepeatOpen(this.repeatOpen);
    header.setRepeatAlternative(this.repeatAlternative);
    header.setRepeatClose(this.repeatClose);
    header.setTripletFeel(this.tripletFeel);
    header.setTimeSignature(this.timeSignature.clone());
    header.setTempo(this.tempo.clone());
    header.setMarker(this.hasMarker() ? this.marker.clone() : null);
    return header;
  }

  public long getLength() {
    return getTimeSignature().getNumerator()
        * getTimeSignature().getDenominator().getTime();
  }

  public TGMarker getMarker() {
    return this.marker;
  }

  public int getNumber() {
    return this.number;
  }

  public int getRepeatAlternative() {
    return this.repeatAlternative;
  }

  public int getRepeatClose() {
    return this.repeatClose;
  }

  public TGSong getSong() {
    return this.song;
  }

  public long getStart() {
    return this.start;
  }

  public TGTempo getTempo() {
    return this.tempo;
  }

  public TGTimeSignature getTimeSignature() {
    return this.timeSignature;
  }

  public int getTripletFeel() {
    return this.tripletFeel;
  }

  public boolean hasMarker() {
    return (getMarker() != null);
  }

  public boolean isRepeatOpen() {
    return this.repeatOpen;
  }

  public void makeEqual(TGMeasureHeader measure) {
    this.start = measure.getStart();
    this.timeSignature = measure.getTimeSignature();
    this.tempo = measure.getTempo();
    this.marker = measure.getMarker();
    this.repeatOpen = measure.isRepeatOpen();
    this.repeatClose = measure.getRepeatClose();
    this.repeatAlternative = measure.getRepeatAlternative();
    this.checkMarker();
  }

  public void setMarker(TGMarker marker) {
    this.marker = marker;
  }

  public void setNumber(int number) {
    this.number = number;
    this.checkMarker();
  }

  /**
   * bitwise value 1 TO 8. (1 << AlternativeNumber)
   */
  public void setRepeatAlternative(int repeatAlternative) {
    this.repeatAlternative = repeatAlternative;
  }

  public void setRepeatClose(int repeatClose) {
    this.repeatClose = repeatClose;
  }

  public void setRepeatOpen(boolean repeatOpen) {
    this.repeatOpen = repeatOpen;
  }

  public void setSong(TGSong song) {
    this.song = song;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public void setTempo(TGTempo tempo) {
    this.tempo = tempo;
  }

  public void setTimeSignature(TGTimeSignature timeSignature) {
    this.timeSignature = timeSignature;
  }

  public void setTripletFeel(int tripletFeel) {
    this.tripletFeel = tripletFeel;
  }
}
