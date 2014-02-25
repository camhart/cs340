package gui.product;

import gui.common.ButtonBankListener;
import gui.common.ButtonBankPanel;
import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.GridBagConstraintsExt;
import gui.common.SizeUnits;
import gui.main.GUI;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class ProductView extends
		DialogView {

	private JPanel _valuesPanel;
	private JLabel _barcodeLabel;
	private JTextField _barcodeField;
	private JLabel _descriptionLabel;
	private JTextField _descriptionField;
	private JLabel _sizeLabel;
	private JTextField _sizeField;
	private JComboBox _sizeBox;
	private JLabel _supplyLabel;
	private JTextField _supplyField;
	private JLabel _supplyCountLabel;
	private JLabel _shelfLifeLabel;
	private JTextField _shelfLifeField;
	private JLabel _shelfLifeMonthsLabel;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public ProductView(GUI parent,
			DialogBox dialog) {
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

		this._valuesPanel = new JPanel();

		this._barcodeLabel = new JLabel(
				"Product Barcode:");

		this._barcodeField = new JTextField(15);
		this._barcodeField
				.addKeyListener(keyListener);

		this._descriptionLabel = new JLabel(
				"Description:");

		this._descriptionField = new JTextField(
				30);
		this._descriptionField
				.addKeyListener(keyListener);

		this._sizeLabel = new JLabel("Size:");

		this._sizeField = new JTextField(8);
		this._sizeField
				.addKeyListener(keyListener);

		this._sizeBox = new JComboBox();
		this._sizeBox.addItem(SizeUnits.Count);
		this._sizeBox.addItem(SizeUnits.Pounds);
		this._sizeBox.addItem(SizeUnits.Ounces);
		this._sizeBox.addItem(SizeUnits.Grams);
		this._sizeBox
				.addItem(SizeUnits.Kilograms);
		this._sizeBox.addItem(SizeUnits.Gallons);
		this._sizeBox.addItem(SizeUnits.Quarts);
		this._sizeBox.addItem(SizeUnits.Pints);
		this._sizeBox
				.addItem(SizeUnits.FluidOunces);
		this._sizeBox.addItem(SizeUnits.Liters);
		this._sizeBox
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

		this._supplyLabel = new JLabel(
				"3 Month Supply:");

		this._supplyField = new JTextField(8);
		this._supplyField
				.addKeyListener(keyListener);

		this._supplyCountLabel = new JLabel(
				"count");

		this._shelfLifeLabel = new JLabel(
				"Shelf Life:");

		this._shelfLifeField = new JTextField(8);
		this._shelfLifeField
				.addKeyListener(keyListener);

		this._shelfLifeMonthsLabel = new JLabel(
				"months");
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
								ProductView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								ProductView.this._dialog
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
		this._valuesPanel.add(this._barcodeLabel,
				c);

		c.place(1, 0, 1, 1);
		this._valuesPanel.add(this._barcodeField,
				c);

		c.place(0, 1, 1, 1);
		this._valuesPanel.add(
				this._descriptionLabel, c);

		c.place(1, 1, 3, 1);
		this._valuesPanel.add(
				this._descriptionField, c);

		c.place(0, 2, 1, 1);
		this._valuesPanel.add(this._sizeLabel, c);

		c.place(1, 2, 1, 1);
		this._valuesPanel.add(this._sizeField, c);

		c.place(2, 2, 2, 1);
		this._valuesPanel.add(this._sizeBox, c);

		c.place(0, 3, 1, 1);
		this._valuesPanel.add(
				this._shelfLifeLabel, c);

		c.place(1, 3, 1, 1);
		this._valuesPanel.add(
				this._shelfLifeField, c);

		c.place(2, 3, 1, 1);
		this._valuesPanel.add(
				this._shelfLifeMonthsLabel, c);

		c.place(0, 4, 1, 1);
		this._valuesPanel.add(this._supplyLabel,
				c);

		c.place(1, 4, 1, 1);
		this._valuesPanel.add(this._supplyField,
				c);

		c.place(2, 4, 1, 1);
		this._valuesPanel.add(
				this._supplyCountLabel, c);
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

	public String getSizeValue() {
		return this._sizeField.getText();
	}

	public void setSizeValue(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sizeField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSizeValue(boolean value) {
		this._sizeField.setEnabled(value);
	}

	public SizeUnits getSizeUnit() {
		return (SizeUnits) this._sizeBox
				.getSelectedItem();
	}

	public void setSizeUnit(SizeUnits value) {
		boolean disabledEvents = disableEvents();
		try {
			this._sizeBox.setSelectedItem(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSizeUnit(boolean value) {
		this._sizeBox.setEnabled(value);
	}

	public String getSupply() {
		return this._supplyField.getText();
	}

	public void setSupply(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._supplyField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSupply(boolean value) {
		this._supplyField.setEnabled(value);
	}

	public String getShelfLife() {
		return this._shelfLifeField.getText();
	}

	public void setShelfLife(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._shelfLifeField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableShelfLife(boolean value) {
		this._shelfLifeField.setEnabled(value);
	}

	public void enableOK(boolean value) {
		this._okButton.setEnabled(value);
	}

}
