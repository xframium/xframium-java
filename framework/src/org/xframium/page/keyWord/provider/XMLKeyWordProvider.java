/*******************************************************************************
 * xFramium
 *
 * Copyright 2016 by Moreland Labs, Ltd. (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.page.keyWord.provider;

import gherkin.parser.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.container.SuiteContainer;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.keyWord.*;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordStep.ValidationType;
import org.xframium.page.keyWord.KeyWordToken.TokenType;
import org.xframium.page.keyWord.gherkinExtension.XMLFormatter;
import org.xframium.page.keyWord.provider.xsd.*;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLKeyWordProvider.
 */
public class XMLKeyWordProvider implements KeyWordProvider
{
	

	/** The log. */
	private Log log = LogFactory.getLog( KeyWordTest.class );
	
	/** The file name. */
	private File fileName;
	private File rootFolder;
	
	/** The resource name. */
	private String resourceName;
	private byte[] resourceData;

	/**
	 * Instantiates a new XML key word provider.
	 *
	 * @param fileName
	 *            the file name
	 */
	public XMLKeyWordProvider( File fileName )
	{
		this.fileName = fileName;
		rootFolder = fileName.getParentFile();
	}

	/**
	 * Instantiates a new XML key word provider.
	 *
	 * @param resourceName
	 *            the resource name
	 */
	public XMLKeyWordProvider( String resourceName )
	{
		this.resourceName = resourceName;
	}
	
