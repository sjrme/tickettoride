package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.StartGameCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class StartGameCommandX extends StartGameCommand implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.startGame(gameName, username);
    }

    //Save the newly created StartedGame to the database
    @Override
    public void addToDatabase() {
        serverFacade.addNewStartedGameToDatabase(super.gameName);
    }
}
