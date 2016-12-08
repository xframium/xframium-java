package org.xframium.email.receive;

public class MessageWrapper
{
    private int messageCount;
    private String from;
    private String subject;
    private String body;
    private String mimeType;
    
    
    
    public MessageWrapper( int messageCount, String from, String subject, String body, String mimeType )
    {
        super();
        this.messageCount = messageCount;
        this.from = from;
        this.subject = subject;
        this.body = body;
        this.mimeType = mimeType;
    }
    public int getMessageCount()
    {
        return messageCount;
    }
    public void setMessageCount( int messageCount )
    {
        this.messageCount = messageCount;
    }
    public String getFrom()
    {
        return from;
    }
    public void setFrom( String from )
    {
        this.from = from;
    }
    public String getSubject()
    {
        return subject;
    }
    public void setSubject( String subject )
    {
        this.subject = subject;
    }
    public String getBody()
    {
        return body;
    }
    public void setBody( String body )
    {
        this.body = body;
    }
    public String getMimeType()
    {
        return mimeType;
    }
    public void setMimeType( String mimeType )
    {
        this.mimeType = mimeType;
    }
    
   
    
}
