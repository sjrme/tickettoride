package database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import commands.Command;
import model.StartedGame;
import model.User;

public interface IDatabase {

    /**
     * Clears all persistent data in the database.
     * @return True if cleared, false if failed.
     */
    public boolean clearDatabase();

    /**
     * Called every a game mutating command is accepted by the server.
     * @param gameName The name of the game to add the command to.
     * @param nextCommand The Command object to be saved.
     * @return Returns true if adding the new command has caused the list of commands to reach the limit.
     *         Otherwise returns false.
     */
    boolean saveCommandToDatabase(String gameName, Command nextCommand);

    /**
     * Called when the server accepts a StartGameCommand.
     * @param myGame The new StartedGame to insert into the database.
     */
    public void saveNewStartedGameToDatabase(StartedGame myGame);

    /**
     * Called when the saveCommandToDatabase method returns true, meaning that the database wants to
     * save the entire state of a StarteGame.
     * @param myGame The StartedGame to overwrite a previously stored game of the same name.
     * @pre The database has reached it's command limit and has a full command list.
     * @post 1) Saved the entire state of the StartedGame.
     *       2) Cleared the database's list of commands for that game.
     */
   public void updateStartedGameInDatabase(StartedGame myGame);

    /**
     * Called when the server accepts a RegisterCommand.
     * @param myNewUser The new User to insert into the database.
     */
    public void saveNewUserToDatabase(User myNewUser);

    /**
     * Called on server startup.
     * @return A set of all users saved in the database.
     */
    public Set<User> loadUsersFromDatabase();

    /**
     * Called on server startup.
     * @return A map of all StartedGames saved in the database.
     *         Key: gameName
     *         Value: StartedGame
     */
    public Map<String, StartedGame> loadStartedGamesFromDatabase();

    /**
     * Called on server startup.
     * @return A map of all outstanding commands for each game.
     *         Key: gameName
     *         Value: List&ltCommand&gt
     */
    public Map<String, List<Command>> loadOutstandingCommandsFromDatabase();

    /*
    Kalan thinks that saving UnstartedGames into the database is not reasonable. If they are saved
    without usernames in them, then they are empty and should be deleted. If they are saved with
    usernames in it, then players can't join a game that they are already a member of. This could be
    changed in the ServerModel to where if the person joining a game is already in that game, the
    server accepts it. But honestly, he thinks this is pointless work and doesn't think any
    real world industries would make the choice to save this sort of object persistently.
     */
    //abstract Map<String, UnstartedGame> loadUnstartedGamesFromDatabase();
    //abstract void saveUnstartedGameToDatabase(UnstartedGame myGame);

}
