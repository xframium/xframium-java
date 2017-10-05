/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.io.FileReader;
import org.w3c.dom.Node;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import org.xframium.utility.parser.xpath.*;

public class XPathGenerator
{
    //
    // Class Data
    //
    
    private static final String COMMA = ",";
    private static final String AMP = "&";
    private static final String EQUALS = "=";
    private static final String AT = "@";
    private static final String COLON = ":";
    private static final String RESOURCE_ID = "resource-id";

    public static final MobileOS IOS = new MobileOS();
    public static final MobileOS ANDROID = new MobileOS();
    private static final String EXTERNAL_SUBSTITUTIONS_PARAM = "xframium.xpath.substitutions";
    private static final String INTERNAL_SUBSTITUTIONS_PARAM = "xpath.substitution.properties";
    private static Properties substitutions = null;

    private static String[][] XCUI_SUBSTITUTION = new String[][] { 
        {"UIApplication", "XCUIElementTypeApplication"},
        {"UIAActionSheet ", "XCUIElementTypeSheet"},
        {"UIAActivityIndicator ", "XCUIElementTypeActivityIndicator"},
        {"UIAAlert ", "XCUIElementTypeAlert"},
        {"UIAButton ", "XCUIElementTypeButton"},
        {"UIACollectionCell", "XCUIElementTypeCell"},
        {"UIACollectionView", "XCUIElementTypeCollectionView"},
        {"UIAEditingMenu", "XCUIElementTypeMenu"},
        {"UIAElement ", "XCUIElementTypeAny"},
        {"UIAImage ", "XCUIElementTypeImage"},
        {"UIAKey ", "XCUIElementTypeKey"},
        {"UIAKeyboard ", "XCUIElementTypeKeyboard"},
        {"UIALink ", "XCUIElementTypeLink"},
        {"UIAMapView", "XCUIElementTypeMap"},
        {"UIANavigationBar", "XCUIElementTypeNavigationBar"},
        {"UIAPageIndicator ", "XCUIElementTypePageIndicator"},
        {"UIAPicker ", "XCUIElementTypePicker"},
        {"UIAPickerWheel ", "XCUIElementTypePickerWheel"},
        {"UIAPopover ", "XCUIElementTypePopover"},
        {"UIAProgressIndicator ", "XCUIElementTypeProgressIndicator"},
        {"UIAScrollView ", "XCUIElementTypeScrollView"},
        {"UIASearchBar ", "XCUIElementTypeSearchField"},
        {"UIASecureTextField ", "XCUIElementTypeSecureTextField"},
        {"UIASegmentedControl ", "XCUIElementTypeSegmentedControl"},
        {"UIASlider ", "XCUIElementTypeSlider"},
        {"UIAStaticText ", "XCUIElementTypeStaticText"},
        {"UIAStatusBar ", "XCUIElementTypeStatusBar"},
        {"UIASwitch ", "XCUIElementTypeSwitch"},
        {"UIATabBar ", "XCUIElementTypeTabBar"},
        {"UIATableCell", "XCUIElementTypeTableColumn"},
        {"UIATableGroup ", "XCUIElementTypeOther"},
        {"UIATableView ", "XCUIElementTypeTable"},
        {"UIATextField ", "XCUIElementTypeTextField"},
        {"UIATextView ", "XCUIElementTypeTextView"},
        {"UIAToolbar ", "XCUIElementTypeToolbar"},
        {"UIAWebView ", "XCUIElementTypeWebView"},
        {"UIAWindow ", "XCUIElementTypeWindow"} 
    };
    
    
    public static String XCUIConvert( String xpath )
    {
        String returnValue = xpath;
        for ( String[] conversion : XCUI_SUBSTITUTION )
        {
            returnValue = returnValue.replace( conversion[ 0 ],  conversion[ 1 ] );
        }
        
        return returnValue;
    }
    
    /**
     * Generate an XPath expression from properties.
     *
     * @param propertyMap - the properties to build from
     * @param propertyDefinition - the property definition
     * @return the xpath expression
     */
    public static String generateXPathFromProperty( Map<String,String> propertyMap, String propertyDefinition )
    {
        StringBuilder xpathBuilder = new StringBuilder();
        String[] ors = propertyDefinition.split( COMMA );
        
        for ( String myOr : ors )
        {
            xpathBuilder.append( "//*" );
            String[] ands = myOr.split( AMP );
            
            for ( int i=0; i<ands.length; i++ )
            {
                String[] nameValue = ands[ i ].split( EQUALS ); 
                
                xpathBuilder.append( "[" ).append( AT ).append( nameValue[ 0 ] ).append( EQUALS ).append( "'" );
                if ( RESOURCE_ID.equals( nameValue[ 0 ].toLowerCase() ) )
                {
                    if ( propertyMap.get( RESOURCE_ID ) != null && !nameValue[ 1 ].contains( COLON ) )
                        xpathBuilder.append( propertyMap.get( RESOURCE_ID ) ).append( ":id/" ).append(  nameValue[ 1 ] ).append( "'" );
                    else
                        xpathBuilder.append( nameValue[ 1 ] ).append( "'" );
                }
                else
                    xpathBuilder.append( nameValue[ 1 ] ).append( "'" );
                
                xpathBuilder.append( "]" );
            }
            
            xpathBuilder.append( "|" );
        }
        
        return xpathBuilder.substring( 0, xpathBuilder.length() - 1 );
        
    }

