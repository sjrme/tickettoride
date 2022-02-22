package commands.game;

import java.io.Serializable;

import commands.Command;
import utils.Utils;

public class DrawTrainCardFromFaceUpCommand extends Command implements Serializable {
    protected String gameName;
    protected int index;

    protected DrawTrainCardFromFaceUpCommand(){}
    public DrawTrainCardFromFaceUpCommand(String username, String gameName, int index){
        super.type = Utils.DRAW_TRAIN_CARD_FACEUP_TYPE;
        super.username = username;
        this.gameName = gameName;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
