package org.xframium.page.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class PageDataContainer
{
    private String recordType;
    private boolean lockRecords;
    private Deque<PageData> recordList;
    private List<String> fieldNames = null;
    
    public PageDataContainer( String recordType, boolean lockRecords )
    {
        this.recordType = recordType;
        this.lockRecords = lockRecords;
        if ( lockRecords )
            this.recordList = new LinkedBlockingDeque<PageData>();
        else
            this.recordList = new LinkedList<PageData>();
    }
    
    
    
    public String getRecordType()
    {
        return recordType;
    }
    public void setRecordType( String recordType )
    {
        this.recordType = recordType;
    }
    public boolean isLockRecords()
    {
        return lockRecords;
    }
    public void setLockRecords( boolean lockRecords )
    {
        this.lockRecords = lockRecords;
    }
    public Deque<PageData> getRecordList()
    {
        return recordList;
    }
    public void setRecordList( Deque<PageData> recordList )
    {
        this.recordList = recordList;
    }
    
    public void addRecord( PageData pageData )
    {
        recordList.add( pageData );
        if ( fieldNames == null )
            fieldNames = Arrays.asList( pageData.getFieldNames() );
    }
    
    
}
