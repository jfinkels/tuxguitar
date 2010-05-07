/*
 * Created on 08-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.io.base.TGFileFormat;
import org.herac.tuxguitar.io.base.TGFileFormatManager;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FileChooser {

  private class FilterList {
    private String[] filterExtensions;
    private String[] filterNames;

    public FilterList(List<TGFileFormat> formats) {
      int size = (formats.size() + 2);
      this.filterNames = new String[size];
      this.filterExtensions = new String[size];
      this.filterNames[0] = new String("All Supported Formats");
      this.filterExtensions[0] = new String();
      for (int i = 1; i < (size - 1); i++) {
        TGFileFormat format = (TGFileFormat) formats.get(i - 1);
        this.filterNames[i] = format.getName();
        this.filterExtensions[i] = format.getSupportedFormats();
        this.filterExtensions[0] += (i > 1) ? ";" : "";
        this.filterExtensions[0] += format.getSupportedFormats();
      }
      this.filterNames[(size - 1)] = new String("All Files");
      this.filterExtensions[(size - 1)] = new String("*.*");
    }

    public String[] getFilterExtensions() {
      return this.filterExtensions;
    }

    public String[] getFilterNames() {
      return this.filterNames;
    }

  }

  public static TGFileFormat ALL_FORMATS = new TGFileFormat("All Files", "*.*");

  public static final String DEFAULT_OPEN_FILENAME = null;

  public static final String DEFAULT_SAVE_FILENAME = ("Untitled" + TGFileFormatManager.DEFAULT_EXTENSION);

  private static FileChooser instance;

  public static FileChooser instance() {
    if (instance == null) {
      instance = new FileChooser();
    }
    return instance;
  }

  private String getFileName(List<TGFileFormat> formats, String defaultName,
      boolean replaceExtension) {
    if (formats == null || formats.isEmpty()) {
      return defaultName;
    }
    String file = TuxGuitar.instance().getFileHistory().getCurrentFileName(
        defaultName);
    if (file != null && file.length() > 0) {
      int index = file.lastIndexOf('.');
      if (index > 0) {
        String fileName = file.substring(0, index);
        String fileExtension = file.substring(index).toLowerCase();
        for (final TGFileFormat format :  formats){
          if (format.getSupportedFormats() != null) {
            String[] extensions = format.getSupportedFormats().split(
                TGFileFormat.EXTENSION_SEPARATOR);
            if (extensions != null && extensions.length > 0) {
              for (int i = 0; i < extensions.length; i++) {
                if (extensions[i].equals("*" + fileExtension)) {
                  return file;
                }
              }
            }
          }
        }
        if (replaceExtension) {
          TGFileFormat format = (TGFileFormat) formats.get(0);
          if (format.getSupportedFormats() != null) {
            String[] extensions = format.getSupportedFormats().split(
                TGFileFormat.EXTENSION_SEPARATOR);
            if (extensions != null && extensions.length > 0) {
              if (extensions[0].length() > 1) {
                return (fileName + extensions[0].substring(1));
              }
            }
          }
        }
      }
    }
    return defaultName;
  }

  private <E> List<E> list(E o) {
    List<E> list = new ArrayList<E>();
    list.add(o);
    return list;
  }

  public String open(Shell parent, List<TGFileFormat> formats) {
    String currentPath = TuxGuitar.instance().getFileHistory()
        .getCurrentFilePath();
    String chooserPath = TuxGuitar.instance().getFileHistory().getOpenPath();
    boolean localFile = TuxGuitar.instance().getFileHistory().isLocalFile();
    boolean existentFile = (localFile && currentPath != null
        && chooserPath != null && currentPath.equals(chooserPath));

    FilterList filter = new FilterList(formats);
    FileDialog dialog = new FileDialog(parent, SWT.OPEN);
    dialog.setFileName((existentFile ? getFileName(formats,
        DEFAULT_OPEN_FILENAME, false) : null));
    dialog.setFilterPath(chooserPath);
    dialog.setFilterNames(filter.getFilterNames());
    dialog.setFilterExtensions(filter.getFilterExtensions());
    return openDialog(dialog);
  }

  public String open(Shell parent, TGFileFormat format) {
    return open(parent, list(format));
  }

  private String openDialog(FileDialog dialog) {
    String path = dialog.open();
    if (path != null) {
      File file = new File(path);
      File parent = file.getParentFile();
      if (parent != null && parent.exists() && parent.isDirectory()) {
        TuxGuitar.instance().getFileHistory().setChooserPath(
            parent.getAbsolutePath());
      }
    }
    return path;
  }

  public String save(Shell parent, List<TGFileFormat> formats) {
    String chooserPath = TuxGuitar.instance().getFileHistory().getSavePath();

    FilterList filter = new FilterList(formats);
    FileDialog dialog = new FileDialog(parent, SWT.SAVE);
    dialog.setFileName(getFileName(formats, DEFAULT_SAVE_FILENAME, true));
    dialog.setFilterPath(chooserPath);
    dialog.setFilterNames(filter.getFilterNames());
    dialog.setFilterExtensions(filter.getFilterExtensions());
    return openDialog(dialog);
  }

  public String save(Shell parent, TGFileFormat format) {
    return save(parent, list(format));
  }
}