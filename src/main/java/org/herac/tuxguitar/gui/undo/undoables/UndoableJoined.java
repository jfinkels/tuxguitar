package org.herac.tuxguitar.gui.undo.undoables;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;

public class UndoableJoined implements UndoableEdit {
  private int doAction = UNDO_ACTION;
  private UndoableCaretHelper redoCaret;
  private List<UndoableEdit> undoables = new ArrayList<UndoableEdit>();
  private UndoableCaretHelper undoCaret = new UndoableCaretHelper();

  public void addUndoableEdit(UndoableEdit undoable) {
    this.undoables.add(undoable);
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableJoined endUndo() {
    this.redoCaret = new UndoableCaretHelper();
    return this;
  }

  public boolean isEmpty() {
    return this.undoables.isEmpty();
  }

  public void redo() throws CannotRedoException {
    for (final UndoableEdit undoable : this.undoables) {
      undoable.redo();
    }
    this.redoCaret.update();
    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    int count = this.undoables.size();
    for (int i = (count - 1); i >= 0; i--) {
      UndoableEdit undoable = this.undoables.get(i);
      undoable.undo();
    }
    this.undoCaret.update();
    this.doAction = REDO_ACTION;
  }
}
