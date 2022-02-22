package serverfacade.commands.menu;

import java.io.Serializable;

import commands.menu.LoginCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class LoginCommandX extends LoginCommand implements ICommandX,Serializable {

    public LoginCommandX(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean execute(){
        ServerFacade serverFacade = new ServerFacade();
        return serverFacade.login(username, password, sessionID);
    }

    //This does not need to be saved
    @Override
    public void addToDatabase() {}
}
