package model.models;

import java.io.Serializable;
import java.util.Date;

import model.models.barcode.ItemBarcode;
import model.models.productContainer.ProductContainer;

/**
 * The model class for items
 * 
 * @author Joel
 */
public class Item implements Serializable,
		Comparable<Object> {

	private Product product;

	private ItemBarcode barcode;

	private Date entryDate, exitTime,
			expirationDate; // custom DateTime class?

	private ProductContainer container;
	private ProductContainer containerPriorToConsume;

	/**
	 * @pre barcode is a valid itembarcode
	 * @pre product is a valid product
	 * @pre entryDate is not null
	 * @pre product container is a valid product container
	 * @param barcode
	 * @param product
	 * @param entryDate
	 * @param container
	 */
	@SuppressWarnings("deprecation")
	public Item(ItemBarcode barcode,
			Product product, Date entryDate,
			ProductContainer container) {
		assert Item.isValid(barcode, entryDate);
		assert Product.isValid(
				product.getBarcode(),
				product.getDescription(),
				product.getShelfLife(),
				product.getThreeMonthSupply());
		assert ProductContainer.isValid(container
				.getName());

		this.barcode = barcode;
		this.product = product;
		this.entryDate = entryDate;
		this.container = container;

		if (this.product.getShelfLife() > 0) {
			this.expirationDate = new Date();
			this.expirationDate
					.setHours(entryDate
							.getHours());
			this.expirationDate
					.setMinutes(entryDate
							.getMinutes());
			this.expirationDate
					.setSeconds(entryDate
							.getSeconds());

			this.expirationDate
					.setYear(entryDate.getYear()
							+ (this.product
									.getShelfLife() / 12));
			this.expirationDate
					.setMonth(entryDate
							.getMonth()
							+ (this.product
									.getShelfLife() % 12));
		}
	}

	// public Item(ProductContainer productContainer, Product product,
	// ItemBarcode barcode, String description, Date entryDate, Date
	// expirationDate) {
	// this.container = productContainer;
	// this.product = product;
	// this.barcode = barcode;
	// this.description = description;
	// this.entryDate = entryDate;
	// this.expirationDate = expirationDate;
	// }

	/**
	 * Checks if the parameters are valid for a new Item. Valid parameters -
	 * valid upc barcode - entryDate > Jan 1st 2000, entryDate < current time
	 * 
	 * @pre true
	 * @post true
	 * @param barcode
	 * @param entryDate
	 * @return true if valid, false otherwise
	 */
	public static boolean isValid(
			ItemBarcode barcode, Date entryDate) {
		Date currentDate = new Date(
				System.currentTimeMillis() + 10);

		if ((entryDate != null)
				&& (barcode != null)
				&& ItemBarcode.isValid(barcode)
				&& entryDate.after(new Date(
						2000 - 1900, 1, 1, 0, 0))
				&& entryDate.before(currentDate)) {
			return true;
		}
		return false;
	}

	private ItemBarcode getBarcode() {
		return this.barcode;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	private Date getExitTime() {
		return this.exitTime;
	}

	public Date getExpirationDate() {
		return this.expirationDate;
	}

	public ItemBarcode getItemBarcode() {
		return this.barcode;
	}

	/**
	 * Sets this item's barcode
	 * 
	 * @pre itembarcode is a valid item barcode
	 * @post true
	 * @param itemBarcode
	 */
	public void setItemBarcode(
			ItemBarcode itemBarcode) {
		assert ItemBarcode.isValid(itemBarcode);
		this.barcode = itemBarcode;
	}

	/**
	 * @pre true
	 * @post true
	 * @return this items product container
	 */
	public ProductContainer getContainer() {
		return this.container;
	}

	public ProductContainer getContainerPriorToConsume() {
		return this.containerPriorToConsume;
	}

	/**
	 * @pre productContainer is not null and it exists
	 * @pre ItemManager needs to make changes to productContainerMap prior to
	 *      this method being called
	 * @post product container gets changed
	 * @param productContainer
	 */
	public void setProductContainer(
			ProductContainer productContainer) {
		assert productContainer != null;
		// assert StorageUnitManager.getInstance().

		this.container = productContainer;
	}

	public Product getProduct() {
		return this.product;
	}

	/**
	 * Item gets consumed
	 * 
	 * @pre ItemManager needs to make changes to productContainerMap prior to
	 *      this being called
	 * @post item is consumed, exitTime set to the current date and time, item
	 *       product container set to null.
	 */
	public void consume() {
		this.exitTime = new Date();
		this.containerPriorToConsume = this.container;
		this.container = ProductContainer.CONSUMED_ITEMS;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return this.barcode.hashCode();
	}

	/**
	 * Equals based on compareTo. Only equal if entry/exit times are same exact.
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != Item.class)) {
			return false;
		}
		Item newItem = (Item) obj;
		if (this.barcode.equals(newItem.barcode)) {
			return true;
		}

		if (newItem.exitTime != null) {
			if (this.exitTime == null) {
				return false;
			} else {
				return this.exitTime
						.equals(newItem.exitTime);
			}
		} else if (this.exitTime != null) {
			return false;
		}
		return this.entryDate
				.equals(newItem.entryDate);
	}

	/**
	 * Compares this object to another.
	 * 
	 * If both are Items, it compares exit times (if both have, if one has and
	 * the other doesn't then it puts the exit time one towards the end of the
	 * list). Otherwise the one with the earlier date goes in front. If the
	 * dates and times are the same, then it orders by barcode.
	 */
	@Override
	public int compareTo(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != Item.class)) {
			return -1;
		}
		Item newItem = (Item) obj;

		// assert !newItem.barcode.equals(barcode); //this should never happen

		if (newItem.exitTime != null) {
			if (this.exitTime == null) {
				return -1;
			} else {
				int dateStamp = this.exitTime
						.compareTo(newItem.exitTime);
				if (dateStamp == 0) {
					return this.barcode
							.toString()
							.compareTo(
									newItem.barcode
											.toString());
				} else {
					return dateStamp;
				}
			}
		} else if (this.exitTime != null) {
			return 1;
		}
		int dateStamp = this.entryDate
				.compareTo(newItem.entryDate);
		if (dateStamp == 0) {
			return this.barcode.toString()
					.compareTo(
							newItem.barcode
									.toString());
		}
		return dateStamp;
	}

	@Override
	public String toString() {
		return this.barcode.toString();
	}

	public String getDescription() {
		return getProduct().getDescription();
	}

	/**
	 * Changes the items entryDate and updates the expiration date
	 * 
	 * @pre entryDate != null
	 * @post entryDate and expirationDate adjusted accordingly.
	 * @param entryDate
	 */
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
		if (this.product.getShelfLife() > 0) {
			this.expirationDate = new Date();
			this.expirationDate
					.setHours(entryDate
							.getHours());
			this.expirationDate
					.setMinutes(entryDate
							.getMinutes());
			this.expirationDate
					.setSeconds(entryDate
							.getSeconds());

			this.expirationDate
					.setYear(entryDate.getYear()
							+ (this.product
									.getShelfLife() / 12));
			this.expirationDate
					.setMonth(entryDate
							.getMonth()
							+ (this.product
									.getShelfLife() % 12));
		}
	}
}