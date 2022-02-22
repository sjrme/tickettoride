package fysh340.ticket_to_ride.game.fragments.mapsupport;

import android.graphics.Color;

/**
 * Colors for routes on the map
 *
 * @author Shun Sambongi
 * @version 1.0
 * @since 2017-07-22
 */
public enum MapRouteColor {
    RED("#d14622"),
    ORANGE("#e28120"),
    YELLOW("#e5c03e"),
    GREEN("#92ae3d"),
    BLUE("#57819b"),
    PURPLE("#a9899e"),
    WHITE("#ebe9e2"),
    BLACK("#4e4b44"),
    GRAY("#9E9E9E");

    private String color;

    MapRouteColor(String color) {
        this.color = color;
    }

    public int getColor() {
        return Color.parseColor(color);
    }
}
