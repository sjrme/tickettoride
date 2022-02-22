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
public class GameData implements Serializable {

    private String gameName;
    private int numPlayers;
    private List<PlayerData> playerData;
    private PrivatePlayerData privatePlayerData;
    private String activePlayer;
    private int trainDeckSize;
    private int destDeckSize;
    private List<Integer> faceUpCards;
    private int discardPileSize;
    private List<ChatHistoryData> gameHistory;
    private List<ChatHistoryData> chatHistory;

    public GameData() {}

    public GameData(String gameName, int numPlayers, List<PlayerData> playerData, PrivatePlayerData privatePlayerData, String activePlayer, int trainDeckSize, int destDeckSize, List<Integer> faceUpCards, int discardPileSize, List<ChatHistoryData> gameHistory, List<ChatHistoryData> chatHistory) {
        this.gameName = gameName;
        this.numPlayers = numPlayers;
        this.playerData = playerData;
        this.privatePlayerData = privatePlayerData;
        this.activePlayer = activePlayer;
        this.trainDeckSize = trainDeckSize;
        this.destDeckSize = destDeckSize;
        this.faceUpCards = faceUpCards;
        this.discardPileSize = discardPileSize;
        this.gameHistory = gameHistory;
        this.chatHistory = chatHistory;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public List<PlayerData> getPlayerData() {
        return playerData;
    }

    public void setPlayerData(List<PlayerData> playerData) {
        this.playerData = playerData;
    }

    public PrivatePlayerData getPrivatePlayerData() {
        return privatePlayerData;
    }

    public void setPrivatePlayerData(PrivatePlayerData privatePlayerData) {
        this.privatePlayerData = privatePlayerData;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public int getTrainDeckSize() {
        return trainDeckSize;
    }

    public void setTrainDeckSize(int trainDeckSize) {
        this.trainDeckSize = trainDeckSize;
    }

    public int getDestDeckSize() {
        return destDeckSize;
    }

    public void setDestDeckSize(int destDeckSize) {
        this.destDeckSize = destDeckSize;
    }

    public List<Integer> getFaceUpCards() {
        return faceUpCards;
    }

    public void setFaceUpCards(List<Integer> faceUpCards) {
        this.faceUpCards = faceUpCards;
    }

    public int getDiscardPileSize() {
        return discardPileSize;
    }

    public void setDiscardPileSize(int discardPileSize) {
        this.discardPileSize = discardPileSize;
    }

    public List<ChatHistoryData> getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(List<ChatHistoryData> gameHistory) {
        this.gameHistory = gameHistory;
    }

    public List<ChatHistoryData> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<ChatHistoryData> chatHistory) {
        this.chatHistory = chatHistory;
    }
}
