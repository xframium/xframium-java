package org.xframium.jenkins;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.openqa.selenium.WebDriver;
import org.xframium.Initializable;
import org.xframium.artifact.Artifact;
import org.xframium.artifact.ArtifactManager;
import org.xframium.artifact.ArtifactType;
import org.xframium.container.CloudContainer;
import org.xframium.container.DriverContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.device.DeviceManager;
import org.xframium.device.cloud.CloudDescriptor;
import org.xframium.device.cloud.CloudRegistry;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.page.Page;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.listener.KeyWordListener;
import org.xframium.reporting.ExecutionContext;
import org.xframium.reporting.ExecutionContextTest;
import org.xframium.spi.Device;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class XFramiumBuilder extends Builder implements SimpleBuildStep
{

    private final String configFile;
    private final String suiteName;
    private final String testNames;
    private final String tagNames;
    private final String stepTags;
    private final String deviceTags;
    private final String defaultCloud;
    private final Boolean overrideDefaults;

    // Fields in config.jelly must match the parameter names in the
    // "DataBoundConstructor"

    @DataBoundConstructor
    public XFramiumBuilder( String configFile, String suiteName, String testNames, String tagNames, String stepTags, String deviceTags, String defaultCloud, Boolean overrideDefaults )
    {
        super();
        this.configFile = configFile;
        this.suiteName = suiteName;
        this.testNames = testNames;
        this.tagNames = tagNames;
        this.stepTags = stepTags;
        this.deviceTags = deviceTags;
        this.defaultCloud = defaultCloud;
        this.overrideDefaults = overrideDefaults;
    }

    public String getConfigFile()
    {
        return configFile;
    }

    public String getSuiteName()
    {
        return suiteName;
    }

    public String getTestNames()
    {
        return testNames;
    }

    public String getTagNames()
    {
        return tagNames;
    }

    public String getStepTags()
    {
        return stepTags;
    }

    public String getDeviceTags()
    {
        return deviceTags;
    }

    public String getDefaultCloud()
    {
        return defaultCloud;
    }
    

    public Boolean getOverrideDefaults()
    {
        return overrideDefaults;
    }
    
    
    @Override
    public void perform( final Run<?, ?> build, FilePath workspace, Launcher launcher, final TaskListener listener )
    {
        String xFID = UUID.randomUUID().toString();
        try
        {
            listener.getLogger().println( "xF ID: " + xFID );
            
            Map<String,String> customConfig = new HashMap<String,String>(10);
            
            KeyWordDriver.instance( xFID ).addStepListener( new KeyWordListener()
            {
                
                @Override
                public boolean beforeTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                {
                    listener.getLogger().println( "Starting " + keyWordTest.getName() + " on " + ( (DeviceWebDriver) webDriver ).getDevice().getEnvironment() );
                    return true;
                }
                
                @Override
                public boolean beforeStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC, ExecutionContextTest eC )
                {
                    return true;
                }
                
                @Override
                public void afterTest( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                {
                    
                    StringBuilder sBuilder = new StringBuilder();

                    sBuilder.append( eC.getTest().getName() ).append( eC.getStatus() ? " COMPLETED " : " FAILED " ).append( " against " ).append( eC.getDevice().getEnvironment() ).append( "\r\n" );
                    
                    if ( !eC.getStatus() )
                    {
                        if ( eC.getFailedStep() != null )
                        {
                            sBuilder.append( "\tFailed step identified as " ).append( eC.getFailedStep().getName() ).append( " of type " ).append( eC.getFailedStep().getKw() ).append( " on page " ).append( eC.getFailedStep().getPageName() ).append( "\r\n" );
                        }
                        
                        if ( eC.getStepException() != null )
                            sBuilder.append("\t" ).append( eC.getMessage() ).append( "\r\n" );
                    }
                    
                    sBuilder.append( "\tSuccessful Steps: " ).append( eC.getStepCount( StepStatus.SUCCESS ) ).append( "\r\n" );
                    sBuilder.append( "\tFailed Steps: " ).append( eC.getStepCount( StepStatus.FAILURE ) ).append( "\r\n" );
                    sBuilder.append( "\tIgnored Steps: " ).append( eC.getStepCount( StepStatus.FAILURE_IGNORED ) ).append( "\r\n" );
                    sBuilder.append( "\tDuration: " ).append( eC.getEndTime().getTime() - eC.getStartTime().getTime() ).append( " milliseconds" ).append( "\r\n" );
                    
                    if ( eC.getStatus() )
                    {
                        build.setResult( Result.SUCCESS );
                        listener.getLogger().println( sBuilder.toString() );
                    }
                    else
                    {
                        build.setResult( Result.FAILURE );
                        listener.fatalError( sBuilder.toString() );
                    }
                }
                
                @Override
                public void afterStep( WebDriver webDriver, KeyWordStep currentStep, Page pageObject, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, StepStatus stepStatus, SuiteContainer sC, ExecutionContextTest eC )
                {
                    
                    
                }
                
                @Override
                public void afterArtifacts( WebDriver webDriver, KeyWordTest keyWordTest, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, boolean stepPass, SuiteContainer sC, ExecutionContextTest eC )
                {
                    listener.getLogger().println( "Analyzing Artifacts" );
                    
                    
                    for( String key : eC.getExecutionParameters().keySet() )
                    {
                        if ( key.endsWith( Artifact.URL ) )
                        {
                            String artifactName = key.substring( 0, key.length() - Artifact.URL.length() - 1 );
                            
                            String artifactDescription = artifactName;
                            
                            try
                            {
                                artifactDescription = ArtifactType.valueOf( artifactName ).getDescription();
                            }
                            catch( Exception e )
                            {
                                
                            }
                            
                            try
                            {
                                listener.getLogger().print( "External Link for " + artifactDescription + ": " );
                                listener.hyperlink( eC.getExecutionParameters().get( key ), artifactName );
                                listener.getLogger().println( "" );
                                
                            }
                            catch ( IOException e )
                            {
                                
                            }
                        }
                    }
                    
                }
            } );
            
            
            listener.getLogger().println( "xFramium " + Initializable.VERSION );
            listener.getLogger().println( "Attempting to read configuration information from " + configFile );
            
            ConfigurationReader configReader = null;
            if ( configFile.toLowerCase().endsWith( ".txt" ) )
            {
                configReader = new TXTConfigurationReader();
            }
            else if ( configFile.toLowerCase().endsWith( ".xml" ) )
            {
                configReader = new XMLConfigurationReader();
            }
            
            if ( configReader == null )
                throw new IllegalArgumentException( "Unknown file type specified " + configFile );

            Map<String,String> testMap = null;
            
            if ( testNames != null && !testNames.trim().isEmpty() )
            {
                customConfig.put( "driver.testNames", testNames );
                if ( testMap == null )
                    testMap = new HashMap<String,String>( 10 );
                
                for ( String testName : testNames.split( "," ) )
                {
                    testMap.put( testName.trim(), testName.trim() );
                }
            }
            
            if ( tagNames != null && !tagNames.trim().isEmpty() )
                customConfig.put( "driver.tagNames", tagNames );
            
            if ( stepTags != null && !stepTags.trim().isEmpty() )
                customConfig.put( "driver.stepTags", stepTags );
            
            if ( deviceTags != null && !deviceTags.trim().isEmpty() )
                customConfig.put( "driver.deviceTags", deviceTags );
            
            if ( suiteName != null && !suiteName.trim().isEmpty() )
                customConfig.put( "driver.suiteName", suiteName );
            
            customConfig.put( "driver.embeddedServer", "false" );
            
            customConfig.put( "xF-ID", xFID );
            
            SuiteContainer sC = configReader.readConfiguration( new File( configFile ), false, customConfig );
            sC.setxFID( xFID );
            
            if ( tagNames != null && !tagNames.trim().isEmpty() )
            {
                if ( testMap == null )
                    testMap = new HashMap<String,String>( 10 );
                
                for ( KeyWordTest t : sC.getTaggedTests( tagNames.split( "," ) ) )
                    testMap.put( t.getName(), t.getName() );
            }
            
            if ( ExecutionContext.instance( xFID ).getSuiteName() == null || ExecutionContext.instance( xFID ).getSuiteName().isEmpty() )
                ExecutionContext.instance( xFID ).setSuiteName( "CI" );
            
            DataManager.instance( xFID ).setReportFolder( new File( new File( workspace.toURI() ), ExecutionContext.instance( xFID ).getSuiteName() ) );
            ExecutionContext.instance( xFID ).resetReportFolder();
            
            listener.getLogger().println( "Root Output folder set to: " + ExecutionContext.instance( xFID ).getReportFolder( xFID ).getAbsolutePath() );
            
            if ( defaultCloud != null && !defaultCloud.trim().isEmpty() )
                CloudRegistry.instance( xFID ).setCloud( defaultCloud );
            
            ArtifactManager.instance( xFID ).setDisplayArtifact( null );
            
            if ( testMap != null )
            {
                listener.getLogger().println( "Preparing to execute that following " + testMap.size() + " test(s)" );
                for ( String testName : testMap.keySet() )
                    listener.getLogger().println( "\t" + testName );
            }
            else
            {
                String[] testNames = KeyWordDriver.instance( xFID ).getTestNames();
                listener.getLogger().println( "Preparing to execute that following " + testNames.length + " test(s)" );
                for ( String testName : testNames )
                    listener.getLogger().println( "\t" + testName );
            }
            
            listener.getLogger().println( "against the following " + DeviceManager.instance( xFID ).getDevices().size() + "device(s)" );
            for ( Device device : DeviceManager.instance( xFID ).getDevices() )
                listener.getLogger().println( "\t" + device.getEnvironment() );

            listener.getLogger().println( "*********************************************************************************************\r\n" );
            
            Initializable.xFID.set( xFID );
            configReader.executeTest( sC );

            
            build.addAction( new XFramiumAction( "/" + build.getUrl() + "../ws/" + ExecutionContext.instance( xFID ).getSuiteName().replace( " ", "%20" ) + "/" + ExecutionContext.instance( xFID ).getReportFolder( xFID ).getName() + "/index.html", "xFramium Execution Report", "/plugin/xframium-builder/images/xframium.png") );
            
            
        }
        catch( RuntimeException e )
        {
            throw e;
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor()
    {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link XFramiumBuilder}. Used as a singleton. The class is
     * marked as public so that it can be accessed from views.
     *
     * <p>
     * See
     * {@code src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an
               // extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder>
    {
        /**
         * Should we start the embedded Selenium server
         */
        private boolean embeddedServer;

        /**
         * In order to load the persisted global configuration, you have to call
         * load() in the constructor.
         */
        public DescriptorImpl()
        {
            System.setProperty( "hudson.model.DirectoryBrowserSupport.CSP", "" );
            load();
            
        }

        public FormValidation doCheckConfigFile( @QueryParameter String value ) throws IOException, ServletException
        {
            if ( value.length() == 0 )
                return FormValidation.error( "Please specify a driver configruation file" );

            File dC = new File( value );
            if ( !dC.exists() )
                return FormValidation.error( "The specified driver configuration could not be found" );

            return FormValidation.ok();
        }

        public FormValidation doValidateConfigurationFile( @QueryParameter ( "configFile") String value ) throws IOException, ServletException
        {

            File dC = new File( value );
            if ( !dC.exists() )
                return FormValidation.error( "The specified driver configuration could not be found" );

            try
            {

                ConfigurationReader cR = null;

                if ( value.toLowerCase().endsWith( ".xml" ) )
                    cR = new XMLConfigurationReader();
                else if ( value.toLowerCase().endsWith( ".txt" ) )
                    cR = new TXTConfigurationReader();

                if ( cR == null )
                    throw new IllegalArgumentException( "Unknown file type " + value );
                
                cR.readFile( dC );

                PageDataProvider pdp = cR.configureData();
                if ( pdp != null )
                    pdp.readPageData();
                SuiteContainer sC = cR.configureTestCases( pdp, false );
                DriverContainer gC = cR.configureDriver( null );
                cR.configureArtifacts( gC );
                cR.configureCloud( gC.isSecureCloud() );
                cR.configurePageManagement( sC );
                cR.configureApplication();
                cR.configureFavorites();
                cR.configureContent();

            }
            catch ( Throwable e )
            {
                e.printStackTrace();
                return FormValidation.error( "The configuration file provided was not in a format recognized by XFramium or is corrupt" );
            }

            return FormValidation.ok();
        }

        public FormValidation doValidateConfiguration( @QueryParameter ( "configFile") String value, @QueryParameter ( "suiteName") String value2, @QueryParameter ( "testNames") String value3, @QueryParameter ( "tagNames") String value4,
                @QueryParameter ( "stepTags") String value5, @QueryParameter ( "deviceTags") String value6, @QueryParameter ( "defaultCloud") String value7 ) throws IOException, ServletException
        {

            File dC = new File( value );
            if ( !dC.exists() )
                return FormValidation.error( "The specified driver configuration could not be found" );

            try
            {

                ConfigurationReader cR = null;

                if ( value.toLowerCase().endsWith( ".xml" ) )
                    cR = new XMLConfigurationReader();
                else if ( value.toLowerCase().endsWith( ".txt" ) )
                    cR = new TXTConfigurationReader();

                if ( cR == null )
                    throw new IllegalArgumentException( "Unknown file type " + value );
                
                cR.readFile( dC );

                PageDataProvider pdp = cR.configureData();
                if ( pdp != null )
                    pdp.readPageData();
                SuiteContainer sC = cR.configureTestCases( pdp, false );

                if ( value3 != null && !value3.trim().isEmpty() )
                {
                    String[] nameList = value3.split( "," );
                    for ( String tName : nameList )
                    {
                        if ( sC.getTest( tName.trim() ) == null )
                            return FormValidation.error( "A test named " + tName + " was not found" );
                    }
                }
                
                
                

                if ( value4 != null && !value4.trim().isEmpty() )
                {
                    if ( sC.getTaggedTests( value4.split( "," ) ).isEmpty() )
                        return FormValidation.error( "This tag set will not return any tests to execute" );
                }

                DriverContainer gC = cR.configureDriver( null );
                cR.configureArtifacts( gC );
                CloudContainer cC = cR.configureCloud( gC.isSecureCloud() );

                if ( value7 != null && !value7.trim().isEmpty() )
                {
                    boolean found = false;
                    for ( CloudDescriptor c : cC.getCloudList() )
                    {
                        if ( c.getName().equals( value7 ) )
                        {
                            found = true;
                            break;
                        }
                    }

                    if ( !found )
                        return FormValidation.error( value7 + " does not exist in your cloud registry" );
                }
                cR.configurePageManagement( sC );
                cR.configureApplication();
                cR.configureFavorites();
                cR.configureContent();

            }
            catch ( Throwable e )
            {
                e.printStackTrace();
                return FormValidation.error( "The configuration file provided was not in a format recognized by XFramium or is corrupt" );
            }

            return FormValidation.ok();
        }

        public boolean isApplicable( Class<? extends AbstractProject> aClass )
        {
            // Indicates that this builder can be used with all kinds of project
            // types
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName()
        {
            return "XFramium Script Execution";
        }

        @Override
        public boolean configure( StaplerRequest req, JSONObject formData ) throws FormException
        {
            // To persist global configuration information,
            // set that to properties and call save().
            embeddedServer = formData.getBoolean( "embeddedServer" );
            // ^Can also use req.bindJSON(this, formData);
            // (easier when there are many fields; need set* methods for this,
            // like setUseFrench)
            save();
            return super.configure( req, formData );
        }

        /**
         * This method returns true if the global configuration says we should
         * speak French.
         *
         * The method name is bit awkward because global.jelly calls this method
         * to determine the initial state of the checkbox by the naming
         * convention.
         */
        public boolean getEmbeddedServer()
        {
            return embeddedServer;
        }
    }
}
