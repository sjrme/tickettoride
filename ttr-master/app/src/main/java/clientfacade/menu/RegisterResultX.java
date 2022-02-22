package clientfacade.menu;

import clientfacade.ClientFacade;
import interfaces.IResultX;
import results.menu.RegisterResult;

public class RegisterResultX extends RegisterResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.registerUser(super.username, null, super.message, null);
    }
}
