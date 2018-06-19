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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudDescriptor.ProviderType;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.gesture.AbstractDragDropGesture.InitialDragDropAction;
import org.xframium.gesture.Gesture.Direction;
import org.xframium.gesture.Gesture.GestureType;
import org.xframium.gesture.factory.GestureFactory;
import org.xframium.gesture.factory.spi.AppiumGestureFactory;
import org.xframium.gesture.factory.spi.PerfectoGestureFactory;
import org.xframium.gesture.factory.spi.SeleniumGestureFactory;
import org.xframium.page.element.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class GestureManager.
 */
public class GestureManager
{
    
    private static GestureFactory SELENIUM = new SeleniumGestureFactory();
    private static GestureFactory APPIUM = new AppiumGestureFactory();
    private static GestureFactory PERFECTO = new PerfectoGestureFactory();
    
    /**
     * Instance.
     *
     * @return the gesture manager
     */
    public static GestureManager instance( DeviceWebDriver webDriver )
    {
        GestureManager gM = new GestureManager();
        
        
        switch( ProviderType.valueOf( webDriver.getCloud().getProvider().toUpperCase() ) )
        {
            case PERFECTO:
                gM.gestureFactory = PERFECTO;
                break;
            case SAUCELABS:
                gM.gestureFactory = SELENIUM;
                break;
            case BROWSERSTACK:
                gM.gestureFactory = SELENIUM;
                break;
            case SELENIUM:
                gM.gestureFactory = SELENIUM;
                break;
            case WINDOWS:
                gM.gestureFactory = SELENIUM;
                break;
            case APPIUM:
            	gM.gestureFactory = APPIUM;
        }
        
        return gM;
    }

    /**
     * Instantiates a new gesture manager.
     */
    private GestureManager()
    {

    }

    /** The gesture factory. */
    private GestureFactory gestureFactory;

    /** The log. */
    private Log log = LogFactory.getLog( GestureManager.class );


    /**
     * Creates the Hide Keyboard Gesture.
     *
     * @return the gesture
     */
    public Gesture createHideKeyboard( String xFID )
    {
        return gestureFactory.createGesture( GestureType.HIDE_KEYBOARD, new Object[] { false } );
    }

    /**
     * Creates the swipe.
     *
     * @param swipeDirection
     *            the swipe direction
     * @return the gesture
     */
    public Gesture createSwipe( String xFID, Direction swipeDirection )
    {
        switch ( swipeDirection )
        {
            case DOWN:
                return createSwipe( xFID, new Point( 50, 15 ), new Point( 50, 85 ) );

            case LEFT:
                return createSwipe( xFID, new Point( 55, 50 ), new Point( 85, 50 ) );

            case RIGHT:
                return createSwipe( xFID, new Point( 85, 50 ), new Point( 15, 50 ) );

            case UP:
                return createSwipe( xFID, new Point( 50, 85 ), new Point( 50, 15 ) );

            default:
                return null;
        }
    }
    
    public Gesture createDragDrop( String xFID, InitialDragDropAction dropAction, Element fromElement, Element toElement )
    {
        return gestureFactory.createGesture( GestureType.DRAGDROP, new Object[] { fromElement, toElement, dropAction } );
    }
    
    public Gesture createDragDrop( String xFID, Element fromElement, Element toElement )
    {
        return gestureFactory.createGesture( GestureType.DRAGDROP, new Object[] { fromElement, toElement, InitialDragDropAction.PRESS } );
    }

    /**
     * Creates the rotate.
     *
     * @param sOrientation
     *            the s orientation
     * @return the gesture
     */
    public Gesture createRotate( String xFID, ScreenOrientation sOrientation )
    {
        return gestureFactory.createGesture( GestureType.ROTATE, new Object[] { sOrientation } );
    }

    /**
     * Creates the swipe.
     *
     * @param startPosition
     *            the start position
     * @param endPosition
     *            the end position
     * @return the gesture
     */
    public Gesture createSwipe( String xFID, Point startPosition, Point endPosition )
    {
        return gestureFactory.createGesture( GestureType.SWIPE, new Object[] { startPosition, endPosition } );
    }

    /**
     * Creates the press.
     *
     * @param pressPosition
     *            the press position
     * @return the gesture
     */
    public Gesture createPress( String xFID, Point pressPosition )
    {
        return createPress(  xFID, pressPosition, 100l, 1 );
    }

    /**
     * Creates the press.
     *
     * @param pressPosition
     *            the press position
     * @param pressLength
     *            the press length
     * @return the gesture
     */
    public Gesture createPress( String xFID, Point pressPosition, long pressLength )
    {
        return createPress( xFID, pressPosition, pressLength, 1 );
    }

    public Gesture createPress( String xFID, Point pressPosition, long pressLength, int pressCount )
    {
        return gestureFactory.createGesture( GestureType.PRESS, new Object[] { pressPosition, pressLength, pressCount } );
    }

    /**
     * Creates the key press.
     *
     * @param keyCode
     *            the key code
     * @param metaState
     *            the meta state
     * @return the gesture
     */
    public Gesture createKeyPress( String xFID, String keyCode, int metaState )
    {
        return gestureFactory.createGesture( GestureType.KEYPRESS, new Object[] { keyCode, metaState } );
    }

    /**
     * Creates the zoom.
     *
     * @return the gesture
     */
    public Gesture createZoom(String xFID)
    {
        return createZoom( xFID, new Point( 45, 45 ), new Point( 55, 55 ), new Point( 15, 15 ), new Point( 85, 85 ) );
    }

    /**
     * Creates the zoom.
     *
     * @param startOne
     *            the start one
     * @param startTwo
     *            the start two
     * @param endOne
     *            the end one
     * @param endTwo
     *            the end two
     * @return the gesture
     */
    public Gesture createZoom( String xFID, Point startOne, Point startTwo, Point endOne, Point endTwo )
    {
        return gestureFactory.createGesture( GestureType.ZOOM, new Object[] { startOne, startTwo, endOne, endTwo } );
    }

    /**
     * Creates the pinch.
     *
     * @return the gesture
     */
    public Gesture createPinch(String xFID)
    {
        return createPinch( xFID, new Point( 15, 15 ), new Point( 85, 85 ), new Point( 45, 45 ), new Point( 55, 55 ) );
    }

    /**
     * Creates the pinch.
     *
     * @param startOne
     *            the start one
     * @param startTwo
     *            the start two
     * @param endOne
     *            the end one
     * @param endTwo
     *            the end two
     * @return the gesture
     */
    public Gesture createPinch( String xFID, Point startOne, Point startTwo, Point endOne, Point endTwo )
    {
        return gestureFactory.createGesture( GestureType.PINCH, new Object[] { startOne, startTwo, endOne, endTwo } );
    }

}
