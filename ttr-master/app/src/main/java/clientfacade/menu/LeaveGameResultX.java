package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.LeaveGameResult;
import interfaces.IResultX;

public class LeaveGameResultX extends LeaveGameResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.leaveGame(super.username, super.gameName);
    }
}
