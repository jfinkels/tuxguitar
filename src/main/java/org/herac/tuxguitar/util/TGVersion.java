package org.herac.tuxguitar.util;

public class TGVersion {

  public static final TGVersion CURRENT = new TGVersion(1, 2, 0);

  private final int major;
  private final int minor;
  private final int revision;

  public TGVersion(final int major, final int minor, final int revision) {
    this.major = major;
    this.minor = minor;
    this.revision = revision;
  }

  @Override
  public String toString() {
    String version = this.major + "." + this.minor;
    if (this.revision > 0) {
      version += ("." + this.revision);
    }
    return version;
  }

  public boolean isSameVersion(final TGVersion version) {
    if (version == null) {
      return false;
    }
    
    return version.major == this.major && version.minor == this.minor
        && version.revision == this.revision;
  }
}
