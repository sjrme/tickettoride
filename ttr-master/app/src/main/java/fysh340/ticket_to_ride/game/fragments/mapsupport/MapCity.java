package fysh340.ticket_to_ride.game.fragments.mapsupport;

import com.google.android.gms.maps.model.LatLng;

/**
 * Cities to display on the map
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-07-22
 */
public enum MapCity {
    ATLANTA(33.7490, -84.3880),
    BOSTON(42.3601, -71.0589),
    CALGARY(51.0486, -114.0708),
    CHARLESTON(32.7765, -79.9311),
    DALLAS(31.7767, -96.7970),
    CHICAGO(41.8781, -87.6298),
    DENVER(39.7392, -104.9903),
    DULUTH(46.7867, -92.1005),
    EL_PASO(31.7619, -106.4850),
    HELENA(46.5884, -112.0245),
    HOUSTON(29.7604, -95.3698),
    KANSAS_CITY(39.0997, -94.5786),
    LAS_VEGAS(36.1699, -115.1398),
    LITTLE_ROCK(34.7465, -92.2896),
    LOS_ANGELES(34.0522, -118.2437),
    MIAMI(25.7617, -80.1918),
    MONTREAL(45.5017, -73.5673),
    NASHVILLE(36.1627, -86.7816),
    NEW_ORLEANS(29.9511, -90.0715),
    NEW_YORK(40.7128, -74.0059),
    OKLAHOMA_CITY(35.4676, -97.5164),
    OMAHA(42.2524, -96.9980),
    PHOENIX(34.4484, -112.0740),
    PITTSBURGH(40.4406, -79.9959),
    PORTLAND(45.5231, -122.6765),
    RALEIGH(35.7796, -79.6382),
    SAINT_LOUIS(38.6270, -90.1994),
    SALT_LAKE_CITY(40.7608, -111.8910),
    SAN_FRANCISCO(37.7749, -122.4194),
    SANTA_FE(35.6870, -105.9378),
    SAULT_ST_MARIE(46.4953, -84.3453),
    SEATTLE(47.6062, -122.3321),
    TORONTO(43.6532, -79.3832),
    VANCOUVER(49.7827, -123.1207),
    WASHINGTON(37.9072, -76.0369),
    WINNIPEG(49.8951, -97.1384);

    private double latitude;
    private double longitude;

    MapCity(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        String[] parts = name().split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.charAt(0))
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }
        return sb.toString().trim();
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public void setLat(double latitude) {
        this.latitude = latitude;
    }

    public void setLng(double longitude) {
        this.longitude = longitude;
    }

}
