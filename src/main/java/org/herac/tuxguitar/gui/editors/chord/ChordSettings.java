package org.herac.tuxguitar.gui.editors.chord;

public class ChordSettings {

  private static ChordSettings instance;

  public static ChordSettings instance() {
    if (instance == null) {
      instance = new ChordSettings();
    }
    return instance;
  }

  private float bassGrade;
  private int chordsToDisplay;
  private int chordTypeIndex;
  private boolean emptyStringChords;
  private int findChordsMax;
  private int findChordsMin;
  private float fingeringGrade;
  private float goodChordSemanticsGrade;
  private int howManyIncompleteChords;
  private float manyStringsGrade;
  private float requiredBasicsGrade;

  private float subsequentGrade;

  private ChordSettings() {
    this.emptyStringChords = false;
    this.bassGrade = 200.0f;
    this.fingeringGrade = 150.0f; // was:200
    this.subsequentGrade = 200.0f;
    this.requiredBasicsGrade = 150.0f;
    this.manyStringsGrade = 100.0f;
    this.goodChordSemanticsGrade = 200.0f;
    this.chordsToDisplay = 30;
    this.howManyIncompleteChords = 4;
    this.chordTypeIndex = 0;
    this.findChordsMin = 0;
    this.findChordsMax = 15;
  }

  public float getBassGrade() {
    return this.bassGrade;
  }

  public int getChordsToDisplay() {
    return this.chordsToDisplay;
  }

  public int getChordTypeIndex() {
    return this.chordTypeIndex;
  }

  public int getFindChordsMax() {
    return this.findChordsMax;
  }

  public int getFindChordsMin() {
    return this.findChordsMin;
  }

  public float getFingeringGrade() {
    return this.fingeringGrade;
  }

  public float getGoodChordSemanticsGrade() {
    return this.goodChordSemanticsGrade;
  }

  public int getIncompleteChords() {
    return this.howManyIncompleteChords;
  }

  public float getManyStringsGrade() {
    return this.manyStringsGrade;
  }

  public float getRequiredBasicsGrade() {
    return this.requiredBasicsGrade;
  }

  public float getSubsequentGrade() {
    return this.subsequentGrade;
  }

  public boolean isEmptyStringChords() {
    return this.emptyStringChords;
  }

  public void setBassGrade(float bassGrade) {
    this.bassGrade = bassGrade;
  }

  public void setChordsToDisplay(int chordsToDisplay) {
    this.chordsToDisplay = chordsToDisplay;
  }

  public void setChordTypeIndex(int index) {
    switch (index) {
    case 0: // normal
      this.bassGrade = 200.0f;
      this.fingeringGrade = 150.0f;
      this.subsequentGrade = 200.0f;
      this.requiredBasicsGrade = 150.0f;
      this.manyStringsGrade = 100.0f;
      this.goodChordSemanticsGrade = 200.0f;
      break;
    case 1: // inversions
      this.bassGrade = -100.0f;
      this.fingeringGrade = 150.0f;
      this.subsequentGrade = 200.0f;
      this.requiredBasicsGrade = 150.0f;
      this.manyStringsGrade = 50.0f;
      this.goodChordSemanticsGrade = 200.0f;
      break;
    case 2: // close-voiced
      this.bassGrade = 50.0f;
      this.fingeringGrade = 200.0f;
      this.subsequentGrade = 350.0f;
      this.requiredBasicsGrade = 150.0f;
      this.manyStringsGrade = -100.0f;
      this.goodChordSemanticsGrade = 200.0f;
      break;
    case 3: // open-voiced
      this.bassGrade = 100.0f;
      this.fingeringGrade = 100.0f;
      this.subsequentGrade = -80.0f;
      this.requiredBasicsGrade = 100.0f;
      this.manyStringsGrade = -80.0f;
      this.goodChordSemanticsGrade = 200.0f;
      break;
    }
    this.chordTypeIndex = index;
  }

  public void setEmptyStringChords(boolean emptyStringChords) {
    this.emptyStringChords = emptyStringChords;
  }

  public void setFindChordsMax(int max) {
    this.findChordsMax = max;
  }

  public void setFindChordsMin(int min) {
    this.findChordsMin = min;
  }

  public void setFingeringGrade(float fingeringGrade) {
    this.fingeringGrade = fingeringGrade;
  }

  public void setGoodChordSemanticsGrade(float goodChordSemanticsGrade) {
    this.goodChordSemanticsGrade = goodChordSemanticsGrade;
  }

  public void setIncompleteChords(int incomplete) {
    this.howManyIncompleteChords = incomplete;
  }

  public void setManyStringsGrade(float manyStringsGrade) {
    this.manyStringsGrade = manyStringsGrade;
  }

  public void setRequiredBasicsGrade(float requiredBasicsGrade) {
    this.requiredBasicsGrade = requiredBasicsGrade;
  }

  public void setSubsequentGrade(float subsequentGrade) {
    this.subsequentGrade = subsequentGrade;
  }
}
