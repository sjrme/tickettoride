package model;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import interfaces.Observable;
import interfaces.Observer;

public class ClientModel implements Observable{
    //singleton pattern:
    private static ClientModel myClientModel = new ClientModel();
    private ClientModel(){}
    public static ClientModel getMyClientModel() {
        return myClientModel;
    }

    //begin message toasting
    private String messageToToast;
    private boolean hasMessage = false;

    public void setMessageToToast(String message) { //prepare a message to toast
        this.messageToToast = message;
        this.hasMessage = true;
        notifyObserver();
    }

    public String getMessageToToast() { //get toast message
        return messageToToast;
    }
    public boolean hasMessage() { //use to check if there is anything to toast.
        return hasMessage;
    }
    public void receivedMessage(){ //call this after toasting the message, confirming it has been received.
        hasMessage = false;
    }
    //end message toasting


    //begin menu login
    private String ip;
    private String port = "8080";
    private String username;
    private boolean hasUser;

    public void setMyUser(String username, boolean hasUser) {
        this.username = username;
        this.hasUser = hasUser;
        notifyObserver();
    }

    public void removeMyUser(){
        this.username = null;
        this.hasUser = false;
    }

    public String getMyUsername() {
        return username;
    }
    public boolean hasUser() {
        return hasUser;
    }
    //end menu login


    public boolean isLeftGame() {
        return leftGame;
    }

    public void setLeftGame(boolean leftGame) {
        this.leftGame = leftGame;
    }

    //begin User

    private boolean leftGame;
    private boolean hasGame = false;
    private boolean hasJoinedGame = false;
    public boolean hasJoinedGame(){
        return hasJoinedGame;
    }
    public void receivedHasJoinedGame(){
        this.hasJoinedGame = false;
    }

    private boolean hasRejoinedGame = false;
    public boolean hasRejoinedGame() {
        return hasRejoinedGame;
    }
    public void hasRejoinedGame(boolean rejoined) {
        hasRejoinedGame = rejoined;
    }
    public void receivedHasRejoinedGame() {
        this.hasRejoinedGame = false;
    }

    //end Observer

    private List<UnstartedGame> unstartedGameList = new ArrayList<>();
    private List<RunningGame> runningGameList = new ArrayList<>();
    private boolean gameFull;
    private boolean startedGame=false;

    public boolean gameIsFull() {
        gameFull = false;
        for(UnstartedGame i: unstartedGameList) {
            if(i.getGameName().equals(gameName)) {
                if(i.getPlayersNeeded() == i.getPlayersIn()) {
                    gameFull=true;
                }
            }
        }
        return gameFull;
    }


    public String getMyGameName() {
        return gameName;
    }

    public void setMyGame(String gameName){
        this.gameName = gameName;
        this.hasGame = true;
    }

    public void removeMyGame(){
        this.gameName = null;
        this.hasGame = false;
    }

    public boolean isStartedGame() {
        startedGame=true;
        for(UnstartedGame i: unstartedGameList) {
            if(i.getGameName().equals(gameName)) {
                startedGame=false;
            }
        }
        return startedGame;
    }


    private boolean gameIsStarted = false;

    public void startGame(){
        this.gameIsStarted = true;
        notifyObserver();
    }
    public boolean gameIsStarted() {
        return gameIsStarted;
    }


    private String gameName;
    private boolean createdGame = false;

    public void setCreatedGame(String gameName) {
        this.gameName = gameName;
        this.createdGame = true;
    }

    public boolean hasCreatedGame() {
        return createdGame;
    }
    public void receivedCreatedGame(){
        this.createdGame = false;
    }

    public void setHasGame(String gameName) {
        setMyGame(gameName);
        this.hasJoinedGame = true;
        notifyObserver();
    }

    //begin Observer
    private ArrayList<Observer> observers = new ArrayList<>();


    public List<UnstartedGame> getUnstartedGameList() {
        return this.unstartedGameList;
    }
    public List<RunningGame> getRunningGameList(){
        return this.runningGameList;
    }

    private boolean hasNewGameLists = false;
    public boolean hasNewGameLists(){
        return hasNewGameLists;
    }
    public void receivedNewGameLists(){
        this.hasNewGameLists = false;
    }

    public void setGameLists(List<UnstartedGame> unstartedGameList, List<RunningGame> runningGameList){
        if (unstartedGameList != null) {
            this.unstartedGameList = unstartedGameList;
        } else {
            this.unstartedGameList = new ArrayList<>();
        }
        if (runningGameList != null) {
            this.runningGameList = runningGameList;
        } else {
            this.runningGameList = new ArrayList<>();
        }
        this.hasNewGameLists = true;
        notifyObserver();
    }


    public List<String> getPlayersinGame()
    {
        List<String>toreturn=null;
        int size=0;
        for(UnstartedGame i: unstartedGameList) {
            if(i.getGameName().equals(gameName)) {
                size=i.getPlayersNeeded();
                toreturn=i.getUsernamesInGame();
            }
        }
        if(toreturn==null)
        {
            toreturn=new ArrayList();
        }
        for(int l=toreturn.size();l<size;l++)
        {
            toreturn.add("Waiting for player "+l);
        }
        return toreturn;
    }

    private boolean disconnectFlag = false;
    public void setDisconnectFlag(boolean setFlag){
        disconnectFlag = setFlag;
    }
    public boolean disconnected(){
        return disconnectFlag;
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer deleteObserver) {
        int observerIndex = observers.indexOf(deleteObserver);
        if(observerIndex>=0) {
            observers.remove(observerIndex);
        }
    }

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



    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean hasGame() {return hasGame;}


    //in game info

    public List<DestCard> getMyDestinationCards() {
        return myDestinationCards;
    }

    public void setMyDestinationCards(List<DestCard> myDestinationCards) {
        this.myDestinationCards = myDestinationCards;
    }

    public List<DestCard> myDestinationCards;
    public void reset()
    {
        myClientModel = new ClientModel();
    }
}
