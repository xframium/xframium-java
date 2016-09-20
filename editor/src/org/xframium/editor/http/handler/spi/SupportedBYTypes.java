package org.xframium.editor.http.handler.spi;

import org.xframium.editor.http.handler.XOLHandler;
import org.xframium.page.BY;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings("restriction")
public class SupportedBYTypes extends XOLHandler
{

	@Override
	protected byte[] _handle(HttpExchange httpExchange)
	{
		
		return SerializationManager.instance().toByteArray( BY.ID.getSupported() );
	}
}
