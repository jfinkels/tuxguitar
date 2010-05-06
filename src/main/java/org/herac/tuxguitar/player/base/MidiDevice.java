package org.herac.tuxguitar.player.base;

public interface MidiDevice {

  public void check() throws MidiPlayerException;

  public void close() throws MidiPlayerException;

  public String getKey();

  public String getName();

  public void open() throws MidiPlayerException;
}
