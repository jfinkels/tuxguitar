package org.herac.tuxguitar.gui.undo.undoables.custom;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.marker.MarkerList;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGMarker;

public class UndoableChangeMarker implements UndoableEdit {
  public static UndoableChangeMarker startUndo(TGMarker marker) {
    UndoableChangeMarker undoable = new UndoableChangeMarker();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.undoMarker = (marker == null) ? null : marker.clone();

    return undoable;
  }

  private int doAction;
  private UndoableCaretHelper redoCaret;
  private TGMarker redoMarker;
  private UndoableCaretHelper undoCaret;

  private TGMarker undoMarker;

  private UndoableChangeMarker() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableChangeMarker endUndo(TGMarker marker) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoMarker = (marker == null) ? null : marker.clone();
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    if (this.redoMarker != null) {
      TuxGuitar.instance().getSongManager().updateMarker(
          this.redoMarker.clone());
      MarkerList.instance().update(true);
    } else if (this.undoMarker != null) {
      TuxGuitar.instance().getSongManager().removeMarker(
          this.undoMarker.clone());
      MarkerList.instance().update(false);
    }
    this.redoCaret.update();
    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    if (this.undoMarker != null) {
      TuxGuitar.instance().getSongManager().updateMarker(
          this.undoMarker.clone());
      MarkerList.instance().update(true);
    } else if (this.redoMarker != null) {
      TuxGuitar.instance().getSongManager().removeMarker(
          this.redoMarker.clone());
      MarkerList.instance().update(false);
    }
    this.undoCaret.update();
    this.doAction = REDO_ACTION;
  }
}
