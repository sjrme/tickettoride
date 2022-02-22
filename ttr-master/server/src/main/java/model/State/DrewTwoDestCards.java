package model.State;

import java.io.Serializable;

import model.GamePlayException;
import model.StartedGame;

/**
 * Created by sjrme on 8/1/17.
 */

public class DrewTwoDestCards implements TurnState,Serializable {


    private StartedGame game;
    public DrewTwoDestCards(StartedGame game) {
        this.game = game;
    }

    @Override
    public void switchState(CommandType commandType) throws GamePlayException {
        switch (commandType) {
            case RETURN_DEST_CARD: {
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            }
            case RETURN_NO_DEST_CARD: {
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            }
            default:{
                throw new GamePlayException("Illegal move. You may only return dest cards.");
            }
        }
    }

    @Override
    public String getPrettyName() {
        return "DrewTwoDestCards";
    }
}
