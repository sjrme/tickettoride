package commands.game;

import java.io.Serializable;
import java.util.List;

import commands.Command;
import utils.Utils;

public class ClaimRouteCommand extends Command implements Serializable {
    protected String gameName;
    protected int routeID;
    protected List<Integer> trainCards;

    protected ClaimRouteCommand(){}
    public ClaimRouteCommand(String username, String gameName, int routeID, List<Integer> trainCards){
        super.type = Utils.CLAIM_ROUTE_TYPE;
        super.username = username;
        this.gameName = gameName;
        this.routeID = routeID;
        this.trainCards = trainCards;
    }

    public int getRouteID() {
        return routeID;
    }

    public List<Integer> getTrainCards() {
        return trainCards;
    }
}
