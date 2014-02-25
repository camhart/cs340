package gui.storageunit;

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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class StorageUnitView extends
		DialogView {

	private JPanel _addPanel;
	private JLabel _nameLabel;
	private JTextField _nameField;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public StorageUnitView(GUI parent,
			DialogBox dialog) {
		super(parent, dialog);
	}

	@Override
	protected void createComponents() {
		createAddPanel();
		createButtonsPanel();
	}

	private void createAddPanel() {
		this._addPanel = new JPanel();

		this._nameLabel = new JLabel(
				"Storage Unit Name:");

		this._nameField = new JTextField(20);
		this._nameField
				.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(
							KeyEvent e) {
						return;
					}

					@Override
					public void keyReleased(
							KeyEvent e) {
						valuesChanged();
					}

					@Override
					public void keyTyped(
							KeyEvent arg0) {
						return;
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
								StorageUnitView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								StorageUnitView.this._dialog
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
	}

	public String getStorageUnitName() {
		return this._nameField.getText();
	}

	public void setStorageUnitName(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._nameField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableStorageUnitName(
			boolean value) {
		this._nameField.setEnabled(value);
	}

	public void enableOK(boolean value) {
		this._okButton.setEnabled(value);
	}

}
