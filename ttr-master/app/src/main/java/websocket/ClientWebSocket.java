package websocket;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.TimeUnit;

import clientcommunicator.CommandResultXSerializer;
import interfaces.IResultX;
import model.ClientModel;
import model.Game;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import results.Result;
import serverproxy.ServerProxy;

public class ClientWebSocket extends WebSocketListener
{
    private static final ClientWebSocket clientWebSocket = new ClientWebSocket();
    private ClientWebSocket(){}
    public static ClientWebSocket getClientWebSocket()
    {
        return clientWebSocket;
    }

    private WebSocket ws;
    private String ip;
    private String port;
    private final int RECONNECT_CODE = 1;
    private boolean listening = false;
    private OkHttpClient client;
    private String username;
    private String password;
    private boolean isDisconnected = false;
    private ServerProxy serverProxy = new ServerProxy();

    public WebSocket getMyWebSocket(){
        return this.ws;
    }
    public void sendJson(String myString)
    {
        ws.send(myString);
    }
    public boolean initialize(String ip, String port, String username, String password) {
        this.username = username;
        this.password = password;
        if (!listening) {
            ClientModel.getMyClientModel().setDisconnectFlag(false);
            Game.getGameInstance().setDisconnectFlag(false);
            if (ip.trim().equals(""))
                return false;
            this.ip = ip;
            this.port = port;
            String serverEndpoint = "ws://" + ip + ":" + port + "/";
            Request request = new Request.Builder().url(serverEndpoint).build();
            client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .build();
            ws = client.newWebSocket(request, this);
            listening = true;
            //if (isDisconnected)
              //  serverProxy.login(username, password, null);
        }
        return true;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response respone) {


    }

    @Override
    public void onMessage(WebSocket webSocket, String serverMessage) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Result.class, new CommandResultXSerializer());
        Gson customGson = gsonBuilder.create();

        Result result = customGson.fromJson(serverMessage, Result.class);
        synchronized (ClientModel.class) {
            ((IResultX) result).execute();
        }
    }



    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason)
    {
        //output("Closing : " + code + " / " + reason);
        listening = false;
        isDisconnected = true;

        try {
            Thread.sleep(1000);
            //initialize(this.ip, this.port, this.username, this.password);
        }catch (InterruptedException ex){
            //
        }
        //the server has disconnected you or you purposely disconnected yourself
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response)
    {
        if (t.getClass() == JsonSyntaxException.class){
            onClosing(webSocket, RECONNECT_CODE, t.getMessage());
            try {
                Thread.sleep(1000);
                //initialize(this.ip, this.port, this.username, this.password);
            } catch (InterruptedException ex){
                //
            }
        }
        else {
            listening = false;
            isDisconnected = true;
            ClientModel.getMyClientModel().setDisconnectFlag(true);
            Game.getGameInstance().setDisconnectFlag(true);
            ClientModel.getMyClientModel().notifyObserver();
            Game.getGameInstance().notifyObserver();
        }
        //unknown reason for disconnect
    }

    public void quit()
    {
        client.dispatcher().executorService().shutdown();
    }

}