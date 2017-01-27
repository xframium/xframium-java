package org.xframium.utility;

import java.util.Date;
import java.util.List;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class DateUtility
{

    private static final DateUtility singleton = new DateUtility();

    private DateUtility()
    {

    }

    public static DateUtility instance()
    {
        return singleton;
    }

    public Date parseDate( String date )
    {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse( date );

        for ( DateGroup group : groups )
        {
            List<Date> dates = group.getDates();
            return dates.get( 0 );
        }

        return null;

    }
}
