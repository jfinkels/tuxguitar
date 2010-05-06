package org.herac.tuxguitar.gui.undo.undoables.custom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableChangeKeySignature implements UndoableEdit {
  private class KeySignaturePosition {
    private int keySignature;
    private long position;

    public KeySignaturePosition(long position, int keySignature) {
      this.position = position;
      this.keySignature = keySignature;
    }

    public int getKeySignature() {
      return this.keySignature;
    }

    public long getPosition() {
      return this.position;
    }
  }

  private static Caret getCaret() {
    return TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
  }

  public static UndoableChangeKeySignature startUndo() {
    UndoableChangeKeySignature undoable = new UndoableChangeKeySignature();
    Caret caret = getCaret();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.position = caret.getPosition();
    undoable.undoableKeySignature = caret.getMeasure().getKeySignature();
    undoable.track = caret.getTrack();
    undoable.nextKeySignaturePositions = new ArrayList();

    int prevKeySignature = undoable.undoableKeySignature;
    Iterator it = caret.getTrack().getMeasures();
    while (it.hasNext()) {
      TGMeasureImpl measure = (TGMeasureImpl) it.next();
      if (measure.getStart() > undoable.position) {
        int currKeySignature = measure.getKeySignature();
        if (prevKeySignature != currKeySignature) {
          KeySignaturePosition tsp = undoable.new KeySignaturePosition(measure
              .getStart(), currKeySignature);
          undoable.nextKeySignaturePositions.add(tsp);
        }
        prevKeySignature = currKeySignature;
      }
    }

    return undoable;
  }

  private int doAction;
  private List nextKeySignaturePositions;
  private long position;
  private int redoableKeySignature;
  private UndoableCaretHelper redoCaret;
  private boolean toEnd;

  private TGTrack track;

  private int undoableKeySignature;

  private UndoableCaretHelper undoCaret;

  private UndoableChangeKeySignature() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableChangeKeySignature endUndo(int keySignature, boolean toEnd) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoableKeySignature = keySignature;
    this.toEnd = toEnd;
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().getTrackManager().changeKeySignature(
        this.track, this.position, this.redoableKeySignature, this.toEnd);
    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().getTrackManager().changeKeySignature(
        this.track, this.position, this.undoableKeySignature, this.toEnd);
    if (this.toEnd) {
      Iterator it = this.nextKeySignaturePositions.iterator();
      while (it.hasNext()) {
        KeySignaturePosition ksp = (KeySignaturePosition) it.next();
        TuxGuitar.instance().getSongManager().getTrackManager()
            .changeKeySignature(this.track, ksp.getPosition(),
                ksp.getKeySignature(), true);
      }
    }
    TuxGuitar.instance().fireUpdate();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
