package org.herac.tuxguitar.player.impl.midiport.alsa;

import java.util.ArrayList;
import java.util.List;

public class MidiSystem {

  private static final String JNI_LIBRARY_NAME = new String(
      "tuxguitar-alsa-jni");

  static {
    System.loadLibrary(JNI_LIBRARY_NAME);
  }

  private long instance;
  private boolean open;
  private List ports;

  public MidiSystem() {
    this.instance = malloc();
    this.open = false;
    this.ports = new ArrayList();
  }

  protected void addPort(String name, int client, int port) {
    this.ports.add(new MidiOutputPortImpl(this, name, client, port));
  }

  public void close() {
    if (this.instance != 0 && this.open) {
      this.close(this.instance);
      this.open = false;
    }
  }

  private native void close(long instance);

  public void closePort() {
    if (this.instance != 0 && this.open) {
      this.closePort(this.instance);
    }
  }

  private native void closePort(long instance);

  public void controlChange(int channel, int control, int value) {
    if (this.instance != 0 && this.open) {
      this.controlChange(this.instance, channel, control, value);
    }
  }

  private native void controlChange(long instance, int channel, int control,
      int value);

  public void finalize() {
    if (this.instance != 0) {
      this.free(this.instance);
      this.instance = 0;
    }
  }

  public List findPorts() {
    this.ports.clear();
    if (this.instance != 0 && this.open) {
      this.findPorts(this.instance);
    }
    return this.ports;
  }

  private native void findPorts(long instance);

  private native void free(long instance);

  public boolean isOpen() {
    return (this.instance != 0 && this.open);
  }

  private native long malloc();

  public void noteOff(int channel, int note, int velocity) {
    if (this.instance != 0 && this.open) {
      this.noteOff(this.instance, channel, note, velocity);
    }
  }

  private native void noteOff(long instance, int channel, int note, int velocity);

  public void noteOn(int channel, int note, int velocity) {
    if (this.instance != 0 && this.open) {
      this.noteOn(this.instance, channel, note, velocity);
    }
  }

  private native void noteOn(long instance, int channel, int note, int velocity);

  public void open() {
    if (this.instance != 0 && !this.open) {
      this.open(this.instance);
      this.open = true;
    }
  }

  private native void open(long instance);

  public void openPort(int client, int port) {
    if (this.instance != 0 && this.open) {
      this.openPort(this.instance, client, port);
    }
  }

  private native void openPort(long instance, int client, int port);

  public void pitchBend(int channel, int value) {
    if (this.instance != 0 && this.open) {
      this.pitchBend(this.instance, channel, value);
    }
  }

  private native void pitchBend(long instance, int channel, int value);

  public void programChange(int channel, int program) {
    if (this.instance != 0 && this.open) {
      this.programChange(this.instance, channel, program);
    }
  }

  private native void programChange(long instance, int channel, int program);

}
