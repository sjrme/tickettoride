package fysh340.ticket_to_ride.game.fragments.mapsupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fysh340.ticket_to_ride.R;

/**
 * Class that the google maps for TTR by drawing all of the routes, the cities, and the names for
 * the city. It also provides functions to change color of routes when they are claimed.
 *
 * @author Shun Sambongi
 * @version 1.0
 * @invariant none (since all of the methods in this class are static, there are no class
 * invariants)
 * @since 2017-07-22
 */
public class MapHelper {

    /**
     * The width of the routes drawn on the screen
     */
    private static final int ROUTE_WIDTH = 30;

    /**
     * The gap between the segments of a route
     */
    private static final int ROUTE_GAP = 30000;

    /**
     * The maximum zoom level for the map
     */
    private static final float MAX_ZOOM = 6.5f;

    /**
     * The minimum zoom level for the map
     */
    private static final float MIN_ZOOM = 5.0f;

    /**
     * The radius for the circles drawn at the city locations
     */
    private static final int CITY_RADIUS = 75000;

    /**
     * The gap between double routes
     */
    private static final int DOUBLE_ROUTE_GAP = 35000;

    /**
     * The color of the cities
     */
    private static final int CITY_FILL = Color.GRAY;

    /**
     * The color of the stroke for the cities
     */
    private static final int CITY_STROKE = Color.BLACK;

    /**
     * Map that keeps a reference from each route to the segments that make up that route. Since
     * each route is actually drawn using multiple (1 - 6) polylines, this makes sure that the
     * segments can be referenced together (for example, when changing the color)
     */
    private static Map<MapRoute, Set<Polyline>> routePolyLineMap = new HashMap<>();

    private static Set<Polyline> myPolyLines = new HashSet<>();

    public static Set<Polyline> getMyPolyLines(){
        return myPolyLines;
    }

    // initializes the the map
    static {
        for (MapRoute route : MapRoute.values()) {
            routePolyLineMap.put(route, new HashSet<Polyline>());
        }
    }

