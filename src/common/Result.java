package common;

/**
 * Represents the result of an operation.
 */
public class Result {

	/**
	 * Status attribute. Indicates success or failure of the operation.
	 */
	private boolean _status;

	/**
	 * Message attribute. Describes the reason for failure (empty on success).
	 */
	private String _message;

	/**
	 * Constructor.
	 * 
	 * @pre None
	 * 
	 * @post getStatus() == false, getMessage() == ""
	 */
	public Result() {
		this._status = false;
		this._message = "";
	}

	/**
	 * Constructor.
	 * 
	 * @param status
	 *            Initial value of Status attribute.
	 * 
	 * @pre None
	 * 
	 * @post getStatus() == status, getMessage() == ""
	 */
	public Result(boolean status) {
		this._status = status;
		this._message = "";
	}

	/**
	 * Constructor.
	 * 
	 * @param status
	 *            Initial value of Status attribute.
	 * @param message
	 *            Initial value of Message attribute.
	 * 
	 * @pre None
	 * 
	 * @post getStatus() == status, getMessage() == message
	 */
	public Result(boolean status, String message) {
		this._status = status;
		this._message = message;
	}

	/**
	 * Copy Constructor.
	 * 
	 * @param other
	 *            Object to be copied.
	 * 
	 *            {@pre other != null}
	 * 
	 *            {@post this is a copy of other}
	 */
	public Result(Result other) {
		this._status = other._status;
		this._message = other._message;
	}

	/**
	 * Returns value of Status attribute.
	 * 
	 * @pre None
	 * 
	 * @post Returns value of Status attribute.
	 */
	public boolean getStatus() {
		return this._status;
	}

	/**
	 * Sets value of Status attribute.
	 * 
	 * @param status
	 *            New value of Status attribute
	 * 
	 * @pre None
	 * 
	 * @post getStatus() == status
	 */
	public void setStatus(boolean status) {
		this._status = status;
	}

	/**
	 * Returns value of Message attribute.
	 * 
	 * @pre None
	 * 
	 * @post Returns value of Message attribute.
	 */
	public String getMessage() {
		return this._message;
	}

	/**
	 * Sets value of Message attribute.
	 * 
	 * @param message
	 *            New value of Message attribute
	 * 
	 * @pre None
	 * 
	 * @post getMessage() == message
	 */
	public void setMessage(String message) {
		this._message = message;
	}

	/**
	 * Appends string to Message attribute.
	 * 
	 * @param message
	 *            String to be appended to Message attribute.
	 * 
	 *            {@pre message != null}
	 * 
	 *            {@post getMessage() == old(getMessage()) + message}
	 */
	public void appendMessage(String message) {
		this._message += message;
	}

	/**
	 * Returns true if Message is non-null and non-empty, and false otherwise.
	 * 
	 * @pre None
	 * 
	 * @post Returns true if Message is non-null and non-empty, and false
	 *       otherwise.
	 */
	public boolean hasMessage() {
		return !StringOps
				.isNullOrEmpty(this._message);
	}

	/**
	 * Sets the Message using information from the specified exception.
	 * 
	 * @param e
	 *            Exception used to initialize Message
	 * 
	 *            {@pre e != null}
	 * 
	 *            {@post Message contains the message and stack trace contained
	 *            in e.}
	 */
	public void setFrom(Exception e) {
		setStatus(false);
		setMessage(e.getMessage());

		for (StackTraceElement ste : e
				.getStackTrace()) {
			appendMessage("\n" + ste.toString());
		}
	}

}
