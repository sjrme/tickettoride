package clientfacade.game;

import clientfacade.ClientFacade;
import results.game.ChatResult;
import interfaces.IResultX;

public class ChatResultX extends ChatResult implements IResultX {

    @Override
    public void execute() {
        ClientFacade clientFacade = new ClientFacade();
        clientFacade.addChat(super.username, super.message);
    }
}
