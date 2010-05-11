package org.herac.tuxguitar.gui.undo.undoables.measure;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.helpers.TGSongSegment;
import org.herac.tuxguitar.song.helpers.TGSongSegmentHelper;

public class UndoableInsertMeasure implements UndoableEdit {
  private static Caret getCaret() {
    return TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
  }

  private int copyCount;
  private int doAction;
  private int fromNumber;
  private long insertPosition;
  private UndoableCaretHelper redoCaret;
  private long theMove;
  private int toTrack;
  private TGSongSegment tracksMeasures;

  private UndoableCaretHelper undoCaret;

  public UndoableInsertMeasure(int toTrack) {
    Caret caret = getCaret();
    this.doAction = UNDO_ACTION;
    this.toTrack = toTrack;
    this.undoCaret = new UndoableCaretHelper();
    this.insertPosition = caret.getPosition();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableInsertMeasure endUndo(TGSongSegment tracksMeasures,
      int copyCount, int fromNumber, long theMove) {
    this.redoCaret = new UndoableCaretHelper();
    this.copyCount = copyCount;
    this.tracksMeasures = tracksMeasures;
    this.fromNumber = fromNumber;
    this.theMove = theMove;
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TGSongSegmentHelper helper = new TGSongSegmentHelper(TuxGuitar.instance()
        .getSongManager());
    helper.insertMeasures(this.tracksMeasures.clone(), this.fromNumber,
        this.theMove, this.toTrack);
    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    for (int i = 0; i < this.copyCount; i++) {
      TuxGuitar.instance().getSongManager().removeMeasure(this.insertPosition);
    }
    TuxGuitar.instance().fireUpdate();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }

}
