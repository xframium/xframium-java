/*
 * 
 */
package com.xframium.device.factory;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;

// TODO: Auto-generated Javadoc
/**
 * The Class CachedWebElement.
 */
public class MorelandWebElement implements WebElement, Locatable
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

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getLocation()
	 */
	@Override
	public Point getLocation()
	{
		return webElement.getLocation();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getSize()
	 */
	@Override
	public Dimension getSize()
	{
		return webElement.getSize();
	}

	/* (non-Javadoc)
	 * @see org.openqa.selenium.WebElement#getCssValue(java.lang.String)
	 */
	@Override
	public String getCssValue( String propertyName )
	{
		return webElement.getCssValue( propertyName );
	}

    @Override
    public Coordinates getCoordinates()
    {
        if ( webElement instanceof Locatable )
            return ( (Locatable) webElement ).getCoordinates();
        else
            return null;
    }

}
