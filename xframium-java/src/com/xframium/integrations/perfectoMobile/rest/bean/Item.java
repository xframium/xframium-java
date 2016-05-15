package com.xframium.integrations.perfectoMobile.rest.bean;

import com.xframium.integrations.rest.bean.AbstractBean;
import com.xframium.integrations.rest.bean.Bean.BeanDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
@BeanDescriptor( beanName="item" )
public class Item extends AbstractBean
{
    
    /** The text content. */
    @FieldDescriptor ( textContent=true )
    private String textContent;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Item [textContent=" + textContent + "]";
    }

    /**
     * Gets the text context.
     *
     * @return the text context
     */
    public String getTextContext()
    {
        return textContent;
    }
    
    /**
     * Sets the text context.
     *
     * @param val the new text context
     */
    public void setTextContext( String val )
    {
        this.textContent = val;
    }

}
