package org.xframium.reporting;

public class ElementUsage
{
    private String keyName;
    private String siteName;
    private String pageName;
    private String elementName;
    
    private int passCount = 0;
    private int failCount = 0;
    
    public ElementUsage( String siteName, String pageName, String elementName )
    {
        super();
        this.siteName = siteName;
        this.pageName = pageName;
        this.elementName = elementName;
        this.keyName = siteName + "." + pageName + "." + elementName;
    }
    public int getPassCount()
    {
        return passCount;
    }
    public void setPassCount( int passCount )
    {
        this.passCount = passCount;
    }
    public int getFailCount()
    {
        return failCount;
    }
    public void setFailCount( int failCount )
    {
        this.failCount = failCount;
    }
    public String getKeyName()
    {
        return keyName;
    }
    public String getSiteName()
    {
        return siteName;
    }
    public String getPageName()
    {
        return pageName;
    }
    public String getElementName()
    {
        return elementName;
    }
    
    
}