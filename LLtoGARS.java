/* -----------------------------------------------------------------------------
 *       UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED
 *                 (C) Copyright 2013, USAREUR G3 MCSD
 *                         ALL RIGHTS RESERVED
 *                 THIS NOTICE DOES NOT IMPLY PUBLICATION
 * -------------------------------------------------------------------------- */
package gars;

/**
 *
 * @author marty
 */
public class LLtoGARS {

    /**
     * Get the GARS 3 letter longitude band descriptor for a given longitude
     * (-180.0..180.0)
     * <p>
     * The longitude band are the first three characters of the GARS code.<br>
     * The bands are numbered from 001 through 720, starting at -180 through 180
     * and depict 30-minute longitudinal bands.<br>
     * <p>
     * @param longitude as double in degrees decimal
     * @return String representation of the 3 character longitudinal band
     */
    public static String getLongitudeBand(double longitude) {
        Double longBand = longitude + 180;
        // Normalize to 0.0 <= longBand < 360
        while (longBand < 0) {
            longBand = longBand + 360;
        }
        while (longBand > 360) {
            longBand = longBand - 360;
        }
        longBand = Math.floor(longBand * 2.0);
        Integer intLongBand = longBand.intValue() + 1; // Start at 001, not 000
        String strLongBand = intLongBand.toString();
        // Left pad the string with 0's so X becomes 00X
        while (strLongBand.length() < 3) {
            strLongBand = "0" + strLongBand;
        }
        return strLongBand;
    }//getLongitudeBand

    /**
     * Get the GARS 2 letter latitude band descriptor for a given latitude
     * (-90..90)
     * <p>
     * The fourth and fifth characters of a GARS code represent 30-minute
     * latitudinal bands and are numbered from AA through QZ, starting at -90
     * through 90.
     * <p>
     * @param latitude as double in degrees decimal
     * @return String representation of the 2 character latitudinal band
     */
    public static String getLatitudeBand(double latitude) {
        char[] letterArray = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
        Double offset = latitude + 90;
        // Normalize offset to 0< offset <90
        while (offset < 0) {
            offset = offset + 180;
        }
        while (offset > 180) {
            offset = offset - 180;
        }
        offset = Math.floor(offset * 2.0);
        Double firstOffest = offset / letterArray.length;
        Double secondOffest = offset % letterArray.length;
        StringBuilder sb = new StringBuilder();
        sb.append(letterArray[firstOffest.intValue()]).append(letterArray[secondOffest.intValue()]);
        return sb.toString();
    }//getLatitudeBand

    /**
     * Returns the GARS 15 minute quadrant cell identifier for a latitude,
     * longitude pair.This identifier is the 6th character of the GARS
     * code.<br>Each 30-minute cell is divided into four 15-minute by 15-minute
     * quadrants. The quadrants are numbered sequentially, from west to east,
     * starting with the northernmost band. Specifically, the northwest quadrant
     * is “1”; the northeast quadrant is “2”; the southwest quadrant is “3”; the
     * southeast quadrant is “4”.
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @return String of the quadrant identifier (1..4), returns 0 if lat/lon is
     * invalid
     */
    public static String getQudarant(double latitude, double longitude) {
        Double latBand = (Math.floor((latitude + 90.0) * 4.0) % 2.0);
        Double longBand = (Math.floor((longitude + 180.0) * 4.0) % 2.0);
        // Return "0" if error occurs
        if (latBand < 0 || latBand > 1) {
            return "0";
        }
        if (longBand < 0 || longBand > 1) {
            return "0";
        }
        // Otherwise return the quadrant
        if (latBand == 0.0 && longBand == 0.0) {
            return "3";
        } else if (latBand == 1.0 && longBand == 0.0) {
            return "1";
        } else if (latBand == 1.0 && longBand == 1.0) {
            return "2";
        } else if (latBand == 0.0 && longBand == 1.0) {
            return "4";
        }
        return "0";
    }//getQudarant

