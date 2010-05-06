package org.herac.tuxguitar.player.base;

public interface MidiPlayerListener {

  public void notifyLoop();

  public void notifyStarted();

  public void notifyStopped();
}
