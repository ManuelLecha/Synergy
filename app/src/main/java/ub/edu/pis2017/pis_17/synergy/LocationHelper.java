package ub.edu.pis2017.pis_17.synergy;

import android.location.Location;

public class LocationHelper {
    private static final int PROVIDER = 0, LONGITUDE = 1, LATITUDE = 2;

    private LocationHelper() {}

    /**
     * Returns a string with the distance betwenn locations formated properly.
     * @param a
     * @param b
     * @return
     */
    public static String locationDistanceFormat(Location a, Location b) {
        Float dist = a.distanceTo(b) / 1000; // distance in metres
        if (dist < 1000) {
            return String.format("%.0f m",dist);
        } else  {
            return String.format("%.2f km",dist/1000);
        }
    }

    /**
     * Dumps location into String
     * @param loc
     * @return
     */
    public static String dump(Location loc) {
        /**
         * Location descriptor:
         * PROVIDER:LONGITUDE:LATITUDE
         */
        return loc.getProvider()+":"+loc.getLongitude()+":"+loc.getLatitude();
    }

    /**
     * Reads location from string
     * @param s
     * @return
     */
    public static Location read(String s) {
        String[] data = s.split(":");
        Location loc = new Location(data[PROVIDER]);
        loc.setLatitude(Double.parseDouble(data[LATITUDE]));
        loc.setLongitude(Double.parseDouble(data[LONGITUDE]));
        return loc;
    }
}
