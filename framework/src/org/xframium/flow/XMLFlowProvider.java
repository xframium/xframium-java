package org.xframium.flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.flow.xsd.ModuleRegistry;
import org.xframium.flow.xsd.ObjectFactory;

public class XMLFlowProvider implements FlowProvider
{
    private Log log = LogFactory.getLog(FlowProvider.class);

    public ModuleRegistry getFlow( File fileName )
    {
        try
        {
            return getFlow( new FileInputStream( fileName ) );
        }
        catch( Exception e )
        {
            log.fatal( "Error reading XML Flow File", e );
        }
        
        return null;
    }
    
    @Override
    public ModuleRegistry getFlow( String fileName )
    {
        return getFlow( getClass().getResourceAsStream( fileName ) );
    }
    
    public ModuleRegistry getFlow( InputStream inputStream )
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>) u.unmarshal( inputStream );

            return (ModuleRegistry) rootElement.getValue();
        }
        catch ( Exception e )
        {
            log.fatal( "Error reading XML Flow File", e );
        }
        finally
        {
            try { inputStream.close(); } catch( Exception e ) {}
        }
        
        return null;
    }
}
