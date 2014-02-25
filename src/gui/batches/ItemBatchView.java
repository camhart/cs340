package gui.batches;

import gui.common.DialogBox;
import gui.common.DialogView;
import gui.common.GridBagConstraintsExt;
import gui.common.TableOperations;
import gui.common.Tagable;
import gui.item.ItemData;
import gui.main.GUI;
import gui.product.ProductData;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import common.util.DateUtils;

public abstract class ItemBatchView extends
		DialogView {

	// --------------------------
	// Item Batch members
	// --------------------------
	protected JPanel batchPanel;
	protected JLabel barcodeLabel;
	protected JTextField barcodeField;
	protected JCheckBox scannerBox;
	protected JButton itemActionButton;
	protected JButton doneButton;
	protected JButton undoButton;
	protected JButton redoButton;

	// --------------------------
	// Product Table members
	// --------------------------
	protected JTable productTable;
	protected DefaultTableModel productTableModel;
	protected DefaultTableColumnModel productTableColumnModel;
	protected JTableHeader productTableHeader;
	protected JScrollPane productTableScrollPane;

	// --------------------------
	// Item Table members
	// --------------------------
	protected JTable itemTable;
	protected DefaultTableModel itemTableModel;
	protected DefaultTableColumnModel itemTableColumnModel;
	protected JTableHeader itemTableHeader;
	protected JScrollPane itemTableScrollPane;

	// --------------------------
	// Other members
	// --------------------------
	protected JPanel productPanel;
	protected JSplitPane splitPane;

	// --------------------------
	// Abstract method interface
	// --------------------------
	protected abstract String getBarcodeLabel();

	protected abstract String getItemActionName();

	protected abstract void itemAction();

	protected abstract void done();

	protected abstract void undo();

	protected abstract void redo();

	protected abstract void barcodeChanged();

	protected abstract void useScannerChanged();

	protected abstract void selectedProductChanged();

	protected abstract void selectedItemChanged();

	public ItemBatchView(GUI parent,
			final DialogBox dialog) {
		super(parent, dialog);

		dialog.setResizable(true);

		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(
					WindowEvent evt) {
				done();
				ItemBatchView.this._dialog
						.dispose();
			}
		});
	}

	@Override
	protected void createComponents() {
		createItemBatchPanel();
		createProductTable();
		createItemTable();
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

		c.place(0, 0, 1, 1);
		this.batchPanel.add(this.barcodeLabel, c);

		c.place(1, 0, 2, 1);
		this.batchPanel.add(this.barcodeField, c);

		c.place(3, 0, 2, 1);
		this.batchPanel.add(this.scannerBox, c);

		c.place(1, 1, 1, 1);
		this.batchPanel.add(
				this.itemActionButton, c);

		c.place(2, 1, 1, 1);
		this.batchPanel.add(this.undoButton, c);

		c.place(3, 1, 1, 1);
		this.batchPanel.add(this.redoButton, c);

		c.place(4, 1, 1, 1);
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

	protected void setMaximumSize(JComponent c) {
		Dimension preferred = c
				.getPreferredSize();
		Dimension maximum = new Dimension(
				Integer.MAX_VALUE,
				(int) preferred.getHeight());
		c.setMaximumSize(maximum);
	}

	private void createItemBatchPanel() {
		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				return;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				barcodeChanged();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				return;
			}
		};

		this.barcodeLabel = new JLabel(
				getBarcodeLabel());
		Font font = createFont(
				this.barcodeLabel.getFont(),
				ContentFontSize);
		this.barcodeLabel.setFont(font);

		this.barcodeField = new JTextField(15);
		this.barcodeField.setFont(font);
		this.barcodeField
				.addKeyListener(keyListener);

		this.scannerBox = new JCheckBox(
				"Use barcode scanner");
		this.scannerBox
				.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(
							ChangeEvent arg0) {
						if (eventsAreDisabled()) {
							return;
						}
						useScannerChanged();
					}
				});

		this.itemActionButton = new JButton(
				getItemActionName());
		this.itemActionButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(
							ActionEvent arg) {
						itemAction();
					}
				});

		this.doneButton = new JButton("Done");
		this.doneButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(
							ActionEvent arg) {
						done();
					}
				});

		this.undoButton = new JButton("Undo");
		this.undoButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(
							ActionEvent arg) {
						undo();
					}
				});

		this.redoButton = new JButton("Redo");
		this.redoButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(
							ActionEvent arg) {
						redo();
					}
				});
	}

	@SuppressWarnings("serial")
	private void createProductTable() {

		MouseAdapter mouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
			}

			private void handleMouseEvent(
					MouseEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				if (e.getSource() == ItemBatchView.this.productTableHeader) {
					// if (e.getButton() == MouseEvent.BUTTON1 &&
					// e.getID() == MouseEvent.MOUSE_PRESSED) {
					// int clickedColumnIndex =
					// commentsColumnModel.getColumnIndexAtX(e.getX());
					// if (clickedColumnIndex >= 0) {
					// updateCommentSortOrder(clickedColumnIndex);
					// }
					// }
				}
			}
		};

		this.productTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0,
				"Description", ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);
		column = createTableColumn(1, "Size",
				ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);
		column = createTableColumn(2, "Count",
				ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);
		column = createTableColumn(3,
				"Shelf Life", ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);
		column = createTableColumn(4,
				"3-Month Supply", ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);
		column = createTableColumn(5,
				"Product Barcode",
				ContentFontSize);
		this.productTableColumnModel
				.addColumn(column);

		this.productTableModel = new DefaultTableModel(
				0, 6) {
			@Override
			public boolean isCellEditable(
					int row, int column) {
				return false;
			}
		};

		this.productTable = new JTable(
				this.productTableModel,
				this.productTableColumnModel);
		this.productTable.setFont(createFont(
				this.productTable.getFont(),
				ContentFontSize));
		this.productTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.productTable
				.getSelectionModel()
				.addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(
									ListSelectionEvent evt) {
								if (eventsAreDisabled()) {
									return;
								}
								if (evt.getValueIsAdjusting()) {
									return;
								}
								selectedProductChanged();
							}
						});
		this.productTable
				.addMouseListener(mouseListener);

		this.productTableHeader = this.productTable
				.getTableHeader();
		this.productTableHeader
				.setReorderingAllowed(false);
		this.productTableHeader
				.addMouseListener(mouseListener);

		this.productTableScrollPane = new JScrollPane(
				this.productTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.productTableScrollPane
				.setPreferredSize(new Dimension(
						600, 300));
		this.productTableScrollPane
				.setBorder(createTitledBorder(
						"Products",
						BorderFontSize));
	}

	@SuppressWarnings("serial")
	private void createItemTable() {

		MouseAdapter mouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				handleMouseEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				handleMouseEvent(e);
			}

			private void handleMouseEvent(
					MouseEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				if (e.getSource() == ItemBatchView.this.itemTableHeader) {
					// if (e.getButton() == MouseEvent.BUTTON1 &&
					// e.getID() == MouseEvent.MOUSE_PRESSED) {
					// int clickedColumnIndex =
					// commentsColumnModel.getColumnIndexAtX(e.getX());
					// if (clickedColumnIndex >= 0) {
					// updateCommentSortOrder(clickedColumnIndex);
					// }
					// }
				}
			}
		};

		this.itemTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0,
				"Entry Date", ContentFontSize);
		this.itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(1,
				"Expiration Date",
				ContentFontSize);
		this.itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(2,
				"Item Barcode", ContentFontSize);
		this.itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(3,
				"Storage Unit", ContentFontSize);
		this.itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(4,
				"Product Group", ContentFontSize);
		this.itemTableColumnModel
				.addColumn(column);

		this.itemTableModel = new DefaultTableModel(
				0, 5) {
			@Override
			public boolean isCellEditable(
					int row, int column) {
				return false;
			}
		};

		this.itemTable = new JTable(
				this.itemTableModel,
				this.itemTableColumnModel);
		this.itemTable.setFont(createFont(
				this.itemTable.getFont(),
				ContentFontSize));
		this.itemTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.itemTable
				.getSelectionModel()
				.addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(
									ListSelectionEvent evt) {
								if (eventsAreDisabled()) {
									return;
								}
								if (evt.getValueIsAdjusting()) {
									return;
								}
								selectedItemChanged();
							}
						});
		this.itemTable
				.addMouseListener(mouseListener);

		this.itemTableHeader = this.itemTable
				.getTableHeader();
		this.itemTableHeader
				.setReorderingAllowed(false);
		this.itemTableHeader
				.addMouseListener(mouseListener);

		this.itemTableScrollPane = new JScrollPane(
				this.itemTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.itemTableScrollPane
				.setPreferredSize(new Dimension(
						600, 300));
		this.itemTableScrollPane
				.setBorder(createTitledBorder(
						"Items", BorderFontSize));
	}

	//
	//
	//

	public void close() {
		this._dialog.dispose();
	}

	public String getBarcode() {
		return this.barcodeField.getText();
	}

	public void setBarcode(String barcode) {
		boolean disabledEvents = disableEvents();
		try {
			this.barcodeField.setText(barcode);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void giveBarcodeFocus() {
		boolean disabledEvents = disableEvents();
		try {
			this.barcodeField
					.requestFocusInWindow();
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public boolean getUseScanner() {
		return this.scannerBox.isSelected();
	}

	public void setUseScanner(boolean value) {
		boolean disabledEvents = disableEvents();
		try {
			this.scannerBox.setSelected(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void enableItemAction(boolean value) {
		this.itemActionButton.setEnabled(value);
	}

	public void enableUndo(boolean value) {
		this.undoButton.setEnabled(value);
	}

	public void enableRedo(boolean value) {
		this.redoButton.setEnabled(value);
	}

	public ProductData getSelectedProduct() {
		int selectedIndex = this.productTable
				.getSelectedRow();
		if (selectedIndex >= 0) {
			ProductFormatter formatter = (ProductFormatter) this.productTableModel
					.getValueAt(selectedIndex, 0);
			return (ProductData) formatter
					.getTag();
		}
		return null;
	}

	public void selectProduct(ProductData product) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < this.productTableModel
					.getRowCount(); ++i) {
				ProductFormatter formatter = (ProductFormatter) this.productTableModel
						.getValueAt(i, 0);
				ProductData pd = (ProductData) formatter
						.getTag();
				// if (pd == product) {
				if ((pd == product)
						|| pd.getBarcode()
								.equals(product
										.getBarcode())) {
					TableOperations
							.selectTableRow(
									this.productTable,
									i);
					return;
				}
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void setProducts(ProductData[] products) {
		boolean disabledEvents = disableEvents();
		try {
			this.productTableModel.setRowCount(0);
			for (ProductData pd : products) {
				ProductFormatter[] row = new ProductFormatter[6];
				row[0] = new ProductFormatter(0);
				row[0].setTag(pd);
				row[1] = new ProductFormatter(1);
				row[1].setTag(pd);
				row[2] = new ProductFormatter(2);
				row[2].setTag(pd);
				row[3] = new ProductFormatter(3);
				row[3].setTag(pd);
				row[4] = new ProductFormatter(4);
				row[4].setTag(pd);
				row[5] = new ProductFormatter(5);
				row[5].setTag(pd);
				this.productTableModel
						.addRow(row);
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public ItemData getSelectedItem() {
		int selectedIndex = this.itemTable
				.getSelectedRow();
		if (selectedIndex >= 0) {
			ItemFormatter formatter = (ItemFormatter) this.itemTableModel
					.getValueAt(selectedIndex, 0);
			return (ItemData) formatter.getTag();
		}
		return null;
	}

	public void selectItem(ItemData item) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < this.itemTableModel
					.getRowCount(); ++i) {
				ItemFormatter formatter = (ItemFormatter) this.itemTableModel
						.getValueAt(i, 0);
				ItemData id = (ItemData) formatter
						.getTag();
				if (id == item) {
					TableOperations
							.selectTableRow(
									this.itemTable,
									i);
					return;
				}
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	public void setItems(ItemData[] items) {
		boolean disabledEvents = disableEvents();
		try {
			this.itemTableModel.setRowCount(0);
			for (ItemData id : items) {
				ItemFormatter[] row = new ItemFormatter[5];
				row[0] = new ItemFormatter(0);
				row[0].setTag(id);
				row[1] = new ItemFormatter(1);
				row[1].setTag(id);
				row[2] = new ItemFormatter(2);
				row[2].setTag(id);
				row[3] = new ItemFormatter(3);
				row[3].setTag(id);
				row[4] = new ItemFormatter(4);
				row[4].setTag(id);
				this.itemTableModel.addRow(row);
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	//
	//
	//

	private class ProductFormatter extends
			Tagable {
		private int column;

		public ProductFormatter(int column) {
			this.column = column;
		}

		@Override
		public String toString() {
			ProductData data = (ProductData) getTag();
			if (data != null) {
				switch (this.column) {
					case 0:
						return data
								.getDescription();
					case 1:
						return data.getSize();
					case 2:
						return data.getCount();
					case 3:
						return data
								.getShelfLife();
					case 4:
						return data.getSupply();
					case 5:
						return data.getBarcode();
					default:
						assert false;
				}
			}
			return "";
		}
	}

	private class ItemFormatter extends Tagable {
		private int column;

		public ItemFormatter(int column) {
			this.column = column;
		}

		@Override
		public String toString() {
			ItemData data = (ItemData) getTag();
			if (data != null) {
				switch (this.column) {
					case 0:
						return DateUtils
								.formatDate(data
										.getEntryDate());
					case 1:
						return (data
								.getExpirationDate() != null ? DateUtils
								.formatDate(data
										.getExpirationDate())
								: "");
					case 2:
						return data.getBarcode();
					case 3:
						return data
								.getStorageUnit();
					case 4:
						return data
								.getProductGroup();
					default:
						assert false;
				}
			}
			return "";
		}
	}

}
