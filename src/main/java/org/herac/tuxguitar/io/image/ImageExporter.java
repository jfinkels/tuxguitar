package org.herac.tuxguitar.io.image;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.TGPainter;
import org.herac.tuxguitar.gui.editors.tab.Tablature;
import org.herac.tuxguitar.gui.editors.tab.layout.PrinterViewLayout;
import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;
import org.herac.tuxguitar.gui.helper.SyncThread;
import org.herac.tuxguitar.gui.printer.PrintDocument;
import org.herac.tuxguitar.gui.printer.PrintStyles;
import org.herac.tuxguitar.gui.util.MessageDialog;
import org.herac.tuxguitar.io.base.TGRawExporter;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.util.TGSynchronizer;

public class ImageExporter implements TGRawExporter {

  private class PrintDocumentImpl implements PrintDocument {

    private Rectangle bounds;
    private Image buffer;
    private ImageFormat format;
    private PrinterViewLayout layout;
    private List<ImageData> pages;
    private TGPainter painter;
    private String path;

    public PrintDocumentImpl(PrinterViewLayout layout, Rectangle bounds,
        ImageFormat format, String path) {
      this.layout = layout;
      this.bounds = bounds;
      this.path = path;
      this.painter = new TGPainter();
      this.pages = new ArrayList<ImageData>();
      this.format = format;
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

        ImageWriter.write(this.format, this.path, this.pages);
      } catch (Throwable throwable) {
        MessageDialog.errorMessage(throwable);
      }
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
          this.bounds.width + (this.bounds.x * 2), this.bounds.height
              + (this.bounds.y * 2));
      this.painter.init(this.buffer);
    }

    public void start() {
      // Not implemented
    }
  }

  private static final int PAGE_HEIGHT = 800;

  private static final int PAGE_WIDTH = 550;
  private ImageFormat format;
  private String path;

  private PrintStyles styles;

  public void export(final PrinterViewLayout layout) {
    new Thread(new Runnable() {
      public void run() {
        try {
          layout.getTablature().updateTablature();
          layout.makeDocument(new PrintDocumentImpl(layout, new Rectangle(25,
              25, PAGE_WIDTH, PAGE_HEIGHT), getFormat(), getPath()));
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void export(final TGSong song) {
    new Thread(new Runnable() {
      public void run() {
        try {
          TGSongManager manager = new TGSongManager();
          // manager.setFactory(new TGFactoryImpl());
          manager.setSong(song.clone());

          export(manager);
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void export(final TGSongManager manager) {
    new SyncThread(new Runnable() {
      public void run() {
        try {
          Tablature tablature = new Tablature(TuxGuitar.instance().getShell());
          tablature.setSongManager(manager);

          PrinterViewLayout layout = new PrinterViewLayout(tablature,
              getStyles(), 1f);

          export(layout);
        } catch (Throwable throwable) {
          MessageDialog.errorMessage(throwable);
        }
      }
    }).start();
  }

  public void exportSong(final TGSong song) {
    if (this.path != null) {
      if (this.styles == null) {
        this.styles = getDefaultStyles(song);
      }
      if (this.format == null) {
        this.format = ImageFormat.IMAGE_FORMATS[0];
      }
      export(song);
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
    return TuxGuitar.getProperty("tuxguitar-image.export-label");
  }

  public ImageFormat getFormat() {
    return this.format;
  }

  public String getPath() {
    return this.path;
  }

  public PrintStyles getStyles() {
    return this.styles;
  }

  public void setFormat(ImageFormat format) {
    this.format = format;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setStyles(PrintStyles styles) {
    this.styles = styles;
  }
}
