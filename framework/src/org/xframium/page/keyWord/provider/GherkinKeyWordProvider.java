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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.container.SuiteContainer;
import org.xframium.page.data.DefaultPageData;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.data.provider.AbstractPageDataProvider;
import org.xframium.page.keyWord.KeyWordDriver.TRACE;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

import cucumber.api.DataTable;
import cucumber.runtime.table.TableConverter;
import gherkin.formatter.Formatter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.ExamplesTableRow;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Tag;
import gherkin.parser.Parser;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLKeyWordProvider.
 */
public class GherkinKeyWordProvider extends AbstractPageDataProvider implements KeyWordProvider, Formatter
{
	private File[] folderList;
	private String[] packageList;
	private SuiteContainer suiteContainer = new SuiteContainer();
	
	private List<KeyWordStep> backgroundSteps = new ArrayList<KeyWordStep>( 10 );
    private List<KeyWordTest> scenarioList = new ArrayList<KeyWordTest>( 10 );
    private KeyWordTest currentScenario;
    private Section currentSection = Section.FEATURE;
    private Log log = LogFactory.getLog( GherkinKeyWordProvider.class );
    private String xFID;
    
    private enum Section
    {
        FEATURE,
        BACKGROUND,
        SCENARIOS,
        OUTLINE,
    }

	Parser bddParser = new Parser( this );
	
	public GherkinKeyWordProvider( File[] folderList, String[] packageList, String xFID )
	{
		this.folderList = folderList;
		this.packageList = packageList;
		this.xFID = xFID;
	}
	
	@Override
	public SuiteContainer readData(boolean parseDataIterators) 
	{
		log.info( "Scanning for feature files" );
		for ( File f : folderList )
		{
			readFile( f );
		}
		
		suiteContainer.setDataProvider( this );
		
		return suiteContainer;
	}
	
