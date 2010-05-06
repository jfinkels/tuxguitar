package org.herac.tuxguitar.gui.tools.custom.converter;

public class TGConverterFormat {

  private Object exporter;
  private String extension;

  public TGConverterFormat(String extension, Object exporter) {
    this.extension = extension;
    this.exporter = exporter;
  }

  public Object getExporter() {
    return this.exporter;
  }

  public String getExtension() {
    return this.extension;
  }
}
