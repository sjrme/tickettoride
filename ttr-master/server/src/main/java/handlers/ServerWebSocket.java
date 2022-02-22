package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import commands.Command;
import interfaces.ICommandX;
import serverfacade.ServerFacade;
import utils.Utils;

@WebSocket
public class ServerWebSocket {

    //TODO: Kalan: Refactor this mess into a new class whose sole purpose is to publish to users/games.

    private static Logger logger = Logger.getLogger(Utils.SERVER_LOG);
    //for keeping track of all connections
    private static ConcurrentHashMap<String, Session> allSessions = new ConcurrentHashMap<>();

    //for keeping track of who to update when the game list changes: <username, session>
    private static ConcurrentHashMap<String, Session> allMenuSessions = new ConcurrentHashMap<>();

    //for keeping track of individual games to update: <gameName, <username, session>>
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Session>> gameSessions = new ConcurrentHashMap<>();

    //for referencing the ServerWebSocket by using a session
    private static ConcurrentHashMap<Session, ServerWebSocket> myServerWebSockets = new ConcurrentHashMap<>();

    public static boolean userAlreadyLoggedIn(String username){
        for (ServerWebSocket mySocket : myServerWebSockets.values()){
            if (username.equals(mySocket.getUsername()) && mySocket.gameName != null){
                return true;
            }
        }
        return false;
    }
    public static Session getMySessionID(String sessionID)
    {
        return allSessions.get(sessionID);
    }

    public static ConcurrentHashMap<String, Session> getAllMenuSessions() {
        return allMenuSessions;
    }

    public static ConcurrentHashMap<String, Session> getGameSession(String gameName) {
        ConcurrentHashMap<String, Session> myGame = gameSessions.get(gameName);
        if (myGame == null){
            myGame = new ConcurrentHashMap<>();
            gameSessions.put(gameName, myGame);
        }
        return myGame;
    }

    public static Session getMySession(String username) {
        return allMenuSessions.get(username);
    }

    public static Session getMyPlayerSessionInGame(String username, String gameName){
        ConcurrentHashMap<String, Session> myGameSession = getGameSession(gameName);
        return myGameSession.get(username);
    }

    public static ServerWebSocket getMySocket(Session mySession) {
        return myServerWebSockets.get(mySession);
    }


    public void updateMenuSessions(String username) {
        this.username = username;
        allMenuSessions.put(username, this.session);
    }

    public String getUsername() {
        return username;
    }

    public void logout(){
        this.username = null;
    }

    public void joinGameSession(String username, String gameName) {
        this.username = username;
        this.gameName = gameName;
        if (gameSessions.containsKey(gameName)) { //if the gameName is already stored, add them
            ConcurrentHashMap<String, Session> myGameSession = ServerWebSocket.getGameSession(gameName);
            myGameSession.put(this.username, this.session);
        } else { //create a new hashmap for that game
            ConcurrentHashMap<String, Session> myGameSession = new ConcurrentHashMap<>();
            myGameSession.put(this.username, this.session);
            gameSessions.put(gameName, myGameSession);
        }
    }

    public void leaveGameSession(String username, String gameName) {
        this.gameName = null; //set to null to check for logic errors
        if (gameSessions.containsKey(gameName)) {
            ConcurrentHashMap<String, Session> myGameSession = gameSessions.get(gameName);
            if (myGameSession.containsKey(username)) {
                myGameSession.remove(username);
                if (myGameSession.size() == 0) {
                    gameSessions.remove(gameName);
                }
            }
        }
    }

    public void removeFromMenus(String username) {
        if (allMenuSessions.containsKey(username))
            allMenuSessions.remove(username);
    }

    public void rejoinGameSession(String username, String gameName) {
        this.gameName = gameName;
        if (gameSessions.containsKey(gameName)) { //if the session exists, add them back
            ConcurrentHashMap<String, Session> myGameSession = gameSessions.get(gameName);
            myGameSession.put(username, this.session);
            allMenuSessions.remove(username); //remove the client from the menu users
        } else { //if the session doesn't exist (if the server is restarted, loaded from the database and they are the first player to rejoin)
            ConcurrentHashMap<String, Session> myGameSession = new ConcurrentHashMap<>();
            myGameSession.put(username, this.session);
            gameSessions.put(gameName, myGameSession);
        }
    }

    private String sessionID;
    private Session session;
    private String username;
    private String gameName;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        synchronized (ServerFacade.class) {
            logger.info("Close: statusCode = " + statusCode + ", reason = " + reason);

            //removes session from master list & socket list
            if (ServerWebSocket.allSessions.containsKey(this.sessionID)) {
                allSessions.remove(this.sessionID);
                myServerWebSockets.remove(this.session);
            }

            //removes session from logged in list & game if in one
            if (this.username != null) {
                if (allMenuSessions.containsKey(this.username)) {
                    allMenuSessions.remove(this.username);
                }
                if (this.gameName != null) {
                    //if the client was in a game, call ServerFacade.leaveGame();
                    //LeaveGameCommandX kickDC = new LeaveGameCommandX(this.username, this.gameName);
                    //kickDC.execute();
                    ConcurrentHashMap<String, Session> myGameSession = gameSessions.get(gameName);
                    if (myGameSession.containsKey(this.username)) {
                        myGameSession.remove(this.username);
                    }
                }
            }
        }
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        logger.warning("Error: " + t.getMessage());
        t.printStackTrace();
    }

    /**
     * The onConnect method for websockets is similar to a constructor as it is called at the creation
     * of a new thread that will handle the connection. Each client is only allowed one websocket
     * connection at a time.
     * @param session The Session used to identify the connected client.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.sessionID = Integer.toHexString(this.hashCode());
        this.session = session;
        this.session.setIdleTimeout(600000); //timeout occurs if no communication for 10m (may be changed later)

        if (!allSessions.containsKey(this.sessionID)){ //if device not connected, connect and add them
            logger.fine("Connecting new device id: " + this.sessionID);
            allSessions.put(this.sessionID, session);
            myServerWebSockets.put(this.session, this);
        } else {
            try {
                logger.severe("ALREADY CONNECTED! Was this client properly removed when they disconnected?");
                session.disconnect();
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
        logger.fine("Connecting: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(String message)
    {
        logger.finest("Server Input: " + message);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Command.class, new CommandXSerializer());
        Gson gson = gsonBuilder.create();

        synchronized (ServerFacade.class) {
            Command command = gson.fromJson(message, Command.class);
            command.setSessionID(this.sessionID);
            ICommandX myCommand = (ICommandX) command;

            if (myCommand.execute())    //if the server accepted the command, add it to the database
                myCommand.addToDatabase();
        }

    }

}
