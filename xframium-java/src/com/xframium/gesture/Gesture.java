package com.xframium.gesture;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

// TODO: Auto-generated Javadoc
/**
 * The Interface Gesture.
 */
public interface Gesture
{
	
	/**
	 * The Enum GestureType.
	 */
	public enum GestureType
	{
		
		/** The swipe. */
		SWIPE,
		
		/** The press. */
		PRESS,
		
		/** The zoom. */
		ZOOM,
		
		/** The pinch. */
		PINCH,
		
		/** The rotate. */
		ROTATE,
		
		/** The keypress. */
		KEYPRESS,
		
		/** The keypress. */
		HIDE_KEYBOARD;
	}
	
	/**
	 * The Enum Direction.
	 */
	public enum Direction
	{
		
		/** The up. */
		UP,
		
		/** The down. */
		DOWN,
		
		/** The left. */
		LEFT,
		
		/** The right. */
		RIGHT
	}
	
	/**
	 * Execute gesture.
	 *
	 * @param webDriver the web driver
	 * @return true, if successful
	 */
	public boolean executeGesture( WebDriver webDriver );
	
	/**
	 * Execute gesture.
	 *
	 * @param webDriver the web driver
	 * @param webElement the web element
	 * @return true, if successful
	 */
	public boolean executeGesture( WebDriver webDriver, WebElement webElement );
	
	/**
	 * Sets the parameters.
	 *
	 * @param parameterArray the new parameters
	 */
	public void setParameters( Object[] parameterArray );
	
}
