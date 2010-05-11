package org.herac.tuxguitar.gui.actions;

import org.apache.log4j.Logger;

public class ActionLock {

  private static boolean working;

  public synchronized static boolean isLocked() {
    return working;
  }

  public synchronized static void lock() {
    working = true;
  }

  public synchronized static void unlock() {
    working = false;
  }

  public synchronized static void waitFor() {
    try {
      while (isLocked()) {
        synchronized (ActionLock.class) {
          ActionLock.class.wait(1);
        }
      }
    } catch (InterruptedException e) {
      LOG.error(e);
    }
  }
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger.getLogger(ActionLock.class);

}
