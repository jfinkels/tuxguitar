package org.herac.tuxguitar.song.helpers;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.managers.TGTrackManager;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGTrack;

public class TGSongSegmentHelper {

  private TGSongManager sm;

  public TGSongSegmentHelper(TGSongManager sm) {
    this.sm = sm;
  }

  public TGSongSegment copyMeasures(int m1, int m2) {
    TGSongSegment segment = new TGSongSegment();
    int number1 = Math.max(1, m1);
    int number2 = Math.min(this.sm.getSong().countMeasureHeaders(), m2);
    for (int number = number1; number <= number2; number++) {
      segment.getHeaders().add(this.sm.getMeasureHeader(number));
    }
    for (final TGTrack track : this.sm.getSong().getTracks()) {
      List<TGMeasure> measures = new ArrayList<TGMeasure>();
      for (int number = number1; number <= number2; number++) {
        measures.add(this.sm.getTrackManager().getMeasure(track, number));
      }
      segment.addTrack(track.getNumber(), measures);
    }
    return segment.clone();
  }

  public TGSongSegment copyMeasures(int m1, int m2, TGTrack track) {
    TGSongSegment segment = new TGSongSegment();
    List<TGMeasure> measures = new ArrayList<TGMeasure>();
    int number1 = Math.max(1, m1);
    int number2 = Math.min(this.sm.getSong().countMeasureHeaders(), m2);
    for (int number = number1; number <= number2; number++) {
      segment.getHeaders().add(this.sm.getMeasureHeader(number));
      measures.add(this.sm.getTrackManager().getMeasure(track, number));
    }
    segment.addTrack(track.getNumber(), measures);
    return segment.clone();
  }

  public TGSongSegment createSegmentCopies(TGSongSegment srcSegment, int count) {
    TGSongSegment segment = srcSegment.clone();

    int mCount = segment.getHeaders().size();
    int tCount = segment.getTracks().size();

    TGMeasureHeader fMeasure = segment.getHeaders().get(0);
    TGMeasureHeader lMeasure = segment.getHeaders().get(mCount - 1);

    long mMove = ((lMeasure.getStart() + lMeasure.getLength()) - fMeasure
        .getStart());
    for (int i = 1; i < count; i++) {
      for (int m = 0; m < mCount; m++) {
        TGMeasureHeader header = segment.getHeaders().get(m).clone();
        segment.getHeaders().add(header);
        this.sm.moveMeasureHeader(header, (mMove * i), (mCount * i));
        for (int t = 0; t < tCount; t++) {
          TGTrackSegment track = segment.getTracks().get(t);
          TGMeasure measure = track.getMeasures().get(m).clone(header);
          track.getMeasures().add(measure);
          this.sm.getMeasureManager().moveAllBeats(measure, (mMove * i));
        }
      }
    }

    return segment;
  }

  private List<TGMeasure> getEmptyMeasures(int count, int clef, int keySignature) {
    List<TGMeasure> measures = new ArrayList<TGMeasure>();
    for (int i = 0; i < count; i++) {
      TGMeasure measure = new TGMeasureImpl(null);
      measure.setClef(clef);
      measure.setKeySignature(keySignature);
      measures.add(measure);
    }
    return measures;
  }

