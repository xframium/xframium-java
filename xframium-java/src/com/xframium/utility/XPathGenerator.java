package com.xframium.utility;

import org.w3c.dom.Node;

public class XPathGenerator
{

    public static String genrateXpath( Node currentNode )
    {
        StringBuilder xpath = new StringBuilder();
        generateXpath( xpath, currentNode );
        return xpath.toString();

    }
    
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
}
