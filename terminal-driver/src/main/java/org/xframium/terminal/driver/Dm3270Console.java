package org.xframium.terminal.driver;

import javafx.scene.Scene;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;

public class Dm3270Console
    extends Console
{
    private static Dm3270Context.Dm3270Site theSite = null;
    private static Dm3270Context theContext = null;
    private static Object monitor = new Object();
    
    @Override
    public void startPartTwo() throws Exception
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

        //
        // OK, we're started now
        //

        notifyOfStartup();
    }

    public Screen getScreen()
    {
        return screen;
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
            e.printStackTrace();
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
