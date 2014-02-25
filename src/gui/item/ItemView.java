package gui.item;

import gui.common.ButtonBankListener;
import gui.common.ButtonBankPanel;
import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.GridBagConstraintsExt;
import gui.main.GUI;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.util.DateUtils;

@SuppressWarnings("serial")
public abstract class ItemView extends DialogView {

	private JPanel _valuesPanel;
	private JLabel _descriptionLabel;
	private JTextField _descriptionField;
	private JLabel _barcodeLabel;
	private JTextField _barcodeField;
	private JLabel _entryDateLabel;
	private SpinnerModel _entryDateSpinnerModel;
	private JSpinner.DateEditor _entryDateSpinnerEditor;
	private JSpinner _entryDateSpinner;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public ItemView(GUI parent, DialogBox dialog) {
		super(parent, dialog);
	}

	@Override
	protected void createComponents() {
		createValuesPanel();
		createButtonsPanel();
	}

	private void createValuesPanel() {
		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				valuesChanged();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
		};

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils
				.earliestDate();

		this._valuesPanel = new JPanel();

		this._descriptionLabel = new JLabel(
				"Description:");

		this._descriptionField = new JTextField(
				30);
		this._descriptionField
				.addKeyListener(keyListener);

		this._barcodeLabel = new JLabel(
				"Item Barcode:");

		this._barcodeField = new JTextField(18);
		this._barcodeField
				.addKeyListener(keyListener);

		this._entryDateLabel = new JLabel(
				"Entry Date:");

		this._entryDateSpinnerModel = new SpinnerDateModel(
				initDate, earliestDate,
				latestDate, Calendar.YEAR);
		this._entryDateSpinner = new JSpinner(
				this._entryDateSpinnerModel);
		this._entryDateSpinnerEditor = new JSpinner.DateEditor(
				this._entryDateSpinner,
				DateUtils.DATE_FORMAT);
		this._entryDateSpinner
				.setEditor(this._entryDateSpinnerEditor);
		this._entryDateSpinnerEditor
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
								if (ItemView.this._entryDateSpinnerEditor
										.getTextField()
										.hasFocus()) {
									valuesChanged();
								}
							}
						});
		// _entryDateSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent arg0) {
		// if (eventsAreDisabled()) {
		// return;
		// }
		// valuesChanged();
		// }
		// });
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
								ItemView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								ItemView.this._dialog
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

	protected abstract void valuesChanged();

	protected abstract void ok();

	protected abstract void cancel();

	@Override
	protected void layoutComponents() {
		layoutValuesPanel();

		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		add(this._valuesPanel);
		add(Box.createHorizontalStrut(5));
		add(this._buttonsPanel);
	}

	private void layoutValuesPanel() {
		this._valuesPanel
				.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 0, 1, 1);
		this._valuesPanel.add(
				this._descriptionLabel, c);

		c.place(1, 0, 3, 1);
		this._valuesPanel.add(
				this._descriptionField, c);

		c.place(0, 1, 1, 1);
		this._valuesPanel.add(this._barcodeLabel,
				c);

		c.place(1, 1, 1, 1);
		this._valuesPanel.add(this._barcodeField,
				c);

		c.place(0, 2, 1, 1);
		this._valuesPanel.add(
				this._entryDateLabel, c);

		c.place(1, 2, 1, 1);
		this._valuesPanel.add(
				this._entryDateSpinner, c);
	}

	public String getDescription() {
		return this._descriptionField.getText();
	}

	public void setDescription(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._descriptionField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableDescription(boolean value) {
		this._descriptionField.setEnabled(value);
	}

	public String getBarcode() {
		return this._barcodeField.getText();
	}

	public void setBarcode(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._barcodeField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableBarcode(boolean value) {
		this._barcodeField.setEnabled(value);
	}

	public Date getEntryDate() {

		// return (Date)_entryDateSpinnerModel.getValue();

		String entryDateText = this._entryDateSpinnerEditor
				.getTextField().getText();
		if (entryDateText == null) {
			return null;
		}
		try {
			return DateUtils
					.parseDate(entryDateText);
		} catch (ParseException e) {
			return null;
		}
	}

	public void setEntryDate(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			this._entryDateSpinnerModel
					.setValue(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableEntryDate(boolean value) {
		this._entryDateSpinner.setEnabled(value);
	}

	public void enableOK(boolean value) {
		this._okButton.setEnabled(value);
	}

}
