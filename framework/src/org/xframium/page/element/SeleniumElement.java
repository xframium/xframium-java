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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.device.factory.MorelandWebElement;
import org.xframium.exception.ObjectIdentificationException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.exception.ScriptException;
import org.xframium.gesture.GestureManager;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.bean.ImageExecution;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.ImageFormat;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.MatchMode;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Screen;
import org.xframium.integrations.perfectoMobile.rest.services.Repositories.RepositoryType;
import org.xframium.page.BY;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.spi.PropertyProvider;
import org.xframium.spi.driver.CachedElement;
import org.xframium.spi.driver.NativeDriverProvider;
import org.xframium.spi.driver.VisualDriverProvider;
import org.xframium.utility.XPathGenerator;
import org.xframium.utility.html.HTMLElementLookup;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.Element#cloneElement()
     */
    public Element cloneElement()
    {
        SeleniumElement element = new SeleniumElement( getBy(), getRawKey(), getElementName(), getPageName(), getContextElement(), locatedElement, index );
        element.setDriver( webDriver );
        element.setDeviceContext( getDeviceContext() );
        return element;
    }

    /**
     * Instantiates a new selenium element.
     *
     * @param by
     *            the by
     * @param elementKey
     *            the element key
     * @param fieldName
     *            the field name
     * @param pageName
     *            the page name
     * @param contextElement
     *            the context element
     */
    public SeleniumElement( BY by, String elementKey, String fieldName, String pageName, String contextElement )
    {
        super( by, elementKey, fieldName, pageName, contextElement );
    }

    /**
     * Instantiates a new selenium element.
     *
     * @param by
     *            the by
     * @param elementKey
     *            the element key
     * @param fieldName
     *            the field name
     * @param pageName
     *            the page name
     * @param contextElement
     *            the context element
     * @param locatedElement
     *            the located element
     * @param index
     *            the index
     */
    private SeleniumElement( BY by, String elementKey, String fieldName, String pageName, String contextElement, WebElement locatedElement, int index )
    {
        super( by, elementKey, fieldName, pageName, contextElement );
        this.locatedElement = locatedElement;
        this.index = index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_getImage(com.
     * morelandLabs.integrations.perfectoMobile.rest.services.Imaging.
     * Resolution)
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

                byte[] imageData = null;

                String cloudName = ((DeviceWebDriver) webDriver).getDevice().getCloud();
                if ( cloudName == null || cloudName.trim().isEmpty() )
                    cloudName = CloudRegistry.instance().getCloud().getName();

                if ( CloudRegistry.instance().getCloud( cloudName ).getProvider().equals( "PERFECTO" ) )
                {
                    PerfectoMobile.instance().imaging().screenShot( getExecutionId(), getDeviceName(), fileKey, Screen.primary, ImageFormat.png, imageResolution );
                    imageData = PerfectoMobile.instance().repositories().download( RepositoryType.MEDIA, fileKey );
                }
                else
                {
                    if ( webDriver instanceof TakesScreenshot )
                    {
                        try
                        {
                            imageData = ((TakesScreenshot) webDriver).getScreenshotAs( OutputType.BYTES );
                        }
                        catch ( Exception e )
                        {
                            log.error( "Error taking screenshot", e );
                        }
                    }
                }
                if ( imageData != null && imageData.length > 0 )
                {
                    try
                    {
                        BufferedImage fullImage = ImageIO.read( new ByteArrayInputStream( imageData ) );
                        return fullImage.getSubimage( imageElement.getLocation().getX(), imageElement.getLocation().getY(), imageElement.getSize().getWidth(), imageElement.getSize().getHeight() );
                    }
                    catch ( Exception e )
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

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.Element#setDriver(java.lang.Object)
     */
    @Override
    public void setDriver( Object webDriver )
    {
        this.webDriver = (WebDriver) webDriver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getClass().getSimpleName() + " - " + getBy() + " {" + getRawKey() + "}";
    }

    /**
     * Gets the element.
     *
     * @param elementName
     *            the element name
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

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.Element#getCount()
     */
    @Override
    public int getCount()
    {
        if ( count == -1 )
            count = _getAll().length;
        return count;
    }

    /*
     * (non-Javadoc)
     * 
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
        if ( getDeviceContext() != null && !getDeviceContext().trim().isEmpty() )
        {
            if ( webDriver instanceof VisualDriverProvider )
                useVisualDriver = true;
            else if ( webDriver instanceof ContextAware )
                ((ContextAware) webDriver).context( getDeviceContext() );
        }
        else if ( getBy().getContext() != null )
        {
            if ( webDriver instanceof VisualDriverProvider )
                useVisualDriver = true;
            else if ( webDriver instanceof ContextAware )
                ((ContextAware) webDriver).context( getBy().getContext() );
        }

        switch ( getBy() )
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

            case PROP:
                Map<String, String> propertyMap = new HashMap<String, String>( 10 );
                propertyMap.put( "resource-id", ApplicationRegistry.instance().getAUT().getAndroidIdentifier() );
                return By.xpath( XPathGenerator.generateXPathFromProperty( propertyMap, getKey() ) );

            default:
                return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
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

    /*
     * (non-Javadoc)
     * 
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
    
    public boolean _clickAt( int offsetX, int offsetY )
    {
        WebElement webElement = getElement();
        
        if ( webElement != null )
        {
        
            Dimension elementSize = webElement.getSize();
            
            int useX = (int) ((double)elementSize.getWidth() * ((double)offsetX / 100.0 ) );
            int useY = (int) ((double)elementSize.getHeight() * ((double)offsetY / 100.0 ) );
            
            if ( log.isInfoEnabled() )
                log.info( "Clicking " + useX + "," + useY + " pixels relative to " + getName() );
            
            if ( webDriver instanceof HasInputDevices )
            {
                new Actions( webDriver ).moveToElement( webElement, useX, useY ).click().build().perform();
                return true;
            }
            
            return true;
        }
        
        return false;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_mouseDoubleClick()
     */
    public boolean _mouseDoubleClick()
    {
        WebElement webElement = getElement();
        if ( webElement != null && webElement.getSize().getHeight() > 0 && webElement.getSize().getWidth() > 0 )
        {
            if ( webDriver instanceof HasInputDevices )
            {
                new Actions( webDriver ).doubleClick( webElement ).build().perform();
                return true;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_getAll()
     */
    @Override
    protected Element[] _getAll()
    {
        By useBy = useBy();
        if ( useBy != null )
        {
            Element fromContext = getContext();
            if ( fromContext == null && getContextElement() != null && !getContextElement().isEmpty() )
                fromContext = getElement( getContextElement() );

            if ( log.isInfoEnabled() )
                log.info( Thread.currentThread().getName() + ": Locating element by [" + getBy() + "] using [" + getKey() + "]" );

            List<WebElement> webElements = null;

            if ( fromContext != null )
                webElements = ((WebElement) fromContext.getNative()).findElements( useBy() );
            else
                webElements = ((WebDriver) webDriver).findElements( useBy() );

            if ( log.isInfoEnabled() )
            {
                if ( webElements == null )
                    log.info( Thread.currentThread().getName() + ": Could not locate by [" + getBy() + "] using [" + getKey() + "]" );
                else
                    log.info( webElements.size() + "Elements Located using " + getKey() );
            }

            Element[] foundElements = new Element[webElements.size()];

            for ( int i = 0; i < webElements.size(); i++ )
            {
                foundElements[i] = new SeleniumElement( getBy(), getKey() + "[" + (i + 1) + "]", getElementName(), getPageName(), getContextElement(), webElements.get( i ), i );
                foundElements[i].setDriver( webDriver );
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
        {
            if ( log.isDebugEnabled() )
                log.debug( Thread.currentThread().getName() + ": Element " + getKey() + " Read from cache" );
            return locatedElement;
        }

        String currentContext = null;
        if ( webDriver instanceof ContextAware )
            currentContext = ((ContextAware) webDriver).getContext();

        Element fromContext = getContext();
        if ( fromContext == null && getContextElement() != null && !getContextElement().isEmpty() )
            fromContext = getElement( getContextElement() );

        try
        {
            By useBy = useBy();
            if ( useBy != null )
            {
                if ( log.isInfoEnabled() )
                    log.info( Thread.currentThread().getName() + ": Locating element by [" + getBy() + "] using [" + getKey() + "]" + (fromContext != null ? (" from [" + fromContext.getNative() + "]") : "") );

                WebElement webElement = null;

                if ( useVisualDriver && "VISUAL".equals( getBy().getContext() ) )
                {
                    webElement = ((VisualDriverProvider) webDriver).getVisualDriver().findElement( useBy );
                }
                else
                {
                    if ( fromContext != null )
                        webElement = ((WebElement) fromContext.getNative()).findElement( useBy() );
                    else
                        webElement = ((WebDriver) webDriver).findElement( useBy() );
                }

                if ( log.isInfoEnabled() )
                {
                    if ( webElement == null )
                        log.info( Thread.currentThread().getName() + ": Could not locate by [" + getBy() + "] using [" + getKey() + "]" );
                    else
                        log.info( Thread.currentThread().getName() + ": Element " + getKey() + " Located" );
                }

                if ( isCacheNative() )
                    locatedElement = webElement;
                return webElement;
            }

            return null;
        }
        finally
        {
            if ( currentContext != null && webDriver instanceof ContextAware )
                ((ContextAware) webDriver).context( currentContext );
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_getNative()
     */
    @Override
    protected Object _getNative()
    {
        return getElement();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_getStyle(java.lang.
     * String)
     */
    protected String _getStyle( String styleProperty )
    {
        long startTime = System.currentTimeMillis();
        WebElement currentElement = getElement();

        String returnValue = getElement().getCssValue( styleProperty );

        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_getValue()
     */
    @Override
    protected String _getValue()
    {
        long startTime = System.currentTimeMillis();
        WebElement currentElement = getElement();

        String returnValue = null;
        switch ( currentElement.getTagName().toUpperCase() )
        {
            case "IMG":
                returnValue = currentElement.getAttribute( "src" );
                break;

            case "INPUT":
                returnValue = currentElement.getAttribute( "value" );
                break;

            case "SELECT":
                returnValue = new Select( currentElement ).getFirstSelectedOption().getText();
                break;

            case "UIATEXTFIELD":
            	returnValue = currentElement.getAttribute( "value" );
            	break;
            	
            case "ANDROID.WIDGET.EDITTEXT":
            	returnValue = currentElement.getAttribute( "text" );
            	break;
                
            default:
                returnValue = currentElement.getText();
                break;
        }

        return returnValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.element.AbstractElement#_getAttribute(java.lang.
     * String)
     */
    @Override
    protected String _getAttribute( String attributeName )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Getting Attribute value for " + attributeName );
        return getElement().getAttribute( attributeName );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_isVisible()
     */
    @Override
    protected boolean _isVisible()
    {
        long startTime = System.currentTimeMillis();
        WebElement webElement = (WebElement) getElement();
        boolean returnValue = webElement.isDisplayed();

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

        return returnValue;
    }

    @Override
    protected boolean _isEnabled()
    {
        long startTime = System.currentTimeMillis();
        WebElement webElement = (WebElement) getElement();

        boolean returnValue = webElement.isEnabled();

        return returnValue;

    }

    private WebDriver getNativeDriver()
    {

        if ( webDriver instanceof NativeDriverProvider )
            return ((NativeDriverProvider) webDriver).getNativeDriver();
        else
            return webDriver;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_isPresent()
     */
    @Override
    protected boolean _isPresent()
    {
        boolean returnValue = false;
        long startTime = System.currentTimeMillis();

        if ( "V_TEXT".equals( getBy().name().toUpperCase() ) )
        {
            if ( getElementProperties() != null )
            {
                String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript( "mobile:text:find", getElementProperties() ) + "";
                returnValue = !"false".equals( result.toLowerCase() );
            }
            else
            {
                String testValue = PerfectoMobile.instance().imaging().textExists( getExecutionId(), getDeviceName(), getKey(), (short) 30, 50 ).getStatus();
                returnValue = Boolean.parseBoolean( testValue ) | testValue.toUpperCase().equals( "SUCCESS" );
                ;
            }

            return returnValue;
        }
        else if ( "V_IMAGE".equals( getBy().name().toUpperCase() ) )
        {
            returnValue = Boolean.parseBoolean( PerfectoMobile.instance().imaging().imageExists( getExecutionId(), getDeviceName(), getKey(), (short) 30, MatchMode.bounded ).getStatus() );
            return returnValue;
        }

        WebElement webElement = getElement();

        return webElement != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.element.AbstractElement#_waitForPresent(long,
     * java.util.concurrent.TimeUnit)
     */
    @Override
    protected boolean _waitFor( long timeOut, TimeUnit timeUnit, WAIT_FOR waitType, String value )
    {
        long startTime = System.currentTimeMillis();
        if ( "V_TEXT".equals( getBy().name().toUpperCase() ) )
        {
            boolean returnValue = false;
            if ( getElementProperties() != null )
            {
                String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript( "mobile:text:find", getElementProperties() ) + "";
                returnValue = !"false".equals( result.toLowerCase() );
            }
            else
            {
                String testValue = PerfectoMobile.instance().imaging().textExists( getExecutionId(), getDeviceName(), getKey(), (short) 30, 50 ).getStatus();
                if ( testValue == null )
                    returnValue = true;
                else
                    returnValue = Boolean.parseBoolean( testValue ) | testValue.toUpperCase().equals( "SUCCESS" );
            }
            return returnValue;
        }
        else
        {

            try
            {

                String currentContext = null;
                if ( webDriver instanceof ContextAware )
                    currentContext = ((ContextAware) webDriver).getContext();

                WebDriverWait wait = new WebDriverWait( webDriver, timeOut, 250 );
                WebElement webElement = null;

                switch ( waitType )
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

                if ( currentContext != null && webDriver instanceof ContextAware )
                    ((ContextAware) webDriver).context( currentContext );

                return webElement != null;
            }
            catch ( Exception e )
            {
                log.error( Thread.currentThread().getName() + ": Could not locate " + useBy() );
                throw new ObjectIdentificationException( getBy(), useBy() );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_setValue(java.lang.
     * String)
     */
    @Override
    protected void _setValue( String currentValue, SetMethod setMethod )
    {
        WebElement webElement = getElement();

        if ( "V_TEXT".equals( getBy().name().toUpperCase() ) )
        {
            if ( getElementProperties() != null )
            {
                String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript( "mobile:edit-text:set", getElementProperties() ) + "";
                if ( "false".equals( result.toLowerCase() ) )
                    throw new ScriptException( "Visual set failed to execute" );

            }
            else
                throw new ScriptConfigurationException( "A visual set requires element properties to be specified" );
        }
        else
        {
            if ( webElement.getTagName().equalsIgnoreCase( "select" ) )
            {
                switch ( setMethod )
                {
                    case DEFAULT:
                        if ( log.isInfoEnabled() )
                            log.info( Thread.currentThread().getName() + ": Selecting element value from [" + getKey() + "] as " + currentValue );

                        Select selectElement = new Select( webElement );
                        if ( selectElement.isMultiple() )
                            selectElement.deselectAll();
                        try
                        {
                            selectElement.selectByVisibleText( currentValue );
                        }
                        catch ( Exception e )
                        {
                            selectElement.selectByValue( currentValue );
                        }

                        break;

                    case MULTISELECT:
                        if ( log.isInfoEnabled() )
                            log.info( Thread.currentThread().getName() + ": Selecting element value from [" + getKey() + "] as " + currentValue );
                        Select multipleselect = new Select( webElement );
                        String multiSelectTokens[] = currentValue.split( "," );
                        for ( String tokens : multiSelectTokens )
                        {
                            if ( multipleselect.isMultiple() )
                            {
                                try
                                {
                                    multipleselect.selectByVisibleText( tokens );
                                }
                                catch ( Exception e )
                                {
                                    multipleselect.selectByValue( tokens );
                                }
                            }
                        }
                    default:
                        break;
                }
            }
            else if ( webElement.getTagName().equalsIgnoreCase( "UIAPickerWheel" ) )
            {
                try
                {
                    MorelandWebElement x = (MorelandWebElement) webElement;
                    ((IOSElement) x.getWebElement()).sendKeys( currentValue );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();

                    MorelandWebElement x = (MorelandWebElement) webElement;
                    ((IOSElement) x.getWebElement()).setValue( currentValue );
                }

                // try
                // {
                //     MorelandWebElement x = (MorelandWebElement) webElement;
                //     ((IOSElement) x.getWebElement()).setValue( currentValue );
                // }
                // catch ( Exception e )
                // {
                //     e.printStackTrace();
                // }
            }
            else
            {
                switch ( setMethod )
                {
                    case DEFAULT:
                        if ( log.isInfoEnabled() )
                            log.info( Thread.currentThread().getName() + ": Setting element [" + getKey() + "] to " + currentValue );
                        MorelandWebElement x = (MorelandWebElement) webElement;
                        if ( x.getWebElement() instanceof IOSElement )
                            ((IOSElement) x.getWebElement()).setValue( currentValue );
                        else
                        {
                            webElement.clear();
                            webElement.sendKeys( currentValue );
                        }
                        break;

                    case SINGLE:
                        if ( log.isInfoEnabled() )
                            log.info( Thread.currentThread().getName() + ": Setting element [" + getKey() + "] to " + currentValue + " using individual send keys" );

                        webElement.sendKeys( currentValue );
                    default:
                        break;

                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.AbstractElement#_click()
     */
    @Override
    protected void _click()
    {
        if ( "V_TEXT".equals( getBy().name().toUpperCase() ) )
        {
            createPressFromVisual( PerfectoMobile.instance().imaging().textExists( getExecutionId(), getDeviceName(), getKey(), (short) 30, 50 ) );
        }
        else if ( "V_IMAGE".equals( getBy().name().toUpperCase() ) )
        {
            createPressFromVisual( PerfectoMobile.instance().imaging().imageExists( getExecutionId(), getDeviceName(), getKey(), (short) 30, MatchMode.bounded ) );
        }
        else
            getElement().click();

    }

    private void createPressFromVisual(ImageExecution iExec) {
        if ( getElementProperties() != null )
        {
            String result = ((RemoteWebDriver) ((DeviceWebDriver) webDriver).getWebDriver()).executeScript( "mobile:button-text:click", getElementProperties() ) + "";
            if ( "false".equals( result.toLowerCase() ) )
                throw new ScriptException( "Visual click failed to execute" );

        }
        else
        {
            if ( Boolean.parseBoolean( iExec.getStatus() ) )
            {
                int centerWidth = Integer.parseInt( iExec.getLeft() ) + (Integer.parseInt( iExec.getWidth() ) / 2);
                int centerHeight = Integer.parseInt( iExec.getTop() ) + (Integer.parseInt( iExec.getHeight() ) / 2);

                int useX = (int) (((double) centerWidth / (double) Integer.parseInt( iExec.getScreenWidth() )) * 100);
                int useY = (int) (((double) centerHeight / (double) Integer.parseInt( iExec.getScreenHeight() )) * 100);

                GestureManager.instance().createPress( new Point( useX, useY ) ).executeGesture( webDriver );

            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.Element#getExecutionId()
     */
    public String getExecutionId()
    {
        String executionId = null;

        if ( webDriver instanceof PropertyProvider )
        {
            executionId = ((PropertyProvider) webDriver).getProperty( EXECUTION_ID );
        }

        if ( executionId == null )
        {
            if ( webDriver instanceof HasCapabilities )
            {
                Capabilities caps = ((HasCapabilities) webDriver).getCapabilities();
                executionId = caps.getCapability( "executionId" ).toString();
            }
        }

        if ( executionId == null )
        {
            if ( webDriver instanceof NativeDriverProvider )
            {
                WebDriver nativeDriver = ((NativeDriverProvider) webDriver).getNativeDriver();
                if ( nativeDriver instanceof HasCapabilities )
                {
                    Capabilities caps = ((HasCapabilities) webDriver).getCapabilities();
                    executionId = caps.getCapability( "executionId" ).toString();
                }
            }
        }

        if ( executionId == null )
            log.warn( "No Execution ID could be located" );

        return executionId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.element.Element#getDeviceName()
     */
    public String getDeviceName()
    {
        String executionId = null;

        if ( webDriver instanceof PropertyProvider )
        {
            executionId = ((PropertyProvider) webDriver).getProperty( DEVICE_NAME );
        }

        if ( executionId == null )
        {
            if ( webDriver instanceof HasCapabilities )
            {
                Capabilities caps = ((HasCapabilities) webDriver).getCapabilities();
                executionId = caps.getCapability( "deviceName" ).toString();
            }
        }

        if ( executionId == null )
        {
            if ( webDriver instanceof NativeDriverProvider )
            {
                WebDriver nativeDriver = ((NativeDriverProvider) webDriver).getNativeDriver();
                if ( nativeDriver instanceof HasCapabilities )
                {
                    Capabilities caps = ((HasCapabilities) webDriver).getCapabilities();
                    executionId = caps.getCapability( "deviceName" ).toString();
                }
            }
        }

        if ( executionId == null )
            log.warn( Thread.currentThread().getName() + ": No Execution ID could be located" );

        return executionId;
    }

    @Override
    protected Dimension _getSize()
    {
        WebElement webElement = getElement();
        return webElement.getSize();
    }

    @Override
    protected Point _getAt()
    {
        WebElement webElement = getElement();
        return webElement.getLocation();
    }

}
