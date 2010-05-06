package org.herac.tuxguitar.jack.sequencer;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.jack.JackClient;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiSequenceHandler;
import org.herac.tuxguitar.player.base.MidiSequencer;
import org.herac.tuxguitar.player.base.MidiTransmitter;

public class JackSequencer implements MidiSequencer {

  private class JackTimer implements Runnable {

    private static final int TIMER_DELAY = 10;

    private boolean running;
    private JackSequencer sequencer;
    private Object sequencerSync;

    public JackTimer(JackSequencer sequencer) {
      this.sequencerSync = new Object();
      this.sequencer = sequencer;
      this.running = false;
    }

    public void run() {
      try {
        synchronized (this.sequencerSync) {
          while (this.running) {
            this.sequencer.process();
            this.sequencerSync.wait(TIMER_DELAY);
          }
        }
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    public void setRunning(boolean running) {
      this.running = running;
      if (this.running) {
        new Thread(this).start();
      }
    }
  }

  private JackClient jackClient;
  private JackEventController jackEventController;
  private JackEventDispacher jackEventDispacher;
  private JackTickController jackTickController;
  private JackTimer jackTimer;
  private JackTrackController jackTrackController;
  private boolean reset;
  private boolean running;
  private boolean stopped;
  private MidiTransmitter transmitter;
  private boolean transportLockTick;
  private boolean transportRunning;
  private long transportTryCount;
  private long transportTryNumber;

  private long transportUID;

  public JackSequencer(JackClient jackClient) {
    this.stopped = true;
    this.running = false;
    this.transportRunning = false;
    this.transportUID = -1;
    this.transportTryCount = 10;
    this.transportTryNumber = 0;
    this.jackClient = jackClient;
    this.jackTickController = new JackTickController(this);
    this.jackEventController = new JackEventController(this);
    this.jackEventDispacher = new JackEventDispacher(this);
    this.jackTrackController = new JackTrackController(this);
    this.jackTimer = new JackTimer(this);
  }

  public void addEvent(JackEvent event) {
    this.jackEventController.addEvent(event);
    this.jackTickController.notifyTick(event.getTick());
  }

  public void check() throws MidiPlayerException {
    if (!this.jackClient.isServerRunning()
        || !this.jackClient.isTransportOpen()) {
      this.open();
      if (!this.jackClient.isServerRunning()
          || !this.jackClient.isTransportOpen()) {
        throw new MidiPlayerException("Jack server not running?");
      }
    }
  }

  public void close() throws MidiPlayerException {
    this.jackTimer.setRunning(false);
    if (this.isRunning()) {
      this.stop();
    }
    if (this.jackClient.isTransportOpen()) {
      this.jackClient.closeTransport();
    }
  }

  public MidiSequenceHandler createSequence(int tracks)
      throws MidiPlayerException {
    return new JackSequenceHandler(this, tracks);
  }

  public JackClient getJackClient() {
    return this.jackClient;
  }

  public JackEventController getJackEventController() {
    return this.jackEventController;
  }

  public JackTickController getJackTickController() {
    return this.jackTickController;
  }

  public JackTrackController getJackTrackController() {
    return this.jackTrackController;
  }

  public String getKey() {
    return "tuxguitar-jack";
  }

  public String getName() {
    return "Jack Sequencer";
  }

  public long getTickLength() {
    return this.jackTickController.getTickLength();
  }

  public long getTickPosition() {
    return Math.round(this.jackTickController.getTick());
  }

  public MidiTransmitter getTransmitter() {
    return this.transmitter;
  }

  public boolean isRunning() {
    return this.running;
  }

  public void open() {
    if (!this.jackClient.isTransportOpen()) {
      this.jackClient.openTransport();
    }
    this.jackTimer.setRunning(true);
  }

  protected void process() throws MidiPlayerException {
    boolean transportRunning = this.jackClient.isTransportRunning();

    // Check if state was changed
    if (this.transportRunning != transportRunning) {
      if (transportRunning && !this.running) {
        // Transport was started
        this.startPlayer();
      } else if (!transportRunning) {
        // Transport was stopped.
        this.running = false;
      }
    } else {
      if (this.running && transportRunning) {
        long transportUID = this.jackClient.getTransportUID();
        if (this.transportUID != transportUID) {
          this.setTickPosition(0, false);
        }
        if (this.reset) {
          this.reset(false);
          this.reset = false;
          this.jackEventController.reset();
        }
        this.stopped = false;
        this.jackTickController.process();
        this.jackEventController.process();
        if (this.getTickPosition() > this.getTickLength()) {
          this.stop(true);
        }
        this.transportUID = transportUID;
      } else if (!this.stopped) {
        this.stopped = true;
        this.jackEventController.clearEvents();
        this.jackTickController.clearTick();
        this.reset(true);
      } else if (this.running) {
        if (this.transportTryNumber++ > this.transportTryCount) {
          this.running = false;
          this.transportTryNumber = 0;
        }
      }
    }
    this.transportRunning = transportRunning;
  }

  public void reset(boolean systemReset) throws MidiPlayerException {
    this.getTransmitter().sendAllNotesOff();
    for (int channel = 0; channel < 16; channel++) {
      this.getTransmitter().sendPitchBend(channel, 64);
    }
    if (systemReset) {
      this.getTransmitter().sendSystemReset();
    }
  }

  public void sendEvent(JackEvent event) throws MidiPlayerException {
    if (!this.reset) {
      this.jackEventDispacher.dispatch(event);
    }
  }

  public void setMute(int index, boolean mute) throws MidiPlayerException {
    this.getJackTrackController().setMute(index, mute);
  }

  public void setSolo(int index, boolean solo) throws MidiPlayerException {
    this.getJackTrackController().setSolo(index, solo);
  }

  public void setTempo(int tempo) {
    this.jackTickController.setTempo(tempo);
  }

  public void setTickPosition(long tickPosition) {
    this.setTickPosition(tickPosition, !this.transportLockTick);
    this.transportLockTick = false;
  }

  public void setTickPosition(long tickPosition, boolean transportUpdate) {
    this.reset = true;
    this.jackTickController.setTick(tickPosition, transportUpdate);
  }

  public void setTransmitter(MidiTransmitter transmitter) {
    this.transmitter = transmitter;
  }

  public void start() throws MidiPlayerException {
    this.start(true);
  }

  public void start(boolean startTransport) throws MidiPlayerException {
    if (!this.running) {
      this.setTempo(120);
      this.running = true;
      if (startTransport) {
        this.jackClient.setTransportStart();
      }
    }
  }

  protected void startPlayer() {
    // Make sure sequencer was already initialized.
    if (this.transmitter != null) {
      this.transportLockTick = true;
      TuxGuitar.instance().getTransport().play();
    }
  }

  public void stop() throws MidiPlayerException {
    this.stop(true);
  }

  public void stop(boolean stopTransport) throws MidiPlayerException {
    if (this.running) {
      this.running = false;
      if (stopTransport) {
        this.jackClient.setTransportStop();
      }
    }
  }
}
