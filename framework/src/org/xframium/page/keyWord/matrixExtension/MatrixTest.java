package org.xframium.page.keyWord.matrixExtension;

import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordTest;

public class MatrixTest
{
    private MatrixStepArray stepArray;
    private String[] testArray;
    private String testDefinition;
    private String name;
    private String description;
    private String dataProvider;
    private String dataDriver;
    private String tagNames;
    private String linkId;
    private boolean timed;
    private int threshold;
    private boolean active;
    private String os;
    private String type;

    public MatrixTest( String testDefinition )
    {
        this.testDefinition = testDefinition;
        testArray = testDefinition.split( "," );

        parseTest();
    }
    
    public MatrixTest( String[] testDefinition )
    {
        testArray = testDefinition;
        parseTest();
    }
    
    public MatrixTest( String testDefinition, String stepDefinition )
    {
        this.testDefinition = testDefinition;
        testArray = testDefinition.split( "," );

        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );

        parseTest();
    }

    public MatrixTest( String[] testDefinition, String[] stepDefinition )
    {
        testArray = testDefinition;
        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );
        parseTest();
    }

    public MatrixTest( String[] testDefinition, String[][] stepDefinition )
    {
        testArray = testDefinition;
        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );
        parseTest();
    }
    
    public void setStepDefinition( String stepDefinition )
    {
        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );
    }
    
    public void setStepDefinition( String[] stepDefinition )
    {
        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );
    }
    
    public void setStepDefinition( String[][] stepDefinition )
    {
        if ( stepDefinition != null )
            stepArray = new MatrixStepArray( stepDefinition );
    }
    
    public KeyWordTest createTest()
    {
        KeyWordTest kTest = new KeyWordTest( name, active, dataProvider, dataDriver, timed, linkId, os, threshold, description, tagNames );
        
        int currentPosition = 0;
        
        while( currentPosition < stepArray.getStepList().size() )
        {
            MatrixStep currentStep = stepArray.getStepList().get( currentPosition++ );
            KeyWordStep kStep = currentStep.createStep();
            if ( currentStep.getLevel() != null )
                addSteps( kStep, currentPosition );
            
            kTest.addStep( kStep );
        }
        
        return kTest;
    }
    
    private String addSteps( KeyWordStep parentStep, int currentPosition )
    {
        while( currentPosition < stepArray.getStepList().size() )
        {
            MatrixStep currentStep = stepArray.getStepList().get( currentPosition++ );
            KeyWordStep kStep = currentStep.createStep();
            parentStep.addStep( kStep );
            if ( currentStep.getLevel() != null )
            {
                if ( currentStep.getLevel().equals( ">>" ) )
                {
                    String returnValue = addSteps( kStep, currentPosition );
                    if ( returnValue != null && returnValue.startsWith( "<<" ) )
                        return currentStep.getLevel().substring( 2 );
                }
                else if ( currentStep.getLevel().startsWith( "<<" ) )
                    return currentStep.getLevel().substring( 2 );
            }
        }
        
        return null;
    }
    
    

    private String parseString( String currentValue, String defaultValue )
    {
        if ( currentValue == null )
            return defaultValue;
        else
            return currentValue;
    }

    private boolean parseBoolean( String currentValue, boolean defaultValue )
    {
        if ( currentValue == null || currentValue.trim().isEmpty() )
            return defaultValue;
        else
            return Boolean.parseBoolean( currentValue );
    }

    private int parseInt( String currentValue, int defaultValue )
    {
        if ( currentValue == null || currentValue.trim().isEmpty() )
            return defaultValue;
        else
            return Integer.parseInt( currentValue );
    }

    private void parseTest()
    {
        for ( int i = 0; i < testArray.length; i++ )
        {
            switch ( i + 1 )
            {
                case 1:
                    name = parseString( testArray[i], null );
                    break;
                    
                case 2:
                    type = parseString( testArray[i], "test" );
                    type = type.toLowerCase();
                    break;
                    
                case 3:
                    description = parseString( testArray[i], null );
                    break;

                case 4:
                    dataProvider = parseString( testArray[i], null );
                    break;

                case 5:
                    dataDriver = parseString( testArray[i], null );
                    break;

                case 6:
                    tagNames = parseString( testArray[i], null );
                    break;

                case 7:
                    linkId = parseString( testArray[i], null );
                    break;

                case 8:
                    timed = parseBoolean( testArray[i], false );
                    break;

                case 9:
                    threshold = parseInt( testArray[i], 0 );
                    break;

                case 0:
                    active = parseBoolean( testArray[i], true );
                    break;

                case 11:
                    os = parseString( testArray[i], null );
                    break;

            }
        }

    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }

    public String getDataProvider()
    {
        return dataProvider;
    }

    public String getDataDriver()
    {
        return dataDriver;
    }

    public String getTagNames()
    {
        return tagNames;
    }

    public String getLinkId()
    {
        return linkId;
    }

    public boolean isTimed()
    {
        return timed;
    }

    public int getThreshold()
    {
        return threshold;
    }

    public boolean isActive()
    {
        return active;
    }

    public String getOs()
    {
        return os;
    }

}
