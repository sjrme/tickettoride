package fysh340.ticket_to_ride.game.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.ArrayList;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.ClientState;
import model.Game;
import model.Route;
import model.TrainCard;
import static model.TrainCard.WILD;

public class ColorChoiceDialog extends DialogFragment {
    Game mGame = Game.getGameInstance();
    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose a card color")
                .setItems(R.array.mcolors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selectedIndex) {
                        ArrayList<Integer> cards = new ArrayList<>();
                        Route route = Route.getRouteByID(mGame.getCurrentlySelectedRouteID());
                        TrainCard myColor = null;

                        switch(selectedIndex) {
                            case (0):
                                myColor = TrainCard.getTrainCard(0);
                                break;
                            case (1):
                                myColor = TrainCard.getTrainCard(1);
                                break;
                            case (2):
                                myColor = TrainCard.getTrainCard(2);
                                break;
                            case (3):
                                myColor = TrainCard.getTrainCard(3);
                                break;
                            case (4):
                                myColor = TrainCard.getTrainCard(4);
                                break;
                            case (5):
                                myColor = TrainCard.getTrainCard(5);
                                break;
                            case (6):
                                myColor = TrainCard.getTrainCard(6);
                                break;
                            case (7):
                                myColor = TrainCard.getTrainCard(7);
                                break;
                            case (8):
                                myColor = TrainCard.getTrainCard(8);
                                break;
                        }

                        int colored = mGame.getMyself().getNumOfTypeCards(myColor);
                        int wild = mGame.getMyself().getNumOfTypeCards(WILD);
                        int cardsLeft = route.getLength();

                        if((colored+wild>=route.getLength()&&myColor!=WILD)||(wild>=route.getLength())) {
                            for(int i=0;i<colored;i++) {
                                if(cardsLeft>0) {
                                    cardsLeft--;
                                    cards.add(TrainCard.getTrainCardKey(myColor));
                                }
                            }
                            while(cardsLeft > 0) {
                                cards.add(TrainCard.getTrainCardKey(WILD));
                                cardsLeft--;
                            }
                            mGame.setCardsToDiscard(cards);
                            //mSP.claimRoute(mGame.getMyself().getMyUsername(), mGame.getMyGameName(),
                            //mGame.getCurrentlySelectedRouteID(), cards);
                            ClientState.INSTANCE.getState().claimRoute(mGame.getMyself().getMyUsername(),
                                    mGame.getMyGameName(), mGame.getCurrentlySelectedRouteID(), cards);
                            //Toast.makeText(getActivity(), "Route Claimed Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getActivity(), "You don't have enough cards!", Toast.LENGTH_SHORT).show();
                    }
                });

        //ideally would use the below code but does not work in this situation due to fragment/dialog nesting
        //builder.setCancelable(true);

        Dialog myDialog = builder.create();
        myDialog.setCanceledOnTouchOutside(true);   //closes the dialog if user touches outside of it
        return builder.create();
    }
}