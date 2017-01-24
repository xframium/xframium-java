package org.xframium.container;

import java.util.ArrayList;
import java.util.List;
import org.xframium.page.activity.PageActivity;
import org.xframium.page.element.Element;

public class PageContainer
{

    private String pageName;
    private String className;
    
    private List<Element> elementList = new ArrayList<Element>( 20 );
    private List<PageActivity> activityList = new ArrayList<PageActivity>( 10 );

    public PageContainer( String pageName, String className )
    {
        this.pageName = pageName;
        this.className = className;
    }
    
    public String getPageName()
    {
        return pageName;
    }

    public void setPageName( String pageName )
    {
        this.pageName = pageName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName( String className )
    {
        this.className = className;
    }

    public List<Element> getElementList()
    {
        return elementList;
    }

    public void setElementList( List<Element> elementList )
    {
        this.elementList = elementList;
    }

    public List<PageActivity> getActivityList()
    {
        return activityList;
    }

    public void setActivityList( List<PageActivity> activityList )
    {
        this.activityList = activityList;
    }
    
    
    
    
}
