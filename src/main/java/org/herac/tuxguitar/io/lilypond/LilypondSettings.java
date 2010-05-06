package org.herac.tuxguitar.io.lilypond;

public class LilypondSettings {

  public static final int ALL_TRACKS = -1;

  public static final int FIRST_MEASURE = -1;

  public static final int LAST_MEASURE = -1;

  public static LilypondSettings getDefaults() {
    LilypondSettings settings = new LilypondSettings();
    settings.setTrack(ALL_TRACKS);
    settings.setMeasureFrom(FIRST_MEASURE);
    settings.setMeasureTo(LAST_MEASURE);
    settings.setScoreEnabled(true);
    settings.setTablatureEnabled(true);
    settings.setTextEnabled(true);
    settings.setLyricsEnabled(true);
    settings.setChordDiagramEnabled(true);
    settings.setTrackNameEnabled(true);
    settings.setTrackGroupEnabled(false);
    return settings;
  }

  private boolean chordDiagramEnabled;
  private boolean lyricsEnabled;

  private int measureFrom;
  private int measureTo;
  private boolean scoreEnabled;
  private boolean tablatureEnabled;
  private boolean textEnabled;
  private int track;
  private boolean trackGroupEnabled;

  private boolean trackNameEnabled;

  public LilypondSettings() {
    super();
  }

  public void check() {
    if (!this.isScoreEnabled() && !this.isTablatureEnabled()) {
      this.setScoreEnabled(true);
      this.setTablatureEnabled(true);
    }
  }

  public int getMeasureFrom() {
    return this.measureFrom;
  }

  public int getMeasureTo() {
    return this.measureTo;
  }

  public int getTrack() {
    return this.track;
  }

  public boolean isChordDiagramEnabled() {
    return this.chordDiagramEnabled;
  }

  public boolean isLyricsEnabled() {
    return this.lyricsEnabled;
  }

  public boolean isScoreEnabled() {
    return this.scoreEnabled;
  }

  public boolean isTablatureEnabled() {
    return this.tablatureEnabled;
  }

  public boolean isTextEnabled() {
    return this.textEnabled;
  }

  public boolean isTrackGroupEnabled() {
    return this.trackGroupEnabled;
  }

  public boolean isTrackNameEnabled() {
    return this.trackNameEnabled;
  }

  public void setChordDiagramEnabled(boolean chordDiagramEnabled) {
    this.chordDiagramEnabled = chordDiagramEnabled;
  }

  public void setLyricsEnabled(boolean lyricsEnabled) {
    this.lyricsEnabled = lyricsEnabled;
  }

  public void setMeasureFrom(int measureFrom) {
    this.measureFrom = measureFrom;
  }

  public void setMeasureTo(int measureTo) {
    this.measureTo = measureTo;
  }

  public void setScoreEnabled(boolean scoreEnabled) {
    this.scoreEnabled = scoreEnabled;
  }

  public void setTablatureEnabled(boolean tablatureEnabled) {
    this.tablatureEnabled = tablatureEnabled;
  }

  public void setTextEnabled(boolean textEnabled) {
    this.textEnabled = textEnabled;
  }

  public void setTrack(int track) {
    this.track = track;
  }

  public void setTrackGroupEnabled(boolean trackGroupEnabled) {
    this.trackGroupEnabled = trackGroupEnabled;
  }

  public void setTrackNameEnabled(boolean trackNameEnabled) {
    this.trackNameEnabled = trackNameEnabled;
  }
}