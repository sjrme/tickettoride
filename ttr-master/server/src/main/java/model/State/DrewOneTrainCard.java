package model.State;

import java.io.Serializable;

import model.GamePlayException;
import model.StartedGame;
import model.TrainCard;

import static model.TrainCard.WILD;

/**
 * Created by sjrme on 7/29/17.
 */

public class DrewOneTrainCard implements TurnState,Serializable{

    private StartedGame game;

    public DrewOneTrainCard(StartedGame game) {
        this.game = game;
        if (game.getFaceUpCards().size() == 0) {
            game.advancePlayerTurn();
            game.setTurnState(new BeforeTurn(game));
        }
        for(int i = 0; i < game.getFaceUpCards().size(); i++) {
            if (game.getFaceUpCards().get(i) != TrainCard.getTrainCardKey(WILD)) {
                return;
            }
        }
        game.advancePlayerTurn();
        game.setTurnState(new BeforeTurn(game));
    }

    @Override
    public void switchState(CommandType commandType) throws GamePlayException {
        switch (commandType) {
            case DRAW_TRAIN_CARD_FROM_DECK: {
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            }
            case FACEUP_NON_LOCOMOTIVE: {
                game.advancePlayerTurn();
                game.setTurnState(new BeforeTurn(game));
                break;
            }
            default:
                throw new GamePlayException("Illegal move. You may only draw train cards.");
        }
    }

    @Override
    public String getPrettyName(){
        return "DrewOneTrainCard";
    }

}
