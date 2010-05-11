package org.herac.tuxguitar.song.models.effects.harmonics;

import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class SemiHarmonic extends TGEffectHarmonic {

  public static final String LABEL = "S.H";
  public static final int ID = 3;

  @Override
  public int getId() {
    return ID;
  }

  @Override
  public String getLabel() {
    return LABEL;
  }
  
  @Override
  public SemiHarmonic clone() {
    return new SemiHarmonic();
  }

}
