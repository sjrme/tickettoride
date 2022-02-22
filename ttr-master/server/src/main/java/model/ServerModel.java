package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clientproxy.ClientProxy;
import commands.Command;
import interfaces.ICommandX;
import results.Result;
import results.game.ReplaceFaceUpCardsResult;

/**
 * ServerModel is the server model root class.
 * When a ServerModel method is called --
 *  Step one: User the parameter "gameName" to access the corresponding StartedGame in the Map.
 *  Step two: Call this StartedGame's version of the same method. Will return a Result.
 *  Step three: Pass the StartedGame's Result to the ClientProxy
 *
 * @author Stephen Richins
 * @version 1.0
 */

public class ServerModel implements Serializable {

    private Map<String, UnstartedGame> allUnstartedGames;
    private Map<String, StartedGame> allStartedGames;
    private Set<User> allUsers;
    private ClientProxy toClient;
    private Map<String, List<Result>> allCommandLists = new HashMap<>();

    private static class LazyModelHelper{
        private static final ServerModel MODEL_INSTANCE = new ServerModel();
    }

    public static ServerModel getInstance(){

        return LazyModelHelper.MODEL_INSTANCE;
    }

    private ServerModel(){
        allUnstartedGames = new HashMap<>();
        allStartedGames = new HashMap<>();
        allUsers = new HashSet<>();
        toClient = new ClientProxy();
    }

    public List<Result> getGameResultList(String gameName){
        return allCommandLists.get(gameName);
    }


    public void initializeServerModel(Set<User> loadedUsers, Map<String, StartedGame> loadedGames, Map<String, List<Command>> loadedCommands){
        //save loaded users
        if (loadedUsers != null)
            this.allUsers = loadedUsers;
        //save loaded games
        if (loadedGames != null) {
            this.allStartedGames = loadedGames;
            for(StartedGame loadedGame : allStartedGames.values()) {
                allCommandLists.put(loadedGame.getGameName(), new ArrayList<Result>());
            }
        }
        
        //run all outstanding commands
        if (loadedCommands != null)
            for (List<Command> gameCommandList : loadedCommands.values())
                for (Command outstandingCommand : gameCommandList)
                    ((ICommandX) outstandingCommand).execute();
    }

    public User getUser(String username){
        for (User myUser : allUsers)
            if (myUser.getUsername().equals(username))
                return myUser;
        return null;
    }

    public StartedGame getStartedGame(String gameName){
        if (allStartedGames.containsKey(gameName))
            return allStartedGames.get(gameName);
        return null;
    }


   /***********************************BEFORE GAME*******************************************/
    /**
     *  <h1>Add Unstarted Game</h1>
     *  Checks to see if a game exists, rejecting the creation command if so, and adds the game
     *  to the list of unstarted games if not; reports success to the proxy.
     *
     */
   public boolean addNewUnstartedGame(String username, String gameName, int numPlayers){
        UnstartedGame newGame = new UnstartedGame(gameName, numPlayers);
        if (allUnstartedGames.containsKey(newGame.getGameName()) || allStartedGames.containsKey(newGame.getGameName())){
            String message = "Game already exists.";
            toClient.rejectCommand(username, newGame.getGameName(), message);
            return false;
        } else {
            //newGame.addPlayer(username);
            allUnstartedGames.put(newGame.getGameName(), newGame);
            toClient.createGame(username, newGame.getGameName());
            toClient.updateAllUsersInMenus(getUnstartedGamesList(), getStartedGamesList());
            return true;
        }
    }

    public void setAllUnstartedGames( Map<String, UnstartedGame> allUnstartedGame) {
        this.allUnstartedGames = allUnstartedGames;
    }

    public void setAllStartedGames(Map<String, StartedGame> startedGames) {
        allStartedGames = startedGames;
    }

    public boolean register(String username, String password, String sessionID) {
        User user = new User(username, password);
        toClient = new ClientProxy();
        String message = "Registered as " + user.getUsername() + ".";
        if(allUsers.add(user)) {
            toClient.registerUser(user.getUsername(), user.getPassword(), message, sessionID);
            return true;
        } else {
            message = user.getUsername() + " is already registered.";
            toClient.rejectCommand(sessionID, null, message);
            return false;
        }
    }

