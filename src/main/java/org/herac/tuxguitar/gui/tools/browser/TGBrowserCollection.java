package org.herac.tuxguitar.gui.tools.browser;

import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserData;

public class TGBrowserCollection {

  private TGBrowserData data;
  private String type;

  public TGBrowserCollection() {
    super();
  }

  public TGBrowserData getData() {
    return this.data;
  }

  public String getType() {
    return this.type;
  }

  public void setData(TGBrowserData data) {
    this.data = data;
  }

  public void setType(String type) {
    this.type = type;
  }

}
