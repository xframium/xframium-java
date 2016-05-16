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
package com.xframium.page.element;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import com.xframium.page.BY;
import com.xframium.page.PageManager;
import com.xframium.page.StepStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractElement.
 */
public abstract class AbstractElement implements Element
{
	
	
	/** The log. */
	protected Log log = LogFactory.getLog( Element.class );

	/**
	 * _get native.
	 *
	 * @return the object
	 */
	protected abstract Object _getNative();

	/**
	 * _set value.
	 *
	 * @param currentValue the current value
	 */
	protected abstract void _setValue( String currentValue );

	/**
	 * _get value.
	 *
	 * @return the string
	 */
	protected abstract String _getValue();

	/**
	 * _get style.
	 *
	 * @param styleProperty the style property
	 * @return the string
	 */
	protected abstract String _getStyle(String styleProperty);
	
	/**
	 * _get attribute.
	 *
	 * @param attributeName the attribute name
	 * @return the string
	 */
	protected abstract String _getAttribute( String attributeName);

	/**
	 * _is visible.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _isVisible();

	/**
	 * _is present.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _isPresent();

	/**
	 * _is present.
	 *
	 * @param res the res
	 * @return true, if successful
	 */
	protected abstract Image _getImage( Resolution res );
	
	/**
	 * _move to.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _moveTo();
	
	/**
	 * _press.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _press();
	
	/**
	 * _release.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _release();
	
	protected abstract boolean _isFocused();
	/**
	 * _wait for visible.
	 *
	 * @param timeOut the time out
	 * @param timeUnit the time unit
	 * @param waitType the wait type
	 * @param value the value
	 * @return true, if successful
	 */
    protected abstract boolean _waitFor( long timeOut, TimeUnit timeUnit, WAIT_FOR waitType, String value );
	

	/**
	 * _click.
	 */
	protected abstract void _click();

	/**
	 * _get all.
	 *
	 * @return the element[]
	 */
	protected abstract Element[] _getAll();
	
	/** The by. */
	private BY by;
	
	/** The element key. */
	private String elementKey;
	
	/** The timed. */
	private boolean timed;
	
	/** The element name. */
	private String elementName;
	
	/** The page name. */
	private String pageName;
	
	/** The context element. */
	private String contextElement;
	
	/** The context. */
	private Element context;
	
	/** The token map. */
	private Map<String,String> tokenMap = null;
	
	/** The tokens applied. */
	private boolean tokensApplied = false;

