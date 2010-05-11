package org.herac.tuxguitar.song.models;

public abstract class TGLyric {
  private static final String REGEX = " ";

  private int from;
  private String lyrics;

  public TGLyric() {
    this.from = 1;
    this.lyrics = new String();
  }

  /*
   * public static void copy(TGLyric source, TGLyric destination) {
   * destination.setFrom(source.from); destination.setLyrics(source.lyrics); }
   */
  public void copy(TGLyric lyric) {
    lyric.setFrom(getFrom());
    lyric.setLyrics(getLyrics());
  }

  public int getFrom() {
    return this.from;
  }

  public String[] getLyricBeats() {
    String lyrics = getLyrics();
    lyrics = lyrics.replaceAll("\n", REGEX);
    lyrics = lyrics.replaceAll("\r", REGEX);
    return lyrics.split(REGEX);
  }

  public String getLyrics() {
    return this.lyrics;
  }

  public void setFrom(int from) {
    this.from = from;
  }

  public void setLyrics(String lyrics) {
    this.lyrics = lyrics;
  }

}
