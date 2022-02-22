package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The Player class contains all information pertinent to a player in the game; the number of each
 * TrainCard type, all destination cards, the player's color, their points, their continuous routes,
 * etc. and reevaluates the value of these data when a player's decision necessitates it.
 * INVARIANTS: Each numOf[Color]Cards > 0; Number of cars > 0;  0>= newlyDrawnDestCars.size() >= 3;
 *              destCardHand.size() > 2; points >= 0; numOfRoutes>= 0; playerColor != null;
 *
 */

class Player implements java.io.Serializable{

    private String userName;
    private List<TrainCard> trainCardHand = new ArrayList<>();

    private int numOfPurpleCards = 0;
    private int numOfWhiteCards = 0;
    private int numOfBlueCards = 0;
    private int numOfYellowCards = 0;
    private int numOfOrangeCards = 0;
    private int numOfBlackCards = 0;
    private int numOfRedCards = 0;
    private int numOfGreenCards = 0;
    private int numOfWildCards = 0;

    private PlayerColor playerColor;
    private ArrayList<DestCard> destCardHand = new ArrayList<>();
    //private List<DestCard> newlyDrawnDestCards = new ArrayList<>();
    private int points;
    private int numOfRoutes;
    private int numOfCars = 45;
    private List<ContinuousRoute> allContRoutes = new ArrayList<>();
    private List<Integer> claimedRoutes = new ArrayList<>();

    // for handling client state on rejoin result
    private List<Integer> possibleDestCards = new ArrayList<>();
    private boolean drewATrainCard = false;

    public List<Integer> getPossibleDestCards(){
        return possibleDestCards;
    }
    public void addPossibleDestCards(List<DestCard> possibleDestCards){
        for (DestCard card : possibleDestCards){
            this.possibleDestCards.add(DestCard.getDestCardKey(card));
        }
    }

    public void clearPossibleDestCards(){
        possibleDestCards.clear();
    }

    public void setDrewATrainCard(boolean drewCard){
        this.drewATrainCard = drewCard;
    }


    Player(String userName) {
        this.userName = userName;
    }


    public void addClaimedRoute(int routeID){
        claimedRoutes.add(routeID);
    }
    public List<Integer> getClaimedRoutes(){
        return claimedRoutes;
    }

    /**
     * Adds to exact number of train cards in each player's hand.
     * PRE: The drawnCards are actually cards the player has drawn;
     *      These cards have been removed from the Board.
     * POST:Each drawn card will be added to the list of cards in the player's trainCardHand;
     *
     * @param drawnCards List of drawn drawn TrainCards.
     */
    void addTrainCards(List<TrainCard> drawnCards){

         for (TrainCard currentCard : drawnCards){
             trainCardHand.add(currentCard);
             if (currentCard == TrainCard.PURPLE){
                 numOfPurpleCards++;
             }
             else if (currentCard == TrainCard.WHITE) {
                 numOfWhiteCards++;
             }
             else if (currentCard == TrainCard.BLUE){
                 numOfBlueCards++;
             }
             else if (currentCard == TrainCard.YELLOW){
                 numOfYellowCards++;
             }
             else if (currentCard == TrainCard.ORANGE){
                 numOfOrangeCards++;
             }
             else if (currentCard == TrainCard.BLACK){
                 numOfBlackCards++;
             }
             else if (currentCard == TrainCard.RED){
                 numOfRedCards++;
             }
             else if (currentCard == TrainCard.GREEN){
                 numOfGreenCards++;
             }
             else {
                 numOfWildCards++;
             }
         }
     }

    /**
     * Places drawn cards in newlyDrawnDestCards member. Does not add them to their destCardHand.
     * PRE: DrawnCards are cards the player has actually drawn.
     * POST: All cards will be added to the newlyDrawnDestCards. The newly drawn destination cards list
     *         will only contain the cards the player has drawn, in whatever order.
     * @param drawnCards Cards drawn from board's DestCardDeck
     */
     void addDestCards(List<DestCard> drawnCards) {
        for (DestCard currentCard : drawnCards) {
            destCardHand.add(currentCard);
        }
     }

    /**
     * Removes the cards selected from player's newlyDrawnDestCards; adds remaining newlyDrawnDestCards
     * to player's destCard hand; clears newlyDrawnDestCards.
     * PRE: Destination cards are cards the player has already drawn, and are located in newlyDrawnDest
     *      Cards.
     * POST: All cards that are in the newlyDrawnDestCard list, and have also not been passed to the
     *        method, are added to the destCardHand, in whatever order.
     *        NewlyDrawnDestCards list is cleared. Returns true if this process completes.
     * @param cardOne First card returned by player
     * @return True if cards removed
     * @throws GamePlayException Thrown if player has not drawn cards they are trying to remove.
     */
     boolean removeDestCards(DestCard cardOne) throws GamePlayException {
        boolean success = false;

        if (cardOne != null) {
            for (int a = 0; a < destCardHand.size(); a++) {
                if (cardOne.equals(destCardHand.get(a))) {
                    success = true;
                    destCardHand.remove(cardOne);
                }
            }
            if (!success) {
                throw new GamePlayException("Invalid card returned");
            }
        }

        return true;
    }

