package model.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.unit.Unit;

/**
 * Holds the information of a product.
 * 
 * @author Chris McNeill
 */
public class Product implements Serializable,
		Comparable<Object> {
	private int threeMonthSupply;

	private Date creationDate;

	private ProductBarcode barcode;

	private String description;

	private Unit size;

	private int shelfLife;

	private HashSet<ProductContainer> containers;

	/**
	 * Constructs a new Product according to the parameters.
	 * 
	 * @pre Product's isValid returned true of the parameters
	 * @post A valid Product object
	 * 
	 * @param description
	 *            The description of the Product
	 * @param size
	 *            The size of the Product
	 * @param shelfLife
	 *            The shelf life of the Product
	 * @param threeMonthSupply
	 *            The three-month supply of the Product
	 */
	public Product(ProductBarcode barcode,
			String description, Unit size,
			int shelfLife, int threeMonthSupply,
			ProductContainer cont) {
		this.barcode = barcode;
		this.description = description;
		this.size = size;
		this.shelfLife = shelfLife;
		this.threeMonthSupply = threeMonthSupply;
		this.creationDate = new Date();
		this.containers = new HashSet<ProductContainer>();
		this.containers.add(cont);
	}

	/**
	 * Edits the Product object so that it contains the parameters passed in
	 * 
	 * @pre Product's isValid returned true for the parameters
	 * @post A valid Product object whose information has been changed to the
	 *       new parameters.
	 * 
	 * @param newDescription
	 *            The description of the Product
	 * @param newSize
	 *            The description of the Product
	 * @param newShelfLife
	 *            The shelf life of the Product
	 * @param newThreeMonthSupply
	 *            The three-month supply of the Product
	 */
	public void edit(String newDescription,
			Unit newSize, int newShelfLife,
			int newThreeMonthSupply) {
		assert (Product.isValid(this.barcode,
				newDescription, newShelfLife,
				newThreeMonthSupply));
		this.description = newDescription;
		this.size = newSize;
		this.shelfLife = newShelfLife;
		this.threeMonthSupply = newThreeMonthSupply;
	}

	/**
	 * Checks to see if the user input for a Product is valid.
	 * 
	 * @pre True
	 * @post barcode is not null and is a valid ProductBarcode and is unique to
	 *       the Product
	 * @post description is not null and is not an empty string
	 * @post shelfLife >= 0
	 * @post threeMonthSupply >= 0
	 * 
	 * @param barcode
	 *            The Product's barcode.
	 * @param description
	 *            The Product's description.
	 * @param shelfLife
	 *            The Product's shelf life.
	 * @param threeMonthSupply
	 *            The Product's three-month supply.
	 * @return True if the parameters are valid for a Product, false otherwise.
	 */
	public static boolean isValid(
			ProductBarcode barcode,
			String description, int shelfLife,
			int threeMonthSupply) {
		boolean ret = true;
		if (barcode == null) {
			ret = false;
		} else if ((description == null)
				|| (description.compareTo("") == 0)) {
			// description
			// validity
			ret = false;
		} else if (shelfLife < 0) {
			ret = false;
		} else if (threeMonthSupply < 0) {
			ret = false;
		}
		return ret;
	}

	/**
	 * Returns the Product's three-month supply amount.
	 * 
	 * @pre True
	 * @post True
	 * 
	 * @return The Product's three-month supply amount.
	 */
	public int getThreeMonthSupply() {
		return this.threeMonthSupply;
	}

	/**
	 * Returns the Product's creation date.
	 * 
	 * @pre True
	 * @post True
	 * 
	 * @return The Product's creation date.
	 */
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Returns the Product's barcode.
	 * 
	 * @pre True
	 * @post True
	 * @return The Product's barcode.
	 */
	public ProductBarcode getBarcode() {
		return this.barcode;
	}

	/**
	 * Returns the Product's description.
	 * 
	 * @pre True
	 * @post True
	 * 
	 * @return The Product's description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the Product's size.
	 * 
	 * @pre True
	 * @post True
	 * @return The Product's size.
	 */
	public Unit getSize() {
		return this.size;
	}

	/**
	 * Returns the Product's shelf life.
	 * 
	 * @pre True
	 * @post True
	 * 
	 * @return The Product's shelf life.
	 */
	public int getShelfLife() {
		return this.shelfLife;
	}

	/**
	 * Returns an Iterator of the Product's containers.
	 * 
	 * @pre True
	 * @post True
	 * 
	 * @return The Iterator of the Product's containers.
	 */
	public Iterator<ProductContainer> getContainers() {
		return this.containers.iterator();
	}

	/**
	 * Removes container from the Product's containers.
	 * 
	 * @pre True
	 * @post container is removed from containers
	 * 
	 * @param container
	 *            ProductContainer to remove
	 */
	public void removeFromContainer(
			ProductContainer container) {
		assert (container != null);
		Iterator<ProductContainer> itr = this.containers
				.iterator();
		HashSet<ProductContainer> newSet = new HashSet<ProductContainer>();
		while (itr.hasNext()) {
			ProductContainer cont = itr.next();
			if (!cont.equals(container)) {
				newSet.add(cont);
			}
		}
		this.containers = newSet;
	}

	/**
	 * Adds container to the Product's containers.
	 * 
	 * @pre True
	 * @post container is added to containers
	 * 
	 * @param container
	 *            ProductContainer to add
	 */
	public void addToContainer(
			ProductContainer container) {
		assert (container != null);
		this.containers.add(container);
	}

	public boolean productInContainer(
			ProductContainer container) {
		assert container != null;
		return this.containers
				.contains(container);
	}

	@Override
	public int hashCode() {
		// final int prime = 31;
		// int result = 1;
		// result = prime * result + ((barcode == null) ? 0 :
		// barcode.hashCode());
		// result = prime * result
		// + ((containers == null) ? 0 : containers.hashCode());
		// result = prime * result
		// + ((creationDate == null) ? 0 : creationDate.hashCode());
		// result = prime * result
		// + ((description == null) ? 0 : description.hashCode());
		// result = prime * result + shelfLife;
		// result = prime * result + ((size == null) ? 0 : size.hashCode());
		// result = prime * result + threeMonthSupply;
		// return result;
		return this.barcode.hashCode();
	}

	/**
	 * Equals based on compareTo. Only equal if barcodes are same.
	 */

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		// if (this.barcode != null) {
		// return this.barcode.equals(other.barcode);
		// }
		// if (this.barcode == null && other.barcode == null) {
		// assert false : "both product barcodes are null...";
		// return false;
		// }
		return hashCode() == other.hashCode();
	}

	@Override
	public String toString() {
		return "{" + "Barcode: "
				+ this.barcode.toString()
				+ "Description:"
				+ this.description
				+ "Containers:"
				+ this.containers.toString()
				+ "}";
	}

	@Override
	public int compareTo(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != Product.class)) {
			return -1;
		}
		Product other = (Product) obj;
		// return this.barcode.compareTo(other.barcode);
		return this.description
				.compareTo(other.description);
	}
}