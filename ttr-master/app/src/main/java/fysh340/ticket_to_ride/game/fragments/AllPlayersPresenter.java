package fysh340.ticket_to_ride.game.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import clientfacade.ClientFacade;
import fysh340.ticket_to_ride.R;
import interfaces.Observer;
import model.AbstractPlayer;
import model.Game;
import model.TrainCard;


public class AllPlayersPresenter extends Fragment implements Observer {
    private Game mGame = Game.getGameInstance();
    private MyPlayerListAdapter mAdapter = new MyPlayerListAdapter(mGame.getVisiblePlayerInformation());

    public AllPlayersPresenter(){
        mGame.register(this);
    }
    //if a player has changed the player data will update
    @Override
    public void update() {
        if (mGame.aPlayerHasChanged()) {
            mGame.aPlayerHasChanged(false);
            mAdapter.swapData(mGame.getVisiblePlayerInformation());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MyPlayerListAdapter( mGame.getVisiblePlayerInformation());
        setRetainInstance(true);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mGame.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player_data, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        RecyclerView mRV = (RecyclerView) v.findViewById(R.id.player_list);
        mRV.setLayoutManager(layoutManager);
//        Button routine = (Button) v.findViewById(R.id.routine);
        mRV.setAdapter(mAdapter);
        return v;
    }
  /*      routine.setOnClickListener(new View.OnClickListener() {
            private int i = 0;

 /*           @Override
            public void onClick(View view) {
                final ClientFacade cf=new ClientFacade();

                i++;
                AbstractPlayer otherPlayer = mGame.getVisiblePlayerInformation().get(1);
                String otherPlayerUsername = otherPlayer.getMyUsername();
                switch (i) {

                    case (1):
                        //other player draws dest cards
                        Toast.makeText(getActivity(), mGame.getVisiblePlayerInformation().get(1).getMyUsername() + " draws 3 destination cards", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " draws 3 destination cards", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards(), otherPlayer.getNumDestCard() + 3, otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, -1);
                        break;
                    case (2):
                        //other player returns a dest card
                        Toast.makeText(getActivity(), otherPlayerUsername + " returns a destination card", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " returns a destination card", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards(), otherPlayer.getNumDestCard() - 1, otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, -1);
                        break;
                    case (3):
                        //Other player draws a train card from the deck
                        Toast.makeText(getActivity(), otherPlayerUsername + " draws a train card from deck", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " draws a train card from the deck", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards() + 1, otherPlayer.getNumDestCard(), otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, -1);
                        break;
                    case(4):
                        //Other player draws another train card from the deck
                        Toast.makeText(getActivity(), otherPlayerUsername + " draws a train card from deck", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " draws a train card from the deck", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards() + 1, otherPlayer.getNumDestCard(), otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, -1);
                        break;
                    case(5):
                        //Other player draws a train card from the face up
                        List<Integer> trainCardInt0 = mGame.getFaceUpCards();
                        TrainCard trainCard0 = TrainCard.getTrainCard(trainCardInt0.get(0));
                        Toast.makeText(getActivity(), otherPlayerUsername + " draws a train card from face up", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " draws a " + trainCard0.getPrettyname() + " card from face up", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards() + 1, otherPlayer.getNumDestCard(), otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, 0);
                        break;
                    case(6):
                        //Other player draws a train card from the face up
                        List<Integer> trainCardInt3 = mGame.getFaceUpCards();
                        TrainCard trainCard3 = TrainCard.getTrainCard(trainCardInt3.get(3));
                        Toast.makeText(getActivity(), otherPlayerUsername + " draws a train card from face up", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " draws a " + trainCard3.getPrettyname() + " card from face up", otherPlayer.getNumTrains(),
                                otherPlayer.getNumCards() + 1, otherPlayer.getNumDestCard(), otherPlayer.getNumRoutes(), otherPlayer.getScore(), 0, 3);
                        break;
                    case(7):
                        //Other player claim a route 50
                        Toast.makeText(getActivity(), otherPlayerUsername +" claims route 50", Toast.LENGTH_SHORT).show();
                        cf.addHistory(otherPlayerUsername, otherPlayerUsername + " claimed route 50", otherPlayer.getNumTrains() - 5,
                                otherPlayer.getNumCards() - 5, otherPlayer.getNumDestCard(), otherPlayer.getNumRoutes() + 1, otherPlayer.getScore() + 10, 50, -1);
                        break;
                    default:
                        i = 0;
                        break;
                }
            }
        });
        return v;
    }
*/

    private class MyPlayerListAdapter extends RecyclerView.Adapter<AllPlayersPresenter.MyPlayerListAdapter.ViewHolder> {
        private List<AbstractPlayer> allPlayers = new ArrayList<>();

        private MyPlayerListAdapter(List<AbstractPlayer> newList){
            allPlayers = newList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemUsername;
            TextView itemRoutes;
            TextView itemCards;
            TextView itemTrains;
            TextView itemScore;
            View myView;
            TextView itemDCards;
            RelativeLayout player;

            ViewHolder(View view){
                super(view);
                itemUsername = (TextView) view.findViewById(R.id.username);
                itemRoutes = (TextView) view.findViewById(R.id.routesNum);
                itemCards = (TextView) view.findViewById(R.id.cardsNum);
                itemTrains = (TextView) view.findViewById(R.id.trainsNum);
                itemScore = (TextView) view.findViewById(R.id.scoreNum);
                itemDCards = (TextView) view.findViewById(R.id.dcardsNum);
                player=(RelativeLayout) view.findViewById((R.id.playerView));
                myView = view;
            }
        }

        void swapData(List<AbstractPlayer> newGameList){
            allPlayers = newGameList;
            notifyDataSetChanged();
        }

        @Override
        public AllPlayersPresenter.MyPlayerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_player_item_view, parent, false);
            return new AllPlayersPresenter.MyPlayerListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AllPlayersPresenter.MyPlayerListAdapter.ViewHolder holder, final int position) {
            AbstractPlayer myPlayer = allPlayers.get(position);

            String playerName = myPlayer.getMyUsername();
            int myTrains = myPlayer.getNumTrains();
            int myCards = myPlayer.getNumCards();
            int myRoutes = myPlayer.getNumRoutes();
            int myScore = myPlayer.getScore();
            //int color = myPlayer.getColorInt();
            //int playerColors[] = getContext().getResources().getIntArray(R.array.player_colors);
            //int myColor = playerColors[color];

            int color = myPlayer.getColor();
            int myColor = ContextCompat.getColor(getContext(), color);
            int myDCards = myPlayer.getNumDestCard();

            holder.itemUsername.setText(playerName);
            holder.itemTrains.setText(String.valueOf(myTrains));
            holder.itemCards.setText(String.valueOf(myCards));
            holder.itemRoutes.setText(String.valueOf(myRoutes));
            holder.itemScore.setText(String.valueOf(myScore));
            holder.itemUsername.setBackgroundColor(myColor);
            holder.itemDCards.setText(String.valueOf(myDCards));
            if(myPlayer.isMyTurn())
            {
                holder.player.setBackgroundResource(R.drawable.highlight_border);
            }
            else
            {
                holder.player.setBackgroundResource(R.drawable.customborder);
            }

        }

        @Override
        public int getItemCount() {
            return allPlayers.size();
        }

    }
}
