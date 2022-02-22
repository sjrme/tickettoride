package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

/**
 * Created by sjrme on 7/29/17.
 */

public class TurnResult extends Result implements Serializable {

    protected TurnResult(){}
    public TurnResult(String username){
        super.type = Utils.TURN_TYPE;
        super.username = username;
    }
}
