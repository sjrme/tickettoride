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
public class PlayerData implements Serializable {

    private String playerName;
    private int numTrainCards;
    private int numDestCards;
    private int numTrains;
    private int score;
    private List<Integer> routes;

    public PlayerData(String playerName, int numTrainCards, int numDestCards, int numTrains, int score, List<Integer> routes) {
        this.playerName = playerName;
        this.numTrainCards = numTrainCards;
        this.numDestCards = numDestCards;
        this.numTrains = numTrains;
        this.score = score;
        this.routes = routes;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getNumTrainCards() {
        return numTrainCards;
    }

    public void setNumTrainCards(int numTrainCards) {
        this.numTrainCards = numTrainCards;
    }

    public int getNumDestCards() {
        return numDestCards;
    }

    public void setNumDestCards(int numDestCards) {
        this.numDestCards = numDestCards;
    }

    public int getNumTrains() {
        return numTrains;
    }

    public void setNumTrains(int numTrains) {
        this.numTrains = numTrains;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Integer> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Integer> routes) {
        this.routes = routes;
    }
}
