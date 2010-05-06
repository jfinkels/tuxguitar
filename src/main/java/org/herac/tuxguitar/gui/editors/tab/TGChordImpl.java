/*
 * Created on 01-dic-2005
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.editors.tab;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.herac.tuxguitar.gui.editors.TGPainter;
import org.herac.tuxguitar.gui.editors.tab.layout.ViewLayout;
import org.herac.tuxguitar.song.models.TGChord;
import org.herac.tuxguitar.song.models.TGString;

/**
 * @author julian
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class TGChordImpl extends TGChord {

  public static final int MAX_FRETS = 6;

  private Color backgroundColor;
  private Color color;
  private Image diagram;
  private int diagramHeight;
  private int diagramWidth;
  private boolean editing;
  private Font firstFretFont;
  private int firstFretSpacing;
  private Font font;
  private Color foregroundColor;
  private int fretSpacing;
  private int height;
  private int nameHeight;
  private int nameWidth;
  private Color noteColor;
  private int noteSize;
  private int posX;
  private int posY;
  private int stringSpacing;
  private int style;
  private int tonic;
  private Color tonicColor;

  private int width;

  public TGChordImpl(int length) {
    super(length);
  }

  public void addFretValue(int string, int fret) {
    if (!isDisposed() && this.getFretValue(string) != fret) {
      this.dispose();
    }
    super.addFretValue(string, fret);
  }

  public void calculateFirstFret() {
    int minimum = -1;
    int maximum = -1;
    boolean zero = false;
    for (int i = 0; i < getStrings().length; i++) {
      int fretValue = getFretValue(i);
      zero = (zero || fretValue == 0);
      if (fretValue > 0) {
        minimum = (minimum < 0) ? fretValue : Math.min(minimum, fretValue);
        maximum = (Math.max(maximum, fretValue));
      }
    }
    int firstFret = (zero && maximum < MAX_FRETS) ? 1 : minimum;
    setFirstFret(Math.max(firstFret, 1));
  }

  public void dispose() {
    if (!isDisposed()) {
      this.diagram.dispose();
    }
  }

  public Color getBackgroundColor() {
    return this.backgroundColor;
  }

  public TGBeatImpl getBeatImpl() {
    return (TGBeatImpl) getBeat();
  }

  public Color getColor() {
    return this.color;
  }

  public Font getFirstFretFont() {
    return this.firstFretFont;
  }

  public int getFirstFretSpacing() {
    return this.firstFretSpacing;
  }

  public Font getFont() {
    return this.font;
  }

  public Color getForegroundColor() {
    return this.foregroundColor;
  }

  public int getFretSpacing() {
    return this.fretSpacing;
  }

  public int getHeight() {
    return this.height;
  }

  public Color getNoteColor() {
    return this.noteColor;
  }

  public int getNoteSize() {
    return this.noteSize;
  }

  public int getPaintPosition(int index) {
    return getBeatImpl().getMeasureImpl().getTs().getPosition(index);
  }

  public int getPosX() {
    return (isEditing()) ? this.posX : getBeatImpl().getPosX();
  }

  public int getPosY() {
    return this.posY;
  }

  public int getStringSpacing() {
    return this.stringSpacing;
  }

  private int getStringValue(int number) {
    TGString string = getBeat().getMeasure().getTrack().getString(number);
    return string.getValue();
  }

  public Color getTonicColor() {
    return this.tonicColor;
  }

  public int getWidth() {
    return this.width;
  }

  public boolean isDisposed() {
    return (this.diagram == null || this.diagram.isDisposed());
  }

  public boolean isEditing() {
    return this.editing;
  }

  private boolean isSameColor(Color c1, Color c2) {
    if (c1 == null && c2 == null) {
      return true;
    }
    if (c1 != null && c2 != null && !c1.isDisposed() && !c2.isDisposed()) {
      RGB rgb1 = c1.getRGB();
      RGB rgb2 = c2.getRGB();
      if (rgb1 != null && rgb2 != null) {
        return (rgb1.red == rgb2.red && rgb1.green == rgb2.green && rgb1.blue == rgb2.blue);
      }
    }
    return false;
  }

  private boolean isSameFont(Font f1, Font f2) {
    if (f1 == null && f2 == null) {
      return true;
    }
    if (f1 != null && f2 != null && !f1.isDisposed() && !f2.isDisposed()) {
      FontData[] fd1 = f1.getFontData();
      FontData[] fd2 = f2.getFontData();
      if (fd1.length > 0 && fd2.length > 0) {
        boolean sameName = fd1[0].getName().equals(fd2[0].getName());
        boolean sameStyle = fd1[0].getStyle() == fd2[0].getStyle();
        boolean sameHeight = fd1[0].getHeight() == fd2[0].getHeight();
        return (sameName && sameStyle && sameHeight);
      }
    }
    return false;
  }

  public void paint(TGPainter painter, int fromX, int fromY) {
    int x = (fromX + getPosX());
    int y = (fromY + getPosY());
    if ((this.style & ViewLayout.DISPLAY_CHORD_DIAGRAM) != 0) {
      if (this.diagram != null) {
        painter.drawImage(this.diagram, x
            - ((this.diagramWidth - getFirstFretSpacing()) / 2)
            - getFirstFretSpacing(), y);
      } else {
        paintDiagram(painter, x
            - ((this.diagramWidth - getFirstFretSpacing()) / 2)
            - getFirstFretSpacing(), y);
      }
    }
    if ((this.style & ViewLayout.DISPLAY_CHORD_NAME) != 0 && getName() != null
        && getName().length() > 0) {
      painter.setFont(getFont());
      painter.setForeground(getForegroundColor());
      painter.setBackground(getBackgroundColor());
      painter.drawString(getName(), x - (this.nameWidth / 2), y
          + (this.height - this.nameHeight));
    }
  }

  public void paint(ViewLayout layout, TGPainter painter, int fromX, int fromY) {
    layout.setChordStyle(this);
    this.setPosY(getPaintPosition(TGTrackSpacing.POSITION_CHORD));
    this.setEditing(false);
    this.update(painter, layout.isBufferEnabled());
    this.paint(painter, getBeatImpl().getSpacing() + fromX
        + Math.round(4f * layout.getScale()), fromY);
  }

  protected void paintDiagram(TGPainter painter, int fromX, int fromY) {
    Font font = getFirstFretFont();
    painter.setBackground(getBackgroundColor());
    painter.initPath(TGPainter.PATH_FILL);
    painter.addRectangle(fromX, fromY, this.diagramWidth, this.diagramHeight);
    painter.closePath();
    painter.setForeground(getColor());

    // dibujo las cuerdas
    int x = fromX + getStringSpacing();
    int y = fromY + getFretSpacing();

    if (font != null) {
      String firstFretString = Integer.toString(getFirstFret());
      painter.setFont(font);
      Point size = painter.getStringExtent(firstFretString);
      painter.drawString(firstFretString, fromX
          + (getFirstFretSpacing() - size.x), Math.round(y
          + ((getFretSpacing() / 2f) - (size.y / 2f))));
      x += getFirstFretSpacing();
    }

    painter.initPath();
    painter.setAntialias(false);
    for (int i = 0; i < getStrings().length; i++) {
      int x1 = x + (i * getStringSpacing());
      int x2 = x + (i * getStringSpacing());
      int y1 = y;
      int y2 = y + ((getFretSpacing() * (MAX_FRETS - 1)));
      painter.moveTo(x1, y1);
      painter.lineTo(x2, y2);
    }
    painter.closePath();

    // dibujo las cegillas
    painter.initPath();
    painter.setAntialias(false);
    for (int i = 0; i < MAX_FRETS; i++) {
      int x1 = x;
      int x2 = x + ((getStringSpacing() * (countStrings() - 1)));
      int y1 = y + (i * getFretSpacing());
      int y2 = y + (i * getFretSpacing());
      painter.moveTo(x1, y1);
      painter.lineTo(x2, y2);
    }
    painter.closePath();

    painter.setLineWidth(1);
    // dibujo las notas
    for (int i = 0; i < getStrings().length; i++) {
      int fret = getFretValue(i);
      int noteX = x
          + ((getStringSpacing() * (countStrings() - 1)) - (getStringSpacing() * i));
      if (fret < 0) {
        painter.initPath();
        painter.moveTo((noteX - (getNoteSize() / 2)), fromY);
        painter.lineTo((noteX + (getNoteSize() / 2)), fromY + getNoteSize());
        painter.moveTo((noteX + (getNoteSize() / 2)), fromY);
        painter.lineTo((noteX - (getNoteSize() / 2)), fromY + getNoteSize());
        painter.closePath();
      } else if (fret == 0) {
        painter.initPath();
        painter.addOval(noteX - (getNoteSize() / 2), fromY, getNoteSize(),
            getNoteSize());
        painter.closePath();
      } else {
        painter
            .setBackground((this.tonic >= 0 && ((getStringValue(i + 1) + fret) % 12) == this.tonic) ? getTonicColor()
                : getNoteColor());
        painter.initPath(TGPainter.PATH_FILL);
        fret -= (getFirstFret() - 1);
        int noteY = y + ((getFretSpacing() * fret) - (getFretSpacing() / 2));
        painter.addOval(noteX - (getNoteSize() / 2), noteY
            - (getNoteSize() / 2), (getNoteSize() + 1), (getNoteSize() + 1));
        painter.closePath();
      }
    }
  }

  public void setBackgroundColor(Color backgroundColor) {
    if (!isDisposed() && !isSameColor(this.backgroundColor, backgroundColor)) {
      this.dispose();
    }
    this.backgroundColor = backgroundColor;
  }

  public void setColor(Color color) {
    if (!isDisposed() && !isSameColor(this.color, color)) {
      this.dispose();
    }
    this.color = color;
  }

  public void setEditing(boolean editing) {
    this.editing = editing;
  }

  public void setFirstFret(int firstFret) {
    if (!isDisposed() && this.getFirstFret() != firstFret) {
      this.dispose();
    }
    super.setFirstFret(firstFret);
  }

  public void setFirstFretFont(Font firstFretFont) {
    if (!isDisposed() && !isSameFont(this.firstFretFont, firstFretFont)) {
      this.dispose();
    }
    this.firstFretFont = firstFretFont;
  }

  public void setFirstFretSpacing(int firstFretSpacing) {
    if (!isDisposed() && this.firstFretSpacing != firstFretSpacing) {
      this.dispose();
    }
    this.firstFretSpacing = firstFretSpacing;
  }

  public void setFont(Font font) {
    if (!isDisposed() && !isSameFont(this.font, font)) {
      this.dispose();
    }
    this.font = font;
  }

  public void setForegroundColor(Color foregroundColor) {
    if (!isDisposed() && !isSameColor(this.foregroundColor, foregroundColor)) {
      this.dispose();
    }
    this.foregroundColor = foregroundColor;
  }

  public void setFretSpacing(int fretSpacing) {
    if (!isDisposed() && this.fretSpacing != fretSpacing) {
      this.dispose();
    }
    this.fretSpacing = fretSpacing;
  }

  public void setNoteColor(Color noteColor) {
    if (!isDisposed() && !isSameColor(this.noteColor, noteColor)) {
      this.dispose();
    }
    this.noteColor = noteColor;
  }

  public void setNoteSize(int noteSize) {
    if (!isDisposed() && this.noteSize != noteSize) {
      this.dispose();
    }
    this.noteSize = noteSize;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

  public void setStringSpacing(int stringSpacing) {
    if (!isDisposed() && this.stringSpacing != stringSpacing) {
      this.dispose();
    }
    this.stringSpacing = stringSpacing;
  }

  public void setStyle(int style) {
    this.style = style;
  }

  public void setTonic(int tonic) {
    if (!isDisposed() && this.tonic != tonic) {
      this.dispose();
    }
    this.tonic = tonic;
  }

  public void setTonicColor(Color tonicColor) {
    if (!isDisposed() && !isSameColor(this.tonicColor, tonicColor)) {
      this.dispose();
    }
    this.tonicColor = tonicColor;
  }

  public void update(TGPainter painter, boolean makeBuffer) {
    this.width = 0;
    this.height = 0;
    if (getFirstFret() <= 0) {
      this.calculateFirstFret();
    }
    if ((this.style & ViewLayout.DISPLAY_CHORD_NAME) != 0) {
      this.updateName(painter);
      this.width = Math.max(this.width, this.nameWidth);
      this.height += this.nameHeight;
    }
    if ((this.style & ViewLayout.DISPLAY_CHORD_DIAGRAM) != 0) {
      this.updateDiagram((makeBuffer ? painter.getGC().getDevice() : null));
      this.width = Math.max(this.width, this.diagramWidth);
      this.height += this.diagramHeight;
    }
  }

  protected void updateDiagram(Device device) {
    Font font = getFirstFretFont();
    this.diagramWidth = getStringSpacing()
        + (getStringSpacing() * countStrings())
        + ((font != null) ? getFirstFretSpacing() : 0);
    this.diagramHeight = getFretSpacing() + (getFretSpacing() * MAX_FRETS);
    if (device != null && (this.diagram == null || this.diagram.isDisposed())) {
      this.diagram = new Image(device, this.diagramWidth, this.diagramHeight);
      TGPainter painter = new TGPainter(new GC(this.diagram));
      paintDiagram(painter, 0, 0);
      painter.dispose();
    }
  }

  protected void updateName(TGPainter painter) {
    String name = getName();
    if (painter == null || name == null || name.length() == 0) {
      this.nameWidth = 0;
      this.nameHeight = 0;
      return;
    }
    Point point = painter.getStringExtent(name);
    this.nameWidth = point.x;
    this.nameHeight = point.y;
  }
}