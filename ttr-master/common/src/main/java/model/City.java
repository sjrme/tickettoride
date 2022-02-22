package model;

import java.io.Serializable;

/**
 * Created by sjrme on 7/13/17.
 */

public enum City implements Serializable{

    ATLANTA ("Atlanta"),
    BOSTON ("Boston"),
    CALGARY ("Calgary"),
    CHARLESTON ("Charleston"),
    CHICAGO ("Chicago"),
    DALLAS ("Dallas"),
    DENVER ("Denver"),
    DULUTH ("Duluth"),
    EL_PASO ("El Paso"),
    HELENA ("Helena"),
    HOUSTON ("Houston"),
    KANSAS_CITY ("Kansas City"),
    LAS_VEGAS ("Las Vegas"),
    LITTLE_ROCK ("Little Rock"),
    LOS_ANGELES ("Los Angeles"),
    MIAMI ("Miami"),
    MONTREAL ("Montreal"),
    NASHVILLE ("Nashville"),
    NEW_ORLEANS ("New Orleans"),
    NEW_YORK ("New York"),
    OKLAHOMA_CITY ("Oklahoma City"),
    OMAHA ("Omaha"),
    PHOENIX ("Phoenix"),
    PITTSBURGH ("Pittsburgh"),
    PORTLAND ("Portland"),
    RALEIGH ("Raleigh"),
    ST_LOUIS ("St. Louis"),
    SALT_LAKE_CITY ("Salt Lake City"),
    SAN_FRANCISCO ("San Francisco"),
    SANTA_FE ("Santa Fe"),
    SAULT_ST_MARIE ("Sault St. Marie"),
    SEATTLE ("Seattle"),
    TORONTO ("Toronto"),
    VANCOUVER ("Vancouver"),
    WASHINGTON ("Washington"),
    WINNIPEG("Winnipeg");

    private String mPrettyName;

    City(String prettyName) {
        mPrettyName = prettyName;
    }

    public String getPrettyName() {
        return mPrettyName;
    }
}
