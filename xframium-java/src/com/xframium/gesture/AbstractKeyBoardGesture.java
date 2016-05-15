package com.xframium.gesture;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractKeyPressGesture.
 */
public abstract class AbstractKeyBoardGesture extends AbstractGesture
{
	
	/** The show keyboard. */
	private boolean showKeyboard;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#setParameters(java.lang.Object[])
	 */
	public void setParameters( Object[] parameterArray )
	{
		setShown( (boolean) parameterArray[ 0 ] );
	}

	/**
	 * Gets the key code.
	 *
	 * @return the key code
	 */
	public boolean isShown()
	{
		return showKeyboard;
	}

	/**
	 * Sets the key code.
	 *
	 * @param showKeyboard the new shown
	 */
	public void setShown( boolean showKeyboard )
	{
		this.showKeyboard = showKeyboard;
	}	
	
	
	
	
}
