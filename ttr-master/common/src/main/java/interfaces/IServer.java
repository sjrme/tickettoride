package interfaces;

import java.util.List;

/**
 * The IServer defines methods that the client will call, but will actually be executed on the server.
 * These functions will all return void because they require a connection which will be done by an
 * asynchronous task. Rather, results from this class will return from the server to whichever task
 * the ClientCommunicator created to handle (at this point only a commandTask exists). From there,
 * the Facade will update the ClientModel, which will then notify all its current observers.
 */
public interface IServer {

    //MENU LOGIN
    boolean login(String username, String password, String sessionID);
    boolean register(String username, String password, String sessionID);

    //MENU GAMELIST && MENU LOBBY
    boolean pollGameList(String username);
    boolean createGame(String username, String gameName, int playerNum);
    boolean joinGame(String username, String gameName);
    boolean leaveGame(String username, String gameName);
    boolean startGame(String gameName, String username);
    boolean reJoinGame(String username, String gameName);

    boolean drawThreeDestCards(String username, String gameName);
    boolean returnDestCards(String username, String gameName, int destCard);
    boolean drawTrainCardFromDeck(String username, String gameName);
    boolean drawTrainCardFromFaceUp(String username, String gameName, int index);
    boolean claimRoute(String username, String gameName, int routeID, List<Integer> trainCards);
    boolean sendChatMessage(String username, String gameName, String message);

}

