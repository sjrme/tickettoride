package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.State.CommandType;
import model.State.FirstRound;
import model.State.TurnState;
import results.Result;
import results.game.ChatResult;
import results.game.ClaimRouteResult;
import results.game.DrawThreeDestCardsResult;
import results.game.DrawTrainCardFromDeckResult;
import results.game.DrawTrainCardFromFaceUpResult;
import results.game.EndGameResult;
import results.game.FinalRoundResult;
import results.game.GameHistoryResult;
import results.game.ReplaceFaceUpCardsResult;
import results.game.ReturnFirstDestCardResult;
import results.game.StartGameResult;
import results.game.TurnResult;

/**
 * Container class for started games.
 * @author Stephen Richins
 */

public class StartedGame implements java.io.Serializable{

    private String gameName;
    private Map<String, Player> allPlayers = new HashMap<>();
    private List<String> playerOrder = new ArrayList<>();
    private Board board;
    private List<Chat> allChats = new ArrayList<>();
    private boolean replaceFaceUpFlag = false;
    private int turnPointer = 0;
    private TurnState turnState = new FirstRound(this);
    private Result gameHistory;
    private Result turnResult = null;
    private Result endGameResult = null;
    private Result finalRoundResult = null;
    private String afterFinalTurnPlayer = null;
    private boolean secondPass = false;
    private List<ChatHistoryData> chatResultList = new ArrayList<>();
    private List<ChatHistoryData> gameHistories = new ArrayList<>();
    StartedGame(UnstartedGame unstartedGame) {
        this.gameName = unstartedGame.getGameName();
    }

    //sorry stephen
    public List<Integer> getFaceUpCards(){
        return board.getFaceUpCardCodes();
    }

/*****************************************START GAME***********************************************/
    /**
     * Must deal cards before setting up deck for correct error handling.
     * @return True if successful, false otherwise.
     */
    List<Result> preGameSetup(List<String> userNames) {

        board = new Board();
        final int TRAIN_CARD_DRAW = 4;

        for(int a = 0; a < userNames.size(); a++) {
            Player newPlayer = new Player(userNames.get(a));
            newPlayer.addTrainCards(board.drawTrainCardsFromDeck(TRAIN_CARD_DRAW));
            newPlayer.addDestCards(board.drawDestCards());
            newPlayer.setPlayerColor(a);
            allPlayers.put(userNames.get(a), newPlayer);
            playerOrder.add(userNames.get(a));
        }
        replaceFaceUpFlag = board.getReplaceFaceUpFlag();
        return startGameResults();
    }

    /**
     * Creates a list of results for the startGame command.
     * @return
     */
    private List<Result> startGameResults() {

        Set<Player> playerSet = new HashSet<>();
        playerSet.addAll(allPlayers.values());
        List<Result> allResults = new ArrayList<>();

        List<String> playerNames = new ArrayList<>();
        for (Player player : playerSet) {
            playerNames.add(player.getUsername());
        }

        for (Player player : playerSet) {

            List<Integer> destCardKeys = new ArrayList<>();
            for (DestCard destCard : player.getDestCards()) {
                destCardKeys.add(DestCard.getDestCardKey(destCard));
            }

            Result nextPlayer = new StartGameResult(player.getUsername(),
                                                    gameName,
                                                    playerNames,
                                                    destCardKeys,
                                                    player.getTrainCardCodes(),
                                                    board.getFaceUpCardCodes());
            allResults.add(nextPlayer);
        }
        setTurnResult();
        return allResults;
    }

    public int getDestCardDeckSize(){
        return board.getDestCardDeckSize();
    }

    public int getTrainCardDeckSize(){
        return board.getTrainCardDeckSize();
    }

    public List<ChatHistoryData> getAllGameHistory(){
        return gameHistories;
    }

    public List<ChatHistoryData> getAllChatHistory(){
        return chatResultList;
    }

/******************************************DrawDestCards*******************************************/