	/**
	 * Instantiates a new abstract element.
	 *
	 * @param by the by
	 * @param elementKey the element key
	 * @param elementName the element name
	 * @param pageName the page name
	 * @param contextElement the context element
	 */
	protected AbstractElement( BY by, String elementKey, String elementName, String pageName, String contextElement )
	{
		this.by = by;
		this.elementKey = elementKey;
		this.elementName = elementName;
		this.pageName = pageName;
		this.contextElement = contextElement;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#addToken(java.lang.String, java.lang.String)
	 */
	public void addToken( String tokenName, String tokenValue )
	{
		tokensApplied = false;
		if ( tokenMap == null )
			tokenMap = new HashMap<String,String>( 10 );
		
		tokenMap.put( tokenName, tokenValue );
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#addToken(java.lang.String)
	 */
	public void addToken( String tokenPairValue )
	{
		tokensApplied = false;
		if ( tokenMap == null )
			tokenMap = new HashMap<String,String>( 10 );
		
		String[] tokenPair = tokenPairValue.split( "=" );
		if ( tokenPair.length != 2 )
			throw new IllegalArgumentException( "You must specify a token in the format of name=value" );
		
		tokenMap.put( tokenPair[ 0 ].trim(), tokenPair[ 1 ].trim() );
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getContext()
	 */
	public Element getContext()
	{
		return context;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#setContext(com.perfectoMobile.page.element.Element)
	 */
	public void setContext( Element context )
	{
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getAll()
	 */
	@Override
	public Element[] getAll()
	{
		// TODO Auto-generated method stub
		return _getAll();
	}
	
	/**
	 * Gets the by.
	 *
	 * @return the by
	 */
	BY getBy()
	{
		return by;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	String getKey()
	{
		if ( !tokensApplied )
		{
			if ( tokenMap != null && !tokenMap.isEmpty() )
			{
				String newKey = elementKey;
				for ( String tokenName : tokenMap.keySet() )
				{
				    if ( tokenMap.get( tokenName ) != null)
				        newKey = newKey.replace( "{" + tokenName + "}", tokenMap.get( tokenName ) );
				    else
				        log.warn( "Token [" + tokenName + " was null" );
				}
				elementKey = newKey;
			}
			
			tokensApplied = true;
		}
		
		return elementKey;
	}
	
	/**
	 * Gets the element name.
	 *
	 * @return the element name
	 */
	protected String getElementName()
	{
		return elementName;
	}
	
	/**
	 * Gets the page name.
	 *
	 * @return the page name
	 */
	protected String getPageName()
	{
		return pageName;
	}
	
	/**
	 * Gets the context element.
	 *
	 * @return the context element
	 */
	protected String getContextElement()
	{
		return contextElement;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getValue()
	 */
	@Override
	public String getValue()
	{
		String returnValue = null;
		long startTime = System.currentTimeMillis();
		boolean success = false;
		try
		{
			returnValue = _getValue();
			success = true;
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".getValue()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}
		return returnValue;
	}
	
	/* (non-Javadoc)
     * @see com.perfectoMobile.page.element.Element#getValue()
     */
    @Override
    public String getStyle(String styleProperty)
    {
        String returnValue = null;
        long startTime = System.currentTimeMillis();
        boolean success = false;
        try
        {
            returnValue = _getStyle( styleProperty );
            success = true;
        }
        catch( Exception e )
        {
            throw new IllegalStateException( e );
        }
        finally
        {
            if ( timed )
                PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".getStyle()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
        }
        return returnValue;
    }

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#isVisible()
	 */
	@Override
	public boolean isVisible()
	{
		long startTime = System.currentTimeMillis();
		boolean returnValue = false;
		boolean success = false;
		try
		{
			returnValue = _isVisible();
			success = true;
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".isVisible()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#isPresent()
	 */
	@Override
	public boolean isPresent()
	{
		long startTime = System.currentTimeMillis();
		boolean returnValue = false;
		boolean success = false;
		try
		{
			returnValue = _isPresent();
			success = true;
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".isPresent()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}
		return returnValue;
	}

	/* (non-Javadoc)
     * @see com.perfectoMobile.page.element.Element#waitForVisible(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public boolean waitFor( long timeOut, TimeUnit timeUnit, WAIT_FOR waitType, String value )
    {
        long startTime = System.currentTimeMillis();
        boolean returnValue = false;
        boolean success = false;
        try
        {
            returnValue = _waitFor( timeOut, timeUnit, waitType, value );
            success = true;
            
        }
        catch( Exception e )
        {
            throw new IllegalStateException( e );
        }
        finally
        {
            if ( timed )
                PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".waitForVisible()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
        }
        return returnValue;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#waitForVisible(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean waitForVisible( long timeOut, TimeUnit timeUnit )
	{
	    return waitFor( timeOut, timeUnit, WAIT_FOR.VISIBLE, null );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#waitForPresent(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean waitForPresent( long timeOut, TimeUnit timeUnit )
	{
	    return waitFor( timeOut, timeUnit, WAIT_FOR.PRESENT, null );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getNative()
	 */
	@Override
	public Object getNative()
	{
		return _getNative( );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute( String attributeName )
	{
		long startTime = System.currentTimeMillis();
		String returnValue;
		boolean success = false;
		try
		{
			returnValue = _getAttribute( attributeName );
			success = true;
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "attribute(" + returnValue + ")", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".getAttribute()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}
		return returnValue;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getImage(com.morelandLabs.integrations.perfectoMobile.rest.services.Imaging.Resolution)
	 */
	@Override
	public Image getImage( Resolution resolution )
	{
		long startTime = System.currentTimeMillis();
		Image returnValue;
		boolean success = false;
		try
		{
			returnValue = _getImage( resolution );
			success = true;
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "elementImage", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".getImage()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#setValue(java.lang.String)
	 */
	@Override
	public void setValue( String currentValue )
	{
		long startTime = System.currentTimeMillis();
		boolean success = false;
		try
		{
			_setValue( currentValue );
			success = true;
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "setValue(" + currentValue + ")", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".setValue(" + currentValue + ")", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}

	}

	
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#click()
	 */
	@Override
	public void click()
	{
		long startTime = System.currentTimeMillis();
		boolean success = false;
		try
		{
			_click();
			success = true;
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "click", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
		}
		catch( Exception e )
		{
			throw new IllegalStateException( e );
		}
		finally
		{			
			if ( timed )
				PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".click()", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
		}

	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#setTimed(boolean)
	 */
	@Override
	public void setTimed( boolean timed )
	{
		this.timed = timed;
		
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#isTimed()
	 */
	@Override
	public boolean isTimed()
	{
		return timed;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#moveTo()
	 */
	@Override
	public boolean moveTo()
	{
	    return _moveTo();
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#press()
	 */
	@Override
    public boolean press()
    {
        return _press();
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#release()
	 */
	@Override
    public boolean release()
    {
        return _release();
    }
	
	public boolean isFocused()
	{
	    return _isFocused();
	}
	
	


}
