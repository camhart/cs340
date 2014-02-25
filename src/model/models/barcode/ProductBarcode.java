package model.models.barcode;

import java.io.Serializable;

/**
 * Used as a barcode for individual products.
 * 
 * @author Group1
 */
public class ProductBarcode extends Barcode
		implements Serializable {

	/**
	 * @pre barcode is a valid non-empty numeric product barcode string
	 * @post true
	 * @param barcode
	 */
	public ProductBarcode(String barcode) {
		super(barcode);
	}

	/**
	 * Used to validate product barcodes. In order to be a valid product barcode
	 * it must be a non empty numeric string.
	 * 
	 * @pre true
	 * @post correct boolean value for validity of barcode
	 * @param barcode
	 *            the barcode string to validate
	 * @return true if barcode string is a valid product barcode
	 */
	public static boolean isValid(String barcode) {
		assert true;
		if ((barcode == null)
				|| barcode.equals("")) {
			return false;
		}
		if (!barcode.matches("[0-9]*")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null)
				|| (obj.getClass() != ProductBarcode.class)) {
			return false;
		}
		ProductBarcode other = (ProductBarcode) obj;

		return other.hashCode() == hashCode();// other.barcode.equals(barcode);
	}

	int hashcode = -1;// for debugging purposes

	@Override
	public int hashCode() {
		this.hashcode = this.barcode.hashCode();
		return this.hashcode;
	}

}
