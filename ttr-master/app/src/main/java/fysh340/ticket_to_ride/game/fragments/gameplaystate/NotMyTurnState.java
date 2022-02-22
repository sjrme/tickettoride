package fysh340.ticket_to_ride.game.fragments.gameplaystate;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;

import model.Game;

/**
 * The state when it is not the player's turn
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-02
 */
public class NotMyTurnState implements GamePlayState {

    @Override
    public void drawThreeDestCards(String username, String gameName) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }

    @Override
    public void returnDestCards(String username, String gameName, int destCards) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }

    @Override
    public void drawTrainCardFromDeck(String username, String gameName) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }

    @Override
    public void drawTrainCardFaceUp(String username, String gameName, int index) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, List<Integer> trainCards) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, AppCompatActivity context) {
        Game.getGameInstance().getServerError().setMessage("It's not your turn");
    }
}
