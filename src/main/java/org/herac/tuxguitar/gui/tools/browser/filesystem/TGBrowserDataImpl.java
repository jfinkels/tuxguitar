package org.herac.tuxguitar.gui.tools.browser.filesystem;

import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserData;

public class TGBrowserDataImpl implements TGBrowserData {

  private static final String STRING_SEPARATOR = ";";

  public static TGBrowserData fromString(String string) {
    String[] data = string.split(STRING_SEPARATOR);
    if (data.length == 2) {
      return new TGBrowserDataImpl(data[0], data[1]);
    }
    return null;
  }

  private String path;

  private String title;

  public TGBrowserDataImpl(String title, String path) {
    this.title = title;
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }

  public String getTitle() {
    return this.title;
  }

  public String toString() {
    return (getTitle() + STRING_SEPARATOR + getPath());
  }
}
