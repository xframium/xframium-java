package org.xframium.page.activity;

import java.util.ArrayList;
import java.util.List;

public class PageActivity
{
    private String name;
    private String toPage;
    
    private List<ActivityInitiator> initiatorList = new ArrayList<ActivityInitiator>( 10 );
    private List<ActivityValidator> validatorList = new ArrayList<ActivityValidator>( 10 );
    
    public PageActivity( String name, String toPage )
    {
        super();
        this.name = name;
        this.toPage = toPage;
    }
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public String getToPage()
    {
        return toPage;
    }
    public void setToPage( String toPage )
    {
        this.toPage = toPage;
    }
    public List<ActivityInitiator> getInitiatorList()
    {
        return initiatorList;
    }
    public void setInitiatorList( List<ActivityInitiator> initiatorList )
    {
        this.initiatorList = initiatorList;
    }
    public List<ActivityValidator> getValidatorList()
    {
        return validatorList;
    }
    public void setValidatorList( List<ActivityValidator> validatorList )
    {
        this.validatorList = validatorList;
    }
    
    
}
