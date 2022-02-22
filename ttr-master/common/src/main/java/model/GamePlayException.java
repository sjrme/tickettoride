package model;

import java.io.Serializable;

/**
 * Created by sjrme on 7/14/17.
 */

public class GamePlayException extends Exception implements Serializable{

    public GamePlayException(String message){
        super(message);
    }
}