    /**
     * Controls drawThreeDestCard command from the game. Passes information necessary to to the
     * board and the players.
     * @param playerName The player drawing the destination cards.
     * @return Result to send to the client
     * @throws GamePlayException For bad commands
     */
     Result drawThreeDestCards(String playerName) throws GamePlayException {

        throwIfNotPlayersTurn(playerName);

        Player currentPlayer = allPlayers.get(playerName);
        ArrayList<DestCard> drawnDestCards;

        if (currentPlayer != null) {

            if(board.emptyDestCardDeck()) {
                throw new GamePlayException("No destination cards to draw.");
            } else if (board.getDestCardDeck().size() > 2 ) {
                turnState.switchState(CommandType.DRAW_THREE_DEST_CARDS);
            } else if (board.getDestCardDeck().size() == 2 ){
                turnState.switchState(CommandType.DRAW_TWO_DEST_CARDS);
            } else {
                turnState.switchState(CommandType.DRAW_ONE_DEST_CARD);
            }

            drawnDestCards = board.drawDestCards();
            currentPlayer.addPossibleDestCards(drawnDestCards);
            currentPlayer.addDestCards(drawnDestCards);
        } else {
            throw new GamePlayException("Invalid player name");
        }

        String message = playerName + " drew destination cards.";
        setGameHistoryResult(playerName, message, -1, -1);
         setEndGameResult();
        return drawDestCardResults(playerName, drawnDestCards);
    }

    /**
     * Creates the result to send to the client after calling drawThreeDestCards.
     * @param playerName Player making the command.
     * @param drawnDestCards List of destination cards the player drew.
     * @return The Result object to send to the client.
     */
    private Result drawDestCardResults(String playerName, List<DestCard> drawnDestCards) {

        List<Integer> drawnCardKeys = new ArrayList<>();
        for (DestCard destCard : drawnDestCards) {
            drawnCardKeys.add(DestCard.getDestCardKey(destCard));
        }

        return new DrawThreeDestCardsResult(playerName, drawnCardKeys);
    }

    /**
     * Controls returnDestCard command in game.
     * @param playerName Player that is returning destination cards.
     * @param returnedCardKey Key of the card that is being returned
     * @return The Result of the action given.
     * @throws GamePlayException For bad commands
     */
    Result returnDestCard(String playerName, int returnedCardKey)
            throws GamePlayException {
        throwIfNotPlayersTurn(playerName);
        Player currentPlayer = allPlayers.get(playerName);

        if (currentPlayer != null) {

            if (currentPlayer.invalidDestCard(DestCard.getDestCardByID(returnedCardKey))) {
                throw new GamePlayException("You have not drawn that destination card.");
            }
            if (returnedCardKey < 30) {
                turnState.switchState(CommandType.RETURN_DEST_CARD);
            } else {
                turnState.switchState(CommandType.RETURN_NO_DEST_CARD);
            }

            DestCard returnedCard = board.getDestCardMap().get(returnedCardKey);
            currentPlayer.removeDestCards(returnedCard);
            currentPlayer.clearPossibleDestCards();
            board.pushBackDestCards(returnedCard);

        } else {
            throw new GamePlayException("Invalid player name");
        }

       String message = playerName + " returned a destination card.";
       setGameHistoryResult(playerName, message, -1, -1);
        setEndGameResult();
        return new ReturnFirstDestCardResult(playerName, returnedCardKey);
    }



/******************************************DrawTrainCards******************************************/
    Result drawTrainCardFromDeck(String playerName) throws GamePlayException {
        throwIfNotPlayersTurn(playerName);
        final int TRAIN_CARD_DRAW = 1;
        Player currentPlayer = allPlayers.get(playerName);
        List<TrainCard> trainCard;


        if (currentPlayer != null) {
            if (board.emptyTrainCardDeck()) {
                throw new GamePlayException("No train cards to draw.");
            }

            turnState.switchState(CommandType.DRAW_TRAIN_CARD_FROM_DECK);

            trainCard = board.drawTrainCardsFromDeck(TRAIN_CARD_DRAW);
            currentPlayer.addTrainCards(trainCard);
        } else {
            throw new GamePlayException("Invalid player name");
        }
        board.reshuffleIfEmpty();

        String message = playerName + " drew a train card from the deck.";
        setGameHistoryResult(playerName, message, -1, -1);
        setEndGameResult();
        return new DrawTrainCardFromDeckResult(playerName, TrainCard.getTrainCardKey(trainCard.get(0)));
    }

    //How does player whose turn it is get trainCard deck update after reshuffle?
    //Draw one card, and there are none left, including discard?
    //Replace all face up cards and need to reshuffle mid replacement?

