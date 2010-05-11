/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGFactoryImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGBeat {

  public static final int MAX_VOICES = 2;

  private TGChord chord =null;
  private TGMeasure measure = null;
  private long start = TGDuration.QUARTER_TIME;
  private TGStroke stroke = new TGStroke();
  private TGText text = null;
  private TGVoice[] voices = new TGVoice[MAX_VOICES];

  public TGBeat() {
    for (int i = 0; i < MAX_VOICES; i++) {
      this.setVoice(i, TGFactoryImpl.newVoice(i));
    }
  }

  @Override
  public TGBeat clone() {
    TGBeat beat = new TGBeatImpl();
    beat.setStart(getStart());
    this.stroke.copy(beat.getStroke());
    
    for (int i =0; i < this.voices.length; ++i) {
      beat.setVoice(i, this.voices[i].clone());
    }
    
    if (this.chord != null) {
      beat.setChord(this.chord.clone());
    }
    
    if (this.text != null) {
      beat.setText(this.text.clone());
    }
    return beat;
  }

  public int countVoices() {
    return this.voices.length;
  }

  public TGChord getChord() {
    return this.chord;
  }

  public TGMeasure getMeasure() {
    return this.measure;
  }

  public long getStart() {
    return this.start;
  }

  public TGStroke getStroke() {
    return this.stroke;
  }

  public TGText getText() {
    return this.text;
  }

  public TGVoice getVoice(int index) {
    if (index >= 0 && index < this.voices.length) {
      return this.voices[index];
    }
    return null;
  }

  public boolean isChordBeat() {
    return (this.chord != null);
  }

  public boolean isRestBeat() {
    for (int v = 0; v < this.countVoices(); v++) {
      TGVoice voice = this.getVoice(v);
      if (!voice.isEmpty() && !voice.isRestVoice()) {
        return false;
      }
    }
    return true;
  }

  public boolean isTextBeat() {
    return (this.text != null);
  }

  public void removeChord() {
    this.chord = null;
  }

  public void removeText() {
    this.text = null;
  }

  public void setChord(TGChord chord) {
    this.chord = chord;
    this.chord.setBeat(this);
  }

  public void setMeasure(TGMeasure measure) {
    this.measure = measure;
  }

  public void setStart(long start) {
    this.start = start;
  }

  public void setText(TGText text) {
    this.text = text;
    this.text.setBeat(this);
  }

  public void setVoice(int index, TGVoice voice) {
    if (index >= 0 && index < this.voices.length) {
      this.voices[index] = voice;
      this.voices[index].setBeat(this);
    }
  }
}