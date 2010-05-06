package org.herac.tuxguitar.gui.tools.custom;

public class TGCustomTool {

  private String action;
  private String name;

  public TGCustomTool(String name, String action) {
    super();
    this.name = name;
    this.action = action;
  }

  public String getAction() {
    return this.action;
  }

  public String getName() {
    return this.name;
  }

}
