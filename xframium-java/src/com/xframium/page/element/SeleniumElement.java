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
package com.xframium.page.element;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ContextAware;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.ImageFormat;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.MatchMode;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import com.xframium.integrations.perfectoMobile.rest.services.Imaging.Screen;
import com.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import com.xframium.page.BY;
import com.xframium.page.ElementDescriptor;
import com.xframium.page.PageManager;
import com.xframium.page.StepStatus;
import com.xframium.spi.PropertyProvider;
import com.xframium.spi.driver.CachedElement;
import com.xframium.spi.driver.NativeDriverProvider;
import com.xframium.spi.driver.VisualDriverProvider;
import com.xframium.utility.html.HTMLElementLookup;
import io.appium.java_client.AppiumDriver;

// TODO: Auto-generated Javadoc
/**
 * The Class SeleniumElement.
 */
public class SeleniumElement extends AbstractElement
{
	
	/** The log. */
	private static Log log = LogFactory.getLog( SeleniumElement.class );
	
	/** The Constant EXECUTION_ID. */
	private static final String EXECUTION_ID = "EXECUTION_ID";
	
	/** The Constant DEVICE_NAME. */
	private static final String DEVICE_NAME = "DEVICE_NAME";
	
	/** The web driver. */
	private WebDriver webDriver;
	
	/** The located element. */
	private WebElement locatedElement;
	
	/** The count. */
	private int count = -1;
	
	/** The index. */
	private int index = -1;
	
	/** The use visual driver. */
	private boolean useVisualDriver = false;
	

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#cloneElement()
	 */
	public Element cloneElement()
	{
		SeleniumElement element = new SeleniumElement( getBy(), getKey(), getElementName(), getPageName(), getContextElement(), locatedElement, index );
		element.setDriver( webDriver );
		return element;
	}
	
	/**
	 * Instantiates a new selenium element.
	 *
	 * @param by the by
	 * @param elementKey the element key
	 * @param fieldName the field name
	 * @param pageName the page name
	 * @param contextElement the context element
	 */
	public SeleniumElement( BY by, String elementKey, String fieldName, String pageName, String contextElement )
	{
		super( by, elementKey, fieldName, pageName, contextElement );
	}
	
