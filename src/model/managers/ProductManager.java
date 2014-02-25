package model.managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import controller.notify.ProductNotifier;

import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.productContainer.ProductContainer;
import model.models.unit.Unit;

/**
 * ProductManager contains a set of all the Product objects used in the Home
 * Inventory Tracker program. It takes care of adding and removing Products,
 * validating Product objects before they are added, and checking that they
 * exist.
 * 
 * @invariant Singleton
 * @author Cameron Hartmann
 * @author Chris McNeill
 * @author Adam Rogers
 * @author Joel Denning
 * @author Group1
 */
public class ProductManager extends Observable
		implements Saveable {

	private static ProductManager productManager = null;

	HashMap<ProductBarcode, Product> products;
	HashMap<ProductContainer, SortedSet<Product>> productsByContainer;
	HashMap<ProductBarcode, HashSet<ProductContainer>> containersByProduct;

	private ProductManager() {
		this.products = new HashMap<ProductBarcode, Product>();
		this.productsByContainer = new HashMap<ProductContainer, SortedSet<Product>>();
		this.containersByProduct = new HashMap<ProductBarcode, HashSet<ProductContainer>>();
	}

	/**
	 * getInstance() either initializes or returns the ItemManager Singleton
	 * depending on whether it has been created yet.
	 * 
	 * @pre true
	 * @post valid ProductManager Singleton instance returned
	 * @param none
	 * @return ProductManager Singleton instance of ProductManager
	 */
	public static ProductManager getInstance() {
		if (productManager == null) {
			productManager = new ProductManager();
		}
		return productManager;
	}

	/**
	 * add(Product product) adds product to the ProductManager indexed maps.
	 * This happens after validation and creation of the Product. It notifies
	 * its observers of the new product added.
	 * 
	 * @pre product != null
	 * @post product added successfully to ProductManager maps.
	 * @param product
	 *            The Product object to be added to the ProductManager set of
	 *            Products
	 * @return
	 */
	public void add(Product product) {
		assert (product != null);
		assert !this.products.containsKey(product
				.getBarcode()) : "product already contains key... why add?";
		// if(!products.containsKey(product.getBarcode()))
		this.products.put(product.getBarcode(),
				product);

		Iterator<ProductContainer> contItr = product
				.getContainers();
		while (contItr.hasNext()) {
			ProductContainer pc = contItr.next();
			SortedSet<Product> productSet = null;
			if (this.productsByContainer
					.containsKey(pc)) {
				productSet = this.productsByContainer
						.get(pc);
			}
			if (productSet == null) {
				productSet = new TreeSet<Product>();
				this.productsByContainer.put(pc,
						productSet);
			}
			productSet.add(this.products
					.get(product.getBarcode()));

			// productsByContainer.put(pc, productSet);
		}

		HashSet<ProductContainer> containerSet = null;
		if (this.containersByProduct
				.containsKey(this.products
						.get(product.getBarcode()))) {
			containerSet = this.containersByProduct
					.get(this.products.get(product
							.getBarcode()));
		}
		if (containerSet == null) {
			containerSet = new HashSet<ProductContainer>();
			this.containersByProduct.put(
					product.getBarcode(),
					containerSet);
		}
		contItr = product.getContainers();
		while (contItr.hasNext()) {
			ProductContainer pc = contItr.next();
			containerSet.add(pc);
		}
		// containersByProduct.put(product.getBarcode(), containerSet);
		// System.out.println("containersByProduct " +
		// containersByProduct.toString());
		// System.out.println("productsbycontainer " +
		// productsByContainer.toString());
		// System.out.println("products " + products.toString());
		setChanged();
		notifyObservers(new ProductNotifier(true));
	}

	/**
	 * addToContainer(Product product, ProductContainer pc) adds product to pc
	 * in the ProductManager indexed maps. This happens after validation and
	 * creation of the Product. It notifies its observers the the new product
	 * was added.
	 * 
	 * @pre product != null
	 * @post product added successfully to ProductManager maps.
	 * @param product
	 *            The Product object to be added to the ProductManager set of
	 *            Products
	 * @param pc
	 *            ProductContainer object for product to be added to
	 * @return
	 */
	public void addToContainer(Product product,
			ProductContainer pc) {
		assert (product != null);
		SortedSet<Product> productSet = null;
		if (this.productsByContainer
				.containsKey(pc)) {
			productSet = this.productsByContainer
					.get(pc);
		}
		if (productSet == null) {
			productSet = new TreeSet<Product>();
			this.productsByContainer.put(pc,
					productSet);
		}
		// productSet.add(products.get(product.getBarcode()));
		productSet.add(product);

		HashSet<ProductContainer> containerSet = this.containersByProduct
				.get(product.getBarcode());
		if (containerSet == null) {
			containerSet = new HashSet<ProductContainer>();
			this.containersByProduct.put(
					product.getBarcode(),
					containerSet);
		}
		containerSet.add(pc);

		product.addToContainer(pc);
		// assert !products.containsKey(product.getBarcode()) : "contains key?";
		if (!this.products.containsKey(product
				.getBarcode())) {
			this.products
					.put(product.getBarcode(),
							product);
		}
		ProductNotifier notify = new ProductNotifier();
		setChanged();
		notifyObservers(notify);
	}

	/**
	 * Edits the Product object so that it contains the parameters passed in. It
	 * notifies its observers that the product was edited.
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
	public void edit(Product product,
			ProductContainer productContainer,
			String newDescription, Unit newSize,
			int newShelfLife,
			int newThreeMonthSupply)// throws
	// InvalidProductException
	{
		// if(Product.isValid(product.getBarcode(), newDescription,
		// newShelfLife, newThreeMonthSupply))
		// productsByContainer.remove(product);
		Set<Product> productSet = this.productsByContainer
				.get(productContainer);
		productSet.remove(product);
		product.edit(newDescription, newSize,
				newShelfLife, newThreeMonthSupply);
		productSet.add(product);
		// productsByContainer.put(, value)

		ProductNotifier notifier = new ProductNotifier();
		setChanged();
		notifyObservers(notifier);
		// else
		// throw new InvalidProductException("Invalid Product Parameters");
	}

	/**
	 * delete(Product product) deletes product from the ProductManager's maps.
	 * It notifies its observers that the product was deleted.
	 * 
	 * @pre product is not null and exists
	 * @post product successfully deleted from all maps
	 * @param product
	 *            The Product being removed.
	 */
	public void delete(Product product) {
		assert ((product != null) && this.products
				.containsKey(product.getBarcode()));
		Iterator<ProductContainer> contItr = product
				.getContainers();
		while (contItr.hasNext()) {
			// Iterate through every container and remove product from each one
			ProductContainer pc = contItr.next();
			if (this.productsByContainer
					.containsKey(pc)) {
				SortedSet<Product> productSet = this.productsByContainer
						.get(pc);
				Iterator<Product> prodItr = productSet
						.iterator();
				Product toRemove = null;
				while (prodItr.hasNext()) { // Find product to remove in set
					Product next = prodItr.next();
					if (next.equals(product)) {
						toRemove = next;
					}
				}
				productSet.remove(toRemove);
				// Put new product set into productsByContainer
				if (productSet.size() > 0) {
					this.productsByContainer.put(
							pc, productSet);
				} else {
					// Remove that container from the map
					this.productsByContainer
							.remove(pc);
				}
				pc.removeProduct(product);
			}
		}
		Iterator<ProductBarcode> prodItr = this.containersByProduct
				.keySet().iterator();
		ProductBarcode toRemove = null;
		while (prodItr.hasNext()) // Find product to remove in set
		{
			ProductBarcode next = prodItr.next();
			if (next.equals(product.getBarcode())) {
				toRemove = next;
			}
		}
		this.containersByProduct.remove(toRemove);
		this.products
				.remove(product.getBarcode());
		setChanged();
		this.notifyObservers(new ProductNotifier());
	}

	/**
	 * deleteFromContainer(Product product,ProductContainer pc) deletes product
	 * from pc in ProductManager's container maps. It notifies its observers
	 * that the product was deleted.
	 * 
	 * @pre product is not null and pc is not null
	 * @post product successfully deleted from container maps
	 * @param product
	 *            The Product being removed.
	 */
	public void deleteFromContainer(
			Product product, ProductContainer pc) {
		assert ((product != null) && (pc != null));
		if (this.productsByContainer
				.containsKey(pc)) {
			SortedSet<Product> productSet = this.productsByContainer
					.get(pc);
			Iterator<Product> prodItr = productSet
					.iterator();
			Product toRemove = null;
			while (prodItr.hasNext()) { // Find product to remove in set
				Product next = prodItr.next();
				if (next.equals(product)) {
					toRemove = next;
					break;
				}
			}
			productSet.remove(toRemove);
			// // Put new product set into productsByContainer
			// if (productSet.size() > 0)
			// productsByContainer.put(pc, productSet);
			// else
			// // Remove that container from the map
			// productsByContainer.remove(pc);
		}
		if (this.containersByProduct
				.containsKey(product.getBarcode())) {
			HashSet<ProductContainer> contSet = this.containersByProduct
					.get(product.getBarcode());
			Iterator<ProductContainer> contItr = contSet
					.iterator();
			ProductContainer contToRemove = null;
			while (contItr.hasNext()) { // Find container to remove in set
				ProductContainer nextCont = contItr
						.next();
				if (nextCont.equals(pc)) {
					contToRemove = nextCont;
				}
			}
			contSet.remove(contToRemove);
			// Put new product set into productsByContainer
			if (contSet.size() > 0) {
				this.containersByProduct.put(
						product.getBarcode(),
						contSet);
			} else {
				this.containersByProduct
						.remove(product
								.getBarcode());
			}
		}
		product.removeFromContainer(pc);
		this.products.put(product.getBarcode(),
				product);
		pc.removeProduct(product);

		setChanged();
		this.notifyObservers(new ProductNotifier());
	}

	/**
	 * isValid(String description, Unit size, int shelfLife, int
	 * threeMonthSupply) Checks the given input according to the validation
	 * rules of Product. description must be non-empty, size must be a valid
	 * Unit enum, and both shelfLife and threeMonthSupply must be non-negative.
	 * 
	 * @pre true
	 * @post correct boolean for whether product parameters are valid
	 * @param description
	 *            The description of the ProductGroup object.
	 * @param size
	 *            The Unit of the Product's threeMonthSupply attribute.
	 * @param shelfLife
	 *            The shelfLife, in months, of the Product object.
	 * @param threeMonthSupply
	 *            the int value representing the number of Units needed in this
	 *            Product for a three month supply.
	 * @return boolean. True if the input is valid for creating a Product. False
	 *         if the input is not valid.
	 */
	public boolean isValid(
			ProductBarcode barcode,
			String description, int shelfLife,
			int threeMonthSupply) {
		return Product.isValid(barcode,
				description, shelfLife,
				threeMonthSupply)
				&& !productExists(barcode);
	}

	/**
	 * productExists(ProductBarcode barcode) checks the ProductManager's set of
	 * Products to see if a product with barcode barcode exists
	 * 
	 * @pre barcode is not null
	 * @post correct boolean value for whether product with barcode exists
	 * @param barcode
	 *            The ProductBarcode to check.
	 * @return boolean. True if a Product with barcode exists. False if no
	 *         Product with barcode exists.
	 */
	public boolean productExists(
			ProductBarcode barcode) {
		assert (barcode != null);
		boolean ret = false;
		if (this.products.containsKey(barcode)) {
			ret = true;
		}
		return ret;
	}

	// This class must remain static in order to remain Serializable.
	// (the only other option if we want to remove static is to make
	// ItemManager Serializable)
	private static class PMDataObject implements
			Serializable {
		HashMap<ProductBarcode, Product> productsData;
		HashMap<ProductContainer, SortedSet<Product>> productsByContainerData;
		HashMap<ProductBarcode, HashSet<ProductContainer>> containersByProductData;

		public PMDataObject(
				HashMap<ProductBarcode, Product> products,
				HashMap<ProductContainer, SortedSet<Product>> productsByContainer,
				HashMap<ProductBarcode, HashSet<ProductContainer>> tcontainersByProduct) {
			this.productsData = products;
			this.productsByContainerData = productsByContainer;
			this.containersByProductData = tcontainersByProduct;
		}
	}

	@Override
	public void writeObjects(
			ObjectOutputStream objectOut) {
		try {
			PMDataObject data = new PMDataObject(
					this.products,
					this.productsByContainer,
					this.containersByProduct);
			objectOut.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void readObjects(
			ObjectInputStream objectIn) {
		try {
			PMDataObject data = (PMDataObject) objectIn
					.readObject();
			this.products = data.productsData;
			this.productsByContainer = data.productsByContainerData;
			this.containersByProduct = data.containersByProductData;
			// System.out.println("loaded products by container: " +
			// this.productsByContainer);
		} catch (Exception e) { // ClassNotFoundException | IOException
			e.printStackTrace();
		}
	}

	@Override
	public String getDataFileName() {
		return "ProductManager";
	}

	public Product getProductByBarcode(
			ProductBarcode productBarcode) {
		// TODO Auto-generated method stub
		return this.products.get(productBarcode);
	}

	public Collection<Product> getProductsByContainer(
			ProductContainer productContainer) {
		// System.out.println("look for\n\t" + productContainer.getName() +
		// "\ninside of\n\t" + this.productsByContainer);
		ArrayList<String> test = new ArrayList<String>();
		Iterator<ProductContainer> iter = this.productsByContainer
				.keySet().iterator();
		while (iter.hasNext()) {

			test.add(iter.next().hashCode()
					+ ", ");
		}
		// System.out.println("look for " + productContainer.hashCode() +
		// " inside " + test);
		// System.out.println( this.productsByContainer.get(productContainer));
		// System.out.println(this.productsByContainer.containsKey(productContainer));
		return this.productsByContainer
				.get(productContainer);
	}

	HashMap<ProductBarcode, HashSet<ProductContainer>> getContainersByProduct() {
		return this.containersByProduct;
	}

	HashMap<ProductBarcode, Product> getProducts() {
		// TODO Auto-generated method stub
		return this.products;
	}

	HashMap<ProductContainer, SortedSet<Product>> getProductsByContainer() {
		// TODO Auto-generated method stub
		return this.productsByContainer;
	}

	public void setChangedOverride() {
		setChanged();
	}

	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		Iterator<ProductBarcode> iter = this.products
				.keySet().iterator();
		ArrayList<Product> ret = new ArrayList<Product>();
		while (iter.hasNext()) {
			Product curProduct = this.products
					.get(iter.next());
			ret.add(curProduct);
		}
		return ret;
	}
	// TreeMap

}
