package org.herac.tuxguitar.gui.undo.undoables.track;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.undo.CannotRedoException;
import org.herac.tuxguitar.gui.undo.CannotUndoException;
import org.herac.tuxguitar.gui.undo.UndoableEdit;
import org.herac.tuxguitar.gui.undo.undoables.UndoableCaretHelper;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;

public class UndoableTrackChannel implements UndoableEdit {
  public static UndoableTrackChannel startUndo() {
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
    // TGFactory factory = TuxGuitar.instance().getSongManager().getFactory();
    int tracks = song.countTracks();

    UndoableTrackChannel undoable = new UndoableTrackChannel();
    undoable.doAction = UNDO_ACTION;
    undoable.undoCaret = new UndoableCaretHelper();
    undoable.undoChannels = new ArrayList<TGChannel>();

    for (int i = 0; i < tracks; i++) {
      TGTrack track = song.getTrack(i);
      undoable.undoChannels.add(track.getChannel().clone());
    }
    return undoable;
  }

  private int doAction;
  private UndoableCaretHelper redoCaret;
  private List<TGChannel> redoChannels;
  private UndoableCaretHelper undoCaret;

  private List<TGChannel> undoChannels;

  private UndoableTrackChannel() {
    super();
  }

  public boolean canRedo() {
    return (this.doAction == REDO_ACTION);
  }

  public boolean canUndo() {
    return (this.doAction == UNDO_ACTION);
  }

  public UndoableTrackChannel endUndo() {
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
//    TGFactory factory = TuxGuitar.instance().getSongManager().getFactory();
    int tracks = song.countTracks();

    this.redoCaret = new UndoableCaretHelper();
    this.redoChannels = new ArrayList<TGChannel>();

    for (int i = 0; i < tracks; i++) {
      TGTrack track = song.getTrack(i);
      this.redoChannels.add(track.getChannel().clone());
    }
    return this;
  }

  public void redo() throws CannotRedoException {
    if (!canRedo()) {
      throw new CannotRedoException();
    }
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
    for (int i = 0; i < this.redoChannels.size(); i++) {
      TGChannel channel = (TGChannel) this.redoChannels.get(i);
      TGTrack track = song.getTrack(i);
      track.setChannel(channel.clone());
    }
    TuxGuitar.instance().getMixer().updateValues();
    TuxGuitar.instance().getTable().fireUpdate(false);
    TuxGuitar.instance().updateCache(true);
    if (TuxGuitar.instance().getPlayer().isRunning()) {
      TuxGuitar.instance().getPlayer().updateControllers();
    }

    this.redoCaret.update();
    this.doAction = UNDO_ACTION;
  }

  public void undo() throws CannotUndoException {
    if (!canUndo()) {
      throw new CannotUndoException();
    }
    TGSong song = TuxGuitar.instance().getSongManager().getSong();
    for (int i = 0; i < this.undoChannels.size(); i++) {
      TGChannel channel = (TGChannel) this.undoChannels.get(i);
      TGTrack track = song.getTrack(i);
      track.setChannel(channel.clone());
    }
    TuxGuitar.instance().getMixer().updateValues();
    TuxGuitar.instance().getTable().fireUpdate(false);
    TuxGuitar.instance().updateCache(true);
    if (TuxGuitar.instance().getPlayer().isRunning()) {
      TuxGuitar.instance().getPlayer().updateControllers();
    }

    this.undoCaret.update();

    this.doAction = REDO_ACTION;
  }
}
