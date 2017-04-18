package org.xframium.imaging;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.imageio.ImageIO;
import org.xframium.imaging.algorithm.match.spi.NCCMatchAlgorithm;

public class Perf
{

    
    public static void main( String[] args ) throws Exception
    {
        long startTime = System.currentTimeMillis();
        ImageData baseImage = readImage( new File( "C:\\Users\\Allen\\git\\xframium-java\\framework\\src\\org\\xframium\\imaging\\image.png" ) , true );
        ImageData templateImage = readImage( new File( "C:\\Users\\Allen\\git\\xframium-java\\framework\\src\\org\\xframium\\imaging\\lookup.png" ) , true );
        System.out.println( "read time: " + (System.currentTimeMillis() - startTime) );
        System.out.println( baseImage );
        System.out.println( templateImage );

        NCCMatchAlgorithm x = new NCCMatchAlgorithm();
        
        startTime = System.currentTimeMillis();
        double[][] y = x.findMatch( baseImage, templateImage );
        
        
        
        System.out.println( "process time: " + (System.currentTimeMillis() - startTime) );  
        
        System.out.println( asGrid( y ) );
        
//        templateImage.pad( baseImage.getHeight(), baseImage.getWidth(), 10, 10 );
//        
//        System.out.println( baseImage );
//        System.out.println( templateImage );
//        
//        
//        int[] baseRow = baseImage.getRow( 200 );
//        int[] templateRow = templateImage.getRow( 200 );
//        
//        
//        
//        for ( int i=0; i<baseRow.length; i++ )
//        {
//            System.out.println( baseRow[ i ]  + ", " + templateRow[ i ] );
//        }
//        
//
//        System.out.println( "reasd time: " + (System.currentTimeMillis() - startTime) );

    }
    
    public static String asGrid(double[][] f)
    {
        StringBuilder s = new StringBuilder();
        for ( int y=0; y<f.length; y++ )
        {
            for ( int x=0; x<f[y].length; x++ )
            {
                s.append( "[" ).append( f[y][x] ).append( "]" );
                if ( x<f[y].length - 1)
                    s.append( "," );
            }
            s.append(  "\r\n"  );
        }
        
        return s.toString();
    }
    
    
    public static void pagImages( ImageData baseImage, ImageData templateImage )
    {
        if ( templateImage.getHeight() > baseImage.getHeight() )
            throw new IllegalArgumentException( "Template image is a larger height than the base image" );
        
        if ( templateImage.getWidth() > baseImage.getWidth() )
            throw new IllegalArgumentException( "Template image is a larger width than the base image" );
        
        
        
        
    }
    
    
    public static ImageData readImage( File imageFile, boolean convertToGrayScale ) throws Exception
    {
        BufferedImage baseImage = ImageIO.read( imageFile );

        byte[] pixels = ((DataBufferByte) baseImage.getRaster().getDataBuffer()).getData();
        ImageData imageData = new ImageData( baseImage.getWidth(), baseImage.getHeight() );
        
        boolean hasAlphaChannel = baseImage.getAlphaRaster() != null;


        if ( hasAlphaChannel )
        {
            final int pixelLength = 4;
            int imageIndex = 0;
            for ( int pixel = 0; pixel < pixels.length; pixel += pixelLength )
            {
                int argb = 0;
                int alpha = (((int) pixels[pixel] & 0xff) << 24); // alpha
                int blue = ((int) pixels[pixel + 1] & 0xff); // blue
                int green = (((int) pixels[pixel + 2] & 0xff) << 8); // green
                int red = (((int) pixels[pixel + 3] & 0xff) << 16); // red

                argb += alpha;
                argb += blue;
                argb += green;
                argb += red;
                imageData.getPixelData()[imageIndex++] = convertToGrayScale ? ((int) (0.299 * ((double) red) + 0.587 * ((double) green) + 0.114 * ((double) blue))) : argb;

            }
        }
        else
        {
            final int pixelLength = 3;
            int imageIndex = 0;
            for ( int pixel = 0; pixel < pixels.length; pixel += pixelLength )
            {
                int argb = 0;
                int alpha = -16777216; // 255 alpha
                int blue = ((int) pixels[pixel] & 0xff); // blue
                int green = (((int) pixels[pixel + 1] & 0xff) << 8); // green
                int red = (((int) pixels[pixel + 2] & 0xff) << 16); // red

                argb += alpha;
                argb += blue;
                argb += green;
                argb += red;
                
                imageData.getPixelData()[imageIndex++] = convertToGrayScale ? ((int) (0.299 * ((double) red) + 0.587 * ((double) green) + 0.114 * ((double) blue))) : argb;
            }
        }
        
        return imageData;

    }

}
