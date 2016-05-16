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
package com.xframium.page.keyWord.step;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.page.keyWord.KeyWordStep;
import com.xframium.page.keyWord.KeyWordStep.StepFailure;
import com.xframium.page.keyWord.KeyWordStep.ValidationType;
import com.xframium.page.keyWord.step.spi.KWSAddCookie;
import com.xframium.page.keyWord.step.spi.KWSAddDevice;
import com.xframium.page.keyWord.step.spi.KWSAttribute;
import com.xframium.page.keyWord.step.spi.KWSBreak;
import com.xframium.page.keyWord.step.spi.KWSCache;
import com.xframium.page.keyWord.step.spi.KWSCall;
import com.xframium.page.keyWord.step.spi.KWSCheckColor;
import com.xframium.page.keyWord.step.spi.KWSClick;
import com.xframium.page.keyWord.step.spi.KWSCompare;
import com.xframium.page.keyWord.step.spi.KWSContrastRatio;
import com.xframium.page.keyWord.step.spi.KWSDeleteCookie;
import com.xframium.page.keyWord.step.spi.KWSDeleteCookies;
import com.xframium.page.keyWord.step.spi.KWSDevice;
import com.xframium.page.keyWord.step.spi.KWSExecJS;
import com.xframium.page.keyWord.step.spi.KWSExists;
import com.xframium.page.keyWord.step.spi.KWSFocus;
import com.xframium.page.keyWord.step.spi.KWSFork;
import com.xframium.page.keyWord.step.spi.KWSFunction;
import com.xframium.page.keyWord.step.spi.KWSGesture;
import com.xframium.page.keyWord.step.spi.KWSGetCookie;
import com.xframium.page.keyWord.step.spi.KWSGetCookies;
import com.xframium.page.keyWord.step.spi.KWSLoop;
import com.xframium.page.keyWord.step.spi.KWSMath;
import com.xframium.page.keyWord.step.spi.KWSMouse;
import com.xframium.page.keyWord.step.spi.KWSOpenPage;
import com.xframium.page.keyWord.step.spi.KWSReport;
import com.xframium.page.keyWord.step.spi.KWSReturn;
import com.xframium.page.keyWord.step.spi.KWSSet;
import com.xframium.page.keyWord.step.spi.KWSString;
import com.xframium.page.keyWord.step.spi.KWSValue;
import com.xframium.page.keyWord.step.spi.KWSVisible;
import com.xframium.page.keyWord.step.spi.KWSWait;
import com.xframium.page.keyWord.step.spi.KWSWaitFor;
import com.xframium.page.keyWord.step.spi.KWSWindow;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating KeyWordStep objects.
 */
public class KeyWordStepFactory
{
	
	/** The singleton. */
	private static KeyWordStepFactory singleton = new KeyWordStepFactory();

	/**
	 * Instance.
	 *
	 * @return the key word step factory
	 */
	public static KeyWordStepFactory instance()
	{
		return singleton;
	}
	
	/** The step map. */
	private Map<String,Class>stepMap = new HashMap<String,Class>( 20 );
	
	/** The log. */
	private Log log = LogFactory.getLog( KeyWordStepFactory.class );
	
	/**
	 * Instantiates a new key word step factory.
	 */
	private KeyWordStepFactory()
	{
		initializeDefaults();
	}
	
