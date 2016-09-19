package org.xframium.editor.http.handler.spi;

import org.xframium.editor.http.handler.XOLHandler;
import org.xframium.page.PageManager;
import org.xframium.page.element.provider.XMLElementProvider;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class AnalyzePageData extends XOLHandler {

	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{
		PageManager.instance().setSiteName( "mStar" );
		return SerializationManager.instance().toByteArray(new XMLElementProvider("pageElements.xml").getElementTree());
	}
}
