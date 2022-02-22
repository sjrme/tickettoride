package model.State;

import java.io.Serializable;

import model.GamePlayException;
import model.StartedGame;

/**
 * Created by sjrme on 7/29/17.
 */

public class FirstRound implements TurnState,Serializable {

    private StartedGame game;

    public FirstRound(StartedGame game) {
        this.game = game;
    }

    @Override
    public void switchState(CommandType commandType) throws GamePlayException {
        if (commandType != CommandType.RETURN_DEST_CARD && commandType != CommandType.RETURN_NO_DEST_CARD) {
            throw new GamePlayException("Illegal move. You may only return dest cards.");
        }

        game.advancePlayerTurn();
        if (game.getTurnPointer() == 0) {
            game.setTurnState(new BeforeTurn(game));
        }
    }

    @Override
    public String getPrettyName(){
        return "FirstRound";
    }

}
