package org.herac.tuxguitar.io.lilypond;

import java.io.OutputStream;

import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.models.TGSong;

public class LilypondSongExporter implements TGLocalFileExporter {

  private LilypondSettings settings;
  private OutputStream stream;

  public boolean configure(boolean setDefaults) {
    this.settings = (setDefaults ? LilypondSettings.getDefaults()
        : new LilypondSettingsDialog().open());
    return (this.settings != null);
  }

  public void exportSong(TGSong song) {
    if (this.stream != null && this.settings != null) {
      new LilypondOutputStream(this.stream, this.settings).writeSong(song);
    }
  }

  public String getExportName() {
    return "Lilypond";
  }

  public TGFileFormat getFileFormat() {
    return new TGFileFormat("Lilypond", "*.ly");
  }

  public void init(OutputStream stream) {
    this.stream = stream;
  }
}