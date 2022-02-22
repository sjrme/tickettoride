package model;

import java.io.Serializable;

/**
 * Created by sjrme on 7/21/17.
 */

public enum PlayerColor implements Serializable{
    RED,
    BLUE,
    GREEN,
    YELLOW,
    BLACK;


    private static PlayerColor[] values = PlayerColor.values();

    public static PlayerColor getPlayerColor(int index) {
        return values[index];
    }
}
