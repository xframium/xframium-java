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
