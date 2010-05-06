package org.herac.tuxguitar.io.gtp;

import org.herac.tuxguitar.song.factory.TGFactory;

public class GTPFileFormat {

  public static final String DEFAULT_TG_CHARSET = "UTF-8";
  public static final String DEFAULT_VERSION_CHARSET = "UTF-8";

  private TGFactory factory;
  private GTPSettings settings;

  public GTPFileFormat(GTPSettings settings) {
    this.settings = settings;
  }

  protected TGFactory getFactory() {
    return this.factory;
  }

  protected GTPSettings getSettings() {
    return this.settings;
  }

  public void init(TGFactory factory) {
    this.factory = factory;
  }
}
