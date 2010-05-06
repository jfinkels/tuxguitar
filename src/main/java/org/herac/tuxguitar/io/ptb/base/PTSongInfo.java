package org.herac.tuxguitar.io.ptb.base;

public class PTSongInfo {
  private String album;
  private int albumType;
  private String arrenger;
  private String author;
  private String bassInstructions;
  private String bassTranscriber;
  private int classification;
  private String copyright;
  private int day;
  private String guitarInstructions;
  private String guitarTranscriber;
  private String instructions;
  private String interpret;
  private int level;
  private boolean liveRecording;
  private String lyricist;
  private String lyrics;
  private int month;
  private String name;
  private int releaseType;
  private int style;
  private int year;

  public PTSongInfo() {
    super();
  }

  public void copy(PTSongInfo info) {
    info.setClassification(getClassification());
    info.setReleaseType(getReleaseType());
    info.setAlbumType(getAlbumType());
    info.setDay(getDay());
    info.setMonth(getMonth());
    info.setYear(getYear());
    info.setStyle(getStyle());
    info.setLevel(getLevel());
    info.setLiveRecording(isLiveRecording());
    info.setName(getName());
    info.setInterpret(getInterpret());
    info.setAlbum(getAlbum());
    info.setAuthor(getAuthor());
    info.setLyricist(getLyricist());
    info.setArrenger(getArrenger());
    info.setGuitarTranscriber(getGuitarTranscriber());
    info.setBassTranscriber(getBassTranscriber());
    info.setLyrics(getLyrics());
    info.setGuitarInstructions(getGuitarInstructions());
    info.setBassInstructions(getBassInstructions());
    info.setInstructions(getInstructions());
    info.setCopyright(getCopyright());
  }

  public String getAlbum() {
    return this.album;
  }

  public int getAlbumType() {
    return this.albumType;
  }

  public String getArrenger() {
    return this.arrenger;
  }

  public String getAuthor() {
    return this.author;
  }

  public String getBassInstructions() {
    return this.bassInstructions;
  }

  public String getBassTranscriber() {
    return this.bassTranscriber;
  }

  public int getClassification() {
    return this.classification;
  }

  public String getCopyright() {
    return this.copyright;
  }

  public int getDay() {
    return this.day;
  }

  public String getGuitarInstructions() {
    return this.guitarInstructions;
  }

  public String getGuitarTranscriber() {
    return this.guitarTranscriber;
  }

  public String getInstructions() {
    return this.instructions;
  }

  public String getInterpret() {
    return this.interpret;
  }

  public int getLevel() {
    return this.level;
  }

  public String getLyricist() {
    return this.lyricist;
  }

  public String getLyrics() {
    return this.lyrics;
  }

  public int getMonth() {
    return this.month;
  }

  public String getName() {
    return this.name;
  }

  public int getReleaseType() {
    return this.releaseType;
  }

  public int getStyle() {
    return this.style;
  }

  public int getYear() {
    return this.year;
  }

  public boolean isLiveRecording() {
    return this.liveRecording;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public void setAlbumType(int albumType) {
    this.albumType = albumType;
  }

  public void setArrenger(String arrenger) {
    this.arrenger = arrenger;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setBassInstructions(String bassInstructions) {
    this.bassInstructions = bassInstructions;
  }

  public void setBassTranscriber(String bassTranscriber) {
    this.bassTranscriber = bassTranscriber;
  }

  public void setClassification(int classification) {
    this.classification = classification;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setGuitarInstructions(String guitarInstructions) {
    this.guitarInstructions = guitarInstructions;
  }

  public void setGuitarTranscriber(String guitarTranscriber) {
    this.guitarTranscriber = guitarTranscriber;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public void setInterpret(String interpret) {
    this.interpret = interpret;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void setLiveRecording(boolean liveRecording) {
    this.liveRecording = liveRecording;
  }

  public void setLyricist(String lyricist) {
    this.lyricist = lyricist;
  }

  public void setLyrics(String lyrics) {
    this.lyrics = lyrics;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setReleaseType(int releaseType) {
    this.releaseType = releaseType;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public void setYear(int year) {
    this.year = year;
  }
}
