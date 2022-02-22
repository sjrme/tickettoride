package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.ChatCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class ChatCommandX extends ChatCommand implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.sendChatMessage(username, gameName, message);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
