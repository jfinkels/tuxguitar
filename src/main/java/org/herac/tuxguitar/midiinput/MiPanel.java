package org.herac.tuxguitar.midiinput;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.util.DialogUtils;
import org.herac.tuxguitar.gui.util.MessageDialog;

class MiPanel {
  static private MiPanel s_Instance;

  static MiPanel instance() {
    if (s_Instance == null)
      s_Instance = new MiPanel();

    return s_Instance;
  }

  private Button f_BtnConfig;
  private Button f_BtnRecord;
  private Button f_BtnStop;
  private Combo f_CmbMode;

  private Shell f_Dialog = null;

  void showDialog(Shell parent) {
    if (this.f_Dialog != null)
      this.f_Dialog.forceActive();
    else {
      try {
        this.f_Dialog = DialogUtils.newDialog(parent, SWT.DIALOG_TRIM);
        this.f_Dialog.setLayout(new GridLayout());
        this.f_Dialog.setText(TuxGuitar.getProperty("midiinput.panel.title"));

        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.minimumWidth = 80;
        data.minimumHeight = 25;

        // MODE
        Group groupMode = new Group(f_Dialog, SWT.SHADOW_ETCHED_IN);
        groupMode.setLayout(new GridLayout(3, false));
        groupMode.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupMode.setText(TuxGuitar
            .getProperty("midiinput.panel.label.group.mode"));

        // MODE combo
        Label lblMode = new Label(groupMode, SWT.LEFT);
        lblMode.setText(TuxGuitar.getProperty("midiinput.panel.label.mode")
            + ":");

        this.f_CmbMode = new Combo(groupMode, SWT.DROP_DOWN | SWT.READ_ONLY);
        this.f_CmbMode.setLayoutData(new GridData(130, SWT.DEFAULT));

        this.f_CmbMode.add(TuxGuitar.getProperty("midiinput.mode.echo"));
        this.f_CmbMode.add(TuxGuitar.getProperty("midiinput.mode.chords"));
        this.f_CmbMode.add(TuxGuitar.getProperty("midiinput.mode.scales"));
        // /* RECORDING
        this.f_CmbMode.add(TuxGuitar.getProperty("midiinput.mode.record"));
        // */
        this.f_CmbMode.select(MiConfig.instance().getMode());

        this.f_CmbMode.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent arg0) {
            int mode = f_CmbMode.getSelectionIndex();

            if (mode != MiConfig.instance().getMode()) {
              MiConfig.getConfig().setProperty(MiConfig.KEY_MODE, mode);
              MiConfig.getConfig().save();

              MiProvider.instance().setMode(mode);
              updateControls();
            }
          }
        });

        // CONFIGURE button
        this.f_BtnConfig = new Button(groupMode, SWT.PUSH);
        this.f_BtnConfig.setLayoutData(data);

        this.f_BtnConfig.setText(TuxGuitar
            .getProperty("midiinput.panel.button.config"));
        this.f_BtnConfig.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent arg0) {
            MiConfig.instance().showDialog(f_Dialog);
          }
        });

        // /* RECORDING
        // Recording
        Group groupRec = new Group(f_Dialog, SWT.SHADOW_ETCHED_IN);
        groupRec.setLayout(new GridLayout(2, false));
        groupRec.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        groupRec.setText(TuxGuitar
            .getProperty("midiinput.panel.label.group.rec"));

        // START button
        this.f_BtnRecord = new Button(groupRec, SWT.PUSH);
        this.f_BtnRecord.setLayoutData(data);

        this.f_BtnRecord.setText(TuxGuitar
            .getProperty("midiinput.panel.button.start"));
        this.f_BtnRecord.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent arg0) {
            MiRecorder.instance().start();
            updateControls();
          }
        });

        // STOP button
        this.f_BtnStop = new Button(groupRec, SWT.PUSH);
        this.f_BtnStop.setLayoutData(data);

        this.f_BtnStop.setText(TuxGuitar
            .getProperty("midiinput.panel.button.stop"));
        this.f_BtnStop.addSelectionListener(new SelectionAdapter() {
          public void widgetSelected(SelectionEvent arg0) {
            MiRecorder.instance().stop();
            updateControls();
          }
        });
        // */

        updateControls();
        DialogUtils.openDialog(this.f_Dialog, DialogUtils.OPEN_STYLE_CENTER
            | DialogUtils.OPEN_STYLE_PACK | DialogUtils.OPEN_STYLE_WAIT);
        this.f_Dialog = null;
      } catch (Exception e) {
        MessageDialog.errorMessage(e);
      }
    }
  }

  void updateControls() {
    this.f_CmbMode.setEnabled(!MiRecorder.instance().isRecording());
    this.f_BtnConfig.setEnabled(!MiRecorder.instance().isRecording());

    // /* RECORDING
    if (MiProvider.instance().getMode() != MiConfig.MODE_SONG_RECORDING) {
      this.f_BtnRecord.setEnabled(false);
      this.f_BtnStop.setEnabled(false);
    } else {
      this.f_BtnRecord.setEnabled(!MiRecorder.instance().isRecording());
      this.f_BtnStop.setEnabled(MiRecorder.instance().isRecording());
    }
    // */
  }
}
