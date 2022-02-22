package clientfacade;

import java.util.ArrayList;
import java.util.List;

import fysh340.ticket_to_ride.game.fragments.gameplaystate.ClientState;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.DrawSecondTrainCardState;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.MyTurnState;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.NotMyTurnState;
import fysh340.ticket_to_ride.game.fragments.gameplaystate.ReturnDestCardState;
import interfaces.IClient;
import model.AbstractPlayer;
import model.ChatHistoryData;
import model.ChatHistoryModel;
import model.ClientModel;
import model.DestCard;
import model.Game;
import model.GameData;
import model.MapModel;
import model.Player;
import model.PlayerData;
import model.Route;
import model.RunningGame;
import model.ServerError;
import model.TrainCard;
import model.UnstartedGame;
import model.VisiblePlayer;

/**
 * This class handles all communication from the server when something happens, whether in the menus
 * or in the game. Information from the server that passes through here will update the ClientModel
 * for anything pertaining to the menus prior to being in a game. While in a game, the Game model will
 * be updated with the information from the server.
 */
public class ClientFacade implements IClient{
    private ClientModel clientModel = ClientModel.getMyClientModel();
    private Game game = Game.getGameInstance();
    private ChatHistoryModel chatModel = ChatHistoryModel.myChat;
    private MapModel map = MapModel.getMapInstance();
    private ClientState mClientState = ClientState.INSTANCE;

    /**
     * Displays a message to the user visually on the screen in the format of a toast.
     * @pre The client is in a normal functioning state.
     * @post The messasge was visually displayed on the screen.
     * @param message The message that should be displayed to the user.
     */
    public void postMessage(String message){
        clientModel.setMessageToToast(message);
    }

    /**
     * Called by the server after it has accepted a login request by this client. The client will
     * set it's username according to what it receives here. The client will then advance from the login
     * menu to the game list menu.
     * @pre The client is in the login menu.
     * @post The client is in the game list menu.
     * @param username The username that was used in an attempt to login.
     * @param password The password that was used in an attempt to login.
     * @param sessionID The unique ID of the client that attempted to login.
     */
    @Override
    public void loginUser(String username, String password, String sessionID){
        clientModel.setMyUser(username, true);
    }

    /**
     * Called by the server after it has accepted a register request by this client. The client will
     * display the message provided by the server signalling the user that the request succeeded.
     * @pre The client is in the login menu.
     * @post The client will display the success of the register attempt visually to the user.
     * @param username The username that was used in an attempt to register.
     * @param password The password that was used in an attempt to register.
     * @param message A message from the server dictating the success of the register attempt.
     * @param sessionID The unique ID of the client that attempted to register.
     */
    @Override
    public void registerUser (String username, String password, String message, String sessionID){
        this.postMessage(message); //registering should only ever do a toast
    }

    /**
     * Called by the server on every client in the same game. Each client will receive the same gameName,
     * playerNames, and faceUpCards. Each client will receive different usernames, destCards, and
     * trainCards.
     * @pre The client is in the game lobby menu.
     * @post The client is now in the game view.
     * @param username The username (identifier) of the client that is starting a game.
     * @param gameName The name of the game that is being started.
     * @param playerNames A list the usernames of other players in the same game.
     * @param destCards The starting destination cards for this client's player.
     * @param trainCards The starting train cards for this client's player.
     * @param faceUpCards The starting face up cards for this game.
     */
    @Override
    public void startGame(String username, String gameName, List<String> playerNames, List<Integer> destCards,
                          List<Integer> trainCards, List<Integer> faceUpCards) {
        clientModel.startGame();
        Player myself = new Player(username, trainCards);
        game.initializeMyGame(myself, gameName, playerNames, faceUpCards);
        game.setPossibleDestCards(destCards);
        game.iHavePossibleDestCards(true);
        game.notifyObserver();

        game.setPossibleDestCards(destCards);
        game.setFaceUpCards(faceUpCards);
    }

    /**
     * Called by the server anytime game list data changes.
     * @pre The client has outdated game list data compared to the server.
     * @post The client will display the game list data supplied by the server.
     * @param username The username (identifier) of the client to have it's game list updated.
     * @param unstartedGameList The updated list of un-started games from the server.
     * @param runningGameList The updated list of running games from the server.
     */
    @Override
    public void updateSingleUserGameList(String username, List<UnstartedGame> unstartedGameList, List<RunningGame> runningGameList){
        clientModel.setGameLists(unstartedGameList, runningGameList);
    }