	/**
	 * Initialize defaults.
	 */
	private void initializeDefaults()
	{
		addKeyWord( "CALL", KWSCall.class );
		addKeyWord( "CLICK", KWSClick.class );
		addKeyWord( "EXISTS", KWSExists.class );
		addKeyWord( "FUNCTION", KWSFunction.class );
		addKeyWord( "GESTURE", KWSGesture.class );
		addKeyWord( "RETURN", KWSReturn.class );
		addKeyWord( "SET", KWSSet.class );
		addKeyWord( "VALUE", KWSValue.class );
		addKeyWord( "GET", KWSValue.class );
		addKeyWord( "WAIT", KWSWait.class );
		addKeyWord( "WAIT_FOR", KWSWaitFor.class );
		addKeyWord( "ATTRIBUTE", KWSAttribute.class );
		addKeyWord( "LOOP", KWSLoop.class );
		addKeyWord( "BREAK", KWSBreak.class );
		addKeyWord( "DEVICE", KWSDevice.class );
		addKeyWord( "FORK", KWSFork.class );
		addKeyWord( "VISIBLE", KWSVisible.class );
		addKeyWord( "VERIFY_COLOR", KWSCheckColor.class );
		addKeyWord( "VERIFY_CONTRAST", KWSContrastRatio.class );
		addKeyWord( "WINDOW", KWSWindow.class );
                addKeyWord( "EXECJS", KWSExecJS.class );
                addKeyWord( "OPEN_PAGE", KWSOpenPage.class );
                addKeyWord( "ADD_COOKIE", KWSAddCookie.class );
                addKeyWord( "DELETE_COOKIE", KWSDeleteCookie.class );
                addKeyWord( "DELETE_COOKIES", KWSDeleteCookies.class );
		addKeyWord( "GET_COOKIE", KWSGetCookie.class );
		addKeyWord( "GET_COOKIES", KWSGetCookies.class );
		addKeyWord( "COMPARE", KWSCompare.class );
		addKeyWord( "STRING", KWSString.class );
		addKeyWord( "MATH", KWSMath.class );
		addKeyWord( "MOUSE", KWSMouse.class );
		addKeyWord( "CACHE", KWSCache.class );
		addKeyWord( "REPORT", KWSReport.class );
                addKeyWord( "ADD_DEVICE", KWSAddDevice.class );
                addKeyWord( "HAS_FOCUS", KWSFocus.class );
	}
	
	/**
	 * Adds the key word.
	 *
	 * @param keyWord the key word
	 * @param kwImpl the kw impl
	 */
	public void addKeyWord( String keyWord, Class kwImpl )
	{
		if ( stepMap.containsKey( keyWord ) )
			log.warn( "Overwriting Keyword [" + keyWord + "] of type [" + stepMap.get( keyWord ).getClass().getSimpleName() + "] with [" + kwImpl.getClass().getSimpleName() );
		
		stepMap.put( keyWord.toUpperCase(), kwImpl );
	}
	
	/**
	 * Creates a new KeyWordStep object.
	 *
	 * @param name the name
	 * @param pageName the page name
	 * @param active the active
	 * @param type the type
	 * @param linkId the link id
	 * @param timed the timed
	 * @param sFailure the s failure
	 * @param inverse the inverse
	 * @param os the os
	 * @param poi the poi
	 * @param threshold the threshold
	 * @param description the description
	 * @param waitTime the wait time
	 * @param context the context
	 * @param validation the validation
	 * @param device the device
	 * @param validationType the validation type
	 * @return the key word step
	 */
	public KeyWordStep createStep( String name, String pageName, boolean active, String type,
                                       String linkId, boolean timed, StepFailure sFailure, boolean inverse,
                                       String os, String poi, int threshold, String description, long waitTime,
                                       String context, String validation, String device, ValidationType validationType, String tagNames )
	{
		
		Class kwImpl = stepMap.get( type.toUpperCase() );
		
		if ( kwImpl == null )
		{
			log.error( "Unknown KeyWord [" + type + "]" );
			System.exit( -1 );
		}
		try
		{
			KeyWordStep returnValue = (KeyWordStep) kwImpl.newInstance();
			returnValue.setActive( active );
			returnValue.setLinkId( linkId );
			returnValue.setName( name );
			returnValue.setPageName( pageName );
			returnValue.setTimed( timed );
			returnValue.setFailure( sFailure );
			returnValue.setInverse( inverse );
			returnValue.setOs( os );
			returnValue.setPoi( poi );
			returnValue.setThreshold( threshold );
			returnValue.setDescription(description);
			returnValue.setWait( waitTime );
			returnValue.setValidation( validation );
			returnValue.setValidationType( validationType );
			returnValue.setContext( context );
            returnValue.setDevice( device );
            returnValue.setTagNames( tagNames );
			
			return returnValue;
		}
		catch( Exception e )
		{
			throw new IllegalArgumentException( "Unknown KeyWord [" + type + "]", e );
		}
		
		
	}
}
