package model.models.unit;

import java.io.Serializable;

/**
 * Specifies the unit type for a given product.
 * 
 * @author Group1
 */
public enum UnitType implements Serializable {
	Count, Pounds, Ounces, Grams, Kilograms, Gallons, Quarts, Pints, FluidOunces, Liters
}