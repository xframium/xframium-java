package org.xframium.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.data.DataProvider.DriverType;

public class DriverContainer
{
    private List<String> perfectoPersonas = new ArrayList<String>( 20 );
    private boolean perfectoWindTunnel = false;
    private String displayReport = null;
    private boolean smartCaching = false;
    private boolean dryRun = false;
    private boolean embeddedServer = false;
    private String trace = "OFF";
    private String stepTags = "";
    private String testTags = "";
    private String deviceTags = "";
    private String suiteName = "";
    private String phase = "";
    private String domain = "";
    private int retryCount = 0;
    
    private String beforeTest;
    private String afterTest;
    private String beforeDevice;
    private Class[] propertyAdapters;
    
    private DriverType driverType;
    private List<TagContainer> extractors = new ArrayList<TagContainer>( 10 );

    private String deviceInterrupts = "";
    private Map<String,String> propertyMap = new HashMap<String,String>( 20 );
    private String reportFolder;
    private List<String> artifactList = new ArrayList<String>( 20 );
    private boolean secureCloud = false;
    private boolean namesConfigured = false;
    
    
    
    public String getTrace()
    {
        return trace;
    }

    public void setTrace(String trace)
    {
        this.trace = trace;
    }

    public int getRetryCount()
    {
        return retryCount;
    }

    public void setRetryCount( int retryCount )
    {
        this.retryCount = retryCount;
        System.setProperty( "driver.retryCount", retryCount + "" );
    }

    public Class[] getPropertyAdapters()
    {
        return propertyAdapters;
    }

    public void setPropertyAdapters( Class[] propertyAdapters )
    {
        this.propertyAdapters = propertyAdapters;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain( String domain )
    {
        this.domain = domain;
    }

    public String getPhase()
    {
        return phase;
    }

    public void setPhase( String phase )
    {
        this.phase = phase;
    }

    public String getBeforeTest()
    {
        return beforeTest;
    }

    public void setBeforeTest( String beforeTest )
    {
        this.beforeTest = beforeTest;
    }

    public String getAfterTest()
    {
        return afterTest;
    }

    public void setAfterTest( String afterTest )
    {
        this.afterTest = afterTest;
    }

    public String getBeforeDevice()
    {
        return beforeDevice;
    }

    public void setBeforeDevice( String beforeDevice )
    {
        this.beforeDevice = beforeDevice;
    }

    public String getDeviceTags()
    {
        return deviceTags;
    }

    public void setDeviceTags( String deviceTags )
    {
        this.deviceTags = deviceTags;
    }

    public List<TagContainer> getExtractors()
    {
        return extractors;
    }

    public void addExtractor( TagContainer tC )
    {
        extractors.add(  tC  );
    }
    
    public void setExtractors( List<TagContainer> extractors )
    {
        this.extractors = extractors;
    }

    public DriverType getDriverType()
    {
        return driverType;
    }

    public void setDriverType( DriverType driverType )
    {
        this.driverType = driverType;
    }

    public String getSuiteName()
    {
        return suiteName;
    }

    public void setSuiteName( String suiteName )
    {
        this.suiteName = suiteName;
    }
    
    public boolean isSecureCloud()
    {
        return secureCloud;
    }

    public void setSecureCloud( boolean secureCloud )
    {
        this.secureCloud = secureCloud;
    }

    private List<String> testNames = new ArrayList<String>( 20 );

    public DriverContainer()
    {
        
    }

    public String getReportFolder()
    {
        return reportFolder;
    }

    public void setReportFolder( String reportFolder )
    {
        this.reportFolder = reportFolder;
    }

    public List<String> getArtifactList()
    {
        return artifactList;
    }

    public boolean isArtifactEnabled( String aType )
    {
        for ( String a : artifactList )
        {
            if ( a.equals( aType ) )
                return true;
        }
        
        return false;
    }
    
    public void setArtifactList( String artifactList )
    {
        if ( artifactList != null && !artifactList.isEmpty() )
        {
            String[] artifacts = artifactList.split( "," );
            
            for ( String artifact : artifacts )
            {
                try
                {
                    this.artifactList.add( artifact.toUpperCase() );
                }
                catch( Exception e )
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void addArtifact( String artifactType )
    {
        artifactList.add( artifactType );
    }
    
    public void setArtifactList( List<String> artifactList )
    {
        this.artifactList = artifactList;
    }

    public String getDeviceInterrupts()
    {
        return deviceInterrupts;
    }

    public void setDeviceInterrupts( String deviceInterrupts )
    {
        this.deviceInterrupts = deviceInterrupts;
    }

    public List<String> getPerfectoPersonas()
    {
        return perfectoPersonas;
    }

    public void setPerfectoPersonas( String perfectoPersonas )
    {
        if ( perfectoPersonas != null )
            this.perfectoPersonas.addAll( Arrays.asList( perfectoPersonas.split( "," ) ) );
        
        perfectoWindTunnel = this.perfectoPersonas.size() > 0;
        
    }
    
    public void setPerfectoPersonas( List<String> perfectoPersonas )
    {
        this.perfectoPersonas = perfectoPersonas;
        perfectoWindTunnel = this.perfectoPersonas.size() > 0;
    }

    public boolean isPerfectoWindTunnel()
    {
        return perfectoWindTunnel;
    }

    public void setPerfectoWindTunnel( boolean perfectoWindTunnel )
    {
        this.perfectoWindTunnel = perfectoWindTunnel;
    }

    public String getDisplayReport()
    {
        return displayReport;
    }

    public void setDisplayReport( String displayReport )
    {
        this.displayReport = displayReport;
    }

    public boolean isSmartCaching()
    {
        return smartCaching;
    }

    public void setSmartCaching( boolean smartCaching )
    {
        this.smartCaching = smartCaching;
    }

    public boolean isDryRun()
    {
        return dryRun;
    }

    public void setDryRun( boolean dryRun )
    {
        this.dryRun = dryRun;
    }

    public boolean isEmbeddedServer()
    {
        return embeddedServer;
    }

    public void setEmbeddedServer( boolean embeddedServer )
    {
        this.embeddedServer = embeddedServer;
    }

    public String getStepTags()
    {
        return stepTags;
    }

    public void setStepTags( String stepTags )
    {
        this.stepTags = stepTags;
    }

    public String getTestTags()
    {
        return testTags;
    }

    public void setTestTags( String testTags )
    {
        if ( testTags != null )
            namesConfigured = true;
        this.testTags = testTags;
    }

    public Map<String, String> getPropertyMap()
    {
        return propertyMap;
    }

    public void setPropertyMap( Map<String, String> propertyMap )
    {
        this.propertyMap = propertyMap;
    }

    public List<String> getTestNames()
    {
        return testNames;
    }

    public void setTestNames( String testNames )
    {
        
        if ( testNames != null )
        {
            namesConfigured = true;
            this.testNames.addAll( Arrays.asList( testNames.split( "," ) ) );
        }
    }
    
    
    
    public boolean isNamesConfigured()
    {
        return namesConfigured;
    }

    public void setNamesConfigured( boolean namesConfigured )
    {
        this.namesConfigured = namesConfigured;
    }

    public void setTestNames( List<String> testNames )
    {
        this.testNames = testNames;
    }
    
    
    
}
