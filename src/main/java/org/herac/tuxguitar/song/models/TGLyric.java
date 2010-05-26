package org.herac.tuxguitar.song.models;

import joptsimple.internal.Strings;

public abstract class TGLyric {
  private static final String SPACE = " ";

  private int from = 1;
  private String lyrics = Strings.EMPTY;

  @Override
  public abstract TGLyric clone();

  public int getFrom() {
    return this.from;
  }

  public String[] getLyricBeats() {
    return this.lyrics.replaceAll("\n", SPACE).replaceAll("\r", SPACE).split(
        SPACE);
  }

  public String getLyrics() {
    return this.lyrics;
  }

  public void setFrom(final int from) {
    this.from = from;
  }

  public void setLyrics(final String lyrics) {
    this.lyrics = lyrics;
  }

}
