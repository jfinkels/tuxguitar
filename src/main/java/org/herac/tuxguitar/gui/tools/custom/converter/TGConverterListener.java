package org.herac.tuxguitar.gui.tools.custom.converter;

public interface TGConverterListener {

  public void notifyFileProcess(String filename);

  public void notifyFileResult(String filename, int errorCode);

  public void notifyFinish();

  public void notifyStart();

}
