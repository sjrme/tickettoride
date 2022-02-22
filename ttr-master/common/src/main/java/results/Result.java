package results;

import java.io.Serializable;

public class Result implements Serializable{
    protected String type;
    protected String username;

    protected Result(){}
    public Result(String type, String username) {
        this.type = type;
        this.username = username;
    }

    public String getType()
    {
        return type;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
