package model.models.unit;

import java.io.Serializable;

/**
 * Used to specify the unit type and amount of a given product.
 * 
 * @author Group1
 */
public class Unit implements Serializable {

	private float amount;

	private UnitType unit;

	/**
	 * @pre amount >= 0
	 * @pre unit != null
	 * @post true
	 * @param amount
	 * @param unit
	 */
	public Unit(float amount, UnitType unit) {
		assert (Unit.isValid(amount, unit));
		this.amount = amount;
		this.unit = unit;
	}

	/**
	 * @pre true
	 * @post valid amount >= 0
	 * @return amount
	 */
	public float getAmount() {
		return this.amount;
	}

	/**
	 * @pre amount >= 0
	 * @post true
	 * @param amount
	 *            for this unit
	 */
	public void setAmount(float amount) {
		assert Unit.isValid(amount, this.unit);
		this.amount = amount;
	}

	/**
	 * @pre true
	 * @post valid UnitType
	 * @return UnitType for this Unit
	 */
	public UnitType getUnit() {
		return this.unit;
	}

	/**
	 * @pre unit != null
	 * @post true
	 * @param unit
	 *            set the unit-type for this Unit
	 */
	public void setUnit(UnitType unit) {
		assert Unit.isValid(this.amount, unit);
		this.unit = unit;
	}

	/**
	 * Checks to see if the parameters for a Unit are valid
	 * 
	 * @pre True
	 * @post True
	 * @param amount
	 *            The number amount for the Unit
	 * @param unit
	 *            The UnitType.
	 * @return True if the parameters are valid for a Unit object, false
	 *         otherwise.
	 */
	public static boolean isValid(float amount,
			UnitType unit) {
		if (amount < 0) {
			return false;
		}

		if (unit == UnitType.Count) {
			int intAmt = (int) amount;
			if (amount != intAmt) {
				return false;
			}
		}

		if (unit == null) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the String of the unit formatted properly
	 * 
	 * @pre amount and unit are non null
	 * @post true
	 * @return unit string
	 */
	public String getUnitString() {
		String amtAsString = null;
		if (this.unit == UnitType.Count) {
			amtAsString = Integer
					.toString((int) this.amount);
		} else {
			amtAsString = Float
					.toString(this.amount);
		}
		return amtAsString + " "
				+ this.unit.toString();
	}
}