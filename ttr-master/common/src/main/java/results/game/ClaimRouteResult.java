package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

public class ClaimRouteResult extends Result  implements Serializable {
    protected int routeID;

    protected ClaimRouteResult(){}
    public ClaimRouteResult(String username, int routeID){
        super.type = Utils.CLAIM_ROUTE_TYPE;
        super.username = username;
        this.routeID = routeID;
    }
}
