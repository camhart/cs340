package gui.item;

import gui.common.Tagable;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import model.models.Item;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.StorageUnit;

/**
 * Display data class for items.
 */
public class ItemData extends Tagable {

	/**
	 * EntryDate attribute.
	 */
	private Date _entryDate;

	/**
	 * ExpirationDate attribute.
	 */
	private Date _expirationDate;

	/**
	 * Barcode attribute.
	 */
	private String _barcode;

	/**
	 * StorageUnit attribute.
	 */
	private String _storageUnit;

	/**
	 * ProductGroup attribute.
	 */
	private String _productGroup;

	/**
	 * Constructor.
	 * 
	 * {@pre None}
	 * 
	 * {@post getEntryDate() == current date/time} {@post getExpirationDate() ==
	 * current date/time} {@post getBarcode() == ""} {@post getStorageUnit() ==
	 * ""} {@post getProductGroup() == ""}
	 */
	public ItemData() {
		this._entryDate = new Date();
		this._expirationDate = new Date();
		this._barcode = "";
		this._storageUnit = "";
		this._productGroup = "";
	}

	/**
	 * Returns the value of the Barcode attribute.
	 * 
	 * @pre True
	 * @post Returns current value of barcode attribute.
	 */
	public String getBarcode() {
		return this._barcode;
	}

	/**
	 * Sets the value of the Barcode attribute.
	 * 
	 * @param barcode
	 *            New Barcode value
	 * 
	 *            {@pre barcode != null}
	 * 
	 *            {@post getBarcode() == barcode}
	 */
	public void setBarcode(String barcode) {
		this._barcode = barcode;
	}

	/**
	 * Returns the value of the EntryDate attribute.
	 * 
	 * @pre True
	 * @post Returns current value of entry date attribute.
	 */
	public Date getEntryDate() {
		return this._entryDate;
	}

	/**
	 * Sets the value of the EntryDate attribute.
	 * 
	 * @param entryDate
	 *            New EntryDate value
	 * 
	 *            {@pre entryDate != null}
	 * 
	 *            {@post getEntryDate() == entryDate}
	 */
	public void setEntryDate(Date entryDate) {
		this._entryDate = entryDate;
	}

	/**
	 * Returns the value of the ExpirationDate attribute.
	 * 
	 * @pre True
	 * @post Returns current value of expiration date attribute.
	 */
	public Date getExpirationDate() {
		return this._expirationDate;
	}

	/**
	 * Sets the value of the ExpirationDate attribute.
	 * 
	 * @param expirationDate
	 *            New ExpirationDate value
	 * 
	 *            {@pre None}
	 * 
	 *            {@post getExpirationDate() == expirationDate}
	 */
	public void setExpirationDate(
			Date expirationDate) {
		this._expirationDate = expirationDate;
	}

	/**
	 * Returns the value of the StorageUnit attribute.
	 * 
	 * @pre True
	 * @post Returns current value of storage unit attribute.
	 */
	public String getStorageUnit() {
		return this._storageUnit;
	}

	/**
	 * Sets the value of the StorageUnit attribute.
	 * 
	 * @param storageUnit
	 *            New StorageUnit value
	 * 
	 *            {@pre storageUnit != null}
	 * 
	 *            {@post getStorageUnit() == storageUnit}
	 */
	public void setStorageUnit(String storageUnit) {
		this._storageUnit = storageUnit;
	}

	/**
	 * Returns the value of the ProductGroup attribute.
	 * 
	 * @pre True
	 * @post Returns current value of product group attribute.
	 */
	public String getProductGroup() {
		return this._productGroup;
	}

	/**
	 * Sets the value of the ProductGroup attribute.
	 * 
	 * @param productGroup
	 *            New ProductGroup value
	 * 
	 *            {@pre productGroup != null}
	 * 
	 *            {@post getProductGroup() == productGroup}
	 */
	public void setProductGroup(
			String productGroup) {
		this._productGroup = productGroup;
	}

	/**
	 * Converts an array of items to an array of ItemData
	 * 
	 * @pre items not null, and sorted
	 * @post ItemData[] returned sorted the same way items was handed in.
	 * @param items
	 * @return array of ItemData
	 */
	public static ItemData[] convertToItemData(
			Item[] items) {
		assert items != null : "items is null in convertToItemData in ItemData.java";
		ItemData[] ret = new ItemData[items.length];
		for (int c = 0; c < items.length; c++) {
			ret[c] = convertToItemData(items[c]);
		}
		return ret;
	}

	/**
	 * Converts an array of items to an array of ItemData
	 * 
	 * @pre items not null, and sorted
	 * @post ItemData[] returned sorted the same way items was handed in.
	 * @param items
	 * @return array of ItemData
	 */
	public static ItemData[] convertToItemData(
			Collection<Item> items) {
		assert items != null : "items is null in convertToItemData in ItemData.java";
		ItemData[] ret = new ItemData[items
				.size()];
		Iterator<Item> iter = items.iterator();
		int c = 0;
		while (iter.hasNext()) {
			ret[c++] = convertToItemData(iter
					.next());
		}
		return ret;
	}

	/**
	 * Converts Item to ItemData object
	 * 
	 * @pre item is not null
	 * @param item
	 * @return ItemData
	 */
	public static ItemData convertToItemData(
			Item item) {
		assert item != null : "item is null in convertToItemData in ItemData.java";
		ItemData ret = new ItemData();
		ret._barcode = item.getItemBarcode()
				.toString();
		ret._entryDate = item.getEntryDate();
		ret._expirationDate = item
				.getExpirationDate();
		if (item.getContainer() == ProductContainer.CONSUMED_ITEMS) {
			ret._productGroup = item
					.getContainerPriorToConsume()
					.getClass() == StorageUnit.class ? ""
					: item.getContainerPriorToConsume()
							.getName();
			ret._storageUnit = (item
					.getContainerPriorToConsume())
					.getStorageUnit().getName();
		} else {
			ret._productGroup = item
					.getContainer().getClass() == StorageUnit.class ? ""
					: item.getContainer()
							.getName();
			ret._storageUnit = (item
					.getContainer())
					.getStorageUnit().getName();
		}
		ret.setTag(item);
		return ret;
	}

}
