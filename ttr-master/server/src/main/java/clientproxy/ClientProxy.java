package clientproxy;

import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import handlers.ServerWebSocket;
import interfaces.IClient;
import model.GameData;
import model.RunningGame;
import model.ServerModel;
import model.UnstartedGame;
import results.Result;
import results.game.ChatResult;
import results.game.ClaimRouteResult;
import results.game.DrawThreeDestCardsResult;
import results.game.DrawTrainCardFromDeckResult;
import results.game.DrawTrainCardFromFaceUpResult;
import results.game.EndGameResult;
import results.game.FinalRoundResult;
import results.game.GameHistoryResult;
import results.game.RejectResult;
import results.game.RejoinResult;
import results.game.ReplaceFaceUpCardsResult;
import results.game.ReturnDestCardsResult;
import results.game.ReturnFirstDestCardResult;
import results.game.StartGameResult;
import results.game.TurnResult;
import results.menu.CreateGameResult;
import results.menu.JoinGameResult;
import results.menu.LeaveGameResult;
import results.menu.LoginResult;
import results.menu.MessageResult;
import results.menu.PollGamesResult;
import results.menu.RegisterResult;
import utils.Utils;

/**
 * The ClientProxy is the proxy on the server side that does client execution.
 * If the server calls rejectCommand, it has not accepted the command it received from a client. Calling any other
 * method in this class means that the server has accepted the command from a client.
 */
public class ClientProxy implements IClient {

    private static Logger logger = Logger.getLogger(Utils.SERVER_LOG);
    private Gson gson = new Gson();

    /**
     * Updates the game list of all clients who are at the menus. Should be used any time the server
     * modifies the game list in any way.

     */
    public void updateAllUsersInMenus(List<UnstartedGame> unstartedGameList, List<RunningGame> runningGameList) {
        ConcurrentHashMap<String, Session> allMenuSessions = ServerWebSocket.getAllMenuSessions();
        for (Map.Entry<String, Session> sessionEntry : allMenuSessions.entrySet()) {
            updateSingleUserGameList(sessionEntry.getKey(), unstartedGameList, runningGameList);
        }
    }

