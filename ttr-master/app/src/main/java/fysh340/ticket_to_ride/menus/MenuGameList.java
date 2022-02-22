package fysh340.ticket_to_ride.menus;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import fysh340.ticket_to_ride.R;
import fysh340.ticket_to_ride.game.MasterGamePresenter;
import interfaces.Observer;
import model.ClientModel;
import model.Game;
import model.RunningGame;
import model.UnstartedGame;
import okhttp3.WebSocket;
import serverproxy.ServerProxy;
import websocket.ClientWebSocket;

public class MenuGameList extends AppCompatActivity implements Observer{

    private ClientModel mClientModel = ClientModel.getMyClientModel();
    private ServerProxy mServerProxy = new ServerProxy();
    private MyUnstartedGameListAdapter mUnstartedGamesAdapter;
    private MyRunningGameListAdapter mRunningGamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.game_list_title));
        setSupportActionBar(toolbar);

        setupUI(findViewById(android.R.id.content));

        RecyclerView unstartedRecyclerView = (RecyclerView)findViewById(R.id.unstarted_game_recycler);
        LinearLayoutManager unstartedLayoutManager = new LinearLayoutManager(this);
        unstartedLayoutManager.setAutoMeasureEnabled(true);
        unstartedRecyclerView.setLayoutManager(unstartedLayoutManager);
        
        RecyclerView startedRecyclerView = (RecyclerView)findViewById(R.id.started_game_recycler);
        LinearLayoutManager startedLayoutManager = new LinearLayoutManager(this);
        startedLayoutManager.setAutoMeasureEnabled(true);
        startedRecyclerView.setLayoutManager(startedLayoutManager);

        //text watcher for create game button
        EditText gameName = (EditText) findViewById(R.id.gamename);
        gameName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                Button createGameButton = (Button)findViewById(R.id.createGameButton);
                String inputGameName = s.toString().trim();
                if (inputGameName.length() > 0){ //a game name must be at least 1 char long
                    createGameButton.setEnabled(true);
                }
                else
                    createGameButton.setEnabled(false);
            }
        });

        //recyclerview adapter
        //create the search adapter once, update its data later
        mUnstartedGamesAdapter = new MyUnstartedGameListAdapter(mClientModel
                                                                        .getUnstartedGameList());
        unstartedRecyclerView.setAdapter(mUnstartedGamesAdapter);
        
        mRunningGamesAdapter = new MyRunningGameListAdapter(mClientModel.getRunningGameList());
        startedRecyclerView.setAdapter(mRunningGamesAdapter);

        mServerProxy.pollGameList(mClientModel.getMyUsername());

        setupButtons();
        setupSpinner();
    }

    private void setupButtons(){
        Button createGameButton = (Button) findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGame(view);
            }
        });
    }


    private void setupSpinner(){
        Spinner numPlayerSpinner = (Spinner)findViewById(R.id.playernum_spinner);
        ArrayAdapter<CharSequence> numPlayerAdapter = ArrayAdapter.createFromResource(
                this, R.array.player_num_array, R.layout.support_simple_spinner_dropdown_item);
        numPlayerSpinner.setAdapter(numPlayerAdapter);
        LinearLayout spinnerLayout = (LinearLayout) findViewById(R.id.playernum_spinner_layout);
        spinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpinner(view);
            }
        });
    }


    public void createGame(View view){
        EditText gameNameEdit = (EditText)findViewById(R.id.gamename);
        String gameName = gameNameEdit.getText().toString();
        Spinner numPlayerSpinner = (Spinner) findViewById(R.id.playernum_spinner);
        int playerNum = Integer.parseInt(numPlayerSpinner.getSelectedItem().toString()); //spinner is hard coded into xml, so is safe to parseInt
        mServerProxy.createGame(mClientModel.getMyUsername(), gameName, playerNum);
    }

    public void showSpinner(View view){ //allows the layout consisting of text and spinner to open up the spinner item selection dropdown menu
        findViewById(R.id.playernum_spinner).performClick();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mClientModel.removeMyUser(); //clear user
        mClientModel.removeMyGame(); //clear game
    }
    @Override
    protected void onResume(){
        super.onResume();
        mClientModel.register(this); //registers this controller as an observer to the ClientModel
    }
    @Override
    public void onBackPressed(){
        WebSocket myWebSocket = ClientWebSocket.getClientWebSocket().getMyWebSocket();
        myWebSocket.close(1000, "LOGOUT");
        mClientModel.unregister(this);
        super.onBackPressed();
    }

    @Override
    public void update() {
        if (mClientModel.hasMessage()) {
            mClientModel.receivedMessage();
            Toast.makeText(getApplicationContext(), mClientModel.getMessageToToast(), Toast.LENGTH_SHORT).show();
        }
        if (mClientModel.hasCreatedGame()) {
            mClientModel.receivedCreatedGame();
            mServerProxy.joinGame(mClientModel.getMyUsername(), mClientModel.getMyGameName());
        } else if (mClientModel.hasJoinedGame()) {
            mClientModel.receivedHasJoinedGame();
            mClientModel.unregister(this);
            Intent intent = new Intent(getApplicationContext(), MenuGameLobby.class);
            startActivity(intent); //proceed to game list screen
        } else if (mClientModel.hasNewGameLists()){
            mClientModel.receivedNewGameLists();
            mUnstartedGamesAdapter.swapData(mClientModel.getUnstartedGameList());
            mRunningGamesAdapter.swapData(mClientModel.getRunningGameList());
        } else if (mClientModel.hasRejoinedGame()) {
            mClientModel.receivedHasRejoinedGame();
            mClientModel.unregister(this);
            Intent intent = new Intent(getApplicationContext(), MasterGamePresenter.class);
            startActivity(intent);
        }
        if(mClientModel.disconnected()){ // show dialog and force user to exit to login screen
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

    private class MyUnstartedGameListAdapter extends RecyclerView
                                                             .Adapter<MyUnstartedGameListAdapter
                                                                              .ViewHolder> {
        private List<UnstartedGame> allGames = new ArrayList<>();

        private MyUnstartedGameListAdapter(List<UnstartedGame> newList){
            allGames = newList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemGameName;
            TextView itemPlayers;
            View myView;

            ViewHolder(View view){
                super(view);
                itemGameName = (TextView) view.findViewById(R.id.item_game_name);
                itemPlayers = (TextView) view.findViewById(R.id.item_players);
                myView = view;
            }
        }

        void swapData(List<UnstartedGame> newGameList){
            this.allGames = newGameList;
            this.notifyDataSetChanged();
        }

        @Override
        public MyUnstartedGameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                        int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.unstarted_game_list_item_view, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            UnstartedGame myGame = allGames.get(position);

            final String gameName;
            gameName = myGame.getGameName();
            int currentPlayers = myGame.getPlayersIn();
            int neededPlayers = myGame.getPlayersNeeded();
            String playerString = currentPlayers + "/" + neededPlayers + " " + getString(R.string.players);

            holder.itemGameName.setText(gameName);
            holder.itemPlayers.setText(playerString);
            holder.myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.VISIBLE);
                    Animation myAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item);
                    myAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mServerProxy.joinGame(mClientModel.getMyUsername(), gameName);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    holder.myView.startAnimation(myAnimation);
                }
            });
        }

        @Override
        public int getItemCount() {
            return allGames.size();
        }

    }
    
    private class MyRunningGameListAdapter extends RecyclerView.Adapter<MyRunningGameListAdapter
                                                                               .ViewHolder> {
        private List<RunningGame> mAllStartedGames;
        
        public MyRunningGameListAdapter() {
            mAllStartedGames = new ArrayList<>();
        }
        
        public MyRunningGameListAdapter(List<RunningGame> allGames) {
            mAllStartedGames = allGames;
        }
    
        void swapData(List<RunningGame> newGamesList){
            this.mAllStartedGames = newGamesList;
            this.notifyDataSetChanged();
        }
    
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mGameName;
            TextView mPlayers;
            View mCurrentView;
            
            public ViewHolder(View itemView) {
                super(itemView);
                mGameName = (TextView) itemView.findViewById(R.id.item_game_name);
                mPlayers = (TextView) itemView.findViewById(R.id.item_players);
                mCurrentView = itemView;
            }
        }
    
        @Override
        public MyRunningGameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.started_game_list_item_view,
                                                        parent, false);
            return new ViewHolder(itemView);
        }
    
        @Override
        public void onBindViewHolder(final MyRunningGameListAdapter.ViewHolder holder,
                                     int position) {
            RunningGame currentGame = mAllStartedGames.get(position);
    
            final String currentGameName = currentGame.getGameName();
            
            int currentNumberOfPlayers = currentGame.getGameSize();
            String playersLabel = currentNumberOfPlayers + "/" + currentNumberOfPlayers + " "
                                          + getString(R.string.players);
    
            holder.mGameName.setText(currentGameName);
            holder.mPlayers.setText(playersLabel);
            holder.mCurrentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.VISIBLE);
                    Animation myAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item);
                    myAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mServerProxy.reJoinGame(mClientModel.getMyUsername(), currentGameName);
                        }
                
                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    holder.mCurrentView.startAnimation(myAnimation);
                }
            });
        }
    
        @Override
        public int getItemCount() {
            return mAllStartedGames.size();
        }
    }

    public void setupUI(View view) { //Modifies onClick of view to check type of widget clicked.

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MenuGameList.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) { //Hides the keyboard.

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}