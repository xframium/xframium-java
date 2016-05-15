package com.xframium.artifact;

import java.util.ArrayList;
import java.util.List;

public class ArtifactManager
{
    /** The singleton. */
    private static ArtifactManager singleton = new ArtifactManager();

    /**
     * Instance.
     *
     * @return the RunDetails
     */
    public static ArtifactManager instance()
    {
        return singleton;
    }

    /**
     * Instantiates a RunDetails.
     */
    private ArtifactManager()
    {

    }
    
    private List<ArtifactListener> artifactListeners = new ArrayList<ArtifactListener>( 10 );
    
    public void addArtifactListener( ArtifactListener aListener )
    {
        artifactListeners.add( aListener );
    }
    
    public void notifyArtifactListeners( ArtifactType aType, Object aRecord )
    {
        for ( ArtifactListener aListener : artifactListeners )
            aListener.addArtifactRecord( aType, aRecord );
    }
    
}
