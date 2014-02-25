package model.hit;

/**
 * indicates invalid storage unit
 * 
 * @author Joel
 */
public class InvalidStorageUnitException extends
		Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public InvalidStorageUnitException(
			String message) {
		super(message);
	}
}
