package org.herac.tuxguitar.gui.tools.browser.base;

import org.eclipse.swt.widgets.Shell;

public interface TGBrowserFactory {

  public TGBrowserData dataDialog(Shell parent);

  public String getName();

  public String getType();

  public TGBrowser newTGBrowser(TGBrowserData data);

  public TGBrowserData parseData(String string);
}