    public boolean login(String username, String password, String sessionID) {
        User user = new User(username, password);
        toClient = new ClientProxy();

        if (allUsers.contains(user)){
            for (User currentUser : allUsers){
                if (currentUser.getUsername().equals(user.getUsername())){
                    if (currentUser.getPassword().equals(user.getPassword())){
                        toClient.loginUser(user.getUsername(), user.getPassword(), sessionID);
                        return true;
                    }
                }
            }
        } else{
            String message = "Invalid login information.";
            toClient.rejectCommand(sessionID, null, message);
        }
        return false;
    }

    private List<UnstartedGame> getUnstartedGamesList(){
        List<UnstartedGame> joinableGames = new ArrayList<>();

        for(String nextGameName : allUnstartedGames.keySet()){
            joinableGames.add(allUnstartedGames.get(nextGameName));
        }
        return joinableGames;
    }

    private List<RunningGame> getStartedGamesList(){
        List<RunningGame> startedGames = new ArrayList<>();

        for(String nextGameName : allStartedGames.keySet()){
            RunningGame nextStartedGame = new RunningGame(allStartedGames.get(nextGameName)
                    .getGameName(),
                    allStartedGames.get(nextGameName)
                            .getAllPlayers().size());
            startedGames.add(nextStartedGame);
        }
        return startedGames;
    }

    /**
     *  <h1>Poll Game List</h1>
     *  Sends the current list of games that haven't started to the proxy.
     *
     *  @param          username            The user that's requesting the game list
     */
    public boolean pollGameList(String username){
        toClient.updateSingleUserGameList(username, getUnstartedGamesList(), getStartedGamesList());
        return true;
    }

    public boolean addPlayerToGame(String username, String gameName) {
        boolean success = false;
        if (allUnstartedGames.containsKey(gameName)){
            UnstartedGame myUnstartedGame = allUnstartedGames.get(gameName);
            if (!myUnstartedGame.hasEnoughPlayersToStart()){
                myUnstartedGame.addPlayer(username);
                success = true;
            }
        }

        if (success) {
            toClient.joinGame(username, gameName);
            toClient.updateAllUsersInMenus(getUnstartedGamesList(), getStartedGamesList());
            return true;
        } else {
            String message = "Could not join game.";
            toClient.rejectCommand(username, gameName, message);
            return false;
        }
    }

    public boolean removePlayerFromGame(String username, String gameName) {
        boolean success = false;
        if (allUnstartedGames.containsKey(gameName)){
            UnstartedGame myUnstartedGame = allUnstartedGames.get(gameName);
            if (myUnstartedGame.hasPlayer(username)){ //if the player is in the game, remove them from the game
                myUnstartedGame.removePlayer(username);
                success = true;
                if (myUnstartedGame.hasNoPlayers()){ //if number of players == 0, remove the game
                    allUnstartedGames.remove(gameName);
                }
            }
        }

        if (success) {
            toClient.updateAllUsersInMenus(getUnstartedGamesList(), getStartedGamesList());
            toClient.leaveGame(username, gameName);
            return true;
        } else {
            String message = "Player not in game.";
            toClient.rejectCommand(username, gameName, message);
            return false;
        }
    }
    
