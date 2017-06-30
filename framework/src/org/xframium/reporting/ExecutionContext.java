package org.xframium.reporting;

import java.io.File;
import java.sql.DriverManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.xframium.application.ApplicationDescriptor;
import org.xframium.device.data.DataManager;

public class ExecutionContext
{
    private static transient ExecutionContext singleton = new ExecutionContext();
    private static transient DateFormat timeFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss-SSS" );
    private File reportFolder = null;
    private String suiteName;
    private Date startTime;
    private Date endTime;
    private String gridUrl;
    private String phase;
    private String domain;
    private Map<String,String> configProperties;
    private ApplicationDescriptor aut = null;
    private String[] testTags = new String[ 0 ];
    
    public void setTestTags( String[] testTags)
    {
        if ( testTags == null )
            testTags = new String[ 0 ];
        else
            this.testTags = testTags;
    }
    
    public String[] getTestTags()
    {
        return testTags;
    }

    public Map<String, String> getConfigProperties()
    {
        return configProperties;
    }

    public void setConfigProperties( Map<String, String> configProperties )
    {
        this.configProperties = configProperties;
    }

    private List<Map<String, Object>> executionSummary = new ArrayList<Map<String, Object>>( 10 );
    private Map<String,String> sPMap = new HashMap<String,String>( 10 );

    private ExecutionContext()
    {

    }

    public static ExecutionContext instance()
    {
        return singleton;
    }
    
    public void clear()
    {
        sPMap.clear();
        executionSummary.clear();
    }

    private transient List<ExecutionContextTest> executionList = new ArrayList<ExecutionContextTest>( 10 );

    public void popupateSystemProperties()
    {
        sPMap.clear();
        Properties sP = System.getProperties();
        for ( Object key : sP.keySet() )
        {
            sPMap.put( (String) key, sP.getProperty( (String)key ) ); 
        }
    }
    
    public ApplicationDescriptor getAut()
    {
        return aut;
    }

    public void setAut( ApplicationDescriptor aut )
    {
        if ( aut == null )
            return;
        
        if ( this.aut == null && !aut.getName().equals( "NOOP" ) )
            this.aut = aut;
    }
    
    public String getPhase()
    {
        return phase;
    }

    public void setPhase( String phase )
    {
        this.phase = phase;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain( String domain )
    {
        this.domain = domain;
    }

    public String getGridUrl()
    {
        return gridUrl;
    }

    public void setGridUrl( String gridUrl )
    {
        this.gridUrl = gridUrl;
    }

    public synchronized void addExecution( ExecutionContextTest test )
    {
        executionList.add( test );
        executionSummary.add( test.toMap() );
    }

    public boolean isEnabled()
    {
        return executionList != null && executionList.size() > 0;
    }

    public List<Map<String, Object>> getExecutionSummary()
    {
        return executionSummary;
    }

    public void resetReportFolder()
    {
        reportFolder = null;
    }
    
    public File getReportFolder()
    {
        if ( reportFolder == null )
            reportFolder = new File( DataManager.instance().getReportFolder(), timeFormat.format( new Date( System.currentTimeMillis() ) ) );
        return reportFolder;
    }

    public String getSuiteName()
    {
        return suiteName;
    }

    public void setSuiteName( String suiteName )
    {
        this.suiteName = suiteName;
        startTime = new Date( System.currentTimeMillis() );
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime( Date startTime )
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime( Date endTime )
    {
        this.endTime = endTime;
    }
    
    

}
