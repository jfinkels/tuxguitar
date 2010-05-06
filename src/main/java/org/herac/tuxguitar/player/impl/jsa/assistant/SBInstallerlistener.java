package org.herac.tuxguitar.player.impl.jsa.assistant;

public interface SBInstallerlistener {

  public void notifyFailed(Throwable throwable);

  public void notifyFinish();

  public void notifyProcess(String process);

}
