package org.xframium.device.cloud.action;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    
}
