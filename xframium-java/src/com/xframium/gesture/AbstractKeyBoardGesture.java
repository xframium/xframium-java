/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
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