    /**
     * Called by the server after it has accepted a create game request.
     * @pre The client is in the game list menu.
     * @post The client automatically sends another request, this time to join the game that it has created.
     * @param username The username (identifier) of the client that has created a new game.
     * @param gameName The name of the game that was created.
     */
    @Override
    public void createGame(String username, String gameName){ //only called when server sends a create game result
        clientModel.setCreatedGame(gameName);
    }

    /**
     * Called by the server after it has accepted a join game request.
     * @pre The client is in the game list menu.
     * @post The client is in the game lobby menu.
     * @param username The username (identifier) of the client that has joined a game.
     * @param gameName The name of the game that the client has joined.
     */
    @Override
    public void joinGame(String username, String gameName){
        clientModel.setHasGame(gameName);
    }

    /**
     * Called by the server after it has accepted a leave game request.
     * @pre The client is in the game lobby menu.
     * @post The client is in the game list menu.
     * @param username The username (identifier) of the client that has left a game.
     * @param gameName The name of the game that the client has left.
     */
    @Override
    public void leaveGame(String username, String gameName){
        //clientModel.setInGameLobby(false);
    }

    @Override
    public void reJoinGame(String username, GameData gameData) {

        Player myself = new Player(username, gameData.getPrivatePlayerData().getTrainCards());
        List<String> playerNames = new ArrayList<>();
        for (PlayerData data : gameData.getPlayerData()) {
            playerNames.add(data.getPlayerName());
        }
        game.initializeMyGame(myself, gameData.getGameName(), playerNames, gameData.getFaceUpCards());

        for (Integer cardID : gameData.getPrivatePlayerData().getDestCards()) {
            game.getMyself().addDestCard(DestCard.getDestCardByID(cardID));
        }

        for (PlayerData playerData : gameData.getPlayerData()) {
            AbstractPlayer player = game.getPlayerByName(playerData.getPlayerName());
            player.setScore(playerData.getScore());
            player.setNumRoutes(playerData.getRoutes().size());
            player.setNumCards(playerData.getNumTrainCards());
            player.setDestCardNum(playerData.getNumDestCards());
            player.setNumTrains(playerData.getNumTrains());
            if (gameData.getActivePlayer().equals(playerData.getPlayerName())) {
                player.setMyTurn(true);
            } else {
                player.setMyTurn(false);
            }
            player.getClaimedRoutes().addAll(playerData.getRoutes());
        }
        game.setFaceUpCards(gameData.getFaceUpCards());
        game.setTrainCardDeckSize(gameData.getTrainDeckSize());
        game.setDestCardDeckSize(gameData.getDestDeckSize());

        for (ChatHistoryData chatHistory : gameData.getChatHistory()) {
            chatModel.addChat(chatHistory.getUsername(), chatHistory.getMessage());
        }

        for (ChatHistoryData gameHistory : gameData.getGameHistory()) {
            chatModel.addHistory(gameHistory.getUsername(), gameHistory.getMessage());
        }

        game.aPlayerHasChanged();
        game.iHaveDifferentDestDeckSize();
        game.iHaveDifferentFaceUpCards();
        game.iHaveDifferentTrainCards();
        game.iHaveDifferentTrainDeckSize();

        if (game.getMyself().getMyUsername().equals(gameData.getActivePlayer())) {
            if (gameData.getPrivatePlayerData().getPossibleDestCards().size() > 0) {
                mClientState.setState(new ReturnDestCardState());
                game.setPossibleDestCards(gameData.getPrivatePlayerData().getPossibleDestCards());
                game.iHavePossibleDestCards(true);
            } else if (gameData.getPrivatePlayerData().isDrewATrainCard()) {
                mClientState.setState(new DrawSecondTrainCardState());
            } else {
                mClientState.setState(new MyTurnState());
            }
        } else {
            mClientState.setState(new NotMyTurnState());
        }

        game.notifyObserver();

        clientModel.hasRejoinedGame(true);
        clientModel.notifyObserver();
    }
    
