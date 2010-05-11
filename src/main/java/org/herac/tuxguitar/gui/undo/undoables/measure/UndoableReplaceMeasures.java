package org.herac.tuxguitar.gui.undo.undoables.measure;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.helpers.TGSongSegment;
import org.herac.tuxguitar.song.helpers.TGSongSegmentHelper;
import org.herac.tuxguitar.song.models.TGMarker;

public class UndoableReplaceMeasures implements UndoableEdit {
  private class UndoMarkers {
    private List<TGMarker> markers;

    public UndoMarkers() {
      this.markers = new ArrayList<TGMarker>();
      for (final TGMarker marker : TuxGuitar.instance().getSongManager()
          .getMarkers()) {
        this.markers.add(marker.clone());
      }
    }

    public void undo() {
      TuxGuitar.instance().getSongManager().removeAllMarkers();
      for (final TGMarker marker : this.markers) {
        TuxGuitar.instance().getSongManager().updateMarker(marker.clone());
      }
    }
  }

  private int count;
  private int doAction;
  private int freeSpace;
  private UndoableCaretHelper redoCaret;
  private TGSongSegment redoTrackMeasures;
  private long theMove;
  private int toTrack;
  private UndoableCaretHelper undoCaret;
  private UndoMarkers undoMarkers;

  private TGSongSegment undoTrackMeasures;

  public UndoableReplaceMeasures(int p1, int p2, int toTrack) {
    this.doAction = UNDO_ACTION;
    this.toTrack = toTrack;
    this.undoCaret = new UndoableCaretHelper();
    this.undoMarkers = new UndoMarkers();
    this.undoTrackMeasures = new TGSongSegmentHelper(TuxGuitar.instance()
        .getSongManager()).copyMeasures(p1, p2);
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableReplaceMeasures endUndo(TGSongSegment tracksMeasures,
      int count, int freeSpace, long theMove) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoTrackMeasures = tracksMeasures;
    this.count = count;
    this.freeSpace = freeSpace;
    this.theMove = theMove;
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    for (int i = this.freeSpace; i < this.count; i++) {
      TuxGuitar.instance().getSongManager().addNewMeasureBeforeEnd();
    }
    new TGSongSegmentHelper(TuxGuitar.instance().getSongManager())
        .replaceMeasures(this.redoTrackMeasures.clone(), this.theMove,
            this.toTrack);

    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }

    for (int i = this.freeSpace; i < this.count; i++) {
      TuxGuitar.instance().getSongManager().removeLastMeasure();
    }
    new TGSongSegmentHelper(TuxGuitar.instance().getSongManager())
        .replaceMeasures(this.undoTrackMeasures.clone(), 0, 0);

    TuxGuitar.instance().fireUpdate();
    this.undoMarkers.undo();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
