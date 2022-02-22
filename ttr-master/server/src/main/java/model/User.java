package model;

/**
 *  <h1>User Model</h1>
 *  Mirror 'user' database table rows for passing data between the facade and data access objects.
 *
 *  @author     Nathan Finch
 *  @since      7/10/2017
 */
public class User implements java.io.Serializable
{
    //Data Members

    private String mUsername;
    private String mPassword;

    //Constructors

    public User(String username, String password)
    {

        setUsername(username);
        setPassword(password);

    }

    //Access Methods

    public String getUsername()
    {

        return mUsername;

    }

    public String getPassword()
    {

        return mPassword;

    }

    //Mutator Methods

    public void setUsername(String username)
    {

        mUsername = username;

    }

    public void setPassword(String password)
    {

        mPassword = password;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        User comparedUser = (User)obj;

        if (comparedUser.getUsername().equals(this.mUsername)){
            if (comparedUser.getPassword().equals(this.mPassword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.mUsername.hashCode() * this.mPassword.hashCode();
    }
}