    /**
     * Called by the server after it has received a chat message by any other client in the same game
     * as this client.
     * @pre The client is in the same game as the other client who sent the chat message.
     * @post The client has the message added to it's list of chat messages from other clients in the
     * same game.
     * @param username The username (identifier) of this client, who is receiving the chat.
     * @param message The message from another client received via the server.
     */
    @Override
    public void addChat(String username, String message){
        chatModel.addChat(username,message);
    }

    /**
     * Called by the server when this client successfully claims a route. Updates the visual route's
     * color, the client's displayed points, number of trains, number of train cards, routes claimed,
     * and game history.
     * @pre This client attempted to claim a route during their turn.
     * @post This client will have it's information updated and turn ended.
     * @param username The username (identifier) of this client, who has claimed a route.
     * @param routeID The unique identifier of the route that was claimed.
     */
    @Override
    public void claimRoute(String username, int routeID){
        Route myRoute = Route.getRouteByID(routeID);
        int routeSize = myRoute.getLength();

        //change GUI elements
        map.claimRoute(game.getPlayerByName(username).getColor(), routeID);
        game.getMyself().incrementNumRoutesClaimed();
        List<Integer> toRemove=game.getCardsToDiscard();

        for(int i=0;i<toRemove.size();i++)
            game.removeTrainCardByInt(toRemove.get(i));

        game.addToScore(myRoute.getPointValue());
        game.removeTrains(routeSize);

        //raise GUI change flags
        game.iHaveDifferentTrainCards(true);
        game.aPlayerHasChanged(true);
        game.notifyObserver();

        ClientState.INSTANCE.setState(new NotMyTurnState());
    }

    /**
     * Called by the server when this client successfully draws destination cards.
     * @pre This client is the one that attempted to draw destination cards. The server must have at
     * least 1 destination card.
     * @post This client is given the opportunity to decide which destination cards they want to keep,
     * keeping at minimum 1 destination card.
     * @param username The username (identifier) of the client that sent the request.
     * @param destCards The destination cards that the server has given to the client, represented by
     *                  a list of integers, from 0-29.
     */
    @Override
    public void drawDestCards(String username, List<Integer> destCards){
        if (destCards.size() != 0) { //if there are no destCards returned, don't cause the fragment to switch
            game.setPossibleDestCards(destCards);
            game.iHavePossibleDestCards(true);
            ClientState.INSTANCE.setState(new ReturnDestCardState());
        }
    }

    /**
     * Called by the server when this client has successfully drawn a train card from the deck.
     * @pre This client is the one that attempted to draw a train card. The server must have a train
     * card to give to the client.
     * @post This client will add the received train card and update it's GUI to match the type of
     * train card that it has received. The client should decrement the number of train cards in the
     * train card deck.
     * @param username The username (identifier) of the client that sent the request.
     * @param trainCard The train card received from the server represented as an integer, from 0-8.
     */
    @Override
    public void drawTrainCardDeck(String username, int trainCard){
        Player myself = game.getMyself();
        myself.addTrainCardByInt(trainCard);
        game.iHaveDifferentTrainCards(true);

        if (ClientState.INSTANCE.getState() instanceof MyTurnState) {
            ClientState.INSTANCE.setState(new DrawSecondTrainCardState());
        } else {
            ClientState.INSTANCE.setState(new NotMyTurnState());
        }
    }

    /**
     * Called by the server when this client has successfully drawn a train card from the face up cards.
     * @pre This client is the one that attempted to draw a train card. The server must have a train
     * card to give to the client.
     * @post This client will add the received card card and update it's GUI to match the type of train
     * card that is has received. The client should decrement the number of train cards in the train
     * card deck.
     * @param username The username (identifier) of the client that sent the request.
     * @param trainCard The train card received from the server represented as an integer, from 0-8.
     */
    @Override
    public void drawTrainCardFaceUp(String username, int trainCard){
        Player myself = game.getMyself();
        myself.addTrainCardByInt(trainCard);
        game.iHaveDifferentTrainCards(true);

        if (ClientState.INSTANCE.getState() instanceof MyTurnState &&
                TrainCard.getTrainCard(trainCard) != TrainCard.WILD) {
            ClientState.INSTANCE.setState(new DrawSecondTrainCardState());
        } else {
            ClientState.INSTANCE.setState(new NotMyTurnState());
        }
    }

