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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xframium.page.Page;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordTest;
import org.xframium.page.keyWord.matrixExtension.MatrixTest;
import org.xframium.utility.SQLUtil;

/**
 * The Class SQLKeyWordProvider.
 */
public class SQLKeyWordProvider
    implements KeyWordProvider
{
    private static final String DEF_MODEL_QUERY = "SELECT P.ID, P.NAME, CLASS_NAME FROM PAGES P INNER JOIN SITES S ON S.ID = SITE_ID WHERE S.NAME = ?";
    private static final String DEF_TEST_CASE_QUERY = "SELECT T.ID, C.NAME, T.NAME AS TYPE, C.DESCRIPTION, C.DATA_PROVIDER, C.DATA_dRIVER, C.TAG_NAMES, C.LINK_ID, C.TIMED, C.THRESHOLD, C.ACTIVE, C.OS FROM TEST_CASES C INNER JOIN TEST_TYPES T ON T.ID = C.TYPE INNER JOIN TEST_SUITES TS ON TS.ID = C.TEST_SUITE_ID WHERE TS.NAME = ? ORDER BY T.ID";
    private static final String DEF_TEST_STEP_QUERY = "SELECT S.ID, TC.NAME, S.STEP_LEVEL, K.NAME AS TYPE, S.NAME, P.NAME as PAGE_NAME, PT.NAME AS P_TYPE, SP.VALUE AS P_VALUE, TT.NAME AS T_TYPE, ST.NAME AS T_NAME, ST.VALUE AS T_VALUE, S.ACTIVE, F.NAME as FAILURE_TYPE, S.INVERSE, S.OS, VT.NAME AS V_TYPE, S.VALIDATION_VALUE, S.CONTEXT, S.DEVICE, S.POI, S.TIMED, S.TAG_NAMES, S.THRESHOLD, S.WAIT_AFTER, S.OFFSET FROM TEST_STEPS S INNER JOIN STEP_TYPES K ON K.ID = S.STEP_TYPE INNER JOIN PAGES P ON P.ID = S.PAGE INNER JOIN FAILURE_TYPES F ON F.ID = S.FAILURE_MODE LEFT JOIN VALIDATION_TYPES VT ON VT.ID = S.VALIDATION_TYPE INNER JOIN TEST_CASES TC ON TC.ID = S.TEST_CASE_ID INNER JOIN TEST_SUITES TS ON TS.ID = TC.TEST_SUITE_ID LEFT JOIN STEP_PARAMETERS SP ON SP.STEP_ID = S.ID LEFT JOIN PARAMETER_TYPES PT ON PT.ID = SP.TYPE LEFT JOIN STEP_TOKENS ST ON ST.STEP_ID = S.ID LEFT JOIN TOKEN_TYPES TT ON TT.ID = ST.TYPE WHERE TS.NAME = ? ORDER BY S.TEST_CASE_ID, S.OFFSET";
    

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
    private String siteName;
	
    /** The queries. */
    private String modelQuery;
    private String testCaseQuery;
    private String testStepQuery;
    

    /**
     * Instantiates a new SQL key word provider.
     *
     * @param username the user name
     * @param password the password
     * @param url the JDBC URL
     * @param driver the driver class name
     */
    public SQLKeyWordProvider( String username, String password, String url, String driver, String suite, String siteName )
    {
        this.username = username;
        this.password = password;
        this.url = url;
        this.driver = driver;
        this.suite = suite;
        this.siteName = siteName;
        this.modelQuery = DEF_MODEL_QUERY;
        this.testCaseQuery = DEF_TEST_CASE_QUERY;
        this.testStepQuery = DEF_TEST_STEP_QUERY;
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
    public SQLKeyWordProvider( String username, String password, String url, String driver, String suite, String siteName,
                           String modelQuery, String testCaseQuery, String testStepQuery )
    {
        this( username, password, url, driver, suite, siteName );

        this.modelQuery = (( modelQuery != null ) ? modelQuery : DEF_MODEL_QUERY );
        this.testCaseQuery = (( testCaseQuery != null ) ? testCaseQuery : DEF_TEST_CASE_QUERY );
        this.testStepQuery = (( testStepQuery != null ) ? testStepQuery : DEF_TEST_STEP_QUERY );
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
            parseModel();
            parseTestCases();
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

    private void parseTestCases()
    {
        Map<String,MatrixTest> testMap = new HashMap<String,MatrixTest>( 10 );
        try
        {
            Object[][] results = SQLUtil.getResults( username, password, url, driver, testCaseQuery, new String[] { suite } );
            for ( Object[] resultRow : results )
            {
                List<String> testDefinition = new ArrayList<String>( 10 );
                for ( int j=1; j<resultRow.length; j++ )
                    testDefinition.add( resultRow[j] == null ? null : (resultRow[j] + "") );
                
                MatrixTest currentTest = new MatrixTest( testDefinition.toArray( new String[ 0 ] ) );
                if ( currentTest.getName() != null && !currentTest.getName().isEmpty() && currentTest.isActive() )
                    testMap.put( currentTest.getName(), currentTest );
            }
            
            results = SQLUtil.getResults( username, password, url, driver, testStepQuery, new String[] { suite } );
            
            int currentIndex = 0;
            while( currentIndex < results.length )
            {
                String parameterSet = null;
                String tokenSet = null;
                
                Object[] currentRow = results[ currentIndex ];
                //
                // Add the parameter
                //
                if ( currentRow[6] != null && currentRow[7] != null )
                    parameterSet = currentRow[6] + "(" + currentRow[7] + ")";
                
                if ( currentRow[8] != null && currentRow[9] != null && currentRow[10] != null)
                    tokenSet = currentRow[9] + "=" + currentRow[8] + "(" + currentRow[10] + ")";
                int stepId = Integer.parseInt( currentRow[0] + "" );
                String testName = currentRow[ 1 ] + "";
                int nextPosition = 1;
                while ( currentIndex + nextPosition < results.length )
                {
                    int nextStepId = Integer.parseInt( results[ currentIndex + nextPosition ][0] + "" );
                    if ( nextStepId != stepId )
                        break;
                    
                    Object[] thisRow = results[ currentIndex + nextPosition ];
                    
                    if ( thisRow[6] != null && thisRow[7] != null )
                        parameterSet = parameterSet + "\r\n" + thisRow[6] + "(" + thisRow[7] + ")";
                    
                    if ( currentRow[8] != null && currentRow[9] != null && currentRow[10] != null)
                        tokenSet = tokenSet + "\r\n" + thisRow[9] + "=" + thisRow[8] + "(" + thisRow[10] + ")";
                    
                    nextPosition++;
                }
                
                currentIndex += nextPosition;
 
                List<String> stepDefinition = new ArrayList<String>( 10 );
                for ( int j=2; j<currentRow.length; j++ )
                {
                    if ( j==6 )
                        stepDefinition.add( parameterSet );
                    
                    else if ( j==8 )
                        stepDefinition.add( tokenSet );
                    
                    else if ( j == 7 || j == 9 || j == 10 )
                        continue;
                    else
                    {
                        if ( currentRow[ j ] == null )
                            stepDefinition.add( null );
                        else
                            stepDefinition.add( currentRow[ j ] + "" );
                    }
                }
                
                testMap.get( testName ).addStep( stepDefinition.toArray( new String[ 0 ] ) );;
                
            }
            
            
            for ( MatrixTest currentTest : testMap.values() )
            {
                if ( currentTest.getType().toLowerCase().equals( "function" ) )
                    KeyWordDriver.instance().addFunction( currentTest.createTest() );
                else
                    KeyWordDriver.instance().addTest( currentTest.createTest() );
            }
            
            
        }
        catch( Exception e )
        {
            log.error( "Error parsing test cases from " + testCaseQuery, e );
        }
        
        
    }

    
    private void parseModel()
    {
        try
        {
            Object[][] results = SQLUtil.getResults( username, password, url, driver, modelQuery, new String[] { siteName } );
            for ( Object[] resultRow : results )
            {
                try
                {
                    Class useClass = KeyWordPage.class;
                    if ( resultRow[2] != null && !(resultRow[2] + "" ).isEmpty() )
                        useClass = ( Class<Page> ) Class.forName( (resultRow[2] + "" ) );
                    
                    if (log.isDebugEnabled())
                        log.debug( "Creating page as " + useClass.getSimpleName() + " for " + resultRow[1] );
        
                    KeyWordDriver.instance().addPage( resultRow[1] + "", useClass );
                }
                catch( Exception e )
                {
                    log.error( "Error creating instance of [" + resultRow[2] + "]" );
                }
            }
        }
        catch( Exception e )
        {
            log.error( "Error parsing model from " + modelQuery );
        }
    }
}
