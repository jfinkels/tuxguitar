package org.herac.tuxguitar.io.gtp;

public class GTPFileFormat {

  public static final String DEFAULT_TG_CHARSET = "UTF-8";
  public static final String DEFAULT_VERSION_CHARSET = "UTF-8";

  private GTPSettings settings;

  public GTPFileFormat(GTPSettings settings) {
    this.settings = settings;
  }

  protected GTPSettings getSettings() {
    return this.settings;
  }

}