	public XMLKeyWordProvider( byte[] resourceData )
    {
        this.resourceData = resourceData;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.perfectoMobile.page.keyWord.provider.KeyWordProvider#readData()
	 */
	public SuiteContainer readData( boolean parseDataIterators )
	{
	    SuiteContainer sC = new SuiteContainer();
	    
	    if ( resourceData != null )
	    {
	        readElements( sC, new ByteArrayInputStream( resourceData ), true, true, parseDataIterators );
	    }
	    else if (fileName == null)
		{
			if (log.isInfoEnabled())
				log.info( "Reading from CLASSPATH as XMLElementProvider.elementFile" );
			
			readElements( sC, getClass().getClassLoader().getResourceAsStream( resourceName ), true, true, parseDataIterators );
		}
		else
		{
			try
			{
				if (log.isInfoEnabled())
					log.info( "Reading from FILE SYSTEM as [" + fileName + "]" );
				readElements( sC, new FileInputStream( fileName ), true, true, parseDataIterators );
			}
			catch (Exception e)
			{
				log.fatal( "Could not read from " + fileName, e );
				System.exit( -1 );
			}
		}
		
		return sC;
	}

	/**
	 * Read elements.
	 *
	 * @param inputStream the input stream
	 * @param readTests the read tests
	 * @param readFunctions the read functions
	 */
	private void readElements( SuiteContainer sC, InputStream inputStream, boolean readTests, boolean readFunctions, boolean parseDataIterators )
	{

		try
		{
		    JAXBContext jc = JAXBContext.newInstance( ObjectFactory.class );
            Unmarshaller u = jc.createUnmarshaller();
            JAXBElement<?> rootElement = (JAXBElement<?>)u.unmarshal( inputStream );
            
            RegistryRoot rRoot = (RegistryRoot)rootElement.getValue();

			parseModel( rRoot.getModel(), sC );
			parseImports( rRoot.getImport(), sC,parseDataIterators );
			
			if ( readTests )
			{
			    for( Test test : rRoot.getTest() )
			    {
			        if ( sC.testExists( test.getName() ) )
                    {
                        log.warn( "The test [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
			        
			        KeyWordTest currentTest = parseTest( test, "test" );
			        
			        if (currentTest.getDataDriver() != null && !currentTest.getDataDriver().isEmpty() && parseDataIterators)
                    {
			            PageData[] pageData = null;
			            try
			            {
			                pageData = PageDataManager.instance().getRecords( currentTest.getDataDriver() );
			            }
			            catch( Exception e )
			            {
			                
			            }
                        if (pageData == null)
                        {
                            if ( currentTest.isActive() )
                                sC.addActiveTest( currentTest );
                            else
                                sC.addInactiveTest( currentTest );
                        }
                        else
                        {
                            String testName = currentTest.getName();

                            for (PageData record : pageData)
                            {
                                if ( currentTest.isActive() )
                                    sC.addActiveTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                                else
                                    sC.addInactiveTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                            }
                        }
                    }
                    else
                    {
                        if ( currentTest.isActive() )
                            sC.addActiveTest( currentTest );
                        else
                            sC.addInactiveTest( currentTest );
                    }
			        
			    }
			}

			if (readFunctions)
			{
			    for( Test test : rRoot.getFunction() )
                {
                    if ( sC.testExists( test.getName() ) )
                    {
                        log.warn( "The function [" + test.getName() + "] is already defined and will not be added again" );
                        continue;
                    }
                    sC.addFunction( parseTest( test, "function" ) );
                }
			}

		}
		catch( IllegalStateException e )
		{
			throw e;
		}
		catch (Exception e)
		{
			log.fatal( "Error reading XML Element File", e );
		}
	}

	private File findFile( File useFile )
    {
        if ( useFile.exists() || useFile.isAbsolute() )
            return useFile;
        
        File myFile = new File( rootFolder, useFile.getPath() );
        if ( myFile.exists() )
            return myFile;
        
        throw new IllegalArgumentException( "Could not find " + useFile.getName() + " at " + useFile.getPath() + " or " + myFile.getAbsolutePath() );
        
    }
	
	/**
	 * Parses the imports.
	 *
	 * @param importList the import list
	 */
	private void parseImports( List<Import> importList, SuiteContainer sC, boolean parseDataIterators )
	{
	    for ( Import imp : importList )
	    {
	        try
	        {
	            if (log.isInfoEnabled())
                    log.info( "Attempting to import file [" + Paths.get(".").toAbsolutePath().normalize().toString() + imp.getFileName() + "]" );
	            if ( imp.getFileName().toLowerCase().endsWith( ".xml" ) )
	            {
	                InputStream inputStream = ClassLoader.getSystemResourceAsStream( imp.getFileName() );
	                if ( inputStream == null )
	                    readElements( sC, new FileInputStream( findFile( new File( imp.getFileName() ) ) ), imp.isIncludeTests(), imp.isIncludeFunctions(), parseDataIterators );
	                else
	                    readElements( sC, inputStream, imp.isIncludeTests(), imp.isIncludeFunctions(), parseDataIterators );
	            }
	            else if ( imp.getFileName().toLowerCase().endsWith( ".bdd" ) )
	            {
	                Parser bddParser = new Parser( new XMLFormatter( PageDataManager.instance().getDataProvider() ) );
	                
	                byte[] buffer = new byte[512];
	                int bytesRead = 0;
	                
	                FileInputStream is = new FileInputStream( findFile( new File( imp.getFileName() ) ) );
	                StringBuilder s = new StringBuilder();
	                
	                while ( ( bytesRead = is.read( buffer ) ) > 0 )
	                {
	                    s.append( new String( buffer, 0, bytesRead ) );
	                }
	                
	                is.close();
	                
	                bddParser.parse( s.toString(), "", 0 );
	            }
	        }
	        catch( Exception e )
	        {
	            log.fatal( "Could not read from " + imp.getFileName(), e );
                throw new IllegalStateException( e );
	        }
	    }
	}

	/**
	 * Parses the model.
	 *
	 * @param model the model
	 */
	private void parseModel( Model model, SuiteContainer sC )
	{
	    
	    if ( model != null )
	    {
    	    for ( org.xframium.page.keyWord.provider.xsd.Page page : model.getPage() )
    	    {
    	        try
    	        {
        	        Class useClass = KeyWordPage.class;
        	        if ( page.getClazz() != null && !page.getClazz().isEmpty() )
        	            useClass = ( Class<Page> ) Class.forName( page.getClazz() );
        	        
        	        if (log.isDebugEnabled())
                        log.debug( "Creating page as " + useClass.getSimpleName() + " for " + page.getName() );
        
                    sC.addPageModel( page.getSite() != null && page.getSite().trim().length() > 0 ? page.getSite() : sC.getSiteName(), page.getName(), useClass );
    	        }
    	        catch( Exception e )
    	        {
    	            log.error( "Error creating instance of [" + page.getClazz() + "]" );
    	        }
    	    }
	    }
	}

	/**
	 * Parses the test.
	 *
	 * @param xTest the x test
	 * @param typeName the type name
	 * @return the key word test
	 */
	private KeyWordTest parseTest( Test xTest, String typeName )
	{
        KeyWordTest test = new KeyWordTest( xTest.getName(), xTest.isActive(), xTest.getDataProvider(), xTest.getDataDriver(), xTest.isTimed(), xTest.getLinkId(), xTest.getOs(), xTest.getThreshold().intValue(), xTest.getDescription() != null ? xTest.getDescription().getValue() : null, xTest.getTagNames(), xTest.getContentKeys(), xTest.getDeviceTags() );
		
        
		KeyWordStep[] steps = parseSteps( xTest.getStep(), xTest.getName(), typeName );

		for (KeyWordStep step : steps)
		{
			test.addStep( step );
		}

		return test;
	}

	/**
	 * Parses the steps.
	 *
	 * @param xSteps the x steps
	 * @param testName the test name
	 * @param typeName the type name
	 * @return the key word step[]
	 */
	private KeyWordStep[] parseSteps( List<Step> xSteps, String testName, String typeName )
	{

		if (log.isDebugEnabled())
			log.debug( "Extracted " + xSteps.size() + " Steps" );

		List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );

		for ( Step xStep : xSteps )
		{
		    
		    KeyWordStep step = KeyWordStepFactory.instance().createStep( xStep.getName(), xStep.getPage(), xStep.isActive(), xStep.getType(),
                                                                                 xStep.getLinkId(), xStep.isTimed(), StepFailure.valueOf( xStep.getFailureMode() ), xStep.isInverse(),
                                                                                 xStep.getOs(), xStep.getBrowser(), xStep.getPoi(), xStep.getThreshold().intValue(), "", xStep.getWait().intValue(),
                                                                                 xStep.getContext(), xStep.getValidation(), xStep.getDevice(),
                                                                                 (xStep.getValidationType() != null && !xStep.getValidationType().isEmpty() ) ? ValidationType.valueOf( xStep.getValidationType() ) : null, xStep.getTagNames(), xStep.isStartAt(), xStep.isBreakpoint(), xStep.getDeviceTags(), xStep.getSite() );
		    
		    parseParameters( xStep.getParameter(), testName, xStep.getName(), typeName, step );
		    parseTokens( xStep.getToken(), testName, xStep.getName(), typeName, step );
		    
		    step.addAllSteps( parseSteps( xStep.getStep(), testName, typeName ) );
		    stepList.add( step );
		}

		return stepList.toArray( new KeyWordStep[0] );
	}

	/**
	 * Parses the parameters.
	 *
	 * @param pList the list
	 * @param testName the test name
	 * @param stepName the step name
	 * @param typeName the type name
	 * @param parentStep the parent step
	 * @return the key word parameter[]
	 */
	private void parseParameters( List<Parameter> pList, String testName, String stepName, String typeName, KeyWordStep parentStep )
	{
	    if (log.isDebugEnabled())
            log.debug( "Extracted " + pList.size() + " Parameters" );
	    

	    for ( Parameter p : pList )
	    {
	        ParameterType ptype = ParameterType.valueOf( p.getType() );
	        KeyWordParameter kp = new KeyWordParameter( ptype, p.getValue(), p.getName(), p.getUsage() );
	        
	        if ( p.getToken() != null && !p.getToken().isEmpty() )
	        {
	            for ( Token t : p.getToken() )
	            {
	                kp.addToken( new KeyWordToken( TokenType.valueOf(t.getType() ), t.getValue(), t.getName() ) );
	            }
	        }
	        
	        if ( ParameterType.FILE.equals( ptype ))
	        {
	            File dataFile = new File( p.getValue() );
	            if ( dataFile.isFile() )
	            {
	                try
	                {
                            kp.setValue( readFile( new FileInputStream( dataFile ) ) );
                            kp.setFileName( dataFile.getAbsolutePath() );
	                }
	                catch( FileNotFoundException e )
	                {
	                    log.error( "Error reading parameter file", e );
	                }
	            }
	            else
	            {
	                if ( fileName == null )
	                {
	                    kp.setValue( readFile( getClass().getClassLoader().getResourceAsStream( p.getValue() ) ) );
	                    kp.setFileName( "classpath://" + p.getValue() );
	                }
	                else
	                {
	                    dataFile = new File( fileName.getParentFile(), p.getValue() );
	                    if ( dataFile.isFile() )
	                    {
	                        try
	                        {
	                            kp.setValue( readFile( new FileInputStream( dataFile ) ) );
	                            kp.setFileName( dataFile.getAbsolutePath() );
	                        }
	                        catch( FileNotFoundException e )
	                        {
	                            log.error( "Error reading parameter file", e );
	                        }
	                    }
	                }
	            } 
	        }
	        
	        parentStep.addParameter( kp );
	    }
	}
	
	private String readFile( InputStream inputStream )
	{
	    
	    try
	    {
	        StringBuilder stringBuilder = new StringBuilder();
	        int bytesRead = 0;
	        byte[] buffer = new byte[512];

	        while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
	        {
	            stringBuilder.append( new String( buffer, 0, bytesRead ) );
	        }
	        
	        return stringBuilder.toString();
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        try { inputStream.close(); } catch( Exception e ) {}
	    }
	    
	    return null;
	}

	/**
	 * Parses the tokens.
	 *
	 * @param tList the t list
	 * @param testName the test name
	 * @param stepName the step name
	 * @param typeName the type name
	 * @param parentStep the parent step
	 * @return the key word token[]
	 */
	private void parseTokens( List<Token> tList, String testName, String stepName, String typeName, KeyWordStep parentStep )
	{
	    if (log.isDebugEnabled())
            log.debug( "Extracted " + tList + " Tokens" );

		for ( Token t : tList )
		{
		    parentStep.addToken( new KeyWordToken( TokenType.valueOf(t.getType() ), t.getValue(), t.getName() ) );
		}

	}
}
