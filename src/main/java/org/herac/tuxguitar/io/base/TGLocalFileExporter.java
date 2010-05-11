package org.herac.tuxguitar.io.base;

import java.io.OutputStream;

public interface TGLocalFileExporter extends TGRawExporter {

  public boolean configure(boolean setDefaults);

  public TGFileFormat getFileFormat();

  public void init(OutputStream stream);

}
