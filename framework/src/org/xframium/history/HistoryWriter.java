package org.xframium.history;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.spi.Device;
import org.xframium.spi.RunDetails;

public class HistoryWriter
{
    private static NumberFormat percentFormat = new DecimalFormat( "##.##" );

    private static DateFormat rangeDate = new SimpleDateFormat( "dd-MMM-yyyy HH:mm z" );
    private static DateFormat simpleTimeFormat = new SimpleDateFormat( "HH:mm:ss z" );
    private static DateFormat simpleDateFormat = new SimpleDateFormat( "dd-MMM-yyyy" );
    private static DateFormat chartDateFormat = new SimpleDateFormat( "dd-MMM" );
    private static Log log = LogFactory.getLog( HistoryWriter.class );
    private static final String HISTORY_FILE = "tcHistory.xml";
    private static final String INDEX_FILE = "index.html";
    private static final String TC_SUMMARY = "tcSummary.html";
    private static final String EXE_DETAIL = "executionDetail.html";
    private File rootFolder;
    private String suiteName;

    private Map<String, TestCase> testMap = new HashMap<String, TestCase>( 20 );
    private Map<String, Environment> environmentMap = new HashMap<String, Environment>( 20 );
    private List<TestSuite> executionList = new ArrayList<TestSuite>( 10 );

    private boolean fileLoaded = false;

    public HistoryWriter( File rootFolder )
    {
        this.rootFolder = rootFolder;
    }

    public void addExecution( String testName, Device device, long startTime, long stopTime, int passed, int failed, int ignored, boolean success, String indexFile )
    {
        String caseName = testName;
        String persona = null;
        String context = null;
        if ( testName.contains( "!" ) )
        {
            String[] parts = testName.split( "!" );
            caseName = parts[0];
            persona = parts[1];

            if ( persona.contains( "." ) )
            {
                parts = persona.split( "\\." );
                persona = parts[0];
                context = parts[1];
            }
        }
        else
        {
            if ( testName.contains( "." ) )
            {
                String[] parts = testName.split( "\\." );
                caseName = parts[0];
                persona = parts[1];
            }
        }

        //
        // Get or create the Test Case container
        //
        TestCase testCase = testMap.get( caseName );
        if ( testCase == null )
        {
            testCase = new TestCase();
            testCase.setName( caseName );

            testMap.put( caseName, testCase );
        }

        Environment useEnv = null;
        for ( Environment env : testCase.getEnvironment() )
        {
            if ( device.getEnvironment().equals( env.getName() ) )
            {
                useEnv = env;
                break;
            }
        }

        if ( useEnv == null )
        {
            useEnv = new Environment();
            useEnv.setBrowserName( device.getBrowserName() );
            useEnv.setBrowserVersion( device.getBrowserVersion() );
            useEnv.setMake( device.getManufacturer() );
            useEnv.setModel( device.getModel() );
            useEnv.setName( device.getEnvironment() );
            useEnv.setOs( device.getOs() );
            useEnv.setOsVersion( device.getOsVersion() );
            useEnv.setResolution( device.getResolution() );
            testCase.getEnvironment().add( useEnv );
        }

        Execution exe = new Execution();
        exe.setContext( context );
        exe.setFailed( failed );
        exe.setFileName( indexFile );
        exe.setPassed( passed );
        exe.setIgnored( ignored );
        exe.setPersona( persona );
        exe.setStartTime( startTime );
        exe.setStopTime( stopTime );
        exe.setSuccess( success );
        exe.setTotal( passed + failed + ignored );
        useEnv.getExecution().add( exe );

    }

