package serverfacade;


import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import interfaces.IServer;
import commands.Command;
import model.ServerModel;
import database.IDatabase;
import utils.Utils;

public class ServerFacade implements IServer,Serializable {

    private static Logger logger = Logger.getLogger(Utils.SERVER_LOG);
    private static ServerModel mSingletonModel = ServerModel.getInstance();
    private static transient IDatabase myDatabase;

    public static void setAndLoadDatabase(IDatabase myDatabase){
        ServerFacade.myDatabase = myDatabase;

        //call the methods instead of setting variables here to reduce coupling
        mSingletonModel.initializeServerModel(myDatabase.loadUsersFromDatabase(),
                myDatabase.loadStartedGamesFromDatabase(), myDatabase.loadOutstandingCommandsFromDatabase());
    }

    /**
     * The register method attempts to register a new user with the ServerModel with the given
     * parameters. The result will be sent to the single client via the ClientProxy. If unsuccessful,
     * the client which sent the command will be sent an error message via the ClientProxy.
     * @param username Unique identifier to register by.
     * @param password Password to verify identity.
     */
    @Override
    public boolean register(String username, String password, String sessionID) {
        return mSingletonModel.register(username, password, sessionID);
    }

    @Override
    public boolean login(String username, String password, String sessionID) {
        return mSingletonModel.login(username, password, sessionID);
    }

    /**
     * The pollGameList method queries the SeverModel for all unstarted games and then sends them to
     * the single client that requested it via the ClientProxy.
     * @param username The identifier of the single client who should receive the data.
     */
    @Override
    public boolean pollGameList(String username) {
        return mSingletonModel.pollGameList(username);
    }

    /**
     * The createGame method attempts to create a new game by giving the ServerModel parameters
     * by which to create a new unstarted game. If successful, every logged in user will have their
     * game list updated via the ClientProxy. If unsuccessful, the client which sent the command will
     * be sent an error message via the ClientProxy.
     * @param username Unique identifier of which client wants to create a game.
     * @param gameName Unique identifier of the game to be created.
     * @param playerNum Number of players required for the game to start.
     */
    @Override
    public boolean createGame(String username, String gameName, int playerNum) {
        return mSingletonModel.addNewUnstartedGame(username, gameName, playerNum);
    }

    /**
     * The joinGame method is an attempt by a client to join an existing game. If successful, every
     * logged in user will have their game list updated via the ClientProxy. If unsuccessful, the client
     * which sent the command will be sent an error message via the ClientProxy.
     * @param username Unique identifier of which client wants to join a game.
     * @param gameName The name of the game to be joined.
     */
    @Override
    public boolean joinGame(String username, String gameName) {
        return mSingletonModel.addPlayerToGame(username, gameName);
    }

    /**
     * The leaveGame method is an attempt by a client to leave a game. If successful, every logged in
     * user will have their game list updated via the ClientProxy. If unsuccessful, the client which
     * sent the command will be sent an error message via the ClientProxy.
     * @param username Unique identifier of which client wants to leave a game.
     * @param gameName Unique identifier of which game the client wants to leave.
     */
    @Override
    public boolean leaveGame(String username, String gameName) {
        return mSingletonModel.removePlayerFromGame(username, gameName);
    }

    /**
     * The startGame method is an attempt by a client to start the game identified by the given string.
     * If successful, every logged in user will have their game list updated via the ClientProxy. Each
     * client that is in the current game will be started along with the client who sent the request. If
     * unsuccessful, the client which sent the command will be sent an error message via the ClientProxy.
     * @param gameName Unique identifier of the game to be started.
     * @param username Unique identifier of the client which wants to start the game.
     */
    @Override
    public boolean startGame(String gameName, String username) {
        return mSingletonModel.startGame(gameName, username);
    }

    @Override
    public boolean reJoinGame(String username, String gameName) {
        return mSingletonModel.reJoinGame(username, gameName);
    }

    @Override
    public boolean drawThreeDestCards(String username, String gameName) {
        return mSingletonModel.drawThreeDestCards(gameName, username);
    }

    @Override
    public boolean returnDestCards(String username, String gameName, int destCard) {
        return mSingletonModel.returnDestCard(gameName, username, destCard);
    }

    @Override
    public boolean drawTrainCardFromDeck(String username, String gameName) {
        return mSingletonModel.drawTrainCardFromDeck(gameName, username);
    }

    @Override
    public boolean drawTrainCardFromFaceUp(String username, String gameName, int index) {
        return mSingletonModel.drawTrainCardFromFaceUp(gameName, username, index);
    }

    @Override
    public boolean claimRoute(String username, String gameName, int routeID, List<Integer> trainCards) {
        return mSingletonModel.claimRoute(gameName, username, routeID, trainCards);
    }

    @Override
    public boolean sendChatMessage(String username, String gameName, String message) {
        return mSingletonModel.chat(gameName, username, message);
    }


    //facade to database
    public void addUserToDatabase(String username){
        myDatabase.saveNewUserToDatabase(mSingletonModel.getUser(username));
    }

    public void addNewStartedGameToDatabase(String gameName){
        myDatabase.saveNewStartedGameToDatabase(mSingletonModel.getStartedGame(gameName));
    }

    public void addCommandToDatabase(String gameName, Command myCommand){
        if (myDatabase.saveCommandToDatabase(gameName, myCommand))
            myDatabase.updateStartedGameInDatabase(mSingletonModel.getStartedGame(gameName));
    }

    public boolean clearDatabase() {
        if (myDatabase.clearDatabase()) {
            logger.info("Clear database successful");
            return true;
        } else {
            logger.warning("Failed to clear database");
            return false;
        }
    }


}
