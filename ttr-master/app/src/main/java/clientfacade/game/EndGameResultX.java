package clientfacade.game;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import results.game.EndGameResult;


public class EndGameResultX extends EndGameResult implements IResultX {

    @Override
    public void execute(){
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.endGame(players, numRoutesClaimed, pointsFromRoutes, destCardPtsAdded,
                destCardPtsSubtracted, totalPoints, ownsLongestRoute);
    }
}
