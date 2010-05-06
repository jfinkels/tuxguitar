package org.herac.tuxguitar.io.tef.base;

public class TEChord {

  private String name;
  private byte[] strings;

  public TEChord(byte[] strings, String name) {
    this.strings = strings;
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public byte[] getStrings() {
    return this.strings;
  }

  public String toString() {
    String string = new String("[CHORD]");
    string += "\n     Name:       " + getName();
    for (int i = 0; i < this.strings.length; i++) {
      if (this.strings[i] != -1) {
        string += "\n     String " + (i + 1) + ":    " + this.strings[i];
      }
    }
    return string;
  }
}
