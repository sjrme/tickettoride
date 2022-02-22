package commands.game;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class ReturnDestCardsCommand extends Command implements Serializable {
    protected String gameName;
    protected int destCard;

    protected ReturnDestCardsCommand(){}
    public ReturnDestCardsCommand(String username, String gameName, int destCard){
        super.type = Utils.RETURN_DEST_CARDS_TYPE;
        super.username = username;
        this.gameName = gameName;
        this.destCard = destCard;
    }

    public int getDestCard() {
        return destCard;
    }
}
