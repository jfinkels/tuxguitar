/*
 * Created on 17-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.actions.edit;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.Action;
import org.herac.tuxguitar.gui.undo.CannotUndoException;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class UndoAction extends Action {
  public static final String NAME = "action.edit.undo";

  public UndoAction() {
    super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING
        | KEY_BINDING_AVAILABLE);
  }

  @Override
  protected int execute(TypedEvent e) {
    try {
      if (TuxGuitar.instance().getUndoableManager().canUndo()) {
        TuxGuitar.instance().getUndoableManager().undo();
      }
    } catch (CannotUndoException e1) {
      LOG.error(e1);
    }
    return 0;
  }
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger.getLogger(UndoAction.class);


}
