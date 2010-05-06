package org.herac.tuxguitar.player.impl.jsa.assistant;

import java.net.URL;

public class SBUrl {

  private String name;
  private URL url;

  public SBUrl(URL url, String name) {
    this.url = url;
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public URL getUrl() {
    return this.url;
  }
}
