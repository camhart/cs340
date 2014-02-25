package gui.product;

import gui.common.Controller;
import gui.common.IView;
import gui.common.SizeUnitConverter;
import gui.common.SizeUnits;

import java.util.Iterator;
import java.util.Observable;

import model.managers.ProductManager;
import model.models.Product;
import model.models.productContainer.ProductContainer;
import model.models.unit.Unit;
import controller.context.Context;

/**
 * Controller class for the edit product view.
 */
public class EditProductController extends
		Controller implements
		IEditProductController {
	ProductManager pm;
	Context context;
	Product target;

	/**
	 * Constructor.
	 * 
	 * @pre view is not null
	 * @post new controller created
	 * @param view
	 *            Reference to the edit product view
	 * @param target
	 *            Product being edited
	 */
	public EditProductController(IView view,
			ProductData target) {
		super(view);
		this.pm = ProductManager.getInstance();
		this.context = Context.getInstance();
		this.target = (Product) target.getTag();
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
	protected IEditProductView getView() {
		return (IEditProductView) super.getView();
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
		IEditProductView view = getView();
		view.enableBarcode(false);
		view.enableDescription(true);
		view.enableShelfLife(true);
		view.enableSizeUnit(true);
		if (view.getSizeUnit() == SizeUnits.Count) {
			view.setSizeValue("1");
			view.enableSizeValue(false);
		} else {
			view.enableSizeValue(true);
		}
		view.enableSupply(true);
		int shelfLife = -1;
		int supply = -1;
		try {
			shelfLife = Integer.parseInt(view
					.getShelfLife());
			supply = Integer.parseInt(view
					.getSupply());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (Product.isValid(
				this.target.getBarcode(),
				view.getDescription(), shelfLife,
				supply)) {
			view.enableOK(true);
		} else {
			view.enableOK(false);
		}
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
		getView().setBarcode(
				this.target.getBarcode()
						.toString());
		getView().setDescription(
				this.target.getDescription());
		getView().setShelfLife(
				Integer.toString(this.target
						.getShelfLife()));
		Unit unit = this.target.getSize();
		SizeUnits sizeUnit = SizeUnitConverter
				.UnitTypeToSizeUnits(unit
						.getUnit());
		getView().setSizeUnit(sizeUnit);
		getView().setSizeValue(
				Float.toString(unit.getAmount()));
		getView().setSupply(
				Integer.toString(this.target
						.getThreeMonthSupply()));
	}

	//
	// IEditProductController overrides
	//

	/**
	 * @pre values were changed
	 * @post controller updated This method is called when any of the fields in
	 *       the edit product view is changed by the user.
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}

	/**
	 * @pre product exists, can edit product
	 * @post product edited This method is called when the user clicks the "OK"
	 *       button in the edit product view.
	 */
	@Override
	public void editProduct() {
		Unit unit = new Unit(
				Float.parseFloat(getView()
						.getSizeValue()),
				SizeUnitConverter
						.SizeUnitsToUnitType(getView()
								.getSizeUnit()));
		int shelfLife = Integer
				.parseInt(getView()
						.getShelfLife());
		int supply = Integer.parseInt(getView()
				.getSupply());
		ProductContainer selectedContainer = this.context
				.getSelectedContainer();
		if (selectedContainer == this.context
				.getRoot()) {
			Iterator<ProductContainer> iter = this.target
					.getContainers();
			if (iter.hasNext()) {
				selectedContainer = iter.next();
			}
		}
		// pm.edit(target, context.getSelectedContainer(), getView()
		// .getDescription(), unit, shelfLife, supply);
		this.pm.edit(this.target,
				selectedContainer, getView()
						.getDescription(), unit,
				shelfLife, supply);
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
