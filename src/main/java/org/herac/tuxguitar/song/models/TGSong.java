/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGSong {

  private String album;
  private String artist;
  private String author;
  private String comments;
  private String copyright;
  private String date;
  private List<TGMeasureHeader> measureHeaders;
  private String name;
  private List<TGTrack> tracks;
  private String transcriber;
  private String writer;

  public TGSong() {
    this.name = new String();
    this.artist = new String();
    this.album = new String();
    this.author = new String();
    this.date = new String();
    this.copyright = new String();
    this.writer = new String();
    this.transcriber = new String();
    this.comments = new String();
    this.tracks = new ArrayList<TGTrack>();
    this.measureHeaders = new ArrayList<TGMeasureHeader>();
  }

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
    this.addTrack(countTracks(), track);
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
    copy(song);
    return song;
  }

  public void copy(TGSong song) {
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
    return (TGTrack) this.tracks.get(index);
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
    return (countMeasureHeaders() == 0 || countTracks() == 0);
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
