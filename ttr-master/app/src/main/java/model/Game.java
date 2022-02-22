package model;


import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.Observable;
import interfaces.Observer;

/**
 * Represents the game object.
 * This class is a singleton with a private constructor with only one instance of the object at all times
 * that can only be constructed on the first call of getGameInstance()
 */
public class Game implements Observable{
    private static final int STARTING_HAND_SIZE = 4;
    private static final int FACE_UP_SIZE = 5;
    private static final int DEST_CARD_CHOICES = 3;
    
    private static Game myGame;
    private Game(){}
    
    /**
     * If instance of the game is currently null a new instance will be created
     *@return The only instance of the game
     */
    public static Game getGameInstance(){
        if (myGame == null)
            myGame = new Game();
        
        return myGame;
    }
    
    private String gameName;
    private Player myself;
    private Map<String, AbstractPlayer> playerMap = new HashMap<>();
    private List<Integer> trainCards = new ArrayList<>();
    private ArrayList<Observer> observers = new ArrayList<>();
    private int currentlySelectedRouteID;
    private ServerError serverError = new ServerError();
    private Deck gameDecks = Deck.getInstance();
    
    /**
     * retrieves the abstract player object associated with the username
     * @pre this method must be declared on the only instance of the game object
     * @pre the game needs to have been initialized before calling this method or else it will return null
     * @param username is the username of a player that is in the current game
     * @return The AbstractPlayer object associated with the username
     * @post The game object and player object will not be modified
     * @post if the username is not currently in the game null will be returned
     */
    public AbstractPlayer getPlayerByName(String username) {
        return playerMap.get(username);
    }
    
    /**
     * retrieves the players in the game
     * @pre game must be initialized before calling this method
     * @pre must be called on the game instance
     * @return list of abstract player objects currently in the game, if no players are initialized an empty list will be returned
     * @post nothing in the game is modified by this function
     *
     */
    public List<AbstractPlayer> getVisiblePlayerInformation() {
        List<AbstractPlayer> playerListToDisplay = new ArrayList<>();
        for (AbstractPlayer myPlayer : this.playerMap.values()){
            playerListToDisplay.add(myPlayer);
        }
        return playerListToDisplay;
    }

    public void setPlayerMapForEndGame(List<AbstractPlayer> endGamePlayers){
        this.playerMap.clear();
        for (AbstractPlayer myPlayer : endGamePlayers){
            this.playerMap.put(myPlayer.getMyUsername(), myPlayer);
        }
    }
    
    /**
     * initializes the game instance
     * @pre must be called on the game instance
     * @pre This method should only be called once for the duration of the game, else information will be overwritten
     * @post Any past information inside the game will be overwritten
     * @post initialized the values of the game to store the players, game name, and cards that are currently face up
     * @param myself Player object of the current player on the client side, should not be null
     * @param gameName String associated with the current game instance as a identifier to this game, should not be null
     * @param playerNames List of Strings of the usernames of the players in the current game. Length must >=2 and <=5, should not be null
     * @param faceUpCards List of integer values associated with the face up cards, each integer must be <=110 and be
     *                    be the only instance of the integer within the game associated with a train card, should not be null. Length must be equal to 4
     */
    public void initializeMyGame(Player myself, String gameName, List<String> playerNames, List<Integer> faceUpCards){
        this.myself = myself;
        this.gameName = gameName;
        gameDecks.setAvailableFaceUpCards(faceUpCards);
        int colorIndex = 0; //assign each player a different color based on their order in the playerNames list
        for(String name : playerNames) {
            if (name.equals(myself.getMyUsername())){
                playerMap.put(myself.getMyUsername(), this.myself);
                myself.setColor(colorIndex);
                colorIndex++;
            } else {
                VisiblePlayer myPlayer = new VisiblePlayer(name, myself.getNumOfCards());
                myPlayer.setColor(colorIndex);
                colorIndex++;
                playerMap.put(name, myPlayer);
            }
        }
        
        gameDecks.setTrainCardDeckSize(gameDecks.getTrainCardDeckSize()
                                               - (playerNames.size() * STARTING_HAND_SIZE)
                                               - FACE_UP_SIZE);
        gameDecks.setDestinationCardDeckSize(gameDecks.getDestinationCardDeckSize()
                                                - (playerNames.size() * DEST_CARD_CHOICES));
        
    }
    
