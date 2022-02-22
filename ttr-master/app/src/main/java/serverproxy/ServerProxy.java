package serverproxy;

import java.util.List;

import clientcommunicator.ClientCommunicator;
import commands.game.ChatCommand;
import commands.game.ClaimRouteCommand;
import commands.game.DrawThreeDestCardsCommand;
import commands.game.DrawTrainCardFromDeckCommand;
import commands.game.DrawTrainCardFromFaceUpCommand;
import commands.game.RejoinCommand;
import commands.game.ReturnDestCardsCommand;
import commands.game.StartGameCommand;
import commands.menu.CreateGameCommand;
import commands.menu.JoinGameCommand;
import commands.menu.LeaveGameCommand;
import commands.menu.LoginCommand;
import commands.menu.PollGamesCommand;
import commands.menu.RegisterCommand;
import interfaces.IServer;

/**
 * The ServerProxy is the proxy on the client side that lets the server do the actual execution.
 */
public class ServerProxy implements IServer{
    private ClientCommunicator clientCommunicator = new ClientCommunicator();
    //private MockServer clientCommunicator = new MockServer();

    @Override
    public boolean login(String username, String password, String sessionID){
        LoginCommand command = new LoginCommand(username, password);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean register(String username, String password, String sessionID) {
        RegisterCommand command = new RegisterCommand(username, password);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean pollGameList(String username) {
        PollGamesCommand command = new PollGamesCommand(username);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean createGame(String username, String gameName, int playerNum) {
        CreateGameCommand command = new CreateGameCommand(username, gameName, playerNum);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean joinGame(String username, String gameName) {
        JoinGameCommand command = new JoinGameCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean leaveGame(String username, String gameName) {
        LeaveGameCommand command = new LeaveGameCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean startGame(String username, String gameName) {
        StartGameCommand command = new StartGameCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean reJoinGame(String username, String gameName) {
        RejoinCommand command = new RejoinCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean drawThreeDestCards(String username, String gameName) {
        DrawThreeDestCardsCommand command = new DrawThreeDestCardsCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean returnDestCards(String username, String gameName, int destCards) {
        ReturnDestCardsCommand command = new ReturnDestCardsCommand(username, gameName, destCards);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean drawTrainCardFromDeck(String username, String gameName) {
        DrawTrainCardFromDeckCommand command = new DrawTrainCardFromDeckCommand(username, gameName);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean drawTrainCardFromFaceUp(String username, String gameName, int index) {
        DrawTrainCardFromFaceUpCommand command = new DrawTrainCardFromFaceUpCommand(username, gameName, index);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean claimRoute(String username, String gameName, int routeID, List<Integer> trainCards) {
        ClaimRouteCommand command = new ClaimRouteCommand(username, gameName, routeID, trainCards);
        clientCommunicator.doCommand(command);
        return true;
    }

    @Override
    public boolean sendChatMessage(String username, String gameName, String message) {
        ChatCommand command = new ChatCommand(username, gameName, message);
        clientCommunicator.doCommand(command);
        return true;
    }
}
