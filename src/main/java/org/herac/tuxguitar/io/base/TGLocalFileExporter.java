package org.herac.tuxguitar.io.base;

import java.io.OutputStream;

import org.herac.tuxguitar.song.factory.TGFactory;

public interface TGLocalFileExporter extends TGRawExporter {

  public boolean configure(boolean setDefaults);

  public TGFileFormat getFileFormat();

  public void init(TGFactory factory, OutputStream stream);

}
