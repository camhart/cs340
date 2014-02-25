package model.managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.NavigableMap;
import java.util.Observable;
import java.util.TreeMap;

import model.models.productContainer.StorageUnit;
import controller.notify.Notifier;
import controller.notify.ProductContainerNotifier;

/**
 * StorageUnitManager contains a set of all the StorageUnit objects used in the
 * Home Inventory Tracker program. It takes care of adding and deleting, and
 * editing StorageUnitManager and validating StorageUnit objects before they are
 * added.
 * 
 * @invariant Singleton
 * @author Cameron Hartmann
 * @author Chris McNeill
 * @author Adam Rogers
 * @author Joel Denning
 * @author Group1
 */
public class StorageUnitManager extends
		Observable implements Saveable {

	private static StorageUnitManager instance = null;

	private NavigableMap<String, StorageUnit> storageUnits;

	private StorageUnitManager() {
		setStorageUnits(new TreeMap<String, StorageUnit>());
	}

	/**
	 * getInstance() either initializes or returns the ItemManager Singleton
	 * depending on whether it has been created yet.
	 * 
	 * @pre true
	 * @post valid StorageUnitManager Singleton instance returned
	 * @param none
	 * @return StorageUnitManager Singleton instance of StorageUnitManager
	 */
	public static StorageUnitManager getInstance() {

		if (instance == null) {
			instance = new StorageUnitManager();
		}

		return instance;
	}

	/**
	 * add(StorageUnit storageUnit) adds product to the StorageUnitManager set
	 * of StorageUnits. This happens after validation and creation of the
	 * StorageUnit. It notifies its observers that the StorageUnit was added.
	 * 
	 * @pre StorageUnit is a valid StorageUnit.
	 * @post storageUnit added successfully to StorageUnitManager set
	 * @param storageUnit
	 *            The StorageUnit object to be added to the StorageUnitManager
	 *            set of StorageUnits
	 * @return
	 */
	public void add(StorageUnit storageUnit) {
		getStorageUnits().put(
				storageUnit.getName(),
				storageUnit);
		// System.out.println("storageUnits: " + getStorageUnits());
		Notifier notifier = new ProductContainerNotifier(
				storageUnit);
		setChanged();
		notifyObservers(notifier);
	}

	/**
	 * edit(String oldName, String newName) edit StorageUnit with name oldName
	 * from the StorageUnitManager's set of StorageUnit's, changing its name
	 * attribute to newName. It notifies its observers that the StorageUnit was
	 * edited.
	 * 
	 * @pre oldName and newName have been validated and checked for uniqueness.
	 * @post The StorageUnit specified has been changed according to the
	 *       parameters.
	 * @param oldName
	 *            The name of the StorageUnit to be edited.
	 * @param newName
	 *            The String to become the new name of the the StorageUnit to be
	 *            edited.
	 * @return
	 */
	public void edit(String oldName,
			String newName) {
		StorageUnit toEdit = getStorageUnits()
				.remove(oldName);
		toEdit.edit(newName);
		getStorageUnits().put(toEdit.getName(),
				toEdit);
		Notifier notifier = new ProductContainerNotifier(
				toEdit);
		setChanged();
		notifyObservers(notifier);
	}

	/**
	 * Deletes the StorageUnit with name name from the StorageUnitManager's set
	 * of StorageUnits. It notifies its observers that the StorageUnit was
	 * deleted.
	 * 
	 * @pre name must be a real storage unit
	 * @post storage unit with name is deleted
	 * @param name
	 *            The name of the StorageUnit being removed.
	 * @return
	 */
	public void delete(String name) {
		getStorageUnits().remove(name);
		Notifier notifier = new ProductContainerNotifier(
				null);
		setChanged();
		notifyObservers(notifier);
	}

	/**
	 * Checks the given input according to the validation rules of StorageUnit.
	 * 
	 * @pre True
	 * @post True
	 * @param name
	 *            The name of the StorageUnit object.
	 * @return boolean. True if the input is valid for creating a StorageUnit.
	 *         False if the input is not valid.
	 */
	public boolean isValid(String name) {

		if (!StorageUnit.isValid(name)) {
			return false;
		}

		if (getStorageUnits().containsKey(name)) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the number of StorageUnit objects.
	 * 
	 * @pre True
	 * @post True
	 * @return The number of StorageUnits.
	 */
	public int getNumStorageUnits() {
		return getStorageUnits().size();
	}

	/**
	 * Checks to see if a StorageUnit with the parameter name exists.
	 * 
	 * @pre name is not null or the empty string.
	 * @post True
	 * @param name
	 *            The name of the StorageUnit being checked for existence.
	 * @return True if a StorageUnit with the name passed in exists, false
	 *         otherwise.
	 */
	public boolean contains(String name) {
		if (name == null) {
			return false;
		}
		if (getStorageUnits().containsKey(name)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deletes all StorageUnit objects. This method is for testing only.
	 * 
	 * @pre True
	 * @post storageUnits.size() == 0.
	 */
	public void deleteAll() {
		setStorageUnits(new TreeMap<String, StorageUnit>());
		assert (getStorageUnits().size() == 0);
	}

	/**
	 * Writes the data object to the object output stream
	 * 
	 * @pre True
	 * @post The contents of the productGroups have been written to a
	 *       DataObject.
	 * @param objectOut
	 *            The stream where the data is being written to.
	 * @param objectIn
	 *            The stream from which the data is read.
	 */
	@Override
	public void writeObjects(
			ObjectOutputStream objectOut) {
		try {
			DataObject data = new DataObject(
					getStorageUnits());
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
	 */
	@Override
	public void readObjects(
			ObjectInputStream objectIn) {
		try {
			DataObject data = (DataObject) objectIn
					.readObject();
			setStorageUnits(data.storageUnitMap);
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
	 *         StorageUnits.
	 */
	@Override
	public String getDataFileName() {
		return "storageUnit";
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
		NavigableMap<String, StorageUnit> storageUnitMap;

		public DataObject(
				NavigableMap<String, StorageUnit> storageUnitMap) {
			this.storageUnitMap = storageUnitMap;
		}
	}

	public void clear() {
		getStorageUnits().clear();
	}

	public NavigableMap<String, StorageUnit> getStorageUnits() {
		return this.storageUnits;
	}

	public void setStorageUnits(
			NavigableMap<String, StorageUnit> storageUnits) {
		this.storageUnits = storageUnits;
	}
}
