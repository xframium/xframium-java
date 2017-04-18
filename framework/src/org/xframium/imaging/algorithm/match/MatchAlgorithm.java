package org.xframium.imaging.algorithm.match;

import org.xframium.imaging.ImageData;

public interface MatchAlgorithm
{
    public double[][] findMatch( ImageData baseImage, ImageData templateImage );
}
