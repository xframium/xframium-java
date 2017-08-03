package org.xframium.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import org.xframium.page.keyWord.KeyWordTest;

public class SuiteContainer
{
    public List<KeyWordTest> getTestList()
    {
        return testList;
    }

    public List<KeyWordTest> getActiveTestList()
    {
        return activeTestList;
    }

    public List<KeyWordTest> getInactiveTestList()
    {
        return inactiveTestList;
    }

    public List<KeyWordTest> getFunctionList()
    {
        return functionList;
    }

    public Map<String, KeyWordTest> getTestMap()
    {
        return testMap;
    }

    public List<ModelContainer> getModel()
    {
        return modelList;
    }

    public KeyWordTest getTest( String testName )
    {
        return testMap.get( testName );
    }

    public void setElementProvider( ElementProvider elementProvider )
    {
        oR = elementProvider.getSiteList();
    }

    public List<SiteContainer> getSiteList()
    {
        return oR;
    }

    public PageDataProvider getDataProvider()
    {
        return dataProvider;
    }

    public void setDataProvider( PageDataProvider dataProvider )
    {
        this.dataProvider = dataProvider;
    }

    private List<SiteContainer> oR;
    private List<KeyWordTest> activeTestList = new ArrayList<KeyWordTest>( 10 );
    private List<KeyWordTest> testList = new ArrayList<KeyWordTest>( 10 );
    private List<KeyWordTest> inactiveTestList = new ArrayList<KeyWordTest>( 10 );
    private List<KeyWordTest> functionList = new ArrayList<KeyWordTest>( 10 );
    private PageDataProvider dataProvider;
    private Map<String, List<KeyWordTest>> tagMap = new HashMap<String, List<KeyWordTest>>( 10 );
    private String xFID;

    private String siteName;

    
    
    public String getxFID()
    {
        return xFID;
    }

    public void setxFID( String xFID )
    {
        this.xFID = xFID;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }

    private Map<String, KeyWordTest> testMap = new HashMap<String, KeyWordTest>( 20 );
    private List<ModelContainer> modelList = new ArrayList<ModelContainer>( 20 );

    public boolean testExists( String testName )
    {
        return testMap.get( testName ) != null;
    }

    public Collection<KeyWordTest> getTaggedTests( String[] tagNames )
    {
        Map<String, KeyWordTest> testMap = new HashMap<String, KeyWordTest>( 10 );

        for ( String tagName : tagNames )
        {
            if ( tagMap.containsKey( tagName.toLowerCase() ) )
            {
                for ( KeyWordTest t : tagMap.get( tagName.toLowerCase() ) )
                {
                    testMap.put( t.getName(), t );
                }
            }
        }

        return testMap.values();
    }

    public void addActiveTest( KeyWordTest t )
    {
        for ( String tag : t.getTags() )
        {
            List<KeyWordTest> tagList = tagMap.get( tag.toLowerCase() );
            if ( tagList == null )
            {
                tagList = new ArrayList<KeyWordTest>( 10 );
                tagMap.put( tag.toLowerCase(), tagList );
            }

            tagList.add( t );
        }
        testMap.put( t.getName(), t );
        testList.add( t );
        activeTestList.add( t );
    }

    public void addInactiveTest( KeyWordTest t )
    {
        testMap.put( t.getName(), t );
        testList.add( t );
        inactiveTestList.add( t );
    }

    public void addFunction( KeyWordTest t )
    {
        testMap.put( t.getName(), t );
        functionList.add( t );
    }

    public void addPageModel( String siteName, String pageName, Class pageImpl )
    {
        modelList.add( new ModelContainer( siteName, pageName, pageImpl ) );
    }

}
