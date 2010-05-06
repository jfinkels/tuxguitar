package org.herac.tuxguitar.gui.system.plugins;

public interface TGPlugin {

  public void close() throws TGPluginException;

  public String getAuthor();

  public String getDescription();

  public String getName();

  public String getVersion();

  public void init() throws TGPluginException;

  public void setEnabled(boolean enabled) throws TGPluginException;

}