    /**
     * Generate an XPath expression ftom a document node
     * 
     * @param currentNode - the node to generate the expression from
     * @return the xpath expression
     */
    public static String genrateXpath( Node currentNode )
    {
        StringBuilder xpath = new StringBuilder();
        generateXpath( xpath, currentNode );

        return xpath.toString();
    }

    /**
     * Generate an Appium style XPath expression from a PerfectoMobile style input
     *
     * @param os - the mobile operating system of interest
     * @param xpath - the input XPath
     * @return the converted XPath expression
     */
    public static String convertXPath( MobileOS os, String xpath )
    {
        boolean isIOS = IOS.equals( os );

        ArrayList elementNames = new ArrayList();
        ArrayList attributeNames = new ArrayList();
        parseXPath( xpath, elementNames, attributeNames );

        //
        // turns out 'text' is Xpath keyword in addition to
        // a perfecto element name.  The parser doesn't find it as
        // a result, so we'll ust add it in
        //

        elementNames.add( "text" );

        Properties substitutions = loadSubstitutions();

        Iterator elements = elementNames.iterator();
        while( elements.hasNext() )
        {
            String element = (String) elements.next();
            String substitution = substitutions.getProperty( ((isIOS) ? "ios" : "android" ) +
                                                             "." + "element" +
                                                             "." + element );
            if ( substitution != null )
            {
                xpath = replaceAll( xpath, element, substitution );
            }
        }

        Iterator attrs = attributeNames.iterator();
        while( attrs.hasNext() )
        {
            String attr = (String) attrs.next();
            String substitution = substitutions.getProperty( ((isIOS) ? "ios" : "android" ) +
                                                             "." + "attribute" +
                                                             "." + attr );
            if ( substitution != null )
            {
                xpath = xpath.replaceAll( "@" + attr, "@" + substitution );
            }
        }

        return xpath;
    }



    //
    // Helpers
    //
    
    private static void generateXpath( StringBuilder xpath, Node currentNode )
    {
        if ( currentNode.getParentNode() != null )
        {
            currentNode.getParentNode().getChildNodes();
            int currentIndex = 0;
            for ( int i = 0; i < currentNode.getParentNode().getChildNodes().getLength(); i++ )
            {
                if ( currentNode.getParentNode().getChildNodes().item( i ).getNodeType() == Node.ELEMENT_NODE )
                {
                    currentIndex++;
                    if ( currentNode == currentNode.getParentNode().getChildNodes().item( i ) )
                    {
                        xpath.insert( 0, "/*[" + (currentIndex) + "]" );
                        generateXpath( xpath, currentNode.getParentNode() );
                        
                    }
                }
            }
        }
    }

    public static class MobileOS
    {
        private MobileOS()
        {}
    }

    private static void parseXPath( String xpath, List elementNames, List attribureNames )
    {
        XPathLexer lexer = new XPathLexer(new ANTLRInputStream( xpath ));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        
        // Pass the tokens to the parser
        XPathParser parser = new XPathParser(tokens);
        
        // Specify our entry point
        XPathParser.MainContext ctx = parser.main();
        
        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        MyXPathListener listener = new MyXPathListener( elementNames, attribureNames );
        walker.walk(listener, ctx);
    }

    private static class MyXPathListener
        extends XPathBaseListener
    {
        private boolean inPred = false;
        private List elements = null;
        private List attrs = null;

        public MyXPathListener( List elements, List attrs )
        {
            this.elements = elements;
            this.attrs = attrs;
        }

        public void enterPredicate(XPathParser.PredicateContext ctx) { inPred = true; }
        public void enterNCName(XPathParser.NCNameContext ctx) { if ( !inPred ) elements.add(ctx.getText()); else attrs.add(ctx.getText()); }

        public void exitPredicate(XPathParser.PredicateContext ctx) { inPred = false; }
    }

    private static Properties loadSubstitutions()
    {
        if ( substitutions == null )
        {
            substitutions = new Properties();

            try
            {
                String externalSubsPath = System.getProperty( EXTERNAL_SUBSTITUTIONS_PARAM );

                if ( externalSubsPath != null )
                {
                    try
                    {
                        substitutions.load( new FileReader( externalSubsPath ));
                    }
                    catch( Throwable e )
                    {
                        e.printStackTrace();
                    }
                }

                if ( substitutions.size() == 0 )
                {
                    substitutions.load( XPathGenerator.class.getResourceAsStream( INTERNAL_SUBSTITUTIONS_PARAM ));
                }
            }
            catch( Throwable e )
            {
                e.printStackTrace();
            }
        }
            
        return substitutions;
    }

    private static String replaceAll( String xpath, String element, String substitution )
    {
        //
        // The problem is that a perfecto element (ex. text) can also be a function name (ex. text()) or an attribute (ex. @text).  So,
        // we'll hide functions and attributes with this element's name before replacing the element name.
        //

        xpath = xpath.replaceAll( element + "\\(\\)", "zzzzzz" );
        xpath = xpath.replaceAll( "\\@" + element, "yyyyyy" );

        xpath = xpath.replaceAll( element, substitution );

        xpath = xpath.replaceAll( "zzzzzz", element + "\\(\\)" );
        xpath = xpath.replaceAll( "yyyyyy", "\\@" + element );

        return xpath;
    }
}
