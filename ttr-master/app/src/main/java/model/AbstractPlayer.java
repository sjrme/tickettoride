package model;

import java.util.ArrayList;
import java.util.List;

import fysh340.ticket_to_ride.R;


public abstract class AbstractPlayer {
    private final int STARTING_NUMBER_OF_TRAINS = 45;
    protected String username;
    private int numTrains = STARTING_NUMBER_OF_TRAINS;
    protected int numCards = 0;
    private int numRoutes = 0;
    private int destCardNum = 0;
    private int score = 0;
    private boolean myTurn=false;

    public boolean isMyTurn() {
        return myTurn;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    public void setDestCardNum(int destCardNum) {
        this.destCardNum = destCardNum;
    }

    public void setNumCards(int numCards) {
        this.numCards = numCards;

    }

    public void setNumRoutes(int numRoutes) {
        this.numRoutes = numRoutes;
    }

    public int getNumDestCard() {
        return destCardNum;
    }

    public void incrementDestCards(){
        this.destCardNum++;
    }

    private int color;

    public void setNumTrains(int numTrains) {
        this.numTrains = numTrains;
    }

    public void setColor(int myColor) {
        switch(myColor) {
            case(0):
                color = R.color.neon_yellow;
                break;
            case(1):
                color = R.color.neon_pink;
                break;
            case(2):
                color = R.color.neon_green;
                break;
            case(3):
                color = R.color.neon_orange;
                break;
            case(4):
                color = R.color.neon_grey;
                break;
        }
    }

    public String getMyUsername(){
        return this.username;
    }

    public int getNumTrains() {
        return numTrains;
    }

    public int getNumCards(){
        return this.numCards;
    }

    public int getNumRoutes(){
        return this.numRoutes;
    }

    public int getScore() {
        return score;
    }

    //begin train updates
    public void removeTrains(int numTrainsToRemove){
        this.numTrains -= numTrainsToRemove;
    }
    //end train updates

    //begin train card updates
    protected void addTrainCard(){
        this.numCards++;
    }
    protected void removeTrainCard(){
        this.numCards--;
    }
    public void removeMultipleTrainCards(int numCardsToRemove){
        this.numCards -= numCardsToRemove;
    }
    //end train card updates

    //begin route updates
    public void incrementNumRoutesClaimed(){
        this.numRoutes++;
    }
    //end route updates

    //begin score updates
    public void addToScore(int scoreToAdd){
        this.score += scoreToAdd;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //end score updates
    public int getColor() {
       return color;
    }
    private boolean iHaveDifferentDestCards = false;
    public boolean iHaveDifferentDestCards() {
        return iHaveDifferentDestCards;
    }
    public void iHaveDifferentDestCards(boolean hasChanged) {
        iHaveDifferentDestCards = hasChanged;
    }
    //END OF GAME
    private int claimedRoutePoints;
    private int destinationPoints;
    private int destinationPointsLost;

    public int getClaimedRoutePoints() {
        return claimedRoutePoints;
    }

    public void setClaimedRoutePoints(int claimedRoutePoints) {
        this.claimedRoutePoints = claimedRoutePoints;
    }

    public int getDestinationPoints() {
        return destinationPoints;
    }

    public void setDestinationPoints(int destinationPoints) {
        this.destinationPoints = destinationPoints;
    }

    public int getDestinationPointsLost() {
        return destinationPointsLost;
    }

    public void setDestinationPointsLost(int destinationPointsLost) {
        this.destinationPointsLost = destinationPointsLost;
    }

    public int getLongestRoutePoints() {
        return longestRoutePoints;
    }

    public void setLongestRoutePoints(int longestRoutePoints) {
        this.longestRoutePoints = longestRoutePoints;
    }

    private int longestRoutePoints;

    private List<Integer> claimedRoutes = new ArrayList<Integer>();

    public List<Integer> getClaimedRoutes() {
        return claimedRoutes;
    }
}
