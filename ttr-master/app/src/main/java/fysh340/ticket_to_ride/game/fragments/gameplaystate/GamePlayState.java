package fysh340.ticket_to_ride.game.fragments.gameplaystate;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.Observer;

/**
 * The game play state for the deck presenter
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-02
 */
public interface GamePlayState {

    void drawThreeDestCards(String username, String gameName);

    void returnDestCards(String username, String gameName, int destCards);

    void drawTrainCardFromDeck(String username, String gameName);

    void drawTrainCardFaceUp(String username, String gameName, int index);

    void claimRoute(String username, String gameName, int routeID, List<Integer> trainCards);

    void claimRoute(String username, String gameName, int routeID, AppCompatActivity context);

}
