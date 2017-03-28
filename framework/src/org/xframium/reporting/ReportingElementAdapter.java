package org.xframium.reporting;

import java.awt.Image;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.integrations.perfectoMobile.rest.services.Imaging.Resolution;
import org.xframium.page.BY;
import org.xframium.page.StepStatus;
import org.xframium.page.element.Element;
import org.xframium.page.element.SubElement;
import org.xframium.page.element.Element.SetMethod;
import org.xframium.page.element.Element.WAIT_FOR;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

public class ReportingElementAdapter implements Element
{

    private Element baseElement;
    public ReportingElementAdapter( Element baseElement )
    {
        this.baseElement = baseElement;
    }
    
    @Override
    public DeviceWebDriver getWebDriver()
    {
        return baseElement.getWebDriver();
    }
    
    @Override
    public void setExecutionContext( ExecutionContextTest executionContext )
    {
        baseElement.setExecutionContext( executionContext );
    }

    @Override
    public ExecutionContextTest getExecutionContext()
    {
        return baseElement.getExecutionContext();
    }

    @Override
    public String getElementName()
    {
        return baseElement.getElementName();
    }

    @Override
    public String getPageName()
    {
        return baseElement.getPageName();
    }

    @Override
    public String getClassification()
    {
        return baseElement.getClassification();
    }

    @Override
    public void setClassification( String classification )
    {
        baseElement.setClassification( classification );

    }

    @Override
    public void addElementProperty( String name, String value )
    {
        baseElement.addElementProperty( name, value );

    }

    @Override
    public Map<String, String> getElementProperties()
    {
        return baseElement.getElementProperties();
    }

    @Override
    public String getElementProperty( String name )
    {
        return baseElement.getElementProperty( name );
    }

    @Override
    public void addSubElement( SubElement subElement )
    {
        baseElement.addSubElement( subElement );

    }

    @Override
    public BY getBy()
    {
        return baseElement.getBy();
    }

    private KeyWordStep createStep( String type )
    {
        return KeyWordStepFactory.instance().createStep( getElementName(), getPageName(), true, type, "", false, StepFailure.IGNORE, false, "", "", "", 0, "", 0, "", "", "", null, "", false, false, "", "", null, "" );
    }
    
    @Override
    public boolean moveTo()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "MOUSE" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.moveTo();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
        
