package org.herac.tuxguitar.gui.editors;

import org.herac.tuxguitar.song.models.TGBeat;

public interface TGExternalBeatViewerListener {

  public void hideExternalBeat();

  public void showExternalBeat(TGBeat beat);
}
