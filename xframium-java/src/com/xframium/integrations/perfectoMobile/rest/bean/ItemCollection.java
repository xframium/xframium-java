package com.xframium.integrations.perfectoMobile.rest.bean;

import java.util.ArrayList;
import java.util.List;
import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class ItemCollection.
 */
@BeanDescriptor( beanName="response" )
public class ItemCollection extends AbstractBean
{
    
    /** The item list. */
    @FieldCollection( fieldElement=Item.class, fieldPath="items" )
    private List<Item> itemList = new ArrayList<Item>( 10 );

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ItemCollection [itemList=" + itemList + "]";
    }

    /**
     * Gets the item list.
     *
     * @return the item list
     */
    public List<Item> getItemList()
    {
        return itemList;
    }
	
}
