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
package org.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture.InitialDragDropAction;
import org.xframium.gesture.Gesture.Direction;
import org.xframium.gesture.Gesture.GestureType;
import org.xframium.gesture.GestureManager;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;
import org.xframium.reporting.ExecutionContextTest;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSGesture.
 */
public class KWSGesture extends AbstractKeyWordStep
{
	public KWSGesture()
    {
	    kwName = "Perform Gesture";
        kwDescription = "Allows the script to perform a gesture on a touch-enabled device";
        kwHelp = "https://www.xframium.org/keyword.html#kw-gesture";
        orMapping = false;
        category = "Interaction";
        featureId = 33;
    }
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest executionContext )
	{
		if ( log.isDebugEnabled() )
			log.info( "Executing Gesture " + getName() );
		boolean success = false;
		Element gestureElement = null;
			
		DeviceWebDriver wD = (DeviceWebDriver) webDriver;
		
		boolean enableCache = false;
		
		if ( wD.isCachingEnabled() )
		{
		    wD.setCachingEnabled( false );
		    enableCache = true;
		}
		
		String xFID = executionContext.getxFID();
		
		WebElement webElement = null;
		String[] gestureName = getName().split( "\\." );
		if ( gestureName.length == 2 )
		{
			gestureElement = getElement( pageObject, contextMap, webDriver, dataMap, gestureName[ 1 ], executionContext );
			if ( gestureElement != null )
				webElement = (WebElement)gestureElement.getNative();
		}
		
		if ( isTimed() )
            wD.getCloud().getCloudActionProvider().startTimer( (DeviceWebDriver) webDriver, gestureElement, executionContext );
		
		switch( GestureType.valueOf( gestureName[0] ) )
		{
		    case DRAGDROP:
		        
		        Element fromElement = getElement( pageObject, contextMap, webDriver, dataMap, (String) getParameterValue( getParameter( "From" ), contextMap, dataMap, executionContext.getxFID() ), executionContext );
		        Element toElement = getElement( pageObject, contextMap, webDriver, dataMap, (String) getParameterValue( getParameter( "To" ), contextMap, dataMap, executionContext.getxFID() ), executionContext );
		        InitialDragDropAction initialAction = InitialDragDropAction.PRESS;
		        if ( getParameter( "Initial Action" ) != null )
		            initialAction = InitialDragDropAction.valueOf( (String) getParameterValue( getParameter( "Initial Action" ), contextMap, dataMap, executionContext.getxFID() ) );
		        
		        GestureManager.instance(wD).createDragDrop( xFID, initialAction, fromElement, toElement ).executeGesture( webDriver, webElement );
		            
		        
			case PINCH:
				if ( getParameterList().size() == 4 )
				{
					Point[] pPoint = new Point[ 4 ];
					pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 2 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 3 ] = createPoint( (String) getParameterValue( getParameterList().get( 3 ), contextMap, dataMap, executionContext.getxFID() ) );
					GestureManager.instance(wD).createPinch( xFID, pPoint[ 0 ], pPoint[ 1 ], pPoint[ 2 ], pPoint[ 3 ] ).executeGesture( webDriver, webElement );
				}
				else
					GestureManager.instance(wD).createPinch(xFID).executeGesture( webDriver, webElement );
								
				break;
				
			case ZOOM:
				if ( getParameterList().size() == 4 )
				{
					Point[] pPoint = new Point[ 4 ];
					pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 2 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap, executionContext.getxFID() ) );
					pPoint[ 3 ] = createPoint( (String) getParameterValue( getParameterList().get( 3 ), contextMap, dataMap, executionContext.getxFID() ) );
					GestureManager.instance(wD).createPinch( xFID, pPoint[ 0 ], pPoint[ 1 ], pPoint[ 2 ], pPoint[ 3 ] ).executeGesture( webDriver, webElement );
				}
				else
					GestureManager.instance(wD).createZoom(xFID).executeGesture( webDriver, webElement );
				break;
				
			case SWIPE:
				if ( getParameterList().size() > 1 )
				{
					Point[] pPoint = new Point[ 2 ];
					if ( getParameterList().size() == 2 )
					{
						pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) );
						pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) );
					}
					else
					{
						pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) );
						pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap, executionContext.getxFID() ) );
					}
					
					GestureManager.instance(wD).createSwipe( xFID, pPoint[ 0 ], pPoint[ 1 ] ).executeGesture( webDriver, webElement );
					
				}
				else
					GestureManager.instance(wD).createSwipe( xFID, Direction.valueOf( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) ) ).executeGesture( webDriver, webElement );
				
				break;
				
			case PRESS:
			    if ( getParameterList().size() > 1 )
			    {
    			    Point pressPoint = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) );
    				GestureManager.instance(wD).createPress( xFID, pressPoint, 250, Integer.parseInt( getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) + "") ).executeGesture( webDriver, webElement );
			    }
			    else
			    {
			        Point pressPoint = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) );
                    GestureManager.instance(wD).createPress( xFID, pressPoint ).executeGesture( webDriver, webElement );
			    }
				break;
				
			case ROTATE:
				GestureManager.instance(wD).createRotate( xFID, ScreenOrientation.valueOf( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) ) ).executeGesture( webDriver, webElement );
				break;
				
			case KEYPRESS:
				GestureManager.instance(wD).createKeyPress( xFID, ( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap, executionContext.getxFID() ) ), Integer.parseInt( ( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap, executionContext.getxFID() ) ) ) ).executeGesture( webDriver, webElement );
				break;
				
			case HIDE_KEYBOARD:
				GestureManager.instance(wD).createHideKeyboard(xFID).executeGesture( webDriver, webElement );
				break;
		}
		
		success = true;

		
		if ( enableCache )
		    wD.setCachingEnabled( true );
		
		return success;
	}

}