    /**
     *
     * @param playerName
     * @param index
     * @return
     * @throws GamePlayException
     */
    List<Result> drawTrainCardFromFaceUp(String playerName, int index) throws GamePlayException{
        throwIfNotPlayersTurn(playerName);
        Player currentPlayer = allPlayers.get(playerName);
        TrainCard drawnCard;
        int cardDrawnKey;

        //save previous cards
        List<Integer> previousFaceUpCards = board.getFaceUpCardCodes();
        boolean flipAll = false;
        if (currentPlayer != null) {

            final int LOCOMOTIVE_INDEX = 8;

            if (board.noFaceUpCards()) {
                throw new GamePlayException("No train cards to draw.");
            }

            System.out.println("draw @" +index + " w/ " + board.getFaceUpCardCodes().size());
            cardDrawnKey = board.getFaceUpCardCodes().get(index);

            int counter = 0;

            drawnCard = board.drawFaceUpCard(index);

            List<TrainCard> passToPlayer = new ArrayList<>();
            passToPlayer.add(drawnCard);
            currentPlayer.addTrainCards(passToPlayer);

            board.reshuffleIfEmpty();
            //replaceFaceUpFlag = board.getReplaceFaceUpFlag();


            while (board.getReplaceFaceUpFlag()) {
                replaceFaceUpCards(playerName, counter);
                flipAll = true;
                counter++;
            }

            if (cardDrawnKey == LOCOMOTIVE_INDEX) {
                turnState.switchState(CommandType.FACEUP_LOCOMOTIVE);
            } else {
                turnState.switchState(CommandType.FACEUP_NON_LOCOMOTIVE);
            }

        } else {
            throw new GamePlayException("Invalid player name");
        }

        //check for card differences
        List<Boolean> faceUpDifferences = new ArrayList<>();
        if (!flipAll) {
            List<Integer> newFaceUpCards = board.getFaceUpCardCodes();
            for (int i = 0; (i < newFaceUpCards.size() && i < previousFaceUpCards.size()); i++) {
                if (previousFaceUpCards.get(i).equals(newFaceUpCards.get(i)))
                    faceUpDifferences.add(false);
                else
                    faceUpDifferences.add(true);
            }
            while (faceUpDifferences.size() < 5) {
                if (newFaceUpCards.size() > previousFaceUpCards.size()) //if there are more cards than before, flip them
                    faceUpDifferences.add(true);
                else
                    faceUpDifferences.add(false); //if there aren't new cards, don't flip them
            }
            //override the index that was drawn to true
            faceUpDifferences.set(index, true);
        } else
            for (int i = 0; i < board.getFaceUpTrainCards().size(); i++)
                faceUpDifferences.add(true);

        String message = playerName + " drew a " + drawnCard.getPrettyname() + " face-up train card.";
        setGameHistoryResult(playerName, message, -1, index);
        setEndGameResult();
        List<Result> returnResults = new ArrayList<>();
        returnResults.add(new DrawTrainCardFromFaceUpResult(playerName, cardDrawnKey));
        returnResults.add(new ReplaceFaceUpCardsResult(board.getFaceUpCardCodes(), faceUpDifferences));
        return returnResults;
    }


    Result replaceFaceUpCards(String playerName, int counter) {
        if (counter > 3) {
            board.setReplaceUpFlagToFalse();
            board.replaceFaceUpCards();
            return null;
        }
        List<Integer> newFaceUpCards = board.replaceFaceUpCards();
        replaceFaceUpFlag = board.getReplaceFaceUpFlag();
        setGameHistoryResult(playerName, "All faceup cards have been replaced", -1, -1);
        List<Boolean> replaceAllTrue = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            replaceAllTrue.add(true);
        return new ReplaceFaceUpCardsResult(newFaceUpCards, replaceAllTrue);
    }

