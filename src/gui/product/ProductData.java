package gui.product;

import gui.common.Tagable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import model.managers.ItemManager;
import model.managers.ProductManager;
import model.models.Item;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;

/**
 * Display data class for products.
 */
public class ProductData extends Tagable {

	/**
	 * Description attribute.
	 */
	private String _description;

	/**
	 * Size attribute.
	 */
	private String _size;

	/**
	 * Count attribute.
	 */
	private String _count;

	/**
	 * Shelf Life attribute
	 */
	private String _shelfLife;

	/**
	 * Supply attribute.
	 */
	private String _supply;

	/**
	 * Barcode attribute.
	 */
	private String _barcode;

	// private ProductContainerData productContainerData;

	/**
	 * Constructor.
	 * 
	 * {@pre None}
	 * 
	 * {@post getDescription() == ""} {@post getSize() == ""} {@post getCount()
	 * == ""} {@post getShelfLife() == ""} {@post getSupply() == ""} {@post
	 * getBarcode() == ""}
	 * 
	 * @param selectedContainer
	 */
	public ProductData() {
		this._description = "";
		this._size = "";
		this._count = "";
		this._shelfLife = "";
		this._supply = "";
		this._barcode = "";
	}

	/**
	 * Returns the value of the Barcode attribute.
	 * 
	 * @pre True
	 * @post Returns currrent value of attribute.
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
	 * Returns the value of the Description value.
	 * 
	 * @pre True
	 * @post Returns currrent value of description attribute.
	 */
	public String getDescription() {
		return this._description;
	}

	/**
	 * Sets the value of the Description value.
	 * 
	 * @param description
	 *            New Description value
	 * 
	 *            {@pre description != null}
	 * 
	 *            {@post getDescription() == description}
	 */
	public void setDescription(String description) {
		this._description = description;
	}

	/**
	 * Returns the value of the Size attribute.
	 * 
	 * @pre True
	 * @post Returns currrent value of size attribute.
	 */
	public String getSize() {
		return this._size;
	}

	/**
	 * Sets the value of the Size attribute.
	 * 
	 * @param size
	 *            New Size value
	 * 
	 *            {@pre size != null}
	 * 
	 *            {@post getSize() == size}
	 */
	public void setSize(String size) {
		this._size = size;
	}

	/**
	 * Returns the value of the Count attribute.
	 * 
	 * @pre True
	 * @post Returns currrent value of count attribute.
	 */
	public String getCount() {
		return this._count;
	}

	/**
	 * Sets the value of the Count attribute.
	 * 
	 * @param count
	 *            New Count value
	 * 
	 *            {@pre count != null}
	 * 
	 *            {@post getCount() == count}
	 */
	public void setCount(String count) {
		this._count = count;
	}

	/**
	 * Returns the value of the Shelf Life attribute.
	 * 
	 * @pre True
	 * @post Returns currrent value of shelf life attribute.
	 */
	public String getShelfLife() {
		return this._shelfLife;
	}

	/**
	 * Sets the value of the Shelf Life attribute.
	 * 
	 * @param shelfLife
	 *            New Shelf Life value
	 * 
	 *            {@pre shelfLife != null}
	 * 
	 *            {@post getShelfLife() == shelfLife}
	 */
	public void setShelfLife(String shelfLife) {
		this._shelfLife = shelfLife;
	}

	/**
	 * Returns the value of the Supply attribute.
	 * 
	 * @pre True
	 * @post Returns currrent value of supply attribute.
	 */
	public String getSupply() {
		return this._supply;
	}

	/**
	 * Sets the value of the Supply attribute.
	 * 
	 * @param supply
	 *            New Supply value
	 * 
	 *            {@pre supply != null}
	 * 
	 *            {@post getSupply() == supply}
	 */
	public void setSupply(String supply) {
		this._supply = supply;
	}

	// /**
	// * Converts an array of items to an array of ItemData. Keeps the same
	// sorting.
	// *
	// * @pre products not null, and sorted
	// * @pre products[c] coordinates with it's container counts[c]
	// * @post Product[] returned sorted as it was given
	// * @param products
	// * @param productContainers
	// * @return array of ItemData
	// */
	// public static ProductData[] convertToProductData(Product[] products,
	// ProductContainer productContainer) {
	// assert products != null :
	// "items is null in convertToItemData in ItemData.java";
	// ProductData[] ret = new ProductData[products.length];
	// for (int c = 0; c < products.length; c++) {
	// ret[c] = convertToProductData(products[c], productContainer);
	// }
	// return ret;
	// }

	/**
	 * Converts Item to ItemData object
	 * 
	 * @pre item is not null
	 * @post
	 * @param product
	 * @param count
	 *            of items of this product type within the given container
	 * @return
	 */
	public static ProductData convertToProductData(
			Product product,
			ProductContainer productContainer) {
		// ProductContainerData productContainerData) {
		assert product != null : "product is null in convertToItemData in ItemData.java";
		ProductData ret = new ProductData();
		ret._description = product
				.getDescription();
		ret._size = product.getSize()
				.getUnitString();
		// Set<Item> itemInContainer =
		// ItemManager.getInstance().getItemByContainer(
		// (ProductContainer)productContainerData.getTag());
		// Set<Item> itemByProduct =
		// ItemManager.getInstance().getItemsByProduct(product);

		Set<Item> items = ItemManager
				.getInstance()
				.getItemsByContainerAndProduct(
						productContainer,
						product.getBarcode());

		ret._count = Integer.toString(items
				.size());
		ret._shelfLife = Integer.toString(product
				.getShelfLife());
		ret._supply = Integer.toString(product
				.getThreeMonthSupply());
		ret._barcode = product.getBarcode()
				.toString();
		ret.setTag(product);
		return ret;
	}

