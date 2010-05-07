package org.herac.tuxguitar.jack.sequencer;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.jack.JackClient;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class JackSequencerProvider implements MidiSequencerProvider {

  private JackClient jackClient;
  private List<MidiSequencer> jackSequencerProviders;

  public JackSequencerProvider(JackClient jackClient) {
    this.jackClient = jackClient;
  }

  public void closeAll() throws MidiPlayerException {
    for (final MidiSequencer sequencer : listSequencers()) {
      sequencer.close();
    }
  }

  public List<MidiSequencer> listSequencers() throws MidiPlayerException {
    if (this.jackSequencerProviders == null) {
      this.jackSequencerProviders = new ArrayList<MidiSequencer>();
      this.jackSequencerProviders.add(new JackSequencer(this.jackClient));
    }
    return this.jackSequencerProviders;
  }

}