    public void writeData( String fileName, long startTime, long endTime, int env, int oss, int pass, int fail, TreeMap<String, int[]> envMap )
    {
        OutputStream os = null;

        ObjectFactory objFactory = new ObjectFactory();
        History hRoot = objFactory.createHistory();
        JAXBElement<History> jRoot = objFactory.createHistory( hRoot );

        hRoot.setName( RunDetails.instance().getTestName() );

        hRoot.getTestCase().addAll( testMap.values() );
        TestSuite testSuite = new TestSuite();
        testSuite.setFileName( fileName );
        testSuite.setStartTime( startTime );
        testSuite.setEndTime( endTime );
        testSuite.setEnv( env );
        testSuite.setOs( oss );
        testSuite.setPass( pass );
        testSuite.setFail( fail );
        
        for ( String envName : envMap.keySet() )
        {
        	Environment newEnv = new Environment();
        	newEnv.setName( envName );
        	
        	int[] ratio = envMap.get( envName );
        	
        	newEnv.setPass( ratio[ 0 ] );
        	newEnv.setFail( ratio[ 1 ] );
        	
        	testSuite.getEnvironment().add( newEnv );
        }
        
        
        executionList.add( testSuite );
        hRoot.getSuite().addAll( executionList );

        try
        {
            JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance( History.class.getPackage().getName() );
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty( javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8" ); // NOI18N
            marshaller.setProperty( javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            os = new FileOutputStream( new File( rootFolder, HISTORY_FILE ) );
            marshaller.marshal( jRoot, os );

            writeIndex();
        }
        catch ( Exception e )
        {
            log.error( "Error writing history file", e );
        }
        finally
        {
            try
            {
                os.close();
            }
            catch ( Exception e )
            {
            }
        }
    }

    public static void main( String[] args )
    {
        HistoryWriter x = new HistoryWriter( new File( "C:/Users/AJ/git/morelandLabs/customerProjects/xmlDriven/google/test-output/" ) );
        x.readData();

        x.writeIndex();

    }

    public synchronized void readData()
    {
    	suiteName = RunDetails.instance().getTestName();
        if ( fileLoaded )
            return;

        fileLoaded = true;
        FileInputStream inputStream = null;
        try
        {
            File historyFile = new File( rootFolder, HISTORY_FILE );

            if ( historyFile.exists() )
            {
                JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
                Unmarshaller u = jc.createUnmarshaller();
                inputStream = new FileInputStream( historyFile );
                JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( inputStream );
                History hRoot = (History) rootElement.getValue();

                suiteName = RunDetails.instance().getTestName();
                if  (suiteName == null )
                	suiteName = hRoot.getName();

                for ( TestCase tc : hRoot.testCase )
                {
                    testMap.put( tc.getName(), tc );

                    for ( Environment env : tc.environment )
                    {
                        environmentMap.put( env.getName(), env );
                    }
                }
                
                executionList.addAll( hRoot.getSuite() );
            }

        }
        catch ( Exception e )
        {
            log.fatal( "Error reading CSV Element File", e );
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch ( Exception e )
            {
            }
        }
    }

    private class TimeComparator implements Comparator<Execution>
    {
        @Override
        public int compare( Execution o1, Execution o2 )
        {
            return Long.compare( o1.getStartTime(), o2.getStartTime() );
        }
    }
    
    private class SuiteTimeComparator implements Comparator<TestSuite>
    {
        @Override
        public int compare( TestSuite o1, TestSuite o2 )
        {
            return Long.compare( o1.getStartTime(), o2.getStartTime() );
        }
    }
    
    private class ReverseSuiteTimeComparator implements Comparator<TestSuite>
    {
        @Override
        public int compare( TestSuite o1, TestSuite o2 )
        {
            return Long.compare( o2.getStartTime(), o1.getStartTime() );
        }
    }
   
    
    public void writeIndex()
    {
        StringBuilder stringBuilder = new StringBuilder();
        writePageHeader( stringBuilder, 1, null );

        Set<TestSuite> suiteSet = new TreeSet<TestSuite>( new SuiteTimeComparator() );
        suiteSet.addAll( this.executionList );
        
        stringBuilder.append( "<div class=\"row\"><div class=\"col-md-6\">" );
        stringBuilder.append( "<canvas id=\"testsExecuted\" data-title=\"Test Executions\" data-labels='[" );
        
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( "\"" ).append( chartDateFormat.format( new Date( ts.getStartTime() ) ) ).append( "\",");
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]' data-fail=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( ts.getFail() ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\" data-pass=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( ts.getPass() ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\"></canvas></div><div class=\"col-md-6\">" );

        
        
        stringBuilder.append( "<canvas id=\"executionTime\" data-title=\"Execution Time (s)\" data-labels='[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( "\"" ).append( chartDateFormat.format( new Date( ts.getStartTime() ) ) ).append( "\",");
        }

        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]' data-pass=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( ( ts.getEndTime() - ts.getStartTime() ) / 1000 ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\"></canvas></div></div>" );
  
        
        stringBuilder.append( "<div class=\"row\"><div class=\"col-md-6\">" );
        stringBuilder.append( "<canvas id=\"testStepsExecuted\" data-title=\"Test Steps Executed\" data-labels='[" );
        
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( chartDateFormat.format( new Date( ts.getStartTime() ) ) ).append( ",");
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]' data-pass=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( ts.getPass() ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\"></canvas></div><div class=\"col-md-6\">" );
        
        
        
        stringBuilder.append( "<canvas id=\"environmentsTested\" data-title=\"Environments Tested\" data-labels='[" );
        for ( TestSuite ts : suiteSet )
        {
        	stringBuilder.append( "\"" ).append( ( ts.getEndTime() - ts.getStartTime() ) / 1000 ).append( "\",");
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]' data-fail=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	int envFailed = 0;
            
        	for ( Environment env : ts.getEnvironment() )
        	{
        		if ( env.getFail() > 0 )
        			envFailed++;
        	}
        	stringBuilder.append( envFailed ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\" data-pass=\"[" );
        for ( TestSuite ts : suiteSet )
        {
        	int envFailed = 0;
            
        	for ( Environment env : ts.getEnvironment() )
        	{
        		if ( env.getFail() == 0 )
        			envFailed++;
        	}
        	stringBuilder.append( envFailed ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 ).append( "]\"></canvas></div></div>" );
        
        Set<TestSuite> reverseSuiteSet = new TreeSet<TestSuite>( new ReverseSuiteTimeComparator() );
        reverseSuiteSet.addAll( this.executionList );
        
        stringBuilder.append( "<div class=\"panel panel-primary\"><div class=panel-heading><div class=panel-title>Executions</div></div><div class=panel-body><table class=\"table table-hover table-condensed\" id=executions>" );
        stringBuilder.append( "<tr><th>When Executed</th><th>Duration</th><th>Environments</th><th>Tests</th><th style=\"color: #1bc98e\">Passed</th><th style=\"color: #E64759\">Failed</th></tr><tbody>" );
        
        for ( TestSuite t : reverseSuiteSet )
        {
        	long testRunTime = t.getEndTime() - t.getStartTime();
            String useRun = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( testRunTime ), TimeUnit.MILLISECONDS.toMinutes( testRunTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( testRunTime ) ),
                    TimeUnit.MILLISECONDS.toSeconds( testRunTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( testRunTime ) ) );
            
            stringBuilder.append( "<tr>" );
            stringBuilder.append( "<td><a hRef=\"" ).append( t.getFileName().replace( "\\", "/") ).append( "\">").append( simpleDateFormat.format( new Date( t.getStartTime() ) ) ).append( " at ").append( simpleTimeFormat.format( new Date( t.getStartTime() ) ) ).append( "</a></td>" );
            stringBuilder.append( "<td>" ).append( useRun ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getEnv() ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getPass() + t.getFail() ).append( "</td>" );
            stringBuilder.append( "<td class=pass>" ).append( t.getPass() ).append( "</td>" );
            stringBuilder.append( "<td class=fail>" ).append( t.getFail() ).append( "</td>" );
            stringBuilder.append( "</tr>" );
            
        }
        
        stringBuilder.append( "</tbody></table></div></div>" );

        writePageFooter( stringBuilder );

        try
        {
            File useFile = new File( rootFolder, INDEX_FILE );
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
            
            writeTCSummary();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void writeTCSummary()
    {
        StringBuilder stringBuilder = new StringBuilder();
        writePageHeader( stringBuilder, 2, null );

        stringBuilder.append( "<br /><div class=\"row statcards\">" );
        for ( TestCase tc : testMap.values() )
        {
            writeTestCase( tc );
            Set<Execution> executionList = new TreeSet<Execution>( new TimeComparator() );
            for ( Environment env : tc.getEnvironment() )
            {
                executionList.addAll( env.getExecution() );
            }

            long totalTime = 0;
            long lastTime = 0;
            for ( Execution exe : executionList )
            {
                lastTime = (exe.getStopTime() - exe.getStartTime());
                totalTime += lastTime;
            }
            long averageTime = totalTime / executionList.size();

            boolean up = lastTime > averageTime;
            boolean even = lastTime == averageTime;

            double deviation = 0;

            if ( averageTime != lastTime )
            {
                if ( up )
                    deviation = 1 - ((double) averageTime / (double) lastTime);
                else
                    deviation = 1 - ((double) lastTime / (double) averageTime);
            }

            String panelType = "primary";
            if ( lastTime != averageTime )
            {
                if ( deviation > 0.15 )
                    panelType = "warning";

                if ( deviation > 0.50 )
                    panelType = "danger";

            }

            String runLength = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( averageTime ), TimeUnit.MILLISECONDS.toMinutes( averageTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( averageTime ) ),
                    TimeUnit.MILLISECONDS.toSeconds( averageTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( averageTime ) ) );

            stringBuilder.append( "<div class=\"col-sm-4 m-b\"><div class=\"statcard statcard-" + panelType + "\"><div class=\"p-a\"><span class=\"statcard-desc\"><a style=\"color: white;\" hRef=\"" + tc.getName() + ".html\">" ).append( tc.getName() ).append( " (" ).append( executionList.size() )
                    .append( ")</a></span><h5>Averge Duration</h5><h4>" ).append( runLength );

            if ( !even )
                stringBuilder.append( "<span style=\"position: absolute; right: 25px;\" class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</span>" );
            stringBuilder.append( "</h4><hr class=\"-hr m-a-0\"></div>" );
            stringBuilder.append( "<canvas id=\"sparkline1\" width=\"378\" height=\"64\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
            for ( Execution exe : executionList )
            {
                stringBuilder.append( (exe.getStopTime() - exe.getStartTime()) / 1000 ).append( "," );
            }
            stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
            stringBuilder.append( "]}]\" data-labels=\"[" );
            for ( Execution exe : executionList )
            {
                stringBuilder.append( "'" ).append( (exe.getStopTime() - exe.getStartTime()) / 1000 ).append( "'," );
            }
            stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
            stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        }

        stringBuilder.append( "</div><br />" );

        writePageFooter( stringBuilder );

        try
        {
            File useFile = new File( rootFolder, TC_SUMMARY );
            FileWriter fileWriter = new FileWriter( useFile );
            fileWriter.write( stringBuilder.toString() );
            fileWriter.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private String generateEnv( Environment env )
    {
        StringBuilder stringBuilder = new StringBuilder();
        if ( env.getMake() != null )
        {
            stringBuilder.append( env.getMake() ).append( "<br/>" );
            if ( env.getModel() != null )
                stringBuilder.append( env.getModel() );
            else
                stringBuilder.append( "&nbsp;" );
            
        }
        else if ( env.getBrowserName() != null )
        {
            stringBuilder.append( env.getBrowserName() ).append( "<br/>" );
            if ( env.getBrowserVersion() != null )
                stringBuilder.append( env.getBrowserVersion() );
            else
                stringBuilder.append( "&nbsp;" );
        }
        else
            stringBuilder.append( "Unknown<br/>Device" );
        
        stringBuilder.append( "<br/>" );
        if ( env.getOs() != null )
        {
            stringBuilder.append( env.getOs() );
            if ( env.getOsVersion() != null )
                stringBuilder.append( env.getOsVersion() );
            else
                stringBuilder.append( "&nbsp;" );
        }
        
        return stringBuilder.toString();
        
    }
    
    public void writeTestCase( TestCase testCase )
    {
        StringBuilder stringBuilder = new StringBuilder();
        writePageHeader( stringBuilder, 2, testCase );

        stringBuilder.append( "<br /><center><h2>" + testCase.getName() + "</h2></center><div class=\"row statcards\">" );
        
        Set<Execution> executionList = new TreeSet<Execution>( new TimeComparator() );
        for ( Environment env : testCase.getEnvironment() )
        {
            executionList.addAll( env.getExecution() );

            long totalTime = 0;
            long lastTime = 0;
            for ( Execution exe : env.getExecution() )
            {
                lastTime = (exe.getStopTime() - exe.getStartTime());
                totalTime += lastTime;
            }
            long averageTime = totalTime / env.getExecution().size();

            boolean up = lastTime > averageTime;
            boolean even = lastTime == averageTime;

            double deviation = 0;

            if ( averageTime != lastTime )
            {
                if ( up )
                    deviation = 1 - ((double) averageTime / (double) lastTime);
                else
                    deviation = 1 - ((double) lastTime / (double) averageTime);
            }

            String panelType = "primary";
            if ( lastTime != averageTime )
            {
                if ( deviation > 0.15 )
                    panelType = "warning";

                if ( deviation > 0.50 )
                    panelType = "danger";

            }

            String runLength = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( averageTime ), TimeUnit.MILLISECONDS.toMinutes( averageTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( averageTime ) ),
                    TimeUnit.MILLISECONDS.toSeconds( averageTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( averageTime ) ) );

            stringBuilder.append( "<div class=\"col-sm-3 m-b\"><div class=\"statcard statcard-" + panelType + "\"><div class=\"p-a\"><span class=\"statcard-desc\"><h6><a style=\"color: white;\" hRef=\"#" + env.getName() + "\">" ).append( generateEnv( env ) ).append( " (" ).append( env.getExecution().size() )
                    .append( ")</a></h6></span><h3>" ).append( runLength );

            if ( !even )
                stringBuilder.append( "<span style=\"position: absolute; right: 25px;\" class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</span>" );
            stringBuilder.append( "</h3><hr class=\"-hr m-a-0\"></div>" );
            stringBuilder.append( "<canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
            for ( Execution exe : env.getExecution() )
            {
                stringBuilder.append( (exe.getStopTime() - exe.getStartTime()) / 1000 ).append( "," );
            }
            stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
            stringBuilder.append( "]}]\" data-labels=\"[" );
            for ( Execution exe : env.getExecution() )
            {
                stringBuilder.append( "'" ).append( (exe.getStopTime() - exe.getStartTime()) / 1000 ).append( "'," );
            }
            stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
            stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        }
        stringBuilder.append( "</div>" );


        

        writePageFooter( stringBuilder );

        try
        {
            File useFile = new File( rootFolder, testCase.getName() + ".html" );
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
                "<script src=\"http://www.xframium.org/output/assets/js/jquery.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/chart.js\"></script><script src=\"http://www.xframium.org/output/assets/js/tablesorter.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/toolkit.js\"></script><script src=\"http://www.xframium.org/output/assets/js/application.js\"></script><script src=\"http://www.xframium.org/js/history.js\"></script>" );
        stringBuilder.append( "</body></html>" );
    }

    private void writePageHeader( StringBuilder stringBuilder, int activeIndex, TestCase testCase )
    {
        int testSuccess = 0;
        int testFail = 0;
        int stepSuccess = 0;
        int stepFail = 0;
        int stepIgnore = 0;
        TreeMap<String, int[]> envMap = new TreeMap<String, int[]>();
        
        Set<TestSuite> reverseSuiteSet = new TreeSet<TestSuite>( new ReverseSuiteTimeComparator() );
        reverseSuiteSet.addAll( this.executionList );

        if ( testCase == null )
        {
            for ( TestCase tc : testMap.values() )
            {
                for ( Environment env : tc.getEnvironment() )
                {
                    String runKey = env.getOs();
                    if ( runKey == null )
                        runKey = "Unknown";
                    int[] caseValue = envMap.get( runKey );
                    if ( caseValue == null )
                    {
                        caseValue = new int[] { 0, 0 };
                        envMap.put( runKey, caseValue );
                    }
    
                    for ( Execution exe : env.getExecution() )
                    {
                        if ( exe.isSuccess() )
                        {
                            caseValue[0]++;
                            testSuccess++;
                        }
                        else
                        {
                            caseValue[1]++;
                            testFail++;
                        }
    
                        stepSuccess += exe.getPassed();
                        stepFail += exe.getFailed();
                        stepIgnore += exe.getIgnored();
                    }
                }
            }
        }
        else
        {
            for ( Environment env : testCase.getEnvironment() )
            {
                String runKey = env.getOs();
                if ( runKey == null )
                    runKey = "Unknown";
                int[] caseValue = envMap.get( runKey );
                if ( caseValue == null )
                {
                    caseValue = new int[] { 0, 0 };
                    envMap.put( runKey, caseValue );
                }

                for ( Execution exe : env.getExecution() )
                {
                    if ( exe.isSuccess() )
                    {
                        caseValue[0]++;
                        testSuccess++;
                    }
                    else
                    {
                        caseValue[1]++;
                        testFail++;
                    }

                    stepSuccess += exe.getPassed();
                    stepFail += exe.getFailed();
                    stepIgnore += exe.getIgnored();
                }
            }
        }
    

        stringBuilder.append( "<html>" );
        stringBuilder.append( "<head><base href=\"http://www.xframium.org/sample/index.html\" /><meta charset=\"utf-8\" /><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" /><title>Test Suite Execution History</title><link href=\"http://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic\" rel=\"stylesheet\" /><link href=\"http://www.xframium.org/output/assets/css/toolkit-inverse.css\" rel=\"stylesheet\" /><link href=\"http://www.xframium.org/output/assets/css/application.css\" rel=\"stylesheet\" /><style> canvas { -moz-user-select: none; -webkit-user-select: none; -ms-user-select: none;  } .row { margin-bottom: 40px; } .pass {color: #1bc98e;} .fail {color: #e64759;} .tablesorter-header-inner {display: inline;}th:focus {outline: 0;}</style><script src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0/jquery.min.js\"></script><script src=\"http://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.1.6/Chart.bundle.js\"></script> <script src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.26.5/js/jquery.tablesorter.min.js\"></script></head>" );
        
        
        stringBuilder.append( "<body><div class=\"container\"><div class=\"row\">" );
        
        stringBuilder.append( "<div class=\"col-sm-12 content\"><div class=\"dashhead\"><span class=\"pull-right text-muted\">Updated " ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " )
                .append( simpleTimeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</span><h6 class=\"dashhead-subtitle\">xFramium 1.0.2</h6><h3 class=\"dashhead-title\">Test Suite Execution History</h3><h6>" + suiteName + "</h6></div>" );

        
        
        
        
        
        
        
        
    }

}
