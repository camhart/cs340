package model.managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import model.models.productContainer.ProductContainer;
import model.models.productContainer.ProductGroup;
import model.models.unit.Unit;
import controller.notify.ProductContainerNotifier;

/**
 * ProductGroupManager contains a set of all the ProductGroup objects used in
 * the Home Inventory Tracker program. It takes care of adding, removing,
 * editing, and validating ProductGroup objects.
 * 
 * @invariant Singleton
 * @author Cameron Hartmann
 * @author Chris McNeill
 * @author Adam Rogers
 * @author Joel Denning
 * @author Group1
 */
public class ProductGroupManager extends
		Observable implements Saveable {

	private static ProductGroupManager instance = null;

	private Set<ProductGroup> productGroups;

	private ProductGroupManager() {
		this.productGroups = new HashSet<ProductGroup>();
	}

	/**
	 * Gets the instance of the ProductGroupManager. getInstance() either
	 * initializes or returns the ProductGroupManager Singleton depending on
	 * whether it has been created yet.
	 * 
	 * @pre true
	 * @post valid ProductGroupManager Singleton instance returned
	 * @param none
	 * @return ProductGroupManager Singleton instance of ProductGroupManager
	 */
	public static ProductGroupManager getInstance() {
		if (instance == null) {
			instance = new ProductGroupManager();
		}

		return instance;
	}

	/**
	 * Adds newGroup to the ProductGroupManager set. of ProductGroups. This
	 * happens after validation and creation of the ProductGroup.
	 * 
	 * @pre newGroups is a valid ProductGroup.
	 * @post newGroup added successfully to ProductGroupManager set
	 * @param newGroup
	 *            The ProductGroup object to be added to the ProductGroupManager
	 *            set of ProductGroups
	 * @return
	 */
	public void add(ProductGroup newGroup) {

		this.productGroups.add(newGroup);
		newGroup.getParent().addProductGroup(
				newGroup);

		ProductContainerNotifier notifier = new ProductContainerNotifier(
				newGroup);
		setChanged();
		this.notifyObservers(notifier);
	}

	/**
	 * Deletes groupToDelete from the ProductGroupManager's set of ProductGroups
	 * 
	 * @pre groupToDelete is not null.
	 * @post groupToDelete is no longer contained in the ProductGroupManager set
	 *       of ProductGroups
	 * @param groupToDelete
	 *            The ProductGroup being removed.
	 * @return
	 */
	public void delete(ProductGroup groupToDelete) {
		this.productGroups.remove(groupToDelete);
		groupToDelete
				.getParent()
				.deleteProductGroup(groupToDelete);

		ProductContainerNotifier notifier = new ProductContainerNotifier(
				null);
		setChanged();
		this.notifyObservers(notifier);
	}

	/**
	 * Edit ProductGroup with name name from the ProductGroupManager's set of
	 * ProductGroups, changing its threeMonthSupply attribute to
	 * threeMonthSupply
	 * 
	 * @pre toEdit is a valid ProductGroup
	 * @pre newName has been validated by isValid and contains.
	 * @pre unit is has been validated.
	 * @post The ProductGroup specified has been edited according to the
	 *       parameters.
	 * @param name
	 *            The name of the ProductGroup to be edited.
	 * @param threeMonthSupply
	 *            The Unit to change the ProductGroup's threeMonthSupply
	 *            attribute to
	 * @return
	 */
	public void edit(ProductGroup toEdit,
			String newName, Unit unit) {
		String oldName = toEdit.getName();
		toEdit.edit(newName, unit);
		ProductContainerNotifier notifier = new ProductContainerNotifier(
				toEdit, oldName);
		// notifier.setOldName(oldName);
		setChanged();
		this.notifyObservers(notifier);

		ItemManager.getInstance()
				.updateContainerChange(toEdit,
						oldName);
	}

	/**
	 * Removes all ProductGroups. This method is for testing only.
	 * 
	 * @pre True
	 * @post productGroups.size() == 0.
	 */
	public void deleteAll() {
		this.productGroups = new HashSet<ProductGroup>();
		assert (this.productGroups.size() == 0);
	}

	/**
	 * Gets the number of ProductGroups.
	 * 
	 * @pre True
	 * @post True
	 * @return The number of ProductGroups.
	 */
	public int getNumberOfGroups() {
		return this.productGroups.size();
	}

	/**
	 * Checks if a ProductGroup exists with a specified parent.
	 * 
	 * @pre name and parent name are not null or the empty string
	 * @param child
	 *            ProductGroup being tested
	 * @param parent
	 *            Parent ProductContainer
	 * @return True if the ProductGroup exists, false otherwise.
	 */
	public boolean contains(ProductGroup child,
			ProductContainer parent) {
		return parent.containsProductGroup(child
				.getName());
	}

	/**
	 * Checks the given input according to the validation rules of ProductGroup.
	 * name must be non-empty and threeMonthSupply must be non-negative.
	 * 
	 * @pre True
	 * @post The parameters have been validated.
	 * @param name
	 *            The name of the ProductGroup object.
	 * @param threeMonthSupply
	 *            the int value representing the number of Units needed in this
	 *            ProductGroup for a three month supply.
	 * @return boolean. True if the input is valid for creating a ProductGroup.
	 *         False if the input is not valid.
	 */
	public boolean isValid(String name,
			ProductContainer parent, Unit unit) {
		if (!ProductGroup.isValid(name,
				unit.getUnit(), unit.getAmount())) {
			System.out.print("a");
			return false;
		}

		if (parent.containsProductGroup(name)) {
			System.out.print("b");
			return false;
		}

		return true;
	}

	public boolean isValid(String newName,
			String oldName,
			ProductContainer parent, Unit unit) {
		if (!ProductGroup.isValid(newName,
				unit.getUnit(), unit.getAmount())) {
			System.out.print("a");
			return false;
		}

		if (parent.containsProductGroup(newName)
				&& !newName.equals(oldName)) {
			System.out.print("b");
			return false;
		}

		return true;
	}

	/**
	 * Writes the data object to the object output stream
	 * 
	 * @pre True
	 * @post The contents of the productGroups have been written to a
	 *       DataObject.
	 * @param objectOut
	 *            The stream where the data is being written to.
	 */
	@Override
	public void writeObjects(
			ObjectOutputStream objectOut) {
		try {
			DataObject data = new DataObject(
					this.productGroups);
			objectOut.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Reads the object from the ObjectInputStream and initializes it properly.
	 * 
	 * @pre True
	 * @post productGroups contains the ProductGroups from the DataObject read
	 *       in.
	 * @param objectIn
	 *            The stream from which the data is read.
	 */
	@Override
	public void readObjects(
			ObjectInputStream objectIn) {
		try {
			DataObject data = (DataObject) objectIn
					.readObject();
			this.productGroups = data.productGroupSet;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Specifies the file to read/write the data for this manager to.
	 * 
	 * @pre True
	 * @post True
	 * @return The name of the file that will be created to save the
	 *         ProductGroups.
	 */
	@Override
	public String getDataFileName() {
		return "ProductGroup";
	}

	/**
	 * This class must remain static in order to remain Serializable. (the only
	 * other option if we want to remove static is to make ItemManager
	 * Serializable) Used for saving ItemManager data
	 * 
	 * @author Cam
	 */
	private static class DataObject implements
			Serializable {

		Set<ProductGroup> productGroupSet;

		public DataObject(
				Set<ProductGroup> productGroups) {
			this.productGroupSet = productGroups;
		}
	}

	public void clear() {
		this.productGroups.clear();
	}

	public Set<ProductGroup> getProductGroups() {
		return this.productGroups;
	}
}
