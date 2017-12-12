package org.xframium.terminal.driver;

import javafx.scene.Scene;

import org.apache.commons.logging.*;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;
import org.xframium.terminal.driver.util.SceneRobot;

public class Dm3270Console
    extends Console
{
    //
    // Class Data
    //

    private static Log log = LogFactory.getLog( Dm3270Console.class );

    //
    // Instance Data
    //
    
    private static Dm3270Context.Dm3270Site theSite = null;
    private static Dm3270Context theContext = null;
    private static Object monitor = new Object();
    
    @Override
    public void startPartTwo() throws Exception
    {
        try
        {
            //
            // JavaFX doesn't have an easy way to get access to the add once launched, so we'll grab
            // a referecne as we pass through our code
            //
            
            theContext.setConsole( this );
            
            //
            // OK, initialize the UI
            //
            
            setModel (theSite);
            setConsolePane (createScreen (Console.Function.TERMINAL, theSite), theSite);
            consolePane.connect ();
        }
        finally
        {   
            //
            // OK, we're started now
            //
               
            notifyOfStartup();
        }
    }

    public Screen getScreen()
    {
        return screen;
    }

    public ConsolePane getConsolePane()
    {
        return consolePane;
    }

    public SceneRobot getRobot()
    {
        return new SceneRobot( getConsoleScene() );
    }

    public static void doIt( Dm3270Context context, Dm3270Context.Dm3270Site site )
    {
        theSite = site;
        theContext = context;

        launch( new String[0] );
    }

    public static void waitForStartup()
    {
    	try
    	{
            synchronized( monitor )
            {
                monitor.wait();
            }
    	}
    	catch( Exception e )
    	{
            log.error( "Start notification failed with: ", e );
    	}
    }

    public static void notifyOfStartup()
    {
        synchronized( monitor )
        {
            monitor.notify();
        }
    }
}
