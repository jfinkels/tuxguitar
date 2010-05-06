package org.herac.tuxguitar.community;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.community.browser.TGBrowserPluginImpl;
import org.herac.tuxguitar.community.io.TGShareSongPlugin;
import org.herac.tuxguitar.community.startup.TGCommunityStartupPlugin;
import org.herac.tuxguitar.gui.system.plugins.TGPlugin;
import org.herac.tuxguitar.gui.system.plugins.TGPluginException;
import org.herac.tuxguitar.gui.system.plugins.base.TGPluginList;

public class TGCommunityPluginImpl extends TGPluginList {

  public void close() throws TGPluginException {
    TGCommunitySingleton.getInstance().saveSettings();
    super.close();
  }

  public String getAuthor() {
    return "Julian Casadesus <julian@casadesus.com.ar>";
  }

  public String getDescription() {
    return "TuxGuitar Community Integration";
  }

  public String getName() {
    return "TuxGuitar Community Integration";
  }

  protected List<TGPlugin> getPlugins() throws TGPluginException {
    List<TGPlugin> plugins = new ArrayList<TGPlugin>();

    plugins.add(new TGShareSongPlugin());
    plugins.add(new TGBrowserPluginImpl());
    plugins.add(new TGCommunityStartupPlugin());

    return plugins;
  }

  public String getVersion() {
    return "1.2";
  }

  public void init() throws TGPluginException {
    TGCommunitySingleton.getInstance().loadSettings();
    super.init();
  }
}
