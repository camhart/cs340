package controller.notify;

import gui.inventory.IInventoryView;
import gui.inventory.InventoryController;
import gui.inventory.InventoryView;
import gui.inventory.ProductContainerData;
import gui.item.ItemData;
import gui.product.ProductData;
import model.managers.ItemManager;
import model.models.Product;
import model.models.productContainer.ProductContainer;
import controller.context.Context;

;

public class ItemNotifier implements Notifier {

	public ItemNotifier() {

	}

	@Override
	public void performAction(
			IInventoryView view,
			ProductContainerData root) {
		System.out.print("Root?->");

		if (Context.getInstance()
				.getRecentlySelectedProductData() == null) {
			return;
		}

		ProductContainer productContainer = (ProductContainer) view
				.getSelectedProductContainer()
				.getTag();

		if ((view.getClass() == InventoryView.class)
				&& (productContainer == Context
						.getInstance().getRoot())) {
			InventoryView invView = (InventoryView) view;
			InventoryController invController = (InventoryController) invView
					.getController();

			invController.setRootProductData();

			if (Context.getInstance()
					.getSelectedProduct() != null) {
				Product selectedProduct = Context
						.getInstance()
						.getSelectedProduct();
				invController
						.setRootItemData(selectedProduct);
			} else {
				invView.setProducts(new ProductData[0]);
			}
			invView.selectProductContainer(invView
					.getSelectedProductContainer());
			// System.out.println("Root Notifier!");
			return;
		}

		if (view.getSelectedProduct() == null) {
			view.setItems(new ItemData[0]);
			return;
		}

		Product product = (Product) view
				.getSelectedProduct().getTag();
		ItemManager itemManager = ItemManager
				.getInstance();

		// ProductManager productManager = ProductManager.getInstance();

		// view.setProducts(ProductData.convertToProductData(
		// productManager.getProductsByContainer(productContainer),
		// productContainer));

		view.setItems(ItemData.convertToItemData(itemManager
				.getItemsByContainerAndProduct(
						productContainer,
						product.getBarcode())));
	}

}
