/*
 * Created on 29-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.editors.tab;

import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGTrackImpl extends TGTrack {

  private int scoreHeight;
  private int tabHeight;
  
  /**
   * Calcula el el ancho de la partitura
   */
  public void calculateScoreHeight(ViewLayout layout) {
    this.scoreHeight = ((layout.getStyle() & ViewLayout.DISPLAY_SCORE) != 0 ? (layout
        .getScoreLineSpacing() * 4)
        : 0);
  }

  /**
   * Calcula el el ancho de la tablatura
   */
  public void calculateTabHeight(ViewLayout layout) {
    this.tabHeight = ((layout.getStyle() & ViewLayout.DISPLAY_TABLATURE) != 0 ? ((stringCount() - 1) * layout
        .getStringSpacing())
        : 0);
  }

  public void clear() {
    for (final TGMeasure measure : this.getMeasures())
      if (!((TGMeasureImpl) measure).isDisposed()) {
        ((TGMeasureImpl) measure).dispose();
      }

    super.clear();
  }

  public int getScoreHeight() {
    return this.scoreHeight;
  }

  public int getTabHeight() {
    return this.tabHeight;
  }

  public boolean hasCaret(ViewLayout layout) {
    return (this.equals(layout.getTablature().getCaret().getTrack()));
  }

  public void removeMeasure(int index) {
    TGMeasureImpl measure = (TGMeasureImpl) getMeasure(index);
    if (!measure.isDisposed()) {
      measure.dispose();
    }
    super.removeMeasure(index);
  }

  public void setTabHeight(int tabHeight) {
    this.tabHeight = tabHeight;
  }

  public void update(ViewLayout layout) {
    this.calculateTabHeight(layout);
    this.calculateScoreHeight(layout);
  }
}