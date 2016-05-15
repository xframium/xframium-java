/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.xframium.page.factory;

import com.xframium.page.Page;


// TODO: Auto-generated Javadoc
/**
 * A factory for creating Page objects.
 *
 * @author ageary
 */
public interface PageFactory
{
    
    /**
     * Creates a new Page object.
     *
     * @param pageInterface the page interface
     * @param webDriver the web driver
     * @return the page
     */
    Page createPage( Class<Page> pageInterface, Object webDriver );
}
