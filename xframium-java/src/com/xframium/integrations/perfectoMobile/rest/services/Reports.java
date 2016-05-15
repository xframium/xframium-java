package com.xframium.integrations.perfectoMobile.rest.services;

import com.xframium.integrations.perfectoMobile.rest.bean.ExecutionReport;
import com.xframium.integrations.perfectoMobile.rest.services.PerfectoService.ServiceDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Interface Reports.
 */
@ServiceDescriptor( serviceName="reports" )
public interface Reports extends PerfectoService
{
	
	/**
	 * The Enum ReportFormat.
	 */
	public enum ReportFormat
	{
		
		/** The xml. */
		xml,
		
		/** The csv. */
		csv,
		
		/** The pdf. */
		pdf,
		
		/** The html. */
		html;
	}
	
	/**
	 * Download.
	 *
	 * @param reportKey the report key
	 * @param format the format
	 * @return the execution report
	 */
	@Operation( operationName="download" )
	public ExecutionReport download( @ResourceID String reportKey, @NameOverride( name="format" )ReportFormat format );
	
	@Operation( operationName="log" )
    public byte[] download( @ResourceID String reportKey, @Parameter( name="attachment") String attachment, @Parameter( name="admin") boolean admin );
}
