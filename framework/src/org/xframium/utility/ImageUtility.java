package org.xframium.utility;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtility
{
    public static double compareImages( BufferedImage imageOne, BufferedImage imageTwo )
    {
        Dimension dimOne = new Dimension( imageOne.getWidth(), imageOne.getHeight() );
        Dimension dimTwo = new Dimension( imageTwo.getWidth(), imageTwo.getHeight() );
        
        int differenceCount = 0;
        
        System.out.println( dimOne.getWidth() + ", " + dimOne.getHeight() );
        
        if ( dimOne.getWidth() != dimTwo.getWidth() || dimOne.getHeight() != dimTwo.getHeight() )
            return 0;
        else
        {
            for( int x=0; x<dimOne.getWidth(); x++ )
            {
                for( int y=0; y<dimOne.getHeight(); y++ )
                {
                    if ( imageOne.getRGB( x, y ) != imageTwo.getRGB( x, y ) )
                        differenceCount++;
                }
            }
        }
        
        return differenceCount / ( dimOne.getWidth() * dimOne.getHeight() );
        
    }

    public static void main( String[] args ) throws Exception
    {
        BufferedImage one = ImageIO.read( new File( "C:\\Users\\Allen\\git\\xframium-java\\testing\\test-output\\historicalComparison\\Firefox Local\\afterClick-0000.png" ) );
        BufferedImage two = ImageIO.read( new File( "C:\\Users\\Allen\\git\\xframium-java\\testing\\test-output\\historicalComparison\\Firefox Local\\afterClick-0001.png" ) );
        
        System.out.println( compareImages( one, two ) );
        
    }
    
}
