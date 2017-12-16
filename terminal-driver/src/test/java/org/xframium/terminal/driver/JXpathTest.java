package org.xframium.terminal.driver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.*;
import javax.xml.bind.*;

import org.apache.commons.jxpath.*;
import org.xframium.terminal.driver.screen.model.*;

/**
 * Unit test for simple App.
 */
public class JXpathTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public JXpathTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( JXpathTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testJXpath()
    {
        Application app = null;
        FileReader reader = null;
        
        try
        {
            reader = new FileReader( "c:/Users/Dad/projects/moreland/xframium-java/terminal-driver/src/test/resources/TestApp.xml" );
            
            JAXBContext context = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller un = context.createUnmarshaller();

            JAXBElement<?> rootElement = (JAXBElement<?>)un.unmarshal( reader );
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();

            app = rRoot.getApplication().get(0);

            reader.close();

            JXPathContext applicationRdeader = JXPathContext.newContext( app );

            Object result = applicationRdeader.getValue( "//screen[name='SystemBanner']/link[name='ack']" );
            
            System.out.println(result);
        }
        catch( Throwable e )
        {
            e.printStackTrace();
        }
    }
}