    /**
     * Checks if cards returned have been drawn.
     * PRE: Passes any destination card to the method.
     * POST: No state changed; returns true if the card is contained in newlyDrawnDestCards,
     *          false otherwise.
     * @param destCard DestCard being returned.
     * @return True if valid; false otherwise
     */
     boolean invalidDestCard(DestCard destCard) {
        if (destCard == null) {
            return false;
        }
        for (DestCard drawnDestCard : destCardHand) {
            if (destCard.equals(drawnDestCard)){
                return false;
            }
        }
        return true;
    }

    /***
     * Iterates the train card hand to to get a list of keys for the TrainCards in the hand.
     * @return A list of integers representing the train cards in Player's hand.
     */
    List<Integer> getTrainCardCodes() {
        List<Integer> trainCardCodes = new ArrayList<>();
        for (TrainCard trainCard : trainCardHand) {
            trainCardCodes.add(TrainCard.getTrainCardKey(trainCard));
        }
        return trainCardCodes;
    }


    /**
     * Subtracts cars from player's number of cards.
     * PRE: Passes an int the represents a valid route size
     * POST: Decrements the value numOfCards by the size of
     * @param numOfCars Number of cars to be placed on the board.
     * @return true if valid parameter; false otherwise.
     */
    boolean removeCars(int numOfCars) {
        if (numOfCars < 1 || numOfCars > 6){
            return false;
        }
        this.numOfCars -= numOfCars;
        return true;
    }

    /**
     * Adds to the score.
     * PRE: Valid point value;
     * POST: Player's point value is incremented by the parameter value.
     * @param points The number of points that will be added.
     */
    void addScore(int points) {
        this.points += points;
    }

    /**
     * Adds to the number of routes the player owns.
     * PRE: None
     * POST: Increments numOfRoutes by one.
     */
    void addNumOfRoutes() {
        numOfRoutes++;
    }

    /**
     * For every TrainCard passed to the method, will:
     *  Evaluate the color of said TrainCard;
     *  Decrement the number of cards with this color, and
     *  Calls a private function to remove a card with this color from the player's hand.
     *  PRE: A list of discarded cards train cards that the player already has in their hand.
     *  POST: For each card evalauted in the list passed:
     *          Evaluates type of evaluated train card; decrements the amount of this type by one.
     *          Removes a card that matches the type of the evaluated card in the trainCardHand list.
     * @param discardedCards A list of traincards that will be removed frm the player's hand.
     */
    void removeTrainCards(List<TrainCard> discardedCards) {
        for (int a = 0; a  < discardedCards.size(); a++) {
            TrainCard trainCard = discardedCards.get(a);
            switch (trainCard){
                case RED:
                    numOfRedCards--;
                    break;
                case GREEN:
                    numOfGreenCards--;
                    break;
                case BLUE:
                    numOfBlueCards--;
                    break;
                case YELLOW:
                    numOfYellowCards--;
                    break;
                case BLACK:
                    numOfBlackCards--;
                    break;
                case PURPLE:
                    numOfPurpleCards--;
                    break;
                case ORANGE:
                    numOfOrangeCards--;
                    break;
                case WHITE:
                    numOfWhiteCards--;
                    break;
                case WILD:
                    numOfWildCards--;
                    break;
            }
            removeCardFromHand(trainCard);
        }
    }

    boolean ownsCards(List<TrainCard> chosenCards) {
         int numOfPurpleCardsSent = 0;
         int numOfWhiteCardsSent = 0;
         int numOfBlueCardsSent = 0;
         int numOfYellowCardsSent = 0;
         int numOfOrangeCardsSent = 0;
         int numOfBlackCardsSent = 0;
         int numOfRedCardsSent = 0;
         int numOfGreenCardsSent = 0;
         int numOfWildCardsSent = 0;

        for (int a = 0; a < chosenCards.size(); a++){
            TrainCard currentCard = chosenCards.get(a);
            if (currentCard == TrainCard.PURPLE){
                numOfPurpleCardsSent++;
            }
            else if (currentCard == TrainCard.WHITE) {
                numOfWhiteCardsSent++;
            }
            else if (currentCard == TrainCard.BLUE){
                numOfBlueCardsSent++;
            }
            else if (currentCard == TrainCard.YELLOW){
                numOfYellowCardsSent++;
            }
            else if (currentCard == TrainCard.ORANGE){
                numOfOrangeCardsSent++;
            }
            else if (currentCard == TrainCard.BLACK){
                numOfBlackCardsSent++;
            }
            else if (currentCard == TrainCard.RED){
                numOfRedCardsSent++;
            }
            else if (currentCard == TrainCard.GREEN){
                numOfGreenCardsSent++;
            }
            else {
                numOfWildCardsSent++;
            }
        }
        return false;
    }
    /**
     * PRE: There exists a TrainCard in the player's trainCardHand list that matches the type
     *      of the TrainCard passed.
     * POST: One card in the player's hand, that matches the type of the TrainCard passed, is
     *          removed from the hand.
     * Removes a card with the same color as the passed TrainCard from the player's hand.
     * @param trainCard The card to be removed from the player's hand.
     */
    private void removeCardFromHand(TrainCard trainCard) {
        for(int a = 0; a < trainCardHand.size(); a++){
            if (trainCard == trainCardHand.get(a)) {
                trainCardHand.remove(a);
                break;
            }
        }
    }

