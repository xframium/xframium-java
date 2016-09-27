package org.xframium.driver.container;

import org.xframium.application.ApplicationDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ApplicationContainer
{
    private String applicationName;
    private List<ApplicationDescriptor> appList = new ArrayList<ApplicationDescriptor>( 10 );
    
    public String getApplicationName()
    {
        return applicationName;
    }
    public void setApplicationName( String applicationName )
    {
        this.applicationName = applicationName;
    }
    public List<ApplicationDescriptor> getAppList()
    {
        return appList;
    }
    public void setAppList( List<ApplicationDescriptor> appList )
    {
        this.appList = appList;
    }
    
    
}
