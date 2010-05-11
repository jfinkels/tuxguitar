/*
 * Created on 26-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models.effects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class BendingEffect {

  private List<EffectPoint> points = new ArrayList<EffectPoint>();

  public void addPoint(int position, int value) {
    this.points.add(new EffectPoint(position, value));
  }

  @Override
  public BendingEffect clone() {
    BendingEffect effect = new BendingEffect();
    for (final EffectPoint point : this.points) {
      effect.addPoint(point.getPosition(), point.getValue());
    }
    return effect;
  }

  public List<EffectPoint> getPoints() {
    return this.points;
  }

}