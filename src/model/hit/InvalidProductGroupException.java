package model.hit;

/**
 * indicates that a ProductGroup is invalid-- that is that it already exists or
 * that it has invalid parameters
 * 
 * @author Group1
 */
public class InvalidProductGroupException extends
		Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public InvalidProductGroupException(
			String message) {
		super(message);
	}
}