	/**
	 * Converts Item to ItemData object
	 * 
	 * @pre item is not null
	 * @post
	 * @param product
	 * @param count
	 *            of items of this product type within the given container
	 * @return
	 */
	public static ProductData convertToProductData(
			Product product, int count) {
		// ProductContainerData productContainerData) {
		assert product != null : "product is null in convertToItemData in ItemData.java";
		ProductData ret = new ProductData();
		ret._description = product
				.getDescription();
		ret._size = product.getSize()
				.getUnitString();

		ret._count = Integer.toString(count);
		ret._shelfLife = Integer.toString(product
				.getShelfLife());
		ret._supply = Integer.toString(product
				.getThreeMonthSupply());
		ret._barcode = product.getBarcode()
				.toString();
		ret.setTag(product);
		return ret;
	}

	/**
	 * Converts a Collection of items to an array of ItemData. Keeps the same
	 * sorting.
	 * 
	 * @pre products not null, and sorted
	 * @pre products[c] coordinates with it's container counts[c]
	 * @post Product[] returned sorted as it was given
	 * @param products
	 * @param productContainers
	 * @return array of ItemData
	 */
	public static ProductData[] convertToProductData(
			Collection<Product> products,
			ProductContainer targetProductContainer) {
		// assert products != null :
		// "products is null in convertToProductData in ProductData.java";
		if ((products == null)
				|| (products.size() == 0)) {
			return new ProductData[0];
		}
		ProductData[] ret = new ProductData[products
				.size()];
		Iterator<Product> iter = products
				.iterator();
		int c = 0;
		while (iter.hasNext()) {
			ret[c++] = convertToProductData(
					iter.next(),
					targetProductContainer);
		}
		return ret;
	}

	public static ProductData[] convertToProductData(
			Collection<Product> products,
			Map<ProductBarcode, Integer> countMap) {
		assert products != null : "items is null in convertToItemData in ItemData.java";
		assert countMap != null;

		// ProductData[] ret = new ProductData[products.size()];
		ArrayList<ProductData> ret = new ArrayList<ProductData>();
		Iterator<Product> iter = products
				.iterator();
		Product curProduct = null;
		while (iter.hasNext()) {
			curProduct = iter.next();
			if (!ret.contains(curProduct)
					&& (countMap != null)
					&& (countMap.get(curProduct
							.getBarcode()) != null)) {
				ret.add(convertToProductData(
						curProduct,
						countMap.get(curProduct
								.getBarcode())));
			}
			// ret[c++] = convertToProductData(curProduct,
			// countMap.get(curProduct.getBarcode()));
		}
		return ret.toArray(new ProductData[0]);
	}

	public static ProductData[] convertToProductData(
			ArrayList<ProductBarcode> productsLoaded,
			Map<ProductBarcode, Integer> countMap) {
		assert productsLoaded != null : "items is null in convertToItemData in ItemData.java";
		assert countMap != null;

		// ProductData[] ret = new ProductData[productsLoaded.size()];
		ArrayList<ProductData> ret = new ArrayList<ProductData>();
		Iterator<ProductBarcode> iter = productsLoaded
				.iterator();
		Product curProduct = null;
		// int c = 0;
		while (iter.hasNext()) {
			// int additionalCount = 0;
			curProduct = ProductManager
					.getInstance()
					.getProductByBarcode(
							iter.next());
			if (curProduct != null) { // removes any false ones
				// int test = countMap.get(curProduct.getBarcode());
				ProductData productData = convertToProductData(
						curProduct,
						countMap.get(curProduct
								.getBarcode()));
				if (ret.contains(productData)) {
					// Do nothing - test to make sure count is incremented
					// properly.
				} else {
					ret.add(productData);
				}
			}
			// ret[c++] = convertToProductData(curProduct,
			// countMap.get(curProduct.getBarcode()));
		}
		return ret.toArray(new ProductData[0]);
	}

	@Override
	public int hashCode() {
		// final int prime = 31;
		// int result = 1;
		// result = prime * result
		// + ((_barcode == null) ? 0 : _barcode.hashCode());
		// result = prime * result + ((_count == null) ? 0 : _count.hashCode());
		// result = prime * result
		// + ((_description == null) ? 0 : _description.hashCode());
		// result = prime * result
		// + ((_shelfLife == null) ? 0 : _shelfLife.hashCode());
		// result = prime * result + ((_size == null) ? 0 : _size.hashCode());
		// result = prime * result + ((_supply == null) ? 0 :
		// _supply.hashCode());
		// return result;
		return this._barcode.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if ((obj == null)
				|| (obj.getClass() != ProductData.class)) {
			return false;
		}
		return this._barcode
				.equals(((ProductData) obj)._barcode);
		// if (this == obj)
		// return true;
		// if (obj == null)
		// return false;
		// if (getClass() != obj.getClass())
		// return false;
		// ProductData other = (ProductData) obj;
		// if (_barcode == null) {
		// if (other._barcode != null)
		// return false;
		// } else if (!_barcode.equals(other._barcode))
		// return false;
		// if (_count == null) {
		// if (other._count != null)
		// return false;
		// } else if (!_count.equals(other._count))
		// return false;
		// if (_description == null) {
		// if (other._description != null)
		// return false;
		// } else if (!_description.equals(other._description))
		// return false;
		// if (_shelfLife == null) {
		// if (other._shelfLife != null)
		// return false;
		// } else if (!_shelfLife.equals(other._shelfLife))
		// return false;
		// if (_size == null) {
		// if (other._size != null)
		// return false;
		// } else if (!_size.equals(other._size))
		// return false;
		// if (_supply == null) {
		// if (other._supply != null)
		// return false;
		// } else if (!_supply.equals(other._supply))
		// return false;
		// return true;
	}

}
