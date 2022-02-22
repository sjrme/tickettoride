package clientfacade.menu;

import clientfacade.ClientFacade;
import results.menu.MessageResult;
import interfaces.IResultX;

public class MessageResultX extends MessageResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.postMessage(super.message);
    }
}