	private void readFile( File currentFile )
	{
		
		if ( currentFile.isDirectory() )
		{
			File[] fileList = currentFile.listFiles( new FeatureFileFilter() );
			for ( File file : fileList )
				readFile( file );
			
			File[] dirList = currentFile.listFiles( new DirectoryFileFilter() );
			for ( File dir : dirList )
				readFile( dir );
			return;
		}
		
		log.info( "Reading feature file " + currentFile.getAbsolutePath() );
		
		BufferedInputStream iS = null; 
		
		try
		{
			iS = new BufferedInputStream ( new FileInputStream( currentFile ) );
			StringBuilder sB = new StringBuilder();
			byte[] buffer = new byte[ 512 ];
			int bytesRead = 0;
			
			while( ( bytesRead = iS.read( buffer ) ) > 0 )
				sB.append( new String( buffer, 0, bytesRead ) );
			
	        bddParser.parse( sB.toString(), currentFile.getAbsolutePath(), 0 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try { iS.close(); } catch( Exception e ) {}
		}
	}
	
	

	@Override
	public void background(Background arg0) {
		currentSection = Section.BACKGROUND;
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void done() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endOfScenarioLifeCycle(Scenario arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eof() 
	{
		for ( KeyWordTest k : scenarioList )
		{
			boolean addIt = true;
			for ( KeyWordTest t : suiteContainer.getActiveTestList() )
			{
				if ( t.getName().equals( k.getName() ) )
				{
					addIt = false;
					break;
				}
			}
			
			if ( addIt )
				suiteContainer.getActiveTestList().add( k );
		}
	}

	@Override
	public void examples(Examples arg0) {

		addRecordType( currentScenario.getName(), false );
        String[] columnNames = null;
        
        if ( arg0.getName() != null && arg0.getName().toLowerCase().startsWith( "page data=" ) )
        {
        	
        	PageData[] pageData = PageDataManager.instance( xFID ).getRecords( arg0.getName().substring( 10 ) );
        	for ( PageData p : pageData )
        	{
        		addRecord( p, currentScenario.getName() );
        	}
        	
        	return;
        }
        
        int namePosition = -1;
        for ( ExamplesTableRow e : arg0.getRows() )
        {
            if ( columnNames == null )
            {
                
                columnNames = new String[ e.getCells().size() ];
                for ( int i=0; i<e.getCells().size(); i++ )
                {
                    columnNames[ i ] = e.getCells().get( i );
                    if ( columnNames[ i ].equals( "name" ) )
                        namePosition = i;
                }
            }
            else
            {
                DefaultPageData pageData = new DefaultPageData( currentScenario.getName(), namePosition == -1 ? "" : e.getCells().get( namePosition ), true );
                for ( int i=0; i<e.getCells().size(); i++ )
                {
                    pageData.addValue( columnNames[ i ], e.getCells().get( i ) );
                }
                addRecord( pageData );
            }
        }
		
	}

	@Override
	public void feature(Feature arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scenario(Scenario xTest) {
		currentSection = Section.SCENARIOS;
        String tagNames = null;
        if ( xTest.getTags() != null && !xTest.getTags().isEmpty() )
        {
            tagNames = "";
            for ( Tag tagName : xTest.getTags() )
                tagNames = tagNames + tagName.getName() + ",";
            
            tagNames = tagNames.substring( 0, tagNames.length() - 1 );
        }
        
        this.currentScenario = new KeyWordTest( xTest.getName(), true, null, null, false, null, null, 0, xTest.getDescription(), tagNames, null, null, null, 0, null, null, null, null, 0, 0, TRACE.OFF.name() );

        for ( KeyWordStep xStep : backgroundSteps )
            this.currentScenario.addStep( xStep );
        
        log.info( "Adding " + xTest.getName() );
        scenarioList.add( currentScenario );
		
	}

	@Override
	public void scenarioOutline(ScenarioOutline xTest) {
		currentSection = Section.OUTLINE;
        String tagNames = null;
        if ( xTest.getTags() != null && !xTest.getTags().isEmpty() )
        {
            tagNames = "";
            for ( Tag tagName : xTest.getTags() )
                tagNames = tagNames + tagName.getName() + ",";
            
            tagNames = tagNames.substring( 0, tagNames.length() - 1 );
        }
        
        this.currentScenario = new KeyWordTest( xTest.getName(), true, null, xTest.getName(), false, null, null, 0, xTest.getDescription(), tagNames, null, null, null, 0, null, null, null, null, 0, 0, TRACE.OFF.name() );

        for ( KeyWordStep xStep : backgroundSteps )
            this.currentScenario.addStep( xStep );
        
        log.info( "Adding " + xTest.getName() );
        scenarioList.add( currentScenario );
		
	}

	@Override
	public void startOfScenarioLifeCycle(Scenario arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(gherkin.formatter.model.Step xStep) {
		KeyWordStep step = KeyWordStepFactory.instance().createStep( xStep.getName(), "bdd", true, "GHERKIN", "", false, StepFailure.ERROR, false, null, null, null, 0, "", 0, "", null, null, null, null, false, false, null, null, null, null, null, null, false, null, null, false );
		if ( xStep.getRows() != null && !xStep.getRows().isEmpty() )
		{
			step.setDataTable( DataTable.create( xStep.getRows() ) );
		}
        
		
        switch( currentSection )
        {
            case BACKGROUND:
                backgroundSteps.add( step );
                break;
            case FEATURE:
                break;
            case OUTLINE:
                if ( currentScenario.getDataDriver() != null )
                    step.addParameter( new KeyWordParameter( ParameterType.STATIC, "bdd=" + currentScenario.getDataDriver(), null, null ) );
                currentScenario.addStep( step );
                break;
            case SCENARIOS:
                currentScenario.addStep( step );
                break;
        }
		
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2, String arg3, Integer arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uri(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private static class FeatureFileFilter implements FilenameFilter
	{

		@Override
		public boolean accept(File dir, String name) {
			return ( name.endsWith( ".feature") );
		}
		
	}
	
	private static class DirectoryFileFilter implements FileFilter
	{

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}

		
		
	}

	@Override
	public void readPageData() {
		// TODO Auto-generated method stub
		
	}
	

	
}

