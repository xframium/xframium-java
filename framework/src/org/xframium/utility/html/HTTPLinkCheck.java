package org.xframium.utility.html;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.XmlSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class HTTPLinkCheck
{
    private XPathFactory xPathFactory = XPathFactory.newInstance();
    private static int[][] CHAR_LIST = new int[][] { { 0, 9 }, { 11, 13 }, {128, 255 }, {38, 39 } };
    private Map<String, Integer> linkMap = new HashMap<String,Integer>( 10 );
    private Map<String, List<String>> pageMap = new HashMap<String,List<String>>( 10 );
    private List<String> brokenLinks = new ArrayList<String>(20);
    private URL baseUrl;
    
    public List<String> getBrokenLinks()
    {
        return brokenLinks;
    }



    public Map<String,Integer> getLinkMap()
    {
        return linkMap;
    }
    
    public void process( URL currentUrl, URL referencingUrl )
    {
        
        if ( baseUrl == null )
            baseUrl = currentUrl;
        Integer linkCount = linkMap.get( referencingUrl.toString() );
        
        List<String> linkList = pageMap.get( referencingUrl.toString() );
        
        if ( linkList == null )
        {
            linkList = new ArrayList<String>( 20 );
            pageMap.put( referencingUrl.toString(), linkList );
        }
        
        if ( !linkList.contains( currentUrl.toString() ) )
            linkList.add( currentUrl.toString() );
        
        if ( linkCount == null )
            linkCount = linkMap.get( currentUrl.toString() + "/" );
        
        if ( linkCount == null )
        {
            linkMap.put( currentUrl.toString(), 1 );
            
            Document cD = getDocument( currentUrl, referencingUrl );
            if ( cD == null )
                return;
            
            NodeList anchorList = getNodes( cD, "//a[@href]" );
            
            for (  int i=0; i<anchorList.getLength(); i++ )
            {
                
                String hRef = ( (Element)anchorList.item( i ) ).getAttribute( "href" );
                if ( "#".equals( hRef ) )
                    continue;
                
                else if ( hRef.contains( "javascript" ) )
                    continue;
                
                else if ( hRef.startsWith( "http" ) || hRef.startsWith( "//" ) )
                {
                    try
                    {
                        URL useUrl = new URL( hRef.replace( " ", "%20" ) );
                        if ( useUrl.getHost().equals( baseUrl.getHost() ) )
                            process( useUrl, currentUrl );
                        else
                            continue;
                    }
                    catch( Exception e )
                    {
                        continue;
                    }
                }
                else
                {
                    try
                    {
                        URL useUrl = new URL( baseUrl, hRef.replace( " ", "%20" ) );
                        
                        process( useUrl, currentUrl );
                    }
                    catch( Exception e )
                    {
                       continue;
                    }
                }
                
            }
            
        }
        else
            linkMap.put( currentUrl.toString(), 1 + linkCount );
    }

    
    /**
     * Replaces the XML escape characters to XML numeric replacement
     * @param xmlIn - String
     * @return String
     */
    private String escapeXML( String xmlIn )
    {
        String xmlOut = xmlIn;
        for ( int[] currentArray : CHAR_LIST )
        {
           
            for ( int i=currentArray[ 0 ]; i<currentArray[ 1 ]; i++ )
            {
                xmlOut = xmlOut.replace( new String( new byte[] { (byte)i } ), "&#" + i + ";" );
            }
        }
        
        return xmlOut;
    }
    
    private NodeList getNodes( Document xmlDocument, String xPathExpression )
    {
        try
        {
            XPath xPath = xPathFactory.newXPath();
            return ( NodeList ) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODESET );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Converts a given xml to HTML String
     * @param htmlIn - xml in String
     * @return String - in HTML format
     */
    public InputStream toHTML( InputStream htmlIn )
    {
        try
        {
            

            
            byte[] buffer = new byte[ 512 ];
            int bytesRead = 0;
            
            StringBuilder sB = new StringBuilder();
            while ( (bytesRead = htmlIn.read( buffer ) ) != -1 )
            {
                sB.append( new String( buffer, 0, bytesRead ) );
            }
            
            if ( sB.indexOf( "html" ) != -1 )
            {
            
                HtmlCleaner cleaner = new HtmlCleaner();
                cleaner.getProperties().setNamespacesAware( true ); 
                
                XmlSerializer xmlSerializer = new PrettyXmlSerializer( cleaner.getProperties(), "  " );
                String htmlData = xmlSerializer.getAsString( sB.toString() );
                
                htmlData = escapeXML( htmlData.replaceAll("(?m)^[ \t]*\r?\n", "") );
                
                htmlData = htmlData.replace( "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">", "" );
                return new ByteArrayInputStream( htmlData.getBytes() );
            }
            else
                return null;

        }
        catch( Exception e )
        {
            return null;
        }
    }

    
    /**
     * Checks if a given xml is a valid format
     * @param inputDocument - String
     * @return boolean
     */
    private Document getDocument( URL inputUrl, URL referencingUrl )
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            InputStream iS = toHTML( inputUrl.openStream() );
            
            if ( iS != null )
            {
                InputStreamReader streamReader = new InputStreamReader( iS, "UTF-8" );
                InputSource inputSource = new InputSource( streamReader );
                inputSource.setEncoding( "UTF-8" );
    
                return builder.parse( inputSource );
            }
            else
                return null;
        }
        catch( FileNotFoundException t)
        {
            brokenLinks.add( inputUrl.toString() + " <-- " + referencingUrl.toString() );
            System.err.println( "Broken Link: " + inputUrl.toString() + " referenced from " + referencingUrl.toString() );
            return null;
        }
        catch( Exception e )
        {
            return null;
        }
    }
    
    
}
