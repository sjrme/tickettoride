package commands.menu;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class PollGamesCommand extends Command implements Serializable {

    public PollGamesCommand(String username) {
        super.setType(Utils.POLL_TYPE);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
