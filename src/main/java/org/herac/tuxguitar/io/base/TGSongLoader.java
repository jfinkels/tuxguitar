/*
 * Created on 19-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.io.base;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.herac.tuxguitar.song.models.TGSong;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGSongLoader {

  public TGSongLoader() {
    super();
  }

  /**
   * @return TGSong
   * @throws TGFileFormatException
   */
  public TGSong load(InputStream is)
      throws TGFileFormatException {
    try {
      BufferedInputStream stream = new BufferedInputStream(is);
      stream.mark(1);
      for (final TGInputStreamBase reader : TGFileFormatManager.instance()
          .getInputStreams()) {
        reader.init( stream);
        if (reader.isSupportedVersion()) {
          return reader.readSong();
        }
        stream.reset();
      }
      stream.close();
    } catch (Throwable t) {
      throw new TGFileFormatException(t);
    }
    throw new TGFileFormatException("Unsupported file format");
  }
}