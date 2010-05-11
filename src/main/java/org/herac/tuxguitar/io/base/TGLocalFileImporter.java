package org.herac.tuxguitar.io.base;

import java.io.InputStream;

public interface TGLocalFileImporter extends TGRawImporter {

  public boolean configure(boolean setDefaults);

  public TGFileFormat getFileFormat();

  public void init(InputStream stream);

}
