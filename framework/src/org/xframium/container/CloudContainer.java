package org.xframium.container;

import java.util.ArrayList;
import java.util.List;
import org.xframium.device.cloud.CloudDescriptor;

public class CloudContainer
{
    private String cloudName;
    private List<CloudDescriptor> cloudList = new ArrayList<CloudDescriptor>( 10 );
    
    public String getCloudName()
    {
        return cloudName;
    }
    public void setCloudName( String cloudName )
    {
        this.cloudName = cloudName;
    }
    public List<CloudDescriptor> getCloudList()
    {
        return cloudList;
    }
    public void setCloudList( List<CloudDescriptor> cloudList )
    {
        this.cloudList = cloudList;
    }
 
}
