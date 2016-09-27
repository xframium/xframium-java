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
package org.xframium.gesture;

import java.util.ArrayList;
import java.util.List;
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
		SWIPE(1, "SWIPE", "Swipe your finger"),
		
		/** The press. */
		PRESS(2, "PRESS", "Tap on the screen"),
		
		/** The zoom. */
		ZOOM(3, "ZOOM", "Two finger zoom in"),
		
		/** The pinch. */
		PINCH(4, "PINCH", "Two finger zoom out"),
		
		/** The rotate. */
		ROTATE(5, "ROTATE", "Rotate the device"),
		
		/** The keypress. */
		KEYPRESS(6, "KEYPRESS", "Send a key press"),
		
		/** The keypress. */
		HIDE_KEYBOARD(7, "HIDE_KEYBOARD", "Hide the keyboard");
	    
	    private GestureType( int id, String name, String description )
	    {
	        this.id = id;
	        this.name= name;
	        this.description = description;
	    }
	    
	    private int id;
	    private String name;
	    private String description;
	    
	    public List<GestureType> getSupportedGestures()
	    {
	        List<GestureType> gestureList = new ArrayList<GestureType>( 10 );
	        gestureList.add( GestureType.SWIPE );
	        gestureList.add( GestureType.PRESS );
	        gestureList.add( GestureType.ZOOM );
	        gestureList.add( GestureType.PINCH );
	        gestureList.add( GestureType.ROTATE );
	        gestureList.add( GestureType.KEYPRESS );
	        gestureList.add( GestureType.HIDE_KEYBOARD );
	        return gestureList;
	    }
	    
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
