package org.xframium.examples.java.page;

import org.xframium.page.Page;

public interface WebHomePage extends Page
{
    @ElementDefinition
    public static final String TOGGLE_BUTTON = "toggleButton";
    
    @ElementDefinition
    public static final String TOGGLE_VALUE = "toggleValue";
    
    @ElementDefinition
    public static final String ACCORDIAN_OPEN = "aOpen";
    
    @ElementDefinition
    public static final String DELETE_BUTTON = "deleteButton";
}
