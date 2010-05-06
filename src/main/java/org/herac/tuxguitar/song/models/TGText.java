package org.herac.tuxguitar.song.models;

import org.herac.tuxguitar.song.factory.TGFactory;

public class TGText {

  private TGBeat beat;
  private String value;

  public TGText() {
    super();
  }

  public TGText clone(TGFactory factory) {
    TGText text = factory.newText();
    copy(text);
    return text;
  }

  public void copy(TGText text) {
    text.setValue(getValue());
  }

  public TGBeat getBeat() {
    return this.beat;
  }

  public String getValue() {
    return this.value;
  }

  public boolean isEmpty() {
    return (this.value == null || this.value.length() == 0);
  }

  public void setBeat(TGBeat beat) {
    this.beat = beat;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
