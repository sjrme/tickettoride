package interfaces;

import java.util.List;

import model.GameData;
import model.RunningGame;
import model.UnstartedGame;
import results.Result;

public interface IClient {
    //login screen
    void loginUser(String username, String password, String sessionID);
    void registerUser(String username, String password, String message, String sessionID);

    //menus
    void startGame(String username, String gameName, List<String> playerNames, List<Integer> destCards,
                   List<Integer> trainCards, List<Integer> faceUpCards);
    void updateSingleUserGameList(String username, List<UnstartedGame> unstartedGameList, List<RunningGame> runningGameList);
    void joinGame(String username, String gameName);
    void leaveGame(String username, String gameName);
    void reJoinGame(String username, GameData rejoin);
    void createGame(String username, String gameName);

    //in-game
    void addChat(String username, String message);
    void claimRoute(String username, int routeID);
    void drawDestCards(String username, List<Integer> destCards);
    void drawTrainCardDeck(String username, int trainCard);
    void drawTrainCardFaceUp(String username, int trainCard);
    void returnDestCards(String username, int destCard);
    void returnFirstDestCards(String username, int cardReturned);
}
