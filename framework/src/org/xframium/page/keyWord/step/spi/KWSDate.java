package org.xframium.page.keyWord.step.spi;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.page.Page;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.step.AbstractKeyWordStep;

public class KWSDate extends AbstractKeyWordStep
{

	private int dateValue;
	private String dateFormat;
	private DateFormat dateFormatObj;
	private String updatedDateValue;
	
    private enum SwitchType
    {
        CURRENTDATE,CUSTOMDATE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com
     * .perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    @Override
    public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception
    {
        if ( log.isDebugEnabled() )
            log.debug( "Execution Function " + getName() );

        if ( getParameterList().size() < 1 )
            throw new IllegalArgumentException( "First Parameter Switchtype should be provided with values CURRENTDATE| CUSTOMDATE" );

        try
        {
            // Verify if the parameter-1 values are correct
            String switchType = getParameterValue( getParameterList().get( 0 ), contextMap, dataMap ) + "";
            
            switch ( SwitchType.valueOf( switchType ) )
            {
                case CURRENTDATE:
                    if ( getParameterList().size() < 3 )
                        throw new IllegalArgumentException( "Please provide the date value to be added or substracted from current date" );
                    
                    dateFormat  = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";                    
                    dateValue = new Integer(getParameterValue( getParameterList().get( 2 ), contextMap, dataMap ) + "").intValue();
                    
                    dateFormatObj = new SimpleDateFormat(dateFormat);         		   
         		   	Calendar currentCal = Calendar.getInstance();
         		   	currentCal.add(Calendar.DATE,dateValue);
         		    updatedDateValue = dateFormatObj.format(currentCal.getTime());
         		   	break;
                case CUSTOMDATE:
                    if ( getParameterList().size() < 4 )
                        throw new IllegalArgumentException( "Please provide the date value to be added or substracted from current date" );
                    
                    dateFormat  = getParameterValue( getParameterList().get( 1 ), contextMap, dataMap ) + "";
                    dateValue = new Integer(getParameterValue( getParameterList().get( 3 ), contextMap, dataMap ) + "").intValue();                    
                    dateFormatObj = new SimpleDateFormat(dateFormat);         		   
         		    Calendar customCalendar = new GregorianCalendar(2013,0,31);         		   
         		    customCalendar.add(Calendar.DATE,dateValue);
         		    updatedDateValue = dateFormatObj.format(customCalendar.getTime());
         		    break;
                default:
                    throw new IllegalArgumentException( "Parameter switchtype should be BY_WINTITLE| BY_WINURL|BY_FRAME|BY_PARENTFRAME|BY_DEFAULT|BY_ALERT" );
            }
            
            if ( getContext() != null )
    		{
    			if ( log.isDebugEnabled() )
    				log.debug( "Setting Context Data to [" + updatedDateValue + "] for [" + getContext() + "]" );
    			contextMap.put( getContext(), updatedDateValue );
    		}

            
        }
        catch ( Exception e )
        {
            log.error( "Error executing function for validation [" + getName() + "] on page [" + getPageName() + "]", e );
            return false;
        }

        return true;
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#isRecordable()
     */
    public boolean isRecordable()
    {
        return false;
    }

   
}
