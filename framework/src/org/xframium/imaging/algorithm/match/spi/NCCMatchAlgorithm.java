package org.xframium.imaging.algorithm.match.spi;

import org.xframium.imaging.ImageData;
import org.xframium.imaging.algorithm.match.MatchAlgorithm;

public class NCCMatchAlgorithm implements MatchAlgorithm
{
    @Override
    public double[][] findMatch( ImageData baseImage, ImageData templateImage )
    {
        ImageData tI = new ImageData( templateImage.getWidth(), templateImage.getHeight() );
        tI.setPixelData( templateImage.getPixelData() );
        tI.setPaddedHeight( baseImage.getHeight() );
        tI.setPaddedWidth( baseImage.getWidth() );

        double[][] returnValue = new double[baseImage.getWidth() - templateImage.getWidth() ][baseImage.getHeight() - templateImage.getHeight()];
        
        for ( int x = 0; x < baseImage.getWidth() - templateImage.getWidth(); x++ )
        {
            for ( int y = 0; y < baseImage.getHeight() - templateImage.getHeight(); y++ )
            {
                tI.setOffsetX( x );
                tI.setOffsetY( y );
                returnValue[x][y] = ncc( baseImage, tI);
                
                System.out.println( x + ", " + y + " = " + returnValue[x][y] );
            }
        }
        
        return returnValue;

    }

    private double ncc( ImageData baseImage, ImageData templateImage )
    {

        double ncc = -1;
        double mean0 = 0;
        double mean1 = 0;
        double std0 = 0;
        double std1 = 0;

        for ( int x = 0; x < baseImage.getWidth(); x++ )
        {
            for ( int y = 0; y < baseImage.getHeight(); y++ )
            {
                mean0 += baseImage.getPixel( x, y );
                mean1 += templateImage.getPixel( x, y );
            }
        }

        mean0 /= baseImage.getSize();
        mean1 /= baseImage.getSize();

        for ( int x = 0; x < baseImage.getWidth(); x++ )
        {
            for ( int y = 0; y < baseImage.getHeight(); y++ )
            {

                std0 += Math.pow( baseImage.getPixel( x, y ) - mean0, 2 );
                std1 += Math.pow( templateImage.getPixel( x, y ) - mean1, 2 );
            }
        }

        std0 = Math.sqrt( std0 / baseImage.getSize() );
        std1 = Math.sqrt( std1 / templateImage.getSize() );

        ncc = 0;
        for ( int x = 0; x < baseImage.getWidth(); x++ )
        {
            for ( int y = 0; y < baseImage.getHeight(); y++ )
            {
                ncc += ((baseImage.getPixel( x, y ) - mean0) * (templateImage.getPixel( x, y ) - mean1)) / (std0 * std1);
            }
        }
        

        ncc /= baseImage.getSize() - 1;

    return ncc;
}

}
