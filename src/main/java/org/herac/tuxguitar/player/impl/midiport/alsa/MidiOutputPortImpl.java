package org.herac.tuxguitar.player.impl.midiport.alsa;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiOutputPortImpl implements MidiOutputPort {

  private int client;
  private String clientName;
  private int port;
  private MidiReceiverImpl receiver;

  public MidiOutputPortImpl(MidiSystem midiSystem, String clientName,
      int client, int port) {
    this.port = port;
    this.client = client;
    this.clientName = clientName;
    this.receiver = new MidiReceiverImpl(this, midiSystem);
  }

  public void check() {
    // Not implemented
  }

  public void close() {
    this.receiver.disconnect();
  }

  public int getClient() {
    return this.client;
  }

  public String getKey() {
    return ("tuxguitar-alsa_" + this.client + "-" + this.port);
  }

  public String getName() {
    return (this.clientName + " [" + this.client + ":" + this.port + "]");
  }

  public int getPort() {
    return this.port;
  }

  public MidiReceiver getReceiver() {
    this.open();
    return this.receiver;
  }

  public void open() {
    if (!this.receiver.isConnected()) {
      this.receiver.connect();
    }
  }
}