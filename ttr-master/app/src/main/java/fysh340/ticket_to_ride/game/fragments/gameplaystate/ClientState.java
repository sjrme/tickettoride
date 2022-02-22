package fysh340.ticket_to_ride.game.fragments.gameplaystate;

import clientfacade.ClientFacade;

/**
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-08-03
 */
public class ClientState {

    public static ClientState INSTANCE = new ClientState();

    private ClientState() {}

    private GamePlayState mState = new NotMyTurnState();

    public void setState(GamePlayState state) {
        mState = state;
    }

    public GamePlayState getState() {
        return mState;
    }
}
