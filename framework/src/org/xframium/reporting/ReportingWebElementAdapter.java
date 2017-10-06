package org.xframium.reporting;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.page.StepStatus;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

public class ReportingWebElementAdapter implements WebElement
{
    private WebElement baseElement;
    private DeviceWebDriver webDriver;
    private By by;
    
    public ReportingWebElementAdapter( WebElement baseElement, DeviceWebDriver webDriver, By by )
    {
        this.baseElement = baseElement;
        this.webDriver = webDriver;
        this.by = by;
    }
    
    private KeyWordStep createStep( String type )
    {
        return KeyWordStepFactory.instance().createStep( by.toString(), "SELENIUM", true, type, "", false, StepFailure.IGNORE, false, "", "", "", 0, "", 0, "", "", "", null, "", false, false, "", "", null, "", "" );
    }
    
    @Override
    public <X> X getScreenshotAs( OutputType<X> target ) throws WebDriverException
    {
        return getScreenshotAs( target );
    }

    @Override
    public void click()
    {
        webDriver.getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        try
        {
            baseElement.click();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public void submit()
    {
        webDriver.getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        try
        {
            baseElement.submit();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public void sendKeys( CharSequence... keysToSend )
    {
        KeyWordStep step = createStep( "SET" );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, keysToSend[ 0 ].toString(), "", "" ) );
        webDriver.getExecutionContext().startStep( step, null, null );
        try
        {
            baseElement.sendKeys( keysToSend );
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public void clear()
    {
        webDriver.getExecutionContext().startStep( createStep( "SET" ), null, null );
        try
        {
            baseElement.clear();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public String getTagName()
    {
        return baseElement.getTagName();
    }

    @Override
    public String getAttribute( String name )
    {
        String returnValue = null;
        KeyWordStep step = createStep( "ATTRIBUTE" );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, name, "", "" ) );
        webDriver.getExecutionContext().startStep( step, null, null );
        try
        {
            returnValue = baseElement.getAttribute( name );
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isSelected()
    {
        boolean returnValue = false;
        webDriver.getExecutionContext().startStep( createStep( "HAS_FOCUS" ), null, null );
        try
        {
            returnValue = baseElement.isSelected();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isEnabled()
    {
        boolean returnValue = false;
        webDriver.getExecutionContext().startStep( createStep( "ENABLED" ), null, null );
        try
        {
            returnValue = baseElement.isSelected();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public String getText()
    {
        String returnValue = null;
        webDriver.getExecutionContext().startStep( createStep( "GET" ), null, null );
        try
        {
            returnValue = baseElement.getText();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public List<WebElement> findElements( By by )
    {
        return baseElement.findElements( by );
    }

    @Override
    public WebElement findElement( By by )
    {
        return baseElement.findElement( by );
    }

    @Override
    public boolean isDisplayed()
    {
        boolean returnValue = false;
        webDriver.getExecutionContext().startStep( createStep( "VISIBLE" ), null, null );
        try
        {
            returnValue = baseElement.isDisplayed();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public Point getLocation()
    {
        Point returnValue = null;
        webDriver.getExecutionContext().startStep( createStep( "AT" ), null, null );
        try
        {
            returnValue = baseElement.getLocation();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public Dimension getSize()
    {
        Dimension returnValue = null;
        webDriver.getExecutionContext().startStep( createStep( "AT" ), null, null );
        try
        {
            returnValue = baseElement.getSize();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public Rectangle getRect()
    {
        Rectangle returnValue = null;
        webDriver.getExecutionContext().startStep( createStep( "AT" ), null, null );
        try
        {
            returnValue = baseElement.getRect();
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public String getCssValue( String propertyName )
    {
        String returnValue = null;
        webDriver.getExecutionContext().startStep( createStep( "ATTRIBUTE" ), null, null );
        try
        {
            returnValue = baseElement.getCssValue( propertyName );
        }
        catch( Exception e )
        {
            webDriver.getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        webDriver.getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

}
