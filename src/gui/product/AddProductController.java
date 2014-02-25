package gui.product;

import gui.common.Controller;
import gui.common.IView;
import gui.common.SizeUnitConverter;
import gui.common.SizeUnits;

import java.util.Observable;

import model.managers.ProductManager;
import model.models.Product;
import model.models.barcode.ProductBarcode;
import model.models.unit.Unit;
import model.models.unit.UnitType;
import controller.context.Context;

/**
 * Controller class for the add item view.
 */
public class AddProductController extends
		Controller implements
		IAddProductController {

	ProductManager pm;
	String barcode;
	Context context;

	/**
	 * Constructor.
	 * 
	 * @pre None
	 * @post Controller created with args
	 * @param view
	 *            Reference to the add product view
	 * @param barcode
	 *            Barcode for the product being added
	 */
	public AddProductController(IView view,
			String barcode) {
		super(view);
		this.context = Context.getInstance();
		this.barcode = barcode;
		this.pm = ProductManager.getInstance();
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
	protected IAddProductView getView() {
		return (IAddProductView) super.getView();
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
		IAddProductView view = getView();
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
		if (this.pm.isValid(new ProductBarcode(
				this.barcode), view
				.getDescription(), shelfLife,
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
		getView().setBarcode(this.barcode);
		getView().setSizeValue("1");
		getView().setSizeUnit(SizeUnits.Count);
		getView().setShelfLife("0");
		getView().setSupply("0");
	}

	//
	// IAddProductController overrides
	//

	/**
	 * This method is called when any of the fields in the add product view is
	 * changed by the user. {@pre None} {@post controller reacted to changed
	 * values}
	 */
	@Override
	public void valuesChanged() {
		enableComponents();
	}

	/**
	 * This method is called when the user clicks the "OK" button in the add
	 * product view. {@pre None} {@post controller reacted to adding product}
	 */
	@Override
	public void addProduct() {
		IAddProductView view = getView();
		if (!ProductBarcode.isValid(this.barcode)) {
			view.displayErrorMessage("Barcode already exists.");
			return;
		}
		ProductBarcode pBarcode = new ProductBarcode(
				this.barcode);
		float amount;
		try {
			amount = Float.parseFloat(view
					.getSizeValue());
		} catch (NumberFormatException e) {
			view.displayErrorMessage("Amount must be a number.");
			return;
		}
		UnitType unitType = SizeUnitConverter
				.SizeUnitsToUnitType(view
						.getSizeUnit());
		if (!Unit.isValid(amount, unitType)) {
			view.displayErrorMessage(unitType
					+ " " + amount);
			return;
		}
		Unit unit = new Unit(amount, unitType);
		int shelfLife, threeMonthSupply;
		try {
			shelfLife = Integer.parseInt(view
					.getShelfLife());
			threeMonthSupply = Integer
					.parseInt(view.getSupply());
		} catch (NumberFormatException e) {
			view.displayErrorMessage("Shelf life and Supply must be numbers.");
			return;
		}

		if (!this.pm.isValid(pBarcode,
				view.getDescription(), shelfLife,
				threeMonthSupply)) {
			view.displayErrorMessage("Invalid input.");
			return;
		}
		assert this.context
				.getSelectedContainer() != null : "";
		// System.out.println("Adding product to " +
		// context.getSelectedContainer());
		Product product = new Product(pBarcode,
				view.getDescription(), unit,
				threeMonthSupply,
				threeMonthSupply,
				this.context
						.getSelectedContainer());
		// Context context =
		// Context.getInstance().setSelectedProduct(selectedProduct);
		this.pm.add(product);

		// System.out.println("HERE : :: " + pm.toString());
	}

	@Override
	public void update(Observable arg0,
			Object arg1) {
		// TODO Auto-generated method stub

	}

}
