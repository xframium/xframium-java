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
package com.xframium.gesture.factory.spi;

import com.xframium.gesture.Gesture;
import com.xframium.gesture.Gesture.GestureType;
import com.xframium.gesture.factory.AbstractGestureFactory;
import com.xframium.gesture.factory.GestureFactory;
import com.xframium.gesture.factory.spi.perfecto.KeyPressGesture;
import com.xframium.gesture.factory.spi.perfecto.PressGesture;
import com.xframium.gesture.factory.spi.perfecto.RotateGesture;
import com.xframium.gesture.factory.spi.perfecto.SwipeGesture;
import com.xframium.gesture.factory.spi.perfecto.TwoFingerGesture;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating SeleniumGesture objects.
 */
public class PerfectoGestureFactory extends AbstractGestureFactory implements GestureFactory
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.gesture.factory.GestureFactory#createGesture(com.perfectoMobile.gesture.Gesture.GestureType, java.lang.Object[])
	 */
	public Gesture createGesture( GestureType gestureType, Object[] parameterList )
	{
		Gesture returnGesture = null;
		switch( gestureType )
		{
			case PRESS:
				returnGesture = new PressGesture();
				returnGesture.setParameters( parameterList );
				break;
				
			case SWIPE:
				returnGesture = new SwipeGesture();
				returnGesture.setParameters( parameterList );
				break;
				
			case PINCH:
			case ZOOM:
				returnGesture = new TwoFingerGesture();
				returnGesture.setParameters( parameterList );
				break;
				
			case ROTATE:
				returnGesture = new RotateGesture();
				returnGesture.setParameters( parameterList );
				break;
				
			case KEYPRESS:
				returnGesture = new KeyPressGesture();
				returnGesture.setParameters( parameterList );
				break;
		}
		
		return returnGesture;
	}
}
