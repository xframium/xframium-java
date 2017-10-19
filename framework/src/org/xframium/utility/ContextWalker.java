package org.xframium.utility;

import java.util.ArrayList;
import java.util.List;
import org.xframium.container.ContextContainer;
import org.xframium.device.factory.DeviceWebDriver;

public class ContextWalker
{
    public List<ContextContainer> walkContext( DeviceWebDriver webDriver )
    {
        List<ContextContainer> contextList = new ArrayList<ContextContainer>( 10 );
        
        for ( String contextHandle : webDriver.getContextHandles() )
        {
            contextList.add( new ContextContainer( contextHandle ) );
        }

        return contextList;
    }
}
