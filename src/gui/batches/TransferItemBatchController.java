package gui.batches;

import gui.common.Controller;
import gui.common.IView;
import gui.inventory.ProductContainerData;
import gui.item.ItemData;
import gui.product.ProductData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import model.managers.ItemManager;
import model.managers.ProductManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ItemBarcode;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.StorageUnit;
import controller.notify.ItemNotifier;
import controller.notify.ProductNotifier;

/**
 * Controller class for the transfer item batch view.
 */
public class TransferItemBatchController extends
		Controller implements
		ITransferItemBatchController {

	private StorageUnit targetDestination = null;
	private ProductData lastSelectedProduct = null;

	/**
	 * Constructor.
	 * 
	 * @pre view is a non null TransferItemBatchView
	 * @pre target is non null and contains valid product container data
	 * @post true
	 * @param view
	 *            Reference to the transfer item batch view.
	 * @param target
	 *            Reference to the storage unit to which items are being
	 *            transferred.
	 */
	public TransferItemBatchController(
			IView view,
			ProductContainerData target) {
		super(view);
		this.targetDestination = (StorageUnit) target
				.getTag();
		construct();
		ItemManager.getInstance().addObserver(
				this);
		ProductManager.getInstance().addObserver(
				this);
	}

	/**
	 * @pre true
	 * @post true Returns a reference to the view for this controller.
	 */
	@Override
	protected ITransferItemBatchView getView() {
		return (ITransferItemBatchView) super
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
		getView().enableRedo(false);
		getView().enableUndo(false);
		if (!getView().getUseScanner()
				&& !getView().getBarcode()
						.isEmpty()) {
			getView().enableItemAction(true);
		} else {
			getView().enableItemAction(false);
		}
	}

	/**
	 * This method is called when the "Item Barcode" field in the transfer item
	 * batch view is changed by the user.
	 * 
	 * @pre true
	 * @pre true
	 */
	@Override
	public void barcodeChanged() {
		if (getView().getUseScanner()) {
			transferItem();
		}
		enableComponents();
	}

	/**
	 * This method is called when the "Use Barcode Scanner" setting in the
	 * transfer item batch view is changed by the user.
	 * 
	 * @pre true
	 * @pre true
	 */
	@Override
	public void useScannerChanged() {
	}

	/**
	 * This method is called when the selected product changes in the transfer
	 * item batch view.
	 * 
	 * @pre true
	 * @pre true
	 */
	@Override
	public void selectedProductChanged() {
		this.lastSelectedProduct = getView()
				.getSelectedProduct() == null ? this.lastSelectedProduct
				: getView().getSelectedProduct();
		loadItemData();
	}

	/**
	 * Loads the productdata into the view (only does the products that have
	 * been loaded)
	 * 
	 * @pre productsLoaded != null
	 * @post products loaded into view
	 */
	private void loadProductData() {
		// getView().setProducts(ProductData.convertToProductData(productsTransfered,
		// countMap));
		if (this.productsTransfered != null) {
			getView()
					.setProducts(
							ProductData
									.convertToProductData(
											this.productsTransfered,
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
		if (this.lastSelectedProduct != null) {
			getView()
					.setItems(
							ItemData.convertToItemData(this.itemsTransfered
									.get(new ProductBarcode(
											this.lastSelectedProduct
													.getBarcode()))));
		}
	}

	private ArrayList<Product> productsTransfered = new ArrayList<Product>();
	private HashMap<ProductBarcode, Integer> countMap = new HashMap<ProductBarcode, Integer>();
	// private List<Item> itemsAdded = new ArrayList<Item>();
	private HashMap<ProductBarcode, ArrayList<Item>> itemsTransfered
		= new HashMap<ProductBarcode, ArrayList<Item>>();

	/**
	 * This method is called when the user clicks the "Transfer Item" button in
	 * the transfer item batch view.
	 * 
	 * @pre getView must contain valid data for an item to transfer
	 * @post item is transfered according the spec requirements. Placed in
	 *       selected storage unit or a product group within that storage unit
	 *       that contains the item's product.
	 */
	@Override
	public void transferItem() {
		ProductManager.getInstance();
		ItemManager itemManager = ItemManager
				.getInstance();
		ItemBarcode itemBarcode = new ItemBarcode(
				getView().getBarcode());
		if (itemManager.itemExists(itemBarcode)) {
			Item theItem = itemManager
					.getItemByBarcode(itemBarcode);
			Product product = theItem
					.getProduct();
			if (!this.productsTransfered
					.contains(product)) {
				this.productsTransfered
						.add(product);
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

			ArrayList<Item> itemsByProduct = this.itemsTransfered
					.get(product.getBarcode());
			if (itemsByProduct == null) {
				itemsByProduct = new ArrayList<Item>();
				this.itemsTransfered.put(
						product.getBarcode(),
						itemsByProduct);
			}
			itemsByProduct.add(theItem);

			System.out.println(itemsByProduct);
			itemManager.transferItem(theItem,
					this.targetDestination);

			itemManager
					.notifyObservers(new ItemNotifier());
			ProductManager.getInstance()
					.setChangedOverride();
			ProductManager
					.getInstance()
					.notifyObservers(
							new ProductNotifier());

		} else {
			getView()
					.displayErrorMessage(
							"Item barcode doesn't exist.");
		}
		loadValues();
	}

	/**
	 * This method is called when the user clicks the "Redo" button in the
	 * transfer item batch view.
	 * 
	 * @pre undone action exists to redo
	 * @post true
	 */
	@Override
	public void redo() {
	}

	/**
	 * This method is called when the user clicks the "Undo" button in the
	 * transfer item batch view.
	 * 
	 * @pre action exists to undo
	 * @post true
	 */
	@Override
	public void undo() {
	}

	/**
	 * This method is called when the user clicks the "Done" button in the
	 * transfer item batch view.
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
		// TODO Auto-generated method stub
		loadProductData();
		loadItemData();
		loadValues();
		enableComponents();
	}

}
