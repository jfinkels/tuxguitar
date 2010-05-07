package org.herac.tuxguitar.io.base;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.io.tg.TGInputStream;
import org.herac.tuxguitar.io.tg.TGOutputStream;
import org.herac.tuxguitar.io.tg.TGStream;

public class TGFileFormatManager {

  public static final String DEFAULT_EXTENSION = TGStream.TG_FORMAT_EXTENSION;

  private static TGFileFormatManager instance;

  public static TGFileFormatManager instance() {
    if (instance == null) {
      instance = new TGFileFormatManager();
    }
    return instance;
  }

  private List<TGRawExporter> exporters;
  private List<TGRawImporter> importers;
  private List<TGInputStreamBase> inputStreams;
  private TGSongLoader loader;
  private List<TGOutputStreamBase> outputStreams;

  private TGSongWriter writer;

  private TGFileFormatManager() {
    this.loader = new TGSongLoader();
    this.writer = new TGSongWriter();
    this.inputStreams = new ArrayList<TGInputStreamBase>();
    this.outputStreams = new ArrayList<TGOutputStreamBase>();
    this.exporters = new ArrayList<TGRawExporter>();
    this.importers = new ArrayList<TGRawImporter>();
    this.addDefaultStreams();
  }

  private void addDefaultStreams() {
    this.addInputStream(new TGInputStream());
    this.addOutputStream(new TGOutputStream());
  }

  public void addExporter(TGRawExporter exporter) {
    this.exporters.add(exporter);
  }

  public void addImporter(TGRawImporter importer) {
    this.importers.add(importer);
  }

  public void addInputStream(TGInputStreamBase stream) {
    this.inputStreams.add(stream);
  }

  public void addOutputStream(TGOutputStreamBase stream) {
    this.outputStreams.add(stream);
  }

  public int countExporters() {
    return this.exporters.size();
  }

  public int countImporters() {
    return this.importers.size();
  }

  public int countInputStreams() {
    return this.inputStreams.size();
  }

  public int countOutputStreams() {
    return this.outputStreams.size();
  }

  private boolean existsFormat(TGFileFormat format, List<TGFileFormat> formats) {
    for (final TGFileFormat comparator : formats) {
      if (comparator.getName().equals(format.getName())
          || comparator.getSupportedFormats().equals(
              format.getSupportedFormats())) {
        return true;
      }
    }
    return false;
  }

  public List<TGRawExporter> getExporters() {
    return this.exporters;
  }

  public List<TGRawImporter> getImporters() {
    return this.importers;
  }

  public List<TGFileFormat> getInputFormats() {
    List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
    for (final TGInputStreamBase stream : this.inputStreams) {
      TGFileFormat format = stream.getFileFormat();
      if (!existsFormat(format, formats)) {
        formats.add(format);
      }
    }
    return formats;
  }

  public List<TGInputStreamBase> getInputStreams() {
    return this.inputStreams;
  }

  public TGSongLoader getLoader() {
    return this.loader;
  }

  public List<TGFileFormat> getOutputFormats() {
    List<TGFileFormat> formats = new ArrayList<TGFileFormat>();
    for (final TGOutputStreamBase stream : this.outputStreams) {
      TGFileFormat format = stream.getFileFormat();
      if (!existsFormat(format, formats)) {
        formats.add(format);
      }
    }
    return formats;
  }

  public List<TGOutputStreamBase> getOutputStreams() {
    return this.outputStreams;
  }

  public TGSongWriter getWriter() {
    return this.writer;
  }

  public void removeExporter(TGRawExporter exporter) {
    this.exporters.remove(exporter);
  }

  public void removeImporter(TGRawImporter importer) {
    this.importers.remove(importer);
  }

  public void removeInputStream(TGInputStreamBase stream) {
    this.inputStreams.remove(stream);
  }

  public void removeOutputStream(TGOutputStreamBase stream) {
    this.outputStreams.remove(stream);
  }
}
