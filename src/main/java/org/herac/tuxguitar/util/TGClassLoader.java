package org.herac.tuxguitar.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

public class TGClassLoader {

  private class URLClassLoaderImpl extends URLClassLoader {

    public URLClassLoaderImpl() {
      super(new URL[] {}, TGClassLoader.class.getClassLoader());
    }

    @Override
    public void addURL(URL url) {
      super.addURL(url);
    }

  }

  private static TGClassLoader instance;

  public static TGClassLoader instance() {
    if (instance == null) {
      instance = new TGClassLoader();
    }
    return instance;
  }

  private URLClassLoaderImpl classLoader;

  private TGClassLoader() {
    this.classLoader = new URLClassLoaderImpl();
  }

  public void addPath(String path) {
    try {
      this.classLoader.addURL(new File(path).toURI().toURL());
    } catch (MalformedURLException e) {
      LOG.error(e);
    }
  }

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(TGClassLoader.class);

  public void addPaths(File folder) {
    if (folder != null && folder.exists() && folder.isDirectory()) {
      String[] files = folder.list();
      for (int i = 0; i < files.length; i++) {
        try {
          this.addPath((folder.getAbsolutePath() + File.separator + files[i]));
        } catch (Throwable throwable) {
          LOG.error(throwable);
        }
      }
    }
  }

  public ClassLoader getClassLoader() {
    return this.classLoader;
  }

  public Object newInstance(String loadClassName) {
    Object object = null;
    try {
      object = getClassLoader().loadClass(loadClassName).newInstance();
    } catch (InstantiationException e) {
      LOG.error(e);
    } catch (IllegalAccessException e) {
      LOG.error(e);
    } catch (ClassNotFoundException e) {
      LOG.error(e);
    }
    return object;
  }

}