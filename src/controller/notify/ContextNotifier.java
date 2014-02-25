package controller.notify;

import model.models.productContainer.ProductContainer;
import controller.context.Context;
import gui.inventory.IInventoryView;
import gui.inventory.ProductContainerData;
import gui.product.ProductData;

public class ContextNotifier implements Notifier {

	@Override
	public void performAction(
			IInventoryView view,
			ProductContainerData root) {
		// Item item = Context.getInstance().getSelectedItem();
		ProductContainer container = Context
				.getInstance()
				.getSelectedContainer();
		// Product product = Context.getInstance().getSelectedProduct();
		//
		// ItemData itemData = view.getSelectedItem();
		ProductContainerData containerData = view
				.getSelectedProductContainer();
		view.getSelectedProduct();

		// if( item != null && itemData != null
		// && !item.equals((Item)itemData.getTag()))
		// view.selectItem(ItemData.convertToItemData(item));
		//
		// if( product != null && productData != null &&
		// !product.equals((Product)productData.getTag()) )
		// view.selectProduct(ProductData.convertToProductData(product,
		// container));
		//
		if ((container != null)
				&& (containerData != null)
				&& !container
						.equals(containerData
								.getTag())) {
			view.selectProductContainer(new ProductContainerData(
					container));
		}
	}
}
