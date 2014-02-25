package model.models.productContainer;

import java.io.Serializable;

import model.models.unit.Unit;
import model.models.unit.UnitType;

/**
 * ProductGroup holds Products and Items.
 * 
 * @author Chris McNeill
 */
public class ProductGroup extends
		ProductContainer implements Serializable {
	private ProductContainer parent;

	private Unit threeMonthSupply;

	/**
	 * Constructs a new ProductGroup
	 * 
	 * @pre The parameters have been checked with ProductGroup's isValid method.
	 * @post A new ProductGroup with the information contained in the
	 *       parameters.
	 * 
	 * @param name
	 *            The name of the ProductGroup.
	 * @param parent
	 *            The parent ProductContainer.
	 * @param threeMonthSupply
	 *            The three-month supply.
	 */
	public ProductGroup(String name,
			ProductContainer parent, Unit unit) {
		super(name);
		assert parent != null;
		// this.name = name;
		this.parent = parent;
		this.threeMonthSupply = unit;
	}

	/**
	 * Changes the ProductGroup to contain the information in the parameters.
	 * 
	 * @pre The parameters have been checked with ProductGroup's isValid method.
	 * @post The ProductGroup's information matches the parameters.
	 * @param newName
	 *            The new name of the ProductGroup.
	 * @param unit
	 *            the new three-month supply amount.
	 */
	public void edit(String newName, Unit unit) {
		this.name = newName;
		this.threeMonthSupply = unit;
	}

	/**
	 * Checks if the unit of the user is valid for a ProductGroup.
	 * 
	 * @pre True
	 * @post If returns true, name is not be null or an empty string.
	 * @post If returns true, threeMonthSupply's amount is >= 0. If the UnitType
	 *       is count, the amount must be a whole number.
	 * @param name
	 *            The name of the ProductGroup.
	 * @param unitType
	 *            The type of the unit.
	 * @param threeMonthSuply
	 *            The three-month supply amount.
	 * @return True if parameters are valid for a ProductGroup, false otherwise.
	 */
	public static boolean isValid(String name,
			UnitType unitType,
			float threeMonthSupply) {
		if ((name == null) || name.isEmpty()) {
			System.out.println("a");
			return false;
		}

		if (unitType == null) {
			System.out.println("b");
			return false;
		}

		if (threeMonthSupply < 0) {
			System.out.println("c");
			return false;
		}

		if (unitType == UnitType.Count) {
			String amountAsString = Float
					.toString(threeMonthSupply);
			// Do we want this !amountAsString.endsWith()? Since its coming from
			// a float all values have the '.' (it'll come out as 3.0)

			if (!amountAsString.endsWith(".0")
					&& amountAsString
							.contains(".")) {
				System.out.println("d"
						+ amountAsString);
				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the three-month supply.
	 * 
	 * @pre True
	 * @pre True
	 * @return The three-month supply.
	 */
	public Unit GetThreeMonthSupply() {
		return this.threeMonthSupply;
	}

	/**
	 * Gets the parent ProductContainer for this ProductGroup
	 * 
	 * @pre True
	 * @post True
	 * @return the parent ProductContainer.
	 */
	public ProductContainer getParent() {
		return this.parent;
	}

	/**
	 * Creates a hash-code for this ProductGroup.
	 * 
	 * @pre True
	 * @post True
	 * @return A hash-code.
	 */
	@Override
	public int hashCode() {
		// <<<<<<< HEAD
		//
		// //System.out.println("hashcode");
		// =======
		if ((this.name == null)
				|| (this.parent == null)) {
			return 1;
		}
		// >>>>>>> 373f842cf4cac40f77d1ff8db8e2af6381378974 .close()
		assert this.name != null : "null productGroup name but parent is "
				+ this.parent.name;
		assert this.parent != null : "null parent but name is "
				+ this.name; // new
		// ProductGroup(
		return this.name.hashCode()
				+ this.parent.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != ProductGroup.class)) {
			return false;
		} else {
			return (hashCode() == ((ProductGroup) obj)
					.hashCode());
		}
	}

	@Override
	public String toString() {
		if (this.parent == null) {
			return "->" + this.name;
		}
		return this.parent.toString() + "->"
				+ this.name;
	}

	@Override
	public String getParentName() {
		return this.parent.getParentName() + "->"
				+ this.name;
	}
}
