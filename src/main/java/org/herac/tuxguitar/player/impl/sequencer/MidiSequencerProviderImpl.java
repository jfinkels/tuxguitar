package org.herac.tuxguitar.player.impl.sequencer;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class MidiSequencerProviderImpl implements MidiSequencerProvider {

  private List<MidiSequencer> sequencers;

  public MidiSequencerProviderImpl() {
    super();
  }

  public void closeAll() throws MidiPlayerException {
    for (final MidiSequencer sequencer : listSequencers()) {
      sequencer.close();
    }
  }

  public List<MidiSequencer> listSequencers() throws MidiPlayerException {
    if (this.sequencers == null) {
      this.sequencers = new ArrayList<MidiSequencer>();
      this.sequencers.add(new MidiSequencerImpl());
    }
    return this.sequencers;
  }

}
