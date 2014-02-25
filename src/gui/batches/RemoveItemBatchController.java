package gui.batches;

import gui.common.Controller;
import gui.common.IView;
import gui.item.ItemData;
import gui.product.ProductData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import model.managers.ItemManager;
import model.managers.ProductManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ItemBarcode;
import model.models.barcode.ProductBarcode;
import controller.context.Context;
import controller.notify.ItemNotifier;

/**
 * Controller class for the remove item batch view.
 */
public class RemoveItemBatchController extends
		Controller implements
		IRemoveItemBatchController {

	private ItemManager itemManager = null;

	/**
	 * Constructor.
	 * 
	 * @pre view is a non null valid RemoveItemBatchView
	 * @post true
	 * @param view
	 *            Reference to the remove item batch view.
	 */
	public RemoveItemBatchController(IView view) {
		super(view);
		this.itemManager = ItemManager
				.getInstance();
		this.itemManager.addObserver(this);
		ProductManager.getInstance().addObserver(
				this);
		construct();
	}

	/**
	 * @pre true
	 * @post true Returns a reference to the view for this controller.
	 */
	@Override
	protected IRemoveItemBatchView getView() {
		return (IRemoveItemBatchView) super
				.getView();
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 * @pre None
	 * 
	 * @post The controller has loaded data into its view
	 */
	@Override
	protected void loadValues() {
		getView().setBarcode("");
		if (getView().getSelectedProduct() != null) {
			getView()
					.selectProduct(
							getView()
									.getSelectedProduct());
		}
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently allowed to
	 * interact with that component.
	 * 
	 * @pre None
	 * 
	 * @post The enable/disable state of all components in the controller's view
	 *       have been set appropriately.
	 */
	@Override
	protected void enableComponents() {
		if (!getView().getUseScanner()
				&& !getView().getBarcode()
						.isEmpty()) {
			getView().enableItemAction(true);
		} else {
			getView().enableItemAction(false);
		}

		getView().enableRedo(false);
		getView().enableUndo(false);

	}

	/**
	 * This method is called when the "Item Barcode" field is changed in the
	 * remove item batch view by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void barcodeChanged() {
		if (getView().getUseScanner()) {
			removeItem();
		}
		enableComponents();
	}

	/**
	 * This method is called when the "Use Barcode Scanner" setting is changed
	 * in the remove item batch view by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void useScannerChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the selected product changes in the remove
	 * item batch view.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void selectedProductChanged() {
		loadItemData();
	}

	/**
	 * Loads the itemdata into the view
	 * 
	 * @pre getView().getSelectedProduct != null
	 * @post getView has items set to it
	 */
	private void loadItemData() {
		ProductData productData = getView()
				.getSelectedProduct();
		if (productData != null) {
			Product product = (Product) productData
					.getTag();

			getView()
					.setItems(
							ItemData.convertToItemData(this.itemsRemoved.get(product
									.getBarcode())));
		}
	}

	/**
	 * Loads the productdata into the view (only does the products that have
	 * been loaded)
	 * 
	 * @pre productsLoaded != null
	 * @post products loaded into view
	 */
	private void loadProductData() {
		getView().setProducts(
				ProductData.convertToProductData(
						this.productsRemoved,
						this.countMap));
	}

	private List<Product> productsRemoved = new ArrayList<Product>();

	private HashMap<ProductBarcode, Integer> countMap = new HashMap<ProductBarcode, Integer>();
	// private List<Item> itemsAdded = new ArrayList<Item>();
	private HashMap<ProductBarcode, ArrayList<Item>> itemsRemoved
		= new HashMap<ProductBarcode, ArrayList<Item>>();

	/**
	 * This method is called when the user clicks the "Remove Item" button in
	 * the remove item batch view.
	 * 
	 * @pre Valid item data exists in RemoveItemBatchView
	 * @post the item is removed from the data model
	 */
	@Override
	public void removeItem() {
		ItemBarcode itemBarcode = new ItemBarcode(
				getView().getBarcode());
		if (this.itemManager
				.itemExists(itemBarcode)) {
			Item theItem = this.itemManager
					.getItemByBarcode(itemBarcode);
			// System.out.println("theItem's container: " +
			// theItem.getContainer());
			Product product = theItem
					.getProduct();
			if (!this.productsRemoved
					.contains(product)) {
				this.productsRemoved.add(product);
			}

			if (!this.countMap
					.containsKey(product
							.getBarcode())) {
				this.countMap.put(
						product.getBarcode(), 1);
			} else {
				int countAmt = this.countMap
						.get(product.getBarcode());
				countAmt++;
				this.countMap.put(
						product.getBarcode(),
						countAmt);
			}

			ArrayList<Item> itemsByProduct = this.itemsRemoved
					.get(product.getBarcode());
			if (itemsByProduct == null) {
				itemsByProduct = new ArrayList<Item>();
				this.itemsRemoved.put(
						product.getBarcode(),
						itemsByProduct);
			}
			itemsByProduct.add(theItem);

			this.itemManager.consume(theItem);

			this.itemManager
					.notifyObservers(new ItemNotifier());

		} else {
			getView()
					.displayErrorMessage(
							"Item barcode doesn't exist.");
		}
	}

	/**
	 * This method is called when the user clicks the "Redo" button in the
	 * remove item batch view.
	 * 
	 * @pre undone action exists to redo
	 * @post true
	 */
	@Override
	public void redo() {
	}

	/**
	 * This method is called when the user clicks the "Undo" button in the
	 * remove item batch view.
	 * 
	 * @pre action exists to undo
	 * @post true
	 */
	@Override
	public void undo() {
	}

	/**
	 * This method is called when the user clicks the "Done" button in the
	 * remove item batch view.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void done() {
		getView().close();
	}

	/**
	 * Notifies this controller that updates are needed. Controller then pulls
	 * the updates from the observable.
	 * 
	 * @pre An observed class called the Notify method. observable != null
	 * @post The view is updated according to the model.
	 * @param observable
	 *            Object that update is getting called from.
	 * @param obj
	 *            not necessary since we're using pull, just keep as null.
	 */
	@Override
	public void update(Observable observable,
			Object obj) {
		System.out.println("asdf");
		getView()
				.selectProduct(
						Context.getInstance()
								.getRecentlySelectedProductData());
		loadProductData();
		getView()
				.selectProduct(
						Context.getInstance()
								.getRecentlySelectedProductData());
		loadItemData();
		// System.out.println("load values");
		loadValues();
		enableComponents();
	}

}
