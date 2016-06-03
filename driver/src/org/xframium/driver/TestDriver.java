/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs LTD (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.driver;

import java.io.File;
public class TestDriver
{

    
    public static void main( String[] args )
    {
        if ( args.length != 1 )
        {
            System.err.println( "Usage: TestDriver [configurationFile]" );
            System.exit( -1 );
        }

        File configFile = new File( args[0] );
        if ( !configFile.exists() )
        {
            System.err.println( "[" + configFile.getAbsolutePath() + "] could not be located" );
            System.exit( -1 );
        }
        try
        {
            if ( configFile.getName().toLowerCase().endsWith( ".txt" ) )
            {
                new TXTConfigurationReader().readConfiguration( configFile, true );
            }
            else if ( configFile.getName().toLowerCase().endsWith( ".xml" ) )
            {
                new XMLConfigurationReader().readConfiguration( configFile, true );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
                
    }

    
    
    
}
