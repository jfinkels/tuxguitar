package org.herac.tuxguitar.gui.tools.scale;

public class ScaleInfo {
  private String keys;
  private String name;

  public ScaleInfo(String name, String keys) {
    this.name = name;
    this.keys = keys;
  }

  public String getKeys() {
    return this.keys;
  }

  public String getName() {
    return this.name;
  }
}
