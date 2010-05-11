package org.herac.tuxguitar.io.base;

import java.io.IOException;
import java.io.OutputStream;

import org.herac.tuxguitar.song.models.TGSong;

public interface TGOutputStreamBase {

  public TGFileFormat getFileFormat();

  public void init(OutputStream stream);

  public boolean isSupportedExtension(String extension);

  public void writeSong(TGSong song) throws TGFileFormatException, IOException;
}
