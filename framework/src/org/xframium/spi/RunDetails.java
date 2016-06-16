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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class RunDetails implements RunListener
{
    /** The singleton. */
    private static RunDetails singleton = new RunDetails();
    private static DateFormat timeFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss-SSS" );
    private static NumberFormat percentFormat = new DecimalFormat( "##.##" );

    protected static DateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm:ss" );
    protected static DateFormat simpleDateFormat = new SimpleDateFormat( "dd-MMM-yyyy" );

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
        double successValue = ((double) 32 / (double) 69) * 100;
        System.out.println( percentFormat.format( successValue ) );
    }

    public synchronized void writeHTMLIndex( File rootFolder )
    {
        int runTime = (int) System.currentTimeMillis() - (int) startTime;
        TreeMap<String, int[]> caseMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> osMap = new TreeMap<String, int[]>();
        int successCount = 0;
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];

            String deviceKey = device.getManufacturer() + " " + device.getModel();

            int[] caseValue = caseMap.get( runKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                caseMap.put( runKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            String osName = device.getOs();
            if ( osName == null )
                osName = "Unknown";

            caseValue = osMap.get( osName );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                osMap.put( osName, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            if ( (boolean) detailsList.get( i )[2] )
                successCount++;
        }

        StringBuilder stringBuilder = new StringBuilder();

        File useFile = getIndex( rootFolder );

        writePageHeader( stringBuilder, 1 );

        String runLength = String.format( "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours( runTime ), TimeUnit.MILLISECONDS.toMinutes( runTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( runTime ) ),TimeUnit.MILLISECONDS.toSeconds( runTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( runTime ) ) );

        stringBuilder.append( "<br /><div class=\"row statcards\">" );
        stringBuilder.append( "<div class=\"col-sm-3 m-b\"><div class=\"statcard statcard-success\"><div class=\"p-a\"><span class=\"statcard-desc\">Passed</span><h2 class=\"statcard-number\">" + successCount + "</h2></div></div></div>" );
        stringBuilder.append( "<div class=\"col-sm-3 m-b\"><div class=\"statcard statcard-danger\"><div class=\"p-a\"><span class=\"statcard-desc\">Failed</span><h2 class=\"statcard-number\">" + (detailsList.size() - successCount) + "</h2></div></div></div>" );
        stringBuilder.append( "<div class=\"col-sm-3 m-b\"><div class=\"statcard statcard-info\"><div class=\"p-a\"><span class=\"statcard-desc\">Total</span><h2 class=\"statcard-number\">" + detailsList.size() + "</h2></div></div></div>" );
        stringBuilder.append( "<div class=\"col-sm-3 m-b\"><div class=\"statcard statcard-info\"><div class=\"p-a\"><span class=\"statcard-desc\">Run Time</span><h2 class=\"statcard-number\">" + runLength + "</h2></div></div></div>" );
        stringBuilder.append( "</div><br />" );
        stringBuilder.append( "<table class=\"table table-hover table-condensed table-striped\">" );
        stringBuilder.append( "<tr><th>#</th><th>Test Case</th><th>Device</th><th>Device ID</th><th>Status</th><th>Screenshot</th></tr><tbody>" );
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            String location = runKey + "/" + device.getKey() + "/";
            boolean success = (boolean) detailsList.get( i )[2];
            stringBuilder.append( "<tr><td>" ).append( i + 1 ).append( "</td><td><a href='" ).append( location + runKey + ".html'>" ).append( runKey ).append( "</a></td><td>" );
            stringBuilder.append( device.getManufacturer() ).append( " " ).append( device.getModel() ).append( "</td><td>" ).append( device.getKey() ).append( "</td><td>" );
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

        stringBuilder.append( "<tr><td colSpan='6' align='center'><h6>" ).append( new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "executionMap.properties" ).getAbsolutePath() ).append( "</h6></td></tr></tbody></table>" );
        writePageFooter( stringBuilder );

        try
        {

            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();

            writeHTMLDeviceSummary( rootFolder );
            writeHTMLOSSummary( rootFolder );
            writeHTMLTCSummary( rootFolder );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private synchronized void writeHTMLDeviceSummary( File rootFolder )
    {
        StringBuilder stringBuilder = new StringBuilder();

        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();

        for ( int i = 0; i < detailsList.size(); i++ )
        {
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];

            String deviceKey = device.getManufacturer() + " " + device.getModel();

            int[] caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;
        }

        File useFile = getDSIndex( rootFolder );

        writePageHeader( stringBuilder, 2 );
        stringBuilder.append( "<br/><div class=\"col-sm-12 m-b-md\"><div class=\"list-group\"><h4 class=\"list-group-header\">Device Summary</h4>" );

        for ( String deviceName : deviceMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );
            int totalValue = currentRecord[0] + currentRecord[1];
            double successValue = ((double) totalValue / (double) currentRecord[0]) * 100;
            stringBuilder.append( "<a class=\"list-group-item\" href=\"#\"><span class=\"list-group-progress\" style=\"width: " ).append( percentFormat.format( successValue ) ).append( "%;\"></span><span class=\"pull-right text-muted\">" )
                    .append( successValue ).append( "%</span>" ).append( deviceName ).append( "</a>" );
        }

        stringBuilder.append( "</div></div>" );

        writePageFooter( stringBuilder );

        try
        {

            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private synchronized void writeHTMLOSSummary( File rootFolder )
    {
        StringBuilder stringBuilder = new StringBuilder();

        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();

        for ( int i = 0; i < detailsList.size(); i++ )
        {
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];

            String deviceKey = device.getOs();
            if ( deviceKey == null )
                deviceKey = "Unknown";

            int[] caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;
        }

        File useFile = getOSIndex( rootFolder );

        writePageHeader( stringBuilder, 4 );
        stringBuilder.append( "<br/><div class=\"col-sm-12 m-b-md\"><div class=\"list-group\"><h4 class=\"list-group-header\">OS Summary</h4>" );

        for ( String deviceName : deviceMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );
            int totalValue = currentRecord[0] + currentRecord[1];
            double successValue = ((double) totalValue / (double) currentRecord[0]) * 100;
            stringBuilder.append( "<a class=\"list-group-item\" href=\"#\"><span class=\"list-group-progress\" style=\"width: " ).append( percentFormat.format( successValue ) ).append( "%;\"></span><span class=\"pull-right text-muted\">" )
                    .append( successValue ).append( "%</span>" ).append( deviceName ).append( "</a>" );
        }

        stringBuilder.append( "</div></div>" );

        writePageFooter( stringBuilder );

        try
        {

            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private synchronized void writeHTMLTCSummary( File rootFolder )
    {
        StringBuilder stringBuilder = new StringBuilder();

        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();

        for ( int i = 0; i < detailsList.size(); i++ )
        {
            boolean success = (boolean) detailsList.get( i )[2];

            String deviceKey = (String) detailsList.get( i )[0];
            if ( deviceKey == null )
                deviceKey = "Unknown";

            int[] caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;
        }

        File useFile = getTCIndex( rootFolder );

        writePageHeader( stringBuilder, 3 );
        stringBuilder.append( "<br/><div class=\"col-sm-12 m-b-md\"><div class=\"list-group\"><h4 class=\"list-group-header\">Test Case Summary</h4>" );

        for ( String deviceName : deviceMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );
            int totalValue = currentRecord[0] + currentRecord[1];
            double successValue = ((double) totalValue / (double) currentRecord[0]) * 100;
            stringBuilder.append( "<a class=\"list-group-item\" href=\"#\"><span class=\"list-group-progress\" style=\"width: " ).append( percentFormat.format( successValue ) ).append( "%;\"></span><span class=\"pull-right text-muted\">" )
                    .append( successValue ).append( "%</span>" ).append( deviceName ).append( "</a>" );
        }

        stringBuilder.append( "</div></div>" );

        writePageFooter( stringBuilder );

        try
        {

            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    private void writePageFooter( StringBuilder stringBuilder )
    {
        stringBuilder.append( "</div></div></div></div>" );
        stringBuilder.append(
                "<script src=\"http://www.xframium.org/output/assets/js/jquery.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/chart.js\"></script><script src=\"http://www.xframium.org/output/assets/js/tablesorter.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/toolkit.js\"></script><script src=\"http://www.xframium.org/output/assets/js/application.js\"></script>" );
        stringBuilder.append( "</body></html>" );
    }

    private void writePageHeader( StringBuilder stringBuilder, int activeIndex )
    {
        TreeMap<String, int[]> caseMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> osMap = new TreeMap<String, int[]>();
        int successCount = 0;
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];

            String deviceKey = device.getManufacturer() + " " + device.getModel();

            int[] caseValue = caseMap.get( runKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                caseMap.put( runKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            caseValue = deviceMap.get( deviceKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                deviceMap.put( deviceKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            String osName = device.getOs();
            if ( osName == null )
                osName = "Unknown";

            caseValue = osMap.get( osName );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                osMap.put( osName, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            if ( (boolean) detailsList.get( i )[2] )
                successCount++;
        }

        stringBuilder.append( "<html>" );
        stringBuilder.append(
                "<head><link href=\"http://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/toolkit-light.css\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/application.css\" rel=\"stylesheet\"><style>.list-group-progress {background-color: #e6ffe6}</style></head>" );
        stringBuilder.append( "<body><div class=\"container\"><div class=\"row\">" );

        stringBuilder
                .append( "<div class=\"col-sm-3 sidebar\"><nav class=\"sidebar-nav\"><div class=\"collapse nav-toggleable-sm\" id=\"nav-toggleable-sm\"><ul class=\"nav nav-pills nav-stacked\"><li "
                        + (activeIndex == 1 ? " class=\"active\"" : "") + "><a href=\"index.html\">Execution Results</a></li><li " + (activeIndex == 2 ? " class=\"active\"" : "") + "><a href=\"deviceSummary.html\">Device Summary</a></li><li "
                        + (activeIndex == 3 ? " class=\"active\"" : "") + "><a href=\"testSummary.html\">Test Case Summary</a></li><li " + (activeIndex == 4 ? " class=\"active\"" : "")
                        + "><a href=\"osSummary.html\">OS Summary</a></li></ul><hr class=\"visible-xs m-t\"></div></nav></div>" );
        stringBuilder.append( "<div class=\"col-sm-9 content\"><div class=\"dashhead\"><span class=\"pull-right text-muted\">" ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " )
                .append( simpleTimeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</span><h6 class=\"dashhead-subtitle\">xFramium 1.0.1</h6><h3 class=\"dashhead-title\">Test Suite Execution Summary</h3></div>" );

        stringBuilder.append( "<div class=\"row text-center m-t-lg\"><div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );
        stringBuilder.append( "{ value: " ).append( successCount ).append( ", color: '#009900', label: 'Passed' }," );
        stringBuilder.append( "{ value: " ).append( detailsList.size() - successCount ).append( ", color: '#990000', label: 'Failed' }," );
        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\"><a href=\"index.html\">Tests Executed</a></strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );
        for ( String deviceName : caseMap.keySet() )
        {
            int[] currentRecord = caseMap.get( deviceName );

            stringBuilder.append( "{ value: " ).append( currentRecord[0] ).append( ", color: '#009900', label: '" ).append( deviceName ).append( "' }," );
            stringBuilder.append( "{ value: " ).append( currentRecord[1] ).append( ", color: '#990000', label: '" ).append( deviceName ).append( "' }," );

        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\"><a href=\"testSummary.html\">Tests Steps</a></strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );
        for ( String deviceName : deviceMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );

            stringBuilder.append( "{ value: " ).append( currentRecord[0] ).append( ", color: '#009900', label: '" ).append( deviceName ).append( "' }," );
            stringBuilder.append( "{ value: " ).append( currentRecord[1] ).append( ", color: '#990000', label: '" ).append( deviceName ).append( "' }," );

        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\"><a href=\"deviceSummary.html\">Devices</a></strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );
        for ( String deviceName : osMap.keySet() )
        {
            int[] currentRecord = osMap.get( deviceName );

            stringBuilder.append( "{ value: " ).append( currentRecord[0] ).append( ", color: '#009900', label: '" ).append( deviceName ).append( "' }," );
            stringBuilder.append( "{ value: " ).append( currentRecord[1] ).append( ", color: '#990000', label: '" ).append( deviceName ).append( "' }," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\"><a href=\"osSummary.html\">OS</a></strong></center></div>" );

        stringBuilder.append( "</div>" );
    }

    public synchronized void writeDefinitionIndex( File rootFolder )
    {
        StringBuilder stringBuilder = new StringBuilder();

        for ( int i = 0; i < detailsList.size(); i++ )
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
        catch ( Exception e )
        {
            e.printStackTrace();
        }

    }

    public File getIndex( File rootFolder )
    {
        return new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "index.html" );
    }

    public File getDSIndex( File rootFolder )
    {
        return new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "deviceSummary.html" );
    }

    public File getTCIndex( File rootFolder )
    {
        return new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "testSummary.html" );
    }

    public File getOSIndex( File rootFolder )
    {
        return new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "osSummary.html" );
    }

}
