package org.herac.tuxguitar.midiinput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGNoteImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.managers.TGTrackManager;
import org.herac.tuxguitar.song.models.TGBeat;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGMeasureHeader;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGTrack;

class MiStaff {
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger.getLogger(MiStaff.class);

  static long ticksToTimestamp(int inTempo, long inTicks) {
    long timeStamp = (inTicks * 60000000L)
        / (inTempo * TGDuration.QUARTER_TIME);

    return (timeStamp);
  }

  static long timestampToTicks(int inTempo, long inTimeStamp) {
    long ticks = (inTimeStamp * inTempo * TGDuration.QUARTER_TIME) / 60000000L;

    return (ticks);
  }

  private boolean f_Dump_Input = false; // for debugging...

  private boolean f_Dump_TrackGeneration = false; // for debugging...

  private Map<Long, MiStaffEvent> f_Events = new TreeMap<Long, MiStaffEvent>(); // staff
  // events
  // map

  private TGTrack f_TgTrack = null; // work track

  MiStaff(final List<MiNote> inBufferNotes, // MIDI input notes from buffer
      // [microseconds]
      int inTempo, // quarters per minute
      long inStartTime, // first MIDI time stamp [microseconds]
      long inStopTime, // last MIDI time stamp [microseconds]
      long inStartPosition, // recording start position [ticks]
      String inTrackName) // name for the TuxGuitar track to be created
  {
    List<MiNote> midiNotes = new ArrayList<MiNote>();

    // make a deep copy of input buffer notes
    for (final MiNote note : inBufferNotes) {
      midiNotes.add(new MiNote(note));
    }

    // convert time stamps from absolute microseconds to song relative ticks
    for (final MiNote note : midiNotes) {

      long timeOn = note.getTimeOn(), timeOff = note.getTimeOff();

      // absolute to relative time stamps
      timeOn -= inStartTime;
      timeOff -= inStartTime;

      // time stamps to ticks
      timeOn = inStartPosition + timestampToTicks(inTempo, timeOn);
      timeOff = inStartPosition + timestampToTicks(inTempo, timeOff);

      // update values
      note.setTimeOn(timeOn);
      note.setTimeOff(timeOff);
    }

    if (this.f_Dump_Input) {
      MiBuffer.dump(inBufferNotes, "input buffer MIDI notes");
      MiBuffer.dump(midiNotes, "converted MIDI notes");
    }

    TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();
    long startTick = inStartPosition, stopTick = inStartPosition
        + timestampToTicks(inTempo, inStopTime - inStartTime);
    TGMeasureHeader mh = tgSongMgr.getMeasureHeaderAt(startTick);
    long firstBarTick = mh.getStart();

    // insert bars into staff
    for (long tick = firstBarTick; tick <= stopTick; tick += 4 * TGDuration.QUARTER_TIME)
      addBar(tick);

    // insert note events into staff
    for (final MiNote note : midiNotes) {
      this.addNote(note);
    }

    // generate bars
    createMeasures();

    // generate beats
    insertNotesIntoTrack(inTrackName);
  }

  void addBar(long inTime) {
    MiStaffEvent se = this.f_Events.get(new Long(inTime));

    if (se == null) {
      se = new MiStaffEvent(inTime);
      this.f_Events.put(new Long(inTime), se);
    }

    se.markAsBar();
  }

  void addNote(MiNote inNote) {
    MiStaffEvent se = this.f_Events.get(new Long(inNote.getTimeOn()));

    if (se == null) {
      se = new MiStaffEvent(inNote.getTimeOn());
      this.f_Events.put(new Long(inNote.getTimeOn()), se);
    }

    se.addNoteOn(inNote);
  }

