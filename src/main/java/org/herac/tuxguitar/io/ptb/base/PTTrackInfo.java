package org.herac.tuxguitar.io.ptb.base;

public class PTTrackInfo {
  private int balance;
  private int chorus;
  private int instrument;
  private String name;
  private int number;
  private int phaser;
  private int reverb;
  private int[] strings;
  private int tremolo;

  private int volume;

  public PTTrackInfo() {
    super();
  }

  public int getBalance() {
    return this.balance;
  }

  public int getChorus() {
    return this.chorus;
  }

  public PTTrackInfo getClone() {
    int[] strings = new int[this.strings.length];
    for (int i = 0; i < strings.length; i++) {
      strings[i] = this.strings[i];
    }
    PTTrackInfo info = new PTTrackInfo();
    info.setNumber(getNumber());
    info.setName(getName());
    info.setInstrument(getInstrument());
    info.setVolume(getVolume());
    info.setBalance(getBalance());
    info.setChorus(getChorus());
    info.setPhaser(getPhaser());
    info.setReverb(getReverb());
    info.setTremolo(getTremolo());
    info.setStrings(strings);

    return info;
  }

  public int getInstrument() {
    return this.instrument;
  }

  public String getName() {
    return this.name;
  }

  public int getNumber() {
    return this.number;
  }

  public int getPhaser() {
    return this.phaser;
  }

  public int getReverb() {
    return this.reverb;
  }

  public int[] getStrings() {
    return this.strings;
  }

  public int getTremolo() {
    return this.tremolo;
  }

  public int getVolume() {
    return this.volume;
  }

  public void setBalance(int balance) {
    this.balance = balance;
  }

  public void setChorus(int chorus) {
    this.chorus = chorus;
  }

  public void setInstrument(int instrument) {
    this.instrument = instrument;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setPhaser(int phaser) {
    this.phaser = phaser;
  }

  public void setReverb(int reverb) {
    this.reverb = reverb;
  }

  public void setStrings(int[] strings) {
    this.strings = strings;
  }

  public void setTremolo(int tremolo) {
    this.tremolo = tremolo;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }
}
