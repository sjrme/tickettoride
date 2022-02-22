package serverfacade.commands.menu;

import java.io.Serializable;

import commands.menu.RegisterCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class RegisterCommandX extends RegisterCommand implements ICommandX,Serializable {
    private transient ServerFacade serverFacade;

    public RegisterCommandX(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean execute(){
        serverFacade = new ServerFacade();
        return serverFacade.register(username, password, sessionID);
    }

    @Override
    public void addToDatabase() {
        serverFacade.addUserToDatabase(super.username);
    }
}
