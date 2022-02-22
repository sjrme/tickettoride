package results.menu;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class LeaveGameResult extends Result  implements Serializable {
    protected String gameName;

    protected LeaveGameResult(){}
    public LeaveGameResult(String username, String gameName){
        super.type = Utils.LEAVE_TYPE;
        super.username = username;
        this.gameName = gameName;
    }
}
