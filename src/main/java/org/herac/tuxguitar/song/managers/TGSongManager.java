/*
 * Created on 23-nov-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.song.managers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMarker;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGString;
import org.herac.tuxguitar.song.models.TGTempo;
import org.herac.tuxguitar.song.models.TGTimeSignature;
import org.herac.tuxguitar.song.models.TGTrack;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGSongManager {
  public static final short MAX_CHANNELS = 16;

  public static List<TGString> createPercussionStrings(int stringCount) {
    List<TGString> strings = new ArrayList<TGString>();
    for (int i = 1; i <= stringCount; i++) {
      strings.add(new TGString(i, 0));
    }
    return strings;
  }

  public static long getDivisionLength(TGMeasureHeader header) {
    long defaultLenght = TGDuration.QUARTER_TIME;
    int denominator = header.getTimeSignature().getDenominator().getValue();
    switch (denominator) {
    case TGDuration.EIGHTH:
      if (header.getTimeSignature().getNumerator() % 3 == 0) {
        defaultLenght += TGDuration.QUARTER_TIME / 2;
      }
      break;
    }
    return defaultLenght;
  }

  private TGMeasureManager measureManager;

  private TGSong song;

  private TGTrackManager trackManager;

  /**
   * Agrega un Compas
   */
  public void addMeasureHeader(int index, TGMeasureHeader measure) {
    getSong().addMeasureHeader(index, measure);
  }

  /**
   * Agrega un Compas
   */
  public void addMeasureHeader(TGMeasureHeader measure) {
    getSong().addMeasureHeader(measure);
  }

  public void addNewMeasure(int number) {
    // Obtengo un clon para el nuevo Header.
    TGMeasureHeader header = null;
    if (number == 1) {
      header = getMeasureHeader(number).clone();
    } else {
      header = getMeasureHeader((number - 1)).clone();
      header.setStart(header.getStart() + header.getLength());
      header.setNumber(header.getNumber() + 1);
    }
    header.setMarker(null);
    header.setRepeatOpen(false);
    header.setRepeatAlternative(0);
    header.setRepeatClose(0);

    // Si hay Headers siguientes los muevo
    TGMeasureHeader nextHeader = getMeasureHeader(number);
    if (nextHeader != null) {
      moveMeasureHeaders(getMeasureHeadersBeforeEnd(nextHeader.getStart()),
          header.getLength(), 1, true);
    }

    // Agrego el header a la lista
    addMeasureHeader((header.getNumber() - 1), header);

    // Agrego los compases en todas las pistas
    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().addNewMeasure(track, header);
    }
  }

  public TGMeasureHeader addNewMeasureBeforeEnd() {
    TGMeasureHeader lastHeader = getLastMeasureHeader();
    TGMeasureHeader header = new TGMeasureHeaderImpl();
    header.setNumber((lastHeader.getNumber() + 1));
    header.setStart((lastHeader.getStart() + lastHeader.getLength()));
    header.setRepeatOpen(false);
    header.setRepeatClose(0);
    header.setTripletFeel(lastHeader.getTripletFeel());

    header.setTimeSignature(lastHeader.getTimeSignature().clone());
    header.setTempo(lastHeader.getTempo().clone());
    getSong().addMeasureHeader(header);

    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().addNewMeasureBeforeEnd(track, header);
    }
    return header;
  }

  public void addTrack(TGTrack trackToAdd) {
    this.orderTracks();
    int addIndex = -1;
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      if (addIndex == -1 && track.getNumber() == trackToAdd.getNumber()) {
        addIndex = i;
      }
      if (addIndex >= 0) {
        track.setNumber(track.getNumber() + 1);
      }
    }
    if (addIndex < 0) {
      addIndex = getSong().countTracks();
    }
    getSong().addTrack(addIndex, trackToAdd);
  }

  public void autoCompleteSilences() {
    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().autoCompleteSilences(track);
    }
  }

  public void changeAlternativeRepeat(long start, int repeatAlternative) {
    TGMeasureHeader header = getMeasureHeaderAt(start);
    header.setRepeatAlternative(repeatAlternative);
  }

  public void changeCloseRepeat(long start, int repeatClose) {
    TGMeasureHeader header = getMeasureHeaderAt(start);
    header.setRepeatClose(repeatClose);
  }

  public void changeOpenRepeat(long start) {
    TGMeasureHeader header = getMeasureHeaderAt(start);
    header.setRepeatOpen(!header.isRepeatOpen());
  }

  public void changeTempo(TGMeasureHeader header, TGTempo tempo) {
    header.setTempo(tempo.clone());
  }

  public void changeTempos(long start, TGTempo tempo, boolean toEnd) {
    changeTempos(getMeasureHeaderAt(start), tempo, toEnd);
  }

  public void changeTempos(TGMeasureHeader header, TGTempo tempo, boolean toEnd) {
    int oldValue = header.getTempo().getValue();
    for (final TGMeasureHeader nextHeader : getMeasureHeadersAfter(header
        .getNumber() - 1)) {
      if (toEnd || nextHeader.getTempo().getValue() == oldValue) {
        changeTempo(nextHeader, tempo);
      } else {
        break;
      }
    }
  }

  public void changeTimeSignature(long start, TGTimeSignature timeSignature,
      boolean toEnd) {
    changeTimeSignature(getMeasureHeaderAt(start), timeSignature, toEnd);
  }

  public void changeTimeSignature(TGMeasureHeader header,
      TGTimeSignature timeSignature, boolean toEnd) {
    // asigno el nuevo ritmo
    header.setTimeSignature(timeSignature.clone());

    long nextStart = header.getStart() + header.getLength();
    for (final TGMeasureHeader nextHeader : getMeasureHeadersBeforeEnd(header
        .getStart() + 1)) {

      long theMove = nextStart - nextHeader.getStart();

      // moveMeasureComponents(nextHeader,theMove);
      moveMeasureHeader(nextHeader, theMove, 0);

      if (toEnd) {
        nextHeader.setTimeSignature(timeSignature.clone());
      }
      nextStart = nextHeader.getStart() + nextHeader.getLength();
    }
    moveOutOfBoundsBeatsToNewMeasure(header.getStart());
  }

  /*
   * public void changeTimeSignature(TGMeasureHeader header,TGTimeSignature
   * timeSignature,boolean toEnd){ //asigno el nuevo ritmo
   * timeSignature.copy(header.getTimeSignature());
   * 
   * long nextStart = header.getStart() + header.getLength(); List measures =
   * getMeasureHeadersBeforeEnd(header.getStart() + 1); Iterator it =
   * measures.iterator(); while(it.hasNext()){ TGMeasureHeader nextHeader =
   * (TGMeasureHeader)it.next();
   * 
   * long theMove = nextStart - nextHeader.getStart();
   * 
   * moveMeasureComponents(nextHeader,theMove);
   * moveMeasureHeader(nextHeader,theMove,0);
   * 
   * if(toEnd){ timeSignature.copy(nextHeader.getTimeSignature()); } nextStart =
   * nextHeader.getStart() + nextHeader.getLength(); } }
   */
  public void changeTripletFeel(long start, int tripletFeel, boolean toEnd) {
    changeTripletFeel(getMeasureHeaderAt(start), tripletFeel, toEnd);
  }

  public void changeTripletFeel(TGMeasureHeader header, int tripletFeel,
      boolean toEnd) {
    // asigno el nuevo tripletFeel
    header.setTripletFeel(tripletFeel);

    if (toEnd) {
      for (final TGMeasureHeader nextHeader : getMeasureHeadersBeforeEnd(header
          .getStart() + 1)) {
        nextHeader.setTripletFeel(tripletFeel);
      }
    }
  }

  public void clearSong() {
    if (this.getSong() != null) {
      this.getSong().clear();
    }
  }

  public TGTrack cloneTrack(TGTrack track) {
    TGTrack clone = track.clone(getSong());
    clone.setNumber(getNextTrackNumber());
    addTrack(clone);
    return clone;
  }

  public int countTracksForChannel(int channel) {
    int count = 0;
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      if (channel == track.getChannel().getChannel()) {
        count++;
      }
    }
    return count;
  }

  public List<TGString> createDefaultInstrumentStrings() {
    List<TGString> strings = new ArrayList<TGString>();
    strings.add(new TGString(1, 64));
    strings.add(new TGString(2, 59));
    strings.add(new TGString(3, 55));
    strings.add(new TGString(4, 50));
    strings.add(new TGString(5, 45));
    strings.add(new TGString(6, 40));
    return strings;
  }

  public TGTrack createTrack() {
    if (getSong().isEmpty()) {
      setSong(newSong());
      return getLastTrack();
    }
    TGTrack track = makeNewTrack();
    addTrack(track);
    return track;
  }

  public TGMarker getFirstMarker() {
    TGMeasureHeader first = null;
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker()) {
        if (first == null || header.getNumber() < first.getNumber()) {
          first = header;
        }
      }
    }
    return (first != null) ? first.getMarker() : null;
  }

  /*
   * public TGSongSegment copyMeasures(int m1, int m2){ TGSongSegment segment =
   * new TGSongSegment(); int number1 = Math.max(1,m1); int number2 =
   * Math.min(getSong().countMeasureHeaders(),m2); for(int number = number1;
   * number <= number2;number ++){ segment.getHeaders().add(
   * getMeasureHeader(number) ); } Iterator it = getSong().getTracks();
   * while(it.hasNext()){ TGTrack track = (TGTrack)it.next(); List measures =
   * getTrackManager().copyMeasures(track,number1,number2);
   * segment.addTrack(track.getNumber(),measures); } return
   * segment.clone(getFactory()); }
   * 
   * public TGSongSegment copyMeasures(int m1, int m2,TGTrack track){
   * TGSongSegment segment = new TGSongSegment(); int number1 = Math.max(1,m1);
   * int number2 = Math.min(getSong().countMeasureHeaders(),m2); for(int number
   * = number1; number <= number2;number ++){ segment.getHeaders().add(
   * getMeasureHeader(number) ); } List measures =
   * getTrackManager().copyMeasures(track,number1,number2);
   * segment.addTrack(track.getNumber(),measures);
   * 
   * return segment.clone(getFactory()); }
   * 
   * public void insertMeasures(TGSongSegment segment,int fromNumber,long move){
   * List headers = new ArrayList();
   * moveMeasureHeaders(segment.getHeaders(),move,0,false);
   * 
   * int headerNumber = fromNumber; Iterator it =
   * segment.getHeaders().iterator(); while(it.hasNext()){ TGMeasureHeader
   * header = (TGMeasureHeader)it.next(); header.setNumber(headerNumber);
   * headers.add(header); headerNumber ++; } long start =
   * ((TGMeasureHeader)headers.get(0)).getStart(); long end =
   * ((TGMeasureHeader)headers.get(headers.size() - 1)).getStart() +
   * ((TGMeasureHeader)headers.get(headers.size() - 1)).getLength(); List
   * headersBeforeEnd = getMeasureHeadersBeforeEnd(start);
   * moveMeasureHeaders(headersBeforeEnd,end - start,headers.size(),true);
   * 
   * it = segment.getHeaders().iterator(); while(it.hasNext()){ TGMeasureHeader
   * header = (TGMeasureHeader)it.next(); addMeasureHeader(header.getNumber() -
   * 1,header); }
   * 
   * it = getSong().getTracks(); while (it.hasNext()) { TGTrack currTrack =
   * (TGTrack) it.next(); List measures = null;
   * 
   * Iterator tracks = segment.getTracks().iterator(); while(tracks.hasNext()){
   * TGTrackSegment tSegment = (TGTrackSegment)tracks.next();
   * if(tSegment.getTrack() == currTrack.getNumber()){ measures =
   * tSegment.getMeasures(); break; } } if(measures == null){ measures =
   * getEmptyMeasures
   * (((TGTrackSegment)segment.getTracks().get(0)).getMeasures()); }
   * 
   * for(int i = 0;i < measures.size();i++){ TGMeasure measure =
   * (TGMeasure)measures.get(i);
   * measure.setHeader((TGMeasureHeader)headers.get(i));
   * getMeasureManager().moveAllComponents(measure,move); }
   * getTrackManager().insertMeasures(currTrack,measures); } }
   * 
   * private List getEmptyMeasures(List measures) { List emptyMeasures = new
   * ArrayList();
   * 
   * Iterator it = measures.iterator(); while (it.hasNext()) { TGMeasure measure
   * = (TGMeasure) it.next(); TGMeasure emptyMeasure =
   * getFactory().newMeasure(null); emptyMeasure.setClef(measure.getClef());
   * emptyMeasure.setKeySignature(measure.getKeySignature());
   * emptyMeasures.add(emptyMeasure); } return emptyMeasures; }
   */
  /*
   * public void replaceMeasures(TGSongSegment tracksMeasures,long move) { List
   * measureHeaders = new ArrayList();
   * moveMeasureHeaders(tracksMeasures.getHeaders(),move,0,false); Iterator it =
   * tracksMeasures.getHeaders().iterator(); while(it.hasNext()){
   * TGMeasureHeader header = (TGMeasureHeader)it.next(); TGMeasureHeader
   * replace = replaceMeasureHeader(header);
   * 
   * Iterator nextHeaders =
   * getMeasureHeadersAfter(replace.getNumber()).iterator(); long nextStart =
   * (replace.getStart() + replace.getLength()); while(nextHeaders.hasNext()){
   * TGMeasureHeader next = (TGMeasureHeader)nextHeaders.next();
   * moveMeasureComponents(next, (nextStart - next.getStart() ));
   * moveMeasureHeader(next, (nextStart - next.getStart() ) , 0); nextStart =
   * (next.getStart() + next.getLength()); } measureHeaders.add(replace); }
   * 
   * it = tracksMeasures.getTracks().iterator(); while(it.hasNext()){
   * TGTrackSegment trackMeasure = (TGTrackSegment)it.next();
   * 
   * TGTrack currTrack = getTrack(trackMeasure.getTrack()); List measures =
   * trackMeasure.getMeasures(); for(int i = 0;i < measures.size();i++){
   * TGMeasure measure = (TGMeasure)measures.get(i);
   * measure.setHeader((TGMeasureHeader)measureHeaders.get(i));
   * getMeasureManager().moveAllComponents(measure,move);
   * getTrackManager().replaceMeasure(currTrack,measure); } } }
   */
  public TGMeasureHeader getFirstMeasureHeader() {
    TGMeasureHeader firstHeader = null;
    for (int i = 0; i < getSong().countMeasureHeaders(); i++) {
      TGMeasureHeader currHeader = getSong().getMeasureHeader(i);
      if (firstHeader == null
          || (currHeader.getStart() < firstHeader.getStart())) {
        firstHeader = currHeader;
      }
    }
    return firstHeader;
  }

  public TGTrack getFirstTrack() {
    TGTrack track = null;
    if (!getSong().isEmpty()) {
      track = getSong().getTrack(0);
    }
    return track;
  }

  public TGChannel getFreeChannel(short instrument, boolean isPercussion) {
    if (isPercussion) {
      return TGChannel.newPercussionChannel();
    }
    short normalChannel = -1;
    short effectChannel = -1;

    boolean[] usedChannels = getUsedChannels();
    boolean[] usedEffectChannels = getUsedEffectChannels();
    for (short i = 0; i < MAX_CHANNELS; i++) {
      if (!TGChannel.isPercussionChannel(i) && !usedChannels[i]
          && !usedEffectChannels[i]) {
        normalChannel = (normalChannel < 0) ? i : normalChannel;
        effectChannel = (effectChannel < 0 && i != normalChannel) ? i
            : effectChannel;
      }
    }
    if (normalChannel < 0 || effectChannel < 0) {
      if (normalChannel >= 0) {
        effectChannel = normalChannel;
      } else {
        TGChannel songChannel = getLastTrack().getChannel();
        return songChannel.clone();
      }
    }
    TGChannel channel = new TGChannel();
    channel.setChannel(normalChannel);
    channel.setEffectChannel(effectChannel);
    channel.setInstrument(instrument);
    return channel;
  }

  public TGMarker getLastMarker() {
    TGMeasureHeader next = null;
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker()) {
        if (next == null || header.getNumber() > next.getNumber()) {
          next = header;
        }
      }
    }
    return (next != null) ? next.getMarker() : null;
  }

  public TGMeasureHeader getLastMeasureHeader() {
    int lastIndex = getSong().countMeasureHeaders() - 1;
    return getSong().getMeasureHeader(lastIndex);
  }

  public TGTrack getLastTrack() {
    TGTrack track = null;
    if (!getSong().isEmpty()) {
      track = getSong().getTrack(getSong().countTracks() - 1);
    }
    return track;
  }

  public TGMarker getMarker(int number) {
    TGMeasureHeader header = getMeasureHeader(number);
    if (header != null && header.hasMarker()) {
      return header.getMarker();
    }
    return null;
  }

  public List<TGMarker> getMarkers() {
    List<TGMarker> markers = new ArrayList<TGMarker>();
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker()) {
        markers.add(header.getMarker());
      }
    }
    return markers;
  }

  public TGMeasureHeader getMeasureHeader(int number) {
    for (int i = 0; i < getSong().countMeasureHeaders(); i++) {
      TGMeasureHeader header = getSong().getMeasureHeader(i);
      if (header.getNumber() == number) {
        return header;
      }
    }
    return null;
  }

  public TGMeasureHeader getMeasureHeaderAt(long start) {
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      long measureStart = header.getStart();
      long measureLength = header.getLength();
      if (start >= measureStart && start < measureStart + measureLength) {
        return header;
      }
    }
    return null;
  }

  /**
   * Retorna Todos los desde Start hasta el final del compas
   */
  public List<TGMeasureHeader> getMeasureHeadersAfter(int number) {
    List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.getNumber() > number) {
        headers.add(header);
      }
    }
    return headers;
  }

  /**
   * Retorna Todos los desde Start hasta el final del compas
   */
  public List<TGMeasureHeader> getMeasureHeadersBeforeEnd(long fromStart) {
    List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.getStart() >= fromStart) {
        headers.add(header);
      }
    }
    return headers;
  }

  /**
   * Retorna Todos los desde Start hasta el final del compas
   */
  public List<TGMeasureHeader> getMeasureHeadersBetween(long p1, long p2) {
    List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if ((header.getStart() + header.getLength()) > p1
          && header.getStart() < p2) {
        headers.add(header);
      }
    }
    return headers;
  }

  public TGMeasureManager getMeasureManager() {
    if (this.measureManager == null) {
      this.measureManager = new TGMeasureManager(this);
    }
    return this.measureManager;
  }

  public List<TGMeasure> getMeasures(long start) {
    List<TGMeasure> measures = new ArrayList<TGMeasure>();
    for (final TGTrack track : getSong().getTracks()) {
      TGMeasure measure = getTrackManager().getMeasureAt(track, start);
      if (measure != null) {
        measures.add(measure);
      }
    }
    return measures;
  }

  public TGMarker getNextMarker(int from) {
    TGMeasureHeader next = null;
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker() && header.getNumber() > from) {
        if (next == null || next.getNumber() > header.getNumber()) {
          next = header;
        }
      }
    }
    return (next != null) ? next.getMarker() : null;
  }

  public TGMeasureHeader getNextMeasureHeader(TGMeasureHeader header) {
    int nextIndex = header.getNumber();
    if (nextIndex < getSong().countMeasureHeaders()) {
      return getSong().getMeasureHeader(nextIndex);
    }
    return null;
  }

  public int getNextTrackNumber() {
    return (getSong().countTracks() + 1);
  }

  public TGMarker getPreviousMarker(int from) {
    TGMeasureHeader previous = null;
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker() && header.getNumber() < from) {
        if (previous == null || previous.getNumber() < header.getNumber()) {
          previous = header;
        }
      }
    }
    return (previous != null) ? previous.getMarker() : null;
  }

  public TGMeasureHeader getPrevMeasureHeader(TGMeasureHeader header) {
    int prevIndex = header.getNumber() - 1;
    if (prevIndex > 0) {
      return getSong().getMeasureHeader(prevIndex - 1);
    }
    return null;
  }

  public TGSong getSong() {
    return this.song;
  }

  public TGTrack getTrack(int number) {
    TGTrack track = null;
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack currTrack = getSong().getTrack(i);
      if (currTrack.getNumber() == number) {
        track = currTrack;
        break;
      }
    }
    return track;
  }

  public TGTrackManager getTrackManager() {
    if (this.trackManager == null) {
      this.trackManager = new TGTrackManager(this);
    }
    return this.trackManager;
  }

  public TGChannel getUsedChannel(int channel) {
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      if (channel == track.getChannel().getChannel()) {
        return track.getChannel().clone();
      }
    }
    return null;
  }

  public boolean[] getUsedChannels() {
    boolean[] channels = new boolean[MAX_CHANNELS];
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      channels[track.getChannel().getChannel()] = true;
    }
    return channels;
  }

  public boolean[] getUsedEffectChannels() {
    boolean[] channels = new boolean[MAX_CHANNELS];
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      channels[track.getChannel().getEffectChannel()] = true;
    }
    return channels;
  }

  /**
   * Retorna true si el start esta en el rango del compas
   */
  public boolean isAtPosition(TGMeasureHeader header, long start) {
    return (start >= header.getStart() && start < header.getStart()
        + header.getLength());
  }

  private TGTrack makeNewTrack() {
    TGTrack track = new TGTrackImpl();
    track.setNumber(getNextTrackNumber());
    track.setName("Track " + track.getNumber());
    // measures
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      TGMeasure measure = new TGMeasureImpl(header);
      track.addMeasure(measure);
    }
    track.setStrings(createDefaultInstrumentStrings());
    track.setChannel(getFreeChannel(TGChannel.DEFAULT_INSTRUMENT, false));
    track.setColor(Color.RED);
    return track;
  }

  /**
   * Mueve el compas
   */
  public void moveMeasureComponents(TGMeasureHeader header, long theMove) {
    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().moveMeasure(
          getTrackManager().getMeasure(track, header.getNumber()), theMove);
    }
  }

  /**
   * Mueve el compas
   */
  public void moveMeasureHeader(TGMeasureHeader header, long theMove,
      int numberMove) {
    header.setNumber(header.getNumber() + numberMove);
    header.setStart(header.getStart() + theMove);
  }

  public void moveMeasureHeaders(List<TGMeasureHeader> headers, long theMove,
      int numberMove, boolean moveComponents) {
    if (moveComponents) {
      for (final TGMeasureHeader header : headers) {
        moveMeasureComponents(header, theMove);
      }
    }

    for (final TGMeasureHeader header : headers) {
      moveMeasureHeader(header, theMove, numberMove);
    }
  }

  public void moveOutOfBoundsBeatsToNewMeasure(long start) {
    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().moveOutOfBoundsBeatsToNewMeasure(track, start);
    }
  }

  public boolean moveTrackDown(TGTrack track) {
    if (track.getNumber() < getSong().countTracks()) {
      TGTrack nextTrack = getTrack(track.getNumber() + 1);
      nextTrack.setNumber(nextTrack.getNumber() - 1);
      track.setNumber(track.getNumber() + 1);
      orderTracks();
      return true;
    }
    return false;
  }

  public boolean moveTrackUp(TGTrack track) {
    if (track.getNumber() > 1) {
      TGTrack prevTrack = getTrack(track.getNumber() - 1);
      prevTrack.setNumber(prevTrack.getNumber() + 1);
      track.setNumber(track.getNumber() - 1);
      orderTracks();
      return true;
    }
    return false;
  }

  public TGSong newSong() {
    TGSong song = new TGSong();

    TGMeasureHeader header = new TGMeasureHeaderImpl();
    header.setNumber(1);
    header.setStart(TGDuration.QUARTER_TIME);
    header.getTimeSignature().setNumerator(4);
    header.getTimeSignature().getDenominator().setValue(TGDuration.QUARTER);
    song.addMeasureHeader(header);

    TGMeasure measure = new TGMeasureImpl(header);

    TGTrack track = new TGTrackImpl();
    track.setNumber(1);
    track.setName("Track 1");
    track.addMeasure(measure);
    track.getChannel().setChannel((short) 0);
    track.getChannel().setEffectChannel((short) 1);
    track.setStrings(createDefaultInstrumentStrings());
    track.setColor(Color.RED);
    song.addTrack(track);

    return song;
  }

  public void orderBeats() {
    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().orderBeats(track);
    }
  }

  private void orderTracks() {
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack minTrack = null;
      for (int trackIdx = i; trackIdx < getSong().countTracks(); trackIdx++) {
        TGTrack track = getSong().getTrack(trackIdx);
        if (minTrack == null || track.getNumber() < minTrack.getNumber()) {
          minTrack = track;
        }
      }
      getSong().moveTrack(i, minTrack);
    }
  }

  public void removeAllMarkers() {
    for (final TGMeasureHeader header : getSong().getMeasureHeaders()) {
      if (header.hasMarker()) {
        header.setMarker(null);
      }
    }
  }

  public void removeLastMeasure() {
    removeLastMeasureHeader();
  }

  public void removeLastMeasureHeader() {
    removeMeasureHeader(getLastMeasureHeader());
  }

  public void removeMarker(int number) {
    TGMeasureHeader header = getMeasureHeader(number);
    if (header != null && header.hasMarker()) {
      header.setMarker(null);
    }
  }

  public void removeMarker(TGMarker marker) {
    if (marker != null) {
      removeMarker(marker.getMeasure());
    }
  }

  public void removeMeasure(int number) {
    removeMeasureHeader(number);
  }

  public void removeMeasure(long start) {
    removeMeasureHeader(start);
  }

  public void removeMeasureHeader(int number) {
    removeMeasureHeader(getMeasureHeader(number));
  }

  public void removeMeasureHeader(long start) {
    removeMeasureHeader(getMeasureHeaderAt(start));
  }

  public void removeMeasureHeader(TGMeasureHeader header) {
    long start = header.getStart();
    long length = header.getLength();

    for (final TGTrack track : getSong().getTracks()) {
      getTrackManager().removeMeasure(track, start);
    }
    moveMeasureHeaders(getMeasureHeadersBeforeEnd(start + 1), -length, -1, true);
    getSong().removeMeasureHeader(header.getNumber() - 1);
  }

  public void removeMeasureHeaders(int n1, int n2) {
    for (int i = n1; i <= n2; i++) {
      TGMeasureHeader measure = getMeasureHeader(n1);
      removeMeasureHeader(measure);
    }
    /*
     * Iterator it = getMeasureHeadersBetween(p1,p2).iterator();
     * while(it.hasNext()){ TGMeasureHeader measure =
     * (TGMeasureHeader)it.next(); removeMeasureHeader(measure); }
     */
  }

  public void removeTrack(int number) {
    int nextNumber = number;
    TGTrack trackToRemove = null;
    orderTracks();
    for (final TGTrack currTrack : getSong().getTracks()) {
      if (trackToRemove == null && currTrack.getNumber() == nextNumber) {
        trackToRemove = currTrack;
      } else if (currTrack.getNumber() == (nextNumber + 1)) {
        currTrack.setNumber(nextNumber);
        nextNumber++;
      }

    }
    getSong().removeTrack(trackToRemove);
  }

  public void removeTrack(TGTrack track) {
    removeTrack(track.getNumber());
  }

  public TGMeasureHeader replaceMeasureHeader(TGMeasureHeader newMeasure) {
    TGMeasureHeader header = getMeasureHeaderAt(newMeasure.getStart());
    header.makeEqual(newMeasure.clone());
    return header;
  }

  public TGTrack replaceTrack(TGTrack track) {
    
    final int number = track.getNumber();
    
    final List<TGTrack> tracks = this.song.getTracks();
    
    for (int i = 0; i < tracks.size(); i++) {
      if (tracks.get(i).getNumber() == number) {
        tracks.set(i, track.clone(this.song));
        return tracks.get(i);
      }
    }

    return null;
  }

  public void setProperties(String name, String artist, String album,
      String author, String date, String copyright, String writer,
      String transcriber, String comments) {
    getSong().setName(name);
    getSong().setArtist(artist);
    getSong().setAlbum(album);
    getSong().setAuthor(author);
    getSong().setDate(date);
    getSong().setCopyright(copyright);
    getSong().setWriter(writer);
    getSong().setTranscriber(transcriber);
    getSong().setComments(comments);
  }

  public void setSong(TGSong song) {
    if (song != null) {
      this.clearSong();
      this.song = song;
    }
  }

  public void setSongName(String name) {
    getSong().setName(name);
  }

  public void updateChannel(TGChannel channel) {
    for (int i = 0; i < getSong().countTracks(); i++) {
      TGTrack track = getSong().getTrack(i);
      if (channel.getChannel() == track.getChannel().getChannel()) {
        track.setChannel(channel.clone());
      }
    }
  }

  public TGMarker updateMarker(int measure, String title, Color color) {
    TGMeasureHeader header = getMeasureHeader(measure);
    if (header != null) {
      if (!header.hasMarker()) {
        header.setMarker(new TGMarker());
      }
      header.getMarker().setMeasure(measure);
      header.getMarker().setTitle(title);
      header.getMarker().setColor(color);
      return header.getMarker();
    }
    return null;
  }

  public TGMarker updateMarker(TGMarker marker) {
    return updateMarker(marker.getMeasure(), marker.getTitle(), marker
        .getColor());
  }
}
