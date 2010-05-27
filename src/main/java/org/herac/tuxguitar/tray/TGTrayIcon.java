package org.herac.tuxguitar.tray;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TrayItem;
import org.herac.tuxguitar.gui.util.TGFileUtils;

public class TGTrayIcon {

  public TGTrayIcon(final TrayItem item) {
    this.item = item;
  }
  
  private Image image;
  private final TrayItem item;

  public void dispose() {
    if (this.image != null && !this.image.isDisposed()) {
      this.image.dispose();
    }
  }

  public void loadImage() {
    this.dispose();
    if (this.item != null && !this.item.isDisposed()) {
      this.image = TGFileUtils.loadImage("icon-24x24.png");
      this.item.setImage(this.image);
    }
  }

}
