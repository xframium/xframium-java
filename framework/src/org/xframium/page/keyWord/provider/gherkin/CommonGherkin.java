package org.xframium.page.keyWord.provider.gherkin;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.xframium.container.SuiteContainer;
import org.xframium.device.factory.DeviceWebDriver;
import org.xframium.exception.ScriptException;
import org.xframium.page.ElementDescriptor;
import org.xframium.page.Page;
import org.xframium.page.PageManager;
import org.xframium.page.data.PageData;
import org.xframium.page.keyWord.KeyWordPage;
import org.xframium.page.keyWord.KeyWordParameter;
import org.xframium.page.keyWord.KeyWordParameter.ParameterType;
import org.xframium.page.keyWord.KeyWordStep;
import org.xframium.page.keyWord.KeyWordStep.StepFailure;
import org.xframium.page.keyWord.step.KeyWordStepFactory;
import org.xframium.reporting.ExecutionContextTest;

import cucumber.api.java.en.Then;

public class CommonGherkin 
{
	@Then ( "^I use \\{([^\\}]*)\\} on \\{([^\\}]*)\\}$" )
	public void executeXMLKeyword( WebDriver webDriver, SuiteContainer sC, ExecutionContextTest eC, String kW, String elementDescriptor ) throws Exception
	{
		executeXMLKeyword(webDriver, sC, eC, kW, elementDescriptor, false, null );
	}
	
	@Then ( "^I trace \\{([^\\}]*)\\} on \\{([^\\}]*)\\}$" )
	public void traceXMLKeyword( WebDriver webDriver, SuiteContainer sC, ExecutionContextTest eC, String kW, String elementDescriptor ) throws Exception
	{
		executeXMLKeyword(webDriver, sC, eC, kW, elementDescriptor, true, null );
	}
	
	@Then ( "^I use \\{([^\\}]*)\\} on \\{([^\\}]*)\\} with \\{([^\\}]*)\\}$" )
	public void executeXMLKeyword( WebDriver webDriver, SuiteContainer sC, ExecutionContextTest eC, String kW, String elementDescriptor, String parameterData ) throws Exception
	{
		executeXMLKeyword(webDriver, sC, eC, kW, elementDescriptor, false, parameterData );
	}
	
	@Then ( "^I trace \\{([^\\}]*)\\} on \\{([^\\}]*)\\} with \\{([^\\}]*)\\}$" )
	public void traceXMLKeyword( WebDriver webDriver, SuiteContainer sC, ExecutionContextTest eC, String kW, String elementDescriptor, String parameterData ) throws Exception
	{
		executeXMLKeyword(webDriver, sC, eC, kW, elementDescriptor, true, parameterData );
	}
	
	public void executeXMLKeyword( WebDriver webDriver, SuiteContainer sC, ExecutionContextTest eC, String kW, String elementDescriptor, boolean trace, String parameterData ) throws Exception
	{
		boolean reportingElement = ( (DeviceWebDriver) webDriver ).isReportingElement();
		try
		{
		( (DeviceWebDriver) webDriver ).setReportingElement( false );
		ElementDescriptor eD = new ElementDescriptor( elementDescriptor );
		KeyWordStep kS = KeyWordStepFactory.instance().createStep( eD.getElementName(), eD.getPageName(), true, kW, null, false, StepFailure.ERROR, false, null, null, null, 0, null, 0, null, null, null, null, null, false, false, null, eD.getSiteName() == null ? PageManager.instance( eC.getxFID() ).getSiteName() : eD.getSiteName() , null, null, null, null, trace, null, null );
		
		Map<String,Object> contextMap = new HashMap<String,Object>( 10 );
		Map<String,Page> pageMap = new HashMap<String,Page>( 10 );
		Map<String,PageData> dataMap = new HashMap<String,PageData>( 10 );
		
		if ( parameterData != null && !parameterData.trim().isEmpty() )
		{
			String[] parameterArray = parameterData.split( "|" );
			for ( String p : parameterArray )
			{
				String[] nameData = p.split( "=" );
				KeyWordParameter kP = new KeyWordParameter( ParameterType.STATIC, nameData.length > 1 ? nameData[ 1 ] : nameData[ 0 ], nameData.length > 1 ? nameData[ 0 ] : null, null );
				kS.addParameter( kP );
			}
		}
		
		if ( !kS.executeStep( PageManager.instance( eC.getxFID() ).createPage( KeyWordPage.class, webDriver), webDriver, contextMap, dataMap, pageMap, sC, eC ) )
			throw new ScriptException( "The Keyword " + kW + " failed to execute" );
		}
		finally
		{
			( (DeviceWebDriver) webDriver ).setReportingElement( reportingElement );
		}
			
	}
}
