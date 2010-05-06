package org.herac.tuxguitar.player.base;

import java.util.List;

public interface MidiSequencerProvider {

  public void closeAll() throws MidiPlayerException;

  public List listSequencers() throws MidiPlayerException;

}
