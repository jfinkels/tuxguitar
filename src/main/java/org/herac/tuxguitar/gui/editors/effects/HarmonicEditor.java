package org.herac.tuxguitar.gui.editors.effects;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.util.DialogUtils;
import org.herac.tuxguitar.song.models.TGNote;
import org.herac.tuxguitar.song.models.effects.TGEffectHarmonic;
import org.herac.tuxguitar.song.models.effects.harmonics.ArtificialHarmonic;
import org.herac.tuxguitar.song.models.effects.harmonics.NaturalHarmonic;
import org.herac.tuxguitar.song.models.effects.harmonics.PinchHarmonic;
import org.herac.tuxguitar.song.models.effects.harmonics.SemiHarmonic;
import org.herac.tuxguitar.song.models.effects.harmonics.TappedHarmonic;

public class HarmonicEditor extends SelectionAdapter {

  public static final int HEIGHT = 0;
  public static final int WIDTH = 400;

  protected Combo harmonicDataCombo;
  protected Combo harmonicType;
  protected TGEffectHarmonic result;

  protected Button[] typeButtons;

  private GridData getButtonData() {
    GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
    data.minimumWidth = 80;
    data.minimumHeight = 25;
    return data;
  }

  public TGEffectHarmonic getHarmonic() {
    TGEffectHarmonic result = null;
    switch (this.getSelectedType()) {
    case ArtificialHarmonic.ID:
      result = new ArtificialHarmonic();
      break;
    case NaturalHarmonic.ID:
      result = new NaturalHarmonic();
      break;
    case PinchHarmonic.ID:
      result = new PinchHarmonic();
      break;
    case TappedHarmonic.ID:
      result = new TappedHarmonic();
      break;
    case SemiHarmonic.ID:
      result = new SemiHarmonic();
      break;
    }

    if (result != null) {
      result.setData(this.harmonicDataCombo.getSelectionIndex());
    }

    return result;
  }

  protected int getSelectedType() {
    for (final Button button : this.typeButtons) {
      if (button.getSelection()) {
        return (Integer) button.getData();
      }
    }
    return TGEffectHarmonic.UNKNOWN_ID;
  }

  private String getTypeLabel(int harmonicId) {
    switch (harmonicId) {
    case ArtificialHarmonic.ID:
      return ArtificialHarmonic.LABEL;
    case NaturalHarmonic.ID:
      return NaturalHarmonic.LABEL;
    case PinchHarmonic.ID:
      return PinchHarmonic.LABEL;
    case SemiHarmonic.ID:
      return SemiHarmonic.LABEL;
    case TappedHarmonic.ID:
      return TappedHarmonic.LABEL;
    }
    return TGEffectHarmonic.UNKNOWN_LABEL;
  }

  private void initButton(Composite parent, SelectionListener listener,
      int index, int type, String label) {
    this.typeButtons[index] = new Button(parent, SWT.RADIO);
    this.typeButtons[index].setText(label);
    this.typeButtons[index].setLayoutData(new GridData(SWT.FILL, SWT.FILL,
        true, true));
    this.typeButtons[index].setData(type);
    this.typeButtons[index].addSelectionListener(listener);
  }

  protected void initDefaults(TGNote note) {
    int type = NaturalHarmonic.ID;

    if (note.getEffect().isHarmonic()) {
       type = note.getEffect().getHarmonic().getId();
    } else {
      boolean naturalValid = false;
      final int value = note.getValue() % 12;
      for (final int[] freq : NaturalHarmonic.NATURAL_FREQUENCIES) {
        if (value == (freq[0] % 12)) {
          naturalValid = true;
          break;
        }
      }
      if (!naturalValid) {
        this.typeButtons[0].setEnabled(false);
        type = ArtificialHarmonic.ID;
      }

    }

    for (final Button button : this.typeButtons) {
      button.setSelection(button.getData().equals(type));
    }

    update(note, type);
  }

  private GridData resizeData(GridData data, int minWidth) {
    data.minimumWidth = minWidth;
    return data;
  }