	/**
	 * Instantiates a new selenium element.
	 *
	 * @param by the by
	 * @param elementKey the element key
	 * @param fieldName the field name
	 * @param pageName the page name
	 * @param contextElement the context element
	 * @param locatedElement the located element
	 * @param index the index
	 */
	private SeleniumElement( BY by, String elementKey, String fieldName, String pageName, String contextElement, WebElement locatedElement, int index )
	{
		super( by, elementKey, fieldName, pageName, contextElement );
		this.locatedElement = locatedElement;
		this.index = index;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getImage(com.morelandLabs.integrations.perfectoMobile.rest.services.Imaging.Resolution)
	 */
	@Override
	public Image _getImage( Resolution imageResolution )
	{
		WebElement imageElement = getElement();
		
		if ( imageElement != null )
		{
			if ( imageElement.getLocation() != null && imageElement.getSize() != null && imageElement.getSize().getWidth() > 0 && imageElement.getSize().getHeight() > 0 )
			{
				String fileKey = "PRIVATE:" + getDeviceName() + ".png";
				PerfectoMobile.instance().imaging().screenShot( getExecutionId(), getDeviceName(), fileKey, Screen.primary, ImageFormat.png, imageResolution );
				byte[] imageData = PerfectoMobile.instance().repositories().download( RepositoryType.MEDIA, fileKey );
				if ( imageData != null && imageData.length > 0 )
				{
					try
					{
						BufferedImage fullImage = ImageIO.read( new ByteArrayInputStream( imageData ) );
						return fullImage.getSubimage( imageElement.getLocation().getX(), imageElement.getLocation().getY(), imageElement.getSize().getWidth(), imageElement.getSize().getHeight() );
					}
					catch( Exception e )
					{
						log.error( Thread.currentThread().getName() + ": Error extracting image data", e );
					}
				}
				else
					log.warn( Thread.currentThread().getName() + ": No image data could be retrieved for [" + fileKey + "]" );
				
			}
			else
				log.warn( Thread.currentThread().getName() + ": The element returned via " + getKey() + " did not contain a location or size" );
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#setDriver(java.lang.Object)
	 */
	@Override
	public void setDriver( Object webDriver )
	{
		this.webDriver = ( WebDriver ) webDriver;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getClass().getSimpleName() + " - " + getBy() + " {" + getKey() + "}";
	}
	
	
	/**
	 * Gets the element.
	 *
	 * @param elementName the element name
	 * @return the element
	 */
	private Element getElement( String elementName )
    {
	    
    	ElementDescriptor elementDescriptor = new ElementDescriptor( PageManager.instance().getSiteName(), getPageName(), elementName );
    	
    	if ( log.isDebugEnabled() )
    		log.debug( Thread.currentThread().getName() + ": Attempting to locate element using [" + elementDescriptor.toString() + "]" );
    	
    	Element myElement = PageManager.instance().getElementProvider().getElement( elementDescriptor );
    	myElement.setDriver( webDriver );
    	return myElement;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getCount()
	 */
	@Override
	public int getCount()
	{
		if ( count == -1 )
			count = _getAll().length;
		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getIndex()
	 */
	@Override
	public int getIndex()
	{
		return index;
	}

	/**
	 * Use by.
	 *
	 * @return the by
	 */
	private By useBy()
	{
		if (getBy().getContext() != null)
		{
			if ( webDriver instanceof VisualDriverProvider )
				useVisualDriver = true;
			else if (webDriver instanceof ContextAware)
				( ( ContextAware ) webDriver ).context( getBy().getContext() );
		}

		switch (getBy())
		{
			case CLASS:
				return By.className( getKey() );

			case CSS:
				return By.cssSelector( getKey() );

			case ID:
				return By.id( getKey() );

			case LINK_TEXT:
				return By.linkText( getKey() );

			case NAME:
				return By.name( getKey() );

			case TAG_NAME:
				return By.tagName( getKey() );

			case XPATH:
				return By.xpath( getKey() );

			case V_TEXT:
				return By.linkText( getKey() );
				
			case HTML:
			    HTMLElementLookup elementLookup = new HTMLElementLookup( getKey() );
			    return By.xpath( elementLookup.toXPath() );
			    

			default:
				return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_moveTo()
	 */
	public boolean _moveTo()
	{
	    WebElement webElement = getElement();
	    if ( webElement != null && webElement.getSize().getHeight() > 0 && webElement.getSize().getWidth() > 0 )
	    {
	        if ( webDriver instanceof HasInputDevices )
	        {
	            new Actions( webDriver ).moveToElement( webElement ).build().perform();
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_press()
	 */
	public boolean _press()
    {
        WebElement webElement = getElement();
        if ( webElement != null && webElement.getSize().getHeight() > 0 && webElement.getSize().getWidth() > 0 )
        {
            if ( webDriver instanceof HasInputDevices )
            {
                new Actions( webDriver ).clickAndHold( webElement ).build().perform();
                return true;
            }
        }
        
        return false;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_release()
	 */
	public boolean _release()
    {
        WebElement webElement = getElement();
        if ( webElement != null && webElement.getSize().getHeight() > 0 && webElement.getSize().getWidth() > 0 )
        {
            if ( webDriver instanceof HasInputDevices )
            {
                new Actions( webDriver ).release( webElement ).build().perform();
                return true;
            }
        }
        
        return false;
    }
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getAll()
	 */
	@Override
	protected Element[] _getAll()
	{
		By useBy = useBy();
		if (useBy != null)
		{
			Element fromContext = getContext();
			if ( fromContext == null && getContextElement() != null && !getContextElement().isEmpty() )
				fromContext = getElement( getContextElement() );
			
			
			if (log.isInfoEnabled())
				log.info( Thread.currentThread().getName() + ": Locating element by [" + getBy() + "] using [" + getKey() + "]" );

			
			List<WebElement> webElements = null;
			
			if ( fromContext != null )
				webElements = ( (WebElement) fromContext.getNative() ).findElements( useBy() );
			else
				webElements = ( (WebDriver) webDriver ).findElements( useBy() );

			if (log.isInfoEnabled())
			{
				if (webElements == null)
					log.info( Thread.currentThread().getName() + ": Could not locate by [" + getBy() + "] using [" + getKey() + "]" );
				else
					log.info( webElements.size() + "Elements Located using " + getKey() );
			}
			
			Element[] foundElements = new Element[ webElements.size() ];
			
			for( int i=0; i<webElements.size(); i++ )
			{
				foundElements[ i ] = new SeleniumElement( getBy(), getKey() + "[" + i + "]", getElementName(), getPageName(), getContextElement(), webElements.get( i ), i );
				foundElements[ i ].setDriver( webDriver );
			}
			
			return foundElements;
		}
		else
			return null;
	}

	/**
	 * Gets the element.
	 *
	 * @return the element
	 */
	private WebElement getElement()
	{
		if ( locatedElement != null )
			return locatedElement;
		
		String currentContext = null;
		if (webDriver instanceof ContextAware)
			currentContext = ( ( ContextAware ) webDriver ).getContext();

		Element fromContext = getContext();
		if ( fromContext == null && getContextElement() != null && !getContextElement().isEmpty() )
			fromContext = getElement( getContextElement() );

		try
		{
			By useBy = useBy();
			if (useBy != null)
			{
				if (log.isInfoEnabled())
					log.info( Thread.currentThread().getName() + ": Locating element by [" + getBy() + "] using [" + getKey() + "]" + ( fromContext != null ? (" from [" + fromContext.getNative() + "]") : "" ) );

				WebElement webElement = null;
				
				if ( useVisualDriver && "VISUAL".equals( getBy().getContext() ) )
				{
					webElement = ( (VisualDriverProvider) webDriver ).getVisualDriver().findElement( useBy );
				}
				else
				{
					if ( fromContext != null )
						webElement = ( (WebElement) fromContext.getNative() ).findElement( useBy() );
					else
						webElement = ( (WebDriver) webDriver ).findElement( useBy() );
				}
				
				if (log.isInfoEnabled())
				{
					if (webElement == null)
						log.info( Thread.currentThread().getName() + ": Could not locate by [" + getBy() + "] using [" + getKey() + "]" );
					else
						log.info( Thread.currentThread().getName() + ": Element " + getKey() + " Located" );
				}
				
				return webElement;
			}

			return null;
		}
		finally
		{
			if (currentContext != null && webDriver instanceof ContextAware)
				( ( ContextAware ) webDriver ).context( currentContext );
		}

	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getNative()
	 */
	@Override
	protected Object _getNative()
	{
		return getElement();
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getStyle(java.lang.String)
	 */
	protected String _getStyle( String styleProperty )
	{
	    long startTime = System.currentTimeMillis();
        WebElement currentElement = getElement();

        String returnValue = getElement().getCssValue( styleProperty );
        
        PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "style(" + returnValue + ")", System.currentTimeMillis(), System.currentTimeMillis() - startTime, StepStatus.SUCCESS, getKey(), null, 0, "", currentElement instanceof CachedElement );
        return returnValue;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getValue()
	 */
	@Override
	protected String _getValue()
	{
	    long startTime = System.currentTimeMillis();
		WebElement currentElement = getElement();

		String returnValue = null;
		switch (currentElement.getTagName().toUpperCase())
		{
			case "IMG":
			    returnValue = currentElement.getAttribute( "src" );

			case "INPUT":
			    returnValue = currentElement.getAttribute( "value" );

			default:
			    returnValue = currentElement.getText();
		}
		
		PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "get(" + returnValue + ")", System.currentTimeMillis(), System.currentTimeMillis() - startTime, StepStatus.SUCCESS, getKey(), null, 0, "", currentElement instanceof CachedElement );
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_getAttribute(java.lang.String)
	 */
	@Override
	protected String _getAttribute( String attributeName )
	{
		return getElement().getAttribute( attributeName );
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_isVisible()
	 */
	@Override
	protected boolean _isVisible()
	{
	    long startTime = System.currentTimeMillis();
	    WebElement webElement = (WebElement) getElement();
		boolean returnValue = webElement.isDisplayed();
		PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "visible", System.currentTimeMillis(), System.currentTimeMillis() - startTime, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", webElement instanceof CachedElement );
		return returnValue;
	}

	@Override
	protected boolean _isFocused()
	{
	    long startTime = System.currentTimeMillis();
        WebElement webElement = (WebElement) getElement();
        
        boolean returnValue = false;
        
        if ( getNativeDriver() instanceof AppiumDriver )
        {
            String focusValue = getAttribute( "focused" );

            if ( focusValue != null )
                returnValue = Boolean.parseBoolean( focusValue );
        }
        else if ( getNativeDriver() instanceof RemoteWebDriver )
            returnValue = webElement.equals( webDriver.switchTo().activeElement() );
        
        PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "focused", System.currentTimeMillis(), System.currentTimeMillis() - startTime, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", webElement instanceof CachedElement );
        return returnValue;
	}
	
	private WebDriver getNativeDriver()
	{

	    if ( webDriver instanceof NativeDriverProvider )
	        return ( (NativeDriverProvider) webDriver ).getNativeDriver();
	    else
	        return webDriver;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_isPresent()
	 */
	@Override
	protected boolean _isPresent()
	{
	    boolean returnValue = false;
	    long startTime = System.currentTimeMillis();
	    
		if ( "V_TEXT".equals( getBy().name().toUpperCase() ) )
		{
			returnValue = Boolean.parseBoolean( PerfectoMobile.instance().imaging().textExists( getExecutionId(), getDeviceName(), getKey(), (short)30 ).getStatus() );
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "present", System.currentTimeMillis(), System.currentTimeMillis() - startTime, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
		}
		else if ( "V_IMAGE".equals( getBy().name().toUpperCase() ) )
		{
			returnValue = Boolean.parseBoolean( PerfectoMobile.instance().imaging().imageExists( getExecutionId(), getDeviceName(), getKey(), (short)30, MatchMode.bounded ).getStatus() );
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "present", System.currentTimeMillis(), System.currentTimeMillis() - startTime, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", false );
			return returnValue;
		}
		
		WebElement webElement = getElement();
		PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "present", System.currentTimeMillis(), System.currentTimeMillis() - startTime, webElement != null ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", webElement instanceof CachedElement );
		return webElement != null;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_waitForPresent(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	protected boolean _waitFor( long timeOut, TimeUnit timeUnit, WAIT_FOR waitType, String value )
	{
		try
		{
		    long startTime = System.currentTimeMillis();
			String currentContext = null;
			if (webDriver instanceof ContextAware)
				currentContext = ( ( ContextAware ) webDriver ).getContext();

			WebDriverWait wait = new WebDriverWait( webDriver, timeOut, 250 );
			WebElement webElement = null;
			
			switch( waitType )
			{
    			case CLICKABLE:
    			    webElement = wait.until( ExpectedConditions.elementToBeClickable( useBy() ) );
    			    break;
    			    
    			case INVISIBLE:
    			    return wait.until( ExpectedConditions.invisibilityOfElementLocated( useBy() ) );
    			    
    			case PRESENT:
    			    
    			    webElement = wait.until( ExpectedConditions.presenceOfElementLocated( useBy() ) );
                    break;
                    
    			case SELECTABLE:
    			    return wait.until( ExpectedConditions.elementToBeSelected( useBy() ) );
    			    
    			case TEXT_VALUE_PRESENT:
    			    return wait.until( ExpectedConditions.textToBePresentInElementValue( useBy(), value ) );
    			    
    			case VISIBLE:
    			    webElement = wait.until( ExpectedConditions.visibilityOfElementLocated( useBy() ) );
                    break;
    			    
			    default:
			        throw new IllegalArgumentException( "Unknown Wait Condition [" + waitType + "]" );
			}
			
			if (currentContext != null && webDriver instanceof ContextAware)
				( ( ContextAware ) webDriver ).context( currentContext );

			
			PageManager.instance().addExecutionLog( getExecutionId(), getDeviceName(), getPageName(), getElementName(), "waitFor." + waitType, System.currentTimeMillis(), System.currentTimeMillis() - startTime, webElement != null ? StepStatus.SUCCESS : StepStatus.FAILURE, getKey(), null, 0, "", webElement instanceof CachedElement );
			return webElement != null;
		}
		catch (Exception e)
		{
			log.error( Thread.currentThread().getName() + ": Could not locate " + useBy(), e );
			throw new IllegalStateException( "Could not locate " + getKey() + " on page " + getPageName(), e );
		}
	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_setValue(java.lang.String)
	 */
	@Override
	protected void _setValue( String currentValue )
	{
		WebElement webElement = getElement();
		
		if ( webElement.getTagName().equalsIgnoreCase( "select" ) )
		{
			if (log.isInfoEnabled())
				log.info( Thread.currentThread().getName() + ": Selecting element value from [" + getKey() + "] as " + currentValue );
			
			Select selectElement = new Select( webElement );
			if ( selectElement.isMultiple() )
				selectElement.deselectAll();
			try
			{
			    selectElement.selectByVisibleText( currentValue );
			}
			catch( Exception e )
			{
			    selectElement.selectByValue( currentValue );
			}
		}
		else
		{
			if (log.isInfoEnabled())
				log.info( Thread.currentThread().getName() + ": Setting element [" + getKey() + "] to " + currentValue );
			webElement.clear();
			webElement.sendKeys( currentValue );
		}

	}

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.AbstractElement#_click()
	 */
	@Override
	protected void _click()
	{
		getElement().click();

	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getExecutionId()
	 */
	public String getExecutionId()
	{
		String executionId = null;
		
		if ( webDriver instanceof PropertyProvider )
		{
			executionId = ( (PropertyProvider) webDriver ).getProperty( EXECUTION_ID );
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof HasCapabilities )
			{
				Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
				executionId = caps.getCapability( "executionId" ).toString();
			}
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof NativeDriverProvider )
			{
				WebDriver nativeDriver = ( (NativeDriverProvider) webDriver ).getNativeDriver();
				if ( nativeDriver instanceof HasCapabilities )
				{
					Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
					executionId = caps.getCapability( "executionId" ).toString();
				}
			}
		}
		
		if ( executionId == null )
			log.warn( "No Execution ID could be located" );
		
		return executionId;
	}
	
	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.element.Element#getDeviceName()
	 */
	public String getDeviceName()
	{
		String executionId = null;
		
		if ( webDriver instanceof PropertyProvider )
		{
			executionId = ( (PropertyProvider) webDriver ).getProperty( DEVICE_NAME );
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof HasCapabilities )
			{
				Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
				executionId = caps.getCapability( "deviceName" ).toString();
			}
		}
		
		if ( executionId == null )
		{
			if ( webDriver instanceof NativeDriverProvider )
			{
				WebDriver nativeDriver = ( (NativeDriverProvider) webDriver ).getNativeDriver();
				if ( nativeDriver instanceof HasCapabilities )
				{
					Capabilities caps = ( (HasCapabilities) webDriver ).getCapabilities();
					executionId = caps.getCapability( "deviceName" ).toString();
				}
			}
		}
		
		if ( executionId == null )
			log.warn( Thread.currentThread().getName() + ": No Execution ID could be located" );
		
		return executionId;
	}

}
