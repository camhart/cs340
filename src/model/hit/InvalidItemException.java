package model.hit;

/**
 * indicates that an item cannot be created because it is invalid
 * 
 * @author Group 1
 */
public class InvalidItemException extends
		Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public InvalidItemException(String message) {
		super(message);
	}

}