    /*****************************************ClaimRoute*******************************************/
    Result claimRoute(String playerName, int routeId, List<Integer> trainCards) throws GamePlayException {

        Player currentPlayer = allPlayers.get(playerName);
        if (currentPlayer != null) {
            throwIfNotPlayersTurn(playerName);
            List<TrainCard> returnedTrainCards = convertKeysToTrainCards(trainCards);

            if(board.routeIsClaimed(routeId)) {
                throw new GamePlayException("Route has already been claimed.");
            }
            if(board.incorrectCards(routeId, returnedTrainCards)){
                throw new GamePlayException("Cannot claim route with cards given.");
            }
            if (board.notEnoughCars(routeId, currentPlayer.getNumOfCars())) {
                throw new GamePlayException("Not enough cars to claim route.");
            }
            if (board.doubleRouteFailure(routeId, allPlayers.size(), playerName)) {
                throw new GamePlayException("Can not claim double route.");
            }

            Route route = board.getRouteMap().get(routeId);
            turnState.switchState(CommandType.CLAIM_ROUTE);
            board.claimRoute(routeId, currentPlayer.getPlayerColor(), playerName);
            currentPlayer.addNumOfRoutes();
            currentPlayer.removeCars(route.getLength());
            currentPlayer.addScore(route.getPointValue());
            currentPlayer.removeTrainCards(returnedTrainCards);
            board.discardTrainCards(returnedTrainCards);
            currentPlayer.calculateContRoute(route.getStartCity(), route.getEndCity(), route.getLength());
            currentPlayer.addClaimedRoute(routeId);


            board.reshuffleIfEmpty(); //CASE: Empty deck and discard pile before claiming route.
            board.refillFaceUpFromDiscard(); //CASE: If FaceUpCards < 5 WHEN empty deck and discard pile before claiming route.



            if(currentPlayer.finalTurn()){
                afterFinalTurnPlayer = playerOrder.get(turnPointer);
                setFinalRoundResult();
            }


        } else {
            throw new GamePlayException("Invalid player name");
        }

        String message = playerName + " claimed route " + Integer.toString(routeId);
        setGameHistoryResult(playerName, message, routeId, -1);
        setEndGameResult();
        return new ClaimRouteResult(playerName, routeId);
    }

    private List<TrainCard> convertKeysToTrainCards(List<Integer> trainCards) {
        List<TrainCard> returnTrainCards =  new ArrayList<>();
        for(Integer a : trainCards) {
            returnTrainCards.add(TrainCard.getTrainCard(a));
        }
        return returnTrainCards;
    }

    /**********************************RESULT SETTERS***********************************************/

    private void setEndGameResult() {

        if (!playerOrder.get(turnPointer).equals(afterFinalTurnPlayer)) {
            return;
        }
        if (!secondPass){
            secondPass = true;
            return;
        }
        List<Integer> numRoutesClaimed = new ArrayList<>();
        List<Integer> pointsFromRoutes = new ArrayList<>();
        List<Integer> pointsAdded = new ArrayList<>();
        List<Integer> pointsSubtracted = new ArrayList<>();
        List<Integer> totalPoints = new ArrayList<>();
        List<String> largestSizeOwners = new ArrayList<>();
        int largestSize = 0;


        for (int a = 0; a < playerOrder.size(); a++) {
            Player player = allPlayers.get(playerOrder.get(a));
            numRoutesClaimed.add(player.getNumOfRoutesOwned());
            pointsFromRoutes.add(player.getPoints());
            pointsAdded.add(player.addDestCardPoints());
            pointsSubtracted.add(player.subtractDestCardPoints());

            if (player.getLargestContRouteSize() > largestSize) {
                largestSize = player.getLargestContRouteSize();
                largestSizeOwners.clear();
                largestSizeOwners.add((player.getUsername()));
            } else if (player.getLargestContRouteSize() == largestSize) {
                largestSizeOwners.add(player.getUsername());
            }
        }

        for (int a = 0; a < largestSizeOwners.size(); a++) {
            allPlayers.get(largestSizeOwners.get(a)).addScore(10);
        }

        for (int a = 0 ; a < playerOrder.size(); a++) {
            Player player = allPlayers.get(playerOrder.get(a));
            totalPoints.add(player.getPoints());
        }
        endGameResult = new EndGameResult(playerOrder, numRoutesClaimed, pointsFromRoutes, pointsAdded,
                pointsSubtracted, totalPoints, largestSizeOwners);
    }

    private void setFinalRoundResult() {
            finalRoundResult = new FinalRoundResult(afterFinalTurnPlayer);
    }

    public void setTurnResult() {
        turnResult = new TurnResult(playerOrder.get(turnPointer));
    }

