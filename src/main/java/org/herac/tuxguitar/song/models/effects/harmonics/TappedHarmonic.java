package org.herac.tuxguitar.song.models.effects.harmonics;

import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class TappedHarmonic extends TGEffectHarmonic {

  public static final int MAX_OFFSET = 24;

  public static final String LABEL = "T.H";
  public static final int ID = 4;

  @Override
  public int getId() {
    return ID;
  }

  @Override
  public String getLabel() {
    return LABEL;
  }
  
  @Override
  public TappedHarmonic clone() {
    return new TappedHarmonic();
  }

}
