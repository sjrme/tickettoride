package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.LoginResult;
import interfaces.IResultX;

public class LoginResultX extends LoginResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.loginUser(super.username, null, null);
    }
}
