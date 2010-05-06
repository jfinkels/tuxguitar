package org.herac.tuxguitar.player.impl.sequencer;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiPlayerException;

public class MidiTrackController {

  private boolean anySolo;
  private MidiSequencerImpl sequencer;
  private List tracks;

  public MidiTrackController(MidiSequencerImpl sequencer) {
    this.sequencer = sequencer;
    this.tracks = new ArrayList();
    this.anySolo = false;
  }

  public void checkAnySolo() {
    this.anySolo = false;
    for (int i = 0; i < this.tracks.size(); i++) {
      MidiTrack track = (MidiTrack) this.tracks.get(i);
      if (track.isSolo()) {
        this.anySolo = true;
        break;
      }
    }
  }

  public void clearTracks() {
    this.tracks.clear();
  }

  public void init(int count) {
    this.tracks.clear();
    for (int i = 0; i < count; i++) {
      this.tracks.add(new MidiTrack());
    }
  }

  public boolean isAnySolo() {
    return this.anySolo;
  }

  public boolean isMute(int index) {
    if (index >= 0 && index < this.tracks.size()) {
      MidiTrack track = (MidiTrack) this.tracks.get(index);
      return track.isMute();
    }
    return false;
  }

  public boolean isSolo(int index) {
    if (index >= 0 && index < this.tracks.size()) {
      MidiTrack track = (MidiTrack) this.tracks.get(index);
      return track.isSolo();
    }
    return false;
  }

  public void setMute(int index, boolean mute) throws MidiPlayerException {
    if (index >= 0 && index < this.tracks.size()) {
      MidiTrack track = (MidiTrack) this.tracks.get(index);
      track.setMute(mute);
      if (track.isMute()) {
        setSolo(index, false);
        this.sequencer.getTransmitter().sendAllNotesOff();
      }
    }
  }

  public void setSolo(int index, boolean solo) throws MidiPlayerException {
    if (index >= 0 && index < this.tracks.size()) {
      MidiTrack track = (MidiTrack) this.tracks.get(index);
      track.setSolo(solo);
      checkAnySolo();
      if (track.isSolo()) {
        setMute(index, false);
        this.sequencer.getTransmitter().sendAllNotesOff();
      }
    }
  }
}
