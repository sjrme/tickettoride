package fysh340.ticket_to_ride.game.fragments.mapsupport;


import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.ATLANTA;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.BOSTON;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.CALGARY;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.CHARLESTON;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.CHICAGO;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.DALLAS;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.DENVER;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.DULUTH;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.EL_PASO;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.HELENA;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.HOUSTON;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.KANSAS_CITY;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.LAS_VEGAS;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.LITTLE_ROCK;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.LOS_ANGELES;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.MIAMI;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.MONTREAL;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.NASHVILLE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.NEW_ORLEANS;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.NEW_YORK;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.OKLAHOMA_CITY;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.OMAHA;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.PHOENIX;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.PITTSBURGH;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.PORTLAND;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.RALEIGH;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SAINT_LOUIS;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SALT_LAKE_CITY;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SANTA_FE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SAN_FRANCISCO;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SAULT_ST_MARIE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.SEATTLE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.TORONTO;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.VANCOUVER;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.WASHINGTON;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapCity.WINNIPEG;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.BLACK;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.BLUE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.GRAY;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.GREEN;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.ORANGE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.PURPLE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.RED;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.WHITE;
import static fysh340.ticket_to_ride.game.fragments.mapsupport.MapRouteColor.YELLOW;

/**
 * Routes to show on the map
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-07-22
 */
public enum MapRoute {
    ATLANTA_CHARLESTON(0, ATLANTA, CHARLESTON, 2, GRAY),
    ATLANTA_MIAMI(1, ATLANTA, MIAMI, 5, BLUE),
    ATLANTA_NASHVILLE(2, ATLANTA, NASHVILLE, 1, GRAY),
    ATLANTA_NEW_ORLEANS_1(3, ATLANTA, NEW_ORLEANS, 4, YELLOW, 1),
    ATLANTA_NEW_ORLEANS_2(4, ATLANTA, NEW_ORLEANS, 4, ORANGE, -1),
    ATLANTA_RALEIGH_1(5, ATLANTA, RALEIGH, 2, GRAY, 1),
    ATLANTA_RALEIGH_2(6, ATLANTA, RALEIGH, 2, GRAY, -1),

    BOSTON_MONTREAL_1(7, BOSTON, MONTREAL, 2, GRAY, 1),
    BOSTON_MONTREAL_2(8, BOSTON, MONTREAL, 2, GRAY, -1),
    BOSTON_NEW_YORK_1(9, BOSTON, NEW_YORK, 2, RED, 1),
    BOSTON_NEW_YORK_2(10, BOSTON, NEW_YORK, 2, YELLOW, -1),

    CALGARY_HELENA(11, CALGARY, HELENA, 4, GRAY),
    CALGARY_SEATTLE(12, CALGARY, SEATTLE, 4, GRAY),
    CALGARY_VANCOUVER(13, CALGARY, VANCOUVER, 3, GRAY),
    CALGARY_WINNIPEG(14, CALGARY, WINNIPEG, 6, WHITE),

    CHARLESTON_MIAMI(15, CHARLESTON, MIAMI, 4, PURPLE),
    CHARLESTON_RALEIGH(16, CHARLESTON, RALEIGH, 2, GRAY),

    CHICAGO_DULUTH(17, CHICAGO, DULUTH, 3, RED),
    CHICAGO_SAINT_LOUIS_1(18, CHICAGO, SAINT_LOUIS, 2, GREEN, 1),
    CHICAGO_SAINT_LOUIS_2(19, CHICAGO, SAINT_LOUIS, 2, WHITE, -1),
    CHICAGO_OMAHA(20, CHICAGO, OMAHA, 4, BLUE),
    CHICAGO_PITTSBURGH_1(21, CHICAGO, PITTSBURGH, 3, ORANGE, 1),
    CHICAGO_PITTSBURGH_2(22, CHICAGO, PITTSBURGH, 3, BLACK, -1),
    CHICAGO_TORONTO(23, CHICAGO, TORONTO, 4, WHITE),

    DALLAS_EL_PASO(24, DALLAS, EL_PASO, 4, RED),
    DALLAS_HOUSTON_1(25, DALLAS, HOUSTON, 1, GRAY, 1),
    DALLAS_HOUSTON_2(26, DALLAS, HOUSTON, 1, GRAY, -1),
    DALLAS_LITTLE_ROCK(27, DALLAS, LITTLE_ROCK, 2, GRAY),
    DALLAS_OKLAHOMA_CITY_1(28, DALLAS, OKLAHOMA_CITY, 2, GRAY, 1),
    DALLAS_OKLAHOMA_CITY_2(29, DALLAS, OKLAHOMA_CITY, 2, GRAY, -1),

