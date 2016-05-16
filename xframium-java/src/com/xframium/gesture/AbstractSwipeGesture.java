/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
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
