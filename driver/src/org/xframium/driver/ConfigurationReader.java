package org.xframium.driver;

import org.xframium.driver.container.ApplicationContainer;
import org.xframium.driver.container.CloudContainer;
import org.xframium.driver.container.DeviceContainer;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.provider.SuiteContainer;

import java.io.File;
import java.io.InputStream;

public interface ConfigurationReader
{
    public void readConfiguration( File configurationFile, boolean runTest );
    public boolean executeTest();
    public boolean readFile( InputStream inputStream );
    public boolean readFile( File configFile );
    public CloudContainer configureCloud();
    public SuiteContainer configureTestCases( PageDataProvider pdp, boolean parseDataIterators );
    public abstract ElementProvider configurePageManagement( SuiteContainer sC);
    public PageDataProvider configureData();
    public DeviceContainer configureDevice();
    public ApplicationContainer configureApplication();
    
}
