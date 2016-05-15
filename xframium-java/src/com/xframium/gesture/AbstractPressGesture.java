package com.xframium.gesture;

import org.openqa.selenium.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractPressGesture.
 */
public abstract class AbstractPressGesture extends AbstractGesture
{
	
	/** The press position. */
	private Point pressPosition;
	
	/** The press length. */
	private int pressLength;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#setParameters(java.lang.Object[])
	 */
	public void setParameters( Object[] parameterArray )
	{
		setPressPosition( (Point) parameterArray[ 0 ] );
		setPressLength( ( (Long) parameterArray[ 1 ] ).intValue() );
	}
	
	/**
	 * Gets the press position.
	 *
	 * @return the press position
	 */
	public Point getPressPosition()
	{
		return pressPosition;
	}
	
	/**
	 * Sets the press position.
	 *
	 * @param pressPosition the new press position
	 */
	public void setPressPosition( Point pressPosition )
	{
		this.pressPosition = pressPosition;
	}
	
	/**
	 * Gets the press length.
	 *
	 * @return the press length
	 */
	public int getPressLength()
	{
		return pressLength;
	}
	
	/**
	 * Sets the press length.
	 *
	 * @param pressLength the new press length
	 */
	public void setPressLength( int pressLength )
	{
		this.pressLength = pressLength;
	}
	
	
}
