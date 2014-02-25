package gui.storageunit;

import gui.common.Controller;
import gui.common.IView;
import gui.inventory.ProductContainerData;

import java.util.Observable;

import model.managers.StorageUnitManager;

/**
 * Controller class for the edit storage unit view.
 */
public class EditStorageUnitController extends
		Controller implements
		IEditStorageUnitController {

	private String oldName;

	/**
	 * Constructor.
	 * 
	 * @pre view is not null
	 * @post controller is created, for this target
	 * @param view
	 *            Reference to edit storage unit view
	 * @param target
	 *            The storage unit being edited
	 */
	public EditStorageUnitController(IView view,
			ProductContainerData target) {
		super(view);
		this.oldName = target.getName();

		construct();
	}

	//
	// Controller overrides
	//

	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a reference to the view for this controller.}
	 */
	@Override
	protected IEditStorageUnitView getView() {
		return (IEditStorageUnitView) super
				.getView();
	}

	/**
	 * Sets the enable/disable state of all components in the controller's view.
	 * A component should be enabled only if the user is currently allowed to
	 * interact with that component.
	 * 
	 * {@pre None}
	 * 
	 * {@post The enable/disable state of all components in the controller's
	 * view have been set appropriately.}
	 */
	@Override
	protected void enableComponents() {
		getView().enableStorageUnitName(true);
	}

	/**
	 * Loads data into the controller's view.
	 * 
	 * {@pre None}
	 * 
	 * {@post The controller has loaded data into its view}
	 */
	@Override
	protected void loadValues() {
		getView()
				.setStorageUnitName(this.oldName);
	}

	//
	// IEditStorageUnitController overrides
	//

	/**
	 * @pre values were changed
	 * @post controller updated This method is called when any of the fields in
	 *       the edit storage unit view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		String cur = getView()
				.getStorageUnitName();
		if (StorageUnitManager.getInstance()
				.isValid(cur)) {
			getView().enableOK(true);
		} else if (cur.equals(this.oldName)) {
			getView().enableOK(true);
		} else {
			getView().enableOK(false);
		}
	}

	/**
	 * @pre storage unit to edit exists, can edit storage unit
	 * @post storage unit is edited This method is called when the user clicks
	 *       the "OK" button in the edit storage unit view.
	 */
	@Override
	public void editStorageUnit() {
		StorageUnitManager.getInstance().edit(
				this.oldName,
				getView().getStorageUnitName());
		getView().enableOK(false);
	}

	/**
	 * Notifies this controller that updates are needed. Controller then pulls
	 * the updates from the Observable.
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
		// TODO Auto-generated method stub

	}

}
