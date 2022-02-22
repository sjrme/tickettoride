package model;

public class RunningGame {
    private String gameName;
    private int gameSize;

    public RunningGame(String gameName, int gameSize){
        this.gameName = gameName;
        this.gameSize = gameSize;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameSize() {
        return gameSize;
    }
}
