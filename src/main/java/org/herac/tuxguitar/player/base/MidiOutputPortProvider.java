package org.herac.tuxguitar.player.base;

import java.util.List;

public interface MidiOutputPortProvider {

  public void closeAll() throws MidiPlayerException;

  public List<MidiOutputPort> listPorts() throws MidiPlayerException;

}
