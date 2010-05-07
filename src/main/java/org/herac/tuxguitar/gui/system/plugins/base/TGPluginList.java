package org.herac.tuxguitar.gui.system.plugins.base;

import java.util.List;

import org.herac.tuxguitar.gui.system.plugins.TGPlugin;
import org.herac.tuxguitar.gui.system.plugins.TGPluginException;

public abstract class TGPluginList extends TGPluginAdapter {

  public void close() throws TGPluginException {
    for (final TGPlugin plugin : this.getPlugins()) {
      plugin.close();
    }
  }

  protected abstract List<TGPlugin> getPlugins() throws TGPluginException;

  public void init() throws TGPluginException {
    for (final TGPlugin plugin : this.getPlugins()) {
      plugin.init();
    }
  }

  public void setEnabled(boolean enabled) throws TGPluginException {
    for (final TGPlugin plugin : this.getPlugins()) {
      plugin.setEnabled(enabled);
    }
  }
}
