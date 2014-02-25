package gui.productgroup;

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
public abstract class ProductGroupView extends
		DialogView {

	private JPanel _addPanel;
	private JLabel _nameLabel;
	private JTextField _nameField;
	private JLabel _supplyLabel;
	private JTextField _supplyField;
	private JComboBox _supplyBox;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public ProductGroupView(GUI parent,
			DialogBox dialog) {
		super(parent, dialog);
	}

	@Override
	protected void createComponents() {
		createAddPanel();
		createButtonsPanel();
	}

	private void createAddPanel() {
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

		this._addPanel = new JPanel();

		this._nameLabel = new JLabel(
				"Product Group Name:");

		this._nameField = new JTextField(20);
		this._nameField
				.addKeyListener(keyListener);

		this._supplyLabel = new JLabel(
				"3 Month Supply:");

		this._supplyField = new JTextField(8);
		this._supplyField
				.addKeyListener(keyListener);

		this._supplyBox = new JComboBox();
		this._supplyBox.addItem(SizeUnits.Count);
		this._supplyBox.addItem(SizeUnits.Pounds);
		this._supplyBox.addItem(SizeUnits.Ounces);
		this._supplyBox.addItem(SizeUnits.Grams);
		this._supplyBox
				.addItem(SizeUnits.Kilograms);
		this._supplyBox
				.addItem(SizeUnits.Gallons);
		this._supplyBox.addItem(SizeUnits.Quarts);
		this._supplyBox.addItem(SizeUnits.Pints);
		this._supplyBox
				.addItem(SizeUnits.FluidOunces);
		this._supplyBox.addItem(SizeUnits.Liters);
		this._supplyBox
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
								ProductGroupView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								ProductGroupView.this._dialog
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
		layoutAddPanel();

		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		add(this._addPanel);
		add(Box.createHorizontalStrut(5));
		add(this._buttonsPanel);
	}

	private void layoutAddPanel() {
		this._addPanel
				.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 0, 1, 1);
		this._addPanel.add(this._nameLabel, c);

		c.place(1, 0, 3, 1);
		this._addPanel.add(this._nameField, c);

		c.place(0, 1, 1, 1);
		this._addPanel.add(this._supplyLabel, c);

		c.place(1, 1, 1, 1);
		this._addPanel.add(this._supplyField, c);

		c.place(2, 1, 2, 1);
		this._addPanel.add(this._supplyBox, c);
	}

	public String getProductGroupName() {
		return this._nameField.getText();
	}

	public void setProductGroupName(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._nameField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableProductGroupName(
			boolean value) {
		this._nameField.setEnabled(value);
	}

	public String getSupplyValue() {
		return this._supplyField.getText();
	}

	public void setSupplyValue(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._supplyField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSupplyValue(boolean value) {
		this._supplyField.setEnabled(value);
	}

	public SizeUnits getSupplyUnit() {
		return (SizeUnits) this._supplyBox
				.getSelectedItem();
	}

	public void setSupplyUnit(SizeUnits value) {
		boolean disabledEvents = disableEvents();
		try {
			this._supplyBox
					.setSelectedItem(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableSupplyUnit(boolean value) {
		this._supplyBox.setEnabled(value);
	}

	public void enableOK(boolean value) {
		this._okButton.setEnabled(value);
	}

}