    /**
     * Returns the GARS 5 minute keypad cell identifier for a latitude,
     * longitude pair. This is the seventh character of the GARS code.<br>Each
     * 15-minute quadrant is divided into nine 5-minute by 5-minute areas. The
     * areas are numbered sequentially, from west to east, starting with the
     * northernmost band. The graphical representation of a 15-minute quadrant
     * with numbered 5-minute by 5-minute areas resembles a telephone keypad.
     * <p>
     * This code was ported from the Geotrans 3.5 CCP implementation:
     * http://earth-info.nga.mil/GandG/geotrans/
     * <p>
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @return String of the 5x5 identifier (1..9), returns 0 if lat/lon is
     * invalid
     */
    public static String getKeyPad(double latitude, double longitude) {
        // Check for valid lat/lon range
        if (latitude < -90 || latitude > 90) {
            return "0";
        }
        if (longitude < -180 || longitude > 180) {
            return "0";
        }

        /* Convert longitude and latitude from degrees to minutes */
        /* longitude assumed in -180 <= long < +180 range */
        double long_minutes = (longitude + 180) * 60.0;
        double lat_minutes = (latitude + 90) * 60.0;
        /* now we have a positive number of minutes */

        /* Find 30-min cell indices 0-719 and 0-359 */
        long horiz_index_30 = (long) (long_minutes / 30.0);
        long vert_index_30 = (long) (lat_minutes / 30.0);

        /* Compute remainders 0 <= x < 30.0 */
        double long_remainder = long_minutes - (horiz_index_30) * 30.0;
        double lat_remainder = lat_minutes - (vert_index_30) * 30.0;

        /* Find 15-min cell indices 0 or 1 */
        long horiz_index_15 = (long) (long_remainder / 15.0);
        long vert_index_15 = (long) (lat_remainder / 15.0);

        /* Compute remainders 0 <= x < 15.0 */
        long_remainder = long_remainder - (horiz_index_15) * 15.0;
        lat_remainder = lat_remainder - (vert_index_15) * 15.0;

        /* Find 5-min cell indices 0, 1, or 2 */
        long horiz_index_5 = (long) (long_remainder / 5.0);
        long vert_index_5 = (long) (lat_remainder / 5.0);

        String[][] _5_minute_array = {{"7", "4", "1"}, {"8", "5", "2"}, {"9", "6", "3"}};

        String keypad = _5_minute_array[(int) horiz_index_5][(int) vert_index_5];

        return keypad;

    }//getKeyPad

