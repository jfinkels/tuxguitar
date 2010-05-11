package org.herac.tuxguitar.song.helpers;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;

public class TGTrackSegment {
  private final List<TGMeasure> measures;
  private final int track;

  public TGTrackSegment(final int track, final List<TGMeasure> measures) {
    this.track = track;
    this.measures = measures;
  }

  public TGTrackSegment clone(final List<TGMeasureHeader> headers) {
    final List<TGMeasure> measures = new ArrayList<TGMeasure>();

    for (int i = 0; i < getMeasures().size(); i++) {
      TGMeasure measure = getMeasures().get(i);
      measures.add(measure.clone(headers.get(i)));
    }

    return new TGTrackSegment(getTrack(), measures);
  }

  public List<TGMeasure> getMeasures() {
    return this.measures;
  }

  public int getTrack() {
    return this.track;
  }
}
