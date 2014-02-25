package gui.common;

import model.models.unit.UnitType;

public class SizeUnitConverter {

	public SizeUnitConverter() {
	}

	public static UnitType SizeUnitsToUnitType(
			SizeUnits unit) {
		UnitType rval = null;
		switch (unit) {
			case Count:
				rval = UnitType.Count;
				break;
			case Pounds:
				rval = UnitType.Pounds;
				break;
			case Ounces:
				rval = UnitType.Ounces;
				break;
			case Grams:
				rval = UnitType.Grams;
				break;
			case Kilograms:
				rval = UnitType.Kilograms;
				break;
			default:
				rval = null;
				break;
		}

		if (rval == null) {
			rval = fluidUnitTypes(unit);
		}

		return rval;
	}

	public static SizeUnits UnitTypeToSizeUnits(
			UnitType type) {

		SizeUnits rval = null;

		switch (type) {
			case Count:
				rval = SizeUnits.Count;
				break;
			case Pounds:
				rval = SizeUnits.Pounds;
				break;
			case Ounces:
				rval = SizeUnits.Ounces;
				break;
			case Grams:
				rval = SizeUnits.Grams;
				break;
			case Kilograms:
				rval = SizeUnits.Kilograms;
				break;
			default:
				rval = null;
				break;
		}

		if (rval == null) {
			rval = fluidSizeUnits(type);
		}

		return rval;
	}

	private static UnitType fluidUnitTypes(
			SizeUnits unit) {
		UnitType rval = null;

		switch (unit) {
			case Gallons:
				rval = UnitType.Gallons;
				break;
			case Quarts:
				rval = UnitType.Quarts;
				break;
			case Pints:
				rval = UnitType.Pints;
				break;
			case FluidOunces:
				rval = UnitType.FluidOunces;
				break;
			case Liters:
				rval = UnitType.Liters;
				break;
		}

		return rval;
	}

	private static SizeUnits fluidSizeUnits(
			UnitType type) {
		SizeUnits rval = null;

		return rval;
	}
}