    /**
     * Returns a GARS coordinate string for a point (latitude, longitude)
     * <p>
     * The precision parameter indicates if a 30x30min, 15x15min, or 5x5min GARS
     * code is to be returned.
     * <p>
     * Valid precision values are ["30","15","5"]
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     * @param precision as String representing the precision of the GARS string
     *
     * @return String representation of the GARS identifier, returns 0 if
     * lat/lon is invalid
     */
    public static String getGARS(double latitude, double longitude, String precision) {
        /* North pole is an exception, read over and down */
        if (latitude == 90.0) {
            latitude = 89.99999999999;
        }
        // Check for valid lat/lon range
        if (latitude < -90 || latitude > 90) {
            return "0";
        }
        if (longitude < -180 || longitude > 180) {
            return "0";
        }
        // Get the longitude band ==============================================
        Double longBand = longitude + 180;
        // Normalize to 0.0 <= longBand < 360
        while (longBand < 0) {
            longBand = longBand + 360;
        }
        while (longBand > 360) {
            longBand = longBand - 360;
        }
        longBand = Math.floor(longBand * 2.0);
        Integer intLongBand = longBand.intValue() + 1; // Start at 001, not 000
        String strLongBand = intLongBand.toString();
        // Left pad the string with 0's so X becomes 00X
        while (strLongBand.length() < 3) {
            strLongBand = "0" + strLongBand;
        }

        // Get the latitude band ===============================================
        char[] letterArray = "ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
        Double offset = latitude + 90;
        // Normalize offset to 0< offset <90
        while (offset < 0) {
            offset = offset + 180;
        }
        while (offset > 180) {
            offset = offset - 180;
        }
        offset = Math.floor(offset * 2.0);
        Double firstOffest = offset / letterArray.length;
        Double secondOffest = offset % letterArray.length;
        StringBuilder sb = new StringBuilder();
        String strLatBand = sb.append(letterArray[firstOffest.intValue()]).append(letterArray[secondOffest.intValue()]).toString();

        // Id the precision is 30x30min then return the longitudinal and latitudinal bands
        if (precision.contains("30")) {
            return strLongBand + strLatBand;
        }

        // Get the quadrant ====================================================
        Double latBand = (Math.floor((latitude + 90.0) * 4.0) % 2.0);
        longBand = (Math.floor((longitude + 180.0) * 4.0) % 2.0);
        String quadrant = "0";
        // return "0" if error occurs
        if (latBand < 0 || latBand > 1) {
            return "0";
        }
        if (longBand < 0 || longBand > 1) {
            return "0";
        }
        // Otherwise get the quadrant
        if (latBand == 0.0 && longBand == 0.0) {
            quadrant = "3";
        } else if (latBand == 1.0 && longBand == 0.0) {
            quadrant = "1";
        } else if (latBand == 1.0 && longBand == 1.0) {
            quadrant = "2";
        } else if (latBand == 0.0 && longBand == 1.0) {
            quadrant = "4";
        }

        // Id the precision is 15x15min then return the longitudinal and latitudinal bands
        // plus the quadrant
        if (precision.contains("15")) {
            return strLongBand + strLatBand + quadrant;
        }

        // Get the keypad ======================================================
        /* Convert longitude and latitude from degrees to minutes */
        /* longitude assumed in -180 <= long < +180 range */
        double long_minutes = (longitude + 180) * 60.0;
        double lat_minutes = (latitude + 90) * 60.0;
        /* now we have a positive number of minutes */

        /* Find 30-min cell indices 0-719 and 0-359 */
        long horiz_index_30 = (long) (long_minutes / 30.0);
        long vert_index_30 = (long) (lat_minutes / 30.0);

        /* Compute remainders 0 <= x < 30.0 */
        double long_remainder = long_minutes - (horiz_index_30) * 30.0;
        double lat_remainder = lat_minutes - (vert_index_30) * 30.0;

        /* Find 15-min cell indices 0 or 1 */
        long horiz_index_15 = (long) (long_remainder / 15.0);
        long vert_index_15 = (long) (lat_remainder / 15.0);

        /* Compute remainders 0 <= x < 15.0 */
        long_remainder = long_remainder - (horiz_index_15) * 15.0;
        lat_remainder = lat_remainder - (vert_index_15) * 15.0;

        /* Find 5-min cell indices 0, 1, or 2 */
        long horiz_index_5 = (long) (long_remainder / 5.0);
        long vert_index_5 = (long) (lat_remainder / 5.0);

        String[][] _5_minute_array = {{"7", "4", "1"}, {"8", "5", "2"}, {"9", "6", "3"}};

        String keypad = _5_minute_array[(int) horiz_index_5][(int) vert_index_5];

        return strLongBand + strLatBand + quadrant + keypad;
    }//getGARS

    /**
     * Returns a GARS 5x5 minute coordinate string for a point (latitude,
     * longitude)
     * <p>
     *
     * @param latitude as double in degrees decimal
     * @param longitude as double in degrees decimal
     *
     * @return String representation of the GARS identifier, returns 0 if
     * lat/lon is invalid
     */
    public static String getGARS(double latitude, double longitude) {
        return getGARS(latitude, longitude, "5");
    }//getGARS
}
