package model.hit;

import java.io.File;
import java.util.Date;

import model.managers.ItemManager;
import model.managers.ProductGroupManager;
import model.managers.ProductManager;
import model.managers.SerializationManager;
import model.managers.SessionManager;
import model.managers.StorageUnitManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ItemBarcode;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.ProductGroup;
import model.models.productContainer.StorageUnit;
import model.models.unit.Unit;
import model.models.unit.UnitType;

/**
 * The main interface for the use of the Home Inventory Tracker system. Provides
 * functionality for adding, editing, removing, and/or moving for items,
 * products, product groups, and storage units.
 * 
 * @author Group1
 */
public class HomeInventoryTracker {

	private static HomeInventoryTracker homeInventoryTracker = null;
	private ItemManager itemM;
	private ProductGroupManager productGroupM;
	private ProductManager productM;
	private StorageUnitManager storageUnitM;
	private SessionManager sessionM;

	private HomeInventoryTracker() {
		this.itemM = ItemManager.getInstance();
		this.productGroupM = ProductGroupManager
				.getInstance();
		this.productM = ProductManager
				.getInstance();
		this.storageUnitM = StorageUnitManager
				.getInstance();

		this.sessionM = SerializationManager
				.getInstance();
		this.sessionM.loadState();

	}

	/**
	 * @pre true
	 * @post HomeInventoryTracker object returned with state loaded
	 * @return the Singleton Home Inventory Tracker
	 */
	public static HomeInventoryTracker getInstance() {
		if (homeInventoryTracker == null) {
			homeInventoryTracker = new HomeInventoryTracker();
		}
		return homeInventoryTracker;
	}

	// StorageUnit stuff
	/**
	 * Adds a storage unit if the storage unit does not already exists
	 * 
	 * @pre true
	 * @post new storage unit added if valid.
	 * @param name
	 * @throws InvalidStorageUnitException
	 */
	public void addStorageUnit(String name)
			throws InvalidStorageUnitException {
		if (!this.storageUnitM.isValid(name)) {
			throw new InvalidStorageUnitException(
					"StorageUnit name "
							+ name
							+ " is not valid or non-unique");
		}
		this.storageUnitM.add(new StorageUnit(
				name));
	}

	/**
	 * Changes a storage unit's name if the new name is valid and is not already
	 * a storage unit
	 * 
	 * @pre true
	 * @post true
	 * @param oldName
	 * @param newName
	 * @throws InvalidStorageUnitException
	 */
	public void editStorageUnit(String oldName,
			String newName)
			throws InvalidStorageUnitException {
		// check existence of old storage unit
		if (!this.storageUnitM.contains(oldName)) {
			throw new InvalidStorageUnitException(
					"StorageUnit name " + newName
							+ " does not exist");
		}
		if (!StorageUnit.isValid(newName)) {
			throw new InvalidStorageUnitException(
					"Invalid storage unit '"
							+ newName + "'");
		}
		this.storageUnitM.edit(oldName, newName);
	}

	/**
	 * Deletes a storage unit if the storage unit exists. If it doesn't exist,
	 * there is no effect.
	 * 
	 * @pre true
	 * @post storage unit deleted
	 * @param name
	 * @throws InvalidStorageUnitException
	 */
	public void deleteStorageUnit(String name)
			throws InvalidStorageUnitException {
		if (!this.storageUnitM.contains(name)
				|| !StorageUnit.isValid(name)) {
			throw new InvalidStorageUnitException(
					"Storage unit " + name
							+ " does not exist");
		}
		this.storageUnitM.delete(name);
	}

	/**
	 * Checks validity of a storage unit name
	 * 
	 * @pre none
	 * @post storage unit name validity returned
	 * @param name
	 * @return true if a valid storage unit name, false otherwise
	 */
	public boolean isValidStorageUnit(String name) {
		return this.storageUnitM.isValid(name);
	}

