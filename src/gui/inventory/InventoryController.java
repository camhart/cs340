package gui.inventory;

import gui.common.*;
import gui.item.*;
import gui.product.*;

import java.util.*;

import controller.context.Context;
import controller.notify.ItemNotifier;
import controller.notify.Notifier;
import controller.notify.ProductNotifier;

import model.managers.ItemManager;
import model.managers.ProductGroupManager;
import model.managers.ProductManager;
import model.managers.SessionManager;
import model.managers.StorageUnitManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.ProductGroup;
import model.models.productContainer.StorageUnit;
import model.models.unit.Unit;

/**
 * Controller class for inventory view.
 */
public class InventoryController extends
		Controller implements
		IInventoryController, Observer {

	private StorageUnit root;
	private ProductContainerData rootData;

	/**
	 * Constructor.
	 * 
	 * @pre view != null
	 * @post The controller has loaded its data into its view and the
	 *       enable/disable components have been set.
	 * 
	 * @param view
	 *            Reference to the inventory view
	 */
	public InventoryController(IInventoryView view) {
		super(view);
		SessionManager.getInstance().loadState();
		this.root = new StorageUnit(
				"Storage Units");
		// System.out.println("set!!");
		Context.getInstance().setRoot(this.root);
		this.rootData = new ProductContainerData(
				this.root);
		ProductGroupManager.getInstance()
				.addObserver(this);
		StorageUnitManager.getInstance()
				.addObserver(this);
		ProductManager.getInstance().addObserver(
				this);
		ItemManager.getInstance().addObserver(
				this);
		Context.getInstance().addObserver(this);
		construct();
	}

	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * @pre True
	 * @post the view for this controller is returned.
	 */
	@Override
	protected IInventoryView getView() {
		return (IInventoryView) super.getView();
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 * {@pre None}
	 * 
	 * {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {

		Map<String, StorageUnit> map = StorageUnitManager
				.getInstance().getStorageUnits();
		for (String key : map.keySet()) {
			ProductContainer child = map.get(key);
			this.rootData
					.addChild(new ProductContainerData(
							child));
		}
		getView().setProductContainers(
				this.rootData);
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently allowed to
	 * interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's
	 * view have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
	}

	//
	// IInventoryController overrides
	//

	/**
	 * Returns true if and only if the "Add Storage Unit" menu item should be
	 * enabled.
	 * 
	 * @pre The root Storage Unit was right-clicked.
	 * @post Returns true if the "Add Storage Unit" menu item should be enabled.
	 */
	@Override
	public boolean canAddStorageUnit() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return selected.equals(this.root);
	}

	/**
	 * Returns true if and only if the "Add Items" menu item should be enabled.
	 * 
	 * @pre A Storage Unit was right clicked.
	 * @post Returns true if and only if the "Add Items" menu item should be
	 *       enabled.
	 */
	@Override
	public boolean canAddItems() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return (selected instanceof StorageUnit)
				&& (selected != this.root);
	}

	/**
	 * Returns true if and only if the "Transfer Items" menu item should be
	 * enabled.
	 * 
	 * @pre True
	 * @post Returns true if and only if the "Transfer Items" menu item should
	 *       be enabled.
	 */
	@Override
	public boolean canTransferItems() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return selected instanceof ProductGroup;
	}

	/**
	 * Returns true if and only if the "Remove Items" menu item should be
	 * enabled.
	 * 
	 * @pre An Item has been right clicked.
	 * @post Returns true if and only if the "Remove Items" menu item should be
	 *       enabled.
	 */
	@Override
	public boolean canRemoveItems() {
		return getView().getSelectedItem() != null;
	}

	/**
	 * Returns true if and only if the "Delete Storage Unit" menu item should be
	 * enabled.
	 * 
	 * @pre A Storage Unit has been right clicked.
	 * @post Returns true if and only if ProductContainer's canDelete returned
	 *       true.
	 */
	@Override
	public boolean canDeleteStorageUnit() {
		ProductContainer storageUnit = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		if (!(storageUnit instanceof StorageUnit)) {
			return false;
		}
		return containerHasNoItems(storageUnit);
	}

	private boolean containerHasNoItems(
			ProductContainer container) {
		if (!container.canBeDeleted()) {
			return false;
		}

		/*
		 * ProductContainer's containerHasNoItems method already does this for(
		 * ProductGroup pg : container.getProductGroups() ) { boolean hasNoItems
		 * = containerHasNoItems(pg); if( !hasNoItems ) { return false; } }
		 */

		return true;

	}

	/**
	 * This method is called when the user selects the "Delete Storage Unit"
	 * menu item.
	 * 
	 * @pre canDeleteStorageUnit returned true for the specified Storage Unit.
	 * @post The selected StorageUnit is deleted from the model and the view is
	 *       updated.
	 */
	@Override
	public void deleteStorageUnit() {
		StorageUnit toDelete = (StorageUnit) getView()
				.getSelectedProductContainer()
				.getTag();
		StorageUnitManager.getInstance().delete(
				toDelete.getName());
	}

	/**
	 * Returns true if and only if the "Edit Storage Unit" menu item should be
	 * enabled.
	 * 
	 * @pre A Storage Unit has been right clicked.
	 * @post Returns true if and only if the "Edit Storage Unit" menu item
	 *       should be enabled.
	 */
	@Override
	public boolean canEditStorageUnit() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return selected instanceof StorageUnit;
	}

	/**
	 * Returns true if and only if the "Add Product Group" menu item should be
	 * enabled.
	 * 
	 * @pre A ProductContainer has been right clicked.
	 * @post Returns true if and only if the "Add Product Group" menu item
	 *       should be enabled.
	 */
	@Override
	public boolean canAddProductGroup() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return !selected.equals(this.root);
	}

	/**
	 * Returns true if and only if the "Delete Product Group" menu item should
	 * be enabled.
	 * 
	 * @pre A Product Group has been right clicked.
	 * @post Returns true if and only if the ProductGroup's canDelete method
	 *       returned true.
	 */
	@Override
	public boolean canDeleteProductGroup() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return (selected instanceof ProductGroup)
				&& containerHasNoItems(selected);
	}

	/**
	 * Returns true if and only if the "Edit Product Group" menu item should be
	 * enabled.
	 * 
	 * @pre A Product Group has been right clicked.
	 * @post Returns true if and only if the "Edit Product Group" menu item
	 *       should be enabled.
	 */
	@Override
	public boolean canEditProductGroup() {
		ProductContainer selected = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		return selected instanceof ProductGroup;
	}

	/**
	 * This method is called when the user selects the "Delete Product Group"
	 * menu item.
	 * 
	 * @pre A Product Group was right clicked and ProductContainer.canDelete()
	 *      returned true.
	 * @post The selected Product Group is deleted from the model and the view
	 *       is updated.
	 */
	@Override
	public void deleteProductGroup() {
		ProductGroup selected = (ProductGroup) getView()
				.getSelectedProductContainer()
				.getTag();
		ProductGroupManager.getInstance().delete(
				selected);
	}

	private Random rand = new Random();

	private String getRandomBarcode() {
		Random rand = new Random();
		StringBuilder barcode = new StringBuilder();
		for (int i = 0; i < 12; ++i) {
			barcode.append(((Integer) rand
					.nextInt(10)).toString());
		}
		return barcode.toString();
	}

	public void setRootProductData() {
		// do crap here
		List<Product> products = ProductManager
				.getInstance().getAllProducts();
		HashMap<ProductBarcode, Integer> countMap = ItemManager
				.getInstance()
				.getProductCountMap();

		Collections.sort(products);

		assert countMap != null;
		assert products != null;
		ProductData selectedProduct = getView()
				.getSelectedProduct();
		getView().setProducts(
				ProductData.convertToProductData(
						products, countMap));
		if (selectedProduct != null) {
			getView().selectProduct(
					selectedProduct);
			// System.out.println("setting all products");
		}

	}

	public void setRootItemData(
			Product selectedProduct) {
		Set<Item> items = ItemManager
				.getInstance().getItemsByProduct(
						selectedProduct);
		Set<Item> unconsumedItems = new TreeSet<Item>();
		for (Item i : items) {
			if (i.getContainer() != ProductContainer.CONSUMED_ITEMS) {
				unconsumedItems.add(i);
			}
		}
		getView()
				.setItems(
						ItemData.convertToItemData(unconsumedItems));
	}

	/**
	 * This method is called when the selected item container changes.
	 * 
	 * @pre The selected item container was changed.
	 * @post The specified item container is now selected
	 */
	@Override
	public void productContainerSelectionChanged() {
		// System.out.println("container selection changed in controller");
		ProductContainerData selectedContainer = getView()
				.getSelectedProductContainer();
		ProductContainer productContainer = (ProductContainer) selectedContainer
				.getTag();
		// System.out.println("SELECTED: " + productContainer.getName());
		if (productContainer == this.root) {
			setRootProductData();
		} else {

			// get products
			Collection<Product> products = ProductManager
					.getInstance()
					.getProductsByContainer(
							productContainer);
			if (products == null) {
				getView().setProducts(
						new ProductData[0]);
				// System.out.println("Setting no products");
			} else {
				ProductData[] productDataArr = ProductData
						.convertToProductData(
								products,
								productContainer);
				getView().setProducts(
						productDataArr);
				// System.out.println("Setting some products");
			}

			getView().setItems(new ItemData[0]);

		}
		// context
		getView().setContextUnit(
				productContainer.getStorageUnit()
						.getName());
		if (productContainer instanceof ProductGroup) {
			getView().setContextGroup(
					productContainer.getName());
			Unit supply = ((ProductGroup) productContainer)
					.GetThreeMonthSupply();
			getView().setContextSupply(
					supply.getUnitString());
		} else {
			getView().setContextGroup("");
			getView().setContextSupply("");
		}
		Context.getInstance()
				.setSelectedContainer(
						productContainer);
	}

	/**
	 * This method is called when the selected item changes.
	 * 
	 * @pre The item selected by the user changed to a different item.
	 * @post The item indicated by the user is now selected.
	 */
	@Override
	public void productSelectionChanged() {
		Product selectedProduct = (Product) getView()
				.getSelectedProduct().getTag();
		ProductContainer pc = (ProductContainer) getView()
				.getSelectedProductContainer()
				.getTag();
		if (pc == this.root) {
			setRootItemData(selectedProduct);
		} else {
			Set<Item> items = ItemManager
					.getInstance()
					.getItemsByContainerAndProduct(
							pc,
							selectedProduct
									.getBarcode());
			getView()
					.setItems(
							ItemData.convertToItemData(items));
		}
		Context.getInstance().setSelectedProduct(
				selectedProduct);
		Context.getInstance()
				.setRecentlySelectedProductData(
						getView()
								.getSelectedProduct());
	}

	/**
	 * This method is called when the selected item changes.
	 * 
	 * @pre The user clicked on a different item.
	 * @post The newly-selected item the user indicated is now selected.
	 */
	@Override
	public void itemSelectionChanged() {
		Item selectedItem = (Item) getView()
				.getSelectedItem().getTag();
		Context.getInstance().setSelectedItem(
				selectedItem);
	}

	/**
	 * Returns true if and only if the "Delete Product" menu item should be
	 * enabled.
	 * 
	 * @pre The user right clicked on a Product.
	 * @post Returns true is Product's canDelete method returned true.
	 */
	@Override
	public boolean canDeleteProduct() {
		ProductData pd = getView()
				.getSelectedProduct();
		if (pd == null) {
			return false;
		}

		Product product = (Product) pd.getTag();

		if (getView()
				.getSelectedProductContainer() == this.rootData) {
			// return ItemManager.getInstance().itemExists(barcode)
			// Product product =
			// ProductManager.getInstance().getProductByBarcode(productBarcode)
			Set<Item> items = ItemManager
					.getInstance()
					.getItemsByProduct(product);

			int count = 0;
			Iterator<Item> iter = items
					.iterator();
			while (iter.hasNext()) {
				if (iter.next().getContainer() != ProductContainer.CONSUMED_ITEMS) {
					count++;
				}
			}

			// System.out.println(product + " contains " + items);
			return (items == null)
					|| (count == 0);
		}

		return ItemManager
				.getInstance()
				.canDeleteProduct(
						(ProductContainer) getView()
								.getSelectedProductContainer()
								.getTag(),
						product.getBarcode());

	}

	/**
	 * This method is called when the user selects the "Delete Product" menu
	 * item.
	 * 
	 * @pre canDeleteProduct returned true for the specified product.
	 * @post The specified Product is deleted from the model and the view is
	 *       updated.
	 */
	@Override
	public void deleteProduct() {
		Product product = (Product) getView()
				.getSelectedProduct().getTag();
		if (getView()
				.getSelectedProductContainer() == this.rootData) {
			ProductManager.getInstance().delete(
					product);
		} else {
			ProductManager
					.getInstance()
					.deleteFromContainer(
							product,
							(ProductContainer) getView()
									.getSelectedProductContainer()
									.getTag());
		}
		getView().selectProduct(null);
		Context.getInstance()
				.setRecentlySelectedProductData(
						null);
		ProductManager.getInstance()
				.notifyObservers(
						new ProductNotifier());
	}

	/**
	 * Returns true if and only if the "Edit Item" menu item should be enabled.
	 * 
	 * @pre An item was right clicked.
	 * @post
	 */
	@Override
	public boolean canEditItem() {
		return getView().getSelectedItem() != null;
	}

	/**
	 * This method is called when the user selects the "Edit Item" menu item.
	 * 
	 * @pre canEditItem returned true for the specified Item
	 * @post the Item is updated with the new values in the model and the view
	 *       reflects the changes.
	 */
	@Override
	public void editItem() {
		getView().displayEditItemView();
	}

	/**
	 * Returns true if and only if the "Remove Item" menu item should be
	 * enabled.
	 * 
	 * @pre The user right clicked on an item.
	 * @post Item's canRemove method returned true for the specified item.
	 */
	@Override
	public boolean canRemoveItem() {
		return getView().getSelectedItem() != null;
	}

	/**
	 * This method is called when the user selects the "Remove Item" menu item.
	 * 
	 * @pre canRemoveItem returned true for the specified Item.
	 * @post The specified item is removed from the model and the view is
	 *       updated.
	 */
	@Override
	public void removeItem() {
		Item toRemove = (Item) getView()
				.getSelectedItem().getTag();
		ItemManager.getInstance().consume(
				toRemove);
	}

	/**
	 * Returns true if and only if the "Edit Product" menu item should be
	 * enabled.
	 * 
	 * @pre The user right clicked on a Product.
	 * @post Returns true if and only if the "Edit Product" menu item should be
	 *       enabled.
	 */
	@Override
	public boolean canEditProduct() {
		return getView().getSelectedProduct() != null;
	}

	/**
	 * This method is called when the user selects the "Add Product Group" menu
	 * item.
	 * 
	 * @pre canAddProductGroup returned true for the specified product group.
	 * @post The newly-created ProductGroup is added to the model and the view
	 *       is updated.
	 */
	@Override
	public void addProductGroup() {
		getView().displayAddProductGroupView();
	}

	/**
	 * This method is called when the user selects the "Add Items" menu item.
	 * 
	 * @pre canAddItem returned true for all items created by the user.
	 * @post The items created by the user are added to the model and the view
	 *       is updated.
	 */
	@Override
	public void addItems() {
		getView().displayAddItemBatchView();
	}

	/**
	 * This method is called when the user selects the "Transfer Items" menu
	 * item.
	 * 
	 * @pre canTransferItems returned true for all specified items.
	 * @post the items are transfered to the correct container in the model and
	 *       the view is updated.
	 */
	@Override
	public void transferItems() {
		getView().displayTransferItemBatchView();
	}

	/**
	 * This method is called when the user selects the "Remove Items" menu item.
	 * 
	 * @pre canRemoveItems returned true for the specified items.
	 * @post The items are removed from the model and the view is updated.
	 */
	@Override
	public void removeItems() {
		getView().displayRemoveItemBatchView();
	}

	/**
	 * This method is called when the user selects the "Add Storage Unit" menu
	 * item.
	 * 
	 * @pre canAddStorageUnit returned true.
	 * @post The new storage unit is created and inserted into the model and the
	 *       view is updated.
	 */
	@Override
	public void addStorageUnit() {
		getView().displayAddStorageUnitView();
	}

	/**
	 * This method is called when the user selects the "Edit Product Group" menu
	 * item.
	 * 
	 * @pre canEditProductGroup returned true
	 * @post The specified product group is changed in the model and the view is
	 *       updated.
	 */
	@Override
	public void editProductGroup() {
		getView().displayEditProductGroupView();
	}

	/**
	 * This method is called when the user selects the "Edit Storage Unit" menu
	 * item.
	 * 
	 * @pre canEditStorageUnit returned true
	 * @post The specified storage unit is changed in the model and the view is
	 *       updated.
	 */
	@Override
	public void editStorageUnit() {
		getView().displayEditStorageUnitView();
	}

	/**
	 * This method is called when the user selects the "Edit Product" menu item.
	 * 
	 * @pre canEditProduct returned true for the selected product
	 * @post The specified product is changed to the specified values in the
	 *       model and the view is updated.
	 */
	@Override
	public void editProduct() {
		getView().displayEditProductView();
	}

	/**
	 * This method is called when the user drags a product into a product
	 * container.
	 * 
	 * @pre productManager's isValid returned true, productData != null,
	 *      containerData != null
	 * @post The product is added to the container in the model and the view is
	 *       updated.
	 * 
	 * @param productData
	 *            Product dragged into the target product container
	 * @param containerData
	 *            Target product container
	 */
	@Override
	public void addProductToContainer(
			ProductData productData,
			ProductContainerData containerData) {

		ProductManager pm = ProductManager
				.getInstance();
		ItemManager im = ItemManager
				.getInstance();
		Product product = (Product) getView()
				.getSelectedProduct().getTag();

		ProductContainer targetContainer = (ProductContainer) containerData
				.getTag();
		// ProductContainer otherContainerContainingProduct =
		// targetContainer.getStorageUnit().findProductContainer(product.getBarcode());

		targetContainer.addProduct(product);// check this
		ProductContainer oldContainer = targetContainer
				.getStorageUnit()
				.findProductContainer(
						product.getBarcode());// null;
		//
		// Iterator<ProductContainer> containerIterator =
		// product.getContainers();
		// while(containerIterator.hasNext()) {
		// ProductContainer cur = containerIterator.next();
		// if(cur.getStorageUnit().equals(targetContainer.getStorageUnit())) {
		// oldContainer = cur;
		// break;
		// }
		// }

		pm.addToContainer(product,
				targetContainer);

		if (oldContainer != null) {
			// moving product within the same storage unit
			Set<Item> itemsToMove = ItemManager
					.getInstance()
					.getItemsByContainerAndProduct(
							oldContainer,
							product.getBarcode());

			im.productMoveItems(itemsToMove,
					targetContainer, oldContainer);

			pm.deleteFromContainer(product,
					oldContainer);
			System.out.println("huh!?");
		}

	}

	/**
	 * This method is called when the user drags an item into a product
	 * container.
	 * 
	 * @pre canMoveItem returned true, itemData != null, containerData != null
	 * @post the item is moved from it's current container into the new
	 *       container in the model and the view is updated.
	 * 
	 * @param itemData
	 *            Item dragged into the target product container
	 * @param containerData
	 *            Target product container
	 */
	@Override
	public void moveItemToContainer(
			ItemData itemData,
			ProductContainerData containerData) {
		Item toMove = (Item) getView()
				.getSelectedItem().getTag();
		ProductContainer container = (ProductContainer) containerData
				.getTag();
		ItemManager.getInstance().move(toMove,
				container);
		ItemManager.getInstance()
				.notifyObservers(
						new ItemNotifier());
	}

	/**
	 * Notifies this controller that updates are needed. Controller then pulls
	 * the updates from the observable.
	 * 
	 * @pre An observed class called the Notify method. observable != null
	 * @post The view is updated according to the model.
	 * 
	 * @param observable
	 *            Object that update is getting called from.
	 * @param obj
	 *            not necessary since we're using pull, just keep as null.
	 */
	@Override
	public void update(Observable observable,
			Object obj) {
		Notifier notifier = (Notifier) obj;
		notifier.performAction(getView(),
				this.rootData);
	}

}
