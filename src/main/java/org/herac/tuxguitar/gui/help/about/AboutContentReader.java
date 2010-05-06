package org.herac.tuxguitar.gui.help.about;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.util.TGClassLoader;

public class AboutContentReader {
  public static final String AUTHORS = "authors";

  public static final String DESCRIPTION = "description";
  private static final String EXTENSION = ".dist";

  public static final String LICENSE = "license";
  public static final transient Logger LOG = Logger
      .getLogger(AboutContentReader.class);
  private static final String PREFIX = "about_";

  public AboutContentReader() {
    super();
  }

  public StringBuffer read(InputStream is) {
    StringBuffer sb = new StringBuffer();
    try {
      int length = 0;
      byte[] buffer = new byte[1024];
      while ((length = is.read(buffer)) != -1) {
        sb.append(new String(buffer, 0, length));
      }
      is.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sb;
  }

  public StringBuffer read(String doc) {
    String lang = TuxGuitar.instance().getLanguageManager().getLanguage();
    InputStream is = TGClassLoader.instance().getClassLoader()
        .getResourceAsStream(PREFIX + doc + "_" + lang + EXTENSION);
    if (is == null) {
      is = TGClassLoader.instance().getClassLoader().getResourceAsStream(
          PREFIX + doc + EXTENSION);
    }
    if (is != null) {
      return read(is);
    }
    LOG.debug(doc + ".txt");

    return new StringBuffer();
  }
}
