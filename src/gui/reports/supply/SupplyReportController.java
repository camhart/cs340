package gui.reports.supply;

import gui.common.Controller;
import gui.common.IView;

import java.util.Observable;

/**
 * Controller class for the N-month supply report view.
 */
public class SupplyReportController extends
		Controller implements
		ISupplyReportController {
	/*---	STUDENT-INCLUDE-BEGIN

	 /**
	 * Constructor.
	 * 
	 * @param view Reference to the N-month supply report view
	 */
	public SupplyReportController(IView view) {
		super(view);

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
	protected ISupplyReportView getView() {
		return (ISupplyReportView) super
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
	}

	//
	// IExpiredReportController overrides
	//

	/**
	 * This method is called when any of the fields in the N-month supply report
	 * view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
	}

	/**
	 * This method is called when the user clicks the "OK" button in the N-month
	 * supply report view.
	 */
	@Override
	public void display() {
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
