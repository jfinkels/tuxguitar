package org.herac.tuxguitar.song.helpers;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;

public class TGSongSegment {
  private List<TGMeasureHeader> headers;
  private List<TGTrackSegment> tracks;

  public TGSongSegment() {
    this.headers = new ArrayList<TGMeasureHeader>();
    this.tracks = new ArrayList<TGTrackSegment>();
  }

  public void addTrack(int track, List<TGMeasure> measures) {
    this.tracks.add(new TGTrackSegment(track, measures));
  }

  public TGSongSegment clone(TGFactory factory) {
    TGSongSegment segment = new TGSongSegment();
    for (int i = 0; i < getHeaders().size(); i++) {
      TGMeasureHeader header = (TGMeasureHeader) getHeaders().get(i);
      segment.getHeaders().add(header.clone(factory));
    }
    for (int i = 0; i < getTracks().size(); i++) {
      TGTrackSegment trackMeasure = (TGTrackSegment) getTracks().get(i);
      segment.getTracks()
          .add(trackMeasure.clone(factory, segment.getHeaders()));
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
