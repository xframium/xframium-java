package org.xframium.driver;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import org.xframium.container.ApplicationContainer;
import org.xframium.container.CloudContainer;
import org.xframium.container.DeviceContainer;
import org.xframium.container.DriverContainer;
import org.xframium.container.FavoriteContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;

public interface ConfigurationReader
{
    public void readConfiguration( File configurationFile, boolean runTest );
    public void readConfiguration( File configurationFile, boolean runTest, Map<String,String> customConfig );
    public boolean executeTest( SuiteContainer sC );
    public boolean readFile( InputStream inputStream );
    public boolean readFile( File configFile );
    public CloudContainer configureCloud( boolean secured );
    public SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators );
    public abstract ElementProvider configurePageManagement( SuiteContainer sC);
    public PageDataProvider configureData();
    public DeviceContainer configureDevice();
    public ApplicationContainer configureApplication();
    public DriverContainer configureDriver( Map<String, String> customConfig );
    public boolean configureArtifacts( DriverContainer driverContainer );
    public FavoriteContainer configureFavorites();
    public boolean configureContent();
    public String getSuiteName();
    public void afterSuite();
}
