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
        int lineNum = location.getLine().intValue();
        int colNum = location.getColumn().intValue();
        int termLoc = (( lineNum * screenDimensions.columns ) + colNum );

        System.out.println( "Term Loc: " + lineNum + " * " + screenDimensions.columns + " + " + colNum + " = " + termLoc );
        
        return termLoc;
    }

    public static Point asApplicationLocation( int terminalLocation )
    {
        return new Point( terminalLocation / screenDimensions.rows,
                          terminalLocation % screenDimensions.rows );
    }

    public static void setTerminalVisibility( boolean value )
    {
        if ( !value )
        {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    //
    // Helpers
    //

}
