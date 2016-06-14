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
import java.util.TreeMap;
import java.util.TreeSet;

public class RunDetails implements RunListener
{
    /** The singleton. */
    private static RunDetails singleton = new RunDetails();
    private static DateFormat timeFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss-SSS");
    
    protected static DateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm:ss.SSS");
    protected static DateFormat simpleDateFormat = new SimpleDateFormat( "MM-dd-yyyy");
    
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
    
    public static void main( String[] args )
    {
        double successValue = ( (double)2 / (double)4 ) * 100 ;
        System.out.println( successValue );
    }
    
    public synchronized void writeHTMLIndex( File rootFolder )
    {
        long runTime = ( System.currentTimeMillis() - startTime ) / 1000;
        
        int successCount = 0;
        TreeMap<String,int[]> caseMap = new TreeMap<String,int[]>();
        TreeMap<String,int[]> deviceMap = new TreeMap<String,int[]>();
        
        int caseSuccess = 0;
        int caseFail = 0;
        
        for ( int i=0; i<detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean)detailsList.get( i )[2];
            
            String deviceKey = device.getManufacturer() + " " + device.getModel();
            
            int[] caseValue = caseMap.get( runKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                caseMap.put( runKey, caseValue );
            }
            
            if ( success )
            {
                caseValue[ 0 ]++;
                caseSuccess++;
            }
            else
            {
                caseValue[ 1 ]++;
                caseFail++;
            }
            
            caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }
            
            if ( success )
                caseValue[ 0 ]++;
            else
                caseValue[ 1 ]++;
            
            if ( (boolean)detailsList.get( i )[2] )
                successCount++;
        }
        double successValue = ( ( (double)successCount / (double)detailsList.size() ) * 100 );
        double failValue = ( ( ( (double)detailsList.size() - (double)successCount ) / (double)detailsList.size() ) * 100 );
        
        File useFile = getIndex( rootFolder );
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "<html>" );
        stringBuilder.append( "<head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\"><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\"></head>"  );
        stringBuilder.append( "<body><div class=\"table-responsive container\"><br />" );
        
        stringBuilder.append( "<div class=\"progress\"><div class=\"progress-bar progress-bar-success\" style=\"width: " + (int)successValue + "%\">" ).append( "<span class=\"sr-only\">" ).append( (int)successValue ).append( "% Passed</span></div>" );
        stringBuilder.append( "<div class=\"progress-bar progress-bar-danger progress-bar-striped\" style=\"width: " + (int)failValue + "%\">" ).append( "<span class=\"sr-only\">" ).append( (int)failValue ).append( "% Failed</span></div></div>" );
        
        stringBuilder.append( "<br/><div class=\"panel panel-primary\"><div class=\"panel-heading\"><h3 class=\"panel-title\">Summary for tests ran on " ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " ).append( simpleTimeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</h3></div><div class=\"panel-body\">" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Run Length: </b>" ).append( runTime ).append( " seconds</a>" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Tests Cases Executed:</b> <span class=\"label label-default\">" + caseMap.size() + "</span></a>" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Devices Tested:</b> <span class=\"label label-default\">" + deviceMap.size() + "</span></a>" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Total Test Cases Executed:</b> <span class=\"label label-primary\">" + detailsList.size() + "</span></a>" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Total Tests Passed:</b> <span class=\"label label-success\">").append( successCount ).append( " </span></a>" );
        stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>Total Tests Failed:</b> <span class=\"label label-danger\">").append( detailsList.size() - successCount ).append( " </span></a>" );
        stringBuilder.append( "</div></div>" );
        
        stringBuilder.append( "<br/>" );
        stringBuilder.append( "<div><a class=\"btn btn-primary\" role=\"button\" data-toggle=\"collapse\" href=\"#deviceDetail\" aria-expanded=\"false\">View device breakdown</a>" );
        stringBuilder.append( "<div class=\"panel panel-default collapse\" id=\"deviceDetail\"><div class=\"panel-body\">" );
        for ( String deviceName : deviceMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );
            stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>" + deviceName + ":</b> <span class=\"label label-success\">" + currentRecord[ 0 ] + "</span> <span class=\"label label-danger\">" + currentRecord[ 1 ] + "</span></a>" );
        }
        stringBuilder.append( "</div></div></div>" );
        
        stringBuilder.append( "<br/>" );
        stringBuilder.append( "<div><a class=\"btn btn-primary\" role=\"button\" data-toggle=\"collapse\" href=\"#caseDetail\" aria-expanded=\"false\">View test case breakdown</a>" );
        stringBuilder.append( "<div class=\"panel panel-default collapse\" id=\"caseDetail\"><div class=\"panel-body\">" );
        for ( String deviceName : caseMap.keySet() )
        {
            int[] currentRecord = caseMap.get( deviceName );
            stringBuilder.append( "<a hRef=\"#\" class=\"list-group-item\"><b>" + deviceName + ":</b> <span class=\"label label-success\">" + currentRecord[ 0 ] + "</span> <span class=\"label label-danger\">" + currentRecord[ 1 ] + "</span></a>" );
        }
        stringBuilder.append( "</div></div></div>" );

        stringBuilder.append( "<br/><div><a class=\"btn btn-primary\" role=\"button\" data-toggle=\"collapse\" href=\"#executionDetail\" aria-expanded=\"false\">View execution results</a>" );
        stringBuilder.append( "<br/><div class=\"panel panel-default collapse\" id=\"executionDetail\"><div class=\"panel-body\"><table class=\"table table-hover table-condensed table-striped\">" );
        
        stringBuilder.append( "<tr><th>#</th><th>Test Case</th><th>Device</th><th>Device ID</th><th>Status</th><th>Screenshot</th></tr><tbody>" );
       
        
        for ( int i=0; i<detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            String location = runKey + "/" + device.getKey() + "/";
            boolean success = (boolean)detailsList.get( i )[2];
            stringBuilder.append( "<tr><td>" ).append( i+1 ).append( "</td><td><a href='" ).append( location + runKey + ".html'>" ).append( runKey ).append( "</a></td><td>" );
            stringBuilder.append(  device.getManufacturer() ).append( " " ).append(  device.getModel() ).append( "</td><td>" ).append( device.getKey() ).append( "</td><td>" );
            if ( success )
                stringBuilder.append( "<span class=\"label label-success\">Pass</span>" );
            else
                stringBuilder.append( "<span class=\"label label-danger\">Fail</span>" );
            
            stringBuilder.append( "</td><td align='center'>" );
            if ( !success )
            {
                stringBuilder.append( "<a hRef=\"" + location + "failure-screenshot.png\" class=\"thumbnail\"><img class=\"img-rounded img-responsive\" src=\"" + location + "failure-screenshot.png\" style=\"height: 200px;\"/></a>" );
            }
            stringBuilder.append( "</td></tr>" );
        }
        
        
        
        stringBuilder.append( "<tr><td colSpan='6' align='center'>" ).append( new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "executionMap.properties" ).getAbsolutePath() ).append( "</td></tr>" );
        
        stringBuilder.append( "</tbody></table></div></div></div>" );
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
