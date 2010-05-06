package org.herac.tuxguitar.gui.system.plugins.base;

import org.herac.tuxguitar.gui.system.plugins.TGPluginException;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGInputStreamBase;

public abstract class TGInputStreamPlugin extends TGPluginAdapter {

  private boolean loaded;
  private TGInputStreamBase stream;

  protected void addPlugin() throws TGPluginException {
    if (!this.loaded) {
      TGFileFormatManager.instance().addInputStream(this.stream);
      this.loaded = true;
    }
  }

  public void close() throws TGPluginException {
    this.removePlugin();
  }

  protected abstract TGInputStreamBase getInputStream()
      throws TGPluginException;

  public void init() throws TGPluginException {
    this.stream = getInputStream();
  }

  protected void removePlugin() throws TGPluginException {
    if (this.loaded) {
      TGFileFormatManager.instance().removeInputStream(this.stream);
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