    boolean finalTurn() {
        return (numOfCars < 3);
    }

    int addDestCardPoints() {
        int pointsToAdd = 0;
        for (int a = 0 ; a < destCardHand.size(); a++){
            DestCard destCard = destCardHand.get(a);
            if(contRouteContains(destCard)){
                pointsToAdd += destCard.getPointValue();
            }
        }
        points += pointsToAdd;
        return pointsToAdd;
    }

    int subtractDestCardPoints() {
        int pointsToSubtract = 0;
        for (int a = 0 ; a < destCardHand.size(); a++){
            DestCard destCard = destCardHand.get(a);
            if(!contRouteContains(destCard)){
                pointsToSubtract += destCard.getPointValue();
            }
        }
        points -= pointsToSubtract;
        return pointsToSubtract;
    }

    private boolean contRouteContains (DestCard destCard) {
        City startCity = destCard.getStartCity();
        City endCity = destCard.getEndCity();

        for (int a = 0; a < allContRoutes.size(); a++) {
            Set<City> contRoute = allContRoutes.get(a).cities;
            if(contRoute.contains(startCity) && contRoute.contains(endCity)) {
                return true;
            }
        }
        return false;
    }

    void calculateContRoute(City startCity, City endCity, int size) {

        int startCityIndex = -1;
        int endCityIndex = -1;

        for (int a = 0; a < allContRoutes.size(); a++) {
            ContinuousRoute currentRoute = allContRoutes.get(a);
            if (currentRoute.contains(startCity)){
                currentRoute.addToRoute(endCity, size);
                startCityIndex = a;
            } else if (currentRoute.contains(endCity)) {
                currentRoute.addToRoute(startCity, size);
                endCityIndex = a;
            }
        }

        if (startCityIndex > -1 && endCityIndex > -1) {
            ContinuousRoute startCityRoute = allContRoutes.get(startCityIndex);
            ContinuousRoute endCityRoute = allContRoutes.get(endCityIndex);
            startCityRoute.uniteRoutes(endCityRoute, size);
            allContRoutes.remove(endCityIndex);
        } else if (startCityIndex < 0 && endCityIndex < 0){
            allContRoutes.add(new ContinuousRoute(startCity, endCity, size));
        }
    }

    int getLargestContRouteSize() {
        int largestSize = 0;

        for (int a = 0; a < allContRoutes.size(); a++) {
            if (allContRoutes.get(a).size > largestSize) {
                largestSize = allContRoutes.get(a).size;
            }
        }
        return largestSize;
    }
     int getNumOfPurpleCards() {
         return numOfPurpleCards;
     }

     int getNumOfWhiteCards() {
         return numOfWhiteCards;
     }

     int getNumOfBlueCards() {
         return numOfBlueCards;
     }

     int getNumOfYellowCards() {
         return numOfYellowCards;
     }

     int getNumOfOrangeCards() {
         return numOfOrangeCards;
     }

     int getNumOfBlackCards() {
         return numOfBlackCards;
     }

     int getNumOfRedCards() {
         return numOfRedCards;
     }

     int getNumOfGreenCards() {
         return numOfGreenCards;
     }

     int getNumOfWildCards() {
         return numOfWildCards;
     }

    public String getUsername() {
        return userName;
    }

    int getNumOfCars() {
        return numOfCars;
    }

    int getSizeOfTrainCardHand() {
        return trainCardHand.size();
    }

    int getSizeOfDestCardHand() {
        return destCardHand.size();
    }

    int getPoints() {
        return points;
    }

    int getNumOfRoutesOwned() {
        return numOfRoutes;
    }

    public List<DestCard> getDestCards() {
        return destCardHand;
    }

    PlayerColor getPlayerColor() {
        return playerColor;
    }

    void setPlayerColor(int position) {
        playerColor = PlayerColor.getPlayerColor(position);
    }

    void customNumOfCars(int numOfCars) {
        this.numOfCars = numOfCars;
    }



    public List<ContinuousRoute> getAllContRoutes() {
        return allContRoutes;
    }
    class ContinuousRoute implements Serializable {
         Set<City> cities = Collections.synchronizedSet(new HashSet<City>());
         int size = 0;

         ContinuousRoute (City startCity, City endCity, int size) {
             cities.add(startCity);
             cities.add(endCity);
             this.size = size;
         }

         boolean contains(City city) {
            return cities.contains(city);
         }

         void addToRoute(City cityToAdd, int sizeToAdd) {
             cities.add(cityToAdd);
             size += sizeToAdd;
         }

         void uniteRoutes(ContinuousRoute endCityRoute, int claimedSize) {
             this.cities.addAll(endCityRoute.cities);
             this.size += endCityRoute.size - claimedSize;
         }
     }
}
