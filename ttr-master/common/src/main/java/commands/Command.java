package commands;

import java.io.Serializable;

public class Command implements Serializable
{
    protected String type;
    protected String username;
    /**
     * A sessionID is used by the server to keep track of clients who have not yet been logged into
     * the server successfully.
     */
    protected String sessionID;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }
}
