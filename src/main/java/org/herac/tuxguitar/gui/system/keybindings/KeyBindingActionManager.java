package org.herac.tuxguitar.gui.system.keybindings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.Action;
import org.herac.tuxguitar.gui.system.keybindings.xml.KeyBindingReader;
import org.herac.tuxguitar.gui.system.keybindings.xml.KeyBindingWriter;
import org.herac.tuxguitar.gui.util.TGFileUtils;

public class KeyBindingActionManager {

  protected class KeyBindingListener implements KeyListener {

    public void keyPressed(KeyEvent event) {
      KeyBinding kb = new KeyBinding();
      kb.setKey(event.keyCode);
      kb.setMask(event.stateMask);
      Action action = getActionForKeyBinding(kb);
      if (action != null) {
        action.process(event);
      }
    }

    public void keyReleased(KeyEvent evt) {
      // not implemented
    }
  }

  private List<KeyBindingAction> keyBindingsActions;

  private KeyBindingListener listener;

  public KeyBindingActionManager() {
    this.keyBindingsActions = new ArrayList<KeyBindingAction>();
    this.init();
  }

  public void appendListenersTo(Control control) {
    control.addKeyListener(this.listener);
  }

  public Action getActionForKeyBinding(KeyBinding kb) {
    Action action = KeyBindingReserveds.getActionForKeyBinding(kb);
    if (action != null) {
      return action;
    }

    for (final KeyBindingAction keyBindingAction : this.keyBindingsActions) {
      if (keyBindingAction.getKeyBinding() != null
          && kb.isSameAs(keyBindingAction.getKeyBinding())) {
        return TuxGuitar.instance().getAction(keyBindingAction.getAction());
      }
    }
    return null;
  }

  public List<KeyBindingAction> getKeyBindingActions() {
    return this.keyBindingsActions;
  }

  public KeyBinding getKeyBindingForAction(String action) {
    KeyBinding kb = KeyBindingReserveds.getKeyBindingForAction(action);
    if (kb != null) {
      return kb;
    }

    for (final KeyBindingAction keyBindingAction : this.keyBindingsActions) {
      if (action.equals(keyBindingAction.getAction())) {
        return keyBindingAction.getKeyBinding();
      }
    }
    return null;
  }

  private String getUserFileName() {
    return TGFileUtils.PATH_USER_CONFIG + File.separator + "shortcuts.xml";
  }

  public void init() {
    List<KeyBindingAction> enabled = KeyBindingReader.getKeyBindings(getUserFileName());
    this.keyBindingsActions.addAll((enabled != null ? enabled
        : KeyBindingActionDefaults.getDefaultKeyBindings()));
    this.listener = new KeyBindingListener();
  }

  public void reset(List<KeyBindingAction> keyBindings) {
    this.keyBindingsActions.clear();
    this.keyBindingsActions.addAll(keyBindings);
  }

  public void saveKeyBindings() {
    KeyBindingWriter.setBindings(getKeyBindingActions(), getUserFileName());
  }
}