    /**
     * Initializes the TTR map by drawing all of the cities and the routes to the map. In addition,
     * it also sets the location for the map to the US as well as restricting some of the user
     * gestures.
     *
     * @param context The Android context. Can just pass in an instance of the Activity hosting the
     *                map fragment
     * @param map     the GoogleMap object which will be initialized
     *
     * @pre context != null
     * @pre map != null
     * @pre map has to be "ready"; this means this method has to be called in or after the
     *      onMapReady() method in the OnMapReadyCallback for Google maps.
     * @pre map_style.json exists in the resources folder using the styling format defined at
     *      https://developers.google.com/maps/documentation/android-api/style-reference
     *
     * @post the map will show the cities and the lines for ticket to ride
     */
    public static void initMap(final Context context, final GoogleMap map) {

        // set the map style
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style));

        // move camera to USA
        LatLng usa = new LatLng(39.8283, -98.5795);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(usa, 4.6f));

        // gets the bounds for all of the cities in the game to restrict the scrollable area
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MapCity city : MapCity.values())
            builder.include(city.getLatLng());
        final LatLngBounds bounds = builder.build();

        // disable all of the settings besides zooming and scrolling
        UiSettings settings = map.getUiSettings();
        settings.setAllGesturesEnabled(false);
        settings.setMapToolbarEnabled(false);
        settings.setZoomGesturesEnabled(true);
        settings.setScrollGesturesEnabled(true);

        // restrict the area for zooming and scrolling
        map.setMaxZoomPreference(MAX_ZOOM);
        map.setMinZoomPreference(MIN_ZOOM);
        map.setLatLngBoundsForCameraTarget(bounds);

        // draw the cities to the map
        double radius = CITY_RADIUS;
        for (MapCity city : MapCity.values()) {
            map.addCircle(new CircleOptions()
                    .center(city.getLatLng())
                    .radius(radius)
                    .fillColor(CITY_FILL)
                    .strokeColor(CITY_STROKE))
                    .setZIndex(1);
            addText(context, map, city.getLatLng(), city.getName(), 30, 15);
        }

        // draw the routes to the map
        for (MapRoute route : MapRoute.values()) {
            drawMapRoute(map, route, null);
        }
    }

    /**
     * Draws a route on the map using polylines
     *
     * @param map   the map that will be drawn on
     * @param route the route to draw
     * @param color the color to draw the route
     *
     * @pre map != null
     * @pre route != null
     * @pre map has to be "ready"; this means this method has to be called in or after the
     *      onMapReady() method in the OnMapReadyCallback for Google maps.
     *
     * @post map will have a new route drawn on it
     * @post routePolylineMap will have a new entry in it with route as the key and a set of the
     *       polylines representing the segments in the route as the value
     */
    public static void drawMapRoute(GoogleMap map, MapRoute route, Integer color) {
        // if there already is a route there, remove all of the segments
        routePolyLineMap.put(route, new HashSet<Polyline>());

        // get the color of the route
        if (color == null) {
            color = route.getColor();
        }

        // get the cities at the ends of the route
        MapCity city1 = route.getCity1();
        MapCity city2 = route.getCity2();

        // get the lat and long for the cities
        LatLng currPos = city1.getLatLng();
        LatLng destPos = city2.getLatLng();

        // calculate the heading (direction, angle) between the points
        double heading = SphericalUtil.computeHeading(currPos, destPos);

        // if the route is a double route (there are two routes between the cities), adjust the
        // starting and ending positions
        if (route.getDir() != 0) {
            currPos = SphericalUtil.computeOffset(currPos, DOUBLE_ROUTE_GAP, heading + 90.0 * route.getDir());
            destPos = SphericalUtil.computeOffset(destPos, DOUBLE_ROUTE_GAP, heading + 90.0 * route.getDir());
        }

        // adjust the starting and ending positions so that we aren't starting right at the center
        // of the city but rather just outside
        currPos = SphericalUtil.computeOffset(currPos, CITY_RADIUS, heading);
        destPos = SphericalUtil.computeOffsetOrigin(destPos, CITY_RADIUS, heading);

        // calculate the distance between the start and destination
        double distance = SphericalUtil.computeDistanceBetween(currPos, destPos);

        // get the number of segments the path should be split up into
        int segments = route.getLength();

        // calculate the number of gaps there will be (this will always be 1 less than the number of
        // segments).
        int gaps = segments - 1;

        // adjust the distance but removing the distance that will be taken up by gaps
        distance -= ROUTE_GAP * gaps;

        // calculate the distance for each individual segment
        double segment = distance / segments;

        // calculate the heading from the current position to the destination, since the initial
        // start and destination positions have been adjusted (every time the positions are moved,
        // the heading needs to be recalculated because the earth is spherical).
        heading = SphericalUtil.computeHeading(currPos, destPos);

        // draw a polyline from the current position to the end of the next segment
        LatLng nextPos;
        for (int i = 0; i < segments; i++) {
            nextPos = SphericalUtil.computeOffset(currPos, segment, heading);
            heading = SphericalUtil.computeHeading(nextPos, destPos);
            Polyline line = map.addPolyline(new PolylineOptions()
                    .add(currPos, nextPos)
                    .color(color)
                    .width(ROUTE_WIDTH)
                    .clickable(true));
            line.setTag(route);
            currPos = SphericalUtil.computeOffset(nextPos, ROUTE_GAP, heading);
            heading = SphericalUtil.computeHeading(currPos, destPos);
            routePolyLineMap.get(route).add(line);
            myPolyLines.add(line);
        }
    }

    /**
     * Changes the color of a route. Use when a player claims a route.
     *
     * @param route the route to change the color of
     * @param color the color to change the route to
     *
     * @pre route != null
     * @pre color is a valid integer representation of a color.
     *      See https://developer.android.com/reference/android/graphics/Color.html
     * @pre routePolylineMap has a entry with route as a key
     *
     * @post the route on the map will be changed to the new color
     */
    public static void changeColor(MapRoute route, int color) {
        for (Polyline line : routePolyLineMap.get(route)) {
            line.setColor(color);
        }
    }

    /**
     * Adds text on the map. Use to add the city labels onto the map.
     *
     * @param context  Android context. Probably use Activity hosting the map.
     * @param map      the actual GoogleMap object to draw on
     * @param location the location the text will be drawn
     * @param text     the string that will drawn
     * @param padding  the padding for the text
     * @param fontSize the size of the text
     *
     * @pre context != null
     * @pre map != null
     * @pre location != null
     * @pre text != null
     * @pre padding > 0
     * @pre fontSize > 0
     * @pre map has to be "ready"; this means this method has to be called in or after the
     *      onMapReady() method in the OnMapReadyCallback for Google maps.
     *
     * @post the map will have a new marker placed at location with the text
     *
     * @return the Marker (the text) that was placed on the map
     */
    private static Marker addText(final Context context, final GoogleMap map,
                                  final LatLng location, final String text, final int padding,
                                  final int fontSize) {
        Marker marker = null;

        // check for null values
        if (context == null || map == null || location == null || text == null
                || fontSize <= 0) {
            return marker;
        }

        // create a text view
        final TextView textView = new TextView(context);
        textView.setClickable(false);
        textView.setFocusable(false);
        textView.setText(text);
        textView.setTextSize(fontSize);

        final Paint paintText = textView.getPaint();

        final Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, textView.length(), boundsText);
        paintText.setTextAlign(Paint.Align.CENTER);

        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2 * padding,
                boundsText.height() + 2 * padding, conf);

        final Canvas canvasText = new Canvas(bmpText);
        paintText.setColor(Color.WHITE);
        paintText.setShadowLayer(5, 5, 5, Color.BLACK);

        canvasText.drawText(text, canvasText.getWidth() / 2,
                canvasText.getHeight() - padding - boundsText.bottom, paintText);

        // create options to make the marker
        final MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
                .zIndex(2)
                .anchor(0.5f, 0.5f);

        // add the marker to the map
        marker = map.addMarker(markerOptions);

        return marker;
    }
}
