package org.xframium.imaging;

public class ImageData
{
    private int height;
    private int width;
    private int[] pixelData;
    private int size;

    private int paddedHeight = 0;
    private int paddedWidth = 0;
    private int offsetX = 0;
    private int offsetY = 0;

    public ImageData( int width, int height )
    {
        this.width = width;
        this.height = height;
        this.paddedHeight = height;
        this.paddedWidth = width;
        pixelData = new int[width * height];
        size = pixelData.length;
    }

    public void setPaddedHeight( int paddedHeight )
    {
        this.paddedHeight = paddedHeight;
        size = paddedHeight * paddedWidth;
    }

    public void setPaddedWidth( int paddedWidth )
    {
        this.paddedWidth = paddedWidth;
        size = paddedHeight * paddedWidth;
    }
    
    

    public int getSize()
    {
        return size;
    }

    public int getOffsetX()
    {
        return offsetX;
    }

    public void setOffsetX( int offsetX )
    {
        this.offsetX = offsetX;
    }

    public int getOffsetY()
    {
        return offsetY;
    }

    public void setOffsetY( int offsetY )
    {
        this.offsetY = offsetY;
    }

    public int getHeight()
    {
        return paddedHeight;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }

    public int getWidth()
    {
        return paddedWidth;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }

    public int[] getPixelData()
    {
        return pixelData;
    }

    public void setPixelData( int[] pixelData )
    {
        this.pixelData = pixelData;
    }

    public int[] getRow( int rowNumber )
    {
        int[] rowData = new int[paddedWidth];

        for ( int x=0; x<paddedWidth; x++ )
            rowData[ x ] = getPixel( x, rowNumber );


        return rowData;
    }

    public void pad( int height, int width, int offsetX, int offsetY )
    {
        this.paddedHeight = height;
        this.paddedWidth = width;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public String toString()
    {
        return width + ", " + height + " (" + pixelData.length + ")";
    }
    
    public String asGrid()
    {
        StringBuilder s = new StringBuilder();
        for ( int y=0; y<getHeight(); y++ )
        {
            for ( int x=0; x<getWidth(); x++ )
            {
                s.append( "[" ).append( getPixel( x, y ) ).append( "]" );
                if ( x<getWidth() - 1)
                    s.append( "," );
            }
            s.append(  "\r\n"  );
        }
        
        return s.toString();
    }

    public int getPixel( int x, int y )
    {
        if ( paddedHeight == height && paddedWidth == width )
        {
            // no transformations
            return pixelData[(width * y) + x];
        }
        else
        {
            if ( x < offsetX || y < offsetY )
            {
                return 0;
            }
            else if ( x >= (offsetX + width) || (y >= (offsetY + height)) )
            {
                return 0;
            }
            else
            {
                return pixelData[(width * (y - offsetY)) + (x - offsetX)];
            }
        }
    }

    public static void main( String[] args )
    {
        ImageData x = new ImageData( 3, 3 );

        x.setPixelData( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 } );
        System.out.println( x.asGrid() );

        System.out.println( x.getPixel( 2, 1 ) );
        
        ImageData y = new ImageData( 3, 3 );

        y.setPixelData( new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 } );
        y.setOffsetX( 1 );
        y.setOffsetY( 1 );
        y.setPaddedHeight( 5 );
        y.setPaddedWidth( 5 );
        System.out.println( y.asGrid() );
        

        System.out.println( y.getPixel( 2, 1 ) );
    }
}