    /**
     * Called by the server when this client has sent back destination cards that it does not want to
     * keep.
     * @pre This client recently received new destination cards from the server.
     * @post This client will no longer have any access to the destination cards that it sent back to
     * the server. Based on the number of destination cards sent to the server, the client should update
     * the size of it's destination card appropriately.
     * @param username The username (identifier) of the client that sent the request.
     * @param destCard The destination that was returned to the server.
     */
    @Override
    public void returnDestCards(String username, int destCard){
        game.getMyself().iHaveDifferentDestCards(true);
        game.iHaveReturnedDestCards(true);

        ClientState.INSTANCE.setState(new NotMyTurnState());
    }

    /**
     *  Called by the server signalling the first turn destination cards that are returned by the client.
     * @pre The client has recently started the game and has decided on a destination card to return to
     * the server. In the case that the client sends none, the server would not call this method.
     * @post The client will no longer have any access to the destination card it has returned to
     * the server. The client should update the size of it's destination card deck size appropriately.
     * @param username The username (identifier) of the client that sent the request.
     * @param cardReturned The destination card that was returned to the server.
     */
    @Override
    public void returnFirstDestCards(String username, int cardReturned){
        game.getMyself().iHaveDifferentDestCards(true);
        game.iHaveReturnedDestCards(true);
    }

    /**
     * Called by the server anytime a client other than this client has made an action that this client
     * should be aware of. This covers actions such as drawing cards, returning cards, and claiming
     * routes.
     * @pre The username inside this method is never the same as the client that has this method called.
     * @post This client will update the information of the username that is passed via this method in
     * the GUI to match how the server has described them.
     * @param username The username (identifier) of the client that caused the game history to be generated.
     * @param message The message from the server dictating what the username client has done.
     * @param numTrainCars The new number of train cars held by the username client.
     * @param numTrainCardsHeld The new number of train cards held by the username client.
     * @param numDestCardsHeld The new number of destination cards held by the username client.
     * @param numRoutesOwned The new number of routes owned by the username client.
     * @param score The new score held by the username client.
     * @param claimedRouteNumber The route number of the route claimed by the username client. Will be
     *                           -1 if the username client did not claim a route.
     * @param faceUpIndex The index of the face up card drawn by the username client. Will be -1 if the
     *                    username client did not draw from face up.
     */
    public void addHistory(String username, String message, int numTrainCars, int numTrainCardsHeld,
                           int numDestCardsHeld, int numRoutesOwned, int score, int claimedRouteNumber,
                           int trainCardDeckSize, int destCardDeckSize, int faceUpIndex){
        chatModel.addHistory(username,message);
        AbstractPlayer player = game.getPlayerByName(username);

        //updates the players score
        if(player.getScore() < score) { //server says a player has a higher score
            player.setScore(score);

        }
        //updates the players number of train cars
        if(player.getNumTrains() > numTrainCars) { //server says a player has fewer trains
            player.setNumTrains(numTrainCars);
        }

        //if the number of cards held by a player has increased, update that player
        if(player.getNumCards() < numTrainCardsHeld ) {
            player.setNumCards(numTrainCardsHeld);
        } else if (player.getNumCards() > numTrainCardsHeld) { //server says a player has less train cards
            player.setNumCards(numTrainCardsHeld);
        }

        //if the number of dest cards held by this player has changed, update that player
        //leave the client to update it's own dest cards
        if(player.getNumDestCard() != numDestCardsHeld && !username.equals(game.getMyUsername())) {
            int destCardNumDifference = player.getNumDestCard() - numDestCardsHeld;
            player.setDestCardNum(numDestCardsHeld);
        }

        //if the number of routes for this player has increased, update that player
        if(player.getNumRoutes() < numRoutesOwned) {
            map.claimRoute(game.getPlayerByName(username).getColor(), claimedRouteNumber);
            Route.getRouteByID(claimedRouteNumber).setUser(username);
            Route.getRouteByID(claimedRouteNumber).setClaimed(true);
            player.setNumRoutes(numRoutesOwned);
            player.setScore(score);
        }

        if (game.getDestCardDeckSize() != destCardDeckSize) {
            game.setDestCardDeckSize(destCardDeckSize);
            game.iHaveDifferentDestDeckSize(true);
        }
        if ((game.getTrainCardDeckSize() - trainCardDeckSize) > 1){
            /*
            If the new train deck size has decremented more than 1, then we must have dealt an entire new
            hand of face up cards, so we'll let that result take care of it and not raise the new train card
            deck size flag here.
             */
            game.setTrainCardDeckSize(trainCardDeckSize);
        } else if (game.getTrainCardDeckSize() < trainCardDeckSize){ //the deck size has increased
            game.setTrainCardDeckSize(trainCardDeckSize);
        } else if (game.getTrainCardDeckSize() - 1 == trainCardDeckSize && faceUpIndex == -1) { //if decrementing by one and was by deck
            game.setTrainCardDeckSize(trainCardDeckSize);
            game.iHaveDifferentTrainDeckSize(true);
        } else if (game.getTrainCardDeckSize() != trainCardDeckSize) {
            game.setTrainCardDeckSize(trainCardDeckSize);
        }

        game.aPlayerHasChanged(true);
        game.notifyObserver();
    }

