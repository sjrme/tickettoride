package model.State;

import java.io.Serializable;

import model.GamePlayException;
import model.StartedGame;

/**
 * Created by sjrme on 7/29/17.
 */

public class DrewThreeDestCards implements TurnState,Serializable {

    private StartedGame game;

    public DrewThreeDestCards(StartedGame game) {
        this.game = game;
    }

    @Override
    public void switchState(CommandType commandType) throws GamePlayException {
        switch (commandType) {
            case RETURN_DEST_CARD: {
                game.setTurnState(new ReturnedOneDestCard(game));
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
    public String getPrettyName(){
        return "DrewThreeDestCards";
    }

}
