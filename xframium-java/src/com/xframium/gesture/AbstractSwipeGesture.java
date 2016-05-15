package com.xframium.gesture;

import org.openqa.selenium.Point;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractSwipeGesture.
 */
public abstract class AbstractSwipeGesture extends AbstractGesture
{
	
	/** The swipe start. */
	private Point swipeStart;
	
	/** The swipe end. */
	private Point swipeEnd;
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.Gesture#setParameters(java.lang.Object[])
	 */
	public void setParameters( Object[] parameterArray )
	{
		setSwipeStart( (Point) parameterArray[ 0 ] );
		setSwipeEnd( (Point) parameterArray[ 1 ] );
	}
	
	/**
	 * Gets the swipe start.
	 *
	 * @return the swipe start
	 */
	public Point getSwipeStart()
	{
		return swipeStart;
	}
	
	/**
	 * Sets the swipe start.
	 *
	 * @param swipeStart the new swipe start
	 */
	public void setSwipeStart( Point swipeStart )
	{
		this.swipeStart = swipeStart;
	}
	
	/**
	 * Gets the swipe end.
	 *
	 * @return the swipe end
	 */
	public Point getSwipeEnd()
	{
		return swipeEnd;
	}
	
	/**
	 * Sets the swipe end.
	 *
	 * @param swipeEnd the new swipe end
	 */
	public void setSwipeEnd( Point swipeEnd )
	{
		this.swipeEnd = swipeEnd;
	}
	
	
}
