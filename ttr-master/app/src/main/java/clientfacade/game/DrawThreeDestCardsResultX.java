package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.DrawThreeDestCardsResult;
import interfaces.IResultX;

public class DrawThreeDestCardsResultX extends DrawThreeDestCardsResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.drawDestCards(super.username, super.destCards);
    }
}
