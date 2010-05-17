package org.herac.tuxguitar.gui.undo.undoables.custom;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.editors.tab.TGMeasureImpl;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.Clef;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableChangeClef implements UndoableEdit {
  private class ClefPosition {
    private Clef clef;
    private long position;

    public ClefPosition(long position, Clef clef) {
      this.position = position;
      this.clef = clef;
    }

    public Clef getClef() {
      return this.clef;
    }

    public long getPosition() {
      return this.position;
    }
  }

  private static Caret getCaret() {
    return TuxGuitar.instance().getTablatureEditor().getTablature().getCaret();
  }

  public static UndoableChangeClef startUndo() {
    UndoableChangeClef undoable = new UndoableChangeClef();
    Caret caret = getCaret();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.position = caret.getPosition();
    undoable.undoableClef = caret.getMeasure().getClef();
    undoable.track = caret.getTrack();
    undoable.nextClefPositions = new ArrayList<ClefPosition>();

    Clef prevClef = undoable.undoableClef;

    for (final TGMeasure meas : caret.getTrack().getMeasures()) {
      TGMeasureImpl measure = (TGMeasureImpl) meas;
      if (measure.getStart() > undoable.position) {
        Clef currClef = measure.getClef();
        if (prevClef != currClef) {
          ClefPosition tsp = undoable.new ClefPosition(measure.getStart(),
              currClef);
          undoable.nextClefPositions.add(tsp);
        }
        prevClef = currClef;
      }
    }

    return undoable;
  }

  private int doAction;
  private List<ClefPosition> nextClefPositions;
  private long position;
  private Clef redoableClef;
  private UndoableCaretHelper redoCaret;
  private boolean toEnd;

  private TGTrack track;

  private Clef undoableClef;

  private UndoableCaretHelper undoCaret;

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableChangeClef endUndo(Clef clef, boolean toEnd) {
    this.redoCaret = new UndoableCaretHelper();
    this.redoableClef = clef;
    this.toEnd = toEnd;
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().getTrackManager().changeClef(
        this.track, this.position, this.redoableClef, this.toEnd);
    TuxGuitar.instance().fireUpdate();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().getTrackManager().changeClef(
        this.track, this.position, this.undoableClef, this.toEnd);
    if (this.toEnd) {

      for (final ClefPosition ksp : this.nextClefPositions) {
        TuxGuitar.instance().getSongManager().getTrackManager().changeClef(
            this.track, ksp.getPosition(), ksp.getClef(), true);
      }
    }
    TuxGuitar.instance().fireUpdate();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
