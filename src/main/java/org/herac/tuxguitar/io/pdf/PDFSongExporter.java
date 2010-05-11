package org.herac.tuxguitar.io.pdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.TGPainter;
import org.herac.tuxguitar.gui.editors.chord.ChordSelector;
import org.herac.tuxguitar.gui.editors.tab.Tablature;
import org.herac.tuxguitar.gui.editors.tab.layout.PrinterViewLayout;
import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;
import org.herac.tuxguitar.gui.helper.SyncThread;
import org.herac.tuxguitar.gui.printer.PrintDocument;
import org.herac.tuxguitar.gui.printer.PrintStyles;
import org.herac.tuxguitar.gui.printer.PrintStylesDialog;
import org.herac.tuxguitar.gui.util.MessageDialog;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGLocalFileExporter;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.util.TGSynchronizer;

public class PDFSongExporter implements TGLocalFileExporter {

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(PDFSongExporter.class);
  
  private class PrintDocumentImpl implements PrintDocument {

    private Rectangle bounds;
    private Image buffer;
    private PrinterViewLayout layout;
    private List<ImageData> pages;
    private TGPainter painter;
    private OutputStream stream;

    public PrintDocumentImpl(PrinterViewLayout layout, Rectangle bounds,
        OutputStream stream) {
      this.layout = layout;
      this.bounds = bounds;
      this.stream = stream;
      this.painter = new TGPainter();
      this.pages = new ArrayList<ImageData>();
    }

    protected void dispose() {
      this.layout.getTablature().dispose();
    }

    public void finish() {
      try {
        TGSynchronizer.instance().addRunnable(new TGSynchronizer.TGRunnable() {
          public void run() {
            dispose();
          }
        });
      } catch (Throwable e) {
        LOG.error(e);
      }
      this.write();
    }

    public Rectangle getBounds() {
      return this.bounds;
    }

    public TGPainter getPainter() {
      return this.painter;
    }

    public boolean isPaintable(int page) {
      return true;
    }

    public void pageFinish() {
      this.pages.add(this.buffer.getImageData());
      this.painter.dispose();
      this.buffer.dispose();
    }

    public void pageStart() {
      this.buffer = new Image(this.layout.getTablature().getDisplay(),
          this.bounds.width - this.bounds.x, this.bounds.height - this.bounds.y);
      this.painter.init(this.buffer);
    }

    public void start() {
      // Not implemented
    }

    protected void write() {
      try {
        PDFWriter.write(this.stream, this.pages);
      } catch (Throwable throwable) {
        MessageDialog.errorMessage(throwable);
      }
    }
  }

  private static final int PAGE_HEIGHT = 800;

  private static final int PAGE_WIDTH = 550;
  private OutputStream stream;

  private PrintStyles styles;

  public boolean configure(boolean setDefaults) {
    this.styles = (!setDefaults ? PrintStylesDialog.open(TuxGuitar.instance()
        .getShell()) : null);
    return (this.styles != null || setDefaults);
  }

  public void export(final OutputStream stream, final PrinterViewLayout layout) {
    new Thread(new Runnable() {
      public void run() {
        try {
          layout.getTablature().updateTablature();
          layout.makeDocument(new PrintDocumentImpl(layout, new Rectangle(0, 0,
              PAGE_WIDTH, PAGE_HEIGHT), stream));
          // new SyncThread(new Runnable() {
          // public void run() {
          // layout.makeDocument(new PrintDocumentImpl(layout,new
          // Rectangle(0,0,PAGE_WIDTH,PAGE_HEIGHT), stream));
          // }
          // }).start();
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void export(final OutputStream stream, final TGSong song,
      final PrintStyles data) {
    new Thread(new Runnable() {
      public void run() {
        try {
          TGSongManager manager = new TGSongManager();
          // manager.setFactory(new TGFactoryImpl());
          manager.setSong(song.clone());

          export(stream, manager, data);
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void export(final OutputStream stream, final TGSongManager manager,
      final PrintStyles data) {
    new SyncThread(new Runnable() {
      public void run() {
        try {
          Tablature tablature = new Tablature(TuxGuitar.instance().getShell());
          tablature.setSongManager(manager);

          PrinterViewLayout layout = new PrinterViewLayout(tablature, data, 1f);

          export(stream, layout);
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void exportSong(TGSong song) {
    try {
      if (this.stream != null) {
        this.export(this.stream, song, (this.styles != null ? this.styles
            : getDefaultStyles(song)));
      }
    } catch (Throwable throwable) {
      return;
    }
  }

  public PrintStyles getDefaultStyles(TGSong song) {
    PrintStyles styles = new PrintStyles();
    styles.setStyle(ViewLayout.DISPLAY_TABLATURE);
    styles.setFromMeasure(1);
    styles.setToMeasure(song.countMeasureHeaders());
    styles.setTrackNumber(1);
    styles.setBlackAndWhite(false);
    return styles;
  }

  public String getExportName() {
    return "PDF";
  }

  public TGFileFormat getFileFormat() {
    return new TGFileFormat("PDF", "*.pdf");
  }

  public void init(OutputStream stream) {
    this.stream = stream;
  }
}
