package results.menu;


import java.io.Serializable;

import results.Result;
import utils.Utils;

public class JoinGameResult extends Result  implements Serializable {
    protected String gameName;

    protected JoinGameResult(){}
    public JoinGameResult(String username, String gameName){
        super.
                type = Utils.JOIN_TYPE;
        super.username = username;
        this.gameName = gameName;
    }
}
