package serverfacade.commands.menu;

import java.io.Serializable;

import commands.Command;
import interfaces.ICommandX;
import serverfacade.ServerFacade;
import utils.Utils;

public class ClearDatabaseCommandX extends Command implements ICommandX,Serializable {

    public ClearDatabaseCommandX(String username) {
        super.username = username;
        super.setType(Utils.CLEAR_TYPE);
    }

    @Override
    public boolean execute() {
        ServerFacade serverFacade = new ServerFacade();
        return serverFacade.clearDatabase();
    }

    //DO NOT STORE DESTRUCTIVE COMMANDS IN THE DATABASE
    @Override
    public void addToDatabase() {}
}
