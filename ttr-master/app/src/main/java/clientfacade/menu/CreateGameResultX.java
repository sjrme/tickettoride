package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.CreateGameResult;
import interfaces.IResultX;

public class CreateGameResultX extends CreateGameResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.createGame(super.username, super.gameName);
    }
}