  public void insertMeasures(TGSongSegment segment, int fromNumber, long move,
      int track) {
    List<TGMeasureHeader> headers = new ArrayList<TGMeasureHeader>();
    this.sm.moveMeasureHeaders(segment.getHeaders(), move, 0, false);

    int headerNumber = fromNumber;

    for (final TGMeasureHeader header : segment.getHeaders()) {
      header.setNumber(headerNumber);
      headers.add(header);
      headerNumber++;
    }
    long start = headers.get(0).getStart();
    long end = headers.get(headers.size() - 1).getStart()
        + headers.get(headers.size() - 1).getLength();
    List<TGMeasureHeader> headersBeforeEnd = this.sm
        .getMeasureHeadersBeforeEnd(start);
    this.sm.moveMeasureHeaders(headersBeforeEnd, end - start, headers.size(),
        true);

    for (final TGMeasureHeader header : segment.getHeaders()) {
      this.sm.addMeasureHeader(header.getNumber() - 1, header);
    }

    for (final TGTrack currTrack : this.sm.getSong().getTracks()) {
      List<TGMeasure> measures = null;

      for (final TGTrackSegment tSegment : segment.getTracks()) {
        if (((track > 0 && segment.getTracks().size() == 1) ? track : tSegment
            .getTrack()) == currTrack.getNumber()) {
          measures = tSegment.getMeasures();
          break;
        }
      }

      if (measures == null) {
        TGTrackManager tm = this.sm.getTrackManager();
        TGMeasure measure = (fromNumber > 1 ? tm.getMeasure(currTrack,
            (fromNumber - 1)) : tm.getMeasure(currTrack, headerNumber));
        int clef = (measure != null ? measure.getClef()
            : TGMeasure.DEFAULT_CLEF);
        int keySignature = (measure != null ? measure.getKeySignature()
            : TGMeasure.DEFAULT_KEY_SIGNATURE);
        measures = getEmptyMeasures(segment.getHeaders().size(), clef,
            keySignature);
      }

      for (int i = 0; i < measures.size(); i++) {
        TGMeasure measure = (TGMeasure) measures.get(i);
        measure.setHeader((TGMeasureHeader) headers.get(i));
        this.sm.getMeasureManager().moveAllBeats(measure, move);
      }

      insertMeasures(currTrack, measures);
    }
  }

  public void insertMeasures(TGTrack track, List<TGMeasure> measures) {
    if (!measures.isEmpty()) {
      for (final TGMeasure measure : measures) {
        this.sm.getMeasureManager().removeNotesAfterString(measure,
            track.stringCount());
        this.sm.getTrackManager().addMeasure(track, (measure.getNumber() - 1),
            measure);
      }
    }
  }

  public void replaceMeasures(TGSongSegment segment, long move, int track) {
    boolean replaceHeader = (track == 0 || this.sm.getSong().countTracks() == 1);

    List<TGMeasureHeader> measureHeaders = new ArrayList<TGMeasureHeader>();
    this.sm.moveMeasureHeaders(segment.getHeaders(), move, 0, false);

    for (final TGMeasureHeader header : segment.getHeaders()) {
      TGMeasureHeader replace = (replaceHeader ? this.sm
          .replaceMeasureHeader(header) : this.sm.getMeasureHeaderAt(header
          .getStart()));

      long nextStart = (replace.getStart() + replace.getLength());

      for (final TGMeasureHeader next : this.sm.getMeasureHeadersAfter(replace
          .getNumber())) {
        this.sm.moveMeasureComponents(next, (nextStart - next.getStart()));
        this.sm.moveMeasureHeader(next, (nextStart - next.getStart()), 0);
        nextStart = (next.getStart() + next.getLength());
      }
      measureHeaders.add(replace);
    }

    for (final TGTrackSegment tSegment : segment.getTracks()) {
      TGTrack currTrack = this.sm.getTrack((track > 0 && segment.getTracks()
          .size() == 1) ? track : tSegment.getTrack());
      if (currTrack != null) {
        for (int i = 0; i < tSegment.getMeasures().size(); i++) {
          TGMeasure measure = (TGMeasure) tSegment.getMeasures().get(i);
          measure.setHeader((TGMeasureHeader) measureHeaders.get(i));
          // this.sm.getMeasureManager().removeNotesAfterString(measure,
          // currTrack.stringCount());
          // this.sm.getMeasureManager().moveAllBeats(measure,move);
          this.sm.getMeasureManager().moveAllBeats(measure, move);
          this.sm.getMeasureManager().removeVoicesOutOfTime(measure);
          this.sm.getMeasureManager().removeNotesAfterString(measure,
              currTrack.stringCount());
          this.sm.getTrackManager().replaceMeasure(currTrack, measure);
        }
      }
    }
  }
}
