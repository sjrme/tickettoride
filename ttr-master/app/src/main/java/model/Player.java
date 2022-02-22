package model;

import java.util.ArrayList;
import java.util.List;


public class Player extends AbstractPlayer {

    public Player(String userName, List<Integer> trainCardInts){
        super.username = userName;
        initializeMyCards(trainCardInts);
    }

    private void initializeMyCards(List<Integer> trainCardInts){
        for (int myTrainCardID : trainCardInts){
            addTrainCardByInt(myTrainCardID);
        }
    }

    protected int getNumOfCards(){
        return this.numOfPurpleCards + this.numOfWhiteCards + this.numOfBlueCards + this.numOfYellowCards +
                this.numOfOrangeCards + this.numOfBlackCards + this.numOfRedCards + this.numOfGreenCards +
                this.numOfWildCards;
    }

    public void addTrainCardByInt(int myTrainCardInt){
        super.addTrainCard();
        TrainCard myTrainCard = TrainCard.getTrainCard(myTrainCardInt);
        switch (myTrainCard){
            case PURPLE:
                this.numOfPurpleCards++;
                break;
            case WHITE:
                this.numOfWhiteCards++;
                break;
            case BLUE:
                this.numOfBlueCards++;
                break;
            case YELLOW:
                this.numOfYellowCards++;
                break;
            case ORANGE:
                this.numOfOrangeCards++;
                break;
            case BLACK:
                this.numOfBlackCards++;
                break;
            case RED:
                this.numOfRedCards++;
                break;
            case GREEN:
                this.numOfGreenCards++;
                break;
            case WILD:
                this.numOfWildCards++;
                break;
        }
    }
    public void removeTrainCardByInt(int myTrainCardInt){
        super.removeTrainCard();
        TrainCard myTrainCard = TrainCard.getTrainCard(myTrainCardInt);
        switch (myTrainCard){
            case PURPLE:
                this.numOfPurpleCards--;
                break;
            case WHITE:
                this.numOfWhiteCards--;
                break;
            case BLUE:
                this.numOfBlueCards--;
                break;
            case YELLOW:
                this.numOfYellowCards--;
                break;
            case ORANGE:
                this.numOfOrangeCards--;
                break;
            case BLACK:
                this.numOfBlackCards--;
                break;
            case RED:
                this.numOfRedCards--;
                break;
            case GREEN:
                this.numOfGreenCards--;
                break;
            case WILD:
                this.numOfWildCards--;
                break;
        }
    }

    private int numOfPurpleCards = 0;
    private int numOfWhiteCards = 0;
    private int numOfBlueCards = 0;
    private int numOfYellowCards = 0;
    private int numOfOrangeCards = 0;
    private int numOfBlackCards = 0;
    private int numOfRedCards = 0;
    private int numOfGreenCards = 0;
    private int numOfWildCards = 0;

    public int getNumOfTypeCards(TrainCard myTrainCardType){
        switch (myTrainCardType){
            case PURPLE:
                return this.numOfPurpleCards;
            case WHITE:
                return this.numOfWhiteCards;
            case BLUE:
                return this.numOfBlueCards;
            case YELLOW:
                return this.numOfYellowCards;
            case ORANGE:
                return this.numOfOrangeCards;
            case BLACK:
                return this.numOfBlackCards;
            case RED:
                return this.numOfRedCards;
            case GREEN:
                return this.numOfGreenCards;
            case WILD:
                return this.numOfWildCards;
            default:
                return -1;
        }
    }

    private List<DestCard> myDestCards = new ArrayList<>();

    public int[] getMyTrainCardsAsIntArray(){
        int myTrainCards[] = new int[9];
        myTrainCards[0] = this.numOfRedCards;
        myTrainCards[1] = this.numOfBlueCards;
        myTrainCards[2] = this.numOfGreenCards;
        myTrainCards[3] = this.numOfYellowCards;
        myTrainCards[4] = this.numOfBlackCards;
        myTrainCards[5] = this.numOfPurpleCards;
        myTrainCards[6] = this.numOfOrangeCards;
        myTrainCards[7] = this.numOfWhiteCards;
        myTrainCards[8] = this.numOfWildCards;
        return myTrainCards;
    }

    public List<DestCard> getMyDestCards() {
        return myDestCards;
    }

    public void addDestCard(DestCard destCard){
        this.myDestCards.add(destCard);
    }

}
