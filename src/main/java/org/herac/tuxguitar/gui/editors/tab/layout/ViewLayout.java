/*
 * Created on 04-ene-2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.editors.tab.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.TGPainter;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.editors.tab.TGBeatImpl;
import org.herac.tuxguitar.gui.editors.tab.TGChordImpl;
import org.herac.tuxguitar.gui.editors.tab.TGLyricImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureHeaderImpl;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.editors.tab.TGResources;
import org.herac.tuxguitar.gui.editors.tab.TGTrackImpl;
import org.herac.tuxguitar.gui.editors.tab.TGTrackSpacing;
import org.herac.tuxguitar.gui.editors.tab.TGVoiceImpl;
import org.herac.tuxguitar.gui.editors.tab.Tablature;
import org.herac.tuxguitar.gui.system.config.TGConfigKeys;
import org.herac.tuxguitar.gui.system.config.TGConfigManager;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGDuration;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.TGVoice;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ViewLayout {
  public class TrackPosition {
    private int height;
    private int posY;
    private int track;

    public TrackPosition(int track, int posY, int height) {
      this.track = track;
      this.posY = posY;
      this.height = height;
    }

    public int getHeight() {
      return this.height;
    }

    public int getPosY() {
      return this.posY;
    }

    public int getTrack() {
      return this.track;
    }
  }

  public static final int MODE_LINEAR = 2;
  public static final int DEFAULT_MODE = MODE_LINEAR;
  public static final int DISPLAY_CHORD_DIAGRAM = 0x20;

  public static final int DISPLAY_CHORD_NAME = 0x10;
  public static final int DISPLAY_COMPACT = 0x01;
  public static final int DISPLAY_MULTITRACK = 0x02;
  public static final int DISPLAY_SCORE = 0x04;
  public static final int DISPLAY_TABLATURE = 0x08;

  public static final int MODE_PAGE = 1;
  private boolean bufferEnabled;
  private int chordFretIndexSpacing;
  private int chordFretSpacing;
  private int chordNoteSize;
  private int chordStringSpacing;
  private int divisionTypeSpacing;
  private int effectSpacing;
  private int firstMeasureSpacing;
  private int firstTrackSpacing;
  private float fontScale;
  private int height;
  private int loopMarkerSpacing;
  private int markerSpacing;
  private int minBufferSeparator;
  private int minScoreTabSpacing;
  private int minTopSpacing;
  private boolean playModeEnabled;
  private int repeatEndingSpacing;
  private TGResources resources;
  private float scale;
  private int scoreLineSpacing;
  private int scoreSpacing;
  private int stringSpacing;
  private int style;
  private Tablature tablature;
  private int textSpacing;

  private List trackPositions;

  private int trackSpacing;

  private int width;

  public ViewLayout(Tablature tablature, int style) {
    this(tablature, style, 1.0f);
  }

  public ViewLayout(Tablature tablature, int style, float scale) {
    this.tablature = tablature;
    this.trackPositions = new ArrayList();
    this.playModeEnabled = false;
    this.resources = new TGResources(this);
    this.style = style;
    if ((this.style & DISPLAY_TABLATURE) == 0
        && (this.style & DISPLAY_SCORE) == 0) {
      this.style |= DISPLAY_TABLATURE;
    }
    this.init(scale);
  }

  protected void addTrackPosition(int track, int posY, int height) {
    this.trackPositions.add(new TrackPosition(track, posY, height));
  }

  protected void checkDefaultSpacing(TGTrackSpacing ts) {
    int checkPosition = -1;
    int minBufferSeparator = getMinBufferSeparator();
    if ((this.style & DISPLAY_SCORE) != 0) {
      int bufferSeparator = (ts
          .getPosition(TGTrackSpacing.POSITION_SCORE_UP_LINES) - ts
          .getPosition(TGTrackSpacing.POSITION_BUFFER_SEPARATOR));
      if (bufferSeparator < minBufferSeparator) {
        ts.setSize(TGTrackSpacing.POSITION_BUFFER_SEPARATOR, minBufferSeparator
            - bufferSeparator);
      }
      checkPosition = ts
          .getPosition(TGTrackSpacing.POSITION_SCORE_MIDDLE_LINES);
    } else if ((this.style & DISPLAY_TABLATURE) != 0) {
      int bufferSeparator = (ts.getPosition(TGTrackSpacing.POSITION_TABLATURE) - ts
          .getPosition(TGTrackSpacing.POSITION_BUFFER_SEPARATOR));
      if (bufferSeparator < minBufferSeparator) {
        ts.setSize(TGTrackSpacing.POSITION_BUFFER_SEPARATOR, minBufferSeparator
            - bufferSeparator);
      }
      checkPosition = ts.getPosition(TGTrackSpacing.POSITION_TABLATURE);
    }

    if (checkPosition >= 0 && checkPosition < getMinTopSpacing()) {
      ts.setSize(TGTrackSpacing.POSITION_TOP,
          (getMinTopSpacing() - checkPosition));
    }
  }

  protected float checkScale() {
    float v1 = ((this.style & DISPLAY_SCORE) != 0 ? (getScoreLineSpacing() * 1.25f)
        : 0);
    float v2 = ((this.style & DISPLAY_TABLATURE) != 0 ? getStringSpacing() : 0);
    float scale = (Math.max(v1, v2) / 10.0f);
    return scale;
  }

  protected void clearTrackPositions() {
    this.trackPositions.clear();
  }

  public void disposeLayout() {
    this.getResources().dispose();
  }

  public void fireUpdate(int measureNumber) {
    int measureIndex = (measureNumber - 1);
    int trackCount = getSongManager().getSong().countTracks();
    TGMeasureHeaderImpl header = (TGMeasureHeaderImpl) getSongManager()
        .getSong().getMeasureHeader(measureIndex);
    header.update(this, measureIndex);
    for (int trackIdx = 0; trackIdx < trackCount; trackIdx++) {
      TGTrackImpl track = (TGTrackImpl) getSongManager().getSong().getTrack(
          trackIdx);
      TGMeasureImpl measure = null;
      measure = (TGMeasureImpl) track.getMeasure(measureIndex);
      measure.create(this);
    }
    for (int trackIdx = 0; trackIdx < trackCount; trackIdx++) {
      TGTrackImpl track = (TGTrackImpl) getSongManager().getSong().getTrack(
          trackIdx);
      TGMeasureImpl measure = (TGMeasureImpl) track.getMeasure(measureIndex);
      measure.update(this);
    }
    updateCaret();
  }

  /**
   * Calcula el Espacio que ocupara el pulso
   */
  public float getBeatWidth(TGVoice voice) {
    float scale = getScale();
    TGDuration duration = voice.getDuration();
    if (duration != null) {
      switch (duration.getValue()) {
      case TGDuration.WHOLE:
        return (30.0f * scale);
      case TGDuration.HALF:
        return (25.0f * scale);
      case TGDuration.QUARTER:
        return (21.0f * scale);
      case TGDuration.EIGHTH:
        return (20.0f * scale);
      case TGDuration.SIXTEENTH:
        return (19.0f * scale);
      case TGDuration.THIRTY_SECOND:
        return (18.0f * scale);
      default:
        return (17.0f * scale);
      }
    }
    return (20.0f * scale);
  }

  public int getChordFretIndexSpacing() {
    return this.chordFretIndexSpacing;
  }

  public int getChordFretSpacing() {
    return this.chordFretSpacing;
  }

  public int getChordNoteSize() {
    return this.chordNoteSize;
  }

  public int getChordStringSpacing() {
    return this.chordStringSpacing;
  }

  public int getDefaultChordSpacing() {
    int spacing = 0;
    if ((this.style & DISPLAY_CHORD_DIAGRAM) != 0) {
      spacing += ((TGChordImpl.MAX_FRETS * getChordFretSpacing()) + getChordFretSpacing());
    }
    if ((this.style & DISPLAY_CHORD_NAME) != 0) {
      spacing += Math.round((15f * getScale()));
    }
    return spacing;
  }

  public int getDivisionTypeSpacing() {
    return this.divisionTypeSpacing;
  }

  public int getEffectSpacing() {
    return this.effectSpacing;
  }

  public int getFirstMeasureSpacing() {
    return this.firstMeasureSpacing;
  }

  public int getFirstTrackSpacing() {
    return this.firstTrackSpacing;
  }

  public float getFontScale() {
    return this.fontScale;
  }

  public int getHeight() {
    return this.height;
  }

  public int getLoopMarkerSpacing() {
    return this.loopMarkerSpacing;
  }

  public int getMarkerSpacing() {
    return this.markerSpacing;
  }

  public int getMinBufferSeparator() {
    return this.minBufferSeparator;
  }

  public int getMinScoreTabSpacing() {
    return this.minScoreTabSpacing;
  }

  /**
   * Calcula el Espacio minimo que quedara entre nota y nota
   */
  protected float getMinSpacing(TGDuration duration) {
    float scale = getScale();
    switch (duration.getValue()) {
    case TGDuration.WHOLE:
      return (50.0f * scale);
    case TGDuration.HALF:
      return (30.0f * scale);
    case TGDuration.QUARTER:
      return (25.0f * scale);
    case TGDuration.EIGHTH:
      return (20.0f * scale);
    default:
      return (18.0f * scale);
    }
  }

  public int getMinTopSpacing() {
    return this.minTopSpacing;
  }

  public abstract int getMode();

  public Rectangle getNoteOrientation(TGPainter painter, int x, int y,
      TGNote note) {
    String noteAsString = null;
    if (note.isTiedNote()) {
      noteAsString = "L";
      noteAsString = (note.getEffect().isGhostNote()) ? "(" + noteAsString
          + ")" : noteAsString;
    } else if (note.getEffect().isDeadNote()) {
      noteAsString = "X";
      noteAsString = (note.getEffect().isGhostNote()) ? "(" + noteAsString
          + ")" : noteAsString;
    } else {
      noteAsString = Integer.toString(note.getValue());
      noteAsString = (note.getEffect().isGhostNote()) ? "(" + noteAsString
          + ")" : noteAsString;
    }
    return getOrientation(painter, x, y, noteAsString);
  }

  public Rectangle getOrientation(TGPainter painter, int x, int y, String s) {
    Point point = painter.getStringExtent(s);
    return new Rectangle((x - (point.x / 2)), (y - (point.y / 2)), point.x,
        point.y);
  }

  public int getRepeatEndingSpacing() {
    return this.repeatEndingSpacing;
  }

  public TGResources getResources() {
    return this.resources;
  }

  public float getScale() {
    return this.scale;
  }

  public int getScoreLineSpacing() {
    return this.scoreLineSpacing;
  }

  public int getScoreSpacing() {
    return this.scoreSpacing;
  }

  public TGSongManager getSongManager() {
    return getTablature().getSongManager();
  }

  /**
   * Calcula el espacio minimo entre negras, dependiendo de la duracion de la
   * nota
   */
  public int getSpacingForQuarter(TGDuration duration) {
    double spacing = (((double) TGDuration.QUARTER_TIME / (double) duration
        .getTime()) * getMinSpacing(duration));
    return (int) spacing;
  }

  public int getStringSpacing() {
    return this.stringSpacing;
  }

  public int getStyle() {
    return this.style;
  }

  public Tablature getTablature() {
    return this.tablature;
  }

  public int getTextSpacing() {
    return this.textSpacing;
  }

  public int getTrackNumberAt(int y) {
    TrackPosition trackPos = getTrackPositionAt(y);
    return ((trackPos != null) ? trackPos.getTrack() : -1);
  }

  public TrackPosition getTrackPositionAt(int y) {
    TrackPosition trackPos = null;
    int minorDistance = 0;

    Iterator it = this.trackPositions.iterator();
    while (it.hasNext()) {
      TrackPosition pos = (TrackPosition) it.next();
      int distanceY = Math.min(Math.abs(y - (pos.getPosY())), Math.abs(y
          - (pos.getPosY() + pos.getHeight() - 10)));
      if (trackPos == null || distanceY < minorDistance) {
        trackPos = pos;
        minorDistance = distanceY;
      }
    }
    return trackPos;
  }

  public int getTrackSpacing() {
    return this.trackSpacing;
  }

  /**
   * Calcula el Espacio que ocupara el pulso
   */
  public float getVoiceWidth(TGVoiceImpl voice) {
    float scale = getScale();
    TGDuration duration = voice.getDuration();
    if (duration != null) {
      switch (duration.getValue()) {
      case TGDuration.WHOLE:
        return (30.0f * scale);
      case TGDuration.HALF:
        return (25.0f * scale);
      case TGDuration.QUARTER:
        return (21.0f * scale);
      case TGDuration.EIGHTH:
        return (20.0f * scale);
      case TGDuration.SIXTEENTH:
        return (19.0f * scale);
      case TGDuration.THIRTY_SECOND:
        return (18.0f * scale);
      default:
        return (17.0f * scale);
      }
    }
    return (20.0f * scale);
  }

  public int getWidth() {
    return this.width;
  }

  public boolean hasLoopMarker(TGMeasure measure) {
    MidiPlayerMode pm = TuxGuitar.instance().getPlayer().getMode();
    if (pm.isLoop()
        && (pm.getLoopSHeader() == measure.getNumber() || pm.getLoopEHeader() == measure
            .getNumber())) {
      return true;
    }
    return false;
  }

  protected void init(float initScale) {
    TGConfigManager config = TuxGuitar.instance().getConfig();
    this.setBufferEnabled(true);
    this.setStringSpacing((int) (config
        .getIntConfigValue(TGConfigKeys.TAB_LINE_SPACING) * initScale));
    this.setScoreLineSpacing((int) (config
        .getIntConfigValue(TGConfigKeys.SCORE_LINE_SPACING) * initScale));
    this.setScale(checkScale());
    this.setFontScale(getScale());
    this.setFirstMeasureSpacing(Math.round(20f * getScale()));
    this.setMinBufferSeparator(Math.round(20f * getScale()));
    this.setMinTopSpacing(Math.round(30f * getScale()));
    this.setMinScoreTabSpacing(Math.round(config
        .getIntConfigValue(TGConfigKeys.MIN_SCORE_TABLATURE_SPACING)
        * getScale()));
    this
        .setScoreSpacing(((getScoreLineSpacing() * 4) + getMinScoreTabSpacing()));
    this.setFirstTrackSpacing(Math.round(config
        .getIntConfigValue(TGConfigKeys.FIRST_TRACK_SPACING)
        * getScale()));
    this.setTrackSpacing(Math.round(config
        .getIntConfigValue(TGConfigKeys.TRACK_SPACING)
        * getScale()));

    this.setChordFretIndexSpacing(Math.round(8f * getScale()));
    this.setChordStringSpacing(Math.round(5f * getScale()));
    this.setChordFretSpacing(Math.round(6f * getScale()));
    this.setChordNoteSize(Math.round(4f * getScale()));
    this.setRepeatEndingSpacing(Math.round(20f * getScale()));
    this.setTextSpacing(Math.round(15f * getScale()));
    this.setMarkerSpacing(Math.round(15f * getScale()));
    this.setLoopMarkerSpacing(Math.round(5f * getScale()));
    this.setDivisionTypeSpacing(Math.round(10f * getScale()));
    this.setEffectSpacing(Math.round(8f * getScale()));
  }

  public boolean isBufferEnabled() {
    return this.bufferEnabled;
  }

  public boolean isCaretVisible() {
    return true;
  }

  public boolean isFirstMeasure(TGMeasure measure) {
    return (measure.getNumber() == 1);
  }

  public boolean isLastMeasure(TGMeasure measure) {
    return (measure.getNumber() == getSongManager().getSong()
        .countMeasureHeaders());
  }

  public boolean isPlayModeEnabled() {
    return this.playModeEnabled;
  }

  public void paint(TGPainter painter, Rectangle clientArea, int fromX,
      int fromY) {
    this.playModeEnabled = false;
    paintSong(painter, clientArea, fromX, fromY);
  }

  /**
   * Pinta el caret
   */
  public void paintCaret(TGPainter painter) {
    if (isCaretVisible()
        && ((this.style & (DISPLAY_TABLATURE | DISPLAY_SCORE)) != 0)) {
      Caret caret = getTablature().getCaret();
      if (!caret.getMeasure().isOutOfBounds()) {
        caret.paintCaret(this, painter);
      }
    }
  }

  /**
   * Pinta las lineas
   */
  public void paintLines(TGTrackImpl track, TGTrackSpacing ts,
      TGPainter painter, int x, int y, int width) {
    if (width > 0) {
      setLineStyle(painter);
      int tempX = ((x < 0) ? 0 : x);
      int tempY = y;

      // partitura
      if ((this.style & DISPLAY_SCORE) != 0) {
        int posY = tempY
            + ts.getPosition(TGTrackSpacing.POSITION_SCORE_MIDDLE_LINES);

        painter.initPath();
        painter.setAntialias(false);
        for (int i = 1; i <= 5; i++) {
          painter.moveTo(tempX, posY);
          painter.lineTo(tempX + width, posY);
          posY += getScoreLineSpacing();
        }
        painter.closePath();
      }
      // tablatura
      if ((this.style & DISPLAY_TABLATURE) != 0) {
        tempY += ts.getPosition(TGTrackSpacing.POSITION_TABLATURE);

        painter.initPath();
        painter.setAntialias(false);
        for (int i = 0; i < track.stringCount(); i++) {
          painter.moveTo(tempX, tempY);
          painter.lineTo(tempX + width, tempY);
          tempY += getStringSpacing();
        }
        painter.closePath();
      }
    }
  }

  public void paintMeasure(TGMeasureImpl measure, TGPainter painter, int spacing) {
    measure.setSpacing(spacing);
    measure.paintMeasure(this, painter);
  }

  /**
   * Pinta el compas y las notas que estan sonando
   */
  public void paintPlayMode(TGPainter painter, TGMeasureImpl measure,
      TGBeatImpl beat, boolean paintMeasure) {
    this.playModeEnabled = true;

    // pinto el compas
    if (paintMeasure) {
      measure.paintMeasure(this, painter);
    }
    // pinto el pulso
    beat.paint(this, painter, measure.getPosX()
        + measure.getHeaderImpl().getLeftSpacing(this), measure.getPosY());

    // pinto los lyrics
    ((TGLyricImpl) measure.getTrackImpl().getLyrics()).paintCurrentNoteBeats(
        painter, this, measure, measure.getPosX(), measure.getPosY());

    this.playModeEnabled = false;
  }

  public abstract void paintSong(TGPainter painter, Rectangle clientArea,
      int fromX, int fromY);

  public void reloadStyles() {
    this.getResources().load();
  }

  public void setBufferEnabled(boolean bufferEnabled) {
    this.bufferEnabled = bufferEnabled;
  }

  public void setCaretStyle(TGPainter painter, boolean expectedVoice) {
    painter.setForeground(expectedVoice ? getResources().getCaretColor1()
        : getResources().getCaretColor2());
  }

  public void setChordFretIndexSpacing(int chordFretIndexSpacing) {
    this.chordFretIndexSpacing = chordFretIndexSpacing;
  }

  public void setChordFretSpacing(int chordFretSpacing) {
    this.chordFretSpacing = chordFretSpacing;
  }

  public void setChordNoteSize(int chordNoteSize) {
    this.chordNoteSize = chordNoteSize;
  }

  public void setChordStringSpacing(int chordStringSpacing) {
    this.chordStringSpacing = chordStringSpacing;
  }

  public void setChordStyle(TGChordImpl chord) {
    chord.setFont(getResources().getChordFont());
    chord.setForegroundColor(getResources().getColorBlack());
    chord.setBackgroundColor(getResources().getBackgroundColor());
    chord.setColor(getResources().getLineColor());
    chord.setNoteColor(getResources().getTabNoteColor());
    chord.setTonicColor(getResources().getTabNoteColor());
    chord.setStyle(this.style);
    chord.setFretSpacing(getChordFretSpacing());
    chord.setStringSpacing(getChordStringSpacing());
    chord.setNoteSize(getChordNoteSize());
    chord.setFirstFretSpacing(getChordFretIndexSpacing());
    chord.setFirstFretFont(getResources().getChordFretFont());
  }

  public void setClefStyle(TGPainter painter) {
    painter.setBackground(getResources().getColorBlack());
  }

  public void setDivisionsStyle(TGPainter painter, boolean fill) {
    painter.setFont(getResources().getDefaultFont());
    painter.setBackground((fill ? getResources().getColorBlack()
        : getResources().getBackgroundColor()));
    painter.setForeground(getResources().getColorBlack());
  }

  public void setDivisionTypeSpacing(int divisionTypeSpacing) {
    this.divisionTypeSpacing = divisionTypeSpacing;
  }

  public void setDivisionTypeStyle(TGPainter painter) {
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setFont(getResources().getDefaultFont());
  }

  public void setDotStyle(TGPainter painter) {
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground(getResources().getColorBlack());
  }

  public void setEffectSpacing(int effectSpacing) {
    this.effectSpacing = effectSpacing;
  }

  public void setFirstMeasureSpacing(int firstMeasureSpacing) {
    this.firstMeasureSpacing = firstMeasureSpacing;
  }

  public void setFirstTrackSpacing(int firstTrackSpacing) {
    this.firstTrackSpacing = firstTrackSpacing;
  }

  public void setFontScale(float fontScale) {
    this.fontScale = fontScale;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setKeySignatureStyle(TGPainter painter) {
    painter.setBackground(getResources().getColorBlack());
  }

  public void setLineStyle(TGPainter painter) {
    painter.setLineWidth(1);
    painter.setForeground(getResources().getLineColor());
  }

  public void setLoopEMarkerStyle(TGPainter painter) {
    painter.setBackground(getResources().getLoopEMarkerColor());
  }

  public void setLoopMarkerSpacing(int loopMarkerSpacing) {
    this.loopMarkerSpacing = loopMarkerSpacing;
  }

  public void setLoopSMarkerStyle(TGPainter painter) {
    painter.setBackground(getResources().getLoopSMarkerColor());
  }

  public void setLyricStyle(TGPainter painter, boolean playMode) {
    painter.setFont(getResources().getLyricFont());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setForeground((playMode ? getResources().getPlayNoteColor()
        : getResources().getColorBlack()));
  }

  public void setMarkerSpacing(int markerSpacing) {
    this.markerSpacing = markerSpacing;
  }

  public void setMarkerStyle(TGPainter painter, Color color) {
    painter.setFont(getResources().getMarkerFont());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setForeground(color);
  }

  public void setMeasureNumberStyle(TGPainter painter) {
    painter.setFont(getResources().getDefaultFont());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setForeground(getResources().getColorRed());
  }

  public void setMeasurePlayingStyle(TGPainter painter) {
    painter.setBackground(getResources().getBackgroundColor());
    painter.setForeground(getResources().getColorBlack());
  }

  public void setMinBufferSeparator(int minBufferSeparator) {
    this.minBufferSeparator = minBufferSeparator;
  }

  public void setMinScoreTabSpacing(int minScoreTabSpacing) {
    this.minScoreTabSpacing = minScoreTabSpacing;
  }

  public void setMinTopSpacing(int minTopSpacing) {
    this.minTopSpacing = minTopSpacing;
  }

  public void setOfflineEffectStyle(TGPainter painter) {
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setFont(getResources().getDefaultFont());
  }

  public void setPlayNoteColor(TGPainter painter) {
    painter.setForeground(getResources().getPlayNoteColor());
    painter.setBackground(getResources().getPlayNoteColor());
  }

  public void setRepeatEndingSpacing(int repeatEndingSpacing) {
    this.repeatEndingSpacing = repeatEndingSpacing;
  }

  public void setRepeatEndingStyle(TGPainter painter) {
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setFont(getResources().getDefaultFont());
  }

  public void setScale(float scale) {
    this.scale = scale;
  }

  public void setScoreEffectStyle(TGPainter painter) {
    painter.setForeground(getResources().getScoreNoteColor());
    painter.setBackground(getResources().getScoreNoteColor());
  }

  public void setScoreLineSpacing(int scoreLineSpacing) {
    this.scoreLineSpacing = scoreLineSpacing;
  }

  public void setScoreNoteFooterStyle(TGPainter painter) {
    painter.setForeground(getResources().getScoreNoteColor());
    painter.setBackground(getResources().getScoreNoteColor());
  }

  public void setScoreNoteStyle(TGPainter painter, boolean playing) {
    painter.setForeground((playing ? getResources().getPlayNoteColor()
        : getResources().getScoreNoteColor()));
    painter.setBackground((playing ? getResources().getPlayNoteColor()
        : getResources().getScoreNoteColor()));
  }

  public void setScoreSilenceStyle(TGPainter painter, boolean playMode) {
    painter.setForeground((playMode ? getResources().getPlayNoteColor()
        : getResources().getScoreNoteColor()));
    painter.setBackground((playMode ? getResources().getPlayNoteColor()
        : getResources().getScoreNoteColor()));
  }

  public void setScoreSpacing(int scoreSpacing) {
    this.scoreSpacing = scoreSpacing;
  }

  public void setStringSpacing(int stringSpacing) {
    this.stringSpacing = stringSpacing;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public void setTabEffectStyle(TGPainter painter) {
    painter.setForeground(getResources().getTabNoteColor());
    painter.setBackground(getResources().getTabNoteColor());
  }

  public void setTabGraceStyle(TGPainter painter) {
    painter.setFont(getResources().getGraceFont());
    painter.setForeground(getResources().getTabNoteColor());
    painter.setBackground(getResources().getBackgroundColor());
  }

  public void setTablature(Tablature tablature) {
    this.tablature = tablature;
  }

  public void setTabNoteFooterStyle(TGPainter painter) {
    painter.setForeground(getResources().getTabNoteColor());
    painter.setBackground(getResources().getTabNoteColor());
  }

  public void setTabNoteStyle(TGPainter painter, boolean playMode) {
    painter.setForeground((playMode ? getResources().getPlayNoteColor()
        : getResources().getTabNoteColor()));
    painter.setBackground(getResources().getBackgroundColor());
    painter.setFont(getResources().getNoteFont());
  }

  public void setTabSilenceStyle(TGPainter painter, boolean playMode) {
    painter.setForeground((playMode ? getResources().getPlayNoteColor()
        : getResources().getTabNoteColor()));
    painter.setBackground((playMode ? getResources().getPlayNoteColor()
        : getResources().getTabNoteColor()));
  }

  public void setTempoStyle(TGPainter painter, boolean fontStyle) {
    painter.setFont(getResources().getDefaultFont());
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground((fontStyle ? getResources().getBackgroundColor()
        : getResources().getColorBlack()));
  }

  public void setTextSpacing(int textSpacing) {
    this.textSpacing = textSpacing;
  }

  public void setTextStyle(TGPainter painter) {
    painter.setFont(getResources().getTextFont());
    painter.setBackground(getResources().getBackgroundColor());
    painter.setForeground(getResources().getColorBlack());
  }

  public void setTimeSignatureStyle(TGPainter painter) {
    painter.setFont(getResources().getTimeSignatureFont());
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground(getResources().getBackgroundColor());
  }

  public void setTrackSpacing(int trackSpacing) {
    this.trackSpacing = trackSpacing;
  }

  public void setTripletFeelStyle(TGPainter painter, boolean fontStyle) {
    painter.setFont(getResources().getDefaultFont());
    painter.setForeground(getResources().getColorBlack());
    painter.setBackground((fontStyle ? getResources().getBackgroundColor()
        : getResources().getColorBlack()));
  }

  public void setWidth(int width) {
    this.width = width;
  }

  private void updateCaret() {
    this.tablature.getCaret().update();
  }

  public void updateSong() {
    updateTracks();
    updateCaret();
  }

  public void updateTracks() {
    int trackCount = getSongManager().getSong().countTracks();
    int measureCount = getSongManager().getSong().countMeasureHeaders();
    for (int measureIdx = 0; measureIdx < measureCount; measureIdx++) {
      TGMeasureHeaderImpl header = (TGMeasureHeaderImpl) getSongManager()
          .getSong().getMeasureHeader(measureIdx);
      header.update(this, measureIdx);
      for (int trackIdx = 0; trackIdx < trackCount; trackIdx++) {
        TGTrackImpl track = (TGTrackImpl) getSongManager().getSong().getTrack(
            trackIdx);
        TGMeasureImpl measure = (TGMeasureImpl) track.getMeasure(measureIdx);
        measure.create(this);
      }
      for (int trackIdx = 0; trackIdx < trackCount; trackIdx++) {
        TGTrackImpl track = (TGTrackImpl) getSongManager().getSong().getTrack(
            trackIdx);
        TGMeasureImpl measure = (TGMeasureImpl) track.getMeasure(measureIdx);
        track.update(this);
        measure.update(this);
      }
    }
  }
}
