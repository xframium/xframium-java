package com.xframium.gesture;

import org.openqa.selenium.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractTwoFingerGesture.
 */
public abstract class AbstractTwoFingerGesture extends AbstractGesture
{
	
	/** The start one. */
	private Point startOne;
	
	/** The start two. */
	private Point startTwo;
	
	/** The end one. */
	private Point endOne;
	
	/** The end two. */
	private Point endTwo;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#setParameters(java.lang.Object[])
	 */
	@Override
	public void setParameters( Object[] parameterArray )
	{
		startOne = (Point) parameterArray[ 0 ];
		startTwo = (Point) parameterArray[ 1 ];
		endOne = (Point) parameterArray[ 2 ];
		endTwo = (Point) parameterArray[ 3 ];
		
	}
	
	/**
	 * Gets the start one.
	 *
	 * @return the start one
	 */
	public Point getStartOne()
	{
		return startOne;
	}
	
	/**
	 * Sets the start one.
	 *
	 * @param startOne the new start one
	 */
	public void setStartOne( Point startOne )
	{
		this.startOne = startOne;
	}
	
	/**
	 * Gets the start two.
	 *
	 * @return the start two
	 */
	public Point getStartTwo()
	{
		return startTwo;
	}
	
	/**
	 * Sets the start two.
	 *
	 * @param startTwo the new start two
	 */
	public void setStartTwo( Point startTwo )
	{
		this.startTwo = startTwo;
	}
	
	/**
	 * Gets the end one.
	 *
	 * @return the end one
	 */
	public Point getEndOne()
	{
		return endOne;
	}
	
	/**
	 * Sets the end one.
	 *
	 * @param endOne the new end one
	 */
	public void setEndOne( Point endOne )
	{
		this.endOne = endOne;
	}
	
	/**
	 * Gets the end two.
	 *
	 * @return the end two
	 */
	public Point getEndTwo()
	{
		return endTwo;
	}
	
	/**
	 * Sets the end two.
	 *
	 * @param endTwo the new end two
	 */
	public void setEndTwo( Point endTwo )
	{
		this.endTwo = endTwo;
	}
	
	
}
