package org.herac.tuxguitar.gui.tools.custom.tuner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.util.MessageDialog;

/**
 * @author Nikola Kolarovic <nikola.kolarovic at gmail.com>
 * 
 */
public class TGTunerSettings {

  static final int CHANNELS_NUMBER = 1;
  static final int DEFAULT_BUFFER_SIZE = 4096; // 2^12
  static final int DEFAULT_FFT_SIZE = 16384; // 2^14
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(TGTunerSettings.class);

  /** gets dataline from format specification */
  protected static TargetDataLine getDataLine(TGTunerSettings settings)
      throws TGTuner.TGTunerException {
    TargetDataLine targetDataLine = null;

    if (settings != null) {
      // get info for initialization
      DataLine.Info info = settings.getDataLineInfo();

      try {

        targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

      } catch (Exception ex) {
        MessageDialog.errorMessage(ex);
      }
    } else
      throw new TGTuner.TGTunerException(
          "Could not retrieve data from the input. Check your system device settings.");

    return targetDataLine;
  }

  /** default settings, if faild to load from properties */
  public static TGTunerSettings getDefaults() {
    TGTunerSettings retValue = new TGTunerSettings();
    retValue.setSampleRate(11025);
    retValue.setSampleSize(8);
    retValue.setBufferSize(DEFAULT_BUFFER_SIZE);
    retValue.setFFTSize(DEFAULT_FFT_SIZE);
    retValue.setTreshold(0.03);
    retValue.setWaitPeriod(100);
    return retValue;
  }

  public static TGTunerSettings loadTuxGuitarSettings()
      throws TGTuner.TGTunerException {
    // TODO: load system properties and throw exception if failed to load
    return TGTunerSettings.getDefaults();
    // return null;
  }

  protected int bufferSize;
  protected String deviceName;
  protected int fftSize;

  protected float sampleRate;

  protected int sampleSize;

  protected double treshold;

  protected int waitPeriod;

  /*
   * private int getFrameSize() { return (this.sampleSize / 8) *
   * CHANNELS_NUMBER; }
   */

  /** creates AudioFormat based on settings */
  public AudioFormat getAudioFormat() {
    return new AudioFormat(this.getSampleRate(), this.getSampleSize(), 1, true,
        false);
    // TODO: this is changed!!!!
    /*
     * return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, // PCM encoding
     * this.getSampleRate(), // sample rate this.getSampleSize(), // sample size
     * (8-bit, 16-bit) 1, // mono this.getFrameSize(), // 16-bit frame (was:4)
     * this.getSampleRate(), // frame rate false // big-endian );
     */
  }

  public int getBufferSize() {
    return this.bufferSize;
  }

  /** Creates DataLine.Info based on settings */
  protected DataLine.Info getDataLineInfo() {
    return new DataLine.Info(TargetDataLine.class, this.getAudioFormat(), this
        .getBufferSize());

  }

  public int getFFTSize() {
    return this.fftSize;
  }

  public float getSampleRate() {
    return this.sampleRate;
  }

  public int getSampleSize() {
    return this.sampleSize;
  }

  public double getTreshold() {
    return this.treshold;
  }

  public int getWaitPeriod() {
    return this.waitPeriod;
  }

  public void setBufferSize(int bufferSize) {
    // TODO: adjust size in TGTuner also then
    this.bufferSize = bufferSize;
  }

  public void setFFTSize(int size) {
    this.fftSize = size;
  }

  public void setSampleRate(float sampleRate) {
    this.sampleRate = sampleRate;
  }

  public void setSampleSize(int sampleSize) {
    this.sampleSize = sampleSize;
  }

  public void setTreshold(double nt) {
    this.treshold = nt;
  }

  /*
   * MAYBE USEFUL CODE
   * 
   * Port lineIn; FloatControl volCtrl; try { mixer =
   * AudioSystem.getMixer(null); lineIn =
   * (Port)mixer.getLine(Port.Info.LINE_IN); lineIn.open(); volCtrl =
   * (FloatControl) lineIn.getControl( FloatControl.Type.VOLUME); // Assuming
   * getControl call succeeds, // we now have our LINE_IN VOLUME control. }
   * catch (Exception e) { LOG.debug("Failed trying to find LINE_IN" +
   * " VOLUME control: exception = " + e); } float newValue = 2.0F; if (volCtrl
   * != null) // This changes the volume of the signal flowing though the line
   * that "owns" the control. volCtrl.setValue(newValue);
   */

  public void setWaitPeriod(int time) {
    this.waitPeriod = time;
  }
}
