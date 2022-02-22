package interfaces;

import model.StartedGame;
import model.User;

public interface IServerModel {

    StartedGame getStartedGame(String gameName);
    User getUser(String username);
}
