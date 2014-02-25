package gui.common;

import java.awt.GridBagConstraints;

/**
 * GridBagConstraintsExt is an extension of {@link java.awt.GridBagConstraints
 * GridBagConstraints} with an additional operation which makes it easy to reset
 * the (x, y, width, height) properties in one shot.
 */
@SuppressWarnings("serial")
public class GridBagConstraintsExt extends
		GridBagConstraints {

	/**
	 * Constructs an empty GridBagConstraintsExt object.
	 * 
	 * {@pre None}
	 * 
	 * {@post Members have been initialized to default values.}
	 */
	public GridBagConstraintsExt() {
		super();
	}

	/**
	 * Sets the (x, y, width, height) properties of the object in one shot.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * 
	 *            {@pre None}
	 * 
	 *            {@post (x, y, width, height) properties have been set to the
	 *            specified values.}
	 */
	public void place(int x, int y, int width,
			int height) {
		this.gridx = x;
		this.gridy = y;
		this.gridwidth = width;
		this.gridheight = height;
	}

}
