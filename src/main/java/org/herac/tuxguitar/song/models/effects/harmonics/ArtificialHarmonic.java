package org.herac.tuxguitar.song.models.effects.harmonics;

import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class ArtificialHarmonic extends TGEffectHarmonic {
  public static final int MAX_OFFSET = 24;
  public static final int MIN_OFFSET = -24;

  public static final String LABEL = "A.H";
  public static final int ID = 0;

  @Override
  public int getId() {
    return ID;
  }

  @Override
  public String getLabel() {
    return LABEL;
  }

  @Override
  public ArtificialHarmonic clone() {
    return new ArtificialHarmonic();
  }
}