    public void setGameHistoryResult(String playerName, String message, int routeNumber, int faceUpIndex){
        Player player = allPlayers.get(playerName);
        gameHistories.add(new ChatHistoryData(playerName, message));
        gameHistory = new GameHistoryResult(playerName, message,
                player.getNumOfCars(), player.getSizeOfTrainCardHand(),
                player.getSizeOfDestCardHand(), player.getNumOfRoutesOwned(),
                player.getPoints(), routeNumber, board.getTrainCardDeckSize(), board.getDestCardDeck().size(),
                faceUpIndex);
    }
    /******************************************CHAT************************************************/

    Result addChat(String playerName, String message) {
        allChats.add(new Chat(playerName, message));
        chatResultList.add(new ChatHistoryData(playerName, message));
        return new ChatResult(playerName, message);
    }

    List<ChatHistoryData> getChatResultList(){
        return chatResultList;
    }


    /****************************************TURN STATE********************************************/
    private boolean throwIfNotPlayersTurn(String playerName) throws GamePlayException {
        if(playerOrder.get(turnPointer).equals(playerName)) {
            return true;
        }
        throw new GamePlayException("Not your turn!");
    }

    public void advancePlayerTurn() {
        if (playerOrder.size()-1 == turnPointer){
            turnPointer = 0;
        } else {
            turnPointer++;
        }
        setTurnResult();
    }

    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }


    /*****************************************Getters**********************************************/

    Result getFinalTurnResult() {
        return finalRoundResult;
    }

    Result getEndGameResult() {
        return endGameResult;
    }

    Result getThenNullifyTurnResult() {
        Result turn = turnResult;
        //turnResult = null;
        return turn;
    }

    Result getGameHistory() {
        return gameHistory;
    }

    public String getGameName() {
        return gameName;
    }

    Map<String, Player> getAllPlayers() {
        return allPlayers;
    }

    List<String> getAllPlayerNames(){
        List<String> playerNames = new ArrayList<>();
        for (String name : allPlayers.keySet()){
            playerNames.add(name);
        }
        return playerNames;
    }

    List<PlayerData> getPlayerData(){
        List<PlayerData> playerData = new ArrayList<>();
        for (Player player : allPlayers.values()){
            String name = player.getUsername();
            int numTrainCards = player.getSizeOfTrainCardHand();
            int numDestCards = player.getSizeOfDestCardHand();
            int numTrains = player.getNumOfCars();
            int score = player.getPoints();
            List<Integer> routes = player.getClaimedRoutes();
            PlayerData myData = new PlayerData(name, numTrainCards, numDestCards, numTrains, score, routes);
            playerData.add(myData);
        }
        return playerData;
    }

    PrivatePlayerData getPrivateData(String username) {
        Player player = allPlayers.get(username);
        List<DestCard> destCards = player.getDestCards();
        List<Integer> destCardInts = new ArrayList<>();
        for (DestCard card : destCards){
            destCardInts.add(DestCard.getDestCardKey(card));
        }
        List<Integer> trainCards = player.getTrainCardCodes();
        List<Integer> possibleDestCards = player.getPossibleDestCards();
        return new PrivatePlayerData(destCardInts, trainCards, possibleDestCards, false);
    }

    boolean getReplaceFaceUpFlag() {
        return replaceFaceUpFlag;
    }

    String getAfterFinalTurnPlayer() {
        return afterFinalTurnPlayer;
    }

    public List<String> getPlayerOrder() {
        return playerOrder;
    }

    public int getTurnPointer() {
        return turnPointer;
    }

    void printBoardState() {
         List<Boolean> destCardSeen = new ArrayList<>();
         for (int a = 0; a < 30; a++) {
             destCardSeen.add(false);
         }

         int numOfPurpleCards = 0;
         int numOfWhiteCards = 0;
         int numOfBlueCards = 0;
         int numOfYellowCards = 0;
         int numOfOrangeCards = 0;
         int numOfBlackCards = 0;
         int numOfRedCards = 0;
         int numOfGreenCards = 0;
         int numOfWildCards = 0;

        System.out.println("BOARD");
        System.out.println("Num of train cards in deck: " + board.getTrainCardDeck().size());
        System.out.println("Num of dest cards in deck: " + board.getDestCardDeck().size() + "\n");
        System.out.println("Turn state for next turn: " + turnState.getPrettyName());
        System.out.println("Next person's turn" + playerOrder.get(turnPointer));

        for (int a = 0; a < allPlayers.size(); a++) {
            Player player = allPlayers.get(playerOrder.get(a));
            System.out.println("Player: " + player.getUsername());
            System.out.println("Number of cars: " + player.getNumOfCars());
            System.out.println("  Size of train card hand: " + player.getSizeOfTrainCardHand());

            numOfRedCards += player.getNumOfRedCards();
            System.out.println("    Red: " + player.getNumOfRedCards());

            numOfGreenCards += player.getNumOfGreenCards();
            System.out.println("    Green: " + player.getNumOfGreenCards());

            numOfBlueCards += player.getNumOfBlueCards();
            System.out.println("    Blue: " + player.getNumOfBlueCards());

            numOfYellowCards += player.getNumOfYellowCards();
            System.out.println("    Yellow: " + player.getNumOfYellowCards());

            numOfBlackCards += player.getNumOfBlackCards();
            System.out.println("    Black: " + player.getNumOfBlackCards());

            numOfPurpleCards += player.getNumOfPurpleCards();
            System.out.println("    Purple: " + player.getNumOfPurpleCards());

            numOfOrangeCards += player.getNumOfOrangeCards();
            System.out.println("    Orange: " + player.getNumOfOrangeCards());

            numOfWhiteCards += player.getNumOfWhiteCards();
            System.out.println("    White: " + player.getNumOfWhiteCards());

            numOfWildCards += player.getNumOfWildCards();
            System.out.println("    Wild: " + player.getNumOfWildCards());

            System.out.println("  Size of dest card hand:  " + player.getSizeOfDestCardHand());
            for (int b = 0; b < player.getDestCards().size(); b++) {
                DestCard destCard = player.getDestCards().get(b);
                int destCardKey = DestCard.getDestCardKey(destCard);
                if (!destCardSeen.get(destCardKey)){
                    destCardSeen.set(destCardKey, true);
                } else {
                    System.out.println("ERROR: DEST CARD SEEN TWICE.");
                }

                System.out.println("  " + destCardKey);
            }
            System.out.println("  Size of drawndest:  " + player.getDestCards().size());
        }
        numOfRedCards += board.getNumOfRedCards();
        numOfGreenCards += board.getNumOfGreenCards();
        numOfBlueCards += board.getNumOfBlueCards();
        numOfYellowCards += board.getNumOfYellowCards();
        numOfBlackCards += board.getNumOfBlackCards();
        numOfPurpleCards += board.getNumOfPurpleCards();
        numOfOrangeCards += board.getNumOfOrangeCards();
        numOfWhiteCards += board.getNumOfWhiteCards();
        numOfWildCards += board.getNumOfWildCards();

        System.out.println("Game number of red: " + numOfRedCards);
        System.out.println("Game number of green: " + numOfGreenCards);
        System.out.println("Game number of blue: " + numOfBlueCards);
        System.out.println("Game number of yellow: " + numOfYellowCards);
        System.out.println("Game number of black: " + numOfBlackCards);
        System.out.println("Game number of purple: " + numOfPurpleCards);
        System.out.println("Game number of orange: " + numOfOrangeCards);
        System.out.println("Game number of white: " + numOfWhiteCards);
        System.out.println("Game number of wild: " + numOfWildCards);

        for (int a = 0; a < board.getDestCardDeck().size(); a++) {
            DestCard destCard = board.getDestCardDeck().get(a);
            int destCardKey = DestCard.getDestCardKey(destCard);

            if (!destCardSeen.get(destCardKey)){
                destCardSeen.set(destCardKey, true);
            } else {
                System.out.println("ERROR: DEST CARD SEEN TWICE.");
            }
        }

        boolean success = true;
        for (int a = 0; a < destCardSeen.size(); a++) {
            if (!destCardSeen.get(a)){
                success = false;
                break;
            }
        }

        if (success){
            System.out.println("All dest cards accounted for.");
        } else {
            System.out.println("ERROR: MISSING DEST CARDS");
        }

        System.out.println("CLAIMED ROUTES: ");
        Set<Route> routeSet = new HashSet<>(board.getRouteMap().values());
    }

}
