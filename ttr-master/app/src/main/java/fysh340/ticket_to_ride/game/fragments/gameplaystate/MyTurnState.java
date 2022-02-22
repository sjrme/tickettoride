package fysh340.ticket_to_ride.game.fragments.gameplaystate;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import fysh340.ticket_to_ride.game.fragments.ColorChoiceDialog;
import model.Game;
import model.Route;
import model.TrainCard;
import serverproxy.ServerProxy;

import static model.TrainCard.WILD;

/**
 * The state when it is the player's turn
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-02
 */
public class MyTurnState implements GamePlayState {

    private Game mGame = Game.getGameInstance();
    private ServerProxy mServerProxy = new ServerProxy();

    @Override
    public void drawThreeDestCards(String username, String gameName) {
        mServerProxy.drawThreeDestCards(username, gameName);
    }

    @Override
    public void returnDestCards(String username, String gameName, int destCards) {
    }

    @Override
    public void drawTrainCardFromDeck(String username, String gameName) {
        mServerProxy.drawTrainCardFromDeck(username, gameName);
    }

    @Override
    public void drawTrainCardFaceUp(String username, String gameName, int index) {
        mServerProxy.drawTrainCardFromFaceUp(username, gameName, index);
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, List<Integer> trainCards) {
        mServerProxy.claimRoute(username, gameName, routeID, trainCards);
    }

    @Override
    public void claimRoute(String username, String gameName, int routeID, AppCompatActivity context) {
        if (ClientState.INSTANCE.getState() instanceof MyTurnState) {
            ArrayList<Integer> cards = new ArrayList<>();
            Route route = Route.getRouteByID(mGame.getCurrentlySelectedRouteID());
            Route sisterRoute;

            if (route.getSisterRouteKey() != -1) {
                sisterRoute = Route.getRouteByID(route.getSisterRouteKey());
            } else {
                sisterRoute = null;
            }

            if (route.isClaimed())
                Toast.makeText(context, "Route Already Claimed", Toast.LENGTH_SHORT).show();
            else if ((route.getLength() > mGame.getMyNumTrains()))
                Toast.makeText(context, "You don't have enough cars!", Toast.LENGTH_SHORT).show();
            else if (sisterRoute != null && sisterRoute.isClaimed() && mGame.getVisiblePlayerInformation().size() < 4)
                Toast.makeText(context, "Double Route already claimed!", Toast.LENGTH_SHORT).show();
            else if (sisterRoute != null && sisterRoute.isClaimed() && sisterRoute.getUser().equals(mGame.getMyUsername()))
                Toast.makeText(context, "You can't claim double routes!", Toast.LENGTH_SHORT).show();


            else {
                if (route.getOriginalColor() == WILD) {
                    ColorChoiceDialog cd = new ColorChoiceDialog();
                    cd.setCancelable(true);
                    cd.show(context.getSupportFragmentManager(), "NoticeDialogFragment");
                } else {
                    int colored = mGame.getMyself().getNumOfTypeCards(route.getOriginalColor());
                    int wild = mGame.getMyself().getNumOfTypeCards(WILD);
                    int cardsLeft = route.getLength();
                    if (colored + wild >= route.getLength()) {
                        for (int i = 0; i < colored; i++) {
                            if (cardsLeft > 0) {
                                cardsLeft--;
                                cards.add(TrainCard.getTrainCardKey(route.getOriginalColor()));
                            }


                        }
                        while (cardsLeft > 0) {
                            cards.add(TrainCard.getTrainCardKey(WILD));
                            cardsLeft--;
                        }
                        mGame.setCardsToDiscard(cards);
                        ClientState.INSTANCE.getState().claimRoute(mGame.getMyself().getMyUsername(), mGame.getMyGameName(),
                                mGame.getCurrentlySelectedRouteID(), cards);
                        //                        Toast.makeText(getActivity(), "Route Claimed Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "You don't have enough cards!", Toast.LENGTH_SHORT).show();
                    }



                }
            }
        } else {
            Toast.makeText(context, "You don't have enough cards", Toast.LENGTH_SHORT).show();
        }
    }
}
