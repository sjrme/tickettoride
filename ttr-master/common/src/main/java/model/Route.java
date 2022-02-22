package model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static model.City.*;
import static model.TrainCard.BLACK;
import static model.TrainCard.BLUE;
import static model.TrainCard.GREEN;
import static model.TrainCard.ORANGE;
import static model.TrainCard.PURPLE;
import static model.TrainCard.RED;
import static model.TrainCard.WHITE;
import static model.TrainCard.WILD;
import static model.TrainCard.YELLOW;


public class Route implements Serializable {

    private City startCity;
    private City endCity;
    private int length;
    private TrainCard originalColor;
    private PlayerColor claimedColor;
    private int sisterRouteKey;
    private static Map<Integer, Route> routeMap;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    private boolean claimed = false;
    private int pointValue;
    private String owner;
    private boolean doubleRoute;
    private boolean sisterRouteClaimed;


    public Route(City startCity, City endCity, int length, TrainCard color, int sisterRouteKey) {

        this.startCity = startCity;
        this.endCity = endCity;
        this.length = length;
        this.originalColor = color;
        this.sisterRouteKey = sisterRouteKey;

        setPointValue();
        if (sisterRouteKey > -1) {
            doubleRoute = true;
        }
        else {
            doubleRoute = false;
        }
    }

    public boolean claimRoute(PlayerColor playerColor, String playerName,
                              int numOfPlayersInGame, boolean sisterRouteClaimed) {

        final int DOUBLE_ROUTE_LIMIT = 3;
        if (sisterRouteClaimed){
            if (numOfPlayersInGame < DOUBLE_ROUTE_LIMIT) {
                return false;
            }
        }
        owner = playerName;
        claimedColor = playerColor;
        claimed = true;
        return true;
    }

    public static Route getRouteByID(int routeID){
        if(routeMap==null)
        {
            routeMap=createRouteMap();
        }
        return routeMap.get(routeID);
    }

    void claim() {
        claimed = true;
    }

