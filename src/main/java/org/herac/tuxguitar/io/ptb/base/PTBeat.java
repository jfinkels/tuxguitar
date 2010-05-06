package org.herac.tuxguitar.io.ptb.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PTBeat implements PTComponent {

  private boolean arpeggioDown;
  private boolean arpeggioUp;
  private boolean dotted;
  private boolean doubleDotted;
  private int duration;
  private int enters;
  private boolean grace;
  private int multiBarRest;
  private List notes;
  private int staff;
  private int times;
  private boolean vibrato;
  private int voice;

  public PTBeat(int staff, int voice) {
    this.staff = staff;
    this.voice = voice;
    this.notes = new ArrayList();
    this.multiBarRest = 1;
  }

  public void addNote(PTNote note) {
    this.notes.add(note);
  }

  public PTComponent getClone() {
    PTBeat beat = new PTBeat(getStaff(), getVoice());
    beat.setDuration(getDuration());
    beat.setDotted(isDotted());
    beat.setDoubleDotted(isDoubleDotted());
    beat.setTimes(getTimes());
    beat.setEnters(getEnters());
    beat.setMultiBarRest(getMultiBarRest());
    beat.setGrace(isGrace());
    beat.setVibrato(isVibrato());
    beat.setArpeggioUp(isArpeggioUp());
    beat.setArpeggioDown(isArpeggioDown());
    Iterator it = getNotes().iterator();
    while (it.hasNext()) {
      beat.addNote(((PTNote) it.next()).getClone());
    }
    return beat;
  }

  public int getDuration() {
    return this.duration;
  }

  public int getEnters() {
    return this.enters;
  }

  public int getMultiBarRest() {
    return this.multiBarRest;
  }

  public List getNotes() {
    return this.notes;
  }

  public int getStaff() {
    return this.staff;
  }

  public int getTimes() {
    return this.times;
  }

  public int getVoice() {
    return this.voice;
  }

  public boolean isArpeggioDown() {
    return this.arpeggioDown;
  }

  public boolean isArpeggioUp() {
    return this.arpeggioUp;
  }

  public boolean isDotted() {
    return this.dotted;
  }

  public boolean isDoubleDotted() {
    return this.doubleDotted;
  }

  public boolean isGrace() {
    return this.grace;
  }

  public boolean isVibrato() {
    return this.vibrato;
  }

  public void setArpeggioDown(boolean arpeggioDown) {
    this.arpeggioDown = arpeggioDown;
  }

  public void setArpeggioUp(boolean arpeggioUp) {
    this.arpeggioUp = arpeggioUp;
  }

  public void setDotted(boolean dotted) {
    this.dotted = dotted;
  }

  public void setDoubleDotted(boolean doubleDotted) {
    this.doubleDotted = doubleDotted;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public void setEnters(int enters) {
    this.enters = enters;
  }

  public void setGrace(boolean grace) {
    this.grace = grace;
  }

  public void setMultiBarRest(int multiBarRest) {
    this.multiBarRest = multiBarRest;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public void setVibrato(boolean vibrato) {
    this.vibrato = vibrato;
  }
}