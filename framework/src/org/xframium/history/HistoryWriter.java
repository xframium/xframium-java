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

    public void writeData( String fileName, long startTime, long endTime, int env, int oss, int pass, int fail )
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
        HistoryWriter x = new HistoryWriter( new File( "C:\\Users\\AJ\\git\\morelandLabs\\customerProjects\\xmlDriven\\google\\test-output" ) );
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

        stringBuilder.append( "<br /><div class=\"row statcards\">" );
        Set<Execution> executionList = new TreeSet<Execution>( new TimeComparator() );
        
        int tP = 0;
        int tF = 0;
        int sP = 0;
        int sF = 0;
        int sI = 0;
        
        for ( TestCase tc : testMap.values() )
        {    
            for ( Environment env : tc.getEnvironment() )
            {
                for ( Execution exe : env.getExecution() )
                {
                    if ( exe.isSuccess() )
                        tP++;
                    else
                        tF++;
                    
                    sP+= exe.getPassed();
                    sF+= exe.getFailed();
                    sI+= exe.getIgnored();
                }
                executionList.addAll( env.getExecution() );
            }
        }
        
        Set<TestSuite> suiteSet = new TreeSet<TestSuite>( new SuiteTimeComparator() );
        suiteSet.addAll( this.executionList );
        
        Set<TestSuite> reverseSuiteSet = new TreeSet<TestSuite>( new ReverseSuiteTimeComparator() );
        reverseSuiteSet.addAll( this.executionList );
        
        long totalTime = 0;
        long lastTime = 0;

        for ( TestSuite ss : suiteSet )
        {
            lastTime = (ss.getEndTime() - ss.getStartTime());
            totalTime += lastTime;
        }
        double averageTime = (double)totalTime / (double)suiteSet.size();

        boolean up = lastTime > averageTime;

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
        
        String testRunLength = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( (long)averageTime ), TimeUnit.MILLISECONDS.toMinutes( (long)averageTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours((long) averageTime ) ),
                TimeUnit.MILLISECONDS.toSeconds( (long)averageTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( (long)averageTime ) ) );
        

        stringBuilder.append( "<div class=\"col-sm-12 m-b\">" );
        
        stringBuilder.append( "<h4 class=\"text-muted\">Results for " ).append( rangeDate.format( new Date( suiteSet.toArray( new TestSuite[ 0 ] )[ 0 ].getStartTime() ) ) ).append( " to " ).append( rangeDate.format( new Date( suiteSet.toArray( new TestSuite[ 0 ] )[ suiteSet.size() - 1 ].getStartTime() ) ) ).append( "</h4>");
        stringBuilder.append( "<div class=\"statcard statcard-" + panelType + "\"><div class=\"p-a\"><span class=\"statcard-desc\">Duration</span><h2>" ).append( testRunLength );
        if ( deviation != 0 )
            stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</small>" );

        stringBuilder.append( "</h2><hr class=\"-hr m-a-0\"></div><canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( (ss.getEndTime() - ss.getStartTime()) / 1000 ).append( "," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]}]\" data-labels=\"[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( "'" ).append( (ss.getEndTime() - ss.getStartTime()) / 1000 ).append( "'," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        
        
        
        //
        // Tests Passed
        //
        totalTime = 0;

        for ( TestSuite ss : suiteSet )
        {
        	lastTime = ss.getPass();
            totalTime += lastTime;
        }
        stringBuilder.append( "<div class=\"col-sm-6 m-b\"><div class=\"statcard statcard-success\"><div class=\"p-a\"><span class=\"statcard-desc\">Tests Passed</span><h2>" ).append( tP );
        
        
        
        averageTime = (double)totalTime / (double)suiteSet.size();
        
        deviation = 0;

        if ( averageTime != lastTime )
        {
            if ( up )
                deviation = 1 - ((double) averageTime / (double) lastTime);
            else
                deviation = 1 - ((double) lastTime / (double) averageTime);
        }
        
        if ( deviation != 0 )
            stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</small>" );

        stringBuilder.append( "</h2><hr class=\"-hr m-a-0\"></div><canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( ss.getPass() ).append( "," );
        }
        
        
        
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]}]\" data-labels=\"[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( "'" ).append( ss.getPass() ).append( "'," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        
        
        
        //
        // Tests Failed
        //
        totalTime = 0;

        for ( TestSuite ss : suiteSet )
        {
        	lastTime = ss.getFail();
            totalTime += lastTime;
        }
        stringBuilder.append( "<div class=\"col-sm-6 m-b\"><div class=\"statcard statcard-danger\"><div class=\"p-a\"><span class=\"statcard-desc\">Tests Failed</span><h2>" ).append( tF );
        
        
        
        averageTime = (double)totalTime / (double)suiteSet.size();
        
        deviation = 0;

        if ( averageTime > 0 && averageTime != lastTime )
        {
            if ( up )
                deviation = 1 - ((double) averageTime / (double) lastTime);
            else
                deviation = 1 - ((double) lastTime / (double) averageTime);
        }
        
        if ( deviation != 0 )
            stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</small>" );

        stringBuilder.append( "</h2><hr class=\"-hr m-a-0\"></div><canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( ss.getFail() ).append( "," );
        }
        

        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]}]\" data-labels=\"[" );
        for ( TestSuite ss : suiteSet )
        {
            stringBuilder.append( "'" ).append( ss.getPass() ).append( "'," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        
        
        
        //
        // Steps Passed
        //
        totalTime = 0;

        for ( Execution exe : executionList )
        {
        	lastTime = exe.getPassed();
            totalTime += lastTime;
        }
        stringBuilder.append( "<div class=\"col-sm-6 m-b\"><div class=\"statcard statcard-success\"><div class=\"p-a\"><span class=\"statcard-desc\">Steps Passed</span><h2>" ).append( sP );
        
        
        
        averageTime = (double)totalTime / (double)executionList.size();
        
        deviation = 0;

        if ( averageTime != lastTime )
        {
            if ( up )
                deviation = 1 - ((double) averageTime / (double) lastTime);
            else
                deviation = 1 - ((double) lastTime / (double) averageTime);
        }
        
        if ( deviation != 0 )
            stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</small>" );

        stringBuilder.append( "</h2><hr class=\"-hr m-a-0\"></div><canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
        for ( Execution exe : executionList )
        {
            stringBuilder.append( exe.getPassed() ).append( "," );
        }
        
        
        
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]}]\" data-labels=\"[" );
        for ( Execution exe : executionList )
        {
            stringBuilder.append( "'" ).append( exe.getPassed() ).append( "'," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        
        
        
        //
        // Steps Failed
        //
        totalTime = 0;

        for ( Execution exe : executionList )
        {
        	lastTime = exe.getFailed();
            totalTime += lastTime;
        }
        stringBuilder.append( "<div class=\"col-sm-6 m-b\"><div class=\"statcard statcard-danger\"><div class=\"p-a\"><span class=\"statcard-desc\">Steps Failed</span><h2>" ).append( sF );
        
        
        
        averageTime = (double)totalTime / (double)executionList.size();
        
        deviation = 0;

        if ( averageTime > 0 && averageTime != lastTime )
        {
            if ( up )
                deviation = 1 - ((double) averageTime / (double) lastTime);
            else
                deviation = 1 - ((double) lastTime / (double) averageTime);
        }
        
        if ( deviation != 0 )
            stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "%</small>" );

        stringBuilder.append( "</h2><hr class=\"-hr m-a-0\"></div><canvas id=\"sparkline1\" class=\"sparkline\" data-chart=\"spark-line\" data-value=\"[{data:[" );
        for ( Execution exe : executionList )
        {
            stringBuilder.append( exe.getFailed() ).append( "," );
        }
        

        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]}]\" data-labels=\"[" );
        for ( Execution exe : executionList )
        {
            stringBuilder.append( "'" ).append( exe.getPassed() ).append( "'," );
        }
        stringBuilder.deleteCharAt( stringBuilder.length() - 1 );
        stringBuilder.append( "]\" style=\"width: 189px; height: 47px;\"></canvas></div></div>" );
        stringBuilder.append( "</div><br />" );

        
        stringBuilder.append( "<table class=\"table table-hover table-condensed\">" );
        stringBuilder.append( "<tr><th>Execution Date</th><th>Execution Time</th><th>Duration</th><th>Environments</th><th>Tests</th><th>Passed</th><th>Failed</th></tr><tbody>" );
        
        for ( TestSuite t : reverseSuiteSet )
        {
        	long testRunTime = t.getEndTime() - t.getStartTime();
            String useRun = String.format( "%2dh %2dm %2ds", TimeUnit.MILLISECONDS.toHours( testRunTime ), TimeUnit.MILLISECONDS.toMinutes( testRunTime ) - TimeUnit.HOURS.toMinutes( TimeUnit.MILLISECONDS.toHours( testRunTime ) ),
                    TimeUnit.MILLISECONDS.toSeconds( testRunTime ) - TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes( testRunTime ) ) );
            
            stringBuilder.append( "<tr>" );
            stringBuilder.append( "<td><a target=_blank hRef=\"" ).append( t.getFileName().replace( "\\", "/") ).append( "\">").append( simpleDateFormat.format( new Date( t.getStartTime() ) ) ).append( "</a></td>" );
            stringBuilder.append( "<td><a target=_blank hRef=\"" ).append( t.getFileName().replace( "\\", "/") ).append( "\">").append( simpleTimeFormat.format( new Date( t.getStartTime() ) ) ).append( "</a></td>" );
            stringBuilder.append( "<td>" ).append( useRun ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getEnv() ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getPass() + t.getFail() ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getPass() ).append( "</td>" );
            stringBuilder.append( "<td>" ).append( t.getFail() ).append( "</td>" );
            stringBuilder.append( "</tr>" );
            
        }
        
        stringBuilder.append( "</tbody></table>" );

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
                stringBuilder.append( "<small class=\"pull-right delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "</small>" );
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
                stringBuilder.append( "<small class=\"pull-right text-muted delta-indicator delta-" ).append( up ? "positive" : "negative" ).append( "\">" ).append( percentFormat.format( deviation ) ).append( "</small>" );
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
                "<script src=\"http://www.xframium.org/output/assets/js/jquery.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/chart.js\"></script><script src=\"http://www.xframium.org/output/assets/js/tablesorter.min.js\"></script><script src=\"http://www.xframium.org/output/assets/js/toolkit.js\"></script><script src=\"http://www.xframium.org/output/assets/js/application.js\"></script><script>Chart.defaults.global.defaultFontSize=8;</script>" );
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
        stringBuilder.append(
                "<head><meta http-equiv=\"refresh\" content=\"30\"><link href=\"http://fonts.googleapis.com/css?family=Roboto:300,400,500,700,400italic\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/toolkit-inverse.css\" rel=\"stylesheet\"><link href=\"http://www.xframium.org/output/assets/css/application.css\" rel=\"stylesheet\"></head>" );
        stringBuilder.append( "<body><div class=\"container\"><div class=\"row\">" );
        
        //stringBuilder.append( "<div class=\"col-sm-3 sidebar\"><nav class=\"sidebar-nav\"><div class=\"collapse nav-toggleable-sm\" id=\"nav-toggleable-sm\"><ul class=\"nav nav-pills nav-stacked\"><li " + (activeIndex == 1 ? " class=\"active\"" : "")
        //        + "><a href=\"index.html\">Execution Summary</a></li><li " + (activeIndex == 2 ? " class=\"active\"" : "") + "><a href=\"tcSummary.html\">Test Summary</a></li><li " + (activeIndex == 3 ? " class=\"active\"" : "") + "><a href=\"executionDetail.html\">Execution Detail</a></li>"
        //        + "</ul><hr class=\"visible-xs m-t\"></div></nav></div>" );

        stringBuilder.append( "<div class=\"col-sm-10 content\"><div class=\"dashhead\"><span class=\"pull-right text-muted\">Updated " ).append( simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) ).append( " at " )
                .append( simpleTimeFormat.format( new Date( System.currentTimeMillis() ) ) ).append( "</span><h6 class=\"dashhead-subtitle\">xFramium 1.0.1</h6><h3 class=\"dashhead-title\">Test Suite Execution History</h3><h6>" + suiteName + "</div>" );

        stringBuilder.append( "<div class=\"row text-center m-t-lg\"><div class=\"col-sm-2\"></div><div class=\"col-sm-4 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );
        stringBuilder.append( "{ value: " ).append( testSuccess ).append( ", color: '#009900', label: 'Passed' }," );
        stringBuilder.append( "{ value: " ).append( testFail ).append( ", color: '#990000', label: 'Failed' }," );
        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\">Tests Executed</strong></center></div>" );

        stringBuilder.append( "<div class=\"col-sm-4 m-b-md\"><div class=\"w-lg m-x-auto\"><canvas class=\"ex-graph\" width=\"200\" height=\"200\" data-chart=\"doughnut\" data-value=\"[" );

        stringBuilder.append( "{ value: " ).append( stepSuccess ).append( ", color: '#009900', label: 'Passed' }," );
        stringBuilder.append( "{ value: " ).append( stepFail ).append( ", color: '#990000', label: 'Failed' }," );
        stringBuilder.append( "{ value: " ).append( stepIgnore ).append( ", color: '#999900', label: 'Ignored' }" );

        stringBuilder.append( "]\" data-segment-stroke-color=\"#222\" /></div><center><strong class=\"text-muted\">Tests Steps</strong></center></div>" );

    }

}
