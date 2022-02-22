package clientfacade.game;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import results.game.ReplaceFaceUpCardsResult;

public class ReplaceFaceUpCardsResultX extends ReplaceFaceUpCardsResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.replaceFaceUpCards(super.trainCards, super.faceUpDifferences);
    }
}
