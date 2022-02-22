package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class ReturnDestCardsResult extends Result  implements Serializable {
    protected int destCards;

    protected ReturnDestCardsResult(){}
    public ReturnDestCardsResult(String username, int destCards){
        super.type = Utils.RETURN_DEST_CARDS_TYPE;
        super.username = username;
        this.destCards = destCards;
    }
}
