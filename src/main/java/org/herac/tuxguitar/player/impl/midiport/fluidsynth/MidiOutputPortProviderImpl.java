package org.herac.tuxguitar.player.impl.midiport.fluidsynth;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.player.base.MidiOutputPortProvider;
import org.herac.tuxguitar.player.base.MidiPlayerException;

public class MidiOutputPortProviderImpl implements MidiOutputPortProvider {

  private MidiOutputPortSettings settings;
  private MidiSynth synth;

  public MidiOutputPortProviderImpl() {
    super();
  }

  public void closeAll() throws MidiPlayerException {
    try {
      if (this.synth != null && this.synth.isInitialized()) {
        this.synth.finalize();
        this.synth = null;
      }
    } catch (Throwable throwable) {
      throw new MidiPlayerException(throwable.getMessage(), throwable);
    }
  }

  public MidiOutputPortSettings getSettings() {
    if (this.settings == null) {
      this.settings = new MidiOutputPortSettings(this);
    }
    return this.settings;
  }

  public MidiSynth getSynth() {
    if (this.synth == null || !this.synth.isInitialized()) {
      this.synth = new MidiSynth();
      this.getSettings().apply();
    }
    return this.synth;
  }

  public List listPorts() throws MidiPlayerException {
    try {
      List ports = new ArrayList();
      Iterator it = getSettings().getSoundfonts().iterator();
      while (it.hasNext()) {
        String path = (String) it.next();
        File soundfont = new File(path);
        if (soundfont.exists() && !soundfont.isDirectory()) {
          ports.add(new MidiOutputPortImpl(getSynth(), soundfont));
        }
      }
      return ports;
    } catch (Throwable throwable) {
      throw new MidiPlayerException(throwable.getMessage(), throwable);
    }
  }
}
