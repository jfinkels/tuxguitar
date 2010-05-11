package org.herac.tuxguitar.io.musicxml;

import java.io.OutputStream;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatException;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.models.TGSong;

public class MusicXMLSongExporter implements TGLocalFileExporter {

  private OutputStream stream;

  public boolean configure(boolean setDefaults) {
    return true;
  }

  public void exportSong(TGSong song) throws TGFileFormatException {
    if (this.stream != null) {
      new MusicXMLWriter(this.stream).writeSong(song);
    }
  }

  public String getExportName() {
    return "MusicXML";
  }

  public TGFileFormat getFileFormat() {
    return new TGFileFormat("MusicXML", "*.xml");
  }

  public void init(OutputStream stream) {
    this.stream = stream;
  }
}