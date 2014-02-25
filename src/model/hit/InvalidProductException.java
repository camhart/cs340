package model.hit;

/**
 * Indicates invalid Product
 * 
 * @author Group1
 */
public class InvalidProductException extends
		Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public InvalidProductException(String message) {
		super(message);
	}
}
