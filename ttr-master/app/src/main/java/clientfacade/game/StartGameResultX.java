package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.StartGameResult;
import interfaces.IResultX;

public class StartGameResultX extends StartGameResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.startGame(super.username, super.gameName, super.playerNames, super.destCards,
                super.trainCards, super.faceUpCards);
    }
}
