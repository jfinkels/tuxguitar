package org.herac.tuxguitar.player.impl.midiport.oss;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiOutputPort;

public class MidiSystem {

  private static final String JNI_LIBRARY_NAME = new String("tuxguitar-oss-jni");

  static {
    System.loadLibrary(JNI_LIBRARY_NAME);
  }

  private MidiOutputPortImpl connection;
  private long instance;
  private boolean open;
  private List<MidiOutputPort> ports;

  public MidiSystem() {
    this.instance = malloc();
    this.open = false;
    this.ports = new ArrayList<MidiOutputPort>();
    this.connection = null;
  }

  protected void addPort(String name, int device) {
    this.ports.add(new MidiOutputPortImpl(this, name, device));
  }

  public void close() {
    if (this.instance != 0 && this.open) {
      this.close(this.instance);
      this.open = false;
    }
  }

  protected native void close(long instance);

  public void closePort() {
    if (this.instance != 0 && this.open) {
      this.closePort(this.instance);
      this.connection = null;
    }
  }

  protected native void closePort(long instance);

  public void controlChange(int channel, int control, int value) {
    if (this.instance != 0 && this.open) {
      this.controlChange(this.instance, channel, control, value);
    }
  }

  protected native void controlChange(long instance, int channel, int control,
      int value);

  public void finalize() {
    if (this.instance != 0) {
      this.free(this.instance);
      this.instance = 0;
    }
  }

  public List<MidiOutputPort> findPorts() {
    this.ports.clear();
    if (this.instance != 0 && this.open) {
      this.findPorts(this.instance);
    }
    return this.ports;
  }

  protected native void findPorts(long instance);

  private native void free(long instance);

  public MidiOutputPortImpl getConnection() {
    return this.connection;
  }

  public boolean isOpen() {
    return (this.instance != 0 && this.open);
  }

  private native long malloc();

  public void noteOff(int channel, int note, int velocity) {
    if (this.instance != 0 && this.open) {
      this.noteOff(this.instance, channel, note, velocity);
    }
  }

  protected native void noteOff(long instance, int channel, int note,
      int velocity);

  public void noteOn(int channel, int note, int velocity) {
    if (this.instance != 0 && this.open) {
      this.noteOn(this.instance, channel, note, velocity);
    }
  }

  protected native void noteOn(long instance, int channel, int note,
      int velocity);

  protected native void open(long instance, String device);

  public void open(String device) {
    if (this.instance != 0 && !this.open) {
      this.open(this.instance, device);
      this.open = true;
    }
  }

  protected native void openPort(long instance, int device);

  public void openPort(MidiOutputPortImpl port) {
    if (this.instance != 0 && this.open) {
      this.openPort(this.instance, port.getDevice());
      this.connection = port;
    }
  }

  public void pitchBend(int channel, int value) {
    if (this.instance != 0 && this.open) {
      this.pitchBend(this.instance, channel, value);
    }
  }

  protected native void pitchBend(long instance, int channel, int value);

  public void programChange(int channel, int program) {
    if (this.instance != 0 && this.open) {
      this.programChange(this.instance, channel, program);
    }
  }

  protected native void programChange(long instance, int channel, int program);

}
