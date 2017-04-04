package org.xframium.artifact;

import java.io.File;
import org.xframium.device.factory.DeviceWebDriver;

public interface Artifact
{
    public static final String URL  = "ARTIFACT_URL";
    File generateArtifact( String rootFolder, DeviceWebDriver webDriver );
}
