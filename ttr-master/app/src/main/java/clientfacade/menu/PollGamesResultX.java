package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.PollGamesResult;
import interfaces.IResultX;

public class PollGamesResultX extends PollGamesResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.updateSingleUserGameList(super.username, super.unstartedGameList, super.runningGameList);
    }
}
