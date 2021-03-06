package org.herac.tuxguitar.jack.synthesizer;

import org.herac.tuxguitar.gui.system.config.TGConfigManager;
import org.herac.tuxguitar.jack.JackClient;
import org.herac.tuxguitar.jack.settings.JackSettings;
import org.herac.tuxguitar.jack.settings.JackSettingsListener;
import org.herac.tuxguitar.player.base.MidiOutputPort;
import org.herac.tuxguitar.player.base.MidiPlayerException;
import org.herac.tuxguitar.player.base.MidiReceiver;

public class JackOutputPort implements JackSettingsListener, MidiOutputPort {

  private JackClient jackClient;
  private JackOutputPortRouter jackOutputPortRouter;
  private JackReceiver jackReceiver;
  private JackSettings jackSettings;

  public JackOutputPort(JackClient jackClient, JackSettings jackSettings) {
    this.jackOutputPortRouter = new JackOutputPortRouter();
    this.jackReceiver = new JackReceiver(jackClient, this);
    this.jackSettings = jackSettings;
    this.jackClient = jackClient;
  }

  public void check() throws MidiPlayerException {
    if (!this.jackClient.isServerRunning() || !this.jackClient.isPortsOpen()) {
      this.open();
      if (!this.jackClient.isServerRunning() || !this.jackClient.isPortsOpen()) {
        throw new MidiPlayerException("Jack server not running?");
      }
    }
  }

  public void close() {
    if (this.jackClient.isPortsOpen()) {
      this.jackClient.closePorts();
      this.jackSettings.removeListener(this);
    }
  }

  public String getKey() {
    return ("tuxguitar-jack");
  }

  public String getName() {
    return ("Jack Midi Port");
  }

  public MidiReceiver getReceiver() {
    return this.jackReceiver;
  }

  public JackOutputPortRouter getRouter() {
    return this.jackOutputPortRouter;
  }

  public void loadSettings(TGConfigManager config) {
    boolean connected = this.jackClient.isPortsOpen();
    if (connected) {
      this.jackClient.closePorts();
    }
    this.jackOutputPortRouter.loadSettings(config);
    if (connected) {
      this.jackClient.openPorts(this.jackOutputPortRouter.getPortCount());
    }
  }

  public void open() {
    if (!this.jackClient.isPortsOpen()) {
      this.loadSettings(this.jackSettings.getConfig());
      this.jackSettings.addListener(this);
      this.jackClient.openPorts(this.jackOutputPortRouter.getPortCount());
    }
  }
}
