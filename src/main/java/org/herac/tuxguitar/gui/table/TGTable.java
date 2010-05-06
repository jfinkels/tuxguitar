package org.herac.tuxguitar.gui.table;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TGTable {
  private TGTableColumn columnCanvas;
  private SashForm columnControl;
  private TGTableColumn columnInstrument;
  private TGTableColumn columnName;
  private TGTableColumn columnNumber;
  private Composite rowControl;
  private int rowHeight;
  private List rows;
  private ScrolledComposite sComposite;
  private int scrollIncrement;
  private Composite table;

  public TGTable(Composite parent) {
    this.rows = new ArrayList();
    this.newTable(parent);
  }

  public void addRowItem(TGTableColumn column, Control control,
      boolean computeSize) {
    if (computeSize) {
      this.rowHeight = Math.max(this.rowHeight, control.computeSize(
          SWT.DEFAULT, SWT.DEFAULT).y);
      this.scrollIncrement = this.rowHeight;
    }
    column.addControl(control);
  }

  public TGTableColumn getColumnCanvas() {
    return this.columnCanvas;
  }

  public Composite getColumnControl() {
    return this.columnControl;
  }

  public TGTableColumn getColumnInstrument() {
    return this.columnInstrument;
  }

  public TGTableColumn getColumnName() {
    return this.columnName;
  }

  public TGTableColumn getColumnNumber() {
    return this.columnNumber;
  }

  public Composite getControl() {
    return this.table;
  }

  public int getMinHeight() {
    return (this.sComposite.getMinHeight() + (this.sComposite.getBorderWidth() * 2));
  }

  public TGTableRow getRow(int index) {
    if (index >= 0 && index < this.rows.size()) {
      return (TGTableRow) this.rows.get(index);
    }
    return null;
  }

  public Composite getRowControl() {
    return this.rowControl;
  }

  public int getRowCount() {
    return this.rows.size();
  }

  public int getRowHeight() {
    return this.rowHeight;
  }

  public int getScrollIncrement() {
    return this.scrollIncrement;
  }

  private void layoutColumns() {
    this.columnNumber.layout();
    this.columnName.layout();
    this.columnInstrument.layout();
    this.columnCanvas.layout();
  }

  private GridLayout newGridLayout(int cols, int marginWidth, int marginHeight,
      int horizontalSpacing, int verticalSpacing) {
    GridLayout layout = new GridLayout(cols, false);
    layout.marginWidth = marginWidth;
    layout.marginHeight = marginHeight;
    layout.horizontalSpacing = horizontalSpacing;
    layout.verticalSpacing = verticalSpacing;
    return layout;
  }

  public void newRow() {
    this.rows.add(new TGTableRow(this));
  }

  public void newTable(Composite parent) {
    this.sComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
    this.sComposite.setLayout(new GridLayout());
    this.sComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    this.sComposite.setAlwaysShowScrollBars(true);
    this.sComposite.setExpandHorizontal(true);
    this.sComposite.setExpandVertical(true);
    this.table = new Composite(this.sComposite, SWT.NONE);
    this.table.setLayout(newGridLayout(1, 0, 0, 0, 0));
    this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    this.columnControl = new SashForm(this.table, SWT.HORIZONTAL);
    this.columnControl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
        false));

    this.columnNumber = new TGTableColumn(this, SWT.LEFT);
    this.columnName = new TGTableColumn(this, SWT.LEFT);
    this.columnInstrument = new TGTableColumn(this, SWT.LEFT);
    this.columnCanvas = new TGTableColumn(this, SWT.CENTER);
    this.columnControl.setWeights(new int[] { 1, 7, 7, 20 });

    this.rowControl = new Composite(this.table, SWT.NONE);
    this.rowControl.setLayout(newGridLayout(1, 0, 1, 0, 1));
    this.rowControl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    this.sComposite.setContent(this.table);
  }

  private void notifyRemoved() {
    this.columnNumber.notifyRemoved();
    this.columnName.notifyRemoved();
    this.columnInstrument.notifyRemoved();
    this.columnCanvas.notifyRemoved();
  }

  public void removeRowsAfter(int index) {
    while (index < this.rows.size()) {
      TGTableRow row = (TGTableRow) this.rows.get(index);
      row.dispose();
      this.rows.remove(index);
    }
    this.notifyRemoved();
  }

  public void update() {
    this.layoutColumns();
    this.table.layout(true, true);
    this.sComposite.setMinHeight(this.table.computeSize(SWT.DEFAULT,
        SWT.DEFAULT).y);
    this.sComposite.getVerticalBar().setIncrement(
        (getScrollIncrement() + this.sComposite.getBorderWidth()));
  }

}
