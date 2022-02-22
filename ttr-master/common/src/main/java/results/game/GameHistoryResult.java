package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class GameHistoryResult extends Result  implements Serializable {
    protected String message;
    protected int numTrainCars;
    protected int numTrainCardsHeld;
    protected int numDestCardsHeld;
    protected int numRoutesOwned;
    protected int score;
    protected int claimedRouteNumber;
    protected int trainCardDeckSize;
    protected int destCardDeckSize;
    protected int faceUpIndex;
    protected GameHistoryResult(){}
    public GameHistoryResult(String username, String message, int numTrainCars, int numTrainCardsHeld,
                             int numDestCardsHeld, int numRoutesOwned, int score,
                             int claimedRouteNumber, int trainCardDeckSize, int destCardDeckSize,
                             int faceUpIndex){
        super.type = Utils.GAME_HISTORY_TYPE;
        super.username = username;
        this.message = message;
        this.numTrainCars = numTrainCars;
        this.numTrainCardsHeld = numTrainCardsHeld;
        this.numDestCardsHeld = numDestCardsHeld;
        this.numRoutesOwned = numRoutesOwned;
        this.score = score;
        this.claimedRouteNumber = claimedRouteNumber;
        this.trainCardDeckSize = trainCardDeckSize;
        this.destCardDeckSize = destCardDeckSize;
        this.faceUpIndex = faceUpIndex;
    }
}