    /**
     * @pre game must be initialized with a nonnull string value for game name
     * @return returns the game name given upon initialization, otherwise null if the game has not been initialized
     * @post doesn't modify any part of the game object
     */
    public String getMyGameName(){
        return this.gameName;
    }
    
    /**
     * @pre game must be initialized with a nonnull player value for myself
     * @return Player object that was given as the player object myself up game initialization with any changes made for duration of game
     * @post no modification made to any part of the game object
     */
    public Player getMyself() {
        return myself;
    }

    public void addToScore(int scoreToAdd){
        myself.addToScore(scoreToAdd);
    }

    public void removeTrains(int numTrainsToRemove){
        myself.removeTrains(numTrainsToRemove);
    }

    public void removeMultipleTrainCards(int numCardsToRemove){
        myself.removeMultipleTrainCards(numCardsToRemove);
    }

    public void removeTrainCardByInt(int myTrainCardInt){
        myself.removeTrainCardByInt(myTrainCardInt);
    }
    public void addDestCard(DestCard newDestCard){
        myself.addDestCard(newDestCard);
        myself.setDestCardNum(myself.getNumDestCard() + 1);
    }

    public int getMyNumTrains(){
        return this.myself.getNumTrains();
    }
    public String getMyUsername(){
        return this.myself.getMyUsername();
    }
    /**
     * @pre game must be initialized with a nonnull faceup cards list
     * @return returns a list of the integers representing train cards in their current order.
     *  The length will be between 0 and 110 and each integer will have a value associated with a card that is in the deck.
     *  The list will not include cards that currently belong to players or are face up.
     * @post no modification made to any part of the game object
     */
    
    public List<Integer> getTrainCards() {
        return trainCards;
    }
    
    /**
     * @pre game must be initialized with a nonnull faceup cards list
     * @return List of integers representing the cards that are currently face up in the game.
     *  The length will be between 2 and 4
     *  @post nothing else in the game will be modified
     */
    public List<Integer> getFaceUpCards() {
        return gameDecks.getFaceUpCards();
    }
    
    public void setFaceUpCards(List<Integer> newFaceUpCards){
        gameDecks.setAvailableFaceUpCards(newFaceUpCards);
    }

    public List<Boolean> getFaceUpDifferences() {
        return gameDecks.getFaceUpDifferences();
    }

    public void setFaceUpDifferences(List<Boolean> faceUpDifferences){
        gameDecks.setFaceUpDifferences(faceUpDifferences);
    }
    
    
    //begin CurrentlySelectedRouteID flags
    private boolean newRouteID = false;
    
    /**
     * records whether or not a route has been selected
     * @pre the getCurrentlySelectedRouteId() and the setCurrentlySelectedRouteId() must be properly implementd by the game fragment
     * @return boolean value to be used as a flag
     * @post returns false if the currently selected route has already been retreived through getCurrentlySelectedRoute or if a route was not selected
     * @post returns true if the user has clicked on a route and this game object has been notified through setCurrentlySelectedRoutId() and the route has not been retreived
     */
    public boolean routeIDHasChanged() {
        return newRouteID;
    }
    /**
     *  @pre the user should have selected a route, if not returns null;
     * @return int value associated with the route id
     * @post sets the newRouteId flag to false
     */
    public int getCurrentlySelectedRouteID() {
        newRouteID = false;
        return currentlySelectedRouteID;
    }
    /**
     *Sets the currently selected route id by the user in the game
     * @param currentlySelectedRouteID route id that was last clicked on by the user. Has a associated route object
     *  @post sets the flag newRouteID to true
     */
    public void setCurrentlySelectedRouteID(int currentlySelectedRouteID) {
        this.currentlySelectedRouteID = currentlySelectedRouteID;
        newRouteID = true;
        notifyObserver();
    }
    
