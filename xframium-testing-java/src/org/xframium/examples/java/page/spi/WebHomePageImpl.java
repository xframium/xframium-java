package org.xframium.examples.java.page.spi;

import org.xframium.examples.java.page.WebHomePage;
import org.xframium.page.AbstractPage;

public class WebHomePageImpl extends AbstractPage implements WebHomePage
{

    public void initializePage()
    {
        // TODO Auto-generated method stub

    }
    
    public void testKeyword()
    {
        try
        {
            executeStep( "COMPARE2", "page", "element", new String[ 0 ] );
        }
        catch( Exception e )
        {
            
        }
        
    }

}
