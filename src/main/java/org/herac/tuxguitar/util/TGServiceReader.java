package org.herac.tuxguitar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

public class TGServiceReader {
  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(TGServiceReader.class);

  private static final class IteratorImpl implements Iterator<Class<?>> {
    private Iterator<String> iterator;
    private ClassLoader loader;
    private Class<?> spi;
    private Enumeration<URL> urls;

    public IteratorImpl(Class<?> spi, ClassLoader loader, Enumeration<URL> urls) {
      this.spi = spi;
      this.loader = loader;
      this.urls = urls;
      this.initialize();
    }

    public boolean hasNext() {
      return (this.iterator != null && this.iterator.hasNext());
    }

    private void initialize() {
      List<String> providers = new ArrayList<String>();
      while (this.urls.hasMoreElements()) {
        URL url = (URL) this.urls.nextElement();
        try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(url
              .openStream(), "UTF-8"));
          String line = null;
          while ((line = reader.readLine()) != null) {
            String provider = uncommentLine(line).trim();
            if (provider != null && provider.length() > 0) {
              providers.add(provider);
            }
          }
        } catch (UnsupportedEncodingException e) {
          LOG.error(e);
        } catch (IOException e) {
          LOG.error(e);
        }
      }
      this.iterator = providers.iterator();
    }

    public Class<?> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      try {
        Class<?> provider = (Class<?>) this.loader.loadClass(
            this.iterator.next()).newInstance();
        if (this.spi.isInstance(provider)) {
          return provider;
        }
      } catch (Throwable throwable) {
        LOG.error(throwable);
      }
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    private String uncommentLine(String line) {
      int index = line.indexOf('#');
      if (index >= 0) {
        return (line.substring(0, index));
      }
      return line;
    }
  }

  private static final String SERVICE_PATH = new String("META-INF/services/");

  public static Iterator<Class<?>> lookupProviders(Class<?> spi) {
    return TGServiceReader.lookupProviders(spi, TGClassLoader.instance()
        .getClassLoader());
  }

  public static Iterator<Class<?>> lookupProviders(Class<?> spi, ClassLoader loader) {
    try {
      if (spi == null || loader == null) {
        throw new IllegalArgumentException();
      }
      return new IteratorImpl(spi, loader, loader.getResources(SERVICE_PATH
          + spi.getName()));
    } catch (IOException ioex) {
      return Collections.EMPTY_LIST.iterator();
    }
  }
}
