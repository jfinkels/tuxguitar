package org.herac.tuxguitar.player.impl.midiport.fluidsynth;

import java.io.File;

import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class MidiOutputPortImpl implements MidiOutputPort {

  private String key;
  private String name;
  private MidiReceiverImpl receiver;
  private String soundFont;
  private MidiSynth synth;

  public MidiOutputPortImpl(MidiSynth synth, File soundfont) {
    this.key = getUniqueKey(soundfont);
    this.name = getUniqueName(soundfont);
    this.soundFont = soundfont.getAbsolutePath();
    this.receiver = new MidiReceiverImpl(synth);
    this.synth = synth;
  }

  public void check() {
    // Not implemented
  }

  public void close() {
    if (this.synth.isConnected(this)) {
      this.synth.disconnect(this);
    }
  }

  public String getKey() {
    return this.key;
  }

  public String getName() {
    return this.name;
  }

  public MidiReceiver getReceiver() {
    this.open();
    return this.receiver;
  }

  public String getSoundFont() {
    return this.soundFont;
  }

  public String getUniqueKey(File soundfont) {
    return ("tuxguitar-fluidsynth_" + soundfont.getAbsolutePath());
  }

  private String getUniqueName(File soundfont) {
    String name = soundfont.getName();
    int extensionIndex = name.lastIndexOf(".");
    if (extensionIndex > 0) {
      name = name.substring(0, extensionIndex);
    }
    return ("TG Fluidsynth " + "[" + name + "]");
  }

  public void open() {
    if (!this.synth.isConnected(this)) {
      this.synth.connect(this);
    }
  }
}
