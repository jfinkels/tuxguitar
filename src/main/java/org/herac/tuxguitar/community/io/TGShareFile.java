package org.herac.tuxguitar.community.io;

public class TGShareFile {

  private String description;
  private byte[] file;
  private String tagkeys;
  private String title;

  public TGShareFile() {
    this.title = new String();
    this.description = new String();
    this.tagkeys = new String();
  }

  public String getDescription() {
    return this.description;
  }

  public byte[] getFile() {
    return this.file;
  }

  public String getFilename() {
    return (this.title + ".tg");
  }

  public String getTagkeys() {
    return this.tagkeys;
  }

  public String getTitle() {
    return this.title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public void setTagkeys(String tagkeys) {
    this.tagkeys = tagkeys;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
