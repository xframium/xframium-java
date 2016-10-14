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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.xframium.Initializable;
import org.xframium.application.ApplicationRegistry;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.proxy.ProxyRegistry;
import org.xframium.history.HistoryWriter;

public class RunDetails implements RunListener
{
    /** The singleton. */
    private static RunDetails singleton = new RunDetails();
    private static DateFormat timeFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss-SSS" );
    private static NumberFormat percentFormat = new DecimalFormat( "##.##" );

    protected static DateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm:ss z" );
    protected static DateFormat simpleDateFormat = new SimpleDateFormat( "dd-MMM-yyyy" );

    private List<Object[]> detailsList = new ArrayList<Object[]>( 20 );
    private HistoryWriter historyWriter = null;

    private String testName;

    public String getTestName()
    {
        return testName;
    }

    public void setTestName( String testName )
    {
        this.testName = testName;
    }

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
        if ( historyWriter == null )
            historyWriter = new HistoryWriter( DataManager.instance().getReportFolder() );
        historyWriter.readData();
        return true;
    }

    @Override
    public boolean validateDevice( Device currentDevice, String runKey )
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void afterRun( Device currentDevice, String runKey, boolean successful, int stepsPassed, int stepsFailed, int stepsIgnored, long startTime, long stopTime, int scriptFailures, int configFailures, int applicationFailures, int cloudFailures )
    {
        detailsList.add( new Object[] { runKey, currentDevice, successful, stepsPassed, stepsFailed, stepsIgnored, startTime, stopTime, scriptFailures, configFailures, applicationFailures, cloudFailures } );

        String location = runKey + "/" + currentDevice.getKey() + "/" + runKey + ".html";
        File indexFile = new File( getRootFolder(), location );
        if ( historyWriter == null )
            historyWriter = new HistoryWriter( DataManager.instance().getReportFolder() );
        historyWriter.addExecution( runKey, currentDevice, startTime, stopTime, stepsPassed, stepsFailed, stepsIgnored, successful, indexFile.getPath(), scriptFailures, configFailures, applicationFailures, cloudFailures );
    }

    @Override
    public void skipRun( Device currentDevice, String runKey )
    {
        detailsList.add( new Object[] { runKey, currentDevice, false, 0, 0, 0, 0, 0 } );
    }

    private class RunComparator implements Comparator
    {

        @Override
        public int compare( Object o1, Object o2 )
        {
            Object[] listOne = (Object[]) o1;
            Object[] listTwo = (Object[]) o2;

            Device deviceOne = (Device) listOne[1];
            Device deviceTwo = (Device) listTwo[1];

            String caseOne = (String) listOne[0];
            String caseTwo = (String) listTwo[0];

            if ( caseOne.equals( caseTwo ) )
                return deviceOne.getEnvironment().compareTo( deviceTwo.getEnvironment() );
            else
                return caseOne.compareTo( caseTwo );
        }

    }
    public synchronized void writeHTMLIndex( File rootFolder, boolean complete )
    {
        Collections.sort( detailsList, new RunComparator() );

        int runTime = (int) System.currentTimeMillis() - (int) startTime;
        TreeMap<String, int[]> caseMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> osMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> envMap = new TreeMap<String, int[]>();
        int[] stepBreakdown = new int[3];
        int[] failureBreakdown = new int[4];

        int successCount = 0;
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];

            stepBreakdown[0] += (int) detailsList.get( i )[3];
            stepBreakdown[1] += (int) detailsList.get( i )[4];
            stepBreakdown[2] += (int) detailsList.get( i )[5];
            long startTime = (long) detailsList.get( i )[6];
            long stopTime = (long) detailsList.get( i )[7];

            failureBreakdown[0] += (int) detailsList.get( i )[8];
            failureBreakdown[1] += (int) detailsList.get( i )[9];
            failureBreakdown[2] += (int) detailsList.get( i )[10];
            failureBreakdown[3] += (int) detailsList.get( i )[11];

            String deviceKey = device.getEnvironment();

            int[] caseValue = caseMap.get( runKey );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0, 0, 0 };
                caseMap.put( runKey, caseValue );
            }

            if ( success )
                caseValue[0]++;
            else
                caseValue[1]++;

            caseValue[2]++;
            caseValue[3] += (stopTime - startTime);

            caseValue = envMap.get( device.getEnvironment() );
            if ( caseValue == null )
            {
                caseValue = new int[] { 0, 0 };
                envMap.put( device.getEnvironment(), caseValue );
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

        String runLength = String.format( "%dh %dm %ds", TimeUnit.MILLISECONDS.toHours( runTime ), TimeUnit.MILLISECONDS.toMinutes( runTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( runTime ) ),
                TimeUnit.MILLISECONDS.toSeconds( runTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( runTime ) ) );

        stringBuilder.append( "<div class=\"row\"><div class=\"pull-right text-muted\"><a hRef=\"../index.html\" style=\"margin-right: 18px;\">Return to Test Execution History</a></div></div>" );
        stringBuilder.append( "<div class=\"panel panel-primary\"><div class=panel-heading><div class=panel-title>Execution Detail (" + runLength + ")</div></div><div class=panel-body><table class=\"table table-hover table-condensed\">" );
        stringBuilder.append( "<tr><th width=\"40%\">Test</th><th width=\"40%\">Environment</th><th width=\"20%\">Duration</th><th>Status</th></tr><tbody>" );
        int[] localBreakdown = new int[4];
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            String location = runKey + "/" + device.getKey() + "/";
            boolean success = (boolean) detailsList.get( i )[2];
            long startTime = (long) detailsList.get( i )[6];
            long stopTime = (long) detailsList.get( i )[7];

            localBreakdown[0] = (int) detailsList.get( i )[8];
            localBreakdown[1] = (int) detailsList.get( i )[9];
            localBreakdown[2] = (int) detailsList.get( i )[10];
            localBreakdown[3] = (int) detailsList.get( i )[11];

            long testRunTime = stopTime - startTime;
            String testRunLength = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( testRunTime ), TimeUnit.MILLISECONDS.toMinutes( testRunTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( testRunTime ) ),
                    TimeUnit.MILLISECONDS.toSeconds( testRunTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( testRunTime ) ) );

            stringBuilder.append( "<tr><td><a href='" ).append( location + runKey + ".html'>" ).append( runKey ).append( "</a></td><td>" );
            stringBuilder.append( device.getEnvironment() ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( testRunLength ).append( "</td><td style=\"padding-top: 10px; \" align=\"center\">" );
            if ( success )
                stringBuilder.append( "<span class=\"label label-success\">Pass</span>" );
            else
            {
                if ( localBreakdown[0] > 0 )
                    stringBuilder.append( "<span class=\"label label-danger\">Script</span>" );
                else if ( localBreakdown[1] > 0 )
                    stringBuilder.append( "<span class=\"label label-danger\">Configuration</span>" );
                else if ( localBreakdown[2] > 0 )
                    stringBuilder.append( "<span class=\"label label-danger\">Application</span>" );
                else if ( localBreakdown[3] > 0 )
                    stringBuilder.append( "<span class=\"label label-danger\">Cloud</span>" );
                else
                    stringBuilder.append( "<span class=\"label label-danger\">Fail</span>" );
            }

            stringBuilder.append( "</td></tr>" );
        }

        stringBuilder.append( "<tr><td colSpan='6' align='center'><h6>" ).append( new File( rootFolder, getRootFolder() + System.getProperty( "file.separator" ) + "executionMap.properties" ).getAbsolutePath() )
                .append( "</h6></td></tr></tbody></table></div></div>" );

        stringBuilder.append( "<div class=\"panel panel-primary\"><div class=panel-heading><div class=panel-title>Environment Summary</div></div><div class=panel-body><table class=\"table table-hover table-condensed\">" );
        stringBuilder.append( "<thead><tr><th width=60%>Environment</th><th nowrap>Pass Rate</th></thead></tr><tbody>" );

        for ( String deviceName : envMap.keySet() )
        {
            int[] currentRecord = deviceMap.get( deviceName );
            int totalValue = currentRecord[0] + currentRecord[1];
            double successValue = 0;
            if ( totalValue > 0 )
                successValue = ((double) currentRecord[0] / (double) totalValue) * 100;

            stringBuilder.append( "<tr><td width=60%>" ).append( deviceName ).append( "</td><td>" ).append( percentFormat.format( successValue ) ).append( "%</td></tr>" );
        }

        stringBuilder.append( "</tbody></table></div></div>" );

        stringBuilder.append( "<div class=\"panel panel-primary\"><div class=panel-heading><div class=panel-title>Test Summary</div></div><div class=panel-body><table class=\"table table-hover table-condensed\">" );
        stringBuilder.append( "<thead><tr><th width=60%>Test</th><th nowrap>Pass Rate</th><th nowrap>Average Duration</th></thead></tr><tbody>" );

        for ( String deviceName : caseMap.keySet() )
        {
            int[] currentRecord = caseMap.get( deviceName );
            int totalValue = currentRecord[0] + currentRecord[1];
            double successValue = 0;
            if ( totalValue > 0 )
                successValue = ((double) currentRecord[0] / (double) totalValue) * 100;

            int runTimex = (int) ((double) currentRecord[3] / (double) currentRecord[2]);
            String runLengthx = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( runTimex ), TimeUnit.MILLISECONDS.toMinutes( runTimex ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( runTimex ) ),
                    TimeUnit.MILLISECONDS.toSeconds( runTimex ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( runTimex ) ) );

            stringBuilder.append( "<tr><td width=60%>" ).append( deviceName ).append( "</td><td>" ).append( percentFormat.format( successValue ) ).append( "%</td><td>" ).append( runLengthx ).append( "</td></tr>" );
        }

        stringBuilder.append( "</tbody></table></div></div>" );

        stringBuilder.append( "<div class=\"panel panel-primary\"><div class=panel-heading><div class=panel-title>Failure Breakdown</div></div><div class=panel-body><table class=\"table table-hover table-condensed\">" );
        stringBuilder.append( "<thead><tr><th width=90%>Failure Type</th><th nowrap>Failure Count</th></tr></thead><tbody>" );
        stringBuilder.append( "<tbody><tr><td width=90%>Scripting Issues</td><td nowrap>" + failureBreakdown[0] + "</td></tr>" );
        stringBuilder.append( "<tr><td width=90%>Configuration Issues</td><td nowrap>" + failureBreakdown[1] + "</td></tr>" );
        stringBuilder.append( "<tr><td width=90%>Application Issues</td><td nowrap>" + failureBreakdown[2] + "</td></tr>" );
        stringBuilder.append( "<tr><td width=90%>Cloud Issues</td><td nowrap>" + failureBreakdown[3] + "</td></tr>" );
        stringBuilder.append( "</tbody></table></div></div></div>" );
        stringBuilder.append( "</div></div></div></div>" );

        writePageFooter( stringBuilder );

        try
        {

            useFile.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();

            if ( complete )
            {
                if ( historyWriter == null )
                    historyWriter = new HistoryWriter( DataManager.instance().getReportFolder() );
                historyWriter.writeData( getRootFolder() + System.getProperty( "file.separator" ) + "index.html", startTime, System.currentTimeMillis(), envMap.size(), osMap.size(), successCount, detailsList.size() - successCount, envMap,
                        failureBreakdown[0], failureBreakdown[1], failureBreakdown[2], failureBreakdown[3] );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        try
        {
            HttpClient httpclient = HttpClients.createDefault();
            
            int CONNECTION_TIMEOUT_MS = 3000; // Timeout in millis.
            
            Builder requestBuilder = RequestConfig.custom()
                    .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
                    .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                    .setSocketTimeout(CONNECTION_TIMEOUT_MS);
            
            /*if ( CloudRegistry.instance().getCloud().getProxyHost() != null && !CloudRegistry.instance().getCloud().getProxyHost().isEmpty() )
            {
                requestBuilder.setProxy( new HttpHost( CloudRegistry.instance().getCloud().getProxyHost(), Integer.parseInt( CloudRegistry.instance().getCloud().getProxyPort() ) ) );
            }*/
            
            if ( ProxyRegistry.instance().getProxyHost() != null && !ProxyRegistry.instance().getProxyHost().isEmpty() )
            {
                requestBuilder.setProxy( new HttpHost( ProxyRegistry.instance().getProxyHost(), Integer.parseInt( ProxyRegistry.instance().getProxyPort() ) ) );
            }
            
            RequestConfig requestConfig = requestBuilder.build();
            
            HttpPost httppost = new HttpPost( "http://www.google-analytics.com/collect" );
            httppost.setConfig( requestConfig );

            List<NameValuePair> params = new ArrayList<NameValuePair>( 2 );
            params.add( new BasicNameValuePair( "v", "1" ) );
            params.add( new BasicNameValuePair( "tid", "UA-80178289-1" ) );
            params.add( new BasicNameValuePair( "cid", "555" ) );
            params.add( new BasicNameValuePair( "t", "pageview" ) );
            params.add( new BasicNameValuePair( "dt", "/testExecution" ) );
            params.add( new BasicNameValuePair( "dp", ApplicationRegistry.instance().getAUT().getName() ) );
            params.add( new BasicNameValuePair( "an", "xFramium" ) );
            params.add( new BasicNameValuePair( "av", Initializable.VERSION ) );
            params.add( new BasicNameValuePair( "dh", CloudRegistry.instance().getCloud().getHostName() ) );
            
            params.add( new BasicNameValuePair( "cm1", detailsList.size() + "" ) );
            params.add( new BasicNameValuePair( "cm2", successCount + "" ) );
            params.add( new BasicNameValuePair( "cm3", (detailsList.size() - successCount) + "" ) );
            params.add( new BasicNameValuePair( "cm4", (stepBreakdown[0] + stepBreakdown[1] + stepBreakdown[2]) + "" ) );
            params.add( new BasicNameValuePair( "cm5", stepBreakdown[0] + "" ) );
            params.add( new BasicNameValuePair( "cm6", stepBreakdown[1] + "" ) );
            params.add( new BasicNameValuePair( "cm7", stepBreakdown[2] + "" ) );
            params.add( new BasicNameValuePair( "cm8", envMap.size() + "" ) );
            params.add( new BasicNameValuePair( "cm9", (runTime / 1000) + "" ) );
            
            params.add( new BasicNameValuePair( "cd2", System.getProperty( "os.name" ) ) );
            params.add( new BasicNameValuePair( "cd3", System.getProperty( "java.version" ) ) );
            params.add( new BasicNameValuePair( "cd4", "X" + Base64.encodeBase64String( CloudRegistry.instance().getCloud().getUserName().getBytes() ) + "=" ) );
            
            httppost.setEntity( new UrlEncodedFormEntity( params, "UTF-8" ) );

            // Execute and get the response.
            HttpResponse response = httpclient.execute( httppost );


        }
        catch ( Exception e )
        {
            
        }

    }

    private void writePageFooter( StringBuilder stringBuilder )
    {

        stringBuilder.append(
                "<script src=\"http://www.xframium.org/output/assets/js/jquery.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/chart.js\"></script><script src=\"http://www.xframium.org/output/assets/js/tablesorter.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/toolkit.js\"></script><script src=\"http://www.xframium.org/output/assets/js/application.js\"></script><script>Chart.defaults.global.defaultFontSize=8;</script>" );

        stringBuilder.append( "</body></html>" );
    }

    private void writePageHeader( StringBuilder stringBuilder, int activeIndex )
    {
        TreeMap<String, int[]> caseMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> deviceMap = new TreeMap<String, int[]>();
        TreeMap<String, int[]> osMap = new TreeMap<String, int[]>();

        int osSuccess = 0;
        int osFail = 0;
        int[] stepBreakdown = new int[3];
        int successCount = 0;
        int scriptFailure = 0;
        int configFailure = 0;
        int appFailure = 0;
        int cloudFailure = 0;
        for ( int i = 0; i < detailsList.size(); i++ )
        {
            String runKey = (String) detailsList.get( i )[0];
            Device device = (Device) detailsList.get( i )[1];
            boolean success = (boolean) detailsList.get( i )[2];
            stepBreakdown[0] += (int) detailsList.get( i )[3];
            stepBreakdown[1] += (int) detailsList.get( i )[4];
            stepBreakdown[2] += (int) detailsList.get( i )[5];

            scriptFailure += (int) detailsList.get( i )[8];
            configFailure += (int) detailsList.get( i )[9];
            appFailure += (int) detailsList.get( i )[10];
            cloudFailure += (int) detailsList.get( i )[11];

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

            String osName = device.getEnvironment();
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

        for ( int[] caseValue : osMap.values() )
        {
            if ( caseValue[1] > 0 )
                osFail++;
            else
                osSuccess++;
        }

        stringBuilder.append( "<html>" );
        stringBuilder.append(
                "<head><link href=\"http://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/toolkit-inverse.css\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/application.css\" rel=\"stylesheet\"><style>.abscenter { margin: auto; position: absolute; top: 0; left: 0; bottom: 0; right: 0; } h2 { margin-bottom: 0px;}  h4 { margin-bottom: 0px;} .pass {color: #1bc98e;}.fail {color: #e64759;}</style></head>" );
        stringBuilder.append( "<body><div class=\"container\"><div class=\"row\">" );

        stringBuilder.append( "<div class=\"col-sm-12 content\"><div class=\"dashhead\"><span class=\"pull-right text-muted\">" ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " )
                .append( simpleTimeFormat.format( new Date( System.currentTimeMillis() ) ) )
                .append( "</span><h6 class=\"dashhead-subtitle\">xFramium " + Initializable.VERSION + "</h6><h3 class=\"dashhead-title\">Test Suite Execution Summary</h3><h6>" + ApplicationRegistry.instance().getAUT().getName() + "</h6></div>" );

        stringBuilder.append( "<div class=\"row text-center m-t-lg\"><div class=\"col-sm-2 m-b-md\"></div><div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\">" );
        stringBuilder.append( "<div class=\"abscenter\"  style=\"width: 100%; height: 100px; vertical-align: center;  line-height:19px; text-align: center; z-index: 999999999999999\"><h2 class=\"text-muted\"><b>" + detailsList.size()
                + "</b></h2><h4><span class=\"pass\">" + successCount + "</span> / <span class=\"fail\">" + (detailsList.size() - successCount) + "</span></h4></div>" );
        stringBuilder.append( "<canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-animation=\"true\" data-animation-easing=\"easeOutQuart\" data-chart=\"doughnut\" data-value=\"[" );
        stringBuilder.append( "{ value: " ).append( successCount ).append( ", color: '#1bc98e', label: 'Passed' }," );

        int failureCount = detailsList.size() - successCount;

        if ( scriptFailure > 0 )
        {
            stringBuilder.append( "{ value: " ).append( scriptFailure ).append( ", color: '#ea6272', label: 'Script Issues' }," );
            failureCount -= scriptFailure;
        }

        if ( configFailure > 0 )
        {
            stringBuilder.append( "{ value: " ).append( configFailure ).append( ", color: '#e74b5e', label: 'Configuration Issues' }," );
            failureCount -= configFailure;
        }

        if ( appFailure > 0 )
        {
            stringBuilder.append( "{ value: " ).append( appFailure ).append( ", color: '#e33549', label: 'Application Issues' }," );
            failureCount -= appFailure;
        }

        if ( cloudFailure > 0 )
        {
            stringBuilder.append( "{ value: " ).append( cloudFailure ).append( ", color: '#e01f35', label: 'Device Issues' }," );
            failureCount -= cloudFailure;
        }

        if ( failureCount > 0 )
        {
            stringBuilder.append( "{ value: " ).append( failureCount ).append( ", color: '#e64759', label: 'General Failures' }," );
        }

        stringBuilder.append( "]\" data-segment-stroke-color=\"white\" data-percentage-inner-cutout=\"70\" /></div><center><strong class=\"text-muted\">Test Executions</strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\">" );
        stringBuilder.append( "<div class=\"abscenter\"  style=\"width: 100%; height: 100px; vertical-align: center;  line-height:19px; text-align: center; z-index: 999999999999999\"><h2 class=\"text-muted\"><b>"
                + (stepBreakdown[0] + stepBreakdown[1] + stepBreakdown[2]) + "</b></h2><h4><span class=\"pass\">" + stepBreakdown[0] + "</span> / <span class=\"fail\">" + stepBreakdown[1] + "</span></h4></div>" );
        stringBuilder.append( "<canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-animation=\"true\" data-animation-easing=\"easeOutQuart\" data-chart=\"doughnut\" data-value=\"[" );
        stringBuilder.append( "{ value: " ).append( stepBreakdown[0] ).append( ", color: '#1bc98e', label: 'Passed' }," );
        stringBuilder.append( "{ value: " ).append( stepBreakdown[1] ).append( ", color: '#e64759', label: 'Failed' }," );
        stringBuilder.append( "{ value: " ).append( stepBreakdown[2] ).append( ", color: '#e4d836', label: 'Ignored' }" );
        stringBuilder.append( "]\" data-segment-stroke-color=\"white\" data-percentage-inner-cutout=\"70\" /></div><center><strong class=\"text-muted\">Tests Steps</strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-3 m-b-md\"><div class=\"w-lg m-x-auto\">" );
        stringBuilder.append( "<div class=\"abscenter\"  style=\"width: 100%; height: 100px; vertical-align: center;  line-height:19px; text-align: center; z-index: 999999999999999\"><h2 class=\"text-muted\"><b>" + (osSuccess + osFail)
                + "</b></h2><h4><span class=\"pass\">" + osSuccess + "</span> / <span class=\"fail\">" + osFail + "</span></h4></div>" );
        stringBuilder.append( "<canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-animation=\"true\" data-animation-easing=\"easeOutQuart\" data-chart=\"doughnut\" data-value=\"[" );
        stringBuilder.append( "{ value: " ).append( osSuccess ).append( ", color: '#1bc98e', label: 'Passed' }," );
        stringBuilder.append( "{ value: " ).append( osFail ).append( ", color: '#e64759', label: 'Failed' }," );
        stringBuilder.append( "]\" data-segment-stroke-color=\"white\" data-percentage-inner-cutout=\"70\" /></div><center><strong class=\"text-muted\">Environments</strong></center></div>" );

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

}
