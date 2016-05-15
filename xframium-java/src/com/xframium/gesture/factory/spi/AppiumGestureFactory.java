package com.xframium.gesture.factory.spi;

import com.xframium.gesture.Gesture;
import com.xframium.gesture.Gesture.GestureType;
import com.xframium.gesture.factory.AbstractGestureFactory;
import com.xframium.gesture.factory.GestureFactory;
import com.xframium.gesture.factory.spi.appium.KeyBoardGesture;
import com.xframium.gesture.factory.spi.appium.KeyPressGesture;
import com.xframium.gesture.factory.spi.appium.PressGesture;
import com.xframium.gesture.factory.spi.appium.RotateGesture;
import com.xframium.gesture.factory.spi.appium.SwipeGesture;
import com.xframium.gesture.factory.spi.appium.TwoFingerGesture;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AppiumGesture objects.
 */
public class AppiumGestureFactory extends AbstractGestureFactory implements GestureFactory
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
				
				
			case HIDE_KEYBOARD:
				returnGesture = new KeyBoardGesture();
				returnGesture.setParameters( parameterList );
				break;
		}
		
		return returnGesture;
	}
}