    /**
     * Updates a single client's game list. Will be used for when a client sends a pollgamerequest.
     * @param username
     */
    @Override
    public void updateSingleUserGameList(String username, List<UnstartedGame> unstartedGameList, List<RunningGame> runningGameList) {
        PollGamesResult result = new PollGamesResult(unstartedGameList, runningGameList);
        Session mySession = ServerWebSocket.getMySession(username);
        String resultJson = gson.toJson(result);
        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent game lists to: " + username);
        } catch (IOException | WebSocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void loginUser(String username, String password, String sessionID) {
        LoginResult result = new LoginResult(username);
        String resultJson = gson.toJson(result);

        if (ServerWebSocket.userAlreadyLoggedIn(username)){
            rejectCommand(sessionID, null, "User is already logged in");
            return;
        }

        //Update Websocket information with the server accepted login information
        Session mySession = ServerWebSocket.getMySessionID(sessionID);
        ServerWebSocket mySocket = ServerWebSocket.getMySocket(mySession);

        try {
            mySession.getRemote().sendString(resultJson);
            mySocket.updateMenuSessions(username);
            logger.info("Sent a login to: " + username);
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void registerUser(String username, String password, String message, String sessionID)
    {
        RegisterResult result = new RegisterResult(username, message);
        Session mySession = ServerWebSocket.getMySessionID(sessionID);
        String resultJson = gson.toJson(result);

        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent register to: " + username);
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public void startGame(Result result, String username) {
        //String username, String gameName, List<String> playerNames, List<Integer> destCards,
                //List<Integer> trainCards, List<Integer> faceUpCards
        StartGameResult startGameResult = (StartGameResult)result;

        String resultJson = gson.toJson(result);

        Session mySession = ServerWebSocket.getMySession(username);
        ServerWebSocket mySocket = ServerWebSocket.getMySocket(mySession);
        mySocket.joinGameSession(username, startGameResult.getGameName());
        mySocket.removeFromMenus(username);

        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent start game to: " + username);
        } catch (IOException | NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void startGame(String username, String gameName, List<String> playerNames,
                          List<Integer> destCards,List<Integer> trainCards, List<Integer> faceUpCards) {
    //List<Integer> trainCards, List<Integer> faceUpCards
    //Result result = new StartGameResult(username, gameName, playerNames, destCards, trainCards, faceUpCards);){

    }

    @Override
    public void joinGame(String username, String gameName) {
        JoinGameResult result = new JoinGameResult(username, gameName);
        String resultJson = gson.toJson(result);

        Session mySession = ServerWebSocket.getMySession(result.getUsername());
        ServerWebSocket mySocket = ServerWebSocket.getMySocket(mySession);
        mySocket.joinGameSession(username, gameName);

        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent join game to: " + username);
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void leaveGame(String username, String gameName) {
        LeaveGameResult result = new LeaveGameResult(username, gameName);
        Session mySession = ServerWebSocket.getMySession(result.getUsername());
        String resultJson = gson.toJson(result);

        ServerWebSocket mySocket = ServerWebSocket.getMySocket(mySession);
        //mySocket.leaveGameSession(username, gameName);

        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent leave game to: " + username);
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void createGame(String username, String gameName) {
        CreateGameResult result = new CreateGameResult(username, gameName);
        Session mySession = ServerWebSocket.getMySession(result.getUsername());
        String resultJson = gson.toJson(result);

        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Sent create game to: " + username);
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    //only used for webpage testing
    public void clearDatabase(String username, String message) {
        Session mySession = ServerWebSocket.getMySession(username);
        MessageResult messageResult = new MessageResult(username, message);
        String resultJson = gson.toJson(messageResult);

        try {
            mySession.getRemote().sendString(resultJson);
        } catch (IOException | NullPointerException ex) {
            //
        }
    }

    public void rejectCommand(String identifier, String gameName, String message) {
        MessageResult messageResult = new MessageResult(identifier, message);
        Session mySession = null;
        if (gameName != null)
            mySession = ServerWebSocket.getMyPlayerSessionInGame(identifier, gameName); //search by game first
        if (mySession == null){ //search the menus
            mySession = ServerWebSocket.getMySession(identifier);
        }
        if (mySession == null) //search everywhere
            mySession = ServerWebSocket.getMySessionID(identifier);


        String resultJson = gson.toJson(messageResult);
        try {
            mySession.getRemote().sendString(resultJson);
            logger.info("Rejected a command from: " + identifier);
        } catch (IOException | NullPointerException ex) {
            logger.warning("Client: " + identifier + " has already disconnected");
        }
    }

    @Override
    public void reJoinGame(String username, GameData gameData) {
        RejoinResult rejoinResult = new RejoinResult(username, gameData);
        String resultJson = getResultTypeAsJson(rejoinResult);
        Session mySession = ServerWebSocket.getMySession(username);
        ConcurrentHashMap<String, Session> myGameSession = ServerWebSocket.getGameSession(gameData.getGameName());
        myGameSession.put(username, mySession);
        
        try {
            mySession.getRemote().sendString(resultJson); // send rejoin result
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    public void sendToGame(String gameName, Result result){
        String resultJson = getResultTypeAsJson(result);
        ConcurrentHashMap<String, Session> myGameSession = ServerWebSocket.getGameSession(gameName);

        for (Map.Entry<String, Session> sessionEntry : myGameSession.entrySet()) {
            Session myUserSession = sessionEntry.getValue();
            try {
                myUserSession.getRemote().sendString(resultJson);
                logger.info("Sending a " + result.getType() + " result to: " + gameName + " game");
            } catch (IOException | WebSocketException | NullPointerException ex){
                logger.warning("Failed to send a: " + result.getType() + " command to the player: " + sessionEntry.getKey() + "in the: " + gameName + " game.");
            }
        }
        logger.info("Sent a: " + result.getType() + " command to the: " + gameName + " game.");

        //if the game has ended, log them out
        if (result.getType().equals(Utils.END_GAME_TYPE)){
            for (Map.Entry<String, Session> sessionEntry : myGameSession.entrySet()) {
                ServerWebSocket myPlayerWebsocket = ServerWebSocket.getMySocket(sessionEntry.getValue());
                try{
                    logger.info("Logging out: " + myPlayerWebsocket.getUsername() + "  ...");
                    myPlayerWebsocket.onClose(0, "GAME OVER");
                    logger.info("Success");
                } catch (WebSocketException | NullPointerException ex){
                    logger.info("Failed");
                }
            }
        }
    }

    public void sendToUser(String username, String gameName, Result result){
        String resultJson = getResultTypeAsJson(result);
        Session myUserSession = ServerWebSocket.getMyPlayerSessionInGame(username, gameName);

        try {
            myUserSession.getRemote().sendString(resultJson);
            logger.fine("Sent a: " + result.getType() + " command to " + username + ".");
        } catch (IOException | WebSocketException | NullPointerException ex){
            logger.warning("Failed to send a: " + result.getType() + " command to " + username + ".");
        }

    }

    public void sendToOthersInGame(String username, String gameName, Result result){
        String resultJson = getResultTypeAsJson(result);
        ConcurrentHashMap<String, Session> myGameSession = ServerWebSocket.getGameSession(gameName);

        for (Map.Entry<String, Session> sessionEntry : myGameSession.entrySet()){
            Session myUserSession = sessionEntry.getValue();

            ServerWebSocket myUserWebSocket = ServerWebSocket.getMySocket(myUserSession);
            if (myUserWebSocket != null && myUserWebSocket.getUsername().equals(username))
                continue;

            try {
                myUserSession.getRemote().sendString(resultJson);
            } catch (IOException | WebSocketException | NullPointerException ex) {
                logger.warning("Failed to send a: " + result.getType() + " command to " + username + ".");
            }
        }
    }

    private String getResultTypeAsJson(Result result){
        String myJsonString;
        switch (result.getType()){
            case Utils.LOGIN_TYPE:
                myJsonString = gson.toJson(result, LoginResult.class);
                break;
            case Utils.REGISTER_TYPE:
                myJsonString = gson.toJson(result, RegisterResult.class);
                break;
            case Utils.POLL_TYPE:
                myJsonString = gson.toJson(result, PollGamesResult.class);
                break;
            case Utils.CREATE_TYPE:
                myJsonString = gson.toJson(result, CreateGameResult.class);
                break;
            case Utils.JOIN_TYPE:
                myJsonString = gson.toJson(result, JoinGameResult.class);
                break;
            case Utils.LEAVE_TYPE:
                myJsonString = gson.toJson(result, LeaveGameResult.class);
                break;
            case Utils.START_TYPE:
                myJsonString = gson.toJson(result, StartGameResult.class);
                break;
            case Utils.REJOIN_TYPE:
                myJsonString = gson.toJson(result, RejoinResult.class);
                break;
            case Utils.MESSAGE_TYPE:
                myJsonString = gson.toJson(result, MessageResult.class);
                break;
            case Utils.DRAW_DEST_CARDS_TYPE:
                myJsonString = gson.toJson(result, DrawThreeDestCardsResult.class);
                break;
            case Utils.RETURN_FIRST_DEST_CARD_TYPE:
                myJsonString = gson.toJson(result, ReturnFirstDestCardResult.class);
                break;
            case Utils.RETURN_DEST_CARDS_TYPE:
                myJsonString = gson.toJson(result, ReturnDestCardsResult.class);
                break;
            case Utils.DRAW_TRAIN_CARD_DECK_TYPE:
                myJsonString = gson.toJson(result, DrawTrainCardFromDeckResult.class);
                break;
            case Utils.DRAW_TRAIN_CARD_FACEUP_TYPE:
                myJsonString = gson.toJson(result, DrawTrainCardFromFaceUpResult.class);
                break;
            case Utils.CLAIM_ROUTE_TYPE:
                myJsonString = gson.toJson(result, ClaimRouteResult.class);
                break;
            case Utils.CHAT_TYPE:
                myJsonString = gson.toJson(result, ChatResult.class);
                break;
            case Utils.GAME_HISTORY_TYPE:
                myJsonString = gson.toJson(result, GameHistoryResult.class);
                break;
            case Utils.REPLACE_ALL_FACEUP_TYPE:
                myJsonString = gson.toJson(result, ReplaceFaceUpCardsResult.class);
                break;
            case Utils.REJECT_TYPE:
                myJsonString = gson.toJson(result, RejectResult.class);
                break;
            case Utils.END_GAME_TYPE:
                myJsonString = gson.toJson(result, EndGameResult.class);
                break;
            case Utils.FINAL_ROUND_TYPE:
                myJsonString = gson.toJson(result, FinalRoundResult.class);
                break;
            case Utils.TURN_TYPE:
                myJsonString = gson.toJson(result, TurnResult.class);
                break;
            default:
                myJsonString = "Error parsing into Json. Check ClientProxy.";
                logger.warning("Error parsing into Json. Check ClientProxy.");
        }
        return myJsonString;
    }


    //These might not be used... In game Results will pass via sendToUser & sendToGame
    @Override
    public void addChat(String username, String message) {

    }

    @Override
    public void claimRoute(String username, int routeID) {

    }

    @Override
    public void drawDestCards(String username, List<Integer> destCards) {

    }

    @Override
    public void drawTrainCardDeck(String username, int trainCard) {

    }

    @Override
    public void drawTrainCardFaceUp(String username, int trainCard) {

    }

    @Override
    public void returnDestCards(String username, int destCard) {

    }

    @Override
    public void returnFirstDestCards(String username, int cardReturned) {

    }
}
