/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGNote {
  private TGNoteEffect effect = new TGNoteEffect();
  private int string = 1;
  private boolean tiedNote = false;
  private int value = 0;
  private int velocity = TGVelocities.DEFAULT;
  private TGVoice voice = null;

  @Override
  public TGNote clone() {
    TGNote note = new TGNoteImpl();
    note.setValue(this.value);
    note.setVelocity(this.velocity);
    note.setString(this.string);
    note.setTiedNote(this.tiedNote);
    note.setEffect(this.effect.clone());
    return note;
  }

  public TGNoteEffect getEffect() {
    return this.effect;
  }

  public int getString() {
    return this.string;
  }

  public int getValue() {
    return this.value;
  }

  public int getVelocity() {
    return this.velocity;
  }

  public TGVoice getVoice() {
    return this.voice;
  }

  public boolean isTiedNote() {
    return this.tiedNote;
  }

  public void setEffect(TGNoteEffect effect) {
    this.effect = effect;
  }

  public void setString(int string) {
    this.string = string;
  }

  public void setTiedNote(boolean tiedNote) {
    this.tiedNote = tiedNote;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setVelocity(int velocity) {
    this.velocity = velocity;
  }

  public void setVoice(TGVoice voice) {
    this.voice = voice;
  }
}