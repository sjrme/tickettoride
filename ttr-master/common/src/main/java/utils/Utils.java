package utils;

import java.io.Serializable;

public class Utils implements Serializable{
    public static final String CLEAR_TYPE = "cleardb";
    public static final String LOGIN_TYPE = "login";
    public static final String LOGOUT_TYPE = "logout";
    public static final String REGISTER_TYPE = "register";

    public static final String POLL_TYPE = "pollgames";
    public static final String CREATE_TYPE = "creategame";
    public static final String JOIN_TYPE = "joingame";
    public static final String LEAVE_TYPE = "leavegame";

    public static final String START_TYPE = "startgame";
    public static final String REJOIN_TYPE = "rejoingame";

    public static final String MESSAGE_TYPE = "message";
    public static final String DRAW_DEST_CARDS_TYPE = "drawthreedestcards";
    public static final String RETURN_FIRST_DEST_CARD_TYPE = "returnfirstdest";
    public static final String RETURN_DEST_CARDS_TYPE = "returndestcards";

    public static final String DRAW_TRAIN_CARD_DECK_TYPE = "drawtraincarddeck";
    public static final String DRAW_TRAIN_CARD_FACEUP_TYPE = "drawtraincardfaceup";
    public static final String CLAIM_ROUTE_TYPE = "claimroute";
    public static final String CHAT_TYPE = "chatmessage";
    public static final String GAME_HISTORY_TYPE = "gamehistory";
    public static final String REPLACE_ALL_FACEUP_TYPE = "replaceallfaceup";
    public static final String ALL_PLAYER_INFO_TYPE = "allplayerinfo";

    public static final String END_GAME_TYPE = "endgame";
    public static final String FINAL_ROUND_TYPE = "finalround";
    public static final String TURN_TYPE = "turn";

    public static final String REJECT_TYPE = "reject";

    public static final String SERVER_LOG = "serverlog";
}
