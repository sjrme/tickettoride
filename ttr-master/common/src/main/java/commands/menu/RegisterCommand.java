package commands.menu;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class RegisterCommand extends Command implements Serializable {
    protected String password;

    public RegisterCommand(String username, String password) {
        super.setType(Utils.REGISTER_TYPE);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType(){
        return super.getType();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
