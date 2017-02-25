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
/*
 * 
 */
package org.xframium.device.factory;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.HasIdentity;
import org.openqa.selenium.internal.Locatable;

// TODO: Auto-generated Javadoc
/**
 * The Class CachedWebElement.
 */
public class MorelandWebElement implements WebElement, Locatable, HasIdentity
{
	
	private DeviceWebDriver deviceDriver;
	private WebElement webElement;
	
	public MorelandWebElement( DeviceWebDriver deviceDriver, WebElement webElement )
    {
        this.deviceDriver = deviceDriver;
        this.webElement = webElement;
    }

    /* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#click()
	 */
	@Override
	public void click()
	{
		webElement.click();
		deviceDriver.clearCache();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#submit()
	 */
	@Override
	public void submit()
	{
		webElement.submit();
		deviceDriver.clearCache();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#sendKeys(java.lang.CharSequence[])
	 */
	@Override
	public void sendKeys( CharSequence... keysToSend )
	{
	    webElement.sendKeys( keysToSend );
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#clear()
	 */
	@Override
	public void clear()
	{
		webElement.clear();
		
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getTagName()
	 */
	@Override
	public String getTagName()
	{
		return webElement.getTagName();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute( String name )
	{
	    return webElement.getAttribute( name );
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#isSelected()
	 */
	@Override
	public boolean isSelected()
	{
		return webElement.isSelected();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		return webElement.isEnabled();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getText()
	 */
	@Override
	public String getText()
	{
		return webElement.getText();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#findElements(org.openqa.selenium.By)
	 */
	@Override
	public List<WebElement> findElements( By by )
	{
		return webElement.findElements( by );
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#findElement(org.openqa.selenium.By)
	 */
	@Override
	public WebElement findElement( By by )
	{
		return webElement.findElement( by );
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#isDisplayed()
	 */
	@Override
	public boolean isDisplayed()
	{
		return webElement.isDisplayed();
	}

	private Point cachedLocation = null;
	
	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getLocation()
	 */
	@Override
	public Point getLocation()
	{
	    if ( cachedLocation == null )
	        cachedLocation = webElement.getLocation();
		return cachedLocation;
	}

	private Dimension cachedSize = null;
	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getSize()
	 */
	@Override
	public Dimension getSize()
	{
	    if ( cachedSize == null )
	        cachedSize = webElement.getSize();
		return cachedSize;
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getCssValue(java.lang.String)
	 */
	@Override
	public String getCssValue( String propertyName )
	{
		return webElement.getCssValue( propertyName );
	}

	private Coordinates cachedCoordinates = null;
    @Override
    public Coordinates getCoordinates()
    {
        if ( webElement instanceof Locatable )
        {
            if ( cachedCoordinates == null )
                cachedCoordinates = ( (Locatable) webElement ).getCoordinates();
        
            return cachedCoordinates;
        }
        else
            return null;
    }

    public <X> X getScreenshotAs( OutputType<X> arg0 ) throws WebDriverException
    {
        return webElement.getScreenshotAs( arg0 );
    }

    public Rectangle getRect()
    {
        return webElement.getRect();
    }

    public WebElement getWebElement()
    {
        return webElement;
    }

    @Override
    public String getId()
    {
        if ( webElement instanceof HasIdentity )
            return ( (HasIdentity) webElement ).getId();
        else
            return null;
    }
    
    

}