    DENVER_HELENA(30, DENVER, HELENA, 4, GREEN),
    DENVER_KANSAS_CITY_1(31, DENVER, KANSAS_CITY, 4, BLACK, 1),
    DENVER_KANSAS_CITY_2(32, DENVER, KANSAS_CITY, 4, ORANGE, -1),
    DENVER_OKLAHOMA_CITY(33, DENVER, OKLAHOMA_CITY, 4, RED),
    DENVER_OMAHA(34, DENVER, OMAHA, 4, PURPLE),
    DENVER_PHOENIX(35, DENVER, PHOENIX, 5, WHITE),
    DENVER_SALT_LAKE_CITY_1(36, DENVER, SALT_LAKE_CITY, 3, RED, 1),
    DENVER_SALT_LAKE_CITY_2(37, DENVER, SALT_LAKE_CITY, 3, YELLOW, -1),
    DENVER_SANTA_FE(38, DENVER, SANTA_FE, 2, GRAY),

    DULUTH_HELENA(39, DULUTH, HELENA, 6, ORANGE),
    DULUTH_OMAHA_1(40, DULUTH, OMAHA, 2, GRAY, 1),
    DULUTH_OMAHA_2(41, DULUTH, OMAHA, 2, GRAY, -1),
    DULUTH_SAULT_ST_MARIE(42, DULUTH, SAULT_ST_MARIE, 3, GRAY),
    DULUTH_TORONTO(43, DULUTH, TORONTO, 6, PURPLE),
    DULUTH_WINNIPEG(44, DULUTH, WINNIPEG, 4, BLACK),

    EL_PASO_HOUSTON(45, EL_PASO, HOUSTON, 6, GREEN),
    EL_PASO_LOS_ANGELES(46, EL_PASO, LOS_ANGELES, 6, BLACK),
    EL_PASO_OKLAHOMA_CITY(47, EL_PASO, OKLAHOMA_CITY, 5, YELLOW),
    EL_PASO_PHOENIX(48, EL_PASO, PHOENIX, 3, GRAY),
    EL_PASO_SANTA_FE(49, EL_PASO, SANTA_FE, 2, GRAY),

    HELENA_OMAHA(50, HELENA, OMAHA, 5, RED),
    HELENA_SALT_LAKE_CITY(51, HELENA, SALT_LAKE_CITY, 3, PURPLE),
    HELENA_SEATTLE(52, HELENA, SEATTLE, 6, YELLOW),
    HELENA_WINNIPEG(53, HELENA, WINNIPEG, 4, BLUE),

    HOUSTON_NEW_ORLEANS(54, HOUSTON, NEW_ORLEANS, 2, GRAY),

    KANSAS_CITY_OKLAHOMA_CITY_1(55, KANSAS_CITY, OKLAHOMA_CITY, 2, GRAY, 1),
    KANSAS_CITY_OKLAHOMA_CITY_2(56, KANSAS_CITY, OKLAHOMA_CITY, 2, GRAY, -1),
    KANSAS_CITY_OMAHA_1(57, KANSAS_CITY, OMAHA, 1, GRAY, 1),
    KANSAS_CITY_OMAHA_2(58, KANSAS_CITY, OMAHA, 1, GRAY, -1),
    KANSAS_CITY_SAINT_LOUIS_1(59, KANSAS_CITY, SAINT_LOUIS, 2, BLUE, 1),
    KANSAS_CITY_SAINT_LOUIS_2(60, KANSAS_CITY, SAINT_LOUIS, 2, PURPLE, -1),

    LAS_VEGAS_LOS_ANGELES(61, LAS_VEGAS, LOS_ANGELES, 2, GRAY),
    LAS_VEGAS_SALT_LAKE_CITY(62, LAS_VEGAS, SALT_LAKE_CITY, 3, ORANGE),

    LITTLE_ROCK_NASHVILLE(63, LITTLE_ROCK, NASHVILLE, 3, WHITE),
    LITTLE_ROCK_NEW_ORLEANS(64, LITTLE_ROCK, NEW_ORLEANS, 3, GREEN),
    LITTLE_ROCK_OKLAHOMA_CITY(65, LITTLE_ROCK, OKLAHOMA_CITY, 2, GRAY),
    LITTLE_ROCK_SAINT_LOUIS(66, LITTLE_ROCK, SAINT_LOUIS, 2, GRAY),

    LOS_ANGELES_PHOENIX(67, LOS_ANGELES, PHOENIX, 3, GRAY),
    LOS_ANGELES_SAN_FRANCISCO_1(68, LOS_ANGELES, SAN_FRANCISCO, 3, PURPLE, 1),
    LOS_ANGELES_SAN_FRANCISCO_2(69, LOS_ANGELES, SAN_FRANCISCO, 3, YELLOW, -1),

