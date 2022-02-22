package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.DrawTrainCardFromFaceUpResult;
import interfaces.IResultX;

public class DrawTrainCardFromFaceUpResultX extends DrawTrainCardFromFaceUpResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.drawTrainCardFaceUp(super.username, super.trainCard);
    }
}
