package database;

import java.util.List;
import java.util.Map;
import java.util.Set;

import commands.Command;
import model.StartedGame;
import model.User;

//this class used when no other database is loaded. It does nothing
public class FakeDatabase implements IDatabase{
    private int commandsUntilSave;

    @Override
    public boolean clearDatabase() {
        return false;
    }

    FakeDatabase(int commandsUntilSave){
        this.commandsUntilSave = commandsUntilSave;
    }
    FakeDatabase(){
    }

    @Override
    public boolean saveCommandToDatabase(String gameName, Command nextCommand) {
        return false;
    }

    @Override
    public void saveNewStartedGameToDatabase(StartedGame myGame) {}

    @Override
    public void updateStartedGameInDatabase(StartedGame myGame) {}

    @Override
    public void saveNewUserToDatabase(User myNewUser) {}

    @Override
    public Set<User> loadUsersFromDatabase() {
        return null;
    }

    @Override
    public Map<String, StartedGame> loadStartedGamesFromDatabase() {
        return null;
    }

    @Override
    public Map<String, List<Command>> loadOutstandingCommandsFromDatabase() {
        return null;
    }
}
