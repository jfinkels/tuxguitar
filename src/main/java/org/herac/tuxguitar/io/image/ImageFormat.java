package org.herac.tuxguitar.io.image;

import org.eclipse.swt.SWT;

public class ImageFormat {

  public final static ImageFormat[] IMAGE_FORMATS = new ImageFormat[] {
      new ImageFormat(SWT.IMAGE_PNG, "PNG", ".png"),
      new ImageFormat(SWT.IMAGE_JPEG, "JPEG", ".jpg"),
      new ImageFormat(SWT.IMAGE_BMP, "BMP", ".bmp"), };

  private String extension;
  private int format;
  private String name;

  public ImageFormat(int format, String name, String extension) {
    this.format = format;
    this.name = name;
    this.extension = extension;
  }

  public String getExtension() {
    return this.extension;
  }

  public int getFormat() {
    return this.format;
  }

  public String getName() {
    return this.name;
  }
}
