package org.herac.tuxguitar.player.base;

import java.util.List;

public interface MidiSequencerProvider {

  public void closeAll() throws MidiPlayerException;

  public List<MidiSequencer> listSequencers() throws MidiPlayerException;

}
