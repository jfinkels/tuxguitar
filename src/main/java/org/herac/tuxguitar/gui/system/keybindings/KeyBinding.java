package org.herac.tuxguitar.gui.system.keybindings;

public class KeyBinding {

  public static final String MASK_SEPARATOR = "+";

  private int key;
  private int mask;

  public KeyBinding() {
    this(0, 0);
  }

  public KeyBinding(int key, int mask) {
    this.key = key;
    this.mask = mask;
  }

  @Override
  public Object clone() {
    return new KeyBinding(this.key, this.mask);
  }

  public int getKey() {
    return this.key;
  }

  public int getMask() {
    return this.mask;
  }

  private String getSpecialKey() {
    for (final KeyConversion conversion : KeyConversion.relations) {
      if (this.key == conversion.getCode()) {
        return conversion.getKey();
      }
    }
    return null;
  }

  private String getSpecialMask() {
    String mask = new String();
    for (final KeyConversion conversion : KeyConversion.relations) {
      if ((this.mask & conversion.getCode()) == conversion.getCode()) {
        mask += conversion.getKey() + MASK_SEPARATOR;
      }
    }
    return mask;
  }

  public boolean isSameAs(final KeyBinding kb) {
    return kb != null && this.key == kb.key && this.mask == kb.mask;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public void setMask(int mask) {
    this.mask = mask;
  }

  @Override
  public String toString() {
    String mask = getSpecialMask();
    String key = getSpecialKey();
    return (key != null ? (mask + key) : (mask + (char) this.key));
  }
}

class KeyConversion {

  protected static final KeyConversion[] relations = new KeyConversion[] {
      new KeyConversion("F1", KeyBindingConstants.F1),
      new KeyConversion("F2", KeyBindingConstants.F2),
      new KeyConversion("F3", KeyBindingConstants.F3),
      new KeyConversion("F4", KeyBindingConstants.F4),
      new KeyConversion("F5", KeyBindingConstants.F5),
      new KeyConversion("F6", KeyBindingConstants.F6),
      new KeyConversion("F7", KeyBindingConstants.F7),
      new KeyConversion("F8", KeyBindingConstants.F8),
      new KeyConversion("F9", KeyBindingConstants.F9),
      new KeyConversion("F10", KeyBindingConstants.F10),
      new KeyConversion("F11", KeyBindingConstants.F11),
      new KeyConversion("F12", KeyBindingConstants.F12),
      new KeyConversion("Esc", KeyBindingConstants.ESC),
      new KeyConversion("Pause", KeyBindingConstants.PAUSE),
      new KeyConversion("Print", KeyBindingConstants.PRINT_SCREEN),
      new KeyConversion("Ins", KeyBindingConstants.INSERT),
      new KeyConversion("Del", KeyBindingConstants.DELETE),
      new KeyConversion("Home", KeyBindingConstants.HOME),
      new KeyConversion("PgUp", KeyBindingConstants.PAGE_UP),
      new KeyConversion("PgDn", KeyBindingConstants.PAGE_DOWN),
      new KeyConversion("End", KeyBindingConstants.END),
      new KeyConversion("Up", KeyBindingConstants.UP),
      new KeyConversion("Down", KeyBindingConstants.DOWN),
      new KeyConversion("Left", KeyBindingConstants.LEFT),
      new KeyConversion("Right", KeyBindingConstants.RIGHT),
      new KeyConversion("Control", KeyBindingConstants.CONTROL),
      new KeyConversion("\u2318", KeyBindingConstants.COMMAND),
      new KeyConversion("Shift", KeyBindingConstants.SHIFT),
      new KeyConversion("Alt", KeyBindingConstants.ALT),
      new KeyConversion("Tab", KeyBindingConstants.TAB),
      new KeyConversion("Space", KeyBindingConstants.SPACE),
      new KeyConversion("Enter", KeyBindingConstants.ENTER),
      new KeyConversion("*", KeyBindingConstants.KEYPAD_MULTIPLY),
      new KeyConversion("/", KeyBindingConstants.KEYPAD_DIVIDE),
      new KeyConversion(".", KeyBindingConstants.KEYPAD_DECIMAL), };

  private int code;
  private String key;

  private KeyConversion(String key, int code) {
    this.key = key;
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }

  public String getKey() {
    return this.key;
  }
}