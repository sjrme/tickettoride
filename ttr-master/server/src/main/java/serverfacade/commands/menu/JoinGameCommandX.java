package serverfacade.commands.menu;

import java.io.Serializable;

import commands.menu.JoinGameCommand;
import interfaces.ICommandX;
import serverfacade.ServerFacade;

public class JoinGameCommandX extends JoinGameCommand implements ICommandX,Serializable {

    public JoinGameCommandX(String username, String gameName){
        super(username, gameName);
    }

    @Override
    public boolean execute() {
        ServerFacade serverFacade = new ServerFacade();
        return serverFacade.joinGame(username, gameName);
    }

    //This does not need to be saved.
    @Override
    public void addToDatabase() {}
}
