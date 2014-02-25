package controller.notify;

import gui.inventory.IInventoryView;
import gui.inventory.ProductContainerData;
import model.managers.StorageUnitManager;
import model.models.productContainer.ProductContainer;
import model.models.productContainer.StorageUnit;

public class ProductContainerNotifier implements
		Notifier {

	ProductContainerData changedContainer;
	int index;

	public ProductContainerNotifier(
			ProductContainer changedContainer) {
		this.changedContainer = new ProductContainerData(
				changedContainer);
	}

	public ProductContainerNotifier(
			ProductContainer changedContainer,
			String oldName) {
		this.changedContainer = new ProductContainerData(
				changedContainer);
		this.changedContainer.setOldName(oldName);
	}

	@Override
	public void performAction(
			IInventoryView view,
			ProductContainerData root) {

		root.clearChildren();
		for (String key : StorageUnitManager
				.getInstance().getStorageUnits()
				.keySet()) {
			StorageUnit storageUnit = StorageUnitManager
					.getInstance()
					.getStorageUnits().get(key);
			root.addChild(new ProductContainerData(
					storageUnit));

		}
		// System.out.println("a");
		// if(changedContainer.getOldName() != null) {
		// System.out.println("b");

		// ItemManager.getInstance().

		// updateContainerChange((ProductContainer)changedContainer.getTag(),
		// changedContainer.getOldName());
		// }

		ProductContainerDataSorter
				.sortProductContainerData(root);
		view.setProductContainers(root);

		if (this.changedContainer.getName() != null) {

			view.selectProductContainer(this.changedContainer);

		}
	}
	// //<<<<<<< HEAD
	//
	// private void sortProductGroups(ProductContainerData container) {
	//
	// if(container.getChildCount() == 0) {
	// return;
	// }
	//
	// List<ProductContainerData> children = new
	// ArrayList<ProductContainerData>();
	// for(int i = 0; i < container.getChildCount(); i++)
	// {
	// children.add(container.getChild(i));
	// }
	//
	// Collections.sort(children, new Comparator<ProductContainerData>() {
	//
	// public int compare(ProductContainerData pcd1, ProductContainerData pcd2)
	// {
	// //return Collator.getInstance().compare(pcd1.getName(), pcd2.getName());
	// return pcd1.getName().compareTo(pcd2.getName());
	// }
	// });
	//
	// container.clearChildren();
	//
	// for(ProductContainerData pcd : children) {
	// container.addChild(pcd);
	// sortProductGroups(pcd);
	// }
	//
	// }
	// //=======
	// //>>>>>>> cc33fee0540f7448419972ae13426306013717b6
	//
	// public void setOldName(String oldName) {
	// this.changedContainer.setOldName(oldName);
	//
	// }

}
