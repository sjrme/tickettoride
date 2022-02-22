package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.RejoinCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class RejoinCommandX extends RejoinCommand implements ICommandX, Serializable {

    @Override
    public boolean execute() {
        ServerFacade serverFacade = new ServerFacade();
        return serverFacade.reJoinGame(super.username, super.gameName);
    }

    //We do not need to store this because the ServerModel does not care if they rejoin.
    @Override
    public void addToDatabase() {}
}
