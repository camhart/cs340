package gui.reports.expired;

import gui.common.ButtonBankListener;
import gui.common.ButtonBankPanel;
import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.FileFormat;
import gui.common.GridBagConstraintsExt;
import gui.main.GUI;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ExpiredReportView extends DialogView
		implements IExpiredReportView {

	private JPanel _valuesPanel;
	private JLabel _formatLabel;
	private JComboBox _formatBox;
	private ButtonBankPanel _buttonsPanel;
	protected JButton _okButton;

	public ExpiredReportView(GUI parent,
			DialogBox dialog) {
		super(parent, dialog);

		construct();

		this._controller = new ExpiredReportController(
				this);
	}

	@Override
	public IExpiredReportController getController() {
		return (IExpiredReportController) super
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
								ExpiredReportView.this._dialog
										.dispose();
								break;
							case 1:
								cancel();
								ExpiredReportView.this._dialog
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
		this._valuesPanel
				.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 0, 1, 1);
		this._valuesPanel.add(
				Box.createHorizontalStrut(20), c);

		c.place(1, 0, 1, 1);
		this._valuesPanel.add(this._formatLabel,
				c);

		c.place(2, 0, 3, 1);
		this._valuesPanel.add(this._formatBox, c);

		c.place(5, 0, 1, 1);
		this._valuesPanel.add(
				Box.createHorizontalStrut(20), c);
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
