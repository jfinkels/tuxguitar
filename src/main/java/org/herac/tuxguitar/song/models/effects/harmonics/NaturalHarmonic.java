package org.herac.tuxguitar.song.models.effects.harmonics;

import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;

public class NaturalHarmonic extends TGEffectHarmonic {

  public static final String LABEL = "N.H";
  public static final int ID = 1;
  public static final int NATURAL_FREQUENCIES[][] = { { 12, 12 }, // AH12 (+12
      // frets)
      { 9, 28 }, // AH9 (+28 frets)
      { 5, 24 }, // AH5 (+24 frets)
      { 7, 19 }, // AH7 (+19 frets)
      { 4, 28 }, // AH4 (+28 frets)
      { 3, 31 } // AH3 (+31 frets)
  };

  @Override
  public int getId() {
    return ID;
  }

  @Override
  public String getLabel() {
    return LABEL;
  }
  
  @Override
  public NaturalHarmonic clone() {
    return new NaturalHarmonic();
  }

}