    //begin AllPlayerData flags
    private boolean playerHasChanged = false;
    /**
     *@pre aPlayerHasChanged method must be properly implemented to set the flag when a player changes
     * @return boolean value to be used as a flag. If a player has changed returns true, else returns false
     * @post no values are modified in the game object
     */
    public boolean aPlayerHasChanged() {
        return playerHasChanged;
    }
    /**
     * @param playerHasChanged boolean value to be used as a flag, set to true if a player has changed, set to false if a player has not changed.
     * @post  sets the playerHasChanged flag to the playerHasChanged boolean value given
     */
    public void aPlayerHasChanged(boolean playerHasChanged) {
        this.playerHasChanged = playerHasChanged;
    }
    //end AllPlayerData flags
    
    //begin trainCard flags
    private boolean iHaveDifferentTrainCards = false;
    /**
     *@pre the flag must be properly set upon change to the train card
     *@return boolean value to be used as a flag, returns true if a change has been made to the player train cards, false otherwise
     *@post no values in the game object have changed
     */
    public boolean iHaveDifferentTrainCards() {
        return iHaveDifferentTrainCards;
    }
    /**
     * @param trainCardChange boolean value to be used as a flag, set to true if a train card has changed, set to false if a train card has not changed.
     * @post  sets the iHaveDifferentTrainCards flag to the playerHasChanged boolean value given
     */
    public void iHaveDifferentTrainCards(boolean trainCardChange){
        this.iHaveDifferentTrainCards = trainCardChange;
    }
    
    private boolean iHaveDifferentFaceUpCards = false;
    /**
     *@pre the flag must be properly set upon change to the face up cards
     *@return boolean value to be used as a flag, returns true if a change has been made to the face up train cards, false otherwise
     *@post no values in the game object have changed
     */
    public boolean iHaveDifferentFaceUpCards() {
        return iHaveDifferentFaceUpCards;
    }
    /**
     * @param faceUpCardChange boolean value to be used as a flag, set to true if a train card has changed, set to false if a train card has not changed.
     * @post  sets the iHaveDifferentFaceUpCards flag to the playerHasChanged boolean value given
     */
    public void iHaveDifferentFaceUpCards(boolean faceUpCardChange) {
        iHaveDifferentFaceUpCards = faceUpCardChange;
        notifyObserver();
    }
    //end trainCard flags


    private boolean disconnectFlag = false;
    public void setDisconnectFlag(boolean setFlag){
        disconnectFlag = setFlag;
    }
    public boolean disconnected(){
        return disconnectFlag;
    }
    
    //begin Observable
    /**
     * Overrides the register() method on the Observable interface
     * @post the update method on o will be called each time notifyObserver() is called
     * @param o Observer object that implements the observer interface to be notified by this observable
     */
    @Override
    public void register(Observer o) {
        observers.add(o);
    }
    
    /**
     * Overrides the unregister() method on the Observable interface
     * @param deleteObserver Object that implements the observor interface that is currently registered to
     *                       this object
     * @post deletObserver will no longer be updated or registered to this observable
     * @post no other observers, or the observer will be modified
     */
    @Override
    public void unregister(Observer deleteObserver) {
        int observerIndex = observers.indexOf(deleteObserver);
        if(observerIndex>=0) {
            observers.remove(observerIndex);
        }
    }
    
    /**
     * Overrides the notifyObservor() method on the Observable interfaces
     * @pre must have observer objects that have been registered to this observer
     * @post calls the update method on any objects currently registered as observers
     */
    
    @Override
    public void notifyObserver() {
        Handler uiHandler = new Handler(Looper.getMainLooper()); //gets the UI thread
        Runnable runnable = new Runnable() { //
            @Override
            public void run() {
                for (int i = 0; i < observers.size(); i++){
                    observers.get(i).update();
                }
            }
        };
        uiHandler.post(runnable); //do the run() method in previously declared runnable on UI thread
    }
    //end Observerable
    
    /**
     *@pre the flag must be properly set upon change to the destination cards
     *@return boolean value to be used as a flag, returns true if a change has been made to the player's destination cards, false otherwise
     *@post no values in the game object have changed
     */
    public boolean iHaveDifferentDestDeckSize(){
        return gameDecks.destinationCardDeckSizeHasChanged();
    }
    /**
     * @param iHaveDifferentDestCards boolean value to be used as a flag, set to true if a destination card has changed, set to false if a destination card has not changed.
     * @post  sets the iHaveDifferentDestCards flag to the iHaveDifferentDestCards boolean value given
     */
    public void iHaveDifferentDestDeckSize(boolean iHaveDifferentDestCards){
        gameDecks.destinationCardDeckSizeHasChanged(iHaveDifferentDestCards);
    }
    
