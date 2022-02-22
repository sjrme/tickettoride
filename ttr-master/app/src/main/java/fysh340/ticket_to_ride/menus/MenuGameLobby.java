package fysh340.ticket_to_ride.menus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.MasterGamePresenter;
import interfaces.Observer;
import model.ClientModel;
import model.Game;
import serverproxy.ServerProxy;

public class MenuGameLobby extends AppCompatActivity implements Observer {

    private ClientModel clientModel = ClientModel.getMyClientModel();
    private SearchAdapter fAdapter;
    private RecyclerView recyclerView;
    private TextView text;
    private ServerProxy serverProxy = new ServerProxy();
    private Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game_lobby);
        clientModel.register(this); //registers this controller as an observer to the ClientModel
        recyclerView = (RecyclerView) findViewById(R.id.playerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        start = (Button) findViewById(R.id.StartButton);
        Button leave=(Button) findViewById(R.id.LeaveButton);
        start.setEnabled(false);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverProxy.startGame(clientModel.getMyUsername(), clientModel.getMyGameName());
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverProxy.leaveGame(clientModel.getMyUsername(), clientModel.getMyGameName());
                finish();
            }
        });
        clientModel.setLeftGame(false);
        updateUI();
    }

    private void updateUI() {
        recyclerView.removeAllViewsInLayout();
        List<String> players = clientModel.getPlayersinGame();
        fAdapter = new SearchAdapter(players);
        recyclerView.setAdapter(fAdapter);
        if (clientModel.gameIsFull())
            start.setEnabled(true);
        else
            start.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        serverProxy.leaveGame(clientModel.getMyUsername(), clientModel.getMyGameName());
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        clientModel.removeMyGame();
        //clientModel.setHasGame(false);
    }
    @Override
    protected void onPause(){
        super.onPause();
        clientModel.unregister(this);
    }

    @Override
    public void update() {

        if (clientModel.hasMessage()){
            clientModel.receivedMessage();
            Toast.makeText(getApplicationContext(), clientModel.getMessageToToast(), Toast.LENGTH_SHORT).show();
        }
        if(clientModel.gameIsStarted()) {
            Intent intent = new Intent(this, MasterGamePresenter.class);
            startActivity(intent);
        }
        else if (clientModel.hasNewGameLists()){
            clientModel.receivedNewGameLists();
            updateUI();
        }
        if(clientModel.disconnected()){ // show dialog and force user to exit to login screen
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Disconnected from server");
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Game.getGameInstance().reset();
                            ClientModel.getMyClientModel().reset();
                            Intent intent = new Intent(getApplicationContext(), MenuLogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //reset the activity stack
                            startActivity(intent); //proceed to login screen
                        }
                    });
            alertDialog.show();
        }

    }

    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FilterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lobby_players_item_view, parent, false));
            text = (TextView) itemView.findViewById(R.id.player);
            text.setOnClickListener(this);

        }

        private String username;

        public void bind(String tobind) {
            username = tobind;

            text.setText(username);


        }

        @Override
        public void onClick(View view) {
        }

    }

    private class SearchAdapter extends RecyclerView.Adapter<MenuGameLobby.FilterHolder> {
        private List<String> itemlist = null;

        public SearchAdapter(List<String> items) {
            itemlist = items;
        }

        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MenuGameLobby.this);
            return new FilterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FilterHolder holder, int position) {
            String p = itemlist.get(position);
            holder.bind(p);
            holder.setIsRecyclable(false);
        }

        @Override
        public int getItemCount() {
            return itemlist.size();
        }

        }
    }