  private void addTiedNote(long inTime, MiStaffNote inSN,
      long inResidualDuration) {
    MiStaffEvent se = this.f_Events.get(new Long(inTime));

    if (se == null) {
      se = new MiStaffEvent(inTime);
      this.f_Events.put(new Long(inTime), se);
    }

    MiStaffNote sn = new MiStaffNote(inSN);

    sn.setDuration(inResidualDuration);
    se.addTiedNote(sn);
  }

  void createMeasures() {
    TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();

    for (final Entry<Long, MiStaffEvent> entry : this.f_Events.entrySet()) {
      final Long key = entry.getKey();
      final MiStaffEvent se = entry.getValue();

      if (se.isBar() && tgSongMgr.getMeasureHeaderAt(key.longValue()) == null) {
        tgSongMgr.addNewMeasure(tgSongMgr.getSong().countMeasureHeaders() + 1);
      }
    }
  }

  private void dump(String inTitle) {

    LOG.debug("");
    LOG.debug("MiStaff dump " + inTitle + "...");
    LOG.debug("");

    for (final MiStaffEvent se : this.f_Events.values()) {
      LOG.debug(se);
    }
  }

  private void generateTrack(String inTrackName) {
    TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();
    TGTrack tgTrack = tgSongMgr.createTrack();

    if (this.f_Dump_TrackGeneration) {
      LOG.debug("");
      LOG.debug("generating track: " + inTrackName + "...");
      LOG.debug("");
    }

    tgTrack.setName(inTrackName);

    this.f_TgTrack = tgTrack;

    for (final MiStaffEvent se : this.f_Events.values()) {
      se.setBeat(null);
    }

    // generate TuxGuitar track
    for (final MiStaffEvent se : this.f_Events.values()) {
      if (se.isOnBeat() || se.isTieBeat()) {
        final TGBeat tgBeat = getEventBeat(se.getBeginTime());

        for (final MiStaffNote sn : se.getNotes()) {
          insertNoteIntoTrack(tgBeat, sn);
        }
      }
    }
  }

  private TGBeat getEventBeat(long inTime) {
    MiStaffEvent se = this.f_Events.get(new Long(inTime));
    TGBeat tgBeat = se.getBeat();

    // creates a TGBeat if needed
    if (tgBeat == null) {
      TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();
      TGTrackManager tgTrackMgr = tgSongMgr.getTrackManager();
      TGMeasure tgMeasure = tgTrackMgr.getMeasureAt(this.f_TgTrack, inTime);

      if (tgMeasure != null) {
        tgBeat = new TGBeatImpl();

        tgBeat.setStart(inTime);
        tgMeasure.addBeat(tgBeat);
        se.setBeat(tgBeat);
      }
    }

    return (tgBeat);
  }

  private void insertNoteIntoTrack(TGBeat inTgBeat, MiStaffNote inSN) {
    // TGSongManager tgSongMgr = TuxGuitar.instance().getSongManager();
    TGNote tgNote = new TGNoteImpl();
    TGDuration tgDuration = new TGDuration();

    tgNote.setString(inSN.getString());
    tgNote.setValue(inSN.getFret());
    tgNote.setVelocity(inSN.getVelocity());
    tgNote.setTiedNote(inSN.isTied());

    int noteType = MiStaffNote.durationToNoteType(inSN.getNominalDuration());

    tgDuration.setValue(noteType);
    tgDuration.setDotted(inSN.getDotCount() == 1);
    tgDuration.setDoubleDotted(inSN.getDotCount() == 2);

    if (this.f_Dump_TrackGeneration) {
      LOG.debug(""
          + inTgBeat.getMeasure().getNumber()
          + " "
          + inTgBeat.getStart()
          + " ("
          + tgNote.getString()
          + ","
          + tgNote.getValue()
          + ","
          + tgNote.getVelocity()
          + ") "
          + "1/"
          + tgDuration.getValue()
          + ", d: "
          + tgDuration.getTime()
          + (tgNote.isTiedNote() ? " (tied)" : "")
          + (tgDuration.getTime() != inSN.getOverallDuration() ? " snDur="
              + inSN.getOverallDuration() : ""));
    }

    // here we probably should choose the voice
    // it would be nice to have one voice for each string...
    inTgBeat.getVoice(0).setDuration(tgDuration);
    inTgBeat.getVoice(0).addNote(tgNote);
  }