    /**
     *  <h1>reJoinGame</h1>
     *  Checks to see if a user is a player in a started game, confirming that player can re-join
     *  the game if so, or a rejection is sent to the client if not.
     *
     *  @param          username            The player trying to re-join.
     *  @param          gameName            The game the player is trying to re-join
     */
    public boolean reJoinGame(String username, String gameName) {
        String message = "You were not in that game.";
        if(allStartedGames.containsKey(gameName)) {
            StartedGame gameToReJoin = allStartedGames.get(gameName);
            if(gameToReJoin.getAllPlayers().containsKey(username)) {
                List<String> playerNames = gameToReJoin.getAllPlayerNames(); // get player names
                int numPlayers = playerNames.size(); // number of players
                List<PlayerData> playerData = gameToReJoin.getPlayerData();
                PrivatePlayerData privateData = gameToReJoin.getPrivateData(username);
                int destDeckSize = gameToReJoin.getDestCardDeckSize();
                int trainDeckSize = gameToReJoin.getTrainCardDeckSize();
                List<Integer> faceUpCards = gameToReJoin.getFaceUpCards();
                int discardPileSize = 0; // not implemented
                List<ChatHistoryData> gameHistory = gameToReJoin.getAllGameHistory();
                List<ChatHistoryData> chatHistory = gameToReJoin.getAllChatHistory();
                List<String> playerOrder = gameToReJoin.getPlayerOrder();
                String activePlayer = playerOrder.get(gameToReJoin.getTurnPointer());
                GameData gameData = new GameData(gameName, numPlayers, playerData, privateData, activePlayer,
                        trainDeckSize, destDeckSize, faceUpCards, discardPileSize, gameHistory, chatHistory);

                toClient.reJoinGame(username, gameData);
                return true;
            } else {
                toClient.rejectCommand(username, gameName, message);
            }
        } else {
            toClient.rejectCommand(username, gameName,message);
        }
        return false;
    }
    
   /*************************************STARTING GAME*********************************************/
    public boolean startGame(String gameName, String username) {
        if (allUnstartedGames.containsKey(gameName)){

            UnstartedGame myUnstartedGame = allUnstartedGames.get(gameName);
            StartedGame newlyStartedGame = new StartedGame(myUnstartedGame);

            allUnstartedGames.remove(gameName);
            List<Result> startResults =
                    newlyStartedGame.preGameSetup(myUnstartedGame.getUsernamesInGame());

            allStartedGames.put(gameName, newlyStartedGame);
            allCommandLists.put(gameName, new ArrayList<Result>());

            toClient.updateAllUsersInMenus(getUnstartedGamesList(), getStartedGamesList());
            for (Result result : startResults) {
                toClient.startGame(result, result.getUsername());
                allCommandLists.get(gameName).add(result);
            }
            int counter = 0;
            while (newlyStartedGame.getReplaceFaceUpFlag()) { //If three face-up locomotives
                Result nextResult = newlyStartedGame.replaceFaceUpCards(username, counter);
                toClient.sendToGame(gameName, nextResult);
                allCommandLists.get(gameName).add(nextResult);
                counter++;
            }

            Result turnResult = newlyStartedGame.getThenNullifyTurnResult();
            if (turnResult != null) {
                toClient.sendToGame(gameName, turnResult);
                allCommandLists.get(gameName).add(turnResult);
            }
            return true;
        } else {
            String message = "Game " + gameName +  " does not exist, or has started.";
            toClient.rejectCommand(username, gameName, message);
        }
        return false;
    }

   /****************************************ROUND ONE*********************************************/
    public boolean returnDestCard(String gameName, String playerName, int destCard) {
        try {
            StartedGame game = this.getGame(gameName);
            Result result = game.returnDestCard(playerName, destCard);
            sendToClients(playerName, game, result);
            return true;
        }
        catch (GamePlayException ex){
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return true;
        }
    }

    private StartedGame getGame(String gameName) throws GamePlayException {
        StartedGame matchingStartedGame = allStartedGames.get(gameName);

        if (matchingStartedGame != null) {
          return matchingStartedGame;
        }
        else {
            throw new GamePlayException("Game " + gameName +  " does not exist.");
        }
    }

    /**************************************DrawDestCard*******************************************/

    public boolean drawThreeDestCards(String gameName, String playerName) {
        try {
            StartedGame game = this.getGame(gameName);
            Result result = game.drawThreeDestCards(playerName);
            sendToClients(playerName, game, result);
            return true;
        }
        catch (GamePlayException ex) {
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return false;
        }
    }

    /*************************************DrawTrainCard********************************************/

    public boolean drawTrainCardFromDeck(String gameName, String playerName) {

        try {
            StartedGame game = this.getGame(gameName);
            Result result = game.drawTrainCardFromDeck(playerName);
            sendToClients(playerName, game, result);
            return true;
        } catch (GamePlayException ex) {
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return false;
        }
    }

