package org.xframium.container;

public class ModelContainer
{
    private String siteName;
    private String pageName;
    private Class className;

    public ModelContainer( String siteName, String pageName, Class className )
    {
        super();
        this.siteName = siteName;
        this.pageName = pageName;
        this.className = className;
    }
    
    public String getSiteName()
    {
        return siteName;
    }
    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }
    public String getPageName()
    {
        return pageName;
    }
    public void setPageName( String pageName )
    {
        this.pageName = pageName;
    }
    public Class getClassName()
    {
        return className;
    }
    public void setClassName( Class className )
    {
        this.className = className;
    }
    
    public void setClassName( String className )
    {
        try
        {
            this.className = Class.forName( className );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    
 
}
