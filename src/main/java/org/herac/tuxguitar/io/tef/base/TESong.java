package org.herac.tuxguitar.io.tef.base;

import java.util.ArrayList;
import java.util.List;

public class TESong {

  private TEChord[] chords;
  private List<TEComponent> components;
  private TEInfo info;
  private int measures;
  private TEPercussion[] percussions;
  private TERepeat[] repeats;
  private TERhythm[] rhythms;
  private int strings;
  private TETempo tempo;
  private TEText[] texts;
  private TETimeSignature timeSignature;
  private TETrack[] tracks;
  private List<TETimeSignatureChange> tsChanges;

  public TESong() {
    this.components = new ArrayList<TEComponent>();
    this.tsChanges = new ArrayList<TETimeSignatureChange>();
  }

  public void addTimeSignatureChange(TETimeSignatureChange tsChange) {
    this.tsChanges.add(tsChange);
  }

  public TEChord[] getChords() {
    return this.chords;
  }

  public List<TEComponent> getComponents() {
    return this.components;
  }

  public TEInfo getInfo() {
    return this.info;
  }

  public int getMeasures() {
    return this.measures;
  }

  public TEPercussion[] getPercussions() {
    return this.percussions;
  }

  public TERepeat[] getRepeats() {
    return this.repeats;
  }

  public TERhythm[] getRhythms() {
    return this.rhythms;
  }

  public int getStrings() {
    return this.strings;
  }

  public TETempo getTempo() {
    return this.tempo;
  }

  public TEText[] getTexts() {
    return this.texts;
  }

  public TETimeSignature getTimeSignature() {
    return this.timeSignature;
  }

  public TETimeSignature getTimeSignature(int measure) {
    for (final TETimeSignatureChange change : this.tsChanges){
      if (change.getMeasure() == measure) {
        return change.getTimeSignature();
      }
    }
    return getTimeSignature();
  }

  public TETrack[] getTracks() {
    return this.tracks;
  }

  public void setChord(int index, TEChord chord) {
    this.chords[index] = chord;
  }

  public void setChords(int length) {
    this.chords = new TEChord[length];
  }

  public void setInfo(TEInfo info) {
    this.info = info;
  }

  public void setMeasures(int measures) {
    this.measures = measures;
  }

  public void setPercussion(int index, TEPercussion percussions) {
    this.percussions[index] = percussions;
  }

  public void setPercussions(int length) {
    this.percussions = new TEPercussion[length];
  }

  public void setRepeat(int index, TERepeat repeat) {
    this.repeats[index] = repeat;
  }

  public void setRepeats(int length) {
    this.repeats = new TERepeat[length];
  }

  public void setRhythm(int index, TERhythm rhythm) {
    this.rhythms[index] = rhythm;
  }

  public void setRhythms(int length) {
    this.rhythms = new TERhythm[length];
  }

  public void setStrings(int strings) {
    this.strings = strings;
  }

  public void setTempo(TETempo tempo) {
    this.tempo = tempo;
  }

  public void setText(int index, TEText text) {
    this.texts[index] = text;
  }

  public void setTexts(int length) {
    this.texts = new TEText[length];
  }

  public void setTimeSignature(TETimeSignature timeSignature) {
    this.timeSignature = timeSignature;
  }

  public void setTrack(int index, TETrack track) {
    this.tracks[index] = track;
  }

  public void setTracks(int length) {
    this.tracks = new TETrack[length];
  }

  public String toString() {
    String string = new String("[SONG] *** Tabledit file format ***\n");
    string += (this.getInfo().toString() + "\n");
    string += (this.getTempo().toString() + "\n");
    for (int i = 0; i < this.repeats.length; i++) {
      string += (this.repeats[i].toString() + "\n");
    }
    for (int i = 0; i < this.texts.length; i++) {
      string += (this.texts[i].toString() + "\n");
    }
    for (int i = 0; i < this.chords.length; i++) {
      string += (this.chords[i].toString() + "\n");
    }
    for (int i = 0; i < this.percussions.length; i++) {
      string += (this.percussions[i].toString() + "\n");
    }
    for (int i = 0; i < this.rhythms.length; i++) {
      string += (this.rhythms[i].toString() + "\n");
    }
    for (int i = 0; i < this.tracks.length; i++) {
      string += (this.tracks[i].toString() + "\n");
    }
    for (int i = 0; i < this.components.size(); i++) {
      string += (this.components.get(i).toString() + "\n");
    }
    return string;
  }
}
