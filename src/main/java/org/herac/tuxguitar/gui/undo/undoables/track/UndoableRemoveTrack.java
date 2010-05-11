package org.herac.tuxguitar.gui.undo.undoables.track;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableRemoveTrack implements UndoableEdit {
  private static TGTrack cloneTrack(TGTrack track) {
    return track.clone(TuxGuitar.instance().getSongManager().getSong());
  }

  private static Caret getCaret() {
    return TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
  }

  public static UndoableRemoveTrack startUndo() {
    UndoableRemoveTrack undoable = new UndoableRemoveTrack();
    Caret caret = getCaret();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.undoableTrack = cloneTrack(caret.getTrack());

    return undoable;
  }

  private int doAction;

  private UndoableCaretHelper redoCaret;

  private TGTrack undoableTrack;

  private UndoableCaretHelper undoCaret;

  private UndoableRemoveTrack() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableRemoveTrack endUndo() {
    this.redoCaret = new UndoableCaretHelper();
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().removeTrack(
        cloneTrack(this.undoableTrack));
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().addTrack(
        cloneTrack(this.undoableTrack));
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
