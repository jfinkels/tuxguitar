/*
 * Created on 09-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.helper;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.editors.chord.ChordSelector;
import org.herac.tuxguitar.util.TGSynchronizer;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class SyncThread extends Thread {

  private TGSynchronizer.TGRunnable runnable;

  public SyncThread(final Runnable runnable) {
    this(new TGSynchronizer.TGRunnable() {
      public void run() throws Throwable {
        runnable.run();
      }
    });
  }

  public SyncThread(TGSynchronizer.TGRunnable runnable) {
    this.runnable = runnable;
  }

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(SyncThread.class);
  
  @Override
  public void run() {
    try {
      TGSynchronizer.instance().addRunnable(this.runnable);
    } catch (Throwable e) {
      LOG.error(e);
    }
  }
}