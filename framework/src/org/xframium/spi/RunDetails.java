/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
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
package org.xframium.spi;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RunDetails implements RunListener
{
    /** The singleton. */
    private static RunDetails singleton = new RunDetails();
    private static DateFormat timeFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss-SSS");
    
    private List<Object[]> detailsList = new ArrayList<Object[]>( 20 );
    
    /**
     * Instance.
     *
     * @return the RunDetails
     */
    public static RunDetails instance()
    {
        return singleton;
    }

    /**
     * Instantiates a RunDetails.
     */
    private RunDetails()
    {

    }
    
    private long startTime = System.currentTimeMillis();

    public long getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
    }
    
    public String getRootFolder()
    {
        return timeFormat.format( new Date( startTime ) );
    }

    @Override
    public boolean beforeRun( Device currentDevice, String runKey )
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean validateDevice( Device currentDevice, String runKey )
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void afterRun( Device currentDevice, String runKey, boolean successful )
    {
        detailsList.add( new Object[] { runKey, currentDevice, successful } );
    }
    
    public synchronized void writeHTMLIndex( File rootFolder )
    {
        File useFile = getIndex( rootFolder );
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "<html>" );
        stringBuilder.append( "<head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\"><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\"></head>"  );
        stringBuilder.append( "<body><div class=\"table-responsive\"><table class=\"table table-hover table-condensed table-striped\">" );
        
        stringBuilder.append( "<tr><th>ID</th><th>Test Case</th><th>Device</th><th>Device ID</th><th>Status</th><th></th></tr><tbody>" );
        
        for ( int i=0; i<detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            String location = runKey + "/" + device.getKey() + "/";
            boolean success = (boolean)detailsList.get( i )[2];
            stringBuilder.append( "<tr><td>" ).append( i ).append( "</td><td><a href='" ).append( location + runKey + ".html'>" ).append( runKey ).append( "</a></td><td>" );
            stringBuilder.append(  device.getManufacturer() ).append( " " ).append(  device.getModel() ).append( "</td><td>" ).append( device.getKey() ).append( "</td><td>" );
            stringBuilder.append( success ).append( "</td><td align='center'>" );
            if ( !success )
            {
                //"<a hRef=\"../../artifacts/" + randomFile.getName() + "\" class=\"thumbnail\"><img class=\"img-rounded img-responsive\" src=\"../../artifacts/" + randomFile.getName() + "\" style=\"height: 200px;\"/></a>"
                stringBuilder.append( "<a hRef=\"" + location + "failure-screenshot.png\" class=\"thumbnail\"><img class=\"img-rounded img-responsive\" src=\"" + location + "failure-screenshot.png\" style=\"height: 200px;\"/></a>" );
                //stringBuilder.append( "<img height='150' src='").append( location ).append("failure-screenshot.png'/>" );
            }
            stringBuilder.append( "</td></tr>" );
        }
        
        
        
        stringBuilder.append( "<tr><td colSpan='6' align='center'>" ).append( new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "executionMap.properties" ).getAbsolutePath() ).append( "</td></tr>" );
        
        stringBuilder.append( "</tbody></table></div>" );
        stringBuilder.append( "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>");
        stringBuilder.append( "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>" );
        stringBuilder.append( "</body></html>" );
        
        try
        {
            
            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch( Exception e)
        {
            e.printStackTrace( );
        }
        
    }
    
    public synchronized void writeDefinitionIndex( File rootFolder )
    {
        StringBuilder stringBuilder = new StringBuilder();
        
        for ( int i=0; i<detailsList.size(); i++ )
        {
        	String runKey = (String) detailsList.get( i )[0] + "";
            Device device = (Device) detailsList.get( i )[1];
            String location = runKey + "/" + device.getKey() + "/";
        	
        	stringBuilder.append( runKey ).append( "." ).append( device.getKey() ).append( "=" ).append( location ).append( "executionDefinition.properties" ).append( "\r\n" );

        }

        
        try
        {
            File useFile = new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "executionMap.properties" );
            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch( Exception e)
        {
            e.printStackTrace( );
        }
        
    }
    
    public File getIndex( File rootFolder )
    {
        return new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "index.html" );
    }
    
    
}
