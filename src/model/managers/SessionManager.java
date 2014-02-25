package model.managers;

import java.util.List;

import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.ProductGroup;
import model.models.productContainer.StorageUnit;

public abstract class SessionManager {

	private static SessionManager singleton;

	public static SessionManager getInstance() {
		if (singleton == null) {
			singleton = SerializationManager
					.getInstance();
		}
		return singleton;
	}

	// so that you can't instantiate SessionManager
	protected SessionManager() {
	}

	public abstract void loadState();

	public abstract void saveState(
			List<Saveable> managers);

	public abstract void loadState(
			List<Saveable> managers);

	public abstract void saveState();

	// Storage Units
	public abstract void addStorageUnit(
			StorageUnit storageUnit);

	public abstract void editStorageUnit(
			StorageUnit oldStorageUnit,
			StorageUnit newStorageUnit); // lookup by name

	public abstract void deleteStorageUnit(
			StorageUnit storageUnit);

	// Items - by item or item barcode?
	public abstract void addItem(Item item);

	public abstract void consumeItem(Item item);

	public abstract void moveItem(Item item,
			ProductContainer destination);

	// Products
	public abstract void addProduct(
			Product product);

	public abstract void editProduct(
			Product newProduct);

	// lookup in database by product barcode this requires us to be able to
	// access Product.barcode...

	public abstract void deleteProduct(
			ProductBarcode productBarcode); // delete
											// by
	// product
	// barcode

	// ProductGroup
	public abstract void addProductGroup(
			ProductGroup productGroup);

	public abstract void editProductGroup(
			String oldName,
			ProductContainer parent); // lookup
	// by
	// parent
	// and
	// old
	// name?

	public abstract void deleteProductGroup(
			ProductGroup productGroup);

	public abstract void deleteDataFiles();
}
