package org.herac.tuxguitar.gui.printer;

import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.gui.editors.TGPainter;

public interface PrintDocument {

  public void finish();

  public Rectangle getBounds();

  public TGPainter getPainter();

  public boolean isPaintable(int page);

  public void pageFinish();

  public void pageStart();

  public void start();

}
