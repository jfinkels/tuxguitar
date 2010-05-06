package org.herac.tuxguitar.gui.tools.custom.tuner;

public interface TGTunerListener {

  public void fireCurrentString(int string);

  public void fireException(Exception ex);

  public void fireFrequency(double freq);

  public int[] getTuning();

}
