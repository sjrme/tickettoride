package serverfacade.commands.game;

import java.io.Serializable;

import commands.game.ClaimRouteCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class ClaimRouteCommandX extends ClaimRouteCommand implements ICommandX, Serializable {
    private ServerFacade serverFacade;

    @Override
    public boolean execute() {
        serverFacade = new ServerFacade();
        return serverFacade.claimRoute(username, gameName, routeID, trainCards);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addCommandToDatabase(super.gameName, this);
    }
}
