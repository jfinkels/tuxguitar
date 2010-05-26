/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGLyricImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGTrack {
  public static final int MAX_OFFSET = 24;
  public static final int MIN_OFFSET = -24;

  private TGChannel channel = new TGChannel();
  private Color color = Color.BLACK;
  private TGLyric lyrics = new TGLyricImpl();
  private List<TGMeasure> measures = new ArrayList<TGMeasure>();
  private boolean mute = false;
  private String name = "";
  private int number = 0;
  private int offset = 0;
  private boolean solo = false;
  private TGSong song = null;
  private List<TGString> strings = new ArrayList<TGString>();

  public void addMeasure(int index, TGMeasure measure) {
    measure.setTrack(this);
    this.measures.add(index, measure);
  }

  public void addMeasure(TGMeasure measure) {
    measure.setTrack(this);
    this.measures.add(measure);
  }

  public void clear() {
    this.strings.clear();
    this.measures.clear();
  }

  public TGTrack clone(TGSong song) {
    TGTrack track = new TGTrackImpl();
    copy(song, track);
    return track;
  }

  public void copy(TGSong song, TGTrack track) {
    track.clear();
    track.setNumber(getNumber());
    track.setName(getName());
    track.setOffset(getOffset());
    track.setChannel(this.channel.clone());
    this.color = track.getColor();
    track.setLyrics(this.lyrics.clone());

    for (final TGString string : this.strings) {
      track.getStrings().add(string.clone());
    }

    int i = 0;
    for (final TGMeasure measure : this.measures) {
      track.addMeasure(measure.clone(song.getMeasureHeader(i++)));
    }
  }

  public int countMeasures() {
    return this.measures.size();
  }

  public TGChannel getChannel() {
    return this.channel;
  }

  public Color getColor() {
    return this.color;
  }

  public TGLyric getLyrics() {
    return this.lyrics;
  }

  public TGMeasure getMeasure(int index) {
    if (index >= 0 && index < countMeasures()) {
      return (TGMeasure) this.measures.get(index);
    }
    return null;
  }

  public List<TGMeasure> getMeasures() {
    return this.measures;
  }

  public String getName() {
    return this.name;
  }

  public int getNumber() {
    return this.number;
  }

  public int getOffset() {
    return this.offset;
  }

  public TGSong getSong() {
    return this.song;
  }

  public TGString getString(int number) {
    return (TGString) this.strings.get(number - 1);
  }

  public List<TGString> getStrings() {
    return this.strings;
  }

  public boolean isMute() {
    return this.mute;
  }

  public boolean isPercussionTrack() {
    return (getChannel().isPercussionChannel());
  }

  public boolean isSolo() {
    return this.solo;
  }

  public void removeMeasure(int index) {
    this.measures.remove(index);
  }

  public void setChannel(TGChannel channel) {
    this.channel = channel;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setLyrics(TGLyric lyrics) {
    this.lyrics = lyrics;
  }

  public void setMute(boolean mute) {
    this.mute = mute;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public void setSolo(boolean solo) {
    this.solo = solo;
  }

  public void setSong(TGSong song) {
    this.song = song;
  }

  public void setStrings(List<TGString> strings) {
    this.strings = strings;
  }

  public int stringCount() {
    return this.strings.size();
  }

}
