package org.herac.tuxguitar.player.impl.jsa.sequencer;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiSequencerProvider;

public class MidiSequencerProviderImpl implements MidiSequencerProvider {

  public MidiSequencerProviderImpl() {
    super();
  }

  public void closeAll() throws MidiPlayerException {
    // Not implemented
  }

  public List<MidiSequencer> listSequencers() throws MidiPlayerException {
    try {
      List<MidiSequencer> sequencers = new ArrayList<MidiSequencer>();
      MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
      for (int i = 0; i < infos.length; i++) {
        try {
          
          boolean exists = false;
          for (final MidiSequencer sequencer : sequencers) {
            if (sequencer.getKey().equals(infos[i].getName())) {
              exists = true;
              break;
            }
          }
          if (!exists) {
            MidiDevice device = MidiSystem.getMidiDevice(infos[i]);
            if (device instanceof Sequencer) {
              sequencers.add(new MidiSequencerImpl((Sequencer) device));
            }
          }
        } catch (MidiUnavailableException e) {
          throw new MidiPlayerException(TuxGuitar
              .getProperty("jsa.error.midi.unavailable"), e);
        }
      }
      return sequencers;
    } catch (Throwable t) {
      throw new MidiPlayerException(TuxGuitar.getProperty("jsa.error.unknown"),
          t);
    }
  }
}
