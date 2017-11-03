package org.xframium.utility;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.xframium.container.WindowContainer;

public class WindowWalker
{
    private Log log = LogFactory.getLog(WindowWalker.class);
    public List<WindowContainer> walkWindows( WebDriver webDriver )
    {
        List<WindowContainer> windowList = new ArrayList<WindowContainer>( 10 );
        String currentWindow = webDriver.getWindowHandle();
        for ( String windowHandle : webDriver.getWindowHandles() )
        {
            try
            {
                webDriver.switchTo().window( windowHandle );
                windowList.add( new WindowContainer( webDriver.getCurrentUrl(), webDriver.getTitle(), windowHandle ) );
            }
            catch( Exception e )
            {
                log.error( "Could not switch to " + windowHandle, e );
            }
        }
        
        if ( currentWindow != null )
            webDriver.switchTo().window( currentWindow );
        
        return windowList;
    }
}
