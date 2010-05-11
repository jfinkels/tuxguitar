package org.herac.tuxguitar.gui.util;

import org.apache.log4j.Logger;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.chord.ChordSelector;
import org.herac.tuxguitar.gui.system.config.TGConfigKeys;
import org.herac.tuxguitar.util.TGVersion;

public class WindowTitleUtil {
  public static final String VAR_APP_NAME = "appname";
  public static final String VAR_APP_VERSION = "appversion";

  private static final String VAR_END = "}";
  public static final String VAR_FILE_NAME = "filename";
  public static final String VAR_FILE_PATH = "filepath";
  public static final String VAR_SONG_ALBUM = "songalbum";
  public static final String VAR_SONG_ARTIST = "songartist";
  public static final String VAR_SONG_AUTHOR = "songauthor";
  public static final String VAR_SONG_NAME = "songname";
  private static final String VAR_START = "${";

  public static String getVar(String varName) {
    return (VAR_START + varName + VAR_END);
  }

  private static String parseString(String src) {
    try {
      if (src != null) {
        String result = new String();
        for (int pos = 0; pos < src.length(); pos++) {
          int startIndex = src.indexOf(VAR_START, pos);
          int endIndex = startIndex;
          if (startIndex >= 0) {
            endIndex = src.indexOf(VAR_END, (startIndex + VAR_START.length()));
          }
          if ((startIndex >= 0) && endIndex > (startIndex + VAR_START.length())) {
            if (startIndex > pos) {
              result += src.substring(pos, startIndex);
            }
            String var = src.substring(startIndex, (endIndex + 1));
            result += parseVar(var);
            pos = endIndex;
          } else {
            result += src.substring(pos, src.length());
            break;
          }
        }
        return result;
      }
    } catch (Exception e) {
      LOG.error(e);
    }
    return null;
  }

  /** The Logger for this class. */
  public static final transient Logger LOG = Logger
      .getLogger(WindowTitleUtil.class);
  
  public static String parseTitle() {
    String title = parseString(TuxGuitar.instance().getConfig()
        .getStringConfigValue(TGConfigKeys.WINDOW_TITLE));
    return ((title == null) ? TuxGuitar.APPLICATION_NAME : title);
  }

  private static String parseVar(String var) {
    String varName = var.substring((VAR_START.length()), (var.length() - 1));
    String varValue = var;
    if (varName.equals(VAR_APP_NAME)) {
      varValue = TuxGuitar.APPLICATION_NAME;
    } else if (varName.equals(VAR_APP_VERSION)) {
      varValue = TGVersion.CURRENT.toString();
    } else if (varName.equals(VAR_FILE_NAME)) {
      varValue = TuxGuitar.instance().getFileHistory().getCurrentFileName(
          FileChooser.DEFAULT_SAVE_FILENAME);
    } else if (varName.equals(VAR_FILE_PATH)) {
      varValue = TuxGuitar.instance().getFileHistory().getCurrentFilePath();
    } else if (varName.equals(VAR_SONG_NAME)) {
      varValue = TuxGuitar.instance().getSongManager().getSong().getName();
    } else if (varName.equals(VAR_SONG_AUTHOR)) {
      varValue = TuxGuitar.instance().getSongManager().getSong().getAuthor();
    } else if (varName.equals(VAR_SONG_ALBUM)) {
      varValue = TuxGuitar.instance().getSongManager().getSong().getAlbum();
    } else if (varName.equals(VAR_SONG_ARTIST)) {
      varValue = TuxGuitar.instance().getSongManager().getSong().getArtist();
    }
    return varValue;
  }
}