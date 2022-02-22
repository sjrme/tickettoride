package model;

import java.util.ArrayList;
import java.util.List;

/**
 *  <h1>Game Model</h1>
 *  Reflects game and player database table rows for passing data between the facade and data
 *  access objects.
 *
 *  @author     Nathan Finch
 *  @since      7/10/2017
 */
public class Game
{

    //Data Members

    private String mID;
    private int mNumberOfPlayers;
    private List<User> mPlayers;
    private boolean mStarted;

    //Constructors

    public Game(String gameID, int numberOfPlayers, List<User> players, boolean started)
    {

        setID(gameID);

        setNumberofPlayers(numberOfPlayers);

        setPlayers(players);

        setStarted(started);

    }

    //Utility Methods

    /**
     *  Create List
     *  Helps initialize the game object by creating a list with the game creator already in it
     *
     *  @param          creator         The game creator user object
     *
     *  @return                         The list of users playing, initially just the creator
     */
    private List<User> createList(User creator)
    {

        List<User> starting_list = new ArrayList<>();
        starting_list.add(creator);

        return starting_list;

    }

    //Access Methods

    public String getID()
    {

        return mID;

    }

    public int getNumberOfPlayers()
    {

        return mNumberOfPlayers;

    }

    public List<User> getPlayers()
    {

        return mPlayers;

    }

    public boolean hasStarted()
    {

        return mStarted;

    }

    //Mutator Methods

    public void setID(String ID)
    {

        mID = ID;

    }

    public void setNumberofPlayers(int numberOfPlayers)
    {

        mNumberOfPlayers = numberOfPlayers;

    }

    public void setPlayers(List<User> players)
    {

        mPlayers = players;

    }

    public void setStarted(boolean started)
    {

        mStarted = started;

    }

}
