package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class ChatResult extends Result  implements Serializable {
    protected String message;

    protected ChatResult(){}
    public ChatResult(String username, String message){
        super.type = Utils.CHAT_TYPE;
        super.username = username;
        this.message = message;
    }
}
