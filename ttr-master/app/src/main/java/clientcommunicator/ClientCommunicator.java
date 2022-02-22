package clientcommunicator;

import com.google.gson.Gson;

import commands.Command;
import commands.game.ChatCommand;
import commands.game.ClaimRouteCommand;
import commands.game.DrawThreeDestCardsCommand;
import commands.game.DrawTrainCardFromDeckCommand;
import commands.game.DrawTrainCardFromFaceUpCommand;
import commands.game.RejoinCommand;
import commands.game.ReturnDestCardsCommand;
import commands.game.ReturnFirstDestCardCommand;
import commands.game.StartGameCommand;
import commands.menu.CreateGameCommand;
import commands.menu.JoinGameCommand;
import commands.menu.LeaveGameCommand;
import commands.menu.LoginCommand;
import commands.menu.PollGamesCommand;
import commands.menu.RegisterCommand;
import utils.Utils;
import websocket.ClientWebSocket;

/**
 *
 */
public class ClientCommunicator {
    private ClientWebSocket webSocket = ClientWebSocket.getClientWebSocket();
    private Gson gson = new Gson();


    public void doCommand(Command command) {
        String myJsonString;
        switch (command.getType()) {
            case Utils.LOGIN_TYPE:
                myJsonString = gson.toJson(command, LoginCommand.class);
                break;
            case Utils.REGISTER_TYPE:
                myJsonString = gson.toJson(command, RegisterCommand.class);
                break;
            case Utils.POLL_TYPE:
                myJsonString = gson.toJson(command, PollGamesCommand.class);
                break;
            case Utils.CREATE_TYPE:
                myJsonString = gson.toJson(command, CreateGameCommand.class);
                break;
            case Utils.JOIN_TYPE:
                myJsonString = gson.toJson(command, JoinGameCommand.class);
                break;
            case Utils.LEAVE_TYPE:
                myJsonString = gson.toJson(command, LeaveGameCommand.class);
                break;
            case Utils.START_TYPE:
                myJsonString = gson.toJson(command, StartGameCommand.class);
                break;
            case Utils.REJOIN_TYPE:
                myJsonString = gson.toJson(command, RejoinCommand.class);
                break;
            case Utils.MESSAGE_TYPE:
                myJsonString = gson.toJson(command, ChatCommand.class);
                break;
            case Utils.DRAW_DEST_CARDS_TYPE:
                myJsonString = gson.toJson(command, DrawThreeDestCardsCommand.class);
                break;
            case Utils.RETURN_FIRST_DEST_CARD_TYPE:
                myJsonString = gson.toJson(command, ReturnFirstDestCardCommand.class);
                break;
            case Utils.RETURN_DEST_CARDS_TYPE:
                myJsonString = gson.toJson(command, ReturnDestCardsCommand.class);
                break;
            case Utils.DRAW_TRAIN_CARD_DECK_TYPE:
                myJsonString = gson.toJson(command, DrawTrainCardFromDeckCommand.class);
                break;
            case Utils.DRAW_TRAIN_CARD_FACEUP_TYPE:
                myJsonString = gson.toJson(command, DrawTrainCardFromFaceUpCommand.class);
                break;
            case Utils.CLAIM_ROUTE_TYPE:
                myJsonString = gson.toJson(command, ClaimRouteCommand.class);
                break;
            case Utils.CHAT_TYPE:
                myJsonString = gson.toJson(command, ChatCommand.class);
                break;
            default:
                myJsonString = "Error parsing Json String. Check ClientCommunicator";
        }


        webSocket.sendJson(myJsonString);
    }
}
