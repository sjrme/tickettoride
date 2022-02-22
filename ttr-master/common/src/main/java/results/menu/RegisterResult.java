package results.menu;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class RegisterResult extends Result  implements Serializable {
    protected String message;

    protected RegisterResult(){}
    public RegisterResult(String username, String message){
        super.type = Utils.REGISTER_TYPE;
        super.username = username;
        this.message = message;
    }
}
