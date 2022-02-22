package model;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static model.City.ATLANTA;
import static model.City.BOSTON;
import static model.City.CALGARY;
import static model.City.CHICAGO;
import static model.City.DALLAS;
import static model.City.DENVER;
import static model.City.DULUTH;
import static model.City.EL_PASO;
import static model.City.HELENA;
import static model.City.HOUSTON;
import static model.City.KANSAS_CITY;
import static model.City.LITTLE_ROCK;
import static model.City.LOS_ANGELES;
import static model.City.MIAMI;
import static model.City.MONTREAL;
import static model.City.NASHVILLE;
import static model.City.NEW_ORLEANS;
import static model.City.NEW_YORK;
import static model.City.OKLAHOMA_CITY;
import static model.City.PHOENIX;
import static model.City.PITTSBURGH;
import static model.City.PORTLAND;
import static model.City.SALT_LAKE_CITY;
import static model.City.SANTA_FE;
import static model.City.SAN_FRANCISCO;
import static model.City.SAULT_ST_MARIE;
import static model.City.SEATTLE;
import static model.City.TORONTO;
import static model.City.VANCOUVER;
import static model.City.WINNIPEG;

public class DestCard implements Serializable{

    private City startCity;
    private City endCity;
    private int pointValue;
    private int mapValue;

    public DestCard(int mapValue, City startCity, City endCity, int pointValue){
        this.mapValue = mapValue;
        this.startCity = startCity;
        this.endCity = endCity;
        this.pointValue = pointValue;
    }

    public static DestCard getDestCardByID(int cardID) {
        if(destCardMap.containsKey(cardID)) {
            return destCardMap.get(cardID);
        } else {
            return null;
        }
    }

    static int getDestCardKey(DestCard comparedDestCard) {

        Set<DestCard> allDestCards = new HashSet<>(destCardMap.values());
        for(DestCard destCard : allDestCards) {
            if (destCard.equals(comparedDestCard)){
                return destCard.getMapValue();
            }
        }
        return -1;
    }

    private static Map<Integer, DestCard> destCardMap = createDestCardMap();

    public static Map<Integer, DestCard> getDestCardMap(){
        return destCardMap;
    }

    private static Map<Integer, DestCard> createDestCardMap() {

        Map<Integer, DestCard> destCardMap = new HashMap<>();
        destCardMap.put(0, new DestCard(0, BOSTON, MIAMI, 12));
        destCardMap.put(1, new DestCard(1, CALGARY, PHOENIX, 13));
        destCardMap.put(2, new DestCard(2, CALGARY, SALT_LAKE_CITY, 7));
        destCardMap.put(3, new DestCard(3, CHICAGO, NEW_ORLEANS, 7));
        destCardMap.put(4, new DestCard(4, CHICAGO, SANTA_FE, 9) );
        destCardMap.put(5, new DestCard(5, DALLAS, NEW_YORK, 11));
        destCardMap.put(6, new DestCard(6, DENVER, EL_PASO, 4));
        destCardMap.put(7, new DestCard(7, DENVER, PITTSBURGH, 11));
        destCardMap.put(8, new DestCard(8, DULUTH, EL_PASO, 10));
        destCardMap.put(9, new DestCard(9, DULUTH, HOUSTON, 8));
        destCardMap.put(10, new DestCard(10, HELENA, LOS_ANGELES, 8));
        destCardMap.put(11, new DestCard(11, KANSAS_CITY, HOUSTON, 5));
        destCardMap.put(12, new DestCard(12, LOS_ANGELES, CHICAGO, 16));
        destCardMap.put(13, new DestCard(13, LOS_ANGELES, MIAMI, 20));
        destCardMap.put(14, new DestCard(14, LOS_ANGELES, NEW_YORK, 21));
        destCardMap.put(15, new DestCard(15, MONTREAL, ATLANTA, 9));
        destCardMap.put(16, new DestCard(16, MONTREAL, NEW_ORLEANS, 13));
        destCardMap.put(17, new DestCard(17, NEW_YORK, ATLANTA, 6));
        destCardMap.put(18, new DestCard(18, PORTLAND, NASHVILLE, 17));
        destCardMap.put(19, new DestCard(19, PORTLAND, PHOENIX, 11));
        destCardMap.put(20, new DestCard(20, SAN_FRANCISCO, ATLANTA, 17));
        destCardMap.put(21, new DestCard(21, SAULT_ST_MARIE, NASHVILLE, 8));
        destCardMap.put(22, new DestCard(22, SAULT_ST_MARIE, OKLAHOMA_CITY, 9));
        destCardMap.put(23, new DestCard(23, SEATTLE, LOS_ANGELES, 9));
        destCardMap.put(24, new DestCard(24, SEATTLE, NEW_YORK, 22));
        destCardMap.put(25, new DestCard(25, TORONTO, MIAMI, 10));
        destCardMap.put(26, new DestCard(26, VANCOUVER, MONTREAL, 20));
        destCardMap.put(27, new DestCard(27, VANCOUVER, SANTA_FE, 13));
        destCardMap.put(28, new DestCard(28, WINNIPEG, HOUSTON, 12));
        destCardMap.put(29, new DestCard(29, WINNIPEG, LITTLE_ROCK, 11));
        return destCardMap;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        DestCard comparedCard = (DestCard)obj;
        if (this.startCity == comparedCard.startCity){
            if (this.endCity == comparedCard.endCity) {
                return true;
            }
        }
        return false;
    }
    @Override
    public String toString()
    {
        return startCity+" to "+endCity;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public int getPointValue() {
        return pointValue;
    }

    public int getMapValue() {
        return mapValue;
    }

}

