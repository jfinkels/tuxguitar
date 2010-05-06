package org.herac.tuxguitar.midiinput;

class MiNote {
  private byte f_Fret; // 0-based fret index
  private byte f_Pitch; // MIDI note pitch
  private byte f_String; // 1-based string index
  private long f_TimeOff; // MIDI NOTE_OFF time [microsec]
  private long f_TimeOn; // MIDI NOTE_ON time [microsec]
  private byte f_Velocity; // MIDI note velocity

  public MiNote(byte inString, byte inFret, byte inPitch, byte inVelocity,
      long inTime) {
    f_String = inString;
    f_Fret = inFret;
    f_Pitch = inPitch;
    f_Velocity = inVelocity;
    f_TimeOn = inTime;
    f_TimeOff = -1;
  }

  public MiNote(MiNote inNote) {
    f_String = inNote.getString();
    f_Fret = inNote.getFret();
    f_Pitch = inNote.getPitch();
    f_Velocity = inNote.getVelocity();
    f_TimeOn = inNote.getTimeOn();
    f_TimeOff = inNote.getTimeOff();
  }

  long getDuration() {
    return (f_TimeOff - f_TimeOn);
  }

  byte getFret() {
    return (f_Fret);
  }

  byte getPitch() {
    return (f_Pitch);
  }

  byte getString() {
    return (f_String);
  }

  long getTimeOff() {
    return (f_TimeOff);
  }

  long getTimeOn() {
    return (f_TimeOn);
  }

  byte getVelocity() {
    return (f_Velocity);
  }

  void setTimeOff(long inTime) {
    f_TimeOff = inTime;
  }

  void setTimeOn(long inTime) {
    f_TimeOn = inTime;
  }
}
