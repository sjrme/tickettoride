package fysh340.ticket_to_ride.game.fragments.gameplaystate;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import model.Game;
import model.TrainCard;
import serverproxy.ServerProxy;

/**
 * The state when the player has drawn a train card
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-02
 */
public class DrawSecondTrainCardState implements GamePlayState {

    private ServerProxy mServerProxy = new ServerProxy();

    public DrawSecondTrainCardState(){
        for (int trainCardType : Game.getGameInstance().getFaceUpCards()){
            if (TrainCard.getTrainCard(trainCardType) != TrainCard.WILD){
                return;
            }
        }
        Game.getGameInstance().getServerError().setMessage("There are no other cards to draw. Turn ended.");
        ClientState.INSTANCE.setState(new NotMyTurnState());
    }

    @Override
    public void drawThreeDestCards(String username, String gameName) {
        Game.getGameInstance().getServerError().setMessage("You can only draw your second train card");
    }

    @Override
    public void returnDestCards(String username, String gameName, int destCards) {
        Game.getGameInstance().getServerError().setMessage("You can only draw your second train card");
    }

    @Override
    public void drawTrainCardFromDeck(String username, String gameName) {
        mServerProxy.drawTrainCardFromDeck(username, gameName );
    }

    @Override
    public void drawTrainCardFaceUp(String username, String gameName, int index) {
        if(TrainCard.getTrainCard(Game.getGameInstance().getFaceUpCards().get(index))==TrainCard.WILD)
        {
            Game.getGameInstance().getServerError().setMessage("You can't choose a wild card!");
        }
        else {
            mServerProxy.drawTrainCardFromFaceUp(username, gameName, index);
        }
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, List<Integer> trainCards) {
        Game.getGameInstance().getServerError().setMessage("You can only draw your second train card");
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, AppCompatActivity context) {
        Game.getGameInstance().getServerError().setMessage("You can only draw your second train card");
    }
}
