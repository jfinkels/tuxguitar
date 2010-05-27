package org.herac.tuxguitar.tray;

import org.herac.tuxguitar.gui.system.plugins.base.TGPluginAdapter;

public class TGTrayPlugin extends TGPluginAdapter {

  private boolean loaded;
  private TGTray tray;

  protected void addPlugin() {
    if (!this.loaded) {
      this.tray.addTray();
      this.loaded = true;
    }
  }

  public void close() {
    this.removePlugin();
  }

  public String getAuthor() {
    return "Julian Casadesus <julian@casadesus.com.ar>";
  }

  public String getDescription() {
    return "System Tray plugin for tuxguitar";
  }

  public String getName() {
    return "System Tray plugin";
  }

  public String getVersion() {
    return "1.0";
  }

  public void init() {
    this.tray = new TGTray();
  }

  protected void removePlugin() {
    if (this.loaded) {
      this.tray.removeTray();
      this.loaded = false;
    }
  }

  public void setEnabled(boolean enabled) {
    if (enabled) {
      this.addPlugin();
    } else {
      this.removePlugin();
    }
  }
}
