package org.herac.tuxguitar.io.ptb.helper;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.io.ptb.base.PTTrackInfo;
import org.herac.tuxguitar.song.models.TGTrack;

public class TrackInfoHelper {

  private PTTrackInfo defaultInfo;
  private List<TGTrack> staffTracks = new ArrayList<TGTrack>();

  public TrackInfoHelper() {
    this.staffTracks = new ArrayList<TGTrack>();
  }

  public void addStaffTrack(TGTrack track) {
    this.staffTracks.add(track);
  }

  public int countStaffTracks() {
    return this.staffTracks.size();
  }

  public PTTrackInfo getDefaultInfo() {
    return this.defaultInfo;
  }

  public TGTrack getStaffTrack(int staff) {
    if (staff >= 0 && staff < this.staffTracks.size()) {
      return (TGTrack) this.staffTracks.get(staff);
    }
    return null;
  }

  public void removeStaffTrack(int staff) {
    this.staffTracks.remove(staff);
  }

  public void reset(PTTrackInfo defaultInfo) {
    this.defaultInfo = defaultInfo;
    this.staffTracks.clear();
  }
}
