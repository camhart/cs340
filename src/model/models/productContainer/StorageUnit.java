package model.models.productContainer;

import java.io.Serializable;

/**
 * Holds Products and Items.
 * 
 * @author Chris McNeill
 */
public class StorageUnit extends ProductContainer
		implements Serializable {
	/**
	 * Creates a new StorageUnit object.
	 * 
	 * @pre StorageUnit's isValid returned true for the parameters.
	 * @post A new StorageUnit object.
	 * @param name
	 *            The name of the storage unit.
	 */
	public StorageUnit(String name) {
		super(name);
		// this.name = name;
	}

	/**
	 * Changes the StorageUnit's information to match the parameters.
	 * 
	 * @pre StorageUnit's isValid returned true for the parameters.
	 * @pre newName is unique among all StorageUnits.
	 * @post This StorageUnit's information matches the parameters.
	 * 
	 * @param newName
	 *            The new name of the StorageUnit.
	 */
	public void edit(String newName) {
		this.name = newName;
	}

	/**
	 * Checks to see if the input provided from the user is valid for a
	 * StorageUnit.
	 * 
	 * @pre True
	 * @post name is not null or an empty string.
	 * @param name
	 *            The name of the StorageUnit.
	 * @return True if all input is valid StorageUnit information, false
	 *         otherwise.
	 */
	public static boolean isValid(String name) {
		if (name == null) {
			return false;
		}

		if (name.isEmpty()) {
			return false;
		}

		return true;
	}

	/**
	 * Creates a hash-code for this StorageUnit.
	 * 
	 * @pre True
	 * @post True
	 * @return A hash-code corresponding to this StorageUnit.
	 */
	@Override
	public int hashCode() {
		if (this.name == null) {
			return new String(
					"root - null name in storage unit")
					.hashCode();
		}
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		int myHash = hashCode();

		if ((obj == null)
				|| (obj.getClass() != StorageUnit.class)) {
			return false;
		}
		int theirHash = ((StorageUnit) obj)
				.hashCode();

		return (myHash == theirHash);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String getParentName() {
		return this.name;
	}
}
