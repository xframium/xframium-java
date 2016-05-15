package com.xframium.page.keyWord.step.spi;

import java.util.Map;

import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.xframium.gesture.GestureManager;
import com.xframium.gesture.Gesture.Direction;
import com.xframium.gesture.Gesture.GestureType;
import com.xframium.page.Page;
import com.xframium.page.data.PageData;
import com.xframium.page.element.Element;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSGesture.
 */
public class KWSGesture extends AbstractKeyWordStep
{
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		if ( log.isDebugEnabled() )
			log.info( "Executing Gesture " + getName() );
		boolean success = false;

			
		WebElement webElement = null;
		String[] gestureName = getName().split( "\\." );
		if ( gestureName.length == 2 )
		{
			Element gestureElement = getElement( pageObject, contextMap, webDriver, dataMap, gestureName[ 1 ] );
			if ( gestureElement != null )
				webElement = (WebElement)gestureElement.getNative();
		}
		
		
		
		switch( GestureType.valueOf( gestureName[0] ) )
		{
			case PINCH:
				if ( getParameterList().size() == 4 )
				{
					Point[] pPoint = new Point[ 4 ];
					pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) );
					pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) );
					pPoint[ 2 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) );
					pPoint[ 3 ] = createPoint( (String) getParameterValue( getParameterList().get( 3 ), contextMap, dataMap ) );
					GestureManager.instance().createPinch( pPoint[ 0 ], pPoint[ 1 ], pPoint[ 2 ], pPoint[ 3 ] ).executeGesture( webDriver, webElement );
				}
				else
					GestureManager.instance().createPinch().executeGesture( webDriver, webElement );
								
				break;
				
			case ZOOM:
				if ( getParameterList().size() == 4 )
				{
					Point[] pPoint = new Point[ 4 ];
					pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) );
					pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) );
					pPoint[ 2 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) );
					pPoint[ 3 ] = createPoint( (String) getParameterValue( getParameterList().get( 3 ), contextMap, dataMap ) );
					GestureManager.instance().createPinch( pPoint[ 0 ], pPoint[ 1 ], pPoint[ 2 ], pPoint[ 3 ] ).executeGesture( webDriver, webElement );
				}
				else
					GestureManager.instance().createZoom().executeGesture( webDriver, webElement );
				break;
				
			case SWIPE:
				if ( getParameterList().size() > 1 )
				{
					Point[] pPoint = new Point[ 2 ];
					if ( getParameterList().size() == 2 )
					{
						pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) );
						pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) );
					}
					else
					{
						pPoint[ 0 ] = createPoint( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) );
						pPoint[ 1 ] = createPoint( (String) getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) );
					}
					
					GestureManager.instance().createSwipe( pPoint[ 0 ], pPoint[ 1 ] ).executeGesture( webDriver, webElement );
					
				}
				else
					GestureManager.instance().createSwipe( Direction.valueOf( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) ) ).executeGesture( webDriver, webElement );
				
				break;
				
			case PRESS:
			    Point pressPoint = createPoint( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) );
				GestureManager.instance().createPress( pressPoint ).executeGesture( webDriver, webElement );
				break;
				
			case ROTATE:
				GestureManager.instance().createRotate( ScreenOrientation.valueOf( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) ) ).executeGesture( webDriver, webElement );
				break;
				
			case KEYPRESS:
				GestureManager.instance().createKeyPress( Integer.parseInt( ( (String) getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) ) ), Integer.parseInt( ( (String) getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) ) ) ).executeGesture( webDriver, webElement );
				break;
				
			case HIDE_KEYBOARD:
				GestureManager.instance().createHideKeyboard().executeGesture( webDriver, webElement );
				break;
		}
		
		success = true;

		
		return success;
	}

}
