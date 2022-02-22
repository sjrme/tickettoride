package results.menu;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class CreateGameResult extends Result  implements Serializable {
    protected String gameName;

    protected CreateGameResult(){}
    public CreateGameResult(String username, String gameName){
        super.type = Utils.CREATE_TYPE;
        super.username = username;
        this.gameName = gameName;
    }
}
