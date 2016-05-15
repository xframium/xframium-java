package com.xframium.page.keyWord.step.spi;

import java.lang.reflect.Method;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import com.xframium.page.Page;
import com.xframium.page.PageManager;
import com.xframium.page.data.PageData;
import com.xframium.page.keyWord.step.AbstractKeyWordStep;

// TODO: Auto-generated Javadoc
/**
 * <b>Keyword(s):</b> <code>FUNCTION</code><br>
 * The Function keyword will locate a Java function of the page object and execute using the parameter passed in.  For Functions to work
 * the Page object needs to be defined using the Java Page interface and Page implementation.   <br><br>
 * <b>Attributes:</b> Attributes defined here are changes to the base attribute contract
 * <ul>
 * <li><i>name</i>: In this context, name is the Java function
 * </ul><br><br>
 * <b>Parameters:</b> Parameters are passed directly to the function.  The underlying code will do its best to match parameter positions with
 * their appropriate values using reflection<br>
 * <i>Extraction Only</i><br>
 * <ul>
 * <li>Data Override: The specifies a single data override in the format of to=from</li>
 * </ul>
 * <br><b>Example(s): </b><ul>
 * <li> This example will call a function name loginUser on the LoginPage object<br>
 * {@literal <step name="loginUser" type="FUNCTION" page="LoginPage" /> }<br>
 * </li>
 * </ul>
 */
public class KWSFunction extends AbstractKeyWordStep
{

	/* (non-Javadoc)
	 * @see com.perfectoMobile.page.keyWord.step.AbstractKeyWordStep#_executeStep(com.perfectoMobile.page.Page, org.openqa.selenium.WebDriver, java.util.Map, java.util.Map)
	 */
	@Override
	public boolean _executeStep( Page pageObject, WebDriver webDriver, Map<String, Object> contextMap, Map<String, PageData> dataMap, Map<String, Page> pageMap )
	{
		if ( pageObject == null )
			throw new IllegalStateException( "Page Object was not defined" );
		try
		{
			Object[] parameterArray = getParameters( contextMap, dataMap );
			Method method = findMethod( pageObject.getClass(), getName(), parameterArray );
			method.invoke( pageObject, parameterArray );
			return true;
		}
		catch( Exception e )
		{
			throw new IllegalStateException( "Function Call Failed ", e );
		}
	}

}
