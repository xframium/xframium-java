package org.xframium.page.keyWord.matrixExtension;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MatrixStepArray
{
    private String testDefinition;
    
    public MatrixStepArray( String testDefintiion )
    {
        this.testDefinition = testDefintiion;
        parseDefinition();
    }
    
    public MatrixStepArray( String[] stepDefintiion )
    {
        for ( String currentStep : stepDefintiion )
        {
            if ( currentStep != null && currentStep.toLowerCase().startsWith( "level" ) )
                continue;
            
            MatrixStep x = new MatrixStep( currentStep );
            if ( x.getName() != null && !x.getName().trim().isEmpty() )
                stepList.add( x );
        }
    }
    
    public MatrixStepArray( String[][] stepDefintiion )
    {
        for ( String[] currentStep : stepDefintiion )
        {
            if ( currentStep[0] != null && currentStep[0].toLowerCase().startsWith( "level" ) )
                continue;
            MatrixStep x = new MatrixStep( currentStep[ 0 ] );
            if ( x.getName() != null && !x.getName().trim().isEmpty() )
                stepList.add( x );
        }
    }
    
    private List<MatrixStep> stepList = new ArrayList<MatrixStep>( 10 );
    
    
    public List<MatrixStep> getStepList()
    {
        return stepList;
    }

    private void parseDefinition()
    {
        try
        {
            LineNumberReader numberReader = new LineNumberReader( new StringReader( testDefinition ) );
            String currentLine;
            while ( (currentLine = numberReader.readLine() ) != null )
            {

                MatrixStep x = new MatrixStep( currentLine );
                if ( x.getName() != null && !x.getName().trim().isEmpty() )
                    stepList.add( x );
                
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        
    }
}
