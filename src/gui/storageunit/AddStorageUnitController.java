package gui.storageunit;

import gui.common.Controller;
import gui.common.IView;

import java.util.Observable;

import model.managers.StorageUnitManager;
import model.models.productContainer.StorageUnit;
import controller.context.Context;

/**
 * Controller class for the add storage unit view.
 */
public class AddStorageUnitController extends
		Controller implements
		IAddStorageUnitController {

	/**
	 * Constructor.
	 * 
	 * @pre view exists
	 * @post new Controller constructed
	 * @param view
	 *            Reference to add storage unit view
	 */
	public AddStorageUnitController(IView view) {
		super(view);
		getView().enableOK(false);
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
	protected IAddStorageUnitView getView() {
		return (IAddStorageUnitView) super
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
		getView().setStorageUnitName(
				getView().getStorageUnitName());
	}

	//
	// IAddStorageUnitController overrides
	//

	/**
	 * @pre values were changed
	 * @post controller updated This method is called when any of the fields in
	 *       the add storage unit view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		String cur = getView()
				.getStorageUnitName();
		if (StorageUnitManager.getInstance()
				.isValid(cur)) {
			getView().enableOK(true);
		} else {
			getView().enableOK(false);
		}
	}

	/**
	 * @pre storage unit can be added
	 * @post storage unit is added This method is called when the user clicks
	 *       the "OK" button in the add storage unit view.
	 */
	@Override
	public void addStorageUnit() {
		String newStorageUnit = getView()
				.getStorageUnitName();
		StorageUnit unitToAdd = new StorageUnit(
				newStorageUnit);
		StorageUnitManager.getInstance().add(
				unitToAdd);
		Context.getInstance()
				.setSelectedContainer(unitToAdd); // NECESSARY?
		getView().enableOK(false);
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
		// TODO Auto-generated method stub

	}

}
