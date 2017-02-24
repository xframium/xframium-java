package org.xframium.integrations.alm.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ALMEntity
{
    private static SimpleDateFormat dateOnly = new SimpleDateFormat( "yyyy-MM-dd" );
    
    @Retention(RetentionPolicy.RUNTIME)
    protected @interface ALMField
    {
        
        /**
         * Site name.
         *
         * @return the string
         */
        String PhysicalName();
        String Name();
        String Label();
    }
    
    private String entityType;
    protected ALMEntity( String entityType )
    {
        this.entityType = entityType;
    }
    
    
    
    public String getEntityType()
    {
        return entityType;
    }

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
