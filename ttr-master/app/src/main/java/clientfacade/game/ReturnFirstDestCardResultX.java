package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.ReturnFirstDestCardResult;
import interfaces.IResultX;

public class ReturnFirstDestCardResultX extends ReturnFirstDestCardResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.returnFirstDestCards(super.username, super.cardReturned);
    }
}