    //RouteMap is not like a DestCardMap; each game needs its own map it adjusts,
    //so it cannot return a static member.
    //If there is no "sister" or double route, the sisterRoute key passed is -1.
    //This is used to initialize doubleRoute boolean to false.
    public static HashMap<Integer, Route> createRouteMap() {
        HashMap<Integer, Route> routeMap = new HashMap<>();

        routeMap.put(0, new Route(ATLANTA, CHARLESTON, 2, WILD, -1));
        routeMap.put(1, new Route(ATLANTA, MIAMI, 5, BLUE, -1));
        routeMap.put(2, new Route(ATLANTA, NASHVILLE, 1, WILD, -1));
        routeMap.put(3, new Route(ATLANTA, NEW_ORLEANS, 4, YELLOW, 4));
        routeMap.put(4, new Route(ATLANTA, NEW_ORLEANS, 4, ORANGE, 3));
        routeMap.put(5, new Route(ATLANTA, RALEIGH, 2, WILD, 6));
        routeMap.put(6, new Route(ATLANTA, RALEIGH, 2, WILD, 5));

        routeMap.put(7, new Route(BOSTON, MONTREAL, 2, WILD, 8));
        routeMap.put(8, new Route(BOSTON, MONTREAL, 2, WILD, 7));
        routeMap.put(9, new Route(BOSTON, NEW_YORK, 2, RED, 10));
        routeMap.put(10, new Route(BOSTON, NEW_YORK, 2, YELLOW, 9));

        routeMap.put(11, new Route(CALGARY, HELENA, 4, WILD, -1));
        routeMap.put(12, new Route(CALGARY, SEATTLE, 4, WILD, -1));
        routeMap.put(13, new Route(CALGARY, VANCOUVER, 3, WILD, -1));
        routeMap.put(14, new Route(CALGARY, WINNIPEG, 6, WHITE, -1));

        routeMap.put(15, new Route(CHARLESTON, MIAMI, 4, PURPLE, -1));
        routeMap.put(16, new Route(CHARLESTON, RALEIGH, 2, WILD, -1));

        routeMap.put(17, new Route(CHICAGO, DULUTH, 3, RED, -1));
        routeMap.put(18, new Route(CHICAGO, ST_LOUIS, 2, GREEN, 19));
        routeMap.put(19, new Route(CHICAGO, ST_LOUIS, 2, WHITE, 18));
        routeMap.put(20, new Route(CHICAGO, OMAHA, 4, BLUE, -1));
        routeMap.put(21, new Route(CHICAGO, PITTSBURGH, 3, ORANGE, 22));
        routeMap.put(22, new Route(CHICAGO, PITTSBURGH, 3, BLACK, 21));
        routeMap.put(23, new Route(CHICAGO, TORONTO, 4, WHITE, -1));

        routeMap.put(24, new Route(DALLAS, EL_PASO, 4, RED, -1));
        routeMap.put(25, new Route(DALLAS, HOUSTON, 1, WILD, 26));
        routeMap.put(26, new Route(DALLAS, HOUSTON, 1, WILD, 25));
        routeMap.put(27, new Route(DALLAS, LITTLE_ROCK, 2, WILD, -1));
        routeMap.put(28, new Route(DALLAS, OKLAHOMA_CITY, 2, WILD, 29));
        routeMap.put(29, new Route(DALLAS, OKLAHOMA_CITY, 2, WILD, 28));

        routeMap.put(30, new Route(DENVER, HELENA, 4, GREEN, -1));
        routeMap.put(31, new Route(DENVER, KANSAS_CITY, 4, BLACK, 32));
        routeMap.put(32, new Route(DENVER, KANSAS_CITY, 4, ORANGE, 31));
        routeMap.put(33, new Route(DENVER, OKLAHOMA_CITY, 4, RED, -1));
        routeMap.put(34, new Route(DENVER, OMAHA, 4, PURPLE, -1));
        routeMap.put(35, new Route(DENVER, PHOENIX, 5, WHITE, -1));
        routeMap.put(36, new Route(DENVER, SALT_LAKE_CITY, 3, RED, 37));
        routeMap.put(37, new Route(DENVER, SALT_LAKE_CITY, 3, YELLOW, 36));
        routeMap.put(38, new Route(DENVER, SANTA_FE, 2, WILD, -1));

        routeMap.put(39, new Route(DULUTH, HELENA, 6, ORANGE, -1));
        routeMap.put(40, new Route(DULUTH, OMAHA, 2, WILD, 41));
        routeMap.put(41, new Route(DULUTH, OMAHA, 2, WILD, 40));
        routeMap.put(42, new Route(DULUTH, SAULT_ST_MARIE, 3, WILD, -1));
        routeMap.put(43, new Route(DULUTH, TORONTO, 6, PURPLE, -1));
        routeMap.put(44, new Route(DULUTH, WINNIPEG, 4, BLACK, -1));

        routeMap.put(45, new Route(EL_PASO, HOUSTON, 6, GREEN, -1));
        routeMap.put(46, new Route(EL_PASO, LOS_ANGELES, 6, BLACK, -1));
        routeMap.put(47, new Route(EL_PASO, OKLAHOMA_CITY, 5, YELLOW, -1));
        routeMap.put(48, new Route(EL_PASO, PHOENIX, 3, WILD, -1));
        routeMap.put(49, new Route(EL_PASO, SANTA_FE, 2, WILD, -1));

        routeMap.put(50, new Route(HELENA, OMAHA, 5, RED, -1));
        routeMap.put(51, new Route(HELENA, SALT_LAKE_CITY, 3, PURPLE, -1));
        routeMap.put(52, new Route(HELENA, SEATTLE, 6, YELLOW, -1));
        routeMap.put(53, new Route(HELENA, WINNIPEG, 4, BLUE, -1));

        routeMap.put(54, new Route(HOUSTON, NEW_ORLEANS, 2, WILD, -1));

        routeMap.put(55, new Route(KANSAS_CITY, OKLAHOMA_CITY, 2, WILD, 56));
        routeMap.put(56, new Route(KANSAS_CITY, OKLAHOMA_CITY, 2, WILD, 55));
        routeMap.put(57, new Route(KANSAS_CITY, OMAHA, 1, WILD, 58));
        routeMap.put(58, new Route(KANSAS_CITY, OMAHA, 1, WILD, 57));
        routeMap.put(59, new Route(KANSAS_CITY, ST_LOUIS, 2, BLUE, 60));
        routeMap.put(60, new Route(KANSAS_CITY, ST_LOUIS, 2, PURPLE, 59));

        routeMap.put(61, new Route(LAS_VEGAS, LOS_ANGELES, 2, WILD, -1));
        routeMap.put(62, new Route(LAS_VEGAS, SALT_LAKE_CITY, 3, ORANGE, -1));

        routeMap.put(63, new Route(LITTLE_ROCK, NASHVILLE, 3, WHITE, -1));
        routeMap.put(64, new Route(LITTLE_ROCK, NEW_ORLEANS, 3, GREEN, -1));
        routeMap.put(65, new Route(LITTLE_ROCK, OKLAHOMA_CITY, 2, WILD, -1));
        routeMap.put(66, new Route(LITTLE_ROCK, ST_LOUIS, 2, WILD, -1));

        routeMap.put(67, new Route(LOS_ANGELES, PHOENIX, 3, WILD, -1));
        routeMap.put(68, new Route(LOS_ANGELES, SAN_FRANCISCO, 3, PURPLE, 69));
        routeMap.put(69, new Route(LOS_ANGELES, SAN_FRANCISCO, 3, YELLOW, 68));

        routeMap.put(70, new Route(MIAMI, NEW_ORLEANS, 6, RED, -1));

        routeMap.put(71, new Route(MONTREAL, NEW_YORK, 3, BLUE, -1));
        routeMap.put(72, new Route(MONTREAL, SAULT_ST_MARIE, 5, BLACK, -1));
        routeMap.put(73, new Route(MONTREAL, TORONTO, 3, WILD, -1));

        routeMap.put(74, new Route(NASHVILLE, PITTSBURGH, 4, YELLOW, -1));
        routeMap.put(75, new Route(NASHVILLE, RALEIGH, 3, BLACK, -1));
        routeMap.put(76, new Route(NASHVILLE, ST_LOUIS, 2, WILD, -1));

        routeMap.put(77, new Route(NEW_YORK, PITTSBURGH, 2, WHITE, 78));
        routeMap.put(78, new Route(NEW_YORK, PITTSBURGH, 2, GREEN, 77));
        routeMap.put(79, new Route(NEW_YORK, WASHINGTON, 2, ORANGE, 80));
        routeMap.put(80, new Route(NEW_YORK, WASHINGTON, 2, BLACK, 79));

        routeMap.put(81, new Route(OKLAHOMA_CITY, SANTA_FE, 3, BLUE, -1));

        routeMap.put(82, new Route(PHOENIX, SANTA_FE, 3, WILD, -1));

        routeMap.put(83, new Route(PITTSBURGH, RALEIGH, 2, WILD, -1));
        routeMap.put(84, new Route(PITTSBURGH, ST_LOUIS, 5, GREEN, -1));
        routeMap.put(85, new Route(PITTSBURGH, TORONTO, 2, WILD, -1));
        routeMap.put(86, new Route(PITTSBURGH, WASHINGTON, 2, WILD, -1));

        routeMap.put(87, new Route(PORTLAND, SALT_LAKE_CITY, 6, BLUE, -1));
        routeMap.put(88, new Route(PORTLAND, SAN_FRANCISCO, 5, GREEN, 89));
        routeMap.put(89, new Route(PORTLAND, SAN_FRANCISCO, 5, PURPLE, 88));
        routeMap.put(90, new Route(PORTLAND, SEATTLE, 1, WILD, 91));
        routeMap.put(91, new Route(PORTLAND, SEATTLE, 1, WILD, 90));

        routeMap.put(92, new Route(RALEIGH, WASHINGTON, 2, WILD, 93));
        routeMap.put(93, new Route(RALEIGH, WASHINGTON, 2, WILD, 92));

        routeMap.put(94, new Route(SALT_LAKE_CITY, SAN_FRANCISCO, 5, ORANGE, 95));
        routeMap.put(95, new Route(SALT_LAKE_CITY, SAN_FRANCISCO, 5, WHITE, 94));

        routeMap.put(96, new Route(SAULT_ST_MARIE, TORONTO, 2, WILD, -1));
        routeMap.put(97, new Route(SAULT_ST_MARIE, WINNIPEG, 6, WILD, -1));

        routeMap.put(98, new Route(SEATTLE, VANCOUVER, 1, WILD, 99));
        routeMap.put(99, new Route(SEATTLE, VANCOUVER, 1, WILD, 98));
return routeMap;
    }

    private void setPointValue() {
        if (length == 1) {
            pointValue = 1;
        }
        else if (length == 2) {
            pointValue = 2;
        }
        else if (length == 3) {
            pointValue = 4;
        }
        else if (length == 4) {
            pointValue = 7;
        }
        else if (length == 5) {
            pointValue = 10;
        }
        else if (length == 6) {
            pointValue = 15;
        }
    }
    public void setSisterRouteClaimed() {
        sisterRouteClaimed = true;
    }

    void setClaimedColor(PlayerColor claimedColor) {
        this.claimedColor = claimedColor;
    }

    void setOwner(String owner) {
        this.owner = owner;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public int getLength() {
        return length;
    }

    public TrainCard getOriginalColor() {
        return originalColor;
    }

    public int getPointValue() {
        return pointValue;
    }

    public int getSisterRouteKey() {
        return sisterRouteKey;
    }

    public boolean isClaimed(){
        return claimed;
    }

    public void setSisterRouteKey(int sisterRouteKey) {
        this.sisterRouteKey = sisterRouteKey;
    }

    boolean sisterRouteIsClaimed(){
        return sisterRouteClaimed;
    }

    boolean isDoubleRoute() {
        return doubleRoute;
    }

    String getOwner() {
        return owner;
    }
}
