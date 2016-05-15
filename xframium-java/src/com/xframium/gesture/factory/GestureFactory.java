package com.xframium.gesture.factory;

import java.util.List;
import com.xframium.gesture.Gesture;
import com.xframium.gesture.Gesture.GestureType;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Gesture objects.
 */
public interface GestureFactory
{
	
	/**
	 * Creates a new Gesture object.
	 *
	 * @param gestureType the gesture type
	 * @param parameterList the parameter list
	 * @return the gesture
	 */
	public Gesture createGesture( GestureType gestureType, Object[] parameterList );
}
