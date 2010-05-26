/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

import joptsimple.internal.Strings;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGSong {

  private String album = Strings.EMPTY;
  private String artist = Strings.EMPTY;
  private String author = Strings.EMPTY;
  private String comments = Strings.EMPTY;
  private String copyright = Strings.EMPTY;
  private String date = Strings.EMPTY;
  private List<TGMeasureHeader> measureHeaders = new ArrayList<TGMeasureHeader>();
  private String name = Strings.EMPTY;
  private List<TGTrack> tracks = new ArrayList<TGTrack>();
  private String transcriber = Strings.EMPTY;
  private String writer = Strings.EMPTY;

  public void addMeasureHeader(int index, TGMeasureHeader measureHeader) {
    measureHeader.setSong(this);
    this.measureHeaders.add(index, measureHeader);
  }

  public void addMeasureHeader(TGMeasureHeader measureHeader) {
    this.addMeasureHeader(countMeasureHeaders(), measureHeader);
  }

  public void addTrack(int index, TGTrack track) {
    track.setSong(this);
    this.tracks.add(index, track);
  }

  public void addTrack(TGTrack track) {
    this.tracks.add(track);
  }

  public void clear() {
    for (final TGTrack track : this.tracks) {
      track.clear();
    }
    this.tracks.clear();
    this.measureHeaders.clear();
  }

  @Override
  public TGSong clone() {
    TGSong song = new TGSong();

    song.clear();
    song.setName(getName());
    song.setArtist(getArtist());
    song.setAlbum(getAlbum());
    song.setAuthor(getAuthor());
    song.setDate(getDate());
    song.setCopyright(getCopyright());
    song.setWriter(getWriter());
    song.setTranscriber(getTranscriber());
    song.setComments(getComments());

    for (final TGMeasureHeader header : this.measureHeaders) {
      song.addMeasureHeader(header.clone());
    }

    for (final TGTrack track : this.tracks) {
      song.addTrack(track.clone(song));
    }
    
    return song;
  }

  public int countMeasureHeaders() {
    return this.measureHeaders.size();
  }

  public int countTracks() {
    return this.tracks.size();
  }

  public String getAlbum() {
    return this.album;
  }

  public String getArtist() {
    return this.artist;
  }

  public String getAuthor() {
    return this.author;
  }

  public String getComments() {
    return this.comments;
  }

  public String getCopyright() {
    return this.copyright;
  }

  public String getDate() {
    return this.date;
  }

  public TGMeasureHeader getMeasureHeader(int index) {
    return (TGMeasureHeader) this.measureHeaders.get(index);
  }

  public List<TGMeasureHeader> getMeasureHeaders() {
    return this.measureHeaders;
  }

  public String getName() {
    return this.name;
  }

  public TGTrack getTrack(int index) {
    return this.tracks.get(index);
  }

  public List<TGTrack> getTracks() {
    return this.tracks;
  }

  public String getTranscriber() {
    return this.transcriber;
  }

  public String getWriter() {
    return this.writer;
  }

  public boolean isEmpty() {
    return this.measureHeaders.isEmpty() || this.tracks.isEmpty();
  }

  public void moveTrack(int index, TGTrack track) {
    this.tracks.remove(track);
    this.tracks.add(index, track);
  }

  public void removeMeasureHeader(int index) {
    this.measureHeaders.remove(index);
  }

  public void removeMeasureHeader(TGMeasureHeader measureHeader) {
    this.measureHeaders.remove(measureHeader);
  }

  public void removeTrack(TGTrack track) {
    this.tracks.remove(track);
    track.clear();
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTranscriber(String transcriber) {
    this.transcriber = transcriber;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }
}
