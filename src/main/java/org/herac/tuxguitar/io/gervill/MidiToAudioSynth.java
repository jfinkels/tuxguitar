package org.herac.tuxguitar.io.gervill;

import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

public class MidiToAudioSynth {

  private static MidiToAudioSynth instance;

  public static final AudioFormat SRC_FORMAT = MidiToAudioSettings.DEFAULT_FORMAT;

  public static MidiToAudioSynth instance() {
    if (instance == null) {
      instance = new MidiToAudioSynth();
    }
    return instance;
  }

  private Receiver receiver;
  private AudioInputStream stream;

  private Synthesizer synthesizer;

  private MidiToAudioSynth() {
    this.stream = null;
    this.receiver = null;
    this.synthesizer = null;
  }

  public void closeSynth() throws Throwable {
    if (this.receiver != null) {
      this.receiver.close();
    }
    if (this.synthesizer != null && this.synthesizer.isOpen()) {
      this.synthesizer.close();
    }
    this.stream = null;
    this.receiver = null;
    this.synthesizer = null;
  }

  public Receiver getReceiver() {
    return this.receiver;
  }

  public AudioInputStream getStream() {
    return this.stream;
  }

  public boolean isAvailable() {
    try {
      Class.forName("com.sun.media.sound.SoftSynthesizer", false, getClass()
          .getClassLoader());
      return true;
    } catch (Throwable throwable) {
      return false;
    }
  }

  public void openSynth() throws Throwable {
    if (this.synthesizer == null || !this.synthesizer.isOpen()) {
      this.synthesizer = new com.sun.media.sound.SoftSynthesizer();
      this.receiver = this.synthesizer.getReceiver();
      this.stream = ((com.sun.media.sound.AudioSynthesizer) this.synthesizer)
          .openStream(SRC_FORMAT, null);
    }
  }
}
