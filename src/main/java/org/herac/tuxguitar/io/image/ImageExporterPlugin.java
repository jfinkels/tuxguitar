package org.herac.tuxguitar.io.image;

import org.herac.tuxguitar.gui.system.plugins.base.TGExporterPlugin;
import org.herac.tuxguitar.io.base.TGRawExporter;

public class ImageExporterPlugin extends TGExporterPlugin {

  public String getAuthor() {
    return "Julian Casadesus <julian@casadesus.com.ar>";
  }

  public String getDescription() {
    return "Image exporter";
  }

  protected TGRawExporter getExporter() {
    return new ImageExporterDialog();
  }

  public String getName() {
    return "Image exporter";
  }

  public String getVersion() {
    return "0.1";
  }
}
