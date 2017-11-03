package org.xframium.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xframium.container.FrameContainer;
import org.xframium.device.factory.DeviceWebDriver;

public class FrameWalker
{
    private Log log = LogFactory.getLog(FrameWalker.class);
    public List<FrameContainer> walkFrames( DeviceWebDriver webDriver )
    {
        List<FrameContainer> frameList = new ArrayList<FrameContainer>( 10 );
        webDriver.manage().timeouts().implicitlyWait( 1000, TimeUnit.MILLISECONDS );
        List<WebElement> elementList = webDriver.findElements( By.tagName( "frame" ) );
        
        for ( WebElement currentFrame : elementList )
        {
            frameList.add( analyzeFrame( webDriver, currentFrame ) );
        }
        
        elementList = webDriver.findElements( By.tagName( "iframe" ) );
        for ( WebElement currentFrame : elementList )
        {
            frameList.add( analyzeFrame( webDriver, currentFrame ) );
        }
        webDriver.manage().timeouts().implicitlyWait( 5000, TimeUnit.MILLISECONDS );
        
        return frameList;
    }
    
    private FrameContainer analyzeFrame( DeviceWebDriver webDriver, WebElement currentFrame )
    {
        if ( currentFrame == null )
            return null;
        
        FrameContainer frameContainer = new FrameContainer( currentFrame.getAttribute( "id" ), currentFrame.getAttribute( "name" ), currentFrame.getAttribute( "src" ), currentFrame.getTagName() );
        
        try
        {

            if ( log.isInfoEnabled() )
                log.info( "Switching to " + currentFrame );
            webDriver.switchTo().frame( currentFrame );
            
            //
            // TODO: Add Source?
            //
            
            List<WebElement> elementList = webDriver.findElements( By.tagName( "frame" ) );
            for ( WebElement newFrame : elementList )
            {
                frameContainer.addFrame( analyzeFrame( webDriver, newFrame ) );
            }
            
            elementList = webDriver.findElements( By.tagName( "iframe" ) );
            for ( WebElement newFrame : elementList )
            {
                frameContainer.addFrame( analyzeFrame( webDriver, newFrame ) );
            }
            
            webDriver.switchTo().parentFrame();
        }
        catch( Exception e )
        {
            log.error( "Error analyzing frame at " + currentFrame, e );
        }
        
        return frameContainer;
    }
}
