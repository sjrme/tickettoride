package clientfacade.game;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import model.GameData;
import results.game.RejoinResult;

public class RejoinResultX extends RejoinResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.reJoinGame(super.username, super.gameData);
    }
}
