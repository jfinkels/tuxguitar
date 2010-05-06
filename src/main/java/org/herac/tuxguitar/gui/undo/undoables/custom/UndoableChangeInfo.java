package org.herac.tuxguitar.gui.undo.undoables.custom;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGSong;

public class UndoableChangeInfo implements UndoableEdit {
  public static UndoableChangeInfo startUndo() {
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
    UndoableChangeInfo undoable = new UndoableChangeInfo();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.undoName = song.getName();
    undoable.undoArtist = song.getArtist();
    undoable.undoAlbum = song.getAlbum();
    undoable.undoAuthor = song.getAuthor();
    undoable.undoDate = song.getDate();
    undoable.undoCopyright = song.getCopyright();
    undoable.undoWriter = song.getWriter();
    undoable.undoTranscriber = song.getTranscriber();
    undoable.undoComments = song.getComments();
    return undoable;
  }

  private int doAction;
  private String redoAlbum;
  private String redoArtist;
  private String redoAuthor;
  private UndoableCaretHelper redoCaret;
  private String redoComments;
  private String redoCopyright;
  private String redoDate;
  private String redoName;
  private String redoTranscriber;
  private String redoWriter;
  private String undoAlbum;
  private String undoArtist;
  private String undoAuthor;
  private UndoableCaretHelper undoCaret;
  private String undoComments;
  private String undoCopyright;
  private String undoDate;
  private String undoName;
  private String undoTranscriber;

  private String undoWriter;

  private UndoableChangeInfo() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableChangeInfo endUndo() {
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
    this.redoCaret = new UndoableCaretHelper();
    this.redoName = song.getName();
    this.redoArtist = song.getArtist();
    this.redoAlbum = song.getAlbum();
    this.redoAuthor = song.getAuthor();
    this.redoDate = song.getDate();
    this.redoCopyright = song.getCopyright();
    this.redoWriter = song.getWriter();
    this.redoTranscriber = song.getTranscriber();
    this.redoComments = song.getComments();
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TuxGuitar.instance().getSongManager().setProperties(this.redoName,
        this.redoArtist, this.redoAlbum, this.redoAuthor, this.redoDate,
        this.redoCopyright, this.redoWriter, this.redoTranscriber,
        this.redoComments);
    TuxGuitar.instance().showTitle();
    this.redoCaret.update();

    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TuxGuitar.instance().getSongManager().setProperties(this.undoName,
        this.undoArtist, this.undoAlbum, this.undoAuthor, this.undoDate,
        this.undoCopyright, this.undoWriter, this.undoTranscriber,
        this.undoComments);
    TuxGuitar.instance().showTitle();
    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
