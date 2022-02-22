package clientcommunicator;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import clientfacade.game.ChatResultX;
import clientfacade.game.ClaimRouteResultX;
import clientfacade.game.DrawThreeDestCardsResultX;
import clientfacade.game.DrawTrainCardFromDeckResultX;
import clientfacade.game.DrawTrainCardFromFaceUpResultX;
import clientfacade.game.EndGameResultX;
import clientfacade.game.FinalRoundResultX;
import clientfacade.game.GameHistoryX;
import clientfacade.game.RejectResultX;
import clientfacade.game.RejoinResultX;
import clientfacade.game.ReplaceFaceUpCardsResultX;
import clientfacade.game.ReturnDestCardsResultX;
import clientfacade.game.ReturnFirstDestCardResultX;
import clientfacade.game.StartGameResultX;
import clientfacade.game.TurnResultX;
import clientfacade.menu.CreateGameResultX;
import clientfacade.menu.JoinGameResultX;
import clientfacade.menu.LeaveGameResultX;
import clientfacade.menu.LoginResultX;
import clientfacade.menu.MessageResultX;
import clientfacade.menu.PollGamesResultX;
import clientfacade.menu.RegisterResultX;
import results.Result;
import utils.Utils;

/**
 * Client side. Receives a result from the server and makes it into an executable result.
 */
public class CommandResultXSerializer implements JsonDeserializer<Result> {

    @Override
    public Result deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("type");
        String type = jsonType.getAsString();

        Gson gson = new Gson();
        Result typeModel = null;

        switch (type){
            case Utils.LOGIN_TYPE:
                typeModel = gson.fromJson(jsonObject, LoginResultX.class);
                break;
            case Utils.REGISTER_TYPE:
                typeModel = gson.fromJson(jsonObject, RegisterResultX.class);
                break;
            case Utils.POLL_TYPE:
                typeModel = gson.fromJson(jsonObject, PollGamesResultX.class);
                break;
            case Utils.CREATE_TYPE:
                typeModel = gson.fromJson(jsonObject, CreateGameResultX.class);
                break;
            case Utils.JOIN_TYPE:
                typeModel = gson.fromJson(jsonObject, JoinGameResultX.class);
                break;
            case Utils.LEAVE_TYPE:
                typeModel = gson.fromJson(jsonObject, LeaveGameResultX.class);
                break;
            case Utils.START_TYPE:
                typeModel = gson.fromJson(jsonObject, StartGameResultX.class);
                break;
            case Utils.REJOIN_TYPE:
                typeModel = gson.fromJson(jsonObject, RejoinResultX.class);
                break;
            case Utils.MESSAGE_TYPE:
                typeModel = gson.fromJson(jsonObject, MessageResultX.class);
                break;
            case Utils.DRAW_DEST_CARDS_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawThreeDestCardsResultX.class);
                break;
            case Utils.RETURN_FIRST_DEST_CARD_TYPE:
                typeModel = gson.fromJson(jsonObject, ReturnFirstDestCardResultX.class);
                break;
            case Utils.RETURN_DEST_CARDS_TYPE:
                typeModel = gson.fromJson(jsonObject, ReturnDestCardsResultX.class);
                break;
            case Utils.DRAW_TRAIN_CARD_DECK_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawTrainCardFromDeckResultX.class);
                break;
            case Utils.DRAW_TRAIN_CARD_FACEUP_TYPE:
                typeModel = gson.fromJson(jsonObject, DrawTrainCardFromFaceUpResultX.class);
                break;
            case Utils.CLAIM_ROUTE_TYPE:
                typeModel = gson.fromJson(jsonObject, ClaimRouteResultX.class);
                break;
            case Utils.CHAT_TYPE:
                typeModel = gson.fromJson(jsonObject, ChatResultX.class);
                break;
            case Utils.GAME_HISTORY_TYPE:
                typeModel = gson.fromJson(jsonObject, GameHistoryX.class);
                break;
            case Utils.REPLACE_ALL_FACEUP_TYPE:
                typeModel = gson.fromJson(jsonObject, ReplaceFaceUpCardsResultX.class);
                break;
            case Utils.REJECT_TYPE:
                typeModel = gson.fromJson(jsonObject, RejectResultX.class);
                break;
            case Utils.FINAL_ROUND_TYPE:
                typeModel = gson.fromJson(jsonObject, FinalRoundResultX.class);
                break;
            case Utils.END_GAME_TYPE:
                typeModel = gson.fromJson(jsonObject, EndGameResultX.class);
                break;
            case Utils.TURN_TYPE:
                typeModel = gson.fromJson(jsonObject, TurnResultX.class);
                break;

        }
        return typeModel;
    }
}
