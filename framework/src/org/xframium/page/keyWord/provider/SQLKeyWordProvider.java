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

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.data.PageDataManager;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordStep.ValidationType;
import org.xframium.page.keyWord.KeyWordToken.TokenType;
import org.xframium.page.keyWord.provider.xsd.Import;
import org.xframium.page.keyWord.provider.xsd.Model;
import org.xframium.page.keyWord.provider.xsd.ObjectFactory;
import org.xframium.page.keyWord.provider.xsd.Parameter;
import org.xframium.page.keyWord.provider.xsd.RegistryRoot;
import org.xframium.page.keyWord.provider.xsd.Step;
import org.xframium.page.keyWord.provider.xsd.Test;
import org.xframium.page.keyWord.provider.xsd.Token;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

import org.xframium.utility.SQLUtil;

/**
 * The Class SQLKeyWordProvider.
 */
public class SQLKeyWordProvider
    implements KeyWordProvider
{
    //
    // Class Data
    //

    private static final String DEF_SUITE_QUERY =
        "SELECT NAME \n" +
        "FROM PERFECTO_TEST_SUITES";
    
    private static final String DEF_PAGE_QUERY =
        "SELECT SUITE_NAME, PAGE_NAME, CLASS_NAME \n" +
        "FROM PERFECTO_TEST_SUITE_PAGES";
    
    private static final String DEF_IMPORT_QUERY =
        "SELECT SUITE_NAME, IMPORTED_NAME, INCLUDE_TESTS, INCLUDE_FUNCTIONS \n" +
        "FROM PERFECTO_TEST_IMPORTED_SUITES";
    
    private static final String DEF_TEST_QUERY =
        "SELECT SUITE_NAME, NAME, DATA_DRIVER, DATA_PROVIDER, LINK_ID, TIMED, THRESHOLD, ACTIVE, OS, TAG_NAMES \n" +
        "FROM PERFECTO_TESTS";
    
    private static final String DEF_STEP_QUERY =
        "SELECT STEP_KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS, POI, THRESHOLD, TIMED, INVERSE, WAIT, \n" +
        "       FAILURE_MODE, VALIDATION, VALIDATION_TYPE, DEVICE, TAG_NAMES, OFFSET \n" +
        "FROM PERFECTO_TEST_STEPS \n" +
        "ORDER BY SUITE_NAME, TEST_NAME, OFFSET";
    
    private static final String DEF_SUB_STEP_QUERY =    
        "SELECT PARENT_KEY, CHILD_KEY \n" +
        "FROM PERFECTO_TEST_SUBSTEPS";
    
    private static final String DEF_PARAM_QUERY =
        "SELECT STEP_KEY, TYPE, VALUE, OFFSET \n" +
        "FROM PERFECTO_TEST_STEP_PARAMS \n" +
        "ORDER BY STEP_KEY, OFFSET";
    
    private static final String DEF_TOKEN_QUERY =
        "SELECT STEP_KEY, NAME, TYPE, VALUE \n" +
        "FROM PERFECTO_TEST_STEP_TOKENS";
    
    private static final String DEF_FUNCTION_QUERY =
        "SELECT SUITE_NAME, NAME, DATA_DRIVER, DATA_PROVIDER, LINK_ID, TIMED, THRESHOLD, ACTIVE, OS \n" +
        "FROM PERFECTO_FUNCTIONS";

    //
    // Instant Data
    //
    
    /** The log. */
    private Log log = LogFactory.getLog( KeyWordTest.class );

        /** The username. */
    private String username;
	
    /** The password. */
    private String password;
	
    /** The JDBC URL. */
    private String url;
	
    /** The driver class name. */
    private String driver;

    /** The test suite name. */
    private String suite;
	
    /** The queries. */
    private String suiteQuery;
    private String pageQuery;
    private String importQuery;
    private String testQuery;
    private String stepQuery;
    private String substepQuery;
    private String paramQuery;
    private String tokenQuery;
    private String functionQuery;
    

    /**
     * Instantiates a new SQL key word provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLKeyWordProvider( String username, String password, String url, String driver, String suite )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.suite = suite;
        
        this.suiteQuery = DEF_SUITE_QUERY;
        this.pageQuery = DEF_PAGE_QUERY;
        this.importQuery = DEF_IMPORT_QUERY;
        this.testQuery = DEF_TEST_QUERY;
        this.stepQuery = DEF_STEP_QUERY;
        this.substepQuery = DEF_SUB_STEP_QUERY;
        this.paramQuery = DEF_PARAM_QUERY;
        this.tokenQuery = DEF_TOKEN_QUERY;
        this.functionQuery = DEF_FUNCTION_QUERY;

        readData();
    }

    /**
     * Instantiates a new XML key word provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     * @param queryies... the query that loads data
     */
    public SQLKeyWordProvider( String username, String password, String url, String driver, String suite,
                               String suiteQuery, String pageQuery, String importQuery, String testQuery, String stepQuery,
                               String substepQuery, String paramQuery, String tokenQuery, String functionQuery )
    {
        this( username, password, url, driver, suite );

        this.suiteQuery = (( suiteQuery != null ) ? suiteQuery : DEF_SUITE_QUERY );
        this.pageQuery = (( pageQuery != null ) ? pageQuery : DEF_PAGE_QUERY );
        this.importQuery = (( importQuery != null ) ? importQuery : DEF_IMPORT_QUERY );
        this.testQuery = (( testQuery != null ) ? testQuery : DEF_TEST_QUERY );
        this.stepQuery = (( stepQuery != null ) ? stepQuery : DEF_STEP_QUERY );
        this.substepQuery = (( substepQuery != null ) ? substepQuery : DEF_SUB_STEP_QUERY );
        this.paramQuery = (( paramQuery != null ) ? paramQuery : DEF_PARAM_QUERY );
        this.tokenQuery = (( tokenQuery != null ) ? tokenQuery : DEF_TOKEN_QUERY );
        this.functionQuery = (( functionQuery != null ) ? functionQuery : DEF_FUNCTION_QUERY );

        readData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.provider.KeyWordProvider#readData()
     */
    public void readData()
    {
        try
        {
            RegistryRoot suiteData = loadSuiteFromData( suite );

            parseModel( suiteData.getModel() );
            parseImports( suiteData.getImport() );

            readElements( suiteData, suite, true, true );
        }
        catch( IllegalStateException e )
        {
            throw e;
        }
        catch (Exception e)
        {
            log.fatal( "Error reading SQL Element File", e );
        }
    }

    //
    // Helpers
    //
    
    private void readElements( RegistryRoot suiteData, String suiteName, boolean readTests, boolean readFunctions )
        throws Exception
    {
        if ( readTests )
        {
            for( Test test : suiteData.getTest() )
            {
                if (KeyWordDriver.instance().getTest( test.getName() ) != null)
                {
                    log.warn( "The test [" + test.getName() + "] is already defined and will not be added again" );
                    continue;
                }
			        
                KeyWordTest currentTest = parseTest( test, "test" );
			        
                if (currentTest.getDataDriver() != null && !currentTest.getDataDriver().isEmpty())
                {
                    PageData[] pageData = PageDataManager.instance().getRecords( currentTest.getDataDriver() );
                    if (pageData == null)
                    {
                        log.warn( "Specified Data Driver [" + currentTest.getDataDriver() + "] could not be located. Make sure it exists and it was populated prior to initializing your keyword factory" );
                        KeyWordDriver.instance().addTest( currentTest );
                    }
                    else
                    {
                        String testName = currentTest.getName();

                        for (PageData record : pageData)
                        {
                            KeyWordDriver.instance().addTest( currentTest.copyTest( testName + "!" + record.getName() ) );
                        }
                    }
                }
                else
                    KeyWordDriver.instance().addTest( currentTest );
			        
            }
        }

        if (readFunctions)
        {
            for( Test test : suiteData.getFunction() )
            {
                if (KeyWordDriver.instance().getTest( test.getName() ) != null)
                {
                    log.warn( "The function [" + test.getName() + "] is already defined and will not be added again" );
                    continue;
                }
                    
                KeyWordDriver.instance().addFunction( parseTest( test, "function" ) );
            }
        }
    }

    private void parseImports( List<Import> importList )
    {
        for ( Import imp : importList )
        {
            try
            {
                if (log.isInfoEnabled())
                {
                    log.info( "Attempting to import suite [" + imp.getFileName() + "]" );
                }

                RegistryRoot suiteData = loadSuiteFromData( imp.getFileName() );
	            
                readElements( suiteData, imp.getFileName(), imp.isIncludeTests(), imp.isIncludeFunctions() );
            }
            catch( Exception e )
            {
                log.fatal( "Could not read from " + imp.getFileName(), e );
                throw new IllegalStateException( e );
            }
        }
    }

    private void parseModel( Model model )
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
    
                KeyWordDriver.instance().addPage( page.getName(), useClass );
            }
            catch( Exception e )
            {
                log.error( "Error creating instance of [" + page.getClazz() + "]" );
            }
        }
    }

    private KeyWordTest parseTest( Test xTest, String typeName )
    {
        KeyWordTest test = new KeyWordTest( xTest.getName(), xTest.isActive(), xTest.getDataProvider(), xTest.getDataDriver(), xTest.isTimed(), xTest.getLinkId(), xTest.getOs(), xTest.getThreshold().intValue(), "", xTest.getTagNames() );
		
        KeyWordStep[] steps = parseSteps( xTest.getStep(), xTest.getName(), typeName );

        for (KeyWordStep step : steps)
            test.addStep( step );

        return test;
    }

    private KeyWordStep[] parseSteps( List<Step> xSteps, String testName, String typeName )
    {

        if (log.isDebugEnabled())
            log.debug( "Extracted " + xSteps.size() + " Steps" );

        List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );

        for ( Step xStep : xSteps )
        {
            KeyWordStep step = KeyWordStepFactory.instance().createStep( xStep.getName(), xStep.getPage(), xStep.isActive(), xStep.getType(),
                                                                         xStep.getLinkId(), xStep.isTimed(), StepFailure.valueOf( xStep.getFailureMode() ), xStep.isInverse(),
                                                                         xStep.getOs(), xStep.getPoi(), xStep.getThreshold().intValue(), "", xStep.getWait().intValue(),
                                                                         xStep.getContext(), xStep.getValidation(), xStep.getDevice(),
                                                                         (xStep.getValidationType() != null && !xStep.getValidationType().isEmpty() ) ? ValidationType.valueOf( xStep.getValidationType() ) : null, xStep.getTagNames() );
		    
            parseParameters( xStep.getParameter(), testName, xStep.getName(), typeName, step );
            parseTokens( xStep.getToken(), testName, xStep.getName(), typeName, step );
		    
            step.addAllSteps( parseSteps( xStep.getStep(), testName, typeName ) );
            stepList.add( step );
        }

        return stepList.toArray( new KeyWordStep[0] );
    }

    private void parseParameters( List<Parameter> pList, String testName, String stepName, String typeName, KeyWordStep parentStep )
    {
        if (log.isDebugEnabled())
            log.debug( "Extracted " + pList.size() + " Parameters" );
	    

        for ( Parameter p : pList )
        {
            parentStep.addParameter( new KeyWordParameter( ParameterType.valueOf( p.getType() ), p.getValue() ) );
        }
    }

    private void parseTokens( List<Token> tList, String testName, String stepName, String typeName, KeyWordStep parentStep )
    {
        if (log.isDebugEnabled())
            log.debug( "Extracted " + tList + " Tokens" );

        for ( Token t : tList )
        {
            parentStep.addToken( new KeyWordToken( TokenType.valueOf(t.getType() ), t.getValue(), t.getName() ) );
        }
    }

    private RegistryRoot loadSuiteFromData( String suiteName )
        throws Exception
    {
        ObjectFactory factory = new ObjectFactory();
        
        RegistryRoot rtn = factory.createRegistryRoot();
        BulkData data = loadBulkData();

        rtn.setModel( factory.createModel() );
        for( int i = 0; i < data.pageData.length; ++i )
        {
            String pageSuiteName = (String) data.pageData[i][0];

            if ( pageSuiteName.equals( suiteName ))
            {
                String name = (String) data.pageData[i][1];
                String clazz = (String) data.pageData[i][2];

                org.xframium.page.keyWord.provider.xsd.Page page = factory.createPage();
                page.setName( name );
                page.setClazz( clazz );

                rtn.getModel().getPage().add( page );
            }
        }

        for( int i = 0; i < data.importData.length; ++i )
        {
            String importSuiteName = (String) data.importData[i][0];

            if ( importSuiteName.equals( suiteName ))
            {
                String importedSuite = (String) data.importData[i][1];
                String importTests = (String) data.importData[i][2];
                String importFunctions = (String) data.importData[i][3];

                Import impt = factory.createImport();
                impt.setFileName( importedSuite );
                impt.setIncludeTests( "Y".equals( importTests ));
                impt.setIncludeFunctions( "Y".equals( importFunctions ));

                rtn.getImport().add( impt );
            }
        }

        for( int i = 0; i < data.testData.length; ++i )
        {
            String testSuiteName = (String) data.testData[i][0];

            if ( testSuiteName.equals( suiteName ))
            {
                Test test = factory.createTest();

                test.setName( (String) data.testData[i][1] );
                test.setDataDriver( (String) data.testData[i][2] );
                test.setDataProvider( (String) data.testData[i][3] );
                test.setLinkId( (String) data.testData[i][4] );
                test.setTimed( "Y".equals(data.testData[i][5]) );
                if ( data.testData[i][6] != null ) test.setThreshold( new BigInteger( data.testData[i][6].toString()));
                test.setActive( "Y".equals(data.testData[i][7]) );
                test.setOs( (String) data.testData[i][8] );
                test.setTagNames( (String) data.testData[i][9] );

                loadTest( factory, suiteName, test, data );

                rtn.getTest().add( test );
            }
        }

        for( int i = 0; i < data.functionData.length; ++i )
        {
            String functionSuiteName = (String) data.functionData[i][0];

            if ( functionSuiteName.equals( suiteName ))
            {
                Test test = factory.createTest();

                test.setName( (String) data.functionData[i][1] );
                test.setDataDriver( (String) data.functionData[i][2] );
                test.setDataProvider( (String) data.functionData[i][3] );
                test.setLinkId( (String) data.functionData[i][4] );
                test.setTimed( "Y".equals(data.functionData[i][5]) );
                if ( data.functionData[i][6] != null ) test.setThreshold( new BigInteger( data.functionData[i][6].toString()));
                test.setActive( "Y".equals(data.functionData[i][7]) );
                test.setOs( (String) data.functionData[i][8] );
                test.setTagNames( (String) data.functionData[i][9] );

                loadTest( factory, suiteName, test, data );

                rtn.getFunction().add( test );
            }
        }
        
        return rtn;
    }

    private void loadTest( ObjectFactory factory, String suiteName, Test test, BulkData data )
    {
        String testName = test.getName();
        HashMap stepsByKey = new HashMap();

        for( int i = 0; i < data.stepData.length; ++i )
        {
            String stepSuiteName = (String) data.stepData[i][1];
            String stepTestName = (String) data.stepData[i][2];

            if (( stepSuiteName.equals( suiteName )) &&
                ( stepTestName.equals( testName )))
            {
                Number key = (Number) data.stepData[i][0];

                String name = (String) data.stepData[i][3];
                String page_name = (String) data.stepData[i][4];
                String type = (String) data.stepData[i][5];
                boolean active = "Y".equals( data.stepData[i][6] );
                String link_id = (String) data.stepData[i][7];
                String os = (String) data.stepData[i][8];
                String poi = (String) data.stepData[i][9];
                Number threshold = (Number) data.stepData[i][10];
                boolean timed = "Y".equals( data.stepData[i][11] );
                boolean inverse = "Y".equals( data.stepData[i][12] );
                Number wait = (Number) data.stepData[i][13];
                String failure_mode = (String) data.stepData[i][14];
                String validation = (String) data.stepData[i][15];
                String validation_type = (String) data.stepData[i][16];
                String device = (String) data.stepData[i][17];
                String tag_names = (String) data.stepData[i][18];

                Step step = factory.createStep();

                step.setName( name );
                step.setPage( page_name );
                step.setType( type );
                step.setActive( active );
                step.setLinkId( link_id );
                step.setOs( os );
                step.setPoi( poi );
                if (threshold != null) step.setThreshold( new BigInteger( threshold.toString() ));
                step.setTimed( timed );
                step.setInverse( inverse );
                if (wait != null) step.setWait( new BigInteger( wait.toString() ));
                step.setFailureMode( failure_mode );
                step.setValidation( validation );
                step.setValidationType( validation_type );
                step.setDevice( device );
                step.setTagNames( tag_names );

                stepsByKey.put( key, step );
                test.getStep().add( step );
            }
        }

        for( int i = 0; i < data.substepData.length; ++i )
        {
            Number parent_key = (Number) data.substepData[i][0];
            Number child_key = (Number) data.substepData[i][1];

            Step parent = (Step) stepsByKey.get( parent_key );
            Step child = (Step) stepsByKey.get( child_key );

            if (( parent != null ) &&
                ( child != null ))
            {
                parent.getStep().add( child );
            }
        }

        for( int i = 0; i < data.paramData.length; ++i )
        {
            Number key = (Number) data.paramData[i][0];
            Step step = (Step) stepsByKey.get( key );

            if ( step != null )
            {
                String type = (String) data.paramData[i][1];
                String value = (String) data.paramData[i][2];

                Parameter param = factory.createParameter();

                param.setType( type );
                param.setValue( value );

                step.getParameter().add( param );
            }
        }

        for( int i = 0; i < data.tokenData.length; ++i )
        {
            Number key = (Number) data.tokenData[i][0];
            Step step = (Step) stepsByKey.get( key );

            if ( step != null )
            {
                String name = (String) data.tokenData[i][1];
                String type = (String) data.tokenData[i][2];
                String value = (String) data.tokenData[i][3];

                Token token = factory.createToken();

                token.setName( name );
                token.setType( type );
                token.setValue( value );

                step.getToken().add( token );
            }
        }
    }

    private class BulkData
    {
        public Object[][] suiteData;
        public Object[][] pageData;
        public Object[][] importData;
        public Object[][] testData;
        public Object[][] stepData;
        public Object[][] substepData;
        public Object[][] paramData;
        public Object[][] tokenData;
        public Object[][] functionData;
    }

    private BulkData loadBulkData()
        throws Exception
    {
        BulkData rtn = new BulkData();

        rtn.suiteData = SQLUtil.getResults( username, password, url, driver, suiteQuery, null );
        rtn.pageData = SQLUtil.getResults( username, password, url, driver, pageQuery, null );
        rtn.importData = SQLUtil.getResults( username, password, url, driver, importQuery, null );
        rtn.testData = SQLUtil.getResults( username, password, url, driver, testQuery, null );
        rtn.stepData = SQLUtil.getResults( username, password, url, driver, stepQuery, null );
        rtn.substepData = SQLUtil.getResults( username, password, url, driver, substepQuery, null );
        rtn.paramData = SQLUtil.getResults( username, password, url, driver, paramQuery, null );
        rtn.tokenData = SQLUtil.getResults( username, password, url, driver, tokenQuery, null );
        rtn.functionData = SQLUtil.getResults( username, password, url, driver, functionQuery, null );
        
        return rtn;
    }

}
