package org.herac.tuxguitar.gui.undo.undoables.track;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableAddTrack implements UndoableEdit {
  private static TGTrack cloneTrack(TGTrack track) {
    return track.clone(TuxGuitar.instance().getSongManager().getSong());
  }

  public static UndoableAddTrack startUndo() {
    UndoableAddTrack undoable = new UndoableAddTrack();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();

    return undoable;
  }

  private int doAction;
  private TGTrack redoableTrack;

  private UndoableCaretHelper redoCaret;

  private UndoableCaretHelper undoCaret;

  private UndoableAddTrack() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableAddTrack endUndo(TGTrack track) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoableTrack = cloneTrack(track);
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().addTrack(
        cloneTrack(this.redoableTrack));
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().removeTrack(
        cloneTrack(this.redoableTrack));
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }

}