  void insertNotesIntoTrack(String inTrackName) {
    // normalize beats
    SortedMap<Long, MiStaffEvent> normalizedEvents = new TreeMap<Long, MiStaffEvent>();

    for (final MiStaffEvent se : this.f_Events.values()) {
      se.normalizeBeat(TGDuration.SIXTY_FOURTH);
      mergeEvent(normalizedEvents, se);
    }

    this.f_Events = normalizedEvents;

    dump("after beat normalization");
    generateTrack("after beat normalization");

    // add tie events due to bar crossing at the beginning of each crossed bar
    boolean keepGoing = true;

    while (keepGoing) {
      long nextBarBeginTime = 0;

      keepGoing = false;

      for (final MiStaffEvent se : this.f_Events.values()) {
        if (se.isBar())
          nextBarBeginTime = se.getBeginTime() + 4 * TGDuration.QUARTER_TIME; // bar
        // length
        // should
        // be
        // a
        // MiStaff
        // member

        for (final MiStaffNote sn : se.getNotes()) {
          if (se.getBeginTime() + sn.getOverallDuration() > nextBarBeginTime) {
            long limitedDuration = (nextBarBeginTime - se.getBeginTime()), residualDuration = sn
                .getOverallDuration()
                - limitedDuration;

            sn.setDuration(limitedDuration);
            addTiedNote(nextBarBeginTime, sn, residualDuration);
            keepGoing = true;
            break;
          }
        }

        if (keepGoing)
          break;
      }
    }

    dump("after tied due to bar crossing");
    generateTrack("after tied due to bar crossing");

    // normalize durations
    for (final MiStaffEvent se : this.f_Events.values()) {
      se.normalizeDurations();
    }

    dump("after duration normalization");
    generateTrack("after duration normalization");

    // add tie events due to note crossing
    keepGoing = true;

    while (keepGoing) {
      keepGoing = false;

      Iterator<Long> eventsIt2 = this.f_Events.keySet().iterator();

      if (eventsIt2.hasNext())
        eventsIt2.next();

      for (Iterator<Entry<Long, MiStaffEvent>> eventsIt = this.f_Events
          .entrySet().iterator(); eventsIt.hasNext();) {
        Entry<Long, MiStaffEvent> me = eventsIt.next();
        MiStaffEvent se = (MiStaffEvent) me.getValue();

        if (eventsIt.hasNext() && eventsIt2.hasNext()) {
          long nextTime = eventsIt2.next();

          for (final MiStaffNote sn : se.getNotes()) {
            if (se.getBeginTime() + sn.getOverallDuration() > nextTime) {
              long limitedDuration = (nextTime - se.getBeginTime()), residualDuration = sn
                  .getOverallDuration()
                  - limitedDuration;

              sn.setDuration(limitedDuration);
              addTiedNote(nextTime, sn, residualDuration);
              keepGoing = true;
              break;
            }
          }

          if (keepGoing)
            break;
        }
      }
    }

    dump("after tied due to note crossing");
    generateTrack("after tied due to note crossing");

    // normalize durations
    for (final MiStaffEvent se : this.f_Events.values()) {
      se.normalizeDurations();
    }

    dump("after duration normalization 2");
    generateTrack("after duration normalization 2");
  }

  private void mergeEvent(SortedMap<Long, MiStaffEvent> inEventsMap,
      MiStaffEvent inSE) {
    final MiStaffEvent se = inEventsMap.get(new Long(inSE.getBeginTime()));

    if (se == null)
      inEventsMap.put(new Long(inSE.getBeginTime()), inSE);
    else
      se.merge(inSE);
  }
}
