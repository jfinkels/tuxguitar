package org.herac.tuxguitar.song.models.effects.harmonics;

import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class PinchHarmonic extends TGEffectHarmonic {

  public static final String LABEL = "P.H";
  public static final int ID = 2;

  @Override
  public int getId() {
    return ID;
  }

  @Override
  public String getLabel() {
    return LABEL;
  }
  @Override
  public PinchHarmonic clone() {
    return new PinchHarmonic();
  }

}
