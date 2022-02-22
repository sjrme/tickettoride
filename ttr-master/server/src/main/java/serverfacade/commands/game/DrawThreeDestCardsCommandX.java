package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.DrawThreeDestCardsCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class DrawThreeDestCardsCommandX extends DrawThreeDestCardsCommand
                                        implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.drawThreeDestCards(username, gameName);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
