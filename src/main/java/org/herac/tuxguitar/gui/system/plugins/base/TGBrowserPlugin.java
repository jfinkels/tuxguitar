package org.herac.tuxguitar.gui.system.plugins.base;

import org.herac.tuxguitar.gui.system.plugins.TGPluginException;
import org.herac.tuxguitar.gui.tools.browser.TGBrowserManager;
import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserFactory;

public abstract class TGBrowserPlugin extends TGPluginAdapter {
  private TGBrowserFactory factory;
  private boolean loaded;

  protected void addPlugin() throws TGPluginException {
    if (!this.loaded) {
      TGBrowserManager.instance().addFactory(this.factory);
      this.loaded = true;
    }
  }

  public void close() throws TGPluginException {
    this.loaded = false;
  }

  protected abstract TGBrowserFactory getFactory() throws TGPluginException;

  public void init() throws TGPluginException {
    this.factory = getFactory();
    this.loaded = false;
  }

  protected void removePlugin() throws TGPluginException {
    if (this.loaded) {
      TGBrowserManager.instance().removeFactory(this.factory);
      this.loaded = false;
    }
  }

  public void setEnabled(boolean enabled) throws TGPluginException {
    if (enabled) {
      addPlugin();
    } else {
      removePlugin();
    }
  }
}
