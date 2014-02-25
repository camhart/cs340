package controller.context;

import java.util.Observable;

import controller.notify.ContextNotifier;

import gui.product.ProductData;
import model.models.Item;
import model.models.Product;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.StorageUnit;

public class Context extends Observable {

	private static Context singleton;

	public static Context getInstance() {
		if (singleton == null) {
			singleton = new Context();
			return singleton;
		} else {
			return singleton;
		}
	}

	private Context() {
	}

	private Item selectedItem;
	private ProductContainer selectedContainer;
	private Product selectedProduct;
	private ProductData recentlySelectedProduct = null;

	public Item getSelectedItem() {
		return this.selectedItem;
	}

	public void setSelectedItem(Item selectedItem) {
		this.selectedItem = selectedItem;
		// setChanged();
		// notifyObservers(new ContextNotifier());
	}

	public ProductContainer getSelectedContainer() {
		return this.selectedContainer;
	}

	public void setSelectedContainer(
			ProductContainer selectedContainer) {
		this.selectedContainer = selectedContainer;
		setChanged();
		notifyObservers(new ContextNotifier());
	}

	public Product getSelectedProduct() {
		return this.selectedProduct;
	}

	public void setRecentlySelectedProductData(
			ProductData selectedProductData) {
		if (selectedProductData != null) {
			this.recentlySelectedProduct = selectedProductData;
		}
	}

	public ProductData getRecentlySelectedProductData() {
		return this.recentlySelectedProduct;
	}

	public void setSelectedProduct(
			Product selectedProduct) {
		this.selectedProduct = selectedProduct;
		// setChanged();
		// notifyObservers(new ContextNotifier());
	}

	public ProductContainer getRoot() {
		return this.root;
	}

	private StorageUnit root = null;

	public void setRoot(StorageUnit root) {
		assert this.root == null : "Why're we setting the root container twice? "
				+ this.root;
		this.root = root;
	}

}
