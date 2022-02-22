package fysh340.ticket_to_ride.game.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.MasterGamePresenter;
import interfaces.Observer;
import model.ClientModel;
import model.Game;


public class TrainCardPresenter extends Fragment implements Observer{
    private ClientModel mClientModel = ClientModel.getMyClientModel();
    private Game mGame = Game.getGameInstance();
    private List<TextView> trainCardTextViews;
//if the players train cards have changed the view is updated
    @Override
    public void update() {

        if (mGame.iHaveDifferentTrainCards()){
            mGame.iHaveDifferentTrainCards(false);
            updateMyTrainCards(mGame.getMyself().getMyTrainCardsAsIntArray());
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGame.register(this);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player_train_cards, container, false);

        LinearLayout mLinearLayout = (LinearLayout) v.findViewById(R.id.train_card_linear_layout);
        createTrainCardTextViewList(mLinearLayout);

        Button playerCardSwitchButton = (Button) v.findViewById(R.id.player_switch_cards_button);
        playerCardSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MasterGamePresenter)getActivity()).switchPlayerCards();
            }
        });
        updateMyTrainCards(mGame.getMyself().getMyTrainCardsAsIntArray());
        setButtons();
        return v;
    }

    public void updateMyTrainCards(int newTrainCardArray[]){
        for (int i = 0; i < trainCardTextViews.size(); i++){
            trainCardTextViews.get(i).setText(String.valueOf(newTrainCardArray[i]));
        }
    }

    private void createTrainCardTextViewList(LinearLayout myLinearLayout){
        this.trainCardTextViews = new ArrayList<>();
        for (int i = 0; i < myLinearLayout.getChildCount(); i++){
            View myView = myLinearLayout.getChildAt(i);

            if (myView.getClass() == AppCompatTextView.class)
                this.trainCardTextViews.add((TextView) myLinearLayout.getChildAt(i));
        }
    }
    public void setButtons()
    {

    }

}
