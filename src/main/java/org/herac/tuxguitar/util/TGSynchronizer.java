package org.herac.tuxguitar.util;

public class TGSynchronizer {

  public interface TGRunnable {
    public void run() throws Throwable;
  }

  public interface TGSynchronizerController {
    public void execute(TGSynchronizerTask task);

    public void executeLater(TGSynchronizerTask task);
  }

  public class TGSynchronizerTask {
    private TGRunnable runnable;
    private Throwable throwable;

    public TGSynchronizerTask(TGRunnable runnable) {
      this.runnable = runnable;
      this.throwable = null;
    }

    public Throwable getThrowable() {
      return this.throwable;
    }

    public void run() {
      try {
        this.runnable.run();
      } catch (Throwable throwable) {
        this.throwable = throwable;
      }
    }
  }

  private static TGSynchronizer instance;

  public static TGSynchronizer instance() {
    if (instance == null) {
      synchronized (TGSynchronizer.class) {
        instance = new TGSynchronizer();
      }
    }
    return instance;
  }

  private TGSynchronizerController controller;

  private TGSynchronizer() {
    super();
  }

  public void addRunnable(TGRunnable runnable) throws Throwable {
    TGSynchronizerTask task = new TGSynchronizerTask(runnable);
    this.controller.execute(task);
    if (task.getThrowable() != null) {
      throw task.getThrowable();
    }
  }

  public void runLater(TGRunnable runnable) throws Throwable {
    TGSynchronizerTask task = new TGSynchronizerTask(runnable);
    this.controller.executeLater(task);
    if (task.getThrowable() != null) {
      throw task.getThrowable();
    }
  }

  public void setController(TGSynchronizerController controller) {
    this.controller = controller;
  }
}
