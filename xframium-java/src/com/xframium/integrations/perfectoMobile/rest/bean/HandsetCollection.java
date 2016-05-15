package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class HandsetCollection.
 */
@BeanDescriptor( beanName="handsets" )
public class HandsetCollection extends AbstractBean
{
	
	/** The handset list. */
	@FieldCollection( fieldElement=Handset.class, fieldPath="" )
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
