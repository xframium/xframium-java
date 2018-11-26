package org.xframium.utility;

import java.awt.image.BufferedImage;

public class ImageUtility
{
    public static double compareImages( BufferedImage imageOne, BufferedImage imageTwo )
    {        
        int useWidth = imageOne.getWidth() > imageTwo.getWidth() ? imageTwo.getWidth() : imageOne.getWidth();
        int useHeight = imageOne.getHeight() > imageTwo.getHeight() ? imageTwo.getHeight() : imageOne.getHeight();
        
        int maxWidth = imageOne.getWidth() < imageTwo.getWidth() ? imageTwo.getWidth() : imageOne.getWidth();
        int maxHeight = imageOne.getHeight() < imageTwo.getHeight() ? imageTwo.getHeight() : imageOne.getHeight();
        
        int differenceCount = 0;
        
        for( int x=0; x<useWidth; x++ )
        {
            for( int y=0; y<useHeight; y++ )
            {
                if ( imageOne.getRGB( x, y ) != imageTwo.getRGB( x, y ) )
                    differenceCount++;
            }
        }
        
        return differenceCount / ( useWidth * useHeight ) + ( ( maxWidth * maxHeight ) - ( useWidth * useHeight ) );
        
    }
    
}
