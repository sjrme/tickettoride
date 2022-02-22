package results.game;

import java.io.Serializable;
import java.util.List;

import results.Result;
import utils.Utils;

public class StartGameResult extends Result  implements Serializable {
    protected String gameName;
    protected List<String> playerNames;
    protected List<Integer> destCards;
    protected List<Integer> trainCards;
    protected List<Integer> faceUpCards;

    protected StartGameResult(){}
    public StartGameResult(String username, String gameName, List<String> playerNames, List<Integer> destCards,
                           List<Integer> trainCards, List<Integer> faceUpCards){
        super.type = Utils.START_TYPE;
        super.username = username;
        this.gameName = gameName;
        this.playerNames = playerNames;
        this.destCards = destCards;
        this.trainCards = trainCards;
        this.faceUpCards = faceUpCards;
    }

    public String getUsername() {
        return username;
    }
    public String getGameName() { return gameName; }
}
