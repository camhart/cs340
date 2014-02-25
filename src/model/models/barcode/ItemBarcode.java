package model.models.barcode;

import java.io.Serializable;

/**
 * Used as a barcode for individual items.
 * 
 * @author Group1
 */
public class ItemBarcode extends Barcode
		implements Serializable {

	public static final ItemBarcode BOGUS_ITEMBARCODE = new ItemBarcode(
			"000000000000");

	/**
	 * @pre barcode is a valid item barcode. Inorder to be a valid item barcode
	 *      it must be a valid UPC barcode and unique.
	 * @post true
	 * @param barcode
	 */
	public ItemBarcode(String barcode) {
		super(barcode);
		// assert isValid(barcode); //took out for testing bad itembarcodes
	}

	/**
	 * Used to validate item barcode strings. Inorder to be a valid item barcode
	 * it must be a valid UPC barcode and unique.
	 * 
	 * @param barcode
	 *            the barcode string to validate
	 * @return true if the barcode string is a valid item barcode
	 */
	public static boolean isValid(String barcode) {
		if (barcode == null) {
			return false;
		}
		int evenValues = 0;
		int oddValues = 0;
		if (barcode.length() == 12) {
			if (!Character
					.isDigit(barcode.charAt(barcode
							.length() - 1))) {
				return false;
			}
			for (int c = 0; c < (barcode.length() - 1); c++) {
				if (!Character.isDigit(barcode
						.charAt(c))) {
					return false;
				} else {
					if ((c % 2) == 0) {
						oddValues += Integer
								.parseInt(Character
										.toString(barcode
												.charAt(c)));
					} else {
						evenValues += Integer
								.parseInt(Character
										.toString(barcode
												.charAt(c)));
					}
				}
			}
			int checkSum = oddValues * 3;
			checkSum += evenValues;
			checkSum = checkSum % 10;
			checkSum = 10 - checkSum;
			// possibly wrong
			if (checkSum == 10) {
				checkSum = 0;
			}
			// end
			if (checkSum == Integer
					.parseInt(Character.toString(barcode.charAt(barcode
							.length() - 1)))) {
				return true;
			}
			System.out.println(barcode
					+ " should be "
					+ barcode.substring(0,
							barcode.length() - 1)
					+ checkSum);
			System.exit(0);
			return false;
		}
		return false;
	}

	/**
	 * Used to validate item barcode's. Inorder to be a valid item barcode it
	 * must be a valid UPC barcode and unique.
	 * 
	 * @param barcode
	 *            the barcode string to validate
	 * @return true if the barcode string is a valid item barcode
	 */
	public static boolean isValid(
			ItemBarcode barcode) {
		if (barcode == null) {
			return false;
		}
		return isValid(barcode.barcode);
	}

	/**
	 * Used to compare equality of objects
	 * 
	 * @pre true
	 * @post true
	 * @param obj
	 *            object to compare equality to
	 * @return true if obj is equal to this object
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != ItemBarcode.class)) {
			return false;
		}
		ItemBarcode other = (ItemBarcode) obj;

		// return other.barcode.equals(barcode);
		return other.hashCode() == hashCode();
	}

	@Override
	public int hashCode() {
		return this.barcode.hashCode();
	}
}