    MIAMI_NEW_ORLEANS(70, MIAMI, NEW_ORLEANS, 6, RED),

    MONTREAL_NEW_YORK(71, MONTREAL, NEW_YORK, 3, BLUE),
    MONTREAL_SAULT_ST_MARIE(72, MONTREAL, SAULT_ST_MARIE, 5, BLACK),
    MONTREAL_TORONTO(73, MONTREAL, TORONTO, 3, GRAY),

    NASHVILLE_PITTSBURGH(74, NASHVILLE, PITTSBURGH, 4, YELLOW),
    NASHVILLE_RALEIGH(75, NASHVILLE, RALEIGH, 3, BLACK),
    NASHVILLE_SAINT_LOUIS(76, NASHVILLE, SAINT_LOUIS, 2, GRAY),

    NEW_YORK_PITTSBURGH_1(77, NEW_YORK, PITTSBURGH, 2, WHITE, 1),
    NEW_YORK_PITTSBURGH_2(78, NEW_YORK, PITTSBURGH, 2, GREEN, -1),
    NEW_YORK_WASHINGTON_1(79, NEW_YORK, WASHINGTON, 2, ORANGE, 1),
    NEW_YORK_WASHINGTON_2(80, NEW_YORK, WASHINGTON, 2, BLACK, -1),

    OKLAHOMA_CITY_SANTA_FE(81, OKLAHOMA_CITY, SANTA_FE, 3, BLUE),

    PHOENIX_SANTA_FE(82, PHOENIX, SANTA_FE, 3, GRAY),

    PITTSBURGH_RALEIGH(83, PITTSBURGH, RALEIGH, 2, GRAY),
    PITTSBURGH_SAINT_LOUIS(84, PITTSBURGH, SAINT_LOUIS, 5, GREEN),
    PITTSBURGH_TORONTO(85, PITTSBURGH, TORONTO, 2, GRAY),
    PITTSBURGH_WASHINGTON(86, PITTSBURGH, WASHINGTON, 2, GRAY),

    PORTLAND_SALT_LAKE_CITY(87, PORTLAND, SALT_LAKE_CITY, 6, BLUE),
    PORTLAND_SAN_FRANCISCO_1(88, PORTLAND, SAN_FRANCISCO, 5, GREEN, 1),
    PORTLAND_SAN_FRANCISCO_2(89, PORTLAND, SAN_FRANCISCO, 5, PURPLE, -1),
    PORTLAND_SEATTLE_1(90, PORTLAND, SEATTLE, 1, GRAY, 1),
    PORTLAND_SEATTLE_2(91, PORTLAND, SEATTLE, 1, GRAY, -1),

    RALEIGH_WASHINGTON_1(92, RALEIGH, WASHINGTON, 2, GRAY, 1),
    RALEIGH_WASHINGTON_2(93, RALEIGH, WASHINGTON, 2, GRAY, -1),

    SALT_LAKE_CITY_SAN_FRANCISCO_1(94, SALT_LAKE_CITY, SAN_FRANCISCO, 5, ORANGE, 1),
    SALT_LAKE_CITY_SAN_FRANCISCO_2(95, SALT_LAKE_CITY, SAN_FRANCISCO, 5, WHITE, -1),

    SAULT_ST_MARIE_TORONTO(96, SAULT_ST_MARIE, TORONTO, 2, GRAY),
    SAULT_ST_MARIE_WINNIPEG(97, SAULT_ST_MARIE, WINNIPEG, 6, GRAY),

    SEATTLE_VANCOUVER_1(98, SEATTLE, VANCOUVER, 1, GRAY, 1),
    SEATTLE_VANCOUVER_2(99, SEATTLE, VANCOUVER, 1, GRAY, -1);

    private int key;
    private MapCity city1;
    private MapCity city2;
    private int length;
    private MapRouteColor color;
    private int dir;

    MapRoute(int key, MapCity city1, MapCity city2, int length, MapRouteColor color) {
        this.key = key;
        this.city1 = city1;
        this.city2 = city2;
        this.length = length;
        this.color = color;
    }

    MapRoute(int key, MapCity city1, MapCity city2, int length, MapRouteColor color, int dir) {
        this.key = key;
        this.city1 = city1;
        this.city2 = city2;
        this.length = length;
        this.color = color;
        this.dir = dir;
    }

    public MapCity getCity1() {
        return city1;
    }

    public MapCity getCity2() {
        return city2;
    }

    public int getLength() {
        return length;
    }

    public int getColor() {
        return color.getColor();
    }

    public int getDir() {
        return dir;
    }

    public int getKey() {
        return key;
    }

    public static MapRoute getRoute(int key) {
        for (MapRoute route : values()){
            if (key == route.getKey()) return route;
        }
        return null;
    }
}
