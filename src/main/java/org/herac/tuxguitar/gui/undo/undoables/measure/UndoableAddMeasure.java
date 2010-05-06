package org.herac.tuxguitar.gui.undo.undoables.measure;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;

public class UndoableAddMeasure implements UndoableEdit {
  public static UndoableAddMeasure startUndo(int number) {
    UndoableAddMeasure undoable = new UndoableAddMeasure();
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.doAction = UNDO_ACTION;
    undoable.number = number;
    return undoable;
  }

  private int doAction;
  private int number;
  private UndoableCaretHelper redoCaret;

  private UndoableCaretHelper undoCaret;

  private UndoableAddMeasure() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableAddMeasure endUndo() {
    this.redoCaret = new UndoableCaretHelper();
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().addNewMeasure(this.number);
    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().removeMeasure(this.number);
    TuxGuitar.instance().fireUpdate();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }

}