    public boolean iHaveDifferentTrainDeckSize() {
        return gameDecks.trainCardDeckSizeHasChanged();
    }
    public void iHaveDifferentTrainDeckSize(boolean iHaveDifferentTrainCards) {
        gameDecks.trainCardDeckSizeHasChanged(iHaveDifferentTrainCards);
    }
    
    private boolean iHaveReturnedDestCards = false;
    public void iHaveReturnedDestCards(boolean iHaveReturnedDestCards){
        this.iHaveReturnedDestCards = iHaveReturnedDestCards;
    }
    public boolean iHaveReturnedDestCards(){
        return this.iHaveReturnedDestCards;
    }
    
    private List<DestCard> possibleDestCards = new ArrayList<>();
    private boolean iHavePossibleDestCards = false;
    /**@pre the destination card associated with the integers in the list must not belong in the deck or to another player
     * @param possibleDestCards must be a nonnull list of Integers with a value that is associated with a DestCard from 0 to 29
     * @post converts the integer to a destination card and stores the destination card, wil overwrite any previously stored destination cards
     */
    public void setPossibleDestCards(List<Integer> possibleDestCards){
        this.possibleDestCards = new ArrayList<>();
        for (int i = 0; i < possibleDestCards.size(); i++){
            this.possibleDestCards.add(DestCard.getDestCardByID(possibleDestCards.get(i)));
        }
    }
    /**
     *@pre the flag must be properly set upon change to the possible destination cards
     *@return boolean value to be used as a flag, returns true if there are possible destination cards availabe, false otherwise
     *@post no values in the game object have changed
     */
    public boolean iHavePossibleDestCards(){
        return iHavePossibleDestCards;
    }
    /**
     * @param iHavePossibleDestCards boolean value to be used as a flag, set to true if there are possible destination cards available, set to false if there are no cards available.
     * @post  sets the iHaveDifferentTrainCards flag to the playerHasChanged boolean value given
     */
    public void iHavePossibleDestCards(boolean iHavePossibleDestCards){
        this.iHavePossibleDestCards = iHavePossibleDestCards;
    }
    /**
     *@pre the list of objects must be set for the game object by calling setPossibleDestCards() with a valid list pf possible destination cards
     * @return List of DestCard objects for the user to choose from
     * @post will return an empty list if possible destination cards weren't set correctly
     */
    public List<DestCard> getPossibleDestCards(){
        return possibleDestCards;
    }
    
    public void setDestCardDeckSize(int newSize) {
        gameDecks.setDestinationCardDeckSize(newSize);
    }
    
    public int getDestCardDeckSize() {
        return gameDecks.getDestinationCardDeckSize();
    }
    
    public void setTrainCardDeckSize(int newSize) {
        gameDecks.setTrainCardDeckSize(newSize);
    }
    
    public int getTrainCardDeckSize() {
        return gameDecks.getTrainCardDeckSize();
    }
    
    /**
     *@pre the list of objects must be set for the game object by calling setPossibleDestCards() with a valid list pf possible destination cards
     * @return List of DestCard objects for the user to choose from
     * @post will return an empty list if possible destination cards weren't set correctly
     */
    public ServerError getServerError() {
        return serverError;
    }
    
    //GAME OVER DATA
    private boolean gameOver=false;
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        notifyObserver();
    }

    private String winnerUsername;
    public void setWinner(String winnerUsername){
        this.winnerUsername = winnerUsername;
    }
    public String getWinnerUsername(){
        return winnerUsername;
    }
    public void reset()
    {
        myGame = new Game();
    }
    private List<Integer> cardsToDiscard;
    
    public List<Integer> getCardsToDiscard() {
        return cardsToDiscard;
    }
    
    public void setCardsToDiscard(List<Integer> cardsToDiscard) {
        this.cardsToDiscard = cardsToDiscard;
    }

    private List<Integer> claimedRoutes = new ArrayList<>();

    public List<Integer> getClaimedRoutes() {
        return claimedRoutes;
    }

    public void setClaimedRoutes(List<Integer> claimedRoutes) {
        this.claimedRoutes = claimedRoutes;
    }
}