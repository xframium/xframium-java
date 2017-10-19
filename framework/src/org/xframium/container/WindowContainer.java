package org.xframium.container;

public class WindowContainer
{
    private String url;
    private String title;
    private String handle;
    public WindowContainer( String url, String title, String handle )
    {
        this.url = url;
        this.title = title;
        this.handle = handle;
    }
    public String getUrl()
    {
        return url;
    }
    public void setUrl( String url )
    {
        this.url = url;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle( String title )
    {
        this.title = title;
    }
    public String getHandle()
    {
        return handle;
    }
    public void setHandle( String handle )
    {
        this.handle = handle;
    }
    
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "url: " ).append( url ).append( "\r\n" );
        stringBuilder.append( "title: " ).append( title ).append( "\r\n" );
        stringBuilder.append( "handle: " ).append( handle ).append( "\r\n" );
        return stringBuilder.toString();
        
    }
    
    
}
