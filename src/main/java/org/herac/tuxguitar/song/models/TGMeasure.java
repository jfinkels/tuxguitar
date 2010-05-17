/*
 * Created on 26-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGMeasure {

  public static final Clef DEFAULT_CLEF = Clef.TREBLE;
  public static final int DEFAULT_KEY_SIGNATURE = 0;

  private List<TGBeat> beats = new ArrayList<TGBeat>();
  private Clef clef = DEFAULT_CLEF;
  private TGMeasureHeader header;
  private int keySignature = DEFAULT_KEY_SIGNATURE;

  private TGTrack track;

  public TGMeasure(final TGMeasureHeader header) {
    this.header = header;
  }

  public void addBeat(final TGBeat beat) {
    beat.setMeasure(this);
    this.beats.add(beat);
  }

  public TGMeasure clone(final TGMeasureHeader header) {
    final TGMeasure measure = new TGMeasureImpl(header);
    measure.setClef(this.clef);
    measure.setKeySignature(this.keySignature);
    for (final TGBeat beat : this.beats) {
      measure.addBeat(beat.clone());
    }
    return measure;
  }

  public int countBeats() {
    return this.beats.size();
  }

  public TGBeat getBeat(int index) {
    if (index >= 0 && index < countBeats()) {
      return this.beats.get(index);
    }
    return null;
  }

  public List<TGBeat> getBeats() {
    return this.beats;
  }

  public Clef getClef() {
    return this.clef;
  }

  public TGMeasureHeader getHeader() {
    return this.header;
  }

  public int getKeySignature() {
    return this.keySignature;
  }

  public long getLength() {
    return this.header.getLength();
  }

  public TGMarker getMarker() {
    return this.header.getMarker();
  }

  public int getNumber() {
    return this.header.getNumber();
  }

  public int getRepeatClose() {
    return this.header.getRepeatClose();
  }

  public long getStart() {
    return this.header.getStart();
  }

  public TGTempo getTempo() {
    return this.header.getTempo();
  }

  public TGTimeSignature getTimeSignature() {
    return this.header.getTimeSignature();
  }

  public TGTrack getTrack() {
    return this.track;
  }

  public int getTripletFeel() {
    return this.header.getTripletFeel();
  }

  public boolean hasMarker() {
    return this.header.hasMarker();
  }

  public boolean isRepeatOpen() {
    return this.header.isRepeatOpen();
  }

  public void makeEqual(final TGMeasure measure) {
    this.clef = measure.getClef();
    this.keySignature = measure.getKeySignature();

    this.beats.clear();
    for (final TGBeat beat : measure.getBeats()) {
      this.addBeat(beat);
    }
  }

  public void moveBeat(final int index, final TGBeat beat) {
    this.beats.remove(beat);
    this.beats.add(index, beat);
  }

  public boolean removeBeat(final TGBeat beat) {
    return this.beats.remove(beat);
  }

  public void setClef(final Clef clef) {
    this.clef = clef;
  }

  public void setHeader(final TGMeasureHeader header) {
    this.header = header;
  }

  public void setKeySignature(final int keySignature) {
    this.keySignature = keySignature;
  }

  public void setTrack(final TGTrack track) {
    this.track = track;
  }
}
