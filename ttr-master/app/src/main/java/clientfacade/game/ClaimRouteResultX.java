package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.ClaimRouteResult;
import interfaces.IResultX;

public class ClaimRouteResultX extends ClaimRouteResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.claimRoute(super.username, super.routeID);
    }
}
