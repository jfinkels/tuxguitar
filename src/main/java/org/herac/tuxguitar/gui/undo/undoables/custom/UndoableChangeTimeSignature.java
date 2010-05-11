package org.herac.tuxguitar.gui.undo.undoables.custom;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTimeSignature;

public class UndoableChangeTimeSignature implements UndoableEdit {
  public static UndoableChangeTimeSignature startUndo() {
    TGSong song = TuxGuitar.instance().getTablatureEditor().getTablature()
        .getSongManager().getSong();
    UndoableChangeTimeSignature undoable = new UndoableChangeTimeSignature();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.song = song.clone();
    return undoable;
  }

  private int doAction;
  private UndoableCaretHelper redoCaret;
  private TGSong song;
  private TGTimeSignature ts;
  private long tsStart;
  private boolean tsToEnd;

  private UndoableCaretHelper undoCaret;

  private UndoableChangeTimeSignature() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableChangeTimeSignature endUndo(TGTimeSignature timeSignature,
      long start, boolean toEnd) {
    this.ts = timeSignature;
    this.tsStart = start;
    this.tsToEnd = toEnd;
    this.redoCaret = new UndoableCaretHelper();
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getTablatureEditor().getTablature().getSongManager()
        .changeTimeSignature(this.tsStart, this.ts, this.tsToEnd);
    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();
    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    // TGFactory factory = TuxGuitar.instance().getTablatureEditor()
    // .getTablature().getSongManager().getFactory();
    TGSong song = TuxGuitar.instance().getTablatureEditor().getTablature()
        .getSongManager().getSong();
    this.song.copy(song);
    TuxGuitar.instance().fireUpdate();
    this.undoCaret.update();
    this.doAction = REDO_ACTION;
  }
}
