/**
 * DurationEffect.java
 */
package org.herac.tuxguitar.song.models.effects;

import org.herac.tuxguitar.song.models.TGDuration;

/**
 * An effect which has a duration.
 * 
 * @author Jeffrey Finkelstein
 */
public abstract class DurationEffect {
  private TGDuration duration = null;

  public void setDuration(final TGDuration newDuration) {
    this.duration = newDuration;
  }

  public TGDuration getDuration() {
    return this.duration;
  }
}
