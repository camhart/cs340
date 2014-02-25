package controller.notify;

import gui.inventory.IInventoryView;
import gui.inventory.InventoryController;
import gui.inventory.InventoryView;
import gui.inventory.ProductContainerData;
import gui.product.ProductData;
import model.managers.ProductManager;
import model.models.productContainer.ProductContainer;
import controller.context.Context;

public class ProductNotifier implements Notifier {

	private boolean itemNeedsAdded = false;

	public ProductNotifier() {

	}

	public ProductNotifier(boolean itemNeedsAdded) {
		this.itemNeedsAdded = itemNeedsAdded;
	}

	public boolean itemNeedsAdded() {
		return this.itemNeedsAdded;
	}

	@Override
	public void performAction(
			IInventoryView view,
			ProductContainerData root) {
		// TODO Auto-generated method stub
		ProductContainerData pcd = view
				.getSelectedProductContainer();

		if (pcd == root) {// this is just for the root storage unit notifier
			// view.setProducts(new ProductData[0]);
			if (view.getClass() == InventoryView.class) {
				// ((InventoryController)view).
				InventoryView iView = (InventoryView) view;
				InventoryController invController = (InventoryController) iView
						.getController();
				invController
						.setRootProductData();
			}
			return;
		}

		ProductContainer productContainer = (ProductContainer) pcd
				.getTag();
		assert productContainer != null : "null productContainer selected";
		// ProductContainer productContainer =
		// Context.getInstance().getSelectedContainer();
		// Product product = (Product)view.getSelectedProduct().getTag();
		// ItemManager itemManager = ItemManager.getInstance();

		ProductManager productManager = ProductManager
				.getInstance();

		view.getSelectedProduct();

		// System.out.println("huh? :" +
		// productManager.getProductsByContainer(productContainer));

		view.setProducts(ProductData.convertToProductData(
				productManager
						.getProductsByContainer(productContainer),
				productContainer));
		ProductData sProduct = Context
				.getInstance()
				.getRecentlySelectedProductData();

		view.selectProduct(sProduct);
		System.out.println("aiil");
	}
}
