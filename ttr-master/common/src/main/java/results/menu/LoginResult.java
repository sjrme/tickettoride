package results.menu;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class LoginResult extends Result  implements Serializable {

    protected LoginResult(){}
    public LoginResult(String username){
        super.type = Utils.LOGIN_TYPE;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
