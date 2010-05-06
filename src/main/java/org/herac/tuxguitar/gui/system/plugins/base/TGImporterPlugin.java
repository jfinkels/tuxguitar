package org.herac.tuxguitar.gui.system.plugins.base;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.system.plugins.TGPluginException;
import org.herac.tuxguitar.io.base.TGFileFormatManager;
import org.herac.tuxguitar.io.base.TGRawImporter;

public abstract class TGImporterPlugin extends TGPluginAdapter {

  private TGRawImporter importer;
  private boolean loaded;

  protected void addPlugin() throws TGPluginException {
    if (!this.loaded) {
      TGFileFormatManager.instance().addImporter(this.importer);
      TuxGuitar.instance().getItemManager().createMenu();
      this.loaded = true;
    }
  }

  public void close() throws TGPluginException {
    this.removePlugin();
  }

  protected abstract TGRawImporter getImporter() throws TGPluginException;

  public void init() throws TGPluginException {
    this.importer = getImporter();
  }

  protected void removePlugin() throws TGPluginException {
    if (this.loaded) {
      TGFileFormatManager.instance().removeImporter(this.importer);
      TuxGuitar.instance().getItemManager().createMenu();
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
