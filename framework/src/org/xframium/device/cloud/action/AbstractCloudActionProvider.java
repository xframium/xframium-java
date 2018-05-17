package org.xframium.device.cloud.action;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xframium.device.factory.DeviceWebDriver;

public abstract class AbstractCloudActionProvider implements CloudActionProvider
{
    private static XPathFactory xPathFactory = XPathFactory.newInstance();
    protected static DateFormat dateFormat = new SimpleDateFormat( "MM-dd_HH-mm-ss");
    protected Log log = LogFactory.getLog( CloudActionProvider.class );
    
    protected NodeList getNodes( Document xmlDocument, String xPathExpression )
    {
        try
        {
            if (log.isDebugEnabled())
                log.debug( "Attempting to return Nodes for [" + xPathExpression + "]" );

            XPath xPath = xPathFactory.newXPath();
            return ( NodeList ) xPath.evaluate( xPathExpression, xmlDocument, XPathConstants.NODESET );
        }
        catch (Exception e)
        {
            log.error( "Error parsing xPath Expression [" + xPathExpression + "]" );
            return null;
        }
    }
    
    public InputStream getReport( DeviceWebDriver webDriver, String reportType )
    {
        return null;
    }
    
    @Override
    public String getVitals( DeviceWebDriver webDriver )
    {
        return null;
    }
    
    @Override
    public Rectangle findText( DeviceWebDriver webDriver, String text, Map<String, String> propertyMap )
    {
        return null;
    }
    
    @Override
    public Rectangle findImage( DeviceWebDriver webDriver, String imageName, Map<String, String> propertyMap )
    {
        return null;
    }
    
    @Override
    public Dimension translateDimension(DeviceWebDriver webDriver, Dimension currentDimension) {
    	Point p = new Point( currentDimension.width, currentDimension.height );
    	p = translatePoint(webDriver, p);
    	return new Dimension( p.x, p.y );
    }
    
}
