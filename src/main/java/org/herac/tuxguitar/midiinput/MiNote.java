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
    this(inString, inFret, inPitch, inVelocity, inTime, -1);
  }

  public MiNote(byte inString, byte inFret, byte inPitch, byte inVelocity,
      long inTime, long outTime) {
    this.f_String = inString;
    this.f_Fret = inFret;
    this.f_Pitch = inPitch;
    this.f_Velocity = inVelocity;
    this.f_TimeOn = inTime;
    this.f_TimeOff = -1;
  }

  public MiNote(MiNote inNote) {
    this(inNote.getString(), inNote.getFret(), inNote.getPitch(), inNote
        .getVelocity(), inNote.getTimeOn(), inNote.getTimeOff());
  }

  long getDuration() {
    return this.f_TimeOff - this.f_TimeOn;
  }

  byte getFret() {
    return this.f_Fret;
  }

  byte getPitch() {
    return this.f_Pitch;
  }

  byte getString() {
    return this.f_String;
  }

  long getTimeOff() {
    return this.f_TimeOff;
  }

  long getTimeOn() {
    return this.f_TimeOn;
  }

  byte getVelocity() {
    return this.f_Velocity;
  }

  void setTimeOff(long inTime) {
    this.f_TimeOff = inTime;
  }

  void setTimeOn(long inTime) {
    this.f_TimeOn = inTime;
  }
}
