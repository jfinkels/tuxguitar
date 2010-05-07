/*
 * Created on 08-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.system.config.TGConfigKeys;
import org.herac.tuxguitar.gui.util.TGFileUtils;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class FileHistory {

  private static final int URL_LIMIT = TuxGuitar.instance().getConfig()
      .getIntConfigValue(TGConfigKeys.MAX_HISTORY_FILES);

  private boolean changed;
  private String chooserPath;
  private boolean localFile;
  private boolean newFile;
  private boolean unsavedFile;
  private List<URL> urls;

  public FileHistory() {
    this.urls = new ArrayList<URL>();
    this.loadHistory();
    this.reset(null);
  }

  public void addURL(URL url) {
    if (url != null) {
      removeURL(url);
      this.urls.add(0, url);
      checkLimit();
      setChanged(true);
    }
    saveHistory();
  }

  private void checkLimit() {
    while (this.urls.size() > URL_LIMIT) {
      this.urls.remove(this.urls.size() - 1);
    }
  }

  protected String decode(String url) {
    try {
      return URLDecoder.decode(url, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return url;
  }

  public String getCurrentFileName(String defaultName) {
    if (!this.isNewFile()) {
      URL url = getCurrentURL();
      if (url != null) {
        return decode(new File(url.getFile()).getName());
      }
    }
    return defaultName;
  }

  public String getCurrentFilePath() {
    if (!this.isNewFile()) {
      URL url = getCurrentURL();
      if (url != null) {
        String file = getFilePath(url);
        if (file != null) {
          return decode(file);
        }
      }
    }
    return this.chooserPath;
  }

  protected URL getCurrentURL() {
    if (!this.urls.isEmpty()) {
      return (URL) this.urls.get(0);
    }
    return null;
  }

  protected String getFilePath(URL url) {
    if (isLocalFile(url)) {
      return new File(url.getFile()).getParent();
    }
    return null;
  }

  private String getHistoryFileName() {
    return TGFileUtils.PATH_USER_CONFIG + File.separator + "history.properties";
  }

  public String getOpenPath() {
    return this.chooserPath;
  }

  public String getSavePath() {
    String current = getCurrentFilePath();
    return (current != null ? current : this.chooserPath);
  }

  public List<URL> getURLs() {
    return this.urls;
  }

  public boolean isChanged() {
    return this.changed;
  }

  public boolean isLocalFile() {
    return this.localFile;
  }

  protected boolean isLocalFile(URL url) {
    try {
      if (url.getProtocol().equals(
          new File(url.getFile()).toURI().toURL().getProtocol())) {
        return true;
      }
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
    return false;
  }

  public boolean isNewFile() {
    return this.newFile;
  }

  public boolean isUnsavedFile() {
    return this.unsavedFile;
  }

  public void loadHistory() {
    try {
      this.urls.clear();
      if (new File(getHistoryFileName()).exists()) {
        InputStream inputStream = new FileInputStream(getHistoryFileName());
        Properties properties = new Properties();
        properties.load(inputStream);

        this.chooserPath = (String) properties.get("history.path");

        int count = Integer.parseInt((String) properties.get("history.count"));
        for (int i = 0; i < count; i++) {
          String url = (String) properties.get("history." + i);
          if (URL_LIMIT > i && url != null && url.length() > 0) {
            this.urls.add(new URL(url));
          }
        }
        setChanged(true);
      } else {
        this.saveHistory();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void removeURL(URL url) {
    for (int i = 0; i < this.urls.size(); i++) {
      URL old = (URL) this.urls.get(i);
      if (old.toString().equals(url.toString())) {
        this.urls.remove(i);
        break;
      }
    }
  }

  public void reset(URL url) {
    this.unsavedFile = false;
    this.newFile = (url == null);
    this.localFile = (url != null && isLocalFile(url));
    this.addURL(url);
  }

  public void saveHistory() {
    try {
      Properties properties = new Properties();

      int count = this.urls.size();
      for (int i = 0; i < count; i++) {
        properties.put("history." + i, this.urls.get(i).toString());
      }
      properties.put("history.count", Integer.toString(count));
      if (this.chooserPath != null) {
        properties.put("history.path", this.chooserPath);
      }
      properties.store(new FileOutputStream(getHistoryFileName()),
          "History Files");
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  public void setChooserPath(String chooserPath) {
    this.chooserPath = chooserPath;
  }

  public void setChooserPath(URL url) {
    String path = getFilePath(url);
    if (path != null) {
      this.setChooserPath(path);
    }
  }

  public void setUnsavedFile() {
    this.unsavedFile = true;
  }
}
