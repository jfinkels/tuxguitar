/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.song.factory.TGFactory;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TGSong {

  private String album;
  private String artist;
  private String author;
  private String comments;
  private String copyright;
  private String date;
  private List measureHeaders;
  private String name;
  private List tracks;
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
    this.tracks = new ArrayList();
    this.measureHeaders = new ArrayList();
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
    Iterator tracks = getTracks();
    while (tracks.hasNext()) {
      TGTrack track = (TGTrack) tracks.next();
      track.clear();
    }
    this.tracks.clear();
    this.measureHeaders.clear();
  }

  public TGSong clone(TGFactory factory) {
    TGSong song = factory.newSong();
    copy(factory, song);
    return song;
  }

  public void copy(TGFactory factory, TGSong song) {
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
    Iterator headers = getMeasureHeaders();
    while (headers.hasNext()) {
      TGMeasureHeader header = (TGMeasureHeader) headers.next();
      song.addMeasureHeader(header.clone(factory));
    }
    Iterator tracks = getTracks();
    while (tracks.hasNext()) {
      TGTrack track = (TGTrack) tracks.next();
      song.addTrack(track.clone(factory, song));
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

  public Iterator getMeasureHeaders() {
    return this.measureHeaders.iterator();
  }

  public String getName() {
    return this.name;
  }

  public TGTrack getTrack(int index) {
    return (TGTrack) this.tracks.get(index);
  }

  public Iterator getTracks() {
    return this.tracks.iterator();
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
