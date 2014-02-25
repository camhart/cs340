package gui.batches;

import gui.common.DialogBox;
import gui.common.GridBagConstraintsExt;
import gui.inventory.ProductContainerData;
import gui.main.GUI;
import gui.product.AddProductView;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import common.util.DateUtils;

@SuppressWarnings("serial")
public class AddItemBatchView extends
		ItemBatchView implements
		IAddItemBatchView {

	private JLabel entryDateLabel;
	private SpinnerModel entryDateSpinnerModel;
	private JSpinner.DateEditor entryDateSpinnerEditor;
	private JSpinner entryDateSpinner;
	private JLabel countLabel;
	private JTextField countField;

	public AddItemBatchView(GUI parent,
			DialogBox dialog,
			ProductContainerData target) {
		super(parent, dialog);

		construct();

		this._controller = new AddItemBatchController(
				this, target);
	}

	@Override
	protected void createComponents() {
		super.createComponents();

		Date initDate = DateUtils.currentDate();
		Date latestDate = initDate;
		Date earliestDate = DateUtils
				.earliestDate();

		this.entryDateLabel = new JLabel(
				"Entry Date:");

		this.entryDateSpinnerModel = new SpinnerDateModel(
				initDate, earliestDate,
				latestDate, Calendar.YEAR);
		this.entryDateSpinner = new JSpinner(
				this.entryDateSpinnerModel);
		this.entryDateSpinnerEditor = new JSpinner.DateEditor(
				this.entryDateSpinner,
				DateUtils.DATE_FORMAT);
		this.entryDateSpinner
				.setEditor(this.entryDateSpinnerEditor);
		this.entryDateSpinnerEditor
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
								if (AddItemBatchView.this.entryDateSpinnerEditor
										.getTextField()
										.hasFocus()) {
									entryDateChanged();
								}
							}
						});
		// entryDateSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// if (eventsAreDisabled()) {
		// return;
		// }
		// entryDateChanged();
		// }
		// });

		this.countLabel = new JLabel("Count:");

		this.countField = new JTextField(5);
		this.countField
				.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(
							KeyEvent e) {
						return;
					}

					@Override
					public void keyReleased(
							KeyEvent e) {
						if (eventsAreDisabled()) {
							return;
						}
						countChanged();
					}

					// Context
					@Override
					public void keyTyped(
							KeyEvent arg0) {
						return;
					}
				});
	}

	@Override
	protected void layoutComponents() {
		this.batchPanel = new JPanel();
		this.batchPanel
				.setLayout(new GridBagLayout());

		GridBagConstraintsExt c = new GridBagConstraintsExt();
		c.ipadx = 2;
		c.ipady = 2;
		c.insets = new Insets(5, 5, 5, 5);

		c.place(0, 1, 1, 1);
		this.batchPanel.add(this.entryDateLabel,
				c);

		c.place(1, 1, 2, 1);
		this.batchPanel.add(
				this.entryDateSpinner, c);

		c.place(3, 1, 1, 1);
		this.batchPanel.add(this.countLabel, c);

		c.place(4, 1, 1, 1);
		this.batchPanel.add(this.countField, c);

		c.place(0, 0, 1, 1);
		this.batchPanel.add(this.barcodeLabel, c);

		c.place(1, 0, 2, 1);// ounces
		this.batchPanel.add(this.barcodeField, c);

		c.place(3, 0, 2, 1);
		this.batchPanel.add(this.scannerBox, c);

		c.place(1, 2, 1, 1);
		this.batchPanel.add(
				this.itemActionButton, c);

		c.place(2, 2, 1, 1);
		this.batchPanel.add(this.undoButton, c);

		c.place(3, 2, 1, 1);
		this.batchPanel.add(this.redoButton, c);

		c.place(4, 2, 1, 1);
		this.batchPanel.add(this.doneButton, c);

		setMaximumSize(this.batchPanel);

		this.productPanel = new JPanel();
		this.productPanel
				.setLayout(new BoxLayout(
						this.productPanel,
						BoxLayout.Y_AXIS));

		this.productPanel.add(Box
				.createRigidArea(new Dimension(
						10, 10)));
		this.productPanel.add(this.batchPanel);
		this.productPanel.add(Box
				.createRigidArea(new Dimension(
						10, 10)));
		this.productPanel
				.add(this.productTableScrollPane);

		this.splitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				this.productPanel,
				this.itemTableScrollPane);

		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		this.add(this.splitPane);
	}

	private void entryDateChanged() {
		getController().entryDateChanged();
	}

	private void countChanged() {
		getController().countChanged();
	}

	// //////////////////////////
	// ItemBatchView Overrides
	// //////////////////////////

	@Override
	protected String getBarcodeLabel() {
		return "Product Barcode:";
	}

	@Override
	public IAddItemBatchController getController() {
		return (IAddItemBatchController) super
				.getController();
	}

	@Override
	protected void done() {
		getController().done();
	}

	@Override
	protected void itemAction() {
		getController().addItem();
	}

	@Override
	protected String getItemActionName() {
		return "Add Item";
	}

	@Override
	protected void barcodeChanged() {
		getController().barcodeChanged();
	}

	@Override
	protected void useScannerChanged() {
		getController().useScannerChanged();
	}

	@Override
	protected void selectedProductChanged() {
		getController().selectedProductChanged();
	}

	@Override
	protected void selectedItemChanged() {
		return;
	}

	@Override
	protected void redo() {
		getController().redo();
	}

	@Override
	protected void undo() {
		getController().undo();
	}

	// //////////////////////////
	// IAddItemsView overrides
	// //////////////////////////

	@Override
	public void displayAddProductView() {
		DialogBox dialogBox = new DialogBox(
				this._parent, "Add Product");
		AddProductView dialogView = new AddProductView(
				this._parent, dialogBox,
				getBarcode());
		dialogBox.display(dialogView, false);
	}

	@Override
	public String getCount() {
		return this.countField.getText();
	}

	@Override
	public Date getEntryDate() {

		// return DateUtils.removeTimeFromDate((Date) entryDateSpinnerModel
		// .getValue());

		String entryDateText = this.entryDateSpinnerEditor
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

	@Override
	public void setCount(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this.countField.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setEntryDate(Date value) {
		boolean disabledEvents = disableEvents();
		try {
			this.entryDateSpinnerModel
					.setValue(DateUtils
							.removeTimeFromDate(value));
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

}
