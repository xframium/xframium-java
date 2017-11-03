package org.xframium.container;

import java.util.ArrayList;
import java.util.List;

public class FrameContainer
{
    private String id;
    private String name;
    private String src;
    private String tagName;
    private String xPath;
    private List<FrameContainer> frameList = new ArrayList<FrameContainer>( 10 );
    
    
    
    public FrameContainer( String id, String name, String src, String tagName )
    {
        this.id = id;
        this.name = name;
        this.src = src;
        this.tagName = tagName;
    }
    
    
    public String getxPath()
    {
        return xPath;
    }


    public void setxPath( String xPath )
    {
        this.xPath = xPath;
    }


    public String getTagName()
    {
        return tagName;
    }
    public void setTagName( String tagName )
    {
        this.tagName = tagName;
    }
    public String getId()
    {
        return id;
    }
    public void setId( String id )
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName( String name )
    {
        this.name = name;
    }
    public String getSrc()
    {
        return src;
    }
    public void setSrc( String src )
    {
        this.src = src;
    }
    public List<FrameContainer> getFrameList()
    {
        return frameList;
    }

    public void addFrame( FrameContainer frameContainer )
    {
        frameList.add( frameContainer );
    }
    
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "id: " ).append( id ).append( "\r\n" );
        stringBuilder.append( "name: " ).append( name ).append( "\r\n" );
        stringBuilder.append( "src: " ).append( src ).append( "\r\n" );
        stringBuilder.append( "tagName " ).append( tagName ).append( "\r\n" );
        stringBuilder.append( "xPath: " ).append( xPath ).append( "\r\n" );
        for ( FrameContainer f : frameList )
            stringBuilder.append( "\t" ).append( f ).append( "\r\n" );
        
        return stringBuilder.toString();
    }
    
}
