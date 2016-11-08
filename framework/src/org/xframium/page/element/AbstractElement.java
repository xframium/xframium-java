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
package org.xframium.page.element;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.XFramiumException;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import org.xframium.page.BY;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractElement.
 */
public abstract class AbstractElement implements Element
{
	
	
	/** The log. */
    protected Map<String,String> elementProperties;
	protected Log log = LogFactory.getLog( Element.class );
	private boolean cacheNative = false;

	public boolean isCacheNative()
    {
        return cacheNative;
    }

    public void setCacheNative( boolean cacheNative )
    {
        this.cacheNative = cacheNative;
    }

    public void addElementProperty( String name, String value )
    {
        if ( elementProperties == null )
            elementProperties = new HashMap<String,String>( 20 );
        
        elementProperties.put( name, value );
    }
    
    public Map<String,String> getElementProperties()
    {
        return elementProperties;
    }
    
    public String getElementProperty( String name )
    {
        if ( elementProperties != null )
            return elementProperties.get( name );
        
        else return null;
    }
    
    /**
	 * _get native.
	 *
	 * @return the object
	 */
	protected abstract Object _getNative();

	protected abstract Dimension _getSize();
	protected abstract Point _getAt();
	
	/**
	 * _set value.
	 *
	 * @param currentValue the current value
	 */
	protected abstract void _setValue( String currentValue, SetMethod setMethod );

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
	 * _mouseDoubleClick.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _mouseDoubleClick();
	
	/**
	 * _release.
	 *
	 * @return true, if successful
	 */
	protected abstract boolean _release();
	
	protected abstract boolean _isFocused();
	
	protected abstract boolean _isEnabled();
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
	
	protected abstract boolean _clickAt( int offsetPercentX, int offsetPercentY );

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
	
	private String deviceContext;
	
	/** The token map. */
	private Map<String,String> tokenMap = null;
	
	/** The tokens applied. */
	private boolean tokensApplied = false;

	public String getName()
	{
	    return elementName;
	}
	
	@Override
	public String getDeviceContext()
	{
	    return deviceContext;
	}
	@Override
	public void setDeviceContext( String deviceContext )
	{
	    this.deviceContext = deviceContext;
	    
	}
	
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
	public Element addToken( String tokenName, String tokenValue )
	{
		tokensApplied = false;
		if ( tokenMap == null )
			tokenMap = new HashMap<String,String>( 10 );
		
		tokenMap.put( tokenName, tokenValue );
		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#addToken(java.lang.String)
	 */
	public Element addToken( String tokenPairValue )
	{
		tokensApplied = false;
		if ( tokenMap == null )
			tokenMap = new HashMap<String,String>( 10 );
		
		String[] tokenPair = tokenPairValue.split( "=" );
		if ( tokenPair.length != 2 )
			throw new ScriptConfigurationException( "You must specify a token in the format of name=value" );
		
		tokenMap.put( tokenPair[ 0 ].trim(), tokenPair[ 1 ].trim() );
		
		return this;
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
	public BY getBy()
	{
		return by;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey()
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
		    if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
            if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
		    if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
		    if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "attribute", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false, new String[] { attributeName, returnValue } );
		}
		catch( Exception e )
		{
		    if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "image", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false, new String[] { resolution.name() } );
		}
		catch( Exception e )
		{
		    if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
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
		setValue( currentValue, SetMethod.DEFAULT );
	}
	
	@Override
	public void setValue( String currentValue, SetMethod setMethod )
	{
	    long startTime = System.currentTimeMillis();
        boolean success = false;
        try
        {
            _setValue( currentValue, setMethod );
            success = true;
            PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "setValue", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false, new String[] { currentValue } );
        }
        catch( Exception e )
        {
            if ( e instanceof XFramiumException )
                throw e;
            else
                throw new ScriptConfigurationException( e.getMessage() );
        }
        finally
        {
            if ( timed )
                PageManager.instance().addExecutionTiming( getExecutionId(), getDeviceName(), pageName + "." + elementName + ".setValue(" + currentValue + ")", System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, "", 0 );
        }
	    
	}

	
	
	@Override
	public void click()
	{
		click( 1, 0 );
	}
	
	public boolean clickAt( int offsetPercentX, int offsetPercentY )
	{
	    return _clickAt( offsetPercentX, offsetPercentY );
	}

	public void click( int clickCount, int waitTime )
	{
		long startTime = System.currentTimeMillis();
		boolean success = false;
		String clickval = String.valueOf(clickCount);
		String waitval =  String.valueOf(waitTime);
		String clickArray[]=new String[2];
		clickArray[0]=clickval;
		clickArray[1]=waitval;
		try
		{
			if (clickCount == 1) {
				_click();			
			} else if (clickCount > 1) {
				_mouseDoubleClick();
//				new Actions( webDriver ).doubleClick( webElement ).build().perform();
			}
								
			success = true;
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), pageName, elementName, "click", System.currentTimeMillis(), System.currentTimeMillis() - startTime, success ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false,clickArray );
		}	
		catch( Exception e )
		{
			if(e instanceof XFramiumException)
				try {
					throw e;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else
				throw new ScriptConfigurationException( e.getMessage() );
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

	@Override
	public boolean isFocused()
	{
	    return _isFocused();
	}

	@Override
	public Dimension getSize()
    {
        return _getSize();
    }

	@Override
    public Point getAt()
    {
        return _getAt();
    }

	@Override
    public boolean isEnabled()
    {
        return _isEnabled();
    }
}
