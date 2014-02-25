package gui.productgroup;

import gui.common.Controller;
import gui.common.IView;
import gui.common.SizeUnitConverter;
import gui.common.SizeUnits;
import gui.inventory.ProductContainerData;

import java.util.Observable;

import model.managers.ProductGroupManager;
import model.models.productContainer.ProductGroup;
import model.models.unit.Unit;
import model.models.unit.UnitType;

/**
 * Controller class for the edit product group view.
 */
public class EditProductGroupController extends
		Controller implements
		IEditProductGroupController {

	private ProductGroup model;
	private Unit currentUnit;
	private String oldName;

	/**
	 * Constructor.
	 * 
	 * @param view
	 *            Reference to edit product group view
	 * @param target
	 *            Product group being edited
	 */
	public EditProductGroupController(IView view,
			ProductContainerData target) {
		super(view);

		this.model = (ProductGroup) target
				.getTag();
		this.currentUnit = null;
		this.oldName = new String(
				this.model.getName());

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
	protected IEditProductGroupView getView() {
		return (IEditProductGroupView) super
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
		getView().enableProductGroupName(true);
		getView().enableSupplyValue(true);
		getView().enableSupplyUnit(true);
		getView().enableOK(true);
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
		String name = this.model.getName();
		SizeUnits supplyUnit = SizeUnitConverter
				.UnitTypeToSizeUnits(this.model
						.GetThreeMonthSupply()
						.getUnit());
		String supplyValue = Float
				.toString(this.model
						.GetThreeMonthSupply()
						.getAmount());

		getView().setProductGroupName(name);
		getView().setSupplyUnit(supplyUnit);
		getView().setSupplyValue(supplyValue);
	}

	//
	// IEditProductGroupController overrides
	//

	/**
	 * This method is called when any of the fields in the edit product group
	 * view is changed by the user.
	 * 
	 * @pre A change in the input fields has been detected.
	 * @post If the values are valid (ProductGroup.isValid returns true), the
	 *       "OK" button is enabled.
	 */
	@Override
	public void valuesChanged() {
		String name = getView()
				.getProductGroupName();
		String supply = getView()
				.getSupplyValue();
		SizeUnits supplyUnit = getView()
				.getSupplyUnit();

		UnitType unitType = SizeUnitConverter
				.SizeUnitsToUnitType(supplyUnit);
		Float supplyAmount;

		try {
			supplyAmount = Float
					.parseFloat(supply);
		} catch (NumberFormatException e) {
			getView().enableOK(false);
			return;
		}

		if (!Unit.isValid(supplyAmount, unitType)) {
			getView().enableOK(false);
			return;
		}

		Unit unit = new Unit(
				Float.parseFloat(supply),
				unitType);
		this.currentUnit = unit;

		if (ProductGroupManager.getInstance()
				.isValid(name, this.oldName,
						this.model.getParent(),
						unit)) {
			getView().enableOK(true);
		} else {
			getView().enableOK(false);
		}
	}

	/**
	 * This method is called when the user clicks the "OK" button in the edit
	 * product group view.
	 * 
	 * @pre "OK" button is enabled.
	 * @post The ProductGroup being added is added to the model.
	 */
	@Override
	public void editProductGroup() {
		ProductGroupManager.getInstance().edit(
				this.model,
				getView().getProductGroupName(),
				this.currentUnit);
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
