package org.xframium.page.keyWord.matrixExtension;

import java.util.Arrays;
import java.util.List;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.KeyWordStep.ValidationType;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.step.KeyWordStepFactory;

public class MatrixStep
{
    private String stepDefinition;
    private String[] stepArray;
    
    private String level;
    private String type;
    private String name;
    private String pageName;
    private MatrixParameterArray parameterArray;
    private MatrixTokenArray tokenArray;
    private boolean active;
    private StepFailure failureMode = StepFailure.ERROR;
    private boolean inverse;
    private String os;
    private ValidationType vType = ValidationType.REGEX;
    private String validationDetail;
    private String context;
    private String device;
    private String poi;
    private boolean timed;
    private String tagNames;
    private int threshold = 0;
    private int wait = 0;
    private String linkId;
    

    public MatrixStep( String stepDefinition )
    {
        this.stepDefinition = stepDefinition;
        stepArray = stepDefinition.split( "," );
        parseStep();
    }
    
    public MatrixStep( String[] stepDefinition )
    {
        stepArray = stepDefinition;
        parseStep();
    }
    
    public KeyWordStep createStep()
    {
        KeyWordStep kStep = KeyWordStepFactory.instance().createStep( name, pageName, active, type, linkId, timed, failureMode, inverse, os, poi, threshold, "", wait, context, validationDetail, device, vType, tagNames );
        if ( parameterArray != null )
        {
            List<KeyWordParameter> parameterList = parameterArray.getParameters();
            if ( parameterList != null )
            {
                for ( KeyWordParameter p : parameterList )
                    kStep.addParameter( p );
            }
        }
        
        if ( tokenArray != null )
        {
            List<KeyWordToken> tokenList = tokenArray.getTokens();
            if ( tokenList != null )
            {
                for ( KeyWordToken p : tokenList )
                    kStep.addToken( p );
            }
        }
        
        return kStep;
        
        
    }

    public String getStepDefinition()
    {
        return stepDefinition;
    }

    public String[] getStepArray()
    {
        return stepArray;
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
    
    
    
    @Override
    public String toString()
    {
        return "MatrixStep [stepDefinition=" + stepDefinition + ", stepArray=" + Arrays.toString( stepArray ) + ", level=" + level + ", type=" + type + ", name=" + name + ", pageName=" + pageName + ", parameterArray=" + parameterArray + ", tokenArray="
                + tokenArray + ", active=" + active + ", failureMode=" + failureMode + ", inverse=" + inverse + ", os=" + os + ", vType=" + vType + ", validationDetail=" + validationDetail + ", context=" + context + ", device=" + device + ", poi=" + poi
                + ", timed=" + timed + ", tagNames=" + tagNames + ", threshold=" + threshold + ", wait=" + wait + ", linkId=" + linkId + "]";
    }

    private void parseStep()
    {
        for ( int i=0; i<stepArray.length; i++ )
        {
            switch ( i+1 )
            {
                case 1:
                    level = parseString( stepArray[ i ], null );
                    break;
                    
                case 2:
                    type = parseString( stepArray[ i ], "EXISTS" );
                    type = type.toUpperCase();
                    break;
                    
                case 3:
                    name = parseString( stepArray[ i ], null );
                    break;
                    
                case 4:
                    pageName = parseString( stepArray[ i ], null );
                    break;
                    
                case 5:
                    parameterArray = new MatrixParameterArray( parseString( stepArray[ i ], null ) );
                    break;
                    
                case 6:
                    tokenArray = new MatrixTokenArray( parseString( stepArray[ i ], null ) );
                    break;
                    
                case 7:
                    active = parseBoolean( stepArray[ i ], true );
                    break;
                    
                case 8:
                    failureMode = StepFailure.valueOf( parseString( stepArray[ i ], StepFailure.ERROR.name() ) );
                    break;
                    
                case 9:
                    inverse = parseBoolean( stepArray[ i ], false );
                    break;
                    
                case 10:
                    os = parseString( stepArray[ i ], null );
                    break;
                    
                case 11:
                    vType = ValidationType.valueOf( parseString( stepArray[ i ], ValidationType.REGEX.name() ) );
                    break;
                    
                case 12:
                    validationDetail = parseString( stepArray[ i ], null );
                    break;
                    
                case 13:
                    context = parseString( stepArray[ i ], null );
                    break;
                    
                case 14:
                    device = parseString( stepArray[ i ], null );
                    break;
                    
                case 15:
                    poi = parseString( stepArray[ i ], null );
                    break;
                    
                case 16:
                    timed = parseBoolean( stepArray[ i ], false );
                    break;
                    
                case 17:
                    tagNames = parseString( stepArray[ i ], null );
                    break;
                    
                case 18:
                    threshold = parseInt( stepArray[ i ], 0 );
                    break;
                    
                case 19:
                    wait = parseInt( stepArray[ i ], 0 );
                    break;
                
                case 20:
                    linkId = parseString( stepArray[ i ], null );
                    break;
            }
        }
    }

    public String getLevel()
    {
        return level;
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public String getPageName()
    {
        return pageName;
    }

    public boolean isActive()
    {
        return active;
    }

    public StepFailure getFailureMode()
    {
        return failureMode;
    }

    public boolean isInverse()
    {
        return inverse;
    }

    public String getOs()
    {
        return os;
    }

    public ValidationType getvType()
    {
        return vType;
    }

    public String getValidationDetail()
    {
        return validationDetail;
    }

    public String getContext()
    {
        return context;
    }

    public String getDevice()
    {
        return device;
    }

    public String getPoi()
    {
        return poi;
    }

    public boolean isTimed()
    {
        return timed;
    }

    public String getTagNames()
    {
        return tagNames;
    }

    public int getThreshold()
    {
        return threshold;
    }

    public int getWait()
    {
        return wait;
    }

    public String getLinkId()
    {
        return linkId;
    }
    
    
}
