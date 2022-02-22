package model;

import java.io.Serializable;
import java.util.List;

/**
 * TODO: description
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-11
 */
public class PrivatePlayerData implements Serializable{

    private List<Integer> destCards;
    private List<Integer> trainCards;
    private List<Integer> possibleDestCards;
    private boolean drewATrainCard;

    public PrivatePlayerData(List<Integer> destCards, List<Integer> trainCards, List<Integer> possibleDestCards, boolean drewATrainCard) {
        this.destCards = destCards;
        this.trainCards = trainCards;
        this.possibleDestCards = possibleDestCards;
        this.drewATrainCard = drewATrainCard;
    }

    public List<Integer> getDestCards() {
        return destCards;
    }

    public void setDestCards(List<Integer> destCards) {
        this.destCards = destCards;
    }

    public List<Integer> getTrainCards() {
        return trainCards;
    }

    public void setTrainCards(List<Integer> trainCards) {
        this.trainCards = trainCards;
    }

    public List<Integer> getPossibleDestCards() {
        return possibleDestCards;
    }

    public void setPossibleDestCards(List<Integer> possibleDestCards) {
        this.possibleDestCards = possibleDestCards;
    }

    public boolean isDrewATrainCard() {
        return drewATrainCard;
    }

    public void setDrewATrainCard(boolean drewATrainCard) {
        this.drewATrainCard = drewATrainCard;
    }
}
