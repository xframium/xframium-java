package org.xframium.terminal.driver;

import javafx.scene.Scene;

import com.bytezone.dm3270.application.*;
import com.bytezone.dm3270.display.*;

public class Dm3270Console
    extends Console
{
    private static Dm3270Context.Dm3270Site theSite = null;
    private static Dm3270Context theContext = null;
    
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
}
