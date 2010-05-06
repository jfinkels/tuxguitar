/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGNote {
  private TGNoteEffect effect;
  private int string;
  private boolean tiedNote;
  private int value;
  private int velocity;
  private TGVoice voice;

  public TGNote(TGFactory factory) {
    this.value = 0;
    this.velocity = TGVelocities.DEFAULT;
    this.string = 1;
    this.tiedNote = false;
    this.effect = factory.newEffect();
  }

  public TGNote clone(TGFactory factory) {
    TGNote note = factory.newNote();
    note.setValue(getValue());
    note.setVelocity(getVelocity());
    note.setString(getString());
    note.setTiedNote(isTiedNote());
    note.setEffect(getEffect().clone(factory));
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