    /**
     * Called by the server whenever the face up cards for this client's game changes.
     * @pre The client has old face up card information.
     * @post The client has new face up card information that matches the server's description.
     * @param trainCards The train cards to be displayed on this client's GUI, represented by integers
     *                   from 0-8.
     */
    public void replaceFaceUpCards(List<Integer> trainCards, List<Boolean> faceUpDifferences) {
        game.setFaceUpCards(trainCards);
        game.setFaceUpDifferences(faceUpDifferences);
        game.iHaveDifferentFaceUpCards(true);
        if (ClientState.INSTANCE.getState().getClass() == DrawSecondTrainCardState.class)
            ClientState.INSTANCE.setState(new DrawSecondTrainCardState());
        //game.notifyObserver();
    }


    /**
     * Called by the server whenever this client makes a request that the server has rejected.
     * @pre This client has made a request that the server has rejected for some reason.
     * @post This client should display the message from the server on the screen for the user to see.
     * @param message The message from the server describing why the request was denied.
     */
    public void showRejectMessage(String message) {
        Game.getGameInstance().getServerError().setMessage(message);
    }

    //NOTE: Adjusted to make compatible with List of Strings for longest route.
    public void endGame(List<String> players, List<Integer> numRoutesClaimed,  List<Integer> pointsFromRoutes, List<Integer> destCardPtsAdded,
                        List<Integer> destCardPtsSubtracted, List<Integer> totalPoints,
                        List<String> ownsLongestRoute) {
        game.setGameOver(true);
        List<AbstractPlayer> endGamePlayerList = new ArrayList<>();

        String winnerName = ""; //blank initialization
        int winnerScore = -1000; //arbitrary int that is always starts off impossibly low
        for(int i=0;i<players.size();i++)
        {
            AbstractPlayer myPlayer = new VisiblePlayer(players.get(i), 0);
            myPlayer.setNumRoutes(numRoutesClaimed.get(i));
            myPlayer.setClaimedRoutePoints(pointsFromRoutes.get(i));
            myPlayer.setDestinationPoints(destCardPtsAdded.get(i));
            myPlayer.setDestinationPointsLost(destCardPtsSubtracted.get(i));
            myPlayer.setScore(totalPoints.get(i));
            myPlayer.setColor(i);
            if(ownsLongestRoute.contains(myPlayer.getMyUsername()))
            {
                myPlayer.setLongestRoutePoints(10);
            }
            if (myPlayer.getScore() > winnerScore){
                winnerName = myPlayer.getMyUsername();
                winnerScore = myPlayer.getScore();
            }

            endGamePlayerList.add(myPlayer);
        }
        game.setPlayerMapForEndGame(endGamePlayerList);
        game.setWinner(winnerName);

    }

    public void finalRound(String playerToEndOn) {


    }

    public void turn(String username) {
        if (username.equals(game.getMyUsername())) {
            ClientState.INSTANCE.setState(new MyTurnState());
        } else {
            ClientState.INSTANCE.setState(new NotMyTurnState());
        }
        List<AbstractPlayer> players=game.getVisiblePlayerInformation();
        for(int i=0;i<players.size();i++)
        {
            if(players.get(i).getMyUsername().equals(username))
            {
                players.get(i).setMyTurn(true);
            }
            else
            {
                players.get(i).setMyTurn(false);
            }
        }
        game.aPlayerHasChanged(true);
        game.notifyObserver();
    }
}
