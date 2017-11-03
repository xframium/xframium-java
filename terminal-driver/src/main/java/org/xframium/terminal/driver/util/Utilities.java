package org.xframium.terminal.driver.util;

import org.openqa.selenium.Point;

import com.bytezone.dm3270.display.*;

import org.xframium.terminal.driver.screen.model.*;
import org.xframium.terminal.driver.*;

public class Utilities
{
    //
    //  Class Data
    //

    private static ScreenDimensions screenDimensions = null;

    //
    // Helpers
    //

    public static void setscreenDimensions( ScreenDimensions dimensions )
    {
        screenDimensions = dimensions;
    }
    
    public static int asTerminalLocation( Location location )
    {
        return (( location.getLine().intValue() * screenDimensions.rows ) + location.getColumn().intValue() );
    }

    public static Point asApplicationLocation( int terminalLocation )
    {
        return new Point( terminalLocation / screenDimensions.rows,
                          terminalLocation % screenDimensions.rows );
    }

    //
    // Helpers
    //

}
