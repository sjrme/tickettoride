package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.ReturnFirstDestCardCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class ReturnFirstDestCardCommandX extends ReturnFirstDestCardCommand
                                            implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.returnDestCards(super.username, super.gameName, super.destCard);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
