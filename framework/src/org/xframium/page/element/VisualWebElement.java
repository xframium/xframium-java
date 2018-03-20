package org.xframium.page.element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptException;
import org.xframium.gesture.GestureManager;

public class VisualWebElement implements WebElement
{

    private DeviceWebDriver webDriver;
    private Point location;
    private Dimension size;

    public VisualWebElement ( DeviceWebDriver webDriver, Rectangle r )
    {
        this.location = r.getPoint();
        this.size = r.getDimension();
        this.webDriver = webDriver;

    }
    
    public VisualWebElement ( DeviceWebDriver webDriver, Point location, Dimension size )
    {
        this.location = location;
        this.size = size;
        this.webDriver = webDriver;

    }
    
    @Override
    public <X> X getScreenshotAs( OutputType<X> target ) throws WebDriverException
    {
        return null;
    }

    @Override
    public void click()
    {
        if ( location != null && size != null )
            GestureManager.instance( webDriver ).createPress( webDriver.getExecutionContext().getxFID(), new Point( location.getX() + ( size.getWidth() / 2 ), location.getY() + ( size.getHeight() / 2 ) ) ).executeGesture( webDriver );
        else
            throw new ScriptException( "There was no information to perform a click" );
    }

    @Override
    public void submit()
    {
        

    }

    @Override
    public void sendKeys( CharSequence... keysToSend )
    {
        click();
        Map<String, Object> params = new HashMap<>();
        params.put( "text", keysToSend.toString() );
        webDriver.executeScript( "mobile:typetext", params );
    }

    @Override
    public void clear()
    {
        

    }

    @Override
    public String getTagName()
    {
        return null;
    }

    @Override
    public String getAttribute( String name )
    {
        return null;
    }

    @Override
    public boolean isSelected()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public String getText()
    {
        return null;
    }

    @Override
    public List<WebElement> findElements( By by )
    {
        return null;
    }

    @Override
    public WebElement findElement( By by )
    {
        return null;
    }

    @Override
    public boolean isDisplayed()
    {
        return location != null && size != null;
    }

    @Override
    public Point getLocation()
    {
        return location;
    }

    @Override
    public Dimension getSize()
    {
        return size;
    }

    @Override
    public Rectangle getRect()
    {
        return new Rectangle( location, size );
    }

    @Override
    public String getCssValue( String propertyName )
    {
        return null;
    }
    
    public String toString()
    {
    	return location == null ? "NO LOCATION DATA" : location.toString() + " - " + size == null ? "NO SIZING DATA" : size.toString();
    }

}
