package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UnstartedGame implements Serializable{

    private String gameName;
    private List<String> usernames = new ArrayList<>();
    private int playersIn;
    private int playersNeeded;

    public UnstartedGame(){}
    public UnstartedGame(String gameName, int playersNeeded){
        this.gameName = gameName;
        this.playersNeeded = playersNeeded;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    private boolean started=false;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<String> getUsernamesInGame() {
        return usernames;
    }

    public void addPlayer(String username){
        this.usernames.add(username);
        this.playersIn++;
    }

    public boolean hasPlayer(String username){
        return this.usernames.contains(username);
    }

    public void removePlayer(String username){
        if (this.usernames.contains(username)) {
            this.usernames.remove(username);
            this.playersIn--;
        }
    }

    public boolean hasEnoughPlayersToStart(){
        return this.playersIn == this.playersNeeded;
    }

    public boolean hasNoPlayers(){
        return this.playersIn == 0;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public int getPlayersIn() {
        return playersIn;
    }

    public void setPlayersIn(int playersIn) {
        this.playersIn = playersIn;
    }

    public int getPlayersNeeded() {
        return playersNeeded;
    }

    public void setPlayersNeeded(int playersNeeded) {
        this.playersNeeded = playersNeeded;
    }


}
