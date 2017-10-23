package org.xframium.terminal.driver.util;

import org.xframium.terminal.driver.screen.model.*;
import org.xframium.terminal.driver.*;

public class Utilities
{
    public static int asTerminalLocation( Location location )
    {
        return (( location.getLine().intValue() * 80 ) + location.getColumn().intValue() );
    }

    //
    // Helpers
    //

}
