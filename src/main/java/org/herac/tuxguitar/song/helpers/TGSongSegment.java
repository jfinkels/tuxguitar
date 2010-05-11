package org.herac.tuxguitar.song.helpers;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;

public class TGSongSegment {
  private List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
  private List<TGTrackSegment> tracks = new ArrayList<TGTrackSegment>();

  public void addTrack(int track, List<TGMeasure> measures) {
    this.tracks.add(new TGTrackSegment(track, measures));
  }

  @Override
  public TGSongSegment clone() {
    TGSongSegment segment = new TGSongSegment();

    for (final TGMeasureHeader header : this.headers) {
      segment.headers.add(header.clone());
    }

    for (final TGTrackSegment trackMeasure : this.tracks) {
      segment.tracks.add(trackMeasure.clone(segment.headers));
    }

    return segment;
  }

  public List<TGMeasureHeader> getHeaders() {
    return this.headers;
  }

  public List<TGTrackSegment> getTracks() {
    return this.tracks;
  }

  public boolean isEmpty() {
    return (this.headers.isEmpty() || this.tracks.isEmpty());
  }
}
