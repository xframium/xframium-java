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
package org.xframium.page.keyWord.step;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.xframium.artifact.ArtifactType;
import org.xframium.container.SuiteContainer;
import org.xframium.content.ContentManager;
import org.xframium.device.data.DataManager;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ObjectConfigurationException;
import org.xframium.exception.ScriptConfigurationException;
import org.xframium.integrations.perfectoMobile.rest.PerfectoMobile;
import org.xframium.integrations.perfectoMobile.rest.services.WindTunnel.Status;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.StepStatus;
import org.xframium.page.data.PageData;
import org.xframium.page.element.Element;
import org.xframium.page.keyWord.KeyWordDriver;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordToken;
import org.xframium.page.keyWord.step.spi.KWSElse;
import org.xframium.page.keyWord.step.spi.KWSLoopBreak;
import org.xframium.spi.driver.ReportiumProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractKeyWordStep.
 */
public abstract class AbstractKeyWordStep implements KeyWordStep
{
    public AbstractKeyWordStep()
    {
        kwImpl = getClass().getName();
        kw = KeyWordStepFactory.instance().getKW( getClass() );
        natualLanguage = PageManager.instance().getFormattedMessage( getClass().getSimpleName() );
    }
    
    /** The name. */
    private String name;

    /** The page name. */
    private String pageName;
    
    private String siteName;

    /** The active. */
    private boolean active;

    /** The link id. */
    private String linkId;

    /** The timed. */
    private boolean timed;
    
    private boolean startAt;
    
    private boolean breakpoint;
    
    protected boolean orMapping = true;

    @Override
    public boolean isBreakpoint()
    {
        // TODO Auto-generated method stub
        return false;
    }



    @Override
    public void setBreakpoint( boolean breakpoint )
    {
        // TODO Auto-generated method stub
        
    }

    /** The s failure. */
    private StepFailure sFailure;

    /** The inverse. */
    private boolean inverse = false;

    /** The Constant SPLIT. */
    private static final String SPLIT = "-->";

    /** The fork. */
    private boolean fork;

    /** The os. */
    private String os;

    /** The context. */
    private String context;

    /** The validation. */
    private String validation;

    /** The threshold. */
    private int threshold;

    /** The description. */
    private String description;

    /** The validation type. */
    private ValidationType validationType;

    /** The poi. */
    private String poi;

    /** The wait time. */
    private long waitTime;

    /** The device. */
    private String device;
    
    private String[] tagNames;
    private String[] deviceTags;
    
    protected String kwName = "N/A";
    protected String kwDescription;
    protected String kwHelp;
    protected String kwImpl;
    protected String kw;
    protected String natualLanguage;

    @Override
    public void setTagNames( String tagNames )
    {
        if ( tagNames != null && !tagNames.isEmpty() )
        {
            this.tagNames = tagNames.split( "," );
        }
    }
    
    public void setDeviceTags( String deviceTags )
    {
        if ( deviceTags != null && !deviceTags.isEmpty() )
        {
            this.deviceTags = deviceTags.split( "," );
        }
    }
    
    
    
    public String getSiteName()
    {
        return siteName;
    }



    public void setSiteName( String siteName )
    {
        this.siteName = siteName;
    }



    public boolean isStartAt()
    {
        return startAt;
    }



