package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.ReturnDestCardsCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class ReturnDestCardsCommandX extends ReturnDestCardsCommand
                                        implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.returnDestCards(username, gameName, destCard);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
