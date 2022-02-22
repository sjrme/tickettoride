package model.State;

import java.io.Serializable;

import model.GamePlayException;
import model.StartedGame;

/**
 * Created by sjrme on 7/29/17.
 */

public class BeforeTurn implements TurnState,Serializable {

    private StartedGame game;

    public BeforeTurn(StartedGame game) {
        this.game = game;
    }

    @Override
    public void switchState(CommandType commandType) throws GamePlayException {
        switch (commandType) {
            case DRAW_THREE_DEST_CARDS:
                game.setTurnState(new DrewThreeDestCards(game));
                break;
            case DRAW_TWO_DEST_CARDS:
                game.setTurnState(new DrewTwoDestCards(game));
                break;
            case DRAW_ONE_DEST_CARD:
                game.setTurnState(new DrewOneDestCard(game));
                break;
            case DRAW_TRAIN_CARD_FROM_DECK:
                game.setTurnState(new DrewOneTrainCard(game));
                break;
            case FACEUP_NON_LOCOMOTIVE:
                game.setTurnState(new DrewOneTrainCard(game));
                break;
            case FACEUP_LOCOMOTIVE:
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            case CLAIM_ROUTE:
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            default:
                throw new GamePlayException("Illegal move. Cannot return cards before any are drawn.");
        }
    }

    @Override
    public String getPrettyName(){
        return "BeforeTurn";
    }

}
