package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.ReturnDestCardsResult;
import interfaces.IResultX;

public class ReturnDestCardsResultX extends ReturnDestCardsResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.returnDestCards(super.username, super.destCards);
    }
}