    public void setStartAt( boolean startAt )
    {
        this.startAt = startAt;
    }



    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getPoi()
     */
    public String getPoi()
    {
        return poi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setPoi(java.lang.String)
     */
    public void setPoi( String poi )
    {
        this.poi = poi;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getContext()
     */
    public String getContext()
    {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setContext(java.lang.String)
     */
    public void setContext( String context )
    {
        this.context = context;
    }

    /** The parameter list. */
    private List<KeyWordParameter> parameterList = new ArrayList<KeyWordParameter>( 10 );

    /** The token list. */
    private List<KeyWordToken> tokenList = new ArrayList<KeyWordToken>( 10 );

    /** The log. */
    protected Log log = LogFactory.getLog( AbstractKeyWordStep.class );

    /**
     * _execute step.
     *
     * @param pageObject
     *            the page object
     * @param webDriver
     *            the web driver
     * @param contextMap
     *            the context map
     * @param dataMap
     *            the data map
     * @param pageMap
     *            TODO
     * @param sC TODO
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    protected abstract boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception;

    /**
     * Creates the point.
     *
     * @param pointValue
     *            the point value
     * @return the point
     */
    protected Point createPoint( String pointValue )
    {
        Point x = null;

        try
        {
            String[] coors = pointValue.split( "," );

            if ( coors.length == 2 )
            {
                x = new Point( Integer.parseInt( coors[0].trim() ), Integer.parseInt( coors[1].trim() ) );
                return x;
            }
        }
        catch ( Exception e )
        {
            log.warn( Thread.currentThread().getName() + ": Could not parse coordinates " + pointValue + " due to " + e.getMessage() );
        }

        return null;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#toError()
     */
    public String toError()
    {
        return getClass().getSimpleName() + " returned false";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#isFork()
     */
    public boolean isFork()
    {
        return fork;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getOs()
     */
    @Override
    public String getOs()
    {
        return os;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setOs(java.lang.String)
     */
    @Override
    public void setOs( String os )
    {
        this.os = os != null ? os.toUpperCase() : os;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getThreshold()
     */
    public int getThreshold()
    {
        return threshold;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setThreshold(int)
     */
    public void setThreshold( int threshold )
    {
        this.threshold = threshold;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getDescription()
     */
    public String getDescription()
    {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setDescription(java.lang.
     * String)
     */
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * Checks if is recordable.
     *
     * @return true, if is recordable
     */
    public boolean isRecordable()
    {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setFork(boolean)
     */
    public void setFork( boolean fork )
    {
        this.fork = fork;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#isInverse()
     */
    public boolean isInverse()
    {
        return inverse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setInverse(boolean)
     */
    public void setInverse( boolean inverse )
    {
        this.inverse = inverse;
    }

    /** The step list. */
    private List<KeyWordStep> stepList = new ArrayList<KeyWordStep>( 10 );

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#isTimed()
     */
    public boolean isTimed()
    {
        return timed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setTimed(boolean)
     */
    public void setTimed( boolean timed )
    {
        this.timed = timed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getFailure()
     */
    @Override
    public StepFailure getFailure()
    {
        return sFailure;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setFailure(com.perfectoMobile
     * .page.keyWord.KeyWordStep.StepFailure)
     */
    @Override
    public void setFailure( StepFailure sFailure )
    {
        this.sFailure = sFailure;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getDevice()
     */
    public String getDevice()
    {
        return device;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setDevice(java.lang.String)
     */
    public void setDevice( String device )
    {
        this.device = device;
    }

    /**
     * Gets the element.
     *
     * @param pageObject
     *            the page object
     * @param contextMap
     *            the context map
     * @param webDriver
     *            the web driver
     * @param dataMap
     *            the data map
     * @return the element
     */
    protected Element getElement( Page pageObject, Map<String, Object> contextMap, Object webDriver, Map<String, PageData> dataMap )
    {
        return getElement( pageObject, contextMap, webDriver, dataMap, null );
    }

    /**
     * Gets the element.
     *
     * @param pageObject
     *            the page object
     * @param contextMap
     *            the context map
     * @param webDriver
     *            the web driver
     * @param dataMap
     *            the data map
     * @param overrideName
     *            the override name
     * @return the element
     */
    protected Element getElement( Page pageObject, Map<String, Object> contextMap, Object webDriver, Map<String, PageData> dataMap, String overrideName )
    {
        String useName = name;
        if ( overrideName != null && !overrideName.isEmpty() )
            useName = overrideName;

        if ( useName.startsWith( Element.CONTEXT_ELEMENT ) )
        {
            if ( Element.CONTEXT_ELEMENT.equals( useName ) )
            {
                if ( log.isDebugEnabled() )
                    log.debug( Thread.currentThread().getName() + ": Attempting to acquire CONTEXT element" );

                Element currentElement = (Element) contextMap.get( Element.CONTEXT_ELEMENT );

                if ( log.isDebugEnabled() )
                    log.debug( Thread.currentThread().getName() + ": CONTEXT element found as " + currentElement );

                return currentElement;
            }
            else
            {
                String elementName = useName.split( SPLIT )[1];

                if ( log.isDebugEnabled() )
                    log.debug( Thread.currentThread().getName() + ": Attempting to acquire CONTEXT element" );

                Element currentElement = (Element) contextMap.get( Element.CONTEXT_ELEMENT );

                if ( log.isDebugEnabled() )
                    log.debug( Thread.currentThread().getName() + ": CONTEXT element found as " + currentElement );

                ElementDescriptor elementDescriptor = new ElementDescriptor( siteName != null && siteName.trim().length() > 0 ? siteName : PageManager.instance().getSiteName(), getPageName(), elementName );
                Element myElement = pageObject.getElement( elementDescriptor ).cloneElement();

                if ( myElement == null )
                {
                    log.error( Thread.currentThread().getName() + ": **** COULD NOT LOCATE ELEMENT [" + elementDescriptor.toString() + "]  Make sure your Page Name and Element Name are spelled correctly and that they have been defined" );
                    return null;
                }
                
                myElement.setDriver( webDriver );

                for ( KeyWordToken token : tokenList )
                {
                    myElement.addToken( token.getName(), getTokenValue( token, contextMap, dataMap ) );
                }

                myElement.setCacheNative( true );

                myElement.setDriver( webDriver );
                myElement.setContext( currentElement );
                return myElement;
            }
        }
        else
        {
            if ( tokenList != null && !tokenList.isEmpty() )
            {
                if ( log.isInfoEnabled() )
                    log.info( Thread.currentThread().getName() + ": Cloning Element " + useName + " on page " + pageName );
                
                ElementDescriptor elementDescriptor = new ElementDescriptor( siteName != null && siteName.trim().length() > 0 ? siteName : PageManager.instance().getSiteName(), pageName, useName );
                Element originalElement = pageObject.getElement( elementDescriptor ).cloneElement();
                if ( originalElement == null )
                    throw new ObjectConfigurationException( siteName != null && siteName.trim().length() > 0 ? siteName : PageManager.instance().getSiteName(), pageName, useName );
                
                Element clonedElement = originalElement.cloneElement();
                clonedElement.setDriver( webDriver );

                for ( KeyWordToken token : tokenList )
                {
                    clonedElement.addToken( token.getName(), getTokenValue( token, contextMap, dataMap ) );
                }

                clonedElement.setCacheNative( true );
                return clonedElement;
            }
            else
            {
                try
                {
                    ElementDescriptor elementDescriptor = new ElementDescriptor( siteName != null && siteName.trim().length() > 0 ? siteName : PageManager.instance().getSiteName(), pageName, useName );
                    Element elt = pageObject.getElement( elementDescriptor ).cloneElement();
                    elt.setDriver( webDriver );
                    elt.setCacheNative( true );
                    return elt;
                }
                catch( NullPointerException e )
                {
                    throw new ObjectConfigurationException( siteName == null ? PageManager.instance().getSiteName() : siteName, pageName, useName );
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#executeStep(com.
     * perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map,
     * java.util.Map)
     */
    public boolean executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap, SuiteContainer sC ) throws Exception
    {
        PageManager.instance().setThrowable( null );
        long startTime = System.currentTimeMillis();

        try
        {
            //
            // OS Checks to allow for elements to be skipped if the OS did not
            // match
            //
            if ( os != null )
            {
                String deviceOs = getDeviceOs( webDriver );
                if ( deviceOs == null )
                {
                    if ( log.isInfoEnabled() )
                        log.info( Thread.currentThread().getName() + ": A Required OS of [" + os + "] was specified however the OS of the device could not be determined" );
                    return true;
                }
                
                String[] osArray = os.split( "," );
                boolean osFound = false;
                for ( String localOs : osArray )
                {
                    if ( localOs.toUpperCase().trim().equals( deviceOs.toUpperCase() ) )
                    {
                        osFound = true;
                        break;
                    }
                }
                
                if ( !osFound )
                {
                    if ( log.isInfoEnabled() )
                        log.info( Thread.currentThread().getName() + ": A Required OS in [" + os + "] was specified however the OS of the device was [" + deviceOs.toUpperCase() + "]" );
                    return true;
                }
                
                
            }
            
            //
            // Device tagging implementation
            //
            if ( deviceTags != null && deviceTags.length > 0 && PageManager.instance().getDeviceTag( webDriver ) != null && PageManager.instance().getDeviceTag( webDriver ).length > 0 )
            {
                boolean tagFound = false;
                for ( String localTag : deviceTags )
                {
                    for ( String deviceTag : PageManager.instance().getDeviceTag( webDriver ) )
                    {
                        if ( localTag.toUpperCase().trim().equals( deviceTag.toUpperCase() ) )
                        {
                            tagFound = true;
                            break;
                        }
                    }
                    if ( tagFound )
                        break;
                }
                
                if ( !tagFound )
                {
                    if ( log.isInfoEnabled() )
                        log.info( Thread.currentThread().getName() + ": This step was ignored as the tag was not specified" );
                    return true;
                }
            }

            //
            // Check for tag names
            //
            if ( tagNames != null && tagNames.length > 0 && PageManager.instance().getTagNames() != null && PageManager.instance().getTagNames().length > 0 )
            {
                boolean tagEnabled = false;
                for ( String tagName : tagNames )
                {
                    for ( String useTag : PageManager.instance().getTagNames() )
                    {
                        if ( tagName.equals( useTag ) )
                        {
                            tagEnabled = true;
                            break;
                        }
                    }
                    
                    if ( tagEnabled )
                        break;
                }
                
                
                if ( !tagEnabled )
                {
                    if ( log.isInfoEnabled() )
                        log.info( Thread.currentThread().getName() + ": This required a tag that was not enabled for this test run" );
                    return true;
                }
            }

            if ( log.isInfoEnabled() )
                log.info( Thread.currentThread().getName() + ": Executing Step " + name + "(" + getClass().getSimpleName() + ")" + (linkId != null ? " linked to " + linkId : "") );

            Exception stepException = null;
            boolean returnValue = false;
            boolean caughtException = false;
            try
            {
                WebDriver altWebDriver = getAltWebDriver();
                
                //
                // Listener integrations for individual steps
                //
                if ( !KeyWordDriver.instance().notifyBeforeStep( altWebDriver != null ? altWebDriver : webDriver, this, pageObject, contextMap, dataMap, pageMap ) )
                {
                    log.warn( "Test Step was skipped due to a failed step notification listener" );
                    return true;
                }
                
                if ( ( (DeviceWebDriver) webDriver ).getCloud().getProvider().equals( "PERFECTO" ) )
                {
                    if( DataManager.instance().isArtifactEnabled( ArtifactType.REPORTIUM ) )
                    {
                        if ( ( (ReportiumProvider) webDriver ).getReportiumClient() != null )
                        {
                            ( (ReportiumProvider) webDriver ).getReportiumClient().testStep( getPageName() + "." + getName() + " (" + getClass().getSimpleName() + ")" );
                        }
                    }
                }
                
                returnValue = _executeStep( pageObject, ((altWebDriver != null) ? altWebDriver : webDriver), contextMap, dataMap, pageMap, sC );
                
                KeyWordDriver.instance().notifyAfterStep( altWebDriver != null ? altWebDriver : webDriver, this, pageObject, contextMap, dataMap, pageMap, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE );
                
            }
            catch ( KWSLoopBreak lb )
            {
                throw lb;
            }
            catch ( Exception e )
            {
                caughtException = true;
                stepException = e;
                returnValue = false;
                try
                {
                    WebDriver altWebDriver = getAltWebDriver();
                    KeyWordDriver.instance().notifyAfterStep( altWebDriver != null ? altWebDriver : webDriver, this, pageObject, contextMap, dataMap, pageMap, returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE );
                }
                catch( Exception e2 )
                {
                    
                }
                log.error( Thread.currentThread().getName() + ": ***** Step " + name + " on page " + pageName + " failed " );
                log.debug( Thread.currentThread().getName() + ": ***** Step " + name + " on page " + pageName + " failed ", e );
                
            }

            if ( inverse )
                returnValue = !returnValue;

            boolean subFailure = false;
            //
            // If there are sub steps and this was successful, then execute
            // those. If we are in fork mode, then skip that execution
            //
            if ( !fork && getStepList() != null && !getStepList().isEmpty() && returnValue )
            {
                boolean subReturnValue = false;
                for ( KeyWordStep step : getStepList() )
                {
                    if ( step instanceof KWSElse )
                        continue;
                    try
                    {
                        subReturnValue = step.executeStep( pageObject, webDriver, contextMap, dataMap, pageMap, sC );
                    }
                    catch ( KWSLoopBreak e )
                    {
                        throw e;
                    }
                    catch ( Exception e )
                    {
                        stepException = e;
                        subReturnValue = false;
                        log.debug( Thread.currentThread().getName() + ": ***** Step " + name + " on page " + pageName + " encoundered error: ", e );
                    }

                    //if ( step.isInverse() )
                    //{
                    //    subReturnValue = !subReturnValue;
                    //}

                    if ( !subReturnValue )
                    {
                        returnValue = false;
                        if ( step.getFailure().equals( StepStatus.FAILURE_IGNORED ) )
                            subFailure = false;
                        else
                            subFailure = true;
                        break;
                    }

                }
            }
            
            //
            // Special case for the ELSE clause if found
            //
            if ( !fork && getStepList() != null && !getStepList().isEmpty() && !returnValue )
            {
                for ( KeyWordStep parentStep : getStepList() )
                {
                    if ( parentStep instanceof KWSElse && !parentStep.getStepList().isEmpty() )
                    {
                        for ( KeyWordStep step : parentStep.getStepList() )
                        {
                            boolean subReturnValue = false;
                            try
                            {
                                subReturnValue = step.executeStep( pageObject, webDriver, contextMap, dataMap, pageMap, sC );
                            }
                            catch ( KWSLoopBreak e )
                            {
                                throw e;
                            }
                            catch ( Exception e )
                            {
                                stepException = e;
                                subReturnValue = false;
                                log.debug( Thread.currentThread().getName() + ": ***** Step " + name + " on page " + pageName + " encoundered error: ", e );
                            }
    
                            if ( step.isInverse() )
                            {
                                subReturnValue = !subReturnValue;
                            }
    
                            if ( !subReturnValue )
                            {
                                returnValue = false;
                                if ( step.getFailure().equals( StepStatus.FAILURE_IGNORED ) )
                                    subFailure = false;
                                else
                                    subFailure = true;
                                break;
                            }
                        }
                    }
                }
            }

            if ( isRecordable() )
            {
                PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName(), getName(), getClass().getSimpleName(), startTime, System.currentTimeMillis() - startTime,
                        returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, "", null, getThreshold(), getDescription(), false, null );
                if ( isTimed() )
                    PageManager.instance().addExecutionTiming( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName() + "." + getName() + "." + getClass().getSimpleName(), System.currentTimeMillis() - startTime,
                            returnValue ? StepStatus.SUCCESS : StepStatus.FAILURE, description, threshold );
            }

            if ( PageManager.instance().isWindTunnelEnabled() && getPoi() != null && !getPoi().isEmpty() )
                PerfectoMobile.instance().windTunnel().addPointOfInterest( getExecutionId( webDriver ), getPoi() + "(" + getPageName() + "." + getName() + ")", returnValue ? Status.success : Status.failure );

            if ( !returnValue )
            {
                switch ( sFailure )
                {
                    case ERROR:
                        if ( PageManager.instance().getThrowable() == null )
                        {
                            if ( stepException == null )
                                stepException = new ScriptConfigurationException( toError() );

                            PageManager.instance().setThrowable( stepException );
                            PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName(), getName(), "_" + getClass().getSimpleName(), startTime, System.currentTimeMillis() - startTime, StepStatus.FAILURE,
                                    stepException.getMessage(), stepException, getThreshold(), getDescription(), false, new String[] { stepException.getMessage() } );

                            if ( isTimed() )
                                PageManager.instance().addExecutionTiming( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName() + "." + getName() + "." + getClass().getSimpleName(), System.currentTimeMillis() - startTime,
                                        StepStatus.FAILURE, description, threshold );

                            if ( PageManager.instance().isWindTunnelEnabled() && getPoi() != null && !getPoi().isEmpty() )
                                PerfectoMobile.instance().windTunnel().addPointOfInterest( getExecutionId( webDriver ), getPoi() + "(" + getPageName() + "." + getName() + ")", Status.failure );
                        }
                        log.error( Thread.currentThread().getName() + ": ***** Step " + name + " on page " + pageName + " failed as " + PageManager.instance().getThrowable().getMessage() );
                        return false;

                    case LOG_IGNORE:
                        log.warn( Thread.currentThread().getName() + ": Step " + name + " failed but was marked to log and ignore" );

                    case IGNORE:
                        if ( PageManager.instance().getThrowable() == null )
                        {
                            if ( stepException == null )
                                stepException = new IllegalArgumentException( toError() );

                            PageManager.instance().setThrowable( stepException );
                            PageManager.instance().addExecutionLog( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName(), getName(), "_" + getClass().getSimpleName(), startTime, System.currentTimeMillis() - startTime,
                                    StepStatus.FAILURE_IGNORED, stepException.getMessage(), stepException, getThreshold(), getDescription(), false, new String[] { stepException.getMessage() } );

                            if ( isTimed() )
                                PageManager.instance().addExecutionTiming( getExecutionId( webDriver ), getDeviceName( webDriver ), getPageName() + "." + getName() + "." + getClass().getSimpleName(), System.currentTimeMillis() - startTime,
                                        StepStatus.FAILURE, description, threshold );

                            if ( PageManager.instance().isWindTunnelEnabled() && getPoi() != null && !getPoi().isEmpty() )
                                PerfectoMobile.instance().windTunnel().addPointOfInterest( getExecutionId( webDriver ), getPoi() + "(" + getPageName() + "." + getName() + ")", Status.failure );
                        }

                        return !subFailure;

                }
            }

            return returnValue;

        }
        catch ( KWSLoopBreak lb )
        {
            throw lb;
        }
        finally
        {
            if ( waitTime > 0 )
            {
                try
                {
                    Thread.sleep( waitTime );
                }
                catch ( Exception e )
                {

                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setName(java.lang.String)
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setPageName(java.lang.String)
     */
    public void setPageName( String pageName )
    {
        this.pageName = pageName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setActive(boolean)
     */
    public void setActive( boolean active )
    {
        this.active = active;
    }

    /**
     * Gets the parameter list.
     *
     * @return the parameter list
     */
    public List<KeyWordParameter> getParameterList()
    {
        return parameterList;
    }

    /**
     * Gets the token list.
     *
     * @return the token list
     */
    public List<KeyWordToken> getTokenList()
    {
        return tokenList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getStepList()
     */
    public List<KeyWordStep> getStepList()
    {
        return stepList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getLinkId()
     */
    public String getLinkId()
    {
        return linkId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#setLinkId(java.lang.String)
     */
    public void setLinkId( String linkId )
    {
        this.linkId = linkId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#addParameter(com.
     * perfectoMobile.page.keyWord.KeyWordParameter)
     */
    public void addToken( KeyWordToken token )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Adding Token " + token.getValue() );
        tokenList.add( token );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#addParameter(com.
     * perfectoMobile.page.keyWord.KeyWordParameter)
     */
    public void addParameter( KeyWordParameter param )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Adding Parameter " + param.getValue() );
        parameterList.add( param );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.perfectoMobile.page.keyWord.KeyWordStep#addStep(com.perfectoMobile.
     * page.keyWord.KeyWordStep)
     */
    public void addStep( KeyWordStep step )
    {
        if ( log.isDebugEnabled() )
            log.debug( "Adding Sub Step " + step.getName() );
        stepList.add( step );
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#addAllSteps(com.
     * perfectoMobile.page.keyWord.KeyWordStep[])
     */
    public void addAllSteps( KeyWordStep[] step )
    {
        if ( step != null )
        {
            for ( KeyWordStep s : step )
                addStep( s );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getName()
     */
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#isActive()
     */
    public boolean isActive()
    {
        return active;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getPageName()
     */
    public String getPageName()
    {
        return pageName;
    }

    /**
     * Gets the page data.
     *
     * @param recordIdentifier the record identifier
     * @param dataMap the data map
     * @return the page data
     */
    private PageData getPageData( String recordIdentifier, Map<String, PageData> dataMap )
    {
        //
        // First, lets see if this table was cached with the full key
        //
        int dotPosition = recordIdentifier.lastIndexOf( "." );
        String tableName = recordIdentifier.substring( 0, dotPosition );
        
        PageData pageData = dataMap.get( tableName );
        if ( pageData != null )
            return pageData;
        
        String[] tableParts = tableName.split( "\\." );
        
        for ( String currentTable : tableParts )
        {
            if ( pageData != null )
            {
                List<PageData> dataList = pageData.getPageData( currentTable );
                if ( dataList == null || dataList.isEmpty() )
                    log.error( "Could not locate " + currentTable + " in " + recordIdentifier );
                
                pageData = dataList.get( 0 );
            }
            else
            {
                pageData = dataMap.get( currentTable );
                if ( pageData == null )
                    log.error( "Could not locate " + currentTable + " in " + recordIdentifier );
            }
        }
        
        return pageData;
    }
    
    /**
     * Gets the parameter value.
     *
     * @param param
     *            the param
     * @param contextMap
     *            the context map
     * @param dataMap
     *            the data map
     * @return the parameter value
     */
    public Object getParameterValue( KeyWordParameter param, Map<String, Object> contextMap, Map<String, PageData> dataMap )
    {
        String returnValue;
        switch ( param.getType() )
        {
            case CONTEXT:
                returnValue = contextMap.get( param.getValue() ) + "";
                break;

            case STATIC:
                returnValue = param.getValue();
                break;

            case PROPERTY:
                returnValue =  System.getProperty( param.getValue(), "" );
                break;

            case CONTENT:
                returnValue = ContentManager.instance().getContentValue( param.getValue() );
                break;

            case DATA:
                int dotPosition = param.getValue().lastIndexOf( "." );
                String tableName = param.getValue().substring( 0, dotPosition );
                String recordName = param.getValue().substring( param.getValue().lastIndexOf( "." ) + 1 );
                PageData pageData = getPageData( param.getValue(), dataMap );
                if ( pageData == null )
                {
                    throw new ScriptConfigurationException( Thread.currentThread().getName() + ": The Page Data record type [" + tableName + "] does not exist for this test - check your dataProvider or dataDriver attribute" );
                }

                returnValue = pageData.getData( recordName );

                if ( returnValue == null )
                    throw new ScriptConfigurationException(
                            Thread.currentThread().getName() + ": The Page Data field [" + recordName + "] does not exist for the page data record type [" + tableName + "] - Reference one of the following fields - " + pageData );
                break;

            case FILE:
                returnValue = param.getValue();
                for ( KeyWordToken token : param.getTokenList() )
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "Applying token " + token.getName() );
                    
                    returnValue = returnValue.replace( "{" + token.getName() + "}", getTokenValue( token, contextMap, dataMap ) );
                }
                break;
                
            default:
                throw new ScriptConfigurationException( Thread.currentThread().getName() + ": Unknown Parameter Type [" + param.getValue() + "]" );
        }
        
        
        
        return returnValue;
    }

    
    
    
    /**
     * Gets the parameter value.
     *
     * @param token
     *            the token
     * @param contextMap
     *            the context map
     * @param dataMap
     *            the data map
     * @return the parameter value
     */
    public String getTokenValue( KeyWordToken token, Map<String, Object> contextMap, Map<String, PageData> dataMap )
    {
        switch ( token.getType() )
        {
            case CONTEXT:
                return contextMap.get( token.getValue() ) + "";

            case STATIC:
                return token.getValue();

            case PROPERTY:
                return System.getProperty( token.getValue(), "" );

            case CONTENT:
                return ContentManager.instance().getContentValue( token.getValue() + "" );

            case DATA:
                int dotPosition = token.getValue().lastIndexOf( "." );
                String tableName = token.getValue().substring( 0, dotPosition );
                String recordName = token.getValue().substring( token.getValue().lastIndexOf( "." ) + 1 );
                PageData pageData = getPageData( token.getValue(), dataMap );
                if ( pageData == null )
                {
                    throw new ScriptConfigurationException( Thread.currentThread().getName() + ": The Page Data record type [" + tableName + "] does not exist for this test - chexk your dataProvider or dataDriver attribute" );
                }

                Object returnValue = pageData.getData( recordName );

                if ( returnValue == null )
                    throw new ScriptConfigurationException(
                            Thread.currentThread().getName() + ": The Page Data field [" + recordName + "] does not exist for the page data record type [" + tableName + "] - Reference one of the following fields - " + pageData );

                return returnValue + "";

            default:
                throw new ScriptConfigurationException( Thread.currentThread().getName() + ": Unknown Token Type [" + token.getValue() + "]" );
        }
    }

    /**
     * Gets the parameters.
     *
     * @param contextMap
     *            the context map
     * @param dataMap
     *            the data map
     * @return the parameters
     */
    protected Object[] getParameters( Map<String, Object> contextMap, Map<String, PageData> dataMap )
    {
        Object[] parameterArray = new Object[parameterList.size()];

        for ( int i = 0; i < parameterList.size(); i++ )
        {
            parameterArray[i] = getParameterValue( parameterList.get( i ), contextMap, dataMap );
        }

        return parameterArray;
    }

    /**
     * Find method.
     *
     * @param rootClass
     *            the root class
     * @param methodName
     *            the method name
     * @param args
     *            the args
     * @return the method
     */
    protected Method findMethod( Class rootClass, String methodName, Object[] args )
    {
        Method[] methodArray = rootClass.getMethods();

        for ( Method currentMethod : methodArray )
        {
            if ( isCorrectMethod( currentMethod, methodName, args ) )
            {
                if ( log.isDebugEnabled() )
                    log.debug( "Found [" + methodName + "] on " + rootClass.getName() );

                if ( log.isDebugEnabled() && args != null )
                {
                    StringBuilder pBuilder = new StringBuilder();

                    pBuilder.append( args.length ).append( " paramters supplied as: \r\n" );

                    for ( Object arg : args )
                    {
                        pBuilder.append( "\t" );
                        if ( arg == null )
                            pBuilder.append( "NULL" );
                        else
                            pBuilder.append( "[" + arg.toString() + "] of type " + arg.getClass().getName() );
                        pBuilder.append( "\r\n" );

                    }
                    log.debug( pBuilder.toString() );
                }

                return currentMethod;
            }
        }

        if ( log.isWarnEnabled() )
        {
            StringBuilder pBuilder = new StringBuilder();
            pBuilder.append( "Could not locate " ).append( methodName ).append( " with " );
            pBuilder.append( args.length ).append( " paramters supplied as: \r\n" );

            for ( Object arg : args )
            {
                pBuilder.append( "\t" );
                if ( arg == null )
                    pBuilder.append( "NULL" );
                else
                    pBuilder.append( "[" + arg.toString() + "] of type " + arg.getClass().getName() );
                pBuilder.append( "\r\n" );

            }
            log.warn( pBuilder.toString() );
        }
        return null;

    }

    /**
     * Checks if is correct method.
     *
     * @param compareMethod
     *            the compare method
     * @param methodName
     *            the method name
     * @param parameterArray
     *            the parameter array
     * @return true, if is correct method
     */
    protected boolean isCorrectMethod( Method compareMethod, String methodName, Object[] parameterArray )
    {
        if ( !methodName.equals( compareMethod.getName() ) )
            return false;

        if ( (parameterArray == null || parameterArray.length == 0) && (compareMethod.getParameterTypes() == null || compareMethod.getParameterTypes().length == 0) )
            return true;

        if ( parameterArray == null || compareMethod.getParameterTypes() == null )
            return false;

        Class[] parameterTypes = compareMethod.getParameterTypes();

        if ( parameterTypes.length != parameterArray.length )
        {
            if ( log.isDebugEnabled() )
                log.debug( Thread.currentThread().getName() + ": Paramter Count Mismatch " + parameterTypes.length + " - " + parameterArray.length );
            return false;
        }

        for ( int i = 0; i < parameterArray.length; i++ )
        {
            if ( log.isDebugEnabled() )
                log.debug( parameterTypes[i] + " - " + parameterArray[i] );
            if ( !isInstance( parameterTypes[i], parameterArray[i] ) )
                return false;
        }

        return true;
    }

    /**
     * Checks if is instance.
     *
     * @param classType
     *            the class type
     * @param value
     *            the value
     * @return true, if is instance
     */
    protected boolean isInstance( Class classType, Object value )
    {
        try
        {
            if ( classType.isPrimitive() )
            {
                if ( value == null )
                {
                    if ( log.isDebugEnabled() )
                        log.debug( "Primative value null" );
                    return false;
                }
                else
                {
                    Field typeField = value.getClass().getField( TYPE );
                    return classType.isAssignableFrom( (Class) typeField.get( value ) );
                }
            }
            else
                return (value == null || classType.isInstance( value ));
        }
        catch ( Exception e )
        {
            log.error( "Error getting instance", e );
            return false;
        }
    }

    /**
     * Gets the execution id.
     *
     * @param webDriver
     *            the web driver
     * @return the execution id
     */
    public String getExecutionId( WebDriver webDriver )
    {
        return PageManager.instance().getExecutionId( webDriver );
    }

    /**
     * Gets the execution id.
     *
     * @param webDriver
     *            the web driver
     * @return the execution id
     */
    public String getDeviceOs( WebDriver webDriver )
    {
        return PageManager.instance().getDeviceOs( webDriver );
    }

    /**
     * Gets the device name.
     *
     * @param webDriver
     *            the web driver
     * @return the device name
     */
    public String getDeviceName( WebDriver webDriver )
    {
        return PageManager.instance().getDeviceName( webDriver );
    }

    /**
     * Validate data.
     *
     * @param dataValue
     *            the data value
     * @return true, if successful
     */
    protected boolean validateData( String dataValue )
    {
        if ( validationType == null )
            return true;

        switch ( validationType )
        {
            case EMPTY:
                return dataValue == null || dataValue.isEmpty();

            case REGEX:
                if ( dataValue == null )
                {
                    log.warn( "REGEX validation specified with a blank value" );
                    return true;
                }
                else
                {
                    log.debug( Thread.currentThread().getName() + ": Attempting to analyze [" + dataValue + "] using the Regular Expression [" + validation + "]" );
                    if ( !dataValue.matches( validation ) )
                    {
                        log.error( Thread.currentThread().getName() + ": Validation failed for [" + dataValue + "] using the Regular Expression [" + validation + "]" );
                        return false;
                    }
                    return true;
                }

            case NOT_EMPTY:
                return dataValue != null && !dataValue.isEmpty();
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getValidation()
     */
    public String getValidation()
    {
        return validation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setValidation(java.lang.
     * String)
     */
    public void setValidation( String validation )
    {
        this.validation = validation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getValidationType()
     */
    public ValidationType getValidationType()
    {
        return validationType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setValidationType(com.
     * perfectoMobile.page.keyWord.KeyWordStep.ValidationType)
     */
    public void setValidationType( ValidationType validationType )
    {
        this.validationType = validationType;
    }

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#getWait()
     */
    public long getWait()
    {
        return waitTime;
    }

    /* (non-Javadoc)
     * @see com.perfectoMobile.page.keyWord.KeyWordStep#setWait(long)
     */
    public void setWait( long waitAfter )
    {
        this.waitTime = waitAfter;
    }

    //
    // Helpers
    //

    /**
     * Gets the alt web driver.
     *
     * @return the alt web driver
     */
    private WebDriver getAltWebDriver()
    {
        WebDriver rtn = null;

        String deviceName = getDevice();

        if ( (PageManager.instance().getAlternateWebDriverSource() != null) && (deviceName != null) && (!"null".equals( deviceName )) )
        {
            rtn = PageManager.instance().getAlternateWebDriverSource().getAltWebDriver( deviceName );

            if ( rtn == null )
            {
                throw new IllegalArgumentException( "Device: " + deviceName + " is not registered" );
            }
        }

        return rtn;
    }
}
