package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import commands.Command;
import serverfacade.commands.game.ChatCommandX;
import serverfacade.commands.game.ClaimRouteCommandX;
import serverfacade.commands.game.DrawThreeDestCardsCommandX;
import serverfacade.commands.game.DrawTrainCardFromDeckCommandX;
import serverfacade.commands.game.DrawTrainCardFromFaceUpCommandX;
import serverfacade.commands.game.RejoinCommandX;
import serverfacade.commands.game.ReturnDestCardsCommandX;
import serverfacade.commands.game.ReturnFirstDestCardCommandX;
import serverfacade.commands.game.StartGameCommandX;
import serverfacade.commands.menu.ClearDatabaseCommandX;
import serverfacade.commands.menu.CreateGameCommandX;
import serverfacade.commands.menu.JoinGameCommandX;
import serverfacade.commands.menu.LeaveGameCommandX;
import serverfacade.commands.menu.LoginCommandX;
import serverfacade.commands.menu.PollGamesCommandX;
import serverfacade.commands.menu.RegisterCommandX;
import utils.Utils;

/**
 * Server Side. Gets a command as a JSON string and makes it a real command.
 */
class CommandXSerializer implements JsonDeserializer<Command> {

    private static Logger logger = Logger.getLogger(Utils.SERVER_LOG);

    @Override
    public Command deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("type");
        String type = jsonType.getAsString();
        Gson gson = new Gson();
        Command typeModel = null;

        switch (type){
            case Utils.CLEAR_TYPE:
                typeModel = gson.fromJson(jsonObject, ClearDatabaseCommandX.class);
                break;
            case Utils.LOGIN_TYPE:
                typeModel = gson.fromJson(jsonObject, LoginCommandX.class);
                break;
            case Utils.REGISTER_TYPE:
                typeModel = gson.fromJson(jsonObject, RegisterCommandX.class);
                break;
            case Utils.POLL_TYPE:
                typeModel = gson.fromJson(jsonObject, PollGamesCommandX.class);
                break;
            case Utils.CREATE_TYPE:
                typeModel = gson.fromJson(jsonObject, CreateGameCommandX.class);
                break;
            case Utils.JOIN_TYPE:
                typeModel = gson.fromJson(jsonObject, JoinGameCommandX.class);
                break;
            case Utils.LEAVE_TYPE:
                typeModel = gson.fromJson(jsonObject, LeaveGameCommandX.class);
                break;
            case Utils.START_TYPE:
                typeModel = gson.fromJson(jsonObject, StartGameCommandX.class);
                break;
            case Utils.REJOIN_TYPE:
                typeModel = gson.fromJson(jsonObject, RejoinCommandX.class);
                break;
            case Utils.RETURN_FIRST_DEST_CARD_TYPE:
                typeModel = gson.fromJson(jsonObject, ReturnFirstDestCardCommandX.class);
                break;
            case Utils.DRAW_DEST_CARDS_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawThreeDestCardsCommandX.class);
                break;
            case Utils.RETURN_DEST_CARDS_TYPE:
                typeModel = gson.fromJson(jsonObject, ReturnDestCardsCommandX.class);
                break;
            case Utils.DRAW_TRAIN_CARD_DECK_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawTrainCardFromDeckCommandX.class);
                break;
            case Utils.DRAW_TRAIN_CARD_FACEUP_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawTrainCardFromFaceUpCommandX.class);
                break;
            case Utils.CLAIM_ROUTE_TYPE:
                typeModel = gson.fromJson(jsonObject, ClaimRouteCommandX.class);
                break;
            case Utils.CHAT_TYPE:
                typeModel = gson.fromJson(jsonObject, ChatCommandX.class);
                break;
        }
        return typeModel;
    }
}
