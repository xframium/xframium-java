package org.xframium.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordPage;

public class SiteContainer implements Iterable<PageContainer>
{

    private String siteName;
    
    private List<PageContainer> pageList = new ArrayList<PageContainer>( 20 );
    private Map<String,PageContainer> pageMap = new HashMap<String,PageContainer>( 20 );

    public SiteContainer( String siteName )
    {
        super();
        this.siteName = siteName;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }

    public PageContainer getPage( String pageName )
    {
        PageContainer pC = pageMap.get( pageName );
        if ( pC == null )
        {
            pC = new PageContainer( pageName, KeyWordPage.class.getName() );
            pageMap.put( pageName, pC );
            pageList.add( pC );
        }
        
        return pC;
    }
    
    public void addPage( PageContainer pageContainer )
    {
        pageMap.put( pageContainer.getPageName(), pageContainer );
        pageList.add( pageContainer );
    }
    
    @Override
    public Iterator<PageContainer> iterator()
    {
        return pageList.iterator();
    }
    
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "Site Name: " ).append( siteName ).append( "\r\n" );
        
        for ( PageContainer p : pageList )
            stringBuilder.append( p.toString() ).append( "\r\n" );
        
        
        return stringBuilder.toString();
        
    }
    
    
}
