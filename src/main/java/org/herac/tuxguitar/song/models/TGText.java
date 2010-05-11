package org.herac.tuxguitar.song.models;


public class TGText {

  private TGBeat beat = null;
  private String value = null;

  @Override
  public TGText clone() {
    TGText text = new TGText();
    text.setValue(this.value);
    text.setBeat(this.beat);
    return text;
  }

  public TGBeat getBeat() {
    return this.beat;
  }

  public String getValue() {
    return this.value;
  }

  public boolean isEmpty() {
    return (this.value == null || this.value.isEmpty());
  }

  public void setBeat(final TGBeat beat) {
    this.beat = beat;
  }

  public void setValue(final String value) {
    this.value = value;
  }

}
