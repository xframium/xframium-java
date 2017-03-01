/*******************************************************************************
 * xFramium
 *
 * Copyright 2017 by Moreland Labs LTD (http://www.morelandlabs.com)
 *
 * Some open source application is free software: you can redistribute 
 * it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *  
 * Some open source application is distributed in the hope that it will 
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with xFramium.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPL-3.0+ <http://spdx.org/licenses/GPL-3.0+>
 *******************************************************************************/
package org.xframium.integrations.alm.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class ALMEntity.
 */
public abstract class ALMEntity
{
    
    /** The date only. */
    private static SimpleDateFormat dateOnly = new SimpleDateFormat( "yyyy-MM-dd" );
    
    /**
     * The Interface ALMField.
     */
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface ALMField
    {
        
        /**
         * Site name.
         *
         * @return the string
         */
        String PhysicalName();
        
        /**
         * Name.
         *
         * @return the string
         */
        String Name();
        
        /**
         * Label.
         *
         * @return the string
         */
        String Label();
    }
    
    /** The entity type. */
    private String entityType;
    
    /**
     * Instantiates a new ALM entity.
     *
     * @param entityType the entity type
     */
    protected ALMEntity( String entityType )
    {
        this.entityType = entityType;
    }
    
    
    
    /**
     * Gets the entity type.
     *
     * @return the entity type
     */
    public String getEntityType()
    {
        return entityType;
    }

    /**
     * To xml.
     *
     * @return the string
     * @throws Exception the exception
     */
    public String toXML() throws Exception
    {
        StringBuilder xml = new StringBuilder();
        Field[] fieldList = getClass().getDeclaredFields();
        
        xml.append( "<Entity Type=\"" ).append( entityType ).append( "\">" );
        xml.append( "<Fields>" );
        
        for ( Field f : fieldList )
        {
            boolean accessibleValue = f.isAccessible();
            f.setAccessible( true );
            ALMField almField = f.getAnnotation( ALMField.class );
            if ( almField != null )
            {
                Object currentValue = f.get( this );
                if ( currentValue != null )
                {
                    xml.append( "<Field Name=\"" ).append( almField.Name() ).append( "\">" );
                    
                    if ( currentValue instanceof String )
                        xml.append( "<Value>" ).append(  currentValue ).append( "</Value>" );
                    else if ( currentValue instanceof Date )
                        xml.append( "<Value>" ).append( dateOnly.format( ( (Date) currentValue ) ) ).append( "</Value>" );
                    
                    xml.append( "</Field>" );
                }
            }
            
            f.setAccessible( accessibleValue );
        }
        
        xml.append( "</Fields>" );
        xml.append( "</Entity>" );
        
        return xml.toString();
        
    }
    
    
}
