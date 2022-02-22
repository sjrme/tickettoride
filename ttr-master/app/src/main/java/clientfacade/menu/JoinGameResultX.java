package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.JoinGameResult;
import interfaces.IResultX;

public class JoinGameResultX extends JoinGameResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.joinGame(super.username, super.gameName);
    }
}
