package results.game;

import java.io.Serializable;

import model.GameData;
import results.Result;
import utils.Utils;

public class RejoinResult extends Result  implements Serializable {
    protected String gameName;
    protected GameData gameData;

    protected RejoinResult(){}
    public RejoinResult(String username, GameData gameData){
        super.type = Utils.REJOIN_TYPE;
        super.username = username;
        this.gameData = gameData;
    }

}
