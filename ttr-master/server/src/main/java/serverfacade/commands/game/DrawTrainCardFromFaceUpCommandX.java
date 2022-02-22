package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.DrawTrainCardFromFaceUpCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class DrawTrainCardFromFaceUpCommandX extends DrawTrainCardFromFaceUpCommand
                                                implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.drawTrainCardFromFaceUp(username, gameName, index);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
