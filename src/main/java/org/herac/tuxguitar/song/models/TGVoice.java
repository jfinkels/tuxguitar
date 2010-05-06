/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGVoice {

  public static final int DIRECTION_DOWN = 2;
  public static final int DIRECTION_NONE = 0;
  public static final int DIRECTION_UP = 1;

  private TGBeat beat;
  private int direction;
  private TGDuration duration;
  private boolean empty;
  private int index;
  private List notes;

  public TGVoice(TGFactory factory, int index) {
    this.duration = factory.newDuration();
    this.notes = new ArrayList();
    this.index = index;
    this.empty = true;
    this.direction = DIRECTION_NONE;
  }

  public void addNote(TGNote note) {
    note.setVoice(this);
    this.notes.add(note);
    this.setEmpty(false);
  }

  public TGVoice clone(TGFactory factory) {
    TGVoice voice = factory.newVoice(getIndex());
    voice.setEmpty(isEmpty());
    voice.setDirection(getDirection());
    getDuration().copy(voice.getDuration());
    for (int i = 0; i < countNotes(); i++) {
      TGNote note = (TGNote) this.notes.get(i);
      voice.addNote(note.clone(factory));
    }
    return voice;
  }

  public int countNotes() {
    return this.notes.size();
  }

  public TGBeat getBeat() {
    return this.beat;
  }

  public int getDirection() {
    return this.direction;
  }

  public TGDuration getDuration() {
    return this.duration;
  }

  public int getIndex() {
    return this.index;
  }

  public TGNote getNote(int index) {
    if (index >= 0 && index < countNotes()) {
      return (TGNote) this.notes.get(index);
    }
    return null;
  }

  public List getNotes() {
    return this.notes;
  }

  public boolean isEmpty() {
    return this.empty;
  }

  public boolean isRestVoice() {
    return this.notes.isEmpty();
  }

  public void moveNote(int index, TGNote note) {
    getNotes().remove(note);
    getNotes().add(index, note);
  }

  public void removeNote(TGNote note) {
    this.notes.remove(note);
  }

  public void setBeat(TGBeat beat) {
    this.beat = beat;
  }

  public void setDirection(int direction) {
    this.direction = direction;
  }

  public void setDuration(TGDuration duration) {
    this.duration = duration;
  }

  public void setEmpty(boolean empty) {
    this.empty = empty;
  }

  public void setIndex(int index) {
    this.index = index;
  }

}