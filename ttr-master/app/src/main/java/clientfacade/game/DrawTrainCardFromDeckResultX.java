package clientfacade.game;


import clientfacade.ClientFacade;
import results.game.DrawTrainCardFromDeckResult;
import interfaces.IResultX;

public class DrawTrainCardFromDeckResultX extends DrawTrainCardFromDeckResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.drawTrainCardDeck(super.username, super.trainCard);
    }
}