    public boolean drawTrainCardFromFaceUp(String gameName, String playerName, int index) {
        try {
            StartedGame game = this.getGame(gameName);
            List<Result> results = game.drawTrainCardFromFaceUp(playerName, index);

            toClient.sendToUser(playerName, gameName, results.get(0));
            toClient.sendToGame(gameName, game.getGameHistory());
            toClient.sendToGame(gameName, results.get(1));

            Result turnResult = game.getThenNullifyTurnResult();
            if (turnResult != null) {
                toClient.sendToGame(game.getGameName(), turnResult);
                allCommandLists.get(game.getGameName()).add(turnResult);
            }
            Result endGameResult  = game.getEndGameResult();
            if (endGameResult !=  null) {
                toClient.sendToGame(game.getGameName(), endGameResult);
                allCommandLists.get(game.getGameName()).add(endGameResult);
                //remove the game from the list
                allStartedGames.remove(game.getGameName());
            }
            //game.printBoardState();

            //remove the game from the list

        return true;
        } catch (GamePlayException ex) {
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return false;
        }
    }

    /************************************Claim Route***********************************************/
    public boolean claimRoute(String gameName, String playerName, int routeId, List<Integer> trainCards) {
        try {
            StartedGame game = this.getGame(gameName);

            //save previous cards

            List<Integer> previousFaceUpCards = game.getFaceUpCards();

            Result claimRouteResult = game.claimRoute(playerName, routeId, trainCards);

            //check new cards
            List<Boolean> faceUpDifferences = new ArrayList<>();
            List<Integer> newFaceUpCards = game.getFaceUpCards();
            for (int i = 0; (i < newFaceUpCards.size() && i < previousFaceUpCards.size()); i++){
                if (previousFaceUpCards.get(i).equals(newFaceUpCards.get(i)))
                    faceUpDifferences.add(false);
                else
                    faceUpDifferences.add(true);
            }
            while (faceUpDifferences.size() < 5){
                if (newFaceUpCards.size() > previousFaceUpCards.size()) //if there are more cards than before, flip them
                    faceUpDifferences.add(true);
                else
                    faceUpDifferences.add(false); //if there aren't new cards, don't flip them
            }

            Result replaceFaceUpResult = new ReplaceFaceUpCardsResult(newFaceUpCards, faceUpDifferences);


            sendToClients(playerName, game, claimRouteResult);
            sendToClients(playerName, game, replaceFaceUpResult);

            String finalTurnFlag = game.getAfterFinalTurnPlayer();

            if (finalTurnFlag != null) {
                Result finalRoundResult = game.getFinalTurnResult();
                toClient.sendToGame(gameName, finalRoundResult);
                allCommandLists.get(game.getGameName()).add(finalRoundResult);
            }
            return true;
        } catch (GamePlayException ex) {
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return false;
        }
    }

    public boolean chat(String gameName, String playerName, String message) {
        try {
            Result result =
                    this.getGame(gameName).addChat(playerName, message);
            toClient.sendToGame(gameName, result);
            return true;
        }
        catch (GamePlayException ex) {
            toClient.rejectCommand(playerName, gameName, ex.getMessage());
            return false;
        }
    }

    private void sendToClients(String playerName, StartedGame game , Result result) {
        toClient.sendToUser(playerName, game.getGameName(), result);
        allCommandLists.get(game.getGameName()).add(result);
        toClient.sendToGame(game.getGameName(), game.getGameHistory());
        allCommandLists.get(game.getGameName()).add(game.getGameHistory());

        Result turnResult = game.getThenNullifyTurnResult();
        if (turnResult != null) {
            toClient.sendToGame(game.getGameName(), turnResult);
            allCommandLists.get(game.getGameName()).add(turnResult);
        }
        Result endGameResult  = game.getEndGameResult();
        if (endGameResult !=  null) {
            toClient.sendToGame(game.getGameName(), endGameResult);
            allCommandLists.get(game.getGameName()).add(endGameResult);
            //remove the game from the list
            allStartedGames.remove(game.getGameName());
        }
        //game.printBoardState();
    }

    public List<ChatHistoryData> getChatHistoryData(String gameName){
        return allStartedGames.get(gameName).getChatResultList();
    }

    public List<ChatHistoryData> getGameHistories(String gameName) {
        return allStartedGames.get(gameName).getAllChatHistory();
    }
}
