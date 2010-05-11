package org.herac.tuxguitar.player.impl.jsa.sequencer;

import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiTransmitter;
import org.herac.tuxguitar.player.impl.jsa.midiport.MidiPortSynthesizer;

public class MidiSequencerImpl implements MidiSequencer, MidiSequenceLoader {
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(MidiSequencerImpl.class);
  private static final int TICK_MOVE = 1;

  private Object lock;
  private Sequencer sequencer;
  private Transmitter sequencerTransmitter;
  private MidiTransmitter transmitter;

  public MidiSequencerImpl(Sequencer sequencer) {
    this.lock = new Object();
    this.sequencer = sequencer;
  }

  public void check() throws MidiPlayerException {
    this.getSequencer(true);
    if (this.sequencer == null || !this.sequencer.isOpen()) {
      throw new MidiPlayerException(TuxGuitar
          .getProperty("jsa.error.midi.unavailable"));
    }
  }

  public synchronized void close() {
    try {
      if (this.sequencer.isOpen()) {
        this.sequencer.close();
        this.closeTransmitter();
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void closeTransmitter() {
    try {
      if (this.sequencerTransmitter != null) {
        this.sequencerTransmitter.close();
        this.sequencerTransmitter = null;
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public MidiSequenceHandler createSequence(int tracks) {
    this.resetTracks();
    return new MidiSequenceHandlerImpl(this, tracks);
  }

  public String getKey() {
    return this.sequencer.getDeviceInfo().getName();
  }

  public String getName() {
    return this.sequencer.getDeviceInfo().getName();
  }

  protected Sequencer getSequencer() {
    return this.getSequencer(true);
  }

  protected Sequencer getSequencer(boolean open) {
    if (open) {
      this.open();
    }
    return this.sequencer;
  }

  public long getTickLength() {
    try {
      return getSequencer().getTickLength();
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
    return 0;
  }

  public long getTickPosition() {
    try {
      return (getSequencer().getTickPosition() + TICK_MOVE);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
    return 0;
  }

  public synchronized MidiTransmitter getTransmitter() {
    return this.transmitter;
  }

  public boolean isRunning() {
    try {
      return (getSequencer(false) != null && getSequencer(false).isRunning());
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
    return false;
  }

  public synchronized void open() {
    try {
      if (!this.sequencer.isOpen()) {
        this.sequencer.open();
        this.closeTransmitter();
        this.openTransmitter();
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void openTransmitter() {
    try {
      this.sequencerTransmitter = getSequencer().getTransmitter();
      this.sequencerTransmitter.setReceiver(new MidiReceiverImpl(this));
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void reset(boolean systemReset) {
    try {
      this.getTransmitter().sendAllNotesOff();
      for (int channel = 0; channel < 16; channel++) {
        this.getTransmitter().sendPitchBend(channel, 64);
      }
      if (systemReset) {
        this.getTransmitter().sendSystemReset();
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void resetTracks() {
    try {
      Sequence sequence = this.getSequencer().getSequence();
      if (sequence != null) {
        Track[] tracks = sequence.getTracks();
        if (tracks != null) {
          int count = tracks.length;
          for (int i = 0; i < count; i++) {
            this.setSolo(i, false);
            this.setMute(i, false);
          }
        }
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void setMute(int index, boolean mute) {
    try {
      getSequencer().setTrackMute(index, mute);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void setRunning(boolean running) {
    try {
      synchronized (this.lock) {
        if (running && !this.isRunning()) {
          this.getSequencer().start();
        } else if (!running && this.isRunning()) {
          this.getSequencer().stop();
          this.reset(true);
        }
      }
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void setSequence(Sequence sequence) {
    try {
      getSequencer().setSequence(sequence);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void setSolo(int index, boolean solo) {
    try {
      getSequencer().setTrackSolo(index, solo);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void setTickPosition(long tickPosition) {
    try {
      this.getSequencer().setTickPosition(tickPosition - TICK_MOVE);
      this.reset(false);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public synchronized void setTransmitter(MidiTransmitter transmitter) {
    this.transmitter = transmitter;
  }

  public void start() {
    try {
      this.setRunning(true);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }

  public void stop() {
    try {
      this.setRunning(false);
    } catch (Throwable throwable) {
      LOG.error(throwable);
    }
  }
}
