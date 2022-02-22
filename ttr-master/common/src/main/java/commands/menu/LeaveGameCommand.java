package commands.menu;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class LeaveGameCommand extends Command implements Serializable {
    protected String gameName;

    public LeaveGameCommand(String username, String gameName){
        super.setType(Utils.LEAVE_TYPE);
        this.username = username;
        this.gameName = gameName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameName() {
        return gameName;
    }
}
