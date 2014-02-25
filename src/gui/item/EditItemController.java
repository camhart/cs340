package gui.item;

import gui.common.Controller;
import gui.common.IView;

import java.util.Observable;

import model.managers.ItemManager;
import model.models.Item;
import model.models.barcode.ItemBarcode;

/**
 * Controller class for the edit item view.
 */
public class EditItemController extends
		Controller implements IEditItemController {

	private Item targetItem = null;

	/**
	 * Constructor.
	 * 
	 * @pre view is a non null EditItemView
	 * @pre target contains valid item data
	 * @post true
	 * @param view
	 *            Reference to edit item view
	 * @param target
	 *            Item that is being edited
	 */
	public EditItemController(IView view,
			ItemData target) {
		super(view);
		this.targetItem = (Item) target.getTag();
		construct();
	}

	//
	// Controller overrides
	//

	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * @pre None
	 * 
	 * @post Returns a reference to the view for this controller.
	 */
	@Override
	protected IEditItemView getView() {
		return (IEditItemView) super.getView();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently allowed to
	 * interact with that component.
	 * 
	 * @pre None
	 * 
	 * @post The enable/disable state of all components in the controller's view
	 *       have been set appropriately.
	 */
	@Override
	protected void enableComponents() {
		getView().enableBarcode(false);
		getView().enableDescription(false);
		getView().enableEntryDate(true);

		if (getView().getEntryDate() != null) {
			getView().enableOK(true);
		} else {
			getView().enableOK(false);
		}
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 * @pre None
	 * 
	 * @post The controller has loaded data into its view
	 */
	@Override
	protected void loadValues() {
		getView().setBarcode(
				this.targetItem.getItemBarcode()
						.toString());
		getView().setDescription(
				this.targetItem.getDescription());
		getView().setEntryDate(
				this.targetItem.getEntryDate());
	}

	//
	// IEditItemController overrides
	//

	/**
	 * This method is called when any of the fields in the edit item view is
	 * changed by the user.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the user clicks the "OK" button in the edit
	 * item view.
	 * 
	 * @pre EditItemView contains valid item data
	 * @post true
	 */
	@Override
	public void editItem() {
		ItemManager itemManager = ItemManager
				.getInstance();
		if (Item.isValid(
				ItemBarcode.BOGUS_ITEMBARCODE,
				getView().getEntryDate())) {
			itemManager.editItem(this.targetItem,
					getView().getEntryDate());
		} else {
			getView().displayErrorMessage(
					"Invalid entry date.");
		}
	}

	/**
	 * Notifies this controller that updates are needed. Controller then pulls
	 * the updates from the observable.
	 * 
	 * @pre An observed class called the Notify method. observable != null
	 * @post The view is updated according to the model.
	 * @param observable
	 *            Object that update is getting called from.
	 * @param obj
	 *            not necessary since we're using pull, just keep as null.
	 */
	@Override
	public void update(Observable observable,
			Object obj) {

	}

}
