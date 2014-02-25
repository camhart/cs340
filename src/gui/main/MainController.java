package gui.main;

import gui.common.Controller;

import java.util.Observable;

import model.managers.SerializationManager;

/**
 * Controller class for the main view. The main view allows the user to print
 * reports and exit the application.
 */
public class MainController extends Controller
		implements IMainController {

	/**
	 * Constructor.
	 * 
	 * @pre view is a non null MainView object
	 * @post true
	 * @param view
	 *            Reference to the main view
	 */
	public MainController(IMainView view) {
		super(view);
		construct();
	}

	/**
	 * Returns a reference to the view for this controller.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	protected IMainView getView() {
		return (IMainView) super.getView();
	}

	//
	// IMainController overrides
	//

	/**
	 * Returns true if and only if the "Exit" menu item should be enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canExit() {
		return true;
	}

	/**
	 * This method is called when the user exits the application.
	 * 
	 * @pre true
	 * @post true *
	 */
	@Override
	public void exit() {

		SerializationManager.getInstance()
				.saveState();
	}

	/**
	 * Returns true if and only if the "Expired Items" menu item should be
	 * enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canPrintExpiredReport() {
		return false;
	}

	/**
	 * This method is called when the user selects the "Expired Items" menu
	 * item.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void printExpiredReport() {
		getView().displayExpiredReportView();
	}

	/**
	 * Returns true if and only if the "N-Month Supply" menu item should be
	 * enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canPrintSupplyReport() {
		return false;
	}

	/**
	 * This method is called when the user selects the "N-Month Supply" menu
	 * item.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void printSupplyReport() {
		getView().displaySupplyReportView();
	}

	/**
	 * Returns true if and only if the "Product Statistics" menu item should be
	 * enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canPrintProductReport() {
		return false;
	}

	/**
	 * This method is called when the user selects the "Product Statistics" menu
	 * item.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void printProductReport() {
		getView().displayProductReportView();
	}

	/**
	 * Returns true if and only if the "Notices" menu item should be enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canPrintNoticesReport() {
		return false;
	}

	/**
	 * This method is called when the user selects the "Notices" menu item.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void printNoticesReport() {
		getView().displayNoticesReportView();
	}

	/**
	 * Returns true if and only if the "Removed Items" menu item should be
	 * enabled.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public boolean canPrintRemovedReport() {
		return false;
	}

	/**
	 * This method is called when the user selects the "Removed Items" menu
	 * item.
	 * 
	 * @pre true
	 * @post true
	 */
	@Override
	public void printRemovedReport() {
		getView().displayRemovedReportView();
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