	// Item stuff
	/**
	 * Adds Item if valid and unique
	 * 
	 * @pre none
	 * @post item added if valid
	 * @param barcode
	 * @param entryDate
	 * @param storageUnit
	 *            should always be of type StorageUnit when the programs
	 *            running. For testing we need it to allow ProductGroups too
	 *            though.
	 * @return the item created
	 * @throws InvalidItemException
	 *             if not valid or not unique
	 */
	public void addItem(
			ProductContainer storageUnit,
			Product product, ItemBarcode barcode,
			Date entryDate)
			throws InvalidItemException {

		if ((barcode == null)
				|| (entryDate == null)) {
			throw new InvalidItemException(
					"Null input");
		}
		if (!this.itemM.isValid(barcode,
				entryDate)) {
			throw new InvalidItemException(
					"Invalid barcode or entryDate");
		}

		Item item = new Item(barcode, product,
				entryDate, storageUnit);
		this.itemM.add(item);
	}

	/**
	 * Consumes an item (i.e. remove from storage or use) if exists
	 * 
	 * @pre none
	 * @post item is marked as consumed if exists
	 * @param item
	 * @throws InvalidItemException
	 *             if not valid
	 */
	public void consumeItem(Item item)
			throws InvalidItemException {
		if (item == null) {
			throw new InvalidItemException(
					"null item to consume");
		}
		if (!this.itemM.itemExists(item
				.getItemBarcode())) {
			throw new InvalidItemException(
					"bad item to consume");
		}
		this.itemM.consume(item);
	}

	/**
	 * Moves an item to the specified productContainer
	 * 
	 * @pre none
	 * @param item
	 * @param destinationContainer
	 * @throws InvalidItemException
	 *             if item is invalid
	 */
	public void moveItem(Item item,
			ProductContainer destination) {
		if ((item != null)
				&& (destination != null)
				&& this.itemM.itemExists(item
						.getItemBarcode())) {
			this.itemM.move(item, destination);
		}
	}

	/**
	 * Checks validity for a new Item
	 * 
	 * @param barcode
	 * @param entryDate
	 * @return true if valid, false otherwise
	 */
	public boolean isValidItem(
			ItemBarcode barcode, Date entryDate) {
		return this.itemM.isValid(barcode,
				entryDate);
	}

	// Serialization stuff
	/**
	 * Saves the state of the HomeInventoryTracker including its members
	 */
	public void saveState() {
		if (this.sessionM == null) {
			throw new NullPointerException();
		}
		this.sessionM.saveState();
	}

	// Product stuff
	/**
	 * Adds a new product to HIT if the new product is unique and exists
	 * 
	 * @param description
	 * @param unit
	 * @param shelfLife
	 * @param threeMonthSupply
	 * @throws InvalidProductException
	 * @throws InvalidBarcodeException
	 * @throws InvalidUnitException
	 */
	public void addProduct(String barcode,
			String description,
			UnitType unitType, int shelfLife,
			int threeMonthSupply, float amount,
			ProductContainer cont)
			throws InvalidProductException,
			InvalidBarcodeException,
			InvalidUnitException {

		if (!ProductBarcode.isValid(barcode)) {

			throw new InvalidBarcodeException(
					barcode);
		}
		ProductBarcode pBarcode = new ProductBarcode(
				barcode);
		if (!Unit.isValid(amount, unitType)) {
			throw new InvalidUnitException(
					unitType + " " + amount);
		}
		Unit unit = new Unit(amount, unitType);

		if (!this.productM.isValid(pBarcode,
				description, shelfLife,
				threeMonthSupply)) {
			throw new InvalidProductException(
					"Product "
							+ barcode
							+ " is not valid or not unique");
		}
		Product product = new Product(pBarcode,
				description, unit,
				threeMonthSupply,
				threeMonthSupply, cont);

		this.productM.add(product);
	}

	/**
	 * Deletes a product if exists. Does nothing if product does not exists
	 * 
	 * @param poductToDelete
	 */
	public void deleteProduct(
			Product productToDelete) {
		if ((productToDelete == null)
				|| !this.productM
						.productExists(productToDelete
								.getBarcode())) {
			return;
		} else {
			this.productM.delete(productToDelete);
		}
	}

