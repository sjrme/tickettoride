package commands.game;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class DrawTrainCardFromDeckCommand extends Command implements Serializable {
    protected String gameName;

    protected DrawTrainCardFromDeckCommand(){}
    public DrawTrainCardFromDeckCommand(String username, String gameName){
        super.type = Utils.DRAW_TRAIN_CARD_DECK_TYPE;
        super.username = username;
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
