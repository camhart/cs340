package gui.batches;

import gui.common.Controller;
import gui.common.IView;
import gui.inventory.ProductContainerData;
import gui.item.ItemData;
import gui.product.ProductData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import model.managers.ItemManager;
import model.managers.ProductManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.*;

import common.BarcodeLabelGenerator;

import controller.notify.ItemNotifier;
import controller.notify.ProductNotifier;

/**
 * Controller class for the add item batch view.
 */
public class AddItemBatchController extends
		Controller implements
		IAddItemBatchController, Observer {
	BarcodeLabelGenerator pdfer;
	Set<Item> newItems;

	private ProductContainerData target = null;
	private ProductContainer targetProductContainer = null;
	private ItemManager itemManager;
	private ProductManager productManager;

	/**
	 * Constructor.
	 * 
	 * @pre view != null and is a AddItemBatchView
	 * @pre target != null and contains valid ProductContainerData
	 * @post true
	 * @param view
	 *            Reference to the add item batch view.
	 * @param target
	 *            Reference to the storage unit to which items are being added.
	 */
	public AddItemBatchController(IView view,
			ProductContainerData target) {
		super(view);
		construct();
		this.target = target;
		this.targetProductContainer = (ProductContainer) target
				.getTag();
		this.itemManager = ItemManager
				.getInstance();
		this.itemManager.addObserver(this);
		this.productManager = ProductManager
				.getInstance();
		ProductManager.getInstance().addObserver(
				this);
	}

	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * @pre true
	 * @post true
	 * @return IAddItemBatchView interface
	 */
	@Override
	protected IAddItemBatchView getView() {
		return (IAddItemBatchView) super
				.getView();
	}

	boolean firstTime = true;

	/**
	 * Loads data into the controller's view.
	 * 
	 * @pre true
	 * @post The controller has loaded data into its view
	 */
	@Override
	protected void loadValues() {
		getView().setBarcode("");
		getView().setCount("1");
		getView().setEntryDate(new Date());
		if (getView().getSelectedProduct() != null) {
			getView()
					.selectProduct(
							getView()
									.getSelectedProduct());
		}
		getView().setUseScanner(
				this.firstTime ? true : getView()
						.getUseScanner());

		// getView().setUseScanner(getView().getUseScanner());

		// //necessary?
		// if(getView().getSelectedProduct() != null) {
		// loadItemData();
		// }
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently allowed to
	 * interact with that component.
	 * 
	 * @pre true
	 * 
	 * @post The enable/disable state of all components in the controller's view
	 *       have been set appropriately.
	 */
	@Override
	protected void enableComponents() {

		getView().enableRedo(false);
		getView().enableUndo(false);
		if (isCountValid()
				&& ProductBarcode
						.isValid(getView()
								.getBarcode())
				&& getView().getEntryDate() != null
				&& !getView().getUseScanner()) { // Date doesn't have to be
													// newer than now?
			getView().enableItemAction(true);
		} else {
			getView().enableItemAction(false);
		}

	}

	/**
	 * Determines if all of the data in AddItemBatchView is valid data If true,
	 * then AddItem should be enabled
	 * 
	 * @return
	 */
	private boolean isCountValid() {
		try {
			int amount = Integer
					.parseInt(getView()
							.getCount());
			if (amount > 0) {
				return true;
			}
		} catch (NumberFormatException e) {

		}
		return false;
	}

	/**
	 * This method is called when the "Entry Date" field in the add item batch
	 * view is changed by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void entryDateChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the "Count" field in the add item batch view
	 * is changed by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void countChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the "Product Barcode" field in the add item
	 * batch view is changed by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void barcodeChanged() {
		if (getView().getUseScanner()
				&& !getView().getBarcode()
						.isEmpty()) {
			addItem();
		}
		enableComponents();
	}

	/**
	 * This method is called when the "Use Barcode Scanner" setting in the add
	 * item batch view is changed by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void useScannerChanged() {
		// ?
	}

	private ArrayList<ProductBarcode> productsLoaded = new ArrayList<ProductBarcode>();
	private ProductData lastSelectedProduct;

	/**
	 * Loads the productdata into the view (only does the products that have
	 * been loaded)
	 * 
	 * @pre productsLoaded != null
	 * @post products loaded into view
	 */
	private void loadProductData() {
		if (this.productsLoaded != null) {
			getView()
					.setProducts(
							ProductData
									.convertToProductData(
											this.productsLoaded,
											this.countMap));
			if (this.lastSelectedProduct != null) {
				getView().selectProduct(
						this.lastSelectedProduct);
			}
		}
	}

	/**
	 * Loads the itemdata into the view
	 * 
	 * @pre getView().getSelectedProduct != null
	 * @post getView has items set to it
	 */
	private void loadItemData() {
		// ProductData productData = getView().getSelectedProduct();
		// Product product = null;
		// if(productData == null)
		// product = lastProductAdded;
		// else product = (Product)productData.getTag();
		if (this.lastSelectedProduct != null) {
			getView()
					.setItems(
							ItemData.convertToItemData(this.itemsAdded
									.get(new ProductBarcode(
											this.lastSelectedProduct
													.getBarcode()))));
		}
	}

	/**
	 * This method is called when the selected product changes in the add item
	 * batch view.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void selectedProductChanged() {
		this.lastSelectedProduct = getView()
				.getSelectedProduct() == null ? this.lastSelectedProduct
				: getView().getSelectedProduct();
		loadItemData();
	}

	private HashMap<ProductBarcode, Integer> countMap = new HashMap<ProductBarcode, Integer>();
	// private List<Item> itemsAdded = new ArrayList<Item>();
	private HashMap<ProductBarcode, ArrayList<Item>>
		itemsAdded = new HashMap<ProductBarcode, ArrayList<Item>>();

	private Product lastProductAdded = null;

	private void subAddItem() {
		if (getView().getBarcode().isEmpty()) {
			System.out.println("Ghetto hax");
			return; // ghetto hax
		}

		ProductBarcode productBarcode = new ProductBarcode(
				getView().getBarcode());
		this.lastProductAdded = this.productManager
				.getProductByBarcode(productBarcode);
		// new Product(getView().)
		if (this.lastProductAdded == null) {
			System.out.println("nulled!");
		}
		this.productsLoaded
				.add(this.lastProductAdded
						.getBarcode());

		int count = Integer.parseInt(getView()
				.getCount());

		if (this.countMap
				.containsKey(productBarcode)) {
			int oldAmt = this.countMap
					.get(productBarcode);
			oldAmt += count;
			this.countMap.put(productBarcode,
					oldAmt);
		} else {
			this.countMap.put(productBarcode,
					count);
		}

		for (int c = 0; c < count; c++) {
			Item itemToAdd = new Item(
					this.itemManager
							.generateItemBarcode(),
					this.lastProductAdded,
					getView().getEntryDate(),
					this.targetProductContainer);
			// itemsAdded.add(itemToAdd);
			ArrayList<Item> itemsByProduct = this.itemsAdded
					.get(productBarcode);
			if (itemsByProduct == null) {
				itemsByProduct = new ArrayList<Item>();
				this.itemsAdded.put(
						productBarcode,
						itemsByProduct);
			}
			itemsByProduct.add(itemToAdd);
			this.itemManager.add(itemToAdd);
		}
		this.productManager.setChangedOverride();
		this.productManager
				.notifyObservers(new ProductNotifier());
		this.itemManager
				.notifyObservers(new ItemNotifier());
	}

	/**
	 * This method is called when the user clicks the "Add Item" button in the
	 * add item batch view.
	 * 
	 * @pre valid data is loaded in the AddItemBatchView
	 * @post data is grabbed from AddItemBatchView and put into the data model.
	 *       data is also added to the controllers set of new items so
	 *       BarcodeLabelGenerator can generate the pdf of all new barcodes for
	 *       items added.
	 */
	@Override
	public void addItem() {
		// assert this.isCountValid() : "Invalid count!";
		if (!isCountValid()) {
			getView().displayErrorMessage(
					"Invalid count amount.");
			return;
		}

		ProductBarcode productBarcode = new ProductBarcode(
				getView().getBarcode());
		if (this.productManager
				.productExists(productBarcode)) {

			// if(productManager.getProductsByContainer(Context.getInstance()
			// .getSelectedContainer())))
			Collection<Product> productCollection = this.productManager
					.getProductsByContainer(this.targetProductContainer);
			Product curProduct = this.productManager
					.getProductByBarcode(productBarcode);
			if (productCollection == null
					|| !productCollection
							.contains(curProduct)) {
				List<ProductGroup> subList = new LinkedList<ProductGroup>();
				this.targetProductContainer
						.getAllSubProductGroups(subList);
				for (ProductGroup pg : subList) {
					if (pg.containsProduct(productBarcode)) {
						this.targetProductContainer = pg;
					}
				}

				ProductContainer targetHoldingProduct = this.targetProductContainer
						.getStorageUnit()
						.findProductContainer(
								productBarcode);
				if (targetHoldingProduct == null) {
					this.productManager
							.addToContainer(
									curProduct,
									this.targetProductContainer);
					System.out
							.println("adding_product_to_container");
				}
			}
			subAddItem();
		} else {
			// int count = Integer.parseInt(getView().getCount());
			// if(countMap.containsKey(productBarcode)) {
			// int oldAmt = countMap.get(productBarcode);
			// oldAmt+=count;
			// countMap.put(productBarcode, oldAmt);
			// } else {
			// countMap.put(productBarcode, count);
			// }
			this.productsLoaded
					.add(productBarcode);

			getView().displayAddProductView();
		}
	}

	/**
	 * This method is called when the user clicks the "Redo" button in the add
	 * item batch view.
	 * 
	 * @pre undone action exists, available for redo
	 * @post true
	 */
	@Override
	public void redo() {
	}

	/**
	 * This method is called when the user clicks the "Undo" button in the add
	 * item batch view.
	 * 
	 * @pre action exists to be undone
	 * @post true
	 */
	@Override
	public void undo() {
	}

	/**
	 * This method is called when the user clicks the "Done" button in the add
	 * item batch view.
	 * 
	 * @pre true
	 * @post Barcode report generated and shown
	 */
	@Override
	public void done() {
		// Generate Barcode Report if New Items were Added
		ArrayList<Item> addedItems = new ArrayList<Item>();
		Iterator<ProductBarcode> iter = this.itemsAdded
				.keySet().iterator();
		while (iter.hasNext()) {
			ArrayList<Item> itemsToAdd = this.itemsAdded
					.get(iter.next());
			addedItems.addAll(itemsToAdd);
		}

		if (addedItems.size() > 0) {

			try {
				BarcodeLabelGenerator
						.generatePDF(addedItems);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		getView().close();
	}

	/**
	 * Notifies this controller that updates are needed. Controller then pulls
	 * the updates from the observable.
	 * 
	 * @pre observable != null, the ItemManger called update
	 * @post The view contains all the items that are being added from this
	 *       operation.
	 * 
	 * @param observable
	 *            Object that update is getting called from.
	 * @param obj
	 *            not necessary since we're using pull, just keep as null.
	 */
	@Override
	public void update(Observable observable,
			Object obj) {
		// assert observable.getClass() == ItemManager.class : "Wrong class!";
		// TODO Auto-generated method stub
		// getView().setItems(items)
		// getView().setProducts(products)

		// Everything should already be loaded... is this needed?

		if (observable.getClass() == ProductManager.class) {
			System.out.print("did i add? ");
			if (((ProductNotifier) obj)
					.itemNeedsAdded()) { // new
											// ProductNotifier(true)
				System.out
						.println("Batch item notifier...");
				subAddItem();
				// getView().selectProduct(ProductData.convertToProductData(lastProductAdded,
				// countMap.get(lastProductAdded.getBarcode())));
				// getView().selectProduct(product)
				loadItemData();
			} else {
				System.out.println("___");
				loadProductData();
			}
			// System.out.println("asdf");

		} else if (observable.getClass() == ItemManager.class) {
			loadProductData();
			loadItemData();
		}
		loadValues();
		getView().giveBarcodeFocus();
		if (getView().getSelectedProduct() != null) {
			getView()
					.selectProduct(
							getView()
									.getSelectedProduct());
		}

		enableComponents();
	}

}
