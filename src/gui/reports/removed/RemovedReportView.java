package gui.reports.removed;

import gui.common.ButtonBankListener;
import gui.common.ButtonBankPanel;
import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.FileFormat;
import gui.main.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.util.DateUtils;

@SuppressWarnings("serial")
public class RemovedReportView extends DialogView
		implements IRemovedReportView {

	private JPanel _valuesPanel;
	private JLabel _formatLabel;
	private JComboBox _formatBox;
	private ButtonGroup _buttonGroup;
	private JRadioButton _sinceLastButton;
	private JRadioButton _sinceDateButton;
	private SpinnerModel _sinceDateSpinnerModel;
	private JSpinner.DateEditor _sinceDateSpinnerEditor;
	private JSpinner _sinceDateSpinner;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public RemovedReportView(GUI parent,
			DialogBox dialog) {
		super(parent, dialog);

		construct();

		this._controller = new RemovedReportController(
				this);
	}

	@Override
	public IRemovedReportController getController() {
		return (IRemovedReportController) super
				.getController();
	}

	@Override
	protected void createComponents() {
		createValuesPanel();
		createButtonsPanel();
	}

	private void createValuesPanel() {
		this._valuesPanel = new JPanel();

		this._formatLabel = new JLabel("Format:");

		this._formatBox = new JComboBox();
		this._formatBox.addItem(FileFormat.PDF);
		this._formatBox.addItem(FileFormat.HTML);
		this._formatBox
				.setMaximumSize(this._formatBox
						.getPreferredSize());
		this._formatBox
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(
							ActionEvent evt) {
						if (eventsAreDisabled()) {
							return;
						}
						valuesChanged();
					}
				});

		this._sinceLastButton = new JRadioButton(
				"Since the last time I ran this report");
		this._sinceLastButton
				.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(
							ChangeEvent arg0) {
						if (eventsAreDisabled()) {
							return;
						}
						valuesChanged();
					}
				});

		this._sinceDateButton = new JRadioButton(
				"Since the following date:");
		this._sinceDateButton
				.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(
							ChangeEvent arg0) {
						if (eventsAreDisabled()) {
							return;
						}
						valuesChanged();
					}
				});

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils
				.earliestDate();

		this._sinceDateSpinnerModel = new SpinnerDateModel(
				initDate, earliestDate,
				latestDate, Calendar.YEAR);
		this._sinceDateSpinner = new JSpinner(
				this._sinceDateSpinnerModel);
		this._sinceDateSpinnerEditor = new JSpinner.DateEditor(
				this._sinceDateSpinner,
				DateUtils.DATE_FORMAT);
		this._sinceDateSpinner
				.setEditor(this._sinceDateSpinnerEditor);
		this._sinceDateSpinner
				.setMaximumSize(this._sinceDateSpinner
						.getPreferredSize());
		this._sinceDateSpinnerEditor
				.getTextField()
				.getDocument()
				.addDocumentListener(
						new DocumentListener() {

							@Override
							public void changedUpdate(
									DocumentEvent e) {
								return;
							}

							@Override
							public void insertUpdate(
									DocumentEvent e) {
								processChange(e);
							}

							@Override
							public void removeUpdate(
									DocumentEvent e) {
								processChange(e);
							}

							private void processChange(
									DocumentEvent e) {
								if (eventsAreDisabled()) {
									return;
								}
								if (RemovedReportView.this._sinceDateSpinnerEditor
										.getTextField()
										.hasFocus()) {
									valuesChanged();
								}
							}
						});
		// _sinceDateSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent arg0) {
		// if (eventsAreDisabled()) {
		// return;
		// }
		// valuesChanged();
		// }
		// });

		this._buttonGroup = new ButtonGroup();
		this._buttonGroup
				.add(this._sinceLastButton);
		this._buttonGroup
				.add(this._sinceDateButton);
	}

	private void createButtonsPanel() {
		this._buttonsPanel = new ButtonBankPanel(
				new String[] { "OK", "Cancel" },
				new ButtonBankListener() {
					@Override
					public void buttonPressed(
							int index,
							String label) {
						switch (index) {
							case 0:
								ok();
								RemovedReportView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								RemovedReportView.this._dialog
										.dispose();
								break;
							default:
								assert false;
								break;
						}
					}
				});

		this._okButton = this._buttonsPanel
				.getButtons()[0];
		this._dialog.getRootPane()
				.setDefaultButton(this._okButton);
	}

	@Override
	protected void layoutComponents() {
		layoutValuesPanel();

		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(15));
		add(this._valuesPanel);
		add(Box.createVerticalStrut(15));
		add(this._buttonsPanel);
		add(Box.createVerticalStrut(15));
	}

	private void layoutValuesPanel() {

		final int MARGIN_SPACING = 20;
		final int ROW_SPACING = 15;

		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new BoxLayout(
				formatPanel, BoxLayout.X_AXIS));
		formatPanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		formatPanel.add(this._formatLabel);
		formatPanel.add(Box
				.createHorizontalStrut(5));
		formatPanel.add(this._formatBox);
		formatPanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		formatPanel.add(Box
				.createHorizontalGlue());

		JPanel sinceLastPanel = new JPanel();
		sinceLastPanel
				.setLayout(new BoxLayout(
						sinceLastPanel,
						BoxLayout.X_AXIS));
		sinceLastPanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceLastPanel.add(this._sinceLastButton);
		sinceLastPanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceLastPanel.add(Box
				.createHorizontalGlue());

		JPanel sinceDatePanel = new JPanel();
		sinceDatePanel
				.setLayout(new BoxLayout(
						sinceDatePanel,
						BoxLayout.X_AXIS));
		sinceDatePanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceDatePanel.add(this._sinceDateButton);
		sinceDatePanel.add(Box
				.createHorizontalStrut(5));
		sinceDatePanel
				.add(this._sinceDateSpinner);
		sinceDatePanel
				.add(Box.createHorizontalStrut(MARGIN_SPACING));
		sinceDatePanel.add(Box
				.createHorizontalGlue());

		this._valuesPanel
				.setLayout(new BoxLayout(
						this._valuesPanel,
						BoxLayout.Y_AXIS));
		this._valuesPanel.add(formatPanel);
		this._valuesPanel
				.add(Box.createVerticalStrut(ROW_SPACING));
		this._valuesPanel.add(sinceLastPanel);
		this._valuesPanel
				.add(Box.createVerticalStrut(ROW_SPACING));
		this._valuesPanel.add(sinceDatePanel);
	}

	@Override
	public FileFormat getFormat() {
		return (FileFormat) this._formatBox
				.getSelectedItem();
	}

	@Override
	public void setFormat(FileFormat value) {
		boolean disabledEvents = disableEvents();
		try {
			this._formatBox
					.setSelectedItem(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableFormat(boolean value) {
		this._formatBox.setEnabled(value);
	}

	@Override
	public boolean getSinceLast() {
		return this._sinceLastButton.isSelected();
	}

	@Override
	public void setSinceLast(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sinceLastButton
					.setSelected(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceLast(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sinceLastButton
					.setEnabled(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setSinceLastValue(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			String text = "Since the last time I ran this report";
			if (value != null) {
				text += "  ["
						+ DateUtils
								.formatDateTime(value)
						+ "]";
			}
			this._sinceLastButton.setText(text);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public boolean getSinceDate() {
		return this._sinceDateButton.isSelected();
	}

	@Override
	public void setSinceDate(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sinceDateButton
					.setSelected(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceDate(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sinceDateButton
					.setEnabled(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public Date getSinceDateValue() {

		// return (Date) _sinceDateSpinnerModel.getValue();

		String sinceDateText = this._sinceDateSpinnerEditor
				.getTextField().getText();
		if (sinceDateText == null) {
			return null;
		}
		try {
			return DateUtils
					.parseDate(sinceDateText);
		} catch (ParseException e) {
			return null;
		}
	}

	@Override
	public void setSinceDateValue(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sinceDateSpinnerModel
					.setValue(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void enableSinceDateValue(boolean value) {
		this._sinceDateSpinner.setEnabled(value);
	}

	@Override
	public void enableOK(boolean value) {
		this._okButton.setEnabled(value);
	}

	private void valuesChanged() {
		getController().valuesChanged();
	}

	private void cancel() {
		return;
	}

	private void ok() {
		getController().display();
	}

}
