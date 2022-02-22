package serverfacade.commands.menu;

import java.io.Serializable;

import commands.menu.CreateGameCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class CreateGameCommandX extends CreateGameCommand implements ICommandX,Serializable {
    private ServerFacade serverFacade;

    public CreateGameCommandX(String username, String gameName, int numPlayers){
        super(username, gameName, numPlayers);
    }

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.createGame(username, gameName, numPlayers);
    }

    //This would save an UnstartedGame's data.
    @Override
    public void addToDatabase() {}
}
