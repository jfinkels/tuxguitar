/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGFactoryImpl;
import org.herac.tuxguitar.gui.editors.tab.TGVoiceImpl;

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

  private TGBeat beat = null;
  private int direction = DIRECTION_NONE;
  private TGDuration duration = TGFactoryImpl.newDuration();
  private boolean empty = true;
  private int index = 0;
  private List<TGNote> notes = new ArrayList<TGNote>();

  public TGVoice(int index) {
    this.index = index;
  }

  public void addNote(TGNote note) {
    note.setVoice(this);
    this.notes.add(note);
    this.setEmpty(false);
  }

  @Override
  public TGVoice clone() {
    TGVoice voice = new TGVoiceImpl(this.index);
    
    voice.setEmpty(isEmpty());
    voice.setDirection(getDirection());
    getDuration().copy(voice.getDuration());
    for (final TGNote note : this.notes) {
      voice.addNote(note.clone());
    }
    return voice;
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
    if (index >= 0 && index < this.notes.size()) {
      return this.notes.get(index);
    }
    return null;
  }

  public List<TGNote> getNotes() {
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