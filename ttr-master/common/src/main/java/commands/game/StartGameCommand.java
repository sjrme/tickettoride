package commands.game;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class StartGameCommand extends Command implements Serializable {
    protected String gameName;

    protected StartGameCommand(){}
    public StartGameCommand(String username, String gameName){
        super.setType(Utils.START_TYPE);
        super.username = username;
        this.gameName = gameName;
    }
    public String getGameName() {
        return gameName;
    }

    public void setUsername(String username) {
        this.gameName = username;
    }
}
