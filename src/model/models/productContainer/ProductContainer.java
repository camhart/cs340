package model.models.productContainer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;

/**
 * The parent class of ProductGroup and StorageUnit.
 * 
 * @author Chris McNeill
 * @invariant productGroups.size() >= 0
 * @invariant items.size() >= 0
 * @invariant products.size() >= 0
 * */
public abstract class ProductContainer implements
		Serializable {

	public final static ProductContainer CONSUMED_ITEMS = null;

	protected String name;

	protected List<ProductGroup> productGroups;

	protected List<Product> products;

	protected List<Item> items;

	protected ProductContainer(String name) {
		this.name = name;
		this.productGroups = new LinkedList<ProductGroup>();
		this.items = new LinkedList<Item>();
		this.products = new LinkedList<Product>();
	}

	/**
	 * Adds a new PorductGroup the this ProductContainer.
	 * 
	 * @pre toAdd has been validated with ProductGroup's isValid method.
	 * @pre toAdd is not contained within this ProductContainer.
	 * @post toAdd is contained within this ProductContainer.
	 * 
	 * @param toAdd
	 *            The ProductGroup to be added.
	 */
	public void addProductGroup(ProductGroup toAdd) {
		this.productGroups.add(toAdd);
	}

	/**
	 * Deletes the ProductGroup specified by the productGroup parameter.
	 * 
	 * @pre toDelete is contained within this ProductContainer.
	 * @post toDelete is not contained within this ProductContainer.
	 * 
	 * @param productGroup
	 *            The ProductGroup to be deleted. Must be empty of all Items.
	 * @return True if removal was successful, false otherwise.
	 */
	public boolean deleteProductGroup(
			ProductGroup toDelete) {
		boolean success = this.productGroups
				.remove(toDelete);

		return success;
	}

	/**
	 * Adds an Item/Items to the ProductContainer.
	 * 
	 * @pre toAdd has been validated with Item's isValid method
	 * @pre toAdd is not contained within this ProductContainer.
	 * @post toAdd is contained within this ProductContainer.
	 * 
	 * @param toAdd
	 *            The item being added to this ProductContainer.
	 */
	public void addItem(Item toAdd) {
		this.items.add(toAdd);
	}

	/**
	 * Removes an Item from this ProductContainer.
	 * 
	 * @pre toRemove is contained within this ProductContainer.
	 * @post toRemove is not contained within this ProductContainer.
	 * 
	 * @param toRemove
	 *            The Item being removed.
	 * @return True if removal was successful, false otherwise.
	 */
	public boolean removeItem(Item toRemove) {
		boolean success = this.items
				.remove(toRemove);

		return success;
	}

	/**
	 * Adds a new Product to the ProductContainer.
	 * 
	 * @pre toAdd was validated by Product's isValid method.
	 * @pre toAdd is not contained within this ProductContainer.
	 * 
	 * @post toAdd is contained within this ProductContainer.
	 * @param toAdd
	 *            The Product being added to this ProductContainer.
	 */
	public void addProduct(Product toAdd) {
		this.products.add(toAdd);
	}

	/**
	 * Removes the Product from the ProductContainer
	 * 
	 * @pre toRemove is contained within this ProductContainer.
	 * @post toRemove is not contained within this ProductContainer.
	 * 
	 * @param toRemove
	 *            The Product to be removed.
	 * @return True is removal was successful, false otherwise.
	 */
	public boolean removeProduct(Product toRemove) {
		boolean success = this.products
				.remove(toRemove);

		return success;
	}

	/**
	 * Sees if this ProductContainer contains no items.
	 * 
	 * @pre True
	 * @post True
	 * @return True if this ProductContainer contains no items, false otherwise.
	 */
	public boolean containsNoItems() {
		return (this.items.size() == 0);
	}

	/**
	 * Checks to see if this ProductContainer can be deleted.
	 * 
	 * @pre True
	 * @post True
	 * @return True if this ProductContainer and all sub-ProductContainers have
	 *         no items, false otherwise.
	 */
	public boolean canBeDeleted() {
		if (!containsNoItems()) {
			return false;
		}

		if (this.productGroups.size() > 0) {
			for (int i = 0; i < this.productGroups
					.size(); i++) {
				if (!this.productGroups.get(i)
						.canBeDeleted()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Tests whether or not a certain ProductGroup exists within this
	 * ProductContainer.
	 * 
	 * @pre name is not null or an empty string.
	 * @post True
	 * 
	 * @param name
	 *            The name being searched for.
	 * @return True if this ProductContainer contains a ProductGroup named name,
	 *         false otherwise.
	 */
	public boolean containsProductGroup(
			String name) {
		assert (name != null)
				&& (name.length() > 0);
		for (int i = 0; i < this.productGroups
				.size(); i++) {
			if (this.productGroups.get(i)
					.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether or not a certain product is contained within this product
	 * container or any of it's children product containers. Useful for
	 * determining how to move items.
	 * 
	 * @pre productBarcode is a valid product barcode
	 * @post true
	 * @param productBarcode
	 * @return true if the productBarcode is contained within this product
	 *         container
	 */
	public boolean containsProduct(
			ProductBarcode productBarcode) {
		assert ProductBarcode
				.isValid(productBarcode
						.toString());
		// System.out.println("products size = " + this.products.size());
		for (int c = 0; c < this.products.size(); c++) {
			// System.out.println(sb.toString() + " " + productBarcode + " - " +
			// this.products.get(c).getBarcode());
			if (this.products.get(c).getBarcode()
					.equals(productBarcode)) {
				return true;
			}
		}
		// System.out.println("productGroups size = " +
		// this.productGroups.size());
		for (int c = 0; c < this.productGroups
				.size(); c++) {

			// System.out.println(sb.toString() + " " + productBarcode + " - " +
			// this.productGroups.get(c).getName());
			if (this.productGroups.get(c)
					.containsProduct(
							productBarcode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds the storage unit for this product container
	 * 
	 * @pre this product container is in a storage unit
	 * @post Valid storageunit returned
	 * @return StorageUnit for this product container
	 */
	public StorageUnit getStorageUnit() {
		// find the StorageUnit for the destination container
		StorageUnit storageUnit = null;
		ProductContainer parentContainer = this;
		while (storageUnit == null) {
			if (parentContainer.getClass() == StorageUnit.class) {
				storageUnit = (StorageUnit) parentContainer;
			} else {
				parentContainer = ((ProductGroup) parentContainer)
						.getParent();
			}
		}
		return storageUnit;
	}

	/**
	 * Finds the product container within this or any child product containers
	 * that contains the given productbarcode
	 * 
	 * @pre productBarcode is a non null valid product barcode
	 * @post ProductContainer returned if found, null otherwise
	 * @param productBarcode
	 * @return ProductContainer found within this or any child product
	 *         containers
	 */
	public ProductContainer findProductContainer(
			ProductBarcode productBarcode) {
		for (int c = 0; c < this.products.size(); c++) {
			if (this.products.get(c).getBarcode()
					.equals(productBarcode)) {
				return this;
			}
		}

		for (int c = 0; c < this.productGroups
				.size(); c++) {
			ProductContainer foundProductContainer = this.productGroups
					.get(c).findProductContainer(
							productBarcode);
			if (foundProductContainer != null) {
				return foundProductContainer;
			}
		}
		return null;
	}

	/**
	 * Returns the name of this ProductContainer.
	 * 
	 * @pre True
	 * @post True
	 * @return The name of this ProductContainer.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets all the ProductGroups that are immediate children in this
	 * ProductContainer.
	 * 
	 * @pre True
	 * @post True
	 * @return A list of ProductGroups.
	 */
	public List<ProductGroup> getProductGroups() {
		return this.productGroups;
	}

	public void getAllSubProductGroups(
			List<ProductGroup> subList) {

		for (int i = 0; i < this.productGroups
				.size(); i++) {
			ProductGroup cur = this.productGroups
					.get(i);
			subList.add(cur);
			cur.getAllSubProductGroups(subList);
		}
	}

	/**
	 * Gets a list of all Products in this ProductContainer.
	 * 
	 * @pre True
	 * @post True
	 * @return A list of Products.
	 */
	public List<Product> getProducts() {
		return this.products;
	}

	/**
	 * Gets a list of all Items contained in this immediate ProductContainer.
	 * 
	 * @pre True
	 * @post True
	 * @return A list of Items.
	 */
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * Checks if the name of a potential ProductContainer is valid.
	 * 
	 * @pre True
	 * @post True
	 * @param name
	 *            The potential name of a ProductContainer
	 * @return True if name is not null or the empty string, false otherwise.
	 */
	public static boolean isValid(String name) {
		if ((name == null) || name.isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

	public int getItemCountByProduct(
			Product product) {
		// Item[] items = new Items[];
		// ArrayList<Item> itemsByProduct = new ArrayList<Item>();
		int ret = 0;
		for (int c = 0; c < this.items.size(); c++) {
			if (this.items.get(c).getProduct()
					.equals(product)) {
				ret++;
				// itemsByProduct.add(items.get(c));
			}
		}
		// return itemsByProduct.size();
		return ret;
	}

	public abstract String getParentName();

}