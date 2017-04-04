package org.xframium.artifact.spi;

import java.io.File;
import org.xframium.artifact.AbstractArtifact;
import org.xframium.artifact.ArtifactType;
import org.xframium.device.factory.DeviceWebDriver;

public class HTMLSourceArtifact extends AbstractArtifact
{
    private static final String FILE_NAME = "failureDOM.html";
    public HTMLSourceArtifact()
    {
        setArtifactType( ArtifactType.FAILURE_SOURCE_HTML.name() );
    }
    
    @Override
    protected File _generateArtifact( File rootFolder, DeviceWebDriver webDriver )
    {
        return writeToDisk( rootFolder, FILE_NAME, ("<html><head><link href=\"http://www.xframium.org/output/assets/css/prism.css\" rel=\"stylesheet\"><script src=\"http://www.xframium.org/output/assets/js/prism.js\"></script><body><pre class\"line-numbers\"><code class=\"language-markup\">" + webDriver.getPageSource().replace( "<", "&lt;" ).replace( ">", "&gt;" ).replace( "\t", "  " ) + "</code></pre></body></html>").getBytes() );
    }
}
