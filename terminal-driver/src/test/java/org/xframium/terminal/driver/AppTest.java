package org.xframium.terminal.driver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.*;
import java.util.*;

import org.openqa.selenium.*;

import org.xframium.terminal.driver.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        Runnable myTest = new Runnable()
            {
                public void run()
                {
                    try
                    {
                        Properties props = new Properties();
                        props.load( Thread.currentThread().getContextClassLoader().getResourceAsStream( "test.properties" ));

                        Tn3270TerminalDriver.StartupDetails details =
                            new  Tn3270TerminalDriver.StartupDetails( props.getProperty("test.terminal.host"),
                                                                      Integer.parseInt(props.getProperty("test.terminal.port")),
                                                                      Integer.parseInt(props.getProperty("test.terminal.type")),
                                                                      props.getProperty("test.app.start"),
                                                                      props.getProperty("test.app.file") );

                        Tn3270TerminalDriver driver = new Tn3270TerminalDriver( details );

                        sleep(3000);

                        WebElement element = driver.findElement( new By.ByXPath( "//Screen[@name='SystemBanner']/Link[@name='ack']" ));
                        element.click();

                        sleep(3000);

                        element = driver.findElement( new By.ByXPath( "//Screen[@name='Login']/Field[@name='username']" ));
                        element.sendKeys("$000");
                        element = driver.findElement( new By.ByXPath( "//Screen[@name='Login']/Field[@name='password']" ));
                        element.sendKeys("music");
                        element = driver.findElement( new By.ByXPath( "//Screen[@name='SystemBanner']/Link[@name='ack']" ));
                        element.click();

                        sleep(3000);

                        element = driver.findElement( new By.ByXPath( "//Screen[@name='ABlankScreen']/Link[@name='ack']" ));
                        element.click();

                        sleep(3000);

                        element = driver.findElement( new By.ByXPath( "//Screen[@name='MainMenu']/Link[@name='work-with-file-system']" ));
                        element.click();

                        sleep(3000);

                        element = driver.findElement( new By.ByXPath( "//Screen[@name='FileSystemMenu']/Link[@name='file-system-help']" ));
                        element.click();

                        sleep(3000);

                        element = driver.findElement( new By.ByXPath( "//Screen[@name='FileSystemHelp']/Link[@name='help-text']" ));

                        System.out.println( "Got text: " + element.getText() );
                    }
                    catch( Throwable e )
                    {
                        e.printStackTrace();
                    }
                }
            };

        new Thread( myTest ).start();
        
        try
        {
        	Thread.currentThread().sleep(1200000);
        }
        catch(Exception e) {}
    }

    private void sleep( int duration )
        throws Exception 
    {
        Thread.currentThread().sleep( duration );
    }
}
