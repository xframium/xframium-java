package org.xframium.imaging.ocv;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bytedeco.javacpp.opencv_core.Mat;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenCVManager.
 */
public class OpenCVManager
{
	
	/** The singleton. */
	private static OpenCVManager singleton = new OpenCVManager();

	/**
	 * Instance.
	 *
	 * @return the open cv manager
	 */
	public static OpenCVManager instance()
	{
		return singleton;
	}

	/**
	 * Instantiates a new open cv manager.
	 */
	private OpenCVManager()
	{

	}

	/** The library location. */
	private String libraryLocation;
	
	/** The cache folder. */
	private String cacheFolder;
	
	/** The match algorithms. */
	private int[] matchAlgorithms;

	/** The log. */
	private Log log = LogFactory.getLog( OpenCVManager.class );

	/**
	 * Initialize cv.
	 *
	 * @param libraryLocation the library location
	 * @param cacheFolder the cache folder
	 * @param templateMatchAlgorithms the template match algorithms
	 */
	public void initializeCV( String libraryLocation, String cacheFolder, String templateMatchAlgorithms )
	{
		if (log.isInfoEnabled())
			log.info( "Loading OpenCV Library from " + libraryLocation );
		System.load( libraryLocation );

		this.libraryLocation = libraryLocation;
		this.cacheFolder = cacheFolder;
		String[] matchString = templateMatchAlgorithms.split( "," );
		this.matchAlgorithms = new int[ matchString.length ];
		for ( int i=0; i<matchString.length; i++ )
			matchAlgorithms[ i ] = Integer.parseInt( matchString[ i ].trim() );
		
		

	}
	
	/**
	 * Gets the library location.
	 *
	 * @return the library location
	 */
	public String getLibraryLocation()
	{
		return libraryLocation;
	}

	/**
	 * Gets the cache folder.
	 *
	 * @return the cache folder
	 */
	public String getCacheFolder()
	{
		return cacheFolder;
	}

	/**
	 * Gets the match algorithms.
	 *
	 * @return the match algorithms
	 */
	public int[] getMatchAlgorithms()
	{
		return matchAlgorithms;
	}

	/**
	 * Match images.
	 *
	 * @param originalImage the original image
	 * @param templateImage the template image
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public double matchImages( InputStream originalImage, InputStream templateImage ) throws IOException
	{
		return matchImages( ImageIO.read( originalImage ), ImageIO.read(  templateImage ) );

	}

	/**
	 * Match images.
	 *
	 * @param originalImage the original image
	 * @param templateImage the template image
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public double matchImages( BufferedImage originalImage, BufferedImage templateImage ) throws IOException
	{
//		Mat oImage = new Mat( originalImage.getWidth(), originalImage.getHeight(), CvType.CV_8UC3 );
//		//oImage.put( 0, 0, ( ( DataBufferByte ) originalImage.getRaster().getDataBuffer() ).getData() );
//
//		Mat tImage = new Mat( templateImage.getWidth(), templateImage.getHeight(), CvType.CV_8UC3 );
//		//tImage.put( 0, 0, ( ( DataBufferByte ) templateImage.getRaster().getDataBuffer() ).getData() );
//
//		double[] matchScores = new double[ matchAlgorithms.length ];
//		double lowMatch = Double.MAX_VALUE;
//		for ( int i=0; i<matchScores.length; i++ )
//		{
//			double currentValue = matchImages( oImage, tImage, matchAlgorithms[ i ] );
//			if ( currentValue < lowMatch )
//				lowMatch = currentValue;
//		}
//		
//		
//		return lowMatch;
	    return 0;
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main( String[] args ) throws Exception
	{
		OpenCVManager.instance().initializeCV( "c:/tools/opencv_java248.dll", "", "1, 3, 5" );
		System.out.println( OpenCVManager.instance().matchImages( new FileInputStream( "c:/tools/screen.png" ), new FileInputStream( "c:/tools/logo.png" ) ) );	
	}

	/**
	 * Match images.
	 *
	 * @param originalImage the original image
	 * @param imageTemplate the image template
	 * @param matchMethod the match method
	 * @return the double
	 */
	private double matchImages( Mat originalImage, Mat imageTemplate, int matchMethod )
	{

//		int resultCols = originalImage.cols() - imageTemplate.cols() + 1;
//		int resultRows = originalImage.rows() - imageTemplate.rows() + 1;
//		Mat result = new Mat( resultRows, resultCols, CvType.CV_32FC1 );
//
//		//Imgproc.matchTemplate( originalImage, imageTemplate, result, matchMethod );
////
//		//MinMaxLocResult mmr = Core.minMaxLoc( result );
//
//		Point matchLoc;
//		double matchScore;
//		if (matchMethod == Imgproc.TM_SQDIFF || matchMethod == Imgproc.TM_SQDIFF_NORMED)
//		{
//			matchLoc = mmr.minLoc;
//			matchScore = 1.0 - mmr.minVal;
//		}
//		else
//		{
//			matchLoc = mmr.maxLoc;
//			matchScore = mmr.maxVal;
//		}
//		
//		if ( log.isDebugEnabled() )
//			log.debug( "Match Score: " + matchScore + " found at " + matchLoc + " using method " + matchMethod );
//
//		return matchScore;
	    
	    return 0;
	}

}
