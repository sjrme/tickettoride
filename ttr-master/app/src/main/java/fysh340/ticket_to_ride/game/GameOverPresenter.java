package fysh340.ticket_to_ride.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.menus.MenuLogin;
import model.AbstractPlayer;
import model.ClientModel;
import model.Game;
import okhttp3.WebSocket;
import websocket.ClientWebSocket;

public class GameOverPresenter extends AppCompatActivity {
    RecyclerView recyclerView;
    Game mGame= Game.getGameInstance();
    private mAdapter fAdapter;
    TextView username;
    TextView claimedRoutes;
    TextView claimedRoutesPoints;
    TextView totalPoints;
    TextView destinationsPoints;
    TextView destinationsPointsLost;
    TextView longestRoutePoints;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_presenter);
        TextView winnerUsername = (TextView) findViewById(R.id.winner);
        winnerUsername.setText((mGame.getWinnerUsername() + " WINS"));
        recyclerView = (RecyclerView) findViewById(R.id.final_score_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logout=(Button) findViewById(R.id.logout);
        updateUI();
        setButton();
    }

    @Override
    public void onBackPressed(){
        //do nothing
    }
    private void setButton()
    {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.getGameInstance().reset();
                ClientModel.getMyClientModel().reset();
                WebSocket myWebSocket = ClientWebSocket.getClientWebSocket().getMyWebSocket();
                myWebSocket.close(1000, "LOGOUT");
                logout();
            }
        });
    }
    private void logout()
    {
        Intent intent = new Intent(this, MenuLogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //reset the activity stack
        startActivity(intent); //proceed to login screen
    }
    private void updateUI() {
        recyclerView.removeAllViewsInLayout();
        List<AbstractPlayer> players = mGame.getVisiblePlayerInformation();
        fAdapter = new mAdapter(players);
        recyclerView.setAdapter(fAdapter);

    }
    private class mHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public mHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.game_over_item_view, parent, false));
            username= (TextView) itemView.findViewById(R.id.username);
            claimedRoutes= (TextView) itemView.findViewById(R.id.routes_claime);
            claimedRoutesPoints= (TextView) itemView.findViewById(R.id.routes_claimed_points);
            totalPoints= (TextView) itemView.findViewById(R.id.total_points);
            destinationsPoints= (TextView) itemView.findViewById(R.id.destinations_reached_points);
            destinationsPointsLost= (TextView) itemView.findViewById(R.id.destinations_reached_points_lost);
            longestRoutePoints= (TextView) itemView.findViewById(R.id.longest_route_points);
        }

        private AbstractPlayer player;

        public void bind(AbstractPlayer tobind) {
            player = tobind;

            username.setText(player.getMyUsername());
            claimedRoutes.setText(String.valueOf(player.getNumRoutes()));
            claimedRoutesPoints.setText(String.valueOf(player.getClaimedRoutePoints()));
            totalPoints.setText(String.valueOf(player.getScore()));
            destinationsPoints.setText(String.valueOf(player.getDestinationPoints()));
            destinationsPointsLost.setText(String.valueOf(player.getDestinationPointsLost()));
            longestRoutePoints.setText(String.valueOf(player.getLongestRoutePoints()));


        }

        @Override
        public void onClick(View view) {
        }

    }

    private class mAdapter extends RecyclerView.Adapter<mHolder> {
        private List<AbstractPlayer> itemlist = null;

        public mAdapter(List<AbstractPlayer> items) {
            itemlist = items;
        }

        @Override
        public mHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(GameOverPresenter.this);
            return new mHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(mHolder holder, int position) {
            AbstractPlayer p = itemlist.get(position);
            holder.bind(p);
            holder.setIsRecyclable(false);
        }

        @Override
        public int getItemCount() {
            return itemlist.size();
        }

    }
}




