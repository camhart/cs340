package controller.notify;

import gui.inventory.IInventoryView;
import gui.inventory.ProductContainerData;

public interface Notifier {

	public void performAction(
			IInventoryView view,
			ProductContainerData root);
}
