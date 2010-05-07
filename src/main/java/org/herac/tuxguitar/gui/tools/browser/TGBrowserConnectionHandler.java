package org.herac.tuxguitar.gui.tools.browser;

import java.io.InputStream;
import java.util.List;

import org.herac.tuxguitar.gui.tools.browser.base.TGBrowserElement;

public interface TGBrowserConnectionHandler {

  public void notifyCd(int callId);

  public void notifyClosed(int callId);

  public void notifyElements(int callId, List<TGBrowserElement> elements);

  public void notifyError(int callId, Throwable throwable);

  public void notifyLockStatusChanged();

  public void notifyOpened(int callId);

  public void notifyStream(int callId, InputStream stream,
      TGBrowserElement element);
}
