package results.game;

import java.io.Serializable;

import results.Result;
import utils.Utils;

/**
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 7/25/17
 */
public class RejectResult extends Result  implements Serializable {

    private String message;

    public RejectResult(String message) {
        super.type = Utils.REJECT_TYPE;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
