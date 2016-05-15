package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ExecutionCollection.
 */
@BeanDescriptor( beanName="response" )
public class ExecutionCollection extends AbstractBean
{
	
	/** The handset list. */
	@FieldCollection( fieldElement=Handset.class, fieldPath="handsets" )
	private List<Handset> handsetList = new ArrayList<Handset>( 10 );
	
	/**
	 * Gets the handset list.
	 *
	 * @return the handset list
	 */
	public List<Handset> getHandsetList()
	{
		return handsetList;
	}
}
