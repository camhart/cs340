package gui.inventory;

import gui.batches.AddItemBatchView;
import gui.batches.RemoveItemBatchView;
import gui.batches.TransferItemBatchView;
import gui.common.DialogBox;
import gui.common.TableOperations;
import gui.common.Tagable;
import gui.common.TreeOperations;
import gui.common.View;
import gui.item.EditItemView;
import gui.item.ItemData;
import gui.main.GUI;
import gui.product.EditProductView;
import gui.product.ProductData;
import gui.productgroup.AddProductGroupView;
import gui.productgroup.EditProductGroupView;
import gui.storageunit.AddStorageUnitView;
import gui.storageunit.EditStorageUnitView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import common.util.DateUtils;
import controller.notify.ProductContainerDataSorter;

@SuppressWarnings("serial")
public class InventoryView extends View implements
		IInventoryView {

	// -------------------------------
	// Product Container Tree members
	// -------------------------------
	private DefaultTreeModel _productContainerTreeModel;
	private JTree _productContainerTree;
	private JScrollPane _productContainerTreeScrollPane;
	private JPopupMenu _allStorageUnitsMenu;
	private JMenuItem _removeItemsMenuItem;
	private JMenuItem _addStorageUnitMenuItem;
	private JPopupMenu _storageUnitMenu;
	private JMenuItem _addItemsMenuItem;
	private JMenuItem _transferItemsMenuItem;
	private JMenuItem _addProductGroupMenuItem;
	private JMenuItem _editStorageUnitMenuItem;
	private JMenuItem _deleteStorageUnitMenuItem;
	private JPopupMenu _productGroupMenu;
	private JMenuItem _addProductSubgroupMenuItem;
	private JMenuItem _editProductGroupMenuItem;
	private JMenuItem _deleteProductGroupMenuItem;

	// --------------------------
	// Context Info members
	// --------------------------
	private JPanel _contextInfoPanel;
	private JLabel _contextInfoUnitLabel;
	private JTextField _contextInfoUnitField;
	private JLabel _contextInfoGroupLabel;
	private JTextField _contextInfoGroupField;
	private JLabel _contextInfoSupplyLabel;
	private JTextField _contextInfoSupplyField;

	// --------------------------
	// Product Table members
	// --------------------------
	private JTable _productTable;
	private DefaultTableModel _productTableModel;
	private DefaultTableColumnModel _productTableColumnModel;
	private JTableHeader _productTableHeader;
	private JScrollPane _productTableScrollPane;
	private JPopupMenu _productMenu;
	private JMenuItem _editProductMenuItem;
	private JMenuItem _deleteProductMenuItem;

	// --------------------------
	// Item Table members
	// --------------------------
	private JTable _itemTable;
	private DefaultTableModel _itemTableModel;
	private DefaultTableColumnModel _itemTableColumnModel;
	private JTableHeader _itemTableHeader;
	private JScrollPane _itemTableScrollPane;
	private JPopupMenu _itemMenu;
	private JMenuItem _editItemMenuItem;
	private JMenuItem _removeItemMenuItem;

	// --------------------------
	// Other members
	// --------------------------
	private JPanel _productPanel;
	private JSplitPane _midSplitPane;
	private JSplitPane _rightSplitPane;

	private HashMap<ProductContainerData, ProductContainerTreeNode> _productContainers;

	public InventoryView(GUI parent) {
		super(parent);

		construct();

		this._controller = new InventoryController(
				this);
	}

	@Override
	public IInventoryController getController() {
		return (IInventoryController) super
				.getController();
	}

	@Override
	protected void createComponents() {
		createProductContainerTree();
		createContextInfoPanel();
		createProductTable();
		createItemTable();
	}

	@Override
	protected void layoutComponents() {

		final int LabelSpacing = 2;
		final int FieldSpacing = 5;

		this._contextInfoPanel = new JPanel();
		this._contextInfoPanel
				.setLayout(new BoxLayout(
						this._contextInfoPanel,
						BoxLayout.X_AXIS));
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						FieldSpacing,
						FieldSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoUnitLabel);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						LabelSpacing,
						LabelSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoUnitField);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						FieldSpacing,
						FieldSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoGroupLabel);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						LabelSpacing,
						LabelSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoGroupField);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						FieldSpacing,
						FieldSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoSupplyLabel);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						LabelSpacing,
						LabelSpacing)));
		this._contextInfoPanel
				.add(this._contextInfoSupplyField);
		this._contextInfoPanel.add(Box
				.createRigidArea(new Dimension(
						FieldSpacing,
						FieldSpacing)));

		this._productPanel = new JPanel();
		this._productPanel
				.setLayout(new BoxLayout(
						this._productPanel,
						BoxLayout.Y_AXIS));
		this._productPanel
				.setPreferredSize(new Dimension(
						600, 300));

		this._productPanel.add(Box
				.createRigidArea(new Dimension(
						10, 10)));
		this._productPanel
				.add(this._contextInfoPanel);
		this._productPanel.add(Box
				.createRigidArea(new Dimension(
						10, 10)));
		this._productPanel
				.add(this._productTableScrollPane);

		this._rightSplitPane = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT,
				this._productPanel,
				this._itemTableScrollPane);

		this._midSplitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				this._productContainerTreeScrollPane,
				this._rightSplitPane);

		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		this.add(this._midSplitPane);
	}

	private void createProductContainerTree() {

		TreeSelectionListener treeSelectionListener = createProductContainerTreeSelectionListener();

		MouseListener mouseListener = createProductContainerTreeMouseListener();

		this._productContainerTree = new JTree();
		this._productContainerTree
				.setCellRenderer(new ProductContainerTreeCellRenderer());
		this._productContainerTree
				.setFont(createFont(
						this._productContainerTree
								.getFont(),
						ContentFontSize));
		this._productContainerTree
				.setRootVisible(true);
		this._productContainerTree
				.setExpandsSelectedPaths(true);
		this._productContainerTree
				.getSelectionModel()
				.setSelectionMode(
						TreeSelectionModel.SINGLE_TREE_SELECTION);
		this._productContainerTree
				.setDropMode(DropMode.ON);
		this._productContainerTree
				.setTransferHandler(new ProductContainerTransferHandler());

		this._productContainerTreeScrollPane = new JScrollPane(
				this._productContainerTree,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this._productContainerTreeScrollPane
				.setPreferredSize(new Dimension(
						250, 175));
		this._productContainerTreeScrollPane
				.setBorder(createTitledBorder("",
						BorderFontSize));
		this._productContainerTree
				.addTreeSelectionListener(treeSelectionListener);
		this._productContainerTree
				.addMouseListener(mouseListener);

		ActionListener actionListener = createProductContainerTreeActionListener();

		this._allStorageUnitsMenu = new JPopupMenu();
		this._removeItemsMenuItem = new JMenuItem(
				"Remove Items...");
		this._removeItemsMenuItem
				.addActionListener(actionListener);
		this._allStorageUnitsMenu
				.add(this._removeItemsMenuItem);
		this._allStorageUnitsMenu.addSeparator();
		this._addStorageUnitMenuItem = new JMenuItem(
				"Add Storage Unit...");
		this._addStorageUnitMenuItem
				.addActionListener(actionListener);
		this._allStorageUnitsMenu
				.add(this._addStorageUnitMenuItem);

		this._storageUnitMenu = new JPopupMenu();
		this._addItemsMenuItem = new JMenuItem(
				"Add Items...");
		this._addItemsMenuItem
				.addActionListener(actionListener);
		this._storageUnitMenu
				.add(this._addItemsMenuItem);
		this._transferItemsMenuItem = new JMenuItem(
				"Transfer Items...");
		this._transferItemsMenuItem
				.addActionListener(actionListener);
		this._storageUnitMenu
				.add(this._transferItemsMenuItem);
		this._storageUnitMenu.addSeparator();
		this._addProductGroupMenuItem = new JMenuItem(
				"Add Product Group...");
		this._addProductGroupMenuItem
				.addActionListener(actionListener);
		this._storageUnitMenu
				.add(this._addProductGroupMenuItem);
		this._storageUnitMenu.addSeparator();
		this._editStorageUnitMenuItem = new JMenuItem(
				"Edit Storage Unit...");
		this._editStorageUnitMenuItem
				.addActionListener(actionListener);
		this._storageUnitMenu
				.add(this._editStorageUnitMenuItem);
		this._deleteStorageUnitMenuItem = new JMenuItem(
				"Delete Storage Unit");
		this._deleteStorageUnitMenuItem
				.addActionListener(actionListener);
		this._storageUnitMenu
				.add(this._deleteStorageUnitMenuItem);

		this._productGroupMenu = new JPopupMenu();
		this._addProductSubgroupMenuItem = new JMenuItem(
				"Add Product Group...");
		this._addProductSubgroupMenuItem
				.addActionListener(actionListener);
		this._productGroupMenu
				.add(this._addProductSubgroupMenuItem);
		this._productGroupMenu.addSeparator();
		this._editProductGroupMenuItem = new JMenuItem(
				"Edit Product Group...");
		this._editProductGroupMenuItem
				.addActionListener(actionListener);
		this._productGroupMenu
				.add(this._editProductGroupMenuItem);
		this._deleteProductGroupMenuItem = new JMenuItem(
				"Delete Product Group");
		this._deleteProductGroupMenuItem
				.addActionListener(actionListener);
		this._productGroupMenu
				.add(this._deleteProductGroupMenuItem);
	}

	private TreeSelectionListener createProductContainerTreeSelectionListener() {
		return new TreeSelectionListener() {
			@Override
			public void valueChanged(
					TreeSelectionEvent e) {
				if (eventsAreDisabled()) {
					return;
				}
				getController()
						.productContainerSelectionChanged();
			}
		};
	}

	private MouseListener createProductContainerTreeMouseListener() {
		return new MouseAdapter() {

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
				ProductContainerTreeNode node = (ProductContainerTreeNode) TreeOperations
						.getSelectedTreeNode(InventoryView.this._productContainerTree);
				if (node != null) {
					if (node.isAllStorageUnits()) {
						if (e.isPopupTrigger()) {
							InventoryView.this._productContainerTree
									.requestFocus();
							enableAllStorageUnitsMenuItems();
							InventoryView.this._allStorageUnitsMenu
									.show(e.getComponent(),
											e.getX(),
											e.getY());
						}
					} else if (node
							.isStorageUnit()) {
						if (e.isPopupTrigger()) {
							InventoryView.this._productContainerTree
									.requestFocus();
							enableStorageUnitMenuItems();
							InventoryView.this._storageUnitMenu
									.show(e.getComponent(),
											e.getX(),
											e.getY());
						}
					} else if (node
							.isProductGroup()) {
						if (e.isPopupTrigger()) {
							InventoryView.this._productContainerTree
									.requestFocus();
							enableProductGroupMenuItems();
							InventoryView.this._productGroupMenu
									.show(e.getComponent(),
											e.getX(),
											e.getY());
						}
					}
				}
			}
		};
	}

	private ActionListener createProductContainerTreeActionListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(
					ActionEvent evt) {
				if (evt.getSource() == InventoryView.this._addStorageUnitMenuItem) {
					addStorageUnit();
				} else if (evt.getSource() == InventoryView.this._removeItemsMenuItem) {
					removeItems();
				} else if (evt.getSource() == InventoryView.this._addItemsMenuItem) {
					addItems();
				} else if (evt.getSource() == InventoryView.this._transferItemsMenuItem) {
					transferItems();
				} else if (evt.getSource() == InventoryView.this._addProductGroupMenuItem) {
					addProductGroup();
				} else if (evt.getSource() == InventoryView.this._editStorageUnitMenuItem) {
					editStorageUnit();
				} else if (evt.getSource() == InventoryView.this._deleteStorageUnitMenuItem) {
					deleteStorageUnit();
				} else if (evt.getSource() == InventoryView.this._addProductSubgroupMenuItem) {
					addProductGroup();
				} else if (evt.getSource() == InventoryView.this._editProductGroupMenuItem) {
					editProductGroup();
				} else if (evt.getSource() == InventoryView.this._deleteProductGroupMenuItem) {
					deleteProductGroup();
				}
			}
		};
	}

	private void setMaximumSize(JComponent c) {
		Dimension preferred = c
				.getPreferredSize();
		Dimension maximum = new Dimension(
				Integer.MAX_VALUE,
				(int) preferred.getHeight());
		c.setMaximumSize(maximum);
	}

	private void createContextInfoPanel() {
		this._contextInfoUnitLabel = new JLabel(
				"UNIT:");
		Font font = createFont(
				this._contextInfoUnitLabel
						.getFont(),
				ContextInfoFontSize);
		this._contextInfoUnitLabel.setFont(font);

		this._contextInfoUnitField = new JTextField(
				15);
		this._contextInfoUnitField.setFont(font);
		this._contextInfoUnitField
				.setEditable(false);
		setMaximumSize(this._contextInfoUnitField);

		this._contextInfoGroupLabel = new JLabel(
				"GROUP:");
		this._contextInfoGroupLabel.setFont(font);

		this._contextInfoGroupField = new JTextField(
				15);
		this._contextInfoGroupField.setFont(font);
		this._contextInfoGroupField
				.setEditable(false);
		setMaximumSize(this._contextInfoGroupField);

		this._contextInfoSupplyLabel = new JLabel(
				"3-MONTH SUPPLY:");
		this._contextInfoSupplyLabel
				.setFont(font);

		this._contextInfoSupplyField = new JTextField(
				15);
		this._contextInfoSupplyField
				.setFont(font);
		this._contextInfoSupplyField
				.setEditable(false);
		setMaximumSize(this._contextInfoSupplyField);
	}

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
				if (e.getSource() == InventoryView.this._productTable) {
					if (e.isPopupTrigger()) {
						enableProductMenuItems();
						InventoryView.this._productMenu
								.show(e.getComponent(),
										e.getX(),
										e.getY());
					}
				} else if (e.getSource() == InventoryView.this._productTableHeader) {
					if (e.isPopupTrigger()) {
						enableProductMenuItems();
						InventoryView.this._productMenu
								.show(e.getComponent(),
										e.getX(),
										e.getY());
					}
					// else if (e.getButton() == MouseEvent.BUTTON1 &&
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

		this._productTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0,
				"Description", ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);
		column = createTableColumn(1, "Size",
				ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);
		column = createTableColumn(2, "Count",
				ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);
		column = createTableColumn(3,
				"Shelf Life", ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);
		column = createTableColumn(4,
				"3-Month Supply", ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);
		column = createTableColumn(5,
				"Product Barcode",
				ContentFontSize);
		this._productTableColumnModel
				.addColumn(column);

		this._productTableModel = new DefaultTableModel(
				0, 6) {
			@Override
			@SuppressWarnings("serial")
			public boolean isCellEditable(
					int row, int column) {
				return false;
			}
		};

		this._productTable = new JTable(
				this._productTableModel,
				this._productTableColumnModel);
		this._productTable.setDragEnabled(true);
		this._productTable
				.setTransferHandler(new ProductTransferHandler());
		this._productTable.setFont(createFont(
				this._productTable.getFont(),
				ContentFontSize));
		this._productTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this._productTable
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
								getController()
										.productSelectionChanged();
							}
						});
		this._productTable
				.addMouseListener(mouseListener);

		this._productTableHeader = this._productTable
				.getTableHeader();
		this._productTableHeader
				.setReorderingAllowed(false);
		this._productTableHeader
				.addMouseListener(mouseListener);
		this._productTableHeader
				.setBackground(Color.blue);

		this._productTableScrollPane = new JScrollPane(
				this._productTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// _productTableScrollPane.setPreferredSize(new Dimension(600, 300));
		this._productTableScrollPane
				.setBorder(createTitledBorder(
						"Products",
						BorderFontSize));

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(
					ActionEvent evt) {
				if (evt.getSource() == InventoryView.this._editProductMenuItem) {
					editProduct();
				} else if (evt.getSource() == InventoryView.this._deleteProductMenuItem) {
					deleteProduct();
				}
			}
		};

		this._productMenu = new JPopupMenu();
		this._editProductMenuItem = new JMenuItem(
				"Edit Product...");
		this._editProductMenuItem
				.addActionListener(actionListener);
		this._productMenu
				.add(this._editProductMenuItem);
		this._deleteProductMenuItem = new JMenuItem(
				"Delete Product");
		this._deleteProductMenuItem
				.addActionListener(actionListener);
		this._productMenu
				.add(this._deleteProductMenuItem);
	}

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
				if (e.getSource() == InventoryView.this._itemTable) {
					if (e.isPopupTrigger()) {
						enableItemMenuItems();
						InventoryView.this._itemMenu
								.show(e.getComponent(),
										e.getX(),
										e.getY());
					}
				} else if (e.getSource() == InventoryView.this._itemTableHeader) {
					if (e.isPopupTrigger()) {
						enableItemMenuItems();
						InventoryView.this._itemMenu
								.show(e.getComponent(),
										e.getX(),
										e.getY());
					}
					// else if (e.getButton() == MouseEvent.BUTTON1 &&
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

		this._itemTableColumnModel = new DefaultTableColumnModel();
		TableColumn column = createTableColumn(0,
				"Entry Date", ContentFontSize);
		this._itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(1,
				"Expiration Date",
				ContentFontSize);
		this._itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(2,
				"Item Barcode", ContentFontSize);
		this._itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(3,
				"Storage Unit", ContentFontSize);
		this._itemTableColumnModel
				.addColumn(column);
		column = createTableColumn(4,
				"Product Group", ContentFontSize);
		this._itemTableColumnModel
				.addColumn(column);

		this._itemTableModel = new DefaultTableModel(
				0, 5) {
			@Override
			public boolean isCellEditable(
					int row, int column) {
				return false;
			}
		};

		this._itemTable = new JTable(
				this._itemTableModel,
				this._itemTableColumnModel);
		this._itemTable.setDragEnabled(true);
		this._itemTable
				.setTransferHandler(new ItemTransferHandler());
		this._itemTable.setFont(createFont(
				this._itemTable.getFont(),
				ContentFontSize));
		this._itemTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this._itemTable
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
								getController()
										.itemSelectionChanged();
							}
						});
		this._itemTable
				.addMouseListener(mouseListener);

		this._itemTableHeader = this._itemTable
				.getTableHeader();
		this._itemTableHeader
				.setReorderingAllowed(false);
		this._itemTableHeader
				.addMouseListener(mouseListener);

		this._itemTableScrollPane = new JScrollPane(
				this._itemTable,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this._itemTableScrollPane
				.setPreferredSize(new Dimension(
						600, 300));
		this._itemTableScrollPane
				.setBorder(createTitledBorder(
						"Items", BorderFontSize));

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(
					ActionEvent evt) {
				if (evt.getSource() == InventoryView.this._editItemMenuItem) {
					editItem();
				} else if (evt.getSource() == InventoryView.this._removeItemMenuItem) {
					removeItem();
				}
			}
		};

		this._itemMenu = new JPopupMenu();
		this._editItemMenuItem = new JMenuItem(
				"Edit Item");
		this._editItemMenuItem
				.addActionListener(actionListener);
		this._itemMenu
				.add(this._editItemMenuItem);
		this._removeItemMenuItem = new JMenuItem(
				"Remove Item");
		this._removeItemMenuItem
				.addActionListener(actionListener);
		this._itemMenu
				.add(this._removeItemMenuItem);
	}

	private void enableAllStorageUnitsMenuItems() {
		this._addStorageUnitMenuItem
				.setEnabled(getController()
						.canAddStorageUnit());
	}

	private void enableStorageUnitMenuItems() {
		this._addItemsMenuItem
				.setEnabled(getController()
						.canAddItems());
		this._addProductGroupMenuItem
				.setEnabled(getController()
						.canAddProductGroup());
		this._editStorageUnitMenuItem
				.setEnabled(getController()
						.canEditStorageUnit());
		this._deleteStorageUnitMenuItem
				.setEnabled(getController()
						.canDeleteStorageUnit());
	}

	private void enableProductGroupMenuItems() {
		this._addProductSubgroupMenuItem
				.setEnabled(getController()
						.canAddProductGroup());
		this._editProductGroupMenuItem
				.setEnabled(getController()
						.canEditProductGroup());
		this._deleteProductGroupMenuItem
				.setEnabled(getController()
						.canDeleteProductGroup());
	}

	private void enableProductMenuItems() {
		this._editProductMenuItem
				.setEnabled(getController()
						.canEditProduct());
		this._deleteProductMenuItem
				.setEnabled(getController()
						.canDeleteProduct());
	}

	private void enableItemMenuItems() {
		this._editItemMenuItem
				.setEnabled(getController()
						.canEditItem());
		this._removeItemMenuItem
				.setEnabled(getController()
						.canRemoveItem());
	}

	// ////////////////
	// Action Methods
	// ////////////////

	private void addStorageUnit() {
		getController().addStorageUnit();
	}

	private void addItems() {
		getController().addItems();
	}

	private void transferItems() {
		getController().transferItems();
	}

	private void removeItems() {
		getController().removeItems();
	}

	private void editStorageUnit() {
		getController().editStorageUnit();
	}

	private void deleteStorageUnit() {
		getController().deleteStorageUnit();
	}

	private void addProductGroup() {
		getController().addProductGroup();
	}

	private void editProductGroup() {
		getController().editProductGroup();
	}

	private void deleteProductGroup() {
		getController().deleteProductGroup();
	}

	private void editProduct() {
		getController().editProduct();
	}

	private void deleteProduct() {
		getController().deleteProduct();
	}

	private void editItem() {
		getController().editItem();
	}

	private void removeItem() {
		getController().removeItem();
	}

	// ////////////////
	// IInventoryView
	// ////////////////

	@Override
	public void setProductContainers(
			ProductContainerData rootData) {
		boolean disabledEvents = disableEvents();
		try {
			this._productContainers = new HashMap<ProductContainerData, ProductContainerTreeNode>();
			DefaultMutableTreeNode rootNode = null;
			if (rootData == null) {
				rootNode = new ProductContainerTreeNode(
						null);
			} else {
				rootNode = buildProductContainerTree(rootData);
			}
			this._productContainerTreeModel = new DefaultTreeModel(
					rootNode);
			this._productContainerTree
					.setModel(this._productContainerTreeModel);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	/**
	 * Builds the item container tree that will be displayed in the JTree.
	 */
	private DefaultMutableTreeNode buildProductContainerTree(
			ProductContainerData data) {
		ProductContainerDataSorter
				.sortProductContainerData(data);
		ProductContainerTreeNode node = new ProductContainerTreeNode(
				data);
		this._productContainers.put(data, node);
		for (int i = 0; i < data.getChildCount(); ++i) {
			node.add(buildProductContainerTree(data
					.getChild(i)));
		}
		return node;
	}

	@Override
	public void insertProductContainer(
			ProductContainerData parent,
			ProductContainerData newContainer,
			int index) {
		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode parentNode = this._productContainers
					.get(parent);
			assert parentNode != null;
			if (parentNode != null) {
				parent.insertChild(newContainer,
						index);

				ProductContainerTreeNode newNode = new ProductContainerTreeNode(
						newContainer);
				parentNode.insert(newNode, index);
				this._productContainers.put(
						newContainer, newNode);

				// notify the tree model of the change
				this._productContainerTreeModel
						.nodesWereInserted(
								parentNode,
								new int[] { index });
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public ProductContainerData getSelectedProductContainer() {
		ProductContainerTreeNode selectedNode = getSelectedProductContainerNode();
		return ((selectedNode != null) ? selectedNode
				.getProductContainer() : null);
	}

	@Override
	public void selectProductContainer(
			ProductContainerData container) {
		boolean disabledEvents = disableEvents();
		try {
			if (container != null) {
				if (this._productContainers
						.containsKey(container)) {
					selectProductContainerNode(this._productContainers
							.get(container));
				}
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
		getController()
				.productContainerSelectionChanged();
	}

	/**
	 * Selects the specified node in the product container tree.
	 * 
	 * @param node
	 *            The node to be selected
	 */
	private void selectProductContainerNode(
			ProductContainerTreeNode node) {
		TreeOperations.selectTreeNode(
				this._productContainerTree, node);
	}

	/**
	 * Returns the currently selected node in the product container tree.
	 */
	private ProductContainerTreeNode getSelectedProductContainerNode() {
		return (ProductContainerTreeNode) TreeOperations
				.getSelectedTreeNode(this._productContainerTree);
	}

	@Override
	public void deleteProductContainer(
			ProductContainerData deletedContainer) {
		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode deletedNode = this._productContainers
					.get(deletedContainer);
			assert deletedNode != null;

			if (deletedNode != null) {

				ProductContainerTreeNode parentNode = (ProductContainerTreeNode) deletedNode
						.getParent();
				assert parentNode != null;

				if (parentNode != null) {

					ProductContainerData parentContainer = parentNode
							.getProductContainer();
					parentContainer
							.deleteChild(deletedContainer);

					int childIndex = parentNode
							.getIndex(deletedNode);
					assert childIndex >= 0;
					if (childIndex >= 0) {
						parentNode
								.remove(childIndex);
						// notify the tree model of the change
						this._productContainerTreeModel
								.nodesWereRemoved(
										parentNode,
										new int[] { childIndex },
										new Object[] { deletedNode });
					}
				}
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void renameProductContainer(
			ProductContainerData renamedContainer,
			String newName, int newIndex) {

		boolean disabledEvents = disableEvents();
		try {
			ProductContainerTreeNode renamedNode = this._productContainers
					.get(renamedContainer);
			assert renamedNode != null;

			if (renamedNode != null) {

				ProductContainerTreeNode parentNode = (ProductContainerTreeNode) renamedNode
						.getParent();
				assert parentNode != null;

				if (parentNode != null) {

					ProductContainerData parentContainer = parentNode
							.getProductContainer();
					parentContainer.renameChild(
							renamedContainer,
							newName, newIndex);

					TreePath renamedPath = new TreePath(
							renamedNode.getPath());

					// remember which descendant nodes were expanded so they
					// can be re-expanded after the tree is updated
					ArrayList<TreePath> expandedList = null;
					Enumeration<TreePath> expandedEnum = this._productContainerTree
							.getExpandedDescendants(renamedPath);
					if (expandedEnum != null) {
						expandedList = new ArrayList<TreePath>();
						while (expandedEnum
								.hasMoreElements()) {
							TreePath path = expandedEnum
									.nextElement();
							expandedList
									.add(path);
						}
					}

					// update the tree
					this._productContainerTreeModel
							.removeNodeFromParent(renamedNode);
					this._productContainerTreeModel
							.insertNodeInto(
									renamedNode,
									parentNode,
									newIndex);

					// re-expand descendant nodes that were expanded before the
					// update
					if (expandedList != null) {
						for (TreePath path : expandedList) {
							this._productContainerTree
									.expandPath(path);
						}
					}
				}
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextUnit(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._contextInfoUnitField
					.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextGroup(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._contextInfoGroupField
					.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void setContextSupply(String value) {
		boolean disabledEvents = disableEvents();
		try {
			this._contextInfoSupplyField
					.setText(value);
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public ProductData getSelectedProduct() {
		int selectedIndex = this._productTable
				.getSelectedRow();
		if (selectedIndex >= 0) {
			ProductFormatter formatter = (ProductFormatter) this._productTableModel
					.getValueAt(selectedIndex, 0);
			return (ProductData) formatter
					.getTag();
		}
		return null;
	}

	@Override
	public void selectProduct(ProductData product) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < this._productTableModel
					.getRowCount(); ++i) {
				ProductFormatter formatter = (ProductFormatter) this._productTableModel
						.getValueAt(i, 0);
				ProductData id = (ProductData) formatter
						.getTag();
				if ((id == product)
						|| id.equals(product)) {
					TableOperations
							.selectTableRow(
									this._productTable,
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

	@Override
	public void setProducts(ProductData[] products) {
		boolean disabledEvents = disableEvents();
		try {
			this._productTableModel
					.setRowCount(0);
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
				this._productTableModel
						.addRow(row);
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public ItemData getSelectedItem() {
		int selectedIndex = this._itemTable
				.getSelectedRow();
		if (selectedIndex >= 0) {
			ItemFormatter formatter = (ItemFormatter) this._itemTableModel
					.getValueAt(selectedIndex, 0);
			return (ItemData) formatter.getTag();
		}
		return null;
	}

	@Override
	public void selectItem(ItemData item) {
		boolean disabledEvents = disableEvents();
		try {
			for (int i = 0; i < this._itemTableModel
					.getRowCount(); ++i) {
				ItemFormatter formatter = (ItemFormatter) this._itemTableModel
						.getValueAt(i, 0);
				ItemData id = (ItemData) formatter
						.getTag();
				if (id == item) {
					TableOperations
							.selectTableRow(
									this._itemTable,
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

	@Override
	public void setItems(ItemData[] items) {
		boolean disabledEvents = disableEvents();
		try {
			this._itemTableModel.setRowCount(0);
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
				this._itemTableModel.addRow(row);
			}
		} finally {
			if (disabledEvents) {
				enableEvents();
			}
		}
	}

	@Override
	public void displayAddProductGroupView() {
		ProductContainerData container = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(
				this._parent, "Add Product Group");
		AddProductGroupView dialogView = new AddProductGroupView(
				this._parent, dialogBox,
				container);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayAddStorageUnitView() {
		DialogBox dialog = new DialogBox(
				this._parent, "Add Storage Unit");
		dialog.display(new AddStorageUnitView(
				this._parent, dialog), false);
	}

	@Override
	public void displayEditProductGroupView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(
				this._parent,
				"Edit Product Group");
		EditProductGroupView dialogView = new EditProductGroupView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditStorageUnitView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(
				this._parent, "Edit Storage Unit");
		EditStorageUnitView dialogView = new EditStorageUnitView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayAddItemBatchView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(
				this._parent, "Add Items to "
						+ target.getName());
		AddItemBatchView dialogView = new AddItemBatchView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayTransferItemBatchView() {
		ProductContainerData target = getSelectedProductContainer();
		DialogBox dialogBox = new DialogBox(
				this._parent,
				"Transfer Items to "
						+ target.getName());
		TransferItemBatchView dialogView = new TransferItemBatchView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayRemoveItemBatchView() {
		DialogBox dialogBox = new DialogBox(
				this._parent, "Remove Items");
		RemoveItemBatchView dialogView = new RemoveItemBatchView(
				this._parent, dialogBox);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditProductView() {
		ProductData target = getSelectedProduct();
		DialogBox dialogBox = new DialogBox(
				this._parent, "Edit Product");
		EditProductView dialogView = new EditProductView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
	}

	@Override
	public void displayEditItemView() {
		ItemData target = getSelectedItem();
		DialogBox dialogBox = new DialogBox(
				this._parent, "Edit Item");
		EditItemView dialogView = new EditItemView(
				this._parent, dialogBox, target);
		dialogBox.display(dialogView, false);
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

	private static DataFlavor InventoryFlavor = new DataFlavor(
			InventoryTransferable.class,
			"Inventory Flavor");

	private class ProductTransferHandler extends
			TransferHandler {

		@Override
		protected Transferable createTransferable(
				JComponent component) {
			ProductData data = getSelectedProduct();
			if (data != null) {
				return new InventoryTransferable(
						data);
			} else {
				return null;
			}
		}

		@Override
		protected void exportDone(
				JComponent component,
				Transferable transferable,
				int action) {
			return;
		}

		@Override
		public int getSourceActions(
				JComponent component) {
			return COPY_OR_MOVE;
		}

	}

	private class ItemTransferHandler extends
			TransferHandler {

		@Override
		protected Transferable createTransferable(
				JComponent component) {
			ItemData data = getSelectedItem();
			if (data != null) {
				return new InventoryTransferable(
						data);
			} else {
				return null;
			}
		}

		@Override
		protected void exportDone(
				JComponent component,
				Transferable transferable,
				int action) {
			return;
		}

		@Override
		public int getSourceActions(
				JComponent component) {
			return COPY_OR_MOVE;
		}

	}

	private class ProductContainerTransferHandler
			extends TransferHandler {

		@Override
		public boolean canImport(
				TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}

			if (!support
					.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}

			javax.swing.JTree.DropLocation dropLoc = (javax.swing.JTree.DropLocation) support
					.getDropLocation();

			if (dropLoc == null) {
				return false;
			}

			TreePath path = dropLoc.getPath();

			if (path == null) {
				return false;
			}

			ProductContainerTreeNode dropNode = (ProductContainerTreeNode) path
					.getLastPathComponent();

			if (dropNode == null) {
				return false;
			}

			if (dropNode.isAllStorageUnits()) {
				return false;
			}

			return true;
		}

		@Override
		public boolean importData(
				TransferSupport support) {

			if (!support.isDrop()) {
				return false;
			}

			if (!support
					.isDataFlavorSupported(InventoryFlavor)) {
				return false;
			}

			javax.swing.JTree.DropLocation dropLoc = (javax.swing.JTree.DropLocation) support
					.getDropLocation();

			if (dropLoc == null) {
				return false;
			}

			TreePath path = dropLoc.getPath();

			if (path == null) {
				return false;
			}

			ProductContainerTreeNode dropNode = (ProductContainerTreeNode) path
					.getLastPathComponent();

			if (dropNode == null) {
				return false;
			}

			if (dropNode.isAllStorageUnits()) {
				return false;
			}

			Transferable transferable = support
					.getTransferable();

			if (transferable == null) {
				return false;
			}

			Object data = null;
			try {
				data = transferable
						.getTransferData(InventoryFlavor);
			} catch (Exception e) {
			}

			if (data == null) {
				return false;
			}

			ProductContainerData container = dropNode
					.getProductContainer();
			assert (container != null);

			if (data instanceof ProductData) {
				getController()
						.addProductToContainer(
								(ProductData) data,
								container);
			} else if (data instanceof ItemData) {
				getController()
						.moveItemToContainer(
								(ItemData) data,
								container);
			} else {
				return false;
			}

			return true;
		}

	}

	private class InventoryTransferable implements
			Transferable {

		private Object _data;

		public InventoryTransferable(Object data) {
			this._data = data;
		}

		@Override
		public Object getTransferData(
				DataFlavor flavor)
				throws UnsupportedFlavorException,
				IOException {
			if (InventoryFlavor.equals(flavor)) {
				return this._data;
			} else {
				throw new UnsupportedFlavorException(
						flavor);
			}
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { InventoryFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(
				DataFlavor flavor) {
			return InventoryFlavor.equals(flavor);
		}

	}

}

@SuppressWarnings("serial")
class ProductContainerTreeCellRenderer extends
		DefaultTreeCellRenderer {

	private Icon _storageUnitIcon;

	public ProductContainerTreeCellRenderer() {
		this._storageUnitIcon = new ImageIcon(
				"images" + java.io.File.separator
						+ "door-icon.png");
	}

	@Override
	public Component getTreeCellRendererComponent(
			JTree tree, Object value,
			boolean sel, boolean expanded,
			boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree,
				value, sel, expanded, leaf, row,
				hasFocus);

		if (value instanceof ProductContainerTreeNode) {
			ProductContainerTreeNode node = (ProductContainerTreeNode) value;
			if (node.isAllStorageUnits()
					|| node.isStorageUnit()) {
				setIcon(this._storageUnitIcon);
			} else {
				setIcon(this.closedIcon);
			}
		}

		return this;
	}

}
