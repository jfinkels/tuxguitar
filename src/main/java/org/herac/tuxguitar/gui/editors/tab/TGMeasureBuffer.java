package org.herac.tuxguitar.gui.editors.tab;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.herac.tuxguitar.gui.editors.TGPainter;

public class TGMeasureBuffer {

  private Image buffer;

  private Device device;

  private int height;

  private TGPainter painter;

  private int width;

  public TGMeasureBuffer(Device device) {
    this.device = device;
  }

  public void createBuffer(int width, int height, Color background) {
    this.dispose();
    this.buffer = new Image(this.device, width, height);
    this.width = width;
    this.height = height;
    this.fillBuffer(background);
  }

  public void createPainter() {
    this.disposePainter();
    this.painter = new TGPainter(this.buffer);
  }

  public void dispose() {
    this.disposePainter();
    this.disposeBuffer();
  }

  public void disposeBuffer() {
    if (this.buffer != null && !this.buffer.isDisposed()) {
      this.buffer.dispose();
    }
  }

  public void disposePainter() {
    if (this.painter != null && !this.painter.getGC().isDisposed()) {
      this.painter.dispose();
      this.painter = null;
    }
  }

  private void fillBuffer(Color background) {
    getPainter().setBackground(background);
    getPainter().initPath(TGPainter.PATH_FILL);
    getPainter().addRectangle(0, 0, this.width, this.height);
    getPainter().closePath();
  }

  public Image getImage() {
    return this.buffer;
  }

  public TGPainter getPainter() {
    if (this.painter == null || this.painter.getGC().isDisposed()) {
      this.createPainter();
    }
    return this.painter;
  }

  public boolean isDisposed() {
    return (this.buffer == null || this.buffer.isDisposed());
  }

  public void paintBuffer(TGPainter painter, int x, int y, int srcY) {
    painter.drawImage(this.buffer, 0, srcY, this.width, (this.height - srcY),
        x, (y + srcY), this.width, (this.height - srcY));
  }
}
