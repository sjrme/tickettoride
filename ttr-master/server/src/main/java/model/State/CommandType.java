package model.State;

import java.io.Serializable;

/**
 * Created by sjrme on 7/29/17.
 */

public enum CommandType implements Serializable {
    DRAW_THREE_DEST_CARDS,
    DRAW_TWO_DEST_CARDS,
    DRAW_ONE_DEST_CARD,
    RETURN_DEST_CARD,
    RETURN_NO_DEST_CARD,
    DRAW_TRAIN_CARD_FROM_DECK,
    FACEUP_NON_LOCOMOTIVE,
    FACEUP_LOCOMOTIVE,
    CLAIM_ROUTE,
}
