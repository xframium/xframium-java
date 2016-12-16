package org.xframium.container;

import java.util.ArrayList;
import java.util.List;

public class FavoriteContainer
{
    private List<String> favoriteList = new ArrayList<String>( 10 );
    
    public FavoriteContainer( String favorites )
    {
        if ( favorites != null && favorites.trim().length() > 0 )
        {
            String[] favoriteArray = favorites.split( "," );
            
            for ( String fav : favoriteArray )
            {
                favoriteList.add( fav );
            }
        }
    }
    
    public void addFavorite( String keyword )
    {
        favoriteList.add( keyword );
    }
    
    public List<String> getFavorites()
    {
        return favoriteList;
    }
    
    
}
