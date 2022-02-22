package fysh340.ticket_to_ride.menus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fysh340.ticket_to_ride.R;
import interfaces.Observer;
import model.ClientModel;
import okhttp3.WebSocket;
import serverproxy.ServerProxy;
import websocket.ClientWebSocket;

public class MenuLogin extends AppCompatActivity implements Observer {
    private ClientModel clientModel = ClientModel.getMyClientModel();
    private ServerProxy serverProxy = new ServerProxy();
    private ClientWebSocket webSocket = ClientWebSocket.getClientWebSocket();
    private String mPort = "8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        setupUI(findViewById(android.R.id.content));

        //load the previous login attempt data
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.default_string), Context.MODE_PRIVATE);
        String ipString = sharedPref.getString("ip", getString(R.string.default_string));
        String usernameString = sharedPref.getString("username", getString(R.string.default_string));
        String passwordString = sharedPref.getString("password", getString(R.string.default_string));

        EditText ip = (EditText) findViewById(R.id.ip_address);
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        ip.setText(ipString);
        username.setText(usernameString);
        password.setText(passwordString);

        setupButtons();
    }

    private void setupButtons(){
        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        clientModel.register(this); //adds this controller to the list of observers
    }
    @Override
    protected void onPause(){
        super.onPause();
        clientModel.unregister(this);
    }

    public void login(View view){
        EditText ipEdit = (EditText) findViewById(R.id.ip_address);
        EditText usernameEdit = (EditText) findViewById(R.id.username);
        EditText passwordEdit = (EditText) findViewById(R.id.password);

        String ip = ipEdit.getText().toString();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        //begin save current input to load later
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.default_string), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ip", ip);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
        //end save current input to load later

        if (webSocket.initialize(ip, mPort, username, password)){
            clientModel.setIp(ip);
            serverProxy.login(username, password, null);
        }

    }

    public void register(View view){
        EditText ipEdit = (EditText)findViewById(R.id.ip_address);
        EditText usernameEdit = (EditText)findViewById(R.id.username);
        EditText passwordEdit = (EditText)findViewById(R.id.password);

        String ip = ipEdit.getText().toString();
        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (webSocket.initialize(ip, mPort, username, password)){
            clientModel.setIp(ip);
            serverProxy.register(username, password, null);
        }
    }

    @Override
    public void update() {
        if (clientModel.hasUser()) {
            Intent intent = new Intent(this, MenuGameList.class);
            startActivity(intent); //proceed to game list screen
        } else if (clientModel.hasMessage()) {
            clientModel.receivedMessage();
            Toast.makeText(getApplicationContext(), clientModel.getMessageToToast(), Toast.LENGTH_SHORT).show();
            WebSocket myWebSocket = ClientWebSocket.getClientWebSocket().getMyWebSocket();
            myWebSocket.close(1000, "LOGOUT");
        }
    }

    public void setupUI(View view) { //Modifies onClick of view to check type of widget clicked.

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MenuLogin.this);
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