        return returnValue;
    }

    @Override
    public boolean clickAt( int offsetPercentX, int offsetPercentY )
    {
        getWebDriver().getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.clickAt( offsetPercentX, offsetPercentY );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
        
        return returnValue;
    }

    @Override
    public String getKey()
    {
        return baseElement.getKey();
    }

    @Override
    public boolean press()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "MOUSE" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.press();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
        
        return returnValue;
    }

    @Override
    public boolean isSelected()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "SELECTED" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.isSelected();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean release()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "MOUSE" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.release();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, null );
        
        return returnValue;
    }

    @Override
    public Object getNative()
    {
        return baseElement.getNative();
                
    }

    @Override
    public String getValue()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "GET" ), null, null );
        String returnValue = null;
        
        try
        {
            returnValue = baseElement.getValue();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public String getStyle( String styleProperty )
    {
        getWebDriver().getExecutionContext().startStep( createStep( "ATTRIBUTE" ), null, null );
        String returnValue = null;
        
        try
        {
            returnValue = baseElement.getStyle( styleProperty );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isVisible()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "VISIBLE" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.isVisible();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isPresent()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "EXISTS" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.isPresent();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public Dimension getSize()
    {
        return baseElement.getSize();
    }

    @Override
    public Point getAt()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "AT" ), null, null );
        Point returnValue = null;
        
        try
        {
            returnValue = baseElement.getAt();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isFocused()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "HAS_FOCUS" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.isFocused();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean isEnabled()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "ENABLED" ), null, null );
        boolean returnValue= false;
        
        try
        {
            returnValue = baseElement.isEnabled();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean waitForVisible( long timeOut, TimeUnit timeUnit )
    {
        return waitFor( timeOut, timeUnit, WAIT_FOR.VISIBLE, null );
    }

    @Override
    public boolean waitFor( long timeout, TimeUnit timeUnit, WAIT_FOR waitType, String value )
    {
        getWebDriver().getExecutionContext().startStep( createStep( "WAIT_FOR" ), null, null );
        boolean returnValue = false;
        
        try
        {
            returnValue = baseElement.waitFor( timeout, timeUnit, waitType, value );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public boolean waitForPresent( long timeOut, TimeUnit timeUnit )
    {
        return waitFor( timeOut, timeUnit, WAIT_FOR.PRESENT, null );
    }

    @Override
    public String getAttribute( String attributeName )
    {
        KeyWordStep step = createStep( "ATTRIBUTE" );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, attributeName, "", "" ) );
        getWebDriver().getExecutionContext().startStep( step, null, null );
        
        String returnValue = null;
        
        try
        {
            returnValue = baseElement.getAttribute( attributeName );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        
        return returnValue;
    }

    @Override
    public void setValue( String currentValue )
    {
        setValue( currentValue, SetMethod.DEFAULT );
    }

    @Override
    public void setValue( String currentValue, SetMethod setMethod )
    {
        KeyWordStep step = createStep( "SET" );
        step.addParameter( new KeyWordParameter( ParameterType.STATIC, currentValue, "", "" ) );
        getWebDriver().getExecutionContext().startStep( step, null, null );
        try
        {
            baseElement.setValue( currentValue, setMethod );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public void setDriver( Object webDriver )
    {
        baseElement.setDriver( webDriver );

    }

    @Override
    public void setTimed( boolean timed )
    {
        baseElement.setTimed( timed );
    }

    @Override
    public boolean isTimed()
    {
        return baseElement.isTimed();
    }

    @Override
    public Element[] getAll()
    {
        return baseElement.getAll();
    }

    @Override
    public int getIndex()
    {
        return baseElement.getIndex();
    }

    @Override
    public int getCount()
    {
        return baseElement.getCount();
    }

    @Override
    public Element getContext()
    {
        return baseElement.getContext();
    }

    @Override
    public String getRawKey()
    {
        return baseElement.getRawKey();
    }

    @Override
    public void setContext( Element context )
    {
        baseElement.setContext( context );
    }

    @Override
    public void setDeviceContext( String deviceContext )
    {
        baseElement.setDeviceContext( deviceContext );
    }

    @Override
    public String getDeviceContext()
    {
        return baseElement.getDeviceContext();
    }

    @Override
    public String getExecutionId()
    {
        return baseElement.getExecutionId();
    }

    @Override
    public String getDeviceName()
    {
        return baseElement.getDeviceName();
    }

    @Override
    public Element addToken( String tokenName, String tokenValue )
    {
        return baseElement.addToken( tokenName, tokenValue );
    }

    @Override
    public Element addToken( String tokenPairValue )
    {
        return baseElement.addToken( tokenPairValue );
    }

    @Override
    public Element cloneElement()
    {
        return new ReportingElementAdapter( baseElement.cloneElement() );
    }

    @Override
    public Image getImage( Resolution imageResolution )
    {
        return baseElement.getImage( imageResolution );
    }

    @Override
    public void setCacheNative( boolean cacheNative )
    {
        baseElement.setCacheNative( cacheNative );

    }

    @Override
    public boolean isCacheNative()
    {
        return baseElement.isCacheNative();
    }

    @Override
    public void click()
    {
        getWebDriver().getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        try
        {
            baseElement.click();
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
    }

    @Override
    public void click( int clicks, int waitTime )
    {
        getWebDriver().getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        try
        {
            baseElement.click( clicks, waitTime );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );

    }

    @Override
    public boolean clickFor( int lengthInMillis )
    {
        getWebDriver().getExecutionContext().startStep( createStep( "CLICK" ), null, null );
        boolean returnValue = false;
        try
        {
            returnValue = baseElement.clickFor( lengthInMillis );
        }
        catch( Exception e )
        {
            getWebDriver().getExecutionContext().completeStep( StepStatus.FAILURE, e );
        }
        
        getWebDriver().getExecutionContext().completeStep( StepStatus.SUCCESS, null );
        return returnValue;
    }

    @Override
    public String getName()
    {
        return baseElement.getName();
    }

}
