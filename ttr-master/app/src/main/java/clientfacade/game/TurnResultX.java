package clientfacade.game;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import results.game.TurnResult;

public class TurnResultX extends TurnResult implements IResultX {

    @Override
    public void execute(){
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.turn(username);
    }
}
