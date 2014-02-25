package controller.notify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gui.inventory.ProductContainerData;

public class ProductContainerDataSorter {

	public ProductContainerDataSorter() {
	}

	public static void sortProductContainerData(
			ProductContainerData container) {
		if (container.getChildCount() == 0) {
			return;
		}

		List<ProductContainerData> children = new ArrayList<ProductContainerData>();
		for (int i = 0; i < container
				.getChildCount(); i++) {
			children.add(container.getChild(i));
		}

		Collections
				.sort(children,
						new Comparator<ProductContainerData>() {

							@Override
							public int compare(
									ProductContainerData pcd1,
									ProductContainerData pcd2) {
								// return
								// Collator.getInstance().compare(pcd1.getName(),
								// pcd2.getName());
								return pcd1
										.getName()
										.compareTo(
												pcd2.getName());
							}
						});

		container.clearChildren();

		for (ProductContainerData pcd : children) {
			container.addChild(pcd);
			ProductContainerDataSorter
					.sortProductContainerData(pcd);
		}
	}
}