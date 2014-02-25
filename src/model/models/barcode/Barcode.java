package model.models.barcode;

import java.io.Serializable;

/**
 * Abstract parent class to ItemBarcode and ProductBarcode
 * 
 * @author Group1
 */
public abstract class Barcode implements
		Serializable, Comparable<Object> {

	protected final String barcode;

	protected Barcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * @pre true
	 * @post true Returns a string representation of the barcode
	 */
	@Override
	public String toString() { // remember to remove below
		// return barcode + "[" + hashCode() + "]";
		return this.barcode;
	}

	@Override
	public abstract boolean equals(Object bc);

	// if(bc != null && bc instanceof Barcode) {
	// return hashCode() == ((Barcode)bc).hashCode();
	// }
	// return false;
	// assert (bc instanceof Barcode);
	// boolean ret = false;
	// if (barcode.equalsIgnoreCase(((Barcode) bc).toString()))
	// ret = true;
	// return ret;

	@Override
	public abstract int hashCode();

	@Override
	public int compareTo(Object obj) {
		if ((obj == null)
				|| !(obj instanceof Barcode)) {
			return -1;
		}
		Barcode other = (Barcode) obj;
		return this.barcode
				.compareTo(other.barcode);
	}

}
