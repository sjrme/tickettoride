package clientfacade.game;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import results.game.FinalRoundResult;


public class FinalRoundResultX extends FinalRoundResult implements IResultX {

    @Override
    public void execute(){
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.finalRound(playerToEndOn);
    }
}
