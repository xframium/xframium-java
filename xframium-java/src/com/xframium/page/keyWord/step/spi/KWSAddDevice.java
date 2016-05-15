package com.xframium.page.keyWord.step.spi;

import java.util.Map;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;
import com.xframium.spi.driver.NativeDriverProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class KWSAddDevice.
 */
public class KWSAddDevice extends AbstractKeyWordStep
{

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
    {
        if ( pageObject == null )
        {
            throw new IllegalStateException( "Page Object was not defined" );
        }

        Object name = null;
        Object deviceId = null;
                
        if ( getParameterList().size() == 2 )
        {
            name = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap );
            if ( !( name instanceof String ) )
                throw new IllegalStateException( "Device name must be of type String" );

            deviceId = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap );
            if ( !( deviceId instanceof String ) )
                throw new IllegalStateException( "Device id must be of type String" );
        }
        else
        {
            throw new IllegalStateException( "add device requires two string properties (name, deviceId)" );
        }

        if ( PageManager.instance().getAlternateWebDriverSource() != null )
        {
            PageManager.instance().getAlternateWebDriverSource().registerAltWebDriver( (String) name, (String) deviceId );
        }
		
        return true;
    }
	
    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

}