	/**
	 * Checks validity of product parameters
	 * 
	 * @param description
	 * @param unit
	 * @param shelfLife
	 * @param threeMonthSupply
	 * @return true if valid, false otherwise
	 * @throws InvalidProductException
	 */
	public boolean isValidProduct(String barcode,
			String description, Unit unit,
			int shelfLife, int threeMonthSupply)
			throws InvalidBarcodeException {
		if (!ProductBarcode.isValid(barcode)) {
			return false;
		}
		if ((unit == null)
				|| !Unit.isValid(
						unit.getAmount(),
						unit.getUnit())) {
			return false;
		}
		ProductBarcode pBarcode = new ProductBarcode(
				barcode);
		return this.productM.isValid(pBarcode,
				description, shelfLife,
				threeMonthSupply);
	}

	/**
	 * Checks if a product exists already
	 * 
	 * @param barcode
	 * @return true if exists, false otherwise
	 */
	public boolean productExists(
			ProductBarcode barcode) {
		if (barcode == null) {
			return false;
		}
		return this.productM
				.productExists(barcode);
	}

	// ProductGroup stuff
	/**
	 * Adds a product group if unique and parameters are valid
	 * 
	 * @param name
	 * @param threeMonthSupply
	 * @throws InvalidProductGroupException
	 * @throws InvalidUnitException
	 */
	public void addProductGroup(String name,
			ProductContainer parent,
			float threeMonthSupply,
			UnitType unitType)
			throws InvalidProductGroupException,
			InvalidUnitException {

		if (!Unit.isValid(threeMonthSupply,
				unitType)) {
			throw new InvalidUnitException(
					threeMonthSupply + " "
							+ unitType);
		}

		Unit unit = new Unit(threeMonthSupply,
				unitType);
		if (!this.productGroupM.isValid(name,
				parent, unit)) {
			throw new InvalidProductGroupException(
					name);
		}

		ProductGroup pg = new ProductGroup(name,
				parent, unit);
		this.productGroupM.add(pg);
	}

	/**
	 * Deletes a Product Group if exists and is empty. Does nothing otherwise.
	 * 
	 * @param deleteGroup
	 */
	public void deleteProductGroup(
			ProductGroup groupToDelete) {
		this.productGroupM.delete(groupToDelete);
	}

	/**
	 * Edits a product group, changing its name and three month supply
	 * 
	 * @param newName
	 * @param threeMonthSupply
	 * @throws InvalidProductGroupException
	 * @throws InvalidUnitException
	 */
	public void editProductGroup(
			ProductGroup group, String newName,
			float threeMonthSupply,
			UnitType unitType)
			throws InvalidProductGroupException,
			InvalidUnitException {

		if (!Unit.isValid(threeMonthSupply,
				unitType)) {
			throw new InvalidUnitException(
					threeMonthSupply + " "
							+ unitType);
		}

		Unit unit = new Unit(threeMonthSupply,
				unitType);

		if (!ProductContainer.isValid(newName)) {
			throw new InvalidProductGroupException(
					newName
							+ " with three month supply "
							+ threeMonthSupply);
		}
		this.productGroupM.edit(group, newName,
				unit);
	}

	/**
	 * Checks validity of product group
	 * 
	 * @param name
	 * @param threeMonthSupply
	 * @return true if valid, false otherwise
	 */
	public boolean isValidProductGroup(
			String name, ProductGroup parent,
			UnitType unitType, float amount) {

		if (!Unit.isValid(amount, unitType)) {
			return false;
		}
		Unit unit = new Unit(amount, unitType);
		return this.productGroupM.isValid(name,
				parent, unit);
	}

	public void clear() {
		this.itemM.clear();
		// productM.clear();
		this.productGroupM.clear();
		this.storageUnitM.clear();
	}

	public static void deleteAllDataFiles() {
		File dir = new File("data");
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int c = 0; c < files.length; c++) {
				if (files[c].getName().endsWith(
						".data")) {
					files[c].delete();
				}
			}
		}

	}

	/**
	 * Might need to add stuff to this.
	 * 
	 * @param i1
	 * @param su1
	 * @throws InvalidItemException
	 */
	public void transferItem(Item i1,
			StorageUnit su)
			throws InvalidItemException {
		// if(!Item.isValid(i1.getItemBarcode(), i1.getEntryDate())) {
		// throw new InvalidItemException("Invalid item " + i1);
		// }
		this.itemM.transferItem(i1, su);
	}

}