  public TGEffectHarmonic show(final TGNote note) {
    final Shell dialog = DialogUtils.newDialog(TuxGuitar.instance().getShell(),
        SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

    dialog.setLayout(new GridLayout());
    dialog.setText(TuxGuitar.getProperty("effects.harmonic-editor"));

    // ---------------------------------------------------------------------
    // ------------HARMONIC-------------------------------------------------
    // ---------------------------------------------------------------------
    Group group = new Group(dialog, SWT.SHADOW_ETCHED_IN);
    group.setLayout(new GridLayout());
    group.setLayoutData(resizeData(
        new GridData(SWT.FILL, SWT.FILL, true, true), WIDTH));
    group.setText(TuxGuitar.getProperty("effects.harmonic.type-of-harmonic"));

    this.typeButtons = new Button[5];
    SelectionListener listener = new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        update(note, getSelectedType());
      }
    };

    // Natural
    String label = "[" + NaturalHarmonic.LABEL + "] "
        + TuxGuitar.getProperty("effects.harmonic.natural");
    initButton(group, listener, 0, NaturalHarmonic.ID, label);

    // Artificial
    label = ("[" + ArtificialHarmonic.LABEL + "] " + TuxGuitar
        .getProperty("effects.harmonic.artificial"));
    initButton(group, listener, 1, ArtificialHarmonic.ID, label);

    // Tapped
    label = ("[" + TappedHarmonic.LABEL + "] " + TuxGuitar
        .getProperty("effects.harmonic.tapped"));
    initButton(group, listener, 2, TappedHarmonic.ID, label);

    // Pinch
    label = ("[" + PinchHarmonic.LABEL + "] " + TuxGuitar
        .getProperty("effects.harmonic.pinch"));
    initButton(group, listener, 3, PinchHarmonic.ID, label);

    // Semi
    label = ("[" + SemiHarmonic.LABEL + "] " + TuxGuitar
        .getProperty("effects.harmonic.semi"));
    initButton(group, listener, 4, SemiHarmonic.ID, label);

    this.harmonicDataCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
    this.harmonicDataCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
        true));

    // ---------------------------------------------------
    // ------------------BUTTONS--------------------------
    // ---------------------------------------------------
    Composite buttons = new Composite(dialog, SWT.NONE);
    buttons.setLayout(new GridLayout(3, false));
    buttons.setLayoutData(new GridData(SWT.END, SWT.BOTTOM, true, true));

    Button buttonOK = new Button(buttons, SWT.PUSH);
    buttonOK.setText(TuxGuitar.getProperty("ok"));
    buttonOK.setLayoutData(getButtonData());
    buttonOK.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        HarmonicEditor.this.result = getHarmonic();
        dialog.dispose();
      }
    });

    Button buttonClean = new Button(buttons, SWT.PUSH);
    buttonClean.setText(TuxGuitar.getProperty("clean"));
    buttonClean.setLayoutData(getButtonData());
    buttonClean.setEnabled(note.getEffect().isHarmonic());
    buttonClean.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        HarmonicEditor.this.result = null;
        dialog.dispose();
      }
    });

    Button buttonCancel = new Button(buttons, SWT.PUSH);
    buttonCancel.setText(TuxGuitar.getProperty("cancel"));
    buttonCancel.setLayoutData(getButtonData());
    buttonCancel.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        HarmonicEditor.this.result = note.getEffect().getHarmonic();
        dialog.dispose();
      }
    });

    this.initDefaults(note);

    dialog.setDefaultButton(buttonOK);

    DialogUtils.openDialog(dialog, DialogUtils.OPEN_STYLE_CENTER
        | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
    return this.result;
  }

  protected void update(TGNote note, int id) {
    TGEffectHarmonic h = note.getEffect().getHarmonic();
    this.harmonicDataCombo.removeAll();

    final boolean isNatural = id == NaturalHarmonic.ID;

    this.harmonicDataCombo.setEnabled(!isNatural);

    if (!isNatural) {
      for (final int[] freq : NaturalHarmonic.NATURAL_FREQUENCIES) {
        this.harmonicDataCombo.add(this.getTypeLabel(id) + "("
            + Integer.toString(freq[0]) + ")");
      }
      this.harmonicDataCombo.select((h != null && h.getId() == id) ? h
          .getData() : 0);
    }
  }
}
