package org.herac.tuxguitar.song.models;


public class TGChannel {
  public static final short DEFAULT_BALANCE = 64;

  public static final short DEFAULT_CHORUS = 0;
  public static final short DEFAULT_INSTRUMENT = 25;
  public static final short DEFAULT_PERCUSSION_CHANNEL = 9;
  public static final short DEFAULT_PHASER = 0;
  public static final short DEFAULT_REVERB = 0;
  public static final short DEFAULT_TREMOLO = 0;
  public static final short DEFAULT_VOLUME = 127;

  public static boolean isPercussionChannel(int channel) {
    return (channel == DEFAULT_PERCUSSION_CHANNEL);
  }

  public static TGChannel newPercussionChannel() {
    TGChannel channel = new TGChannel();
    TGChannel.setPercussionChannel(channel);
    return channel;
  }

  public static void setPercussionChannel(TGChannel channel) {
    channel.setChannel(DEFAULT_PERCUSSION_CHANNEL);
    channel.setEffectChannel(DEFAULT_PERCUSSION_CHANNEL);
  }

  private short balance = DEFAULT_BALANCE;
  private short channel = 0;
  private short chorus = DEFAULT_CHORUS;
  private short effectChannel = 0;
  private short instrument = DEFAULT_INSTRUMENT;
  private short phaser = DEFAULT_PHASER;
  private short reverb = DEFAULT_REVERB;
  private short tremolo = DEFAULT_TREMOLO;
  private short volume = DEFAULT_VOLUME;

  @Override
  public TGChannel clone() {
    TGChannel channel = new TGChannel();
    copy(channel);
    return channel;
  }

  public void copy(TGChannel channel) {
    channel.setChannel(getChannel());
    channel.setEffectChannel(getEffectChannel());
    channel.setInstrument(getInstrument());
    channel.setVolume(getVolume());
    channel.setBalance(getBalance());
    channel.setChorus(getChorus());
    channel.setReverb(getReverb());
    channel.setPhaser(getPhaser());
    channel.setTremolo(getTremolo());
  }

  public short getBalance() {
    return this.balance;
  }

  public short getChannel() {
    return this.channel;
  }

  public short getChorus() {
    return this.chorus;
  }

  public short getEffectChannel() {
    return this.effectChannel;
  }

  public short getInstrument() {
    return (!this.isPercussionChannel() ? this.instrument : 0);
  }

  public short getPhaser() {
    return this.phaser;
  }

  public short getReverb() {
    return this.reverb;
  }

  public short getTremolo() {
    return this.tremolo;
  }

  public short getVolume() {
    return this.volume;
  }

  public boolean isPercussionChannel() {
    return TGChannel.isPercussionChannel(this.getChannel());
  }

  public void setBalance(short balance) {
    this.balance = balance;
  }

  public void setChannel(short channel) {
    this.channel = channel;
  }

  public void setChorus(short chorus) {
    this.chorus = chorus;
  }

  public void setEffectChannel(short effectChannel) {
    this.effectChannel = effectChannel;
  }

  public void setInstrument(short instrument) {
    this.instrument = instrument;
  }

  public void setPhaser(short phaser) {
    this.phaser = phaser;
  }

  public void setReverb(short reverb) {
    this.reverb = reverb;
  }

  public void setTremolo(short tremolo) {
    this.tremolo = tremolo;
  }

  public void setVolume(short volume) {
    this.volume = volume;
  }
}
