package org.herac.tuxguitar.gui.undo.undoables.track;

import java.awt.Color;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableTrackInfo implements UndoableEdit {
  public static UndoableTrackInfo startUndo(TGTrack track) {
    UndoableTrackInfo undoable = new UndoableTrackInfo();
    undoable.doAction = UNDO_ACTION;
    undoable.trackNumber = track.getNumber();
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.undoName = track.getName();
    undoable.undoColor = track.getColor();
    undoable.undoOffset = track.getOffset();

    return undoable;
  }

  private int doAction;
  private UndoableCaretHelper redoCaret;
  private Color redoColor;
  private String redoName;
  private int redoOffset;
  private int trackNumber;
  private UndoableCaretHelper undoCaret;
  private Color undoColor;
  private String undoName;

  private int undoOffset;

  private UndoableTrackInfo() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableTrackInfo endUndo(TGTrack track) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoName = track.getName();
    this.redoColor = track.getColor();
    this.redoOffset = track.getOffset();

    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TGSongManager manager = TuxGuitar.instance().getSongManager();
    manager.getTrackManager().changeInfo(manager.getTrack(this.trackNumber),
        this.redoName, this.redoColor, this.redoOffset);
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.redoCaret.update();
    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TGSongManager manager = TuxGuitar.instance().getSongManager();
    manager.getTrackManager().changeInfo(manager.getTrack(this.trackNumber),
        this.undoName, this.undoColor, this.undoOffset);
    TuxGuitar.instance().fireUpdate();
    TuxGuitar.instance().getMixer().update();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }

}
