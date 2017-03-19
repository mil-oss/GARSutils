/* -----------------------------------------------------------------------------
 *       UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED UNCLASSIFIED
 *                 (C) Copyright 2013, USAREUR G3 MCSD
 *                         ALL RIGHTS RESERVED
 *                 THIS NOTICE DOES NOT IMPLY PUBLICATION
 * -------------------------------------------------------------------------- */
package gars;

import static java.lang.Character.isDigit;

/**
 *
 * @author marty
 */
public class GARStoLL {

    /**
     * Convert a GARS string to KML STRING representation of the center
     * coordinates.
     * <p>
     * If the GARS string is invalid then an empty string is returned. <br>
     * If the conversion was successful the center point of the GARS tile is
     * returned in the format longitude,latitude. The coordinates are in decimal
     * degrees separated by a comma.<br>
     * Note: KML simply reverses the order of the coordinates and returns
     * longitude first.
     * <p>
     * A typical result string looks like the following:<br>
     * <i>123.45678,12.34567</i>
     *
     * @param GARS String
     * @return String
     */
    public static String getCenterKMLCoords(String GARS) {
        double[] ll = getCenterCoordsArray(GARS);
        if (ll.length != 2) {
            return "";
        }
        StringBuilder latlon = new StringBuilder();
        latlon.append(ll[1]).append(",").append(ll[0]);

        return latlon.toString();
    }//getCenterKmlCoords

    /**
     * Convert a GARS string to JSON representation of the center coordinates.
     * <p>
     * The JSON is will always return a status key with either "error" or
     * "success". <br>
     * If the conversion was successful then latitude and longitude keys are
     * returned with decimal degrees values for the center point of the GARS
     * tile.
     * <p>
     * A typical JSON string looks like the following:<br>
     * <i>{"status":"success","latitude":12.34567,"longitude":123.45678}</i>
     *
     * @param GARS String
     * @return JSON String
     */
    public static String getCenterJSONCoords(String GARS) {
        String latlon = getCenterCoords(GARS);
        if (latlon.toLowerCase().contains("error")) {
            return "{\"status\":\"error\"}";
        }
        String[] ll = latlon.split(",");
        StringBuilder json = new StringBuilder();
        json.append("{\"status\":\"success\",\"latitude\":").append(ll[0]).append(",\"longitude\":").append(ll[1]).append("}");
        return json.toString();

    }//getCenterJSONCoords    

    /**
     * Convert a GARS string to STRING representation of the center coordinates.
     * <p>
     * If the GARS string is invalid then the string 'error' is returned. <br>
     * If the conversion was successful the center point of the GARS tile is
     * returned in the format latitude,longitude. Latitude and longitude are in
     * decimal degrees separated by a comma.
     * <p>
     * A typical result string looks like the following:<br>
     * <i>12.34567,123.45678</i>
     *
     * @param GARS String
     * @return String
     */
    public static String getCenterCoords(String GARS) {
        double[] ll = getCenterCoordsArray(GARS);
        if (ll.length != 2) {
            return "error";
        }
        StringBuilder latlon = new StringBuilder();
        latlon.append(ll[0]).append(",").append(ll[1]);

        return latlon.toString();

    }//getCenterCoords    

    /**
     * Convert a GARS string to double array containing latitude and
     * longitude.<br> This code is a port of the NGA's geotrans cpp routines
     * from the GEOTRANS 3.5 SDK: http://earth-info.nga.mil/GandG/geotrans/
     * <p>
     * If the GARS string is invalid then the returned array has a length of 1
     * and the first and only value will be -360<br>
     * If the conversion was successful the center point of the GARS tile is
     * returned as decimal degrees. The first value (index 0) holds latitude,
     * the second value is longitude.<br>
     * Check the length of the array for error checking, the length of a
     * successful transformation will equal 2.
     * <p>
     * @param GARS String
     * @return double[]
     */
    public static double[] getCenterCoordsArray(String GARS) {
        int GARS_MINIMUM = 5;          // Minimum number of chars for GARS  
        int GARS_MAXIMUM = 7;          // Maximum number of chars for GARS  
        int LETTER_A_OFFSET = 65;      // Letter A offset in character set
        double MIN_PER_DEG = 60;       // Number of minutes per degree
        double[] ll = new double[2];

        /* A = 0, B = 1, C = 2, D = 3, E = 4, F = 5, G = 6, H = 7             */
        int LETTER_I = 8;                /* ARRAY INDEX FOR LETTER I          */
        /* J = 9, K = 10, L = 11, M = 12, N = 13                              */

        int LETTER_O = 14;              /* ARRAY INDEX FOR LETTER O           */
        /* P = 15, Q = 16, R = 17, S = 18, T = 19, U = 20, V = 21,            */
        /* W = 22, X = 23, Y = 24, Z = 25                                     */

        char _1 = '1';
        char _2 = '2';
        char _3 = '3';
        char _4 = '4';
        char _5 = '5';
        char _6 = '6';
        char _7 = '7';
        char _8 = '8';
        char _9 = '9';

        char[] GARSString = GARS.toCharArray();
        int lat = 0;
        int lon = 1;
        int gars_length = GARSString.length;
        if ((gars_length < GARS_MINIMUM) || (gars_length > GARS_MAXIMUM)) {
            ll = new double[1];
            ll[0] = -360;
            return ll;
        }
        String ew_string = "";
        int index = 0;
        while (isDigit(GARSString[index])) {
            ew_string += GARSString[index];
            index++;
        }
        //ERROR Checking longitude band, must be 3 digits
        if (index != 3) {
            ll = new double[1];
            ll[0] = -360;
            return ll;
        }

        /* Get 30 minute east/west value, 1-720 */
        Integer ew_value;
        ew_value = Integer.parseInt(ew_string);

        char letter = ' ';
        letter = GARSString[index];
        if (!Character.isLetter(letter)) {
            ll = new double[1];
            ll[0] = -360;
            return ll; // The latitude band must be a letter
        }

        /* Get first 30 minute north/south letter, A-Q */
        int[] ns_str = new int[3];
        ns_str[0] = letter - LETTER_A_OFFSET;
        letter = GARSString[++index];
        if (!Character.isLetter(letter)) {
            ll = new double[1];
            ll[0] = -360;
            return ll; // The latitude band must be a letter
        }
        /* Get second 30 minute north/south letter, A-Z */
        ns_str[1] = letter - LETTER_A_OFFSET;

        char _15_minute_value = 0;
        char _5_minute_value = 0;
        if (index + 1 < gars_length) {
            /* Get 15 minute quadrant value, 1-4 */
            _15_minute_value = GARSString[++index];
            //System.out.println("GARS " + GARS + " 15 MINUTE VAL: " + _15_minute_value);
            if (!isDigit(_15_minute_value) || _15_minute_value < _1 || _15_minute_value > _4) {
                ll = new double[1];
                ll[0] = -360;
                return ll;
            } else {
                if (index + 1 < gars_length) {
                    /* Get 5 minute quadrant value, 1-9 */
                    _5_minute_value = GARSString[++index];
                    //System.out.println("GARS " + GARS + " 5 MINUTE VAL: " + _5_minute_value);
                    if (!isDigit(_5_minute_value) || _5_minute_value < _1 || _5_minute_value > _9) {
                        ll = new double[1];
                        ll[0] = -360;
                        return ll;
                    }
                }
            }
        }//

        double longitude = (((ew_value - 1.0) / 2.0) - 180.0);
        /* Letter I and O are invalid */
        if (ns_str[0] >= LETTER_O) {
            ns_str[0]--;
        }
        if (ns_str[0] >= LETTER_I) {
            ns_str[0]--;
        }

        if (ns_str[1] >= LETTER_O) {
            ns_str[1]--;
        }
        if (ns_str[1] >= LETTER_I) {
            ns_str[1]--;
        }

        double latitude = ((-90.0 + (ns_str[0] * 12.0)) + (ns_str[1] / 2.0));

        double lat_minutes = 0.0, lon_minutes = 0.0;

        //Strings in switch not allowed in Java 6
        if (Character.toString(_15_minute_value).equals("1")) {
            lat_minutes = 15.0;
        } else if (Character.toString(_15_minute_value).equals("4")) {
            lon_minutes = 15.0;
        } else if (Character.toString(_15_minute_value).equals("2")) {
            lat_minutes = 15.0;
            lon_minutes = 15.0;
        }

        if (Character.toString(_5_minute_value).equals("4")) {
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("1")) {
            lat_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("8")) {
            lon_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("5")) {
            lon_minutes += 5.0;
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("2")) {
            lon_minutes += 5.0;
            lat_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("9")) {
            lon_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("6")) {
            lon_minutes += 10.0;
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("3")) {
            lon_minutes += 10.0;
            lat_minutes += 10.0;
        }

        /* Add these values to force the reference point to be the center of the box */
        if (_5_minute_value != 0) {
            lat_minutes += 2.5;
            lon_minutes += 2.5;
        } else if (_15_minute_value != 0) {
            lat_minutes += 7.5;
            lon_minutes += 7.5;
        } else {
            lat_minutes += 15.0;
            lon_minutes += 15.0;
        }
        latitude += lat_minutes / MIN_PER_DEG;
        longitude += lon_minutes / MIN_PER_DEG;
        ll[0] = latitude;
        ll[1] = longitude;
        return ll;

    }//getCenterCoordsArray        

    /**
     * Convert a GARS string to double array containing latitude and longitude
     * coordinates of the lower left and upper right corners of the GARS
     * tile.<br> This code is a port of the NGA's geotrans cpp routines from the
     * GEOTRANS 3.5 SDK: http://earth-info.nga.mil/GandG/geotrans/
     * <p>
     * If the GARS string is invalid then the returned array has a length of 1
     * and the first and only value will be -360<br>
     * If the conversion was successful the corner points of the GARS tile is
     * returned as decimal degrees. The first value (index 0) holds latitude,
     * the second value is longitude of the lower left corner. The third and
     * fourth values hold the latitude and longitude of the upper right
     * corner.<br>
     * Check the length of the array for error checking, the length of a
     * successful transformation will equal 4.
     * <p>
     * @param GARS
     * @return double[]
     */
    public static double[] getCornerCoordsArray(String GARS) {
        int GARS_MINIMUM = 5;          // Minimum number of chars for GARS  
        int GARS_MAXIMUM = 7;          // Maximum number of chars for GARS  
        int LETTER_A_OFFSET = 65;      // Letter A offset in character set
        double MIN_PER_DEG = 60;       // Number of minutes per degree
        double[] ll = new double[4];

        /* A = 0, B = 1, C = 2, D = 3, E = 4, F = 5, G = 6, H = 7             */
        int LETTER_I = 8;                /* ARRAY INDEX FOR LETTER I          */
        /* J = 9, K = 10, L = 11, M = 12, N = 13                              */

        int LETTER_O = 14;              /* ARRAY INDEX FOR LETTER O           */
        /* P = 15, Q = 16, R = 17, S = 18, T = 19, U = 20, V = 21,            */
        /* W = 22, X = 23, Y = 24, Z = 25                                     */

        char _1 = '1';
        char _2 = '2';
        char _3 = '3';
        char _4 = '4';
        char _5 = '5';
        char _6 = '6';
        char _7 = '7';
        char _8 = '8';
        char _9 = '9';

        char[] GARSString = GARS.toCharArray();
        int lat = 0;
        int lon = 1;
        int gars_length = GARSString.length;
        if ((gars_length < GARS_MINIMUM) || (gars_length > GARS_MAXIMUM)) {
            ll = new double[1];
            ll[0] = -360;
            return ll;
        }
        String ew_string = "";
        int index = 0;
        while (isDigit(GARSString[index])) {
            ew_string += GARSString[index];
            index++;
        }
        //ERROR Checking longitude band, must be 3 digits
        if (index != 3) {
            ll = new double[1];
            ll[0] = -360;
            return ll;
        }

        /* Get 30 minute east/west value, 1-720 */
        Integer ew_value;
        ew_value = Integer.parseInt(ew_string);

        char letter = ' ';
        letter = GARSString[index];
        if (!Character.isLetter(letter)) {
            ll = new double[1];
            ll[0] = -360;
            return ll; // The latitude band must be a letter
        }

        /* Get first 30 minute north/south letter, A-Q */
        int[] ns_str = new int[3];
        ns_str[0] = letter - LETTER_A_OFFSET;
        letter = GARSString[++index];
        if (!Character.isLetter(letter)) {
            ll = new double[1];
            ll[0] = -360;
            return ll; // The latitude band must be a letter
        }
        /* Get second 30 minute north/south letter, A-Z */
        ns_str[1] = letter - LETTER_A_OFFSET;

        char _15_minute_value = 0;
        char _5_minute_value = 0;
        if (index + 1 < gars_length) {
            /* Get 15 minute quadrant value, 1-4 */
            _15_minute_value = GARSString[++index];
            if (!isDigit(_15_minute_value) || _15_minute_value < _1 || _15_minute_value > _4) {
                ll = new double[1];
                ll[0] = -360;
                return ll;
            } else {
                if (index + 1 < gars_length) {
                    /* Get 5 minute quadrant value, 1-9 */
                    _5_minute_value = GARSString[++index];
                    if (!isDigit(_5_minute_value) || _5_minute_value < _1 || _5_minute_value > _9) {
                        ll = new double[1];
                        ll[0] = -360;
                        return ll;
                    }
                }
            }
        }//

        double longitude = (((ew_value - 1.0) / 2.0) - 180.0);
        /* Letter I and O are invalid */
        if (ns_str[0] >= LETTER_O) {
            ns_str[0]--;
        }
        if (ns_str[0] >= LETTER_I) {
            ns_str[0]--;
        }

        if (ns_str[1] >= LETTER_O) {
            ns_str[1]--;
        }
        if (ns_str[1] >= LETTER_I) {
            ns_str[1]--;
        }

        double latitude = ((-90.0 + (ns_str[0] * 12.0)) + (ns_str[1] / 2.0));

        double lat_minutes = 0.0, lon_minutes = 0.0;

        //Strings in switch not allowed in Java 6
        if (Character.toString(_15_minute_value).equals("1")) {
            lat_minutes = 15.0;
        } else if (Character.toString(_15_minute_value).equals("4")) {
            lon_minutes = 15.0;
        } else if (Character.toString(_15_minute_value).equals("2")) {
            lat_minutes = 15.0;
            lon_minutes = 15.0;
        }

        if (Character.toString(_5_minute_value).equals("4")) {
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("1")) {
            lat_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("8")) {
            lon_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("5")) {
            lon_minutes += 5.0;
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("2")) {
            lon_minutes += 5.0;
            lat_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("9")) {
            lon_minutes += 10.0;
        } else if (Character.toString(_5_minute_value).equals("6")) {
            lon_minutes += 10.0;
            lat_minutes += 5.0;
        } else if (Character.toString(_5_minute_value).equals("3")) {
            lon_minutes += 10.0;
            lat_minutes += 10.0;
        }

        // Store lower left coordinates
        ll[0] = latitude + lat_minutes / MIN_PER_DEG;
        ll[1] = longitude + lon_minutes / MIN_PER_DEG;;

        /* Add these values to force the reference point to be the upper right corner of the box */
        if (_5_minute_value != 0) {
            lat_minutes += 5.0;
            lon_minutes += 5.0;
        } else if (_15_minute_value != 0) {
            lat_minutes += 15.0;
            lon_minutes += 15.0;
        } else {
            lat_minutes += 30.0;
            lon_minutes += 30.0;
        }
        latitude += lat_minutes / MIN_PER_DEG;
        longitude += lon_minutes / MIN_PER_DEG;
        ll[2] = latitude;
        ll[3] = longitude;
        return ll;

    }//getCornerCoordsArray    

    /**
     * Convert a GARS string to JSON representation of the corner coordinates.
     * <p>
     * The JSON is will always return a status key with either "error" or
     * "success". <br>
     * If the conversion was successful then latitude and longitude keys are
     * returned with decimal degrees values for the lower left and upper right
     * corners of the GARS tile.<br> The corner coordinates are referenced by
     * the keys "lowerleft" and "upperright" and are an object containing a
     * "latitude" and "longitude" coordinate.
     * <p>
     * A typical JSON string looks like the following:<br>
     * <i>
     * {<br>
     * "status": "success",<br>"lowerleft": { "latitude":-33.4167,
     * "longitude":-70.4167 },<br>"upperright": { "latitude":-33.3336,
     * "longitude":-70.3333 }<br> }
     * </i>
     *
     * @param GARS
     * @return JSON string
     */
    public static String getCornerJSONCoords(String GARS) {
        double[] ll = getCornerCoordsArray(GARS);
        if (ll.length < 4) {
            return "{\"status\":\"error\"}";
        }
        StringBuilder json = new StringBuilder();
        json.append("{\"status\":\"success\",");
        json.append("\"lowerleft\":{\"latitude\":").append(ll[0]).append(",\"longitude\":").append(ll[1]).append("},");
        json.append("\"upperright\":{\"latitude\":").append(ll[2]).append(",\"longitude\":").append(ll[3]).append("}");
        json.append("}");

        return json.toString();

    }//getCornerJSONCoords  

    /**
     * Convert a GARS String to a KML Placemark fragment representing the GARS
     * tile as a line.<p>
     * If the GARS string is invalid then an empty string is returned.
     * <p>
     * kmlColor is a string in the format of aabbggrr, where aa=alpha (00 to
     * ff); bb=blue (00 to ff); gg=green (00 to ff); rr=red (00 to ff)<br>
     * Example: blue color with 50 percent opacity = 7fff0000<p>
     * kmlWidth is an integer representing the line width.<p>
     *
     * @param GARS
     * @param kmlColor the color of the line
     * @param kmlWidth the width of the line
     * @return KML Placemark fragment
     */
    public static String getKMLLine(String GARS, String kmlColor, double kmlWidth) {
        StringBuilder kml = new StringBuilder();
        double[] ll = getCornerCoordsArray(GARS);
        if (ll.length < 4) {
            return "";
        }
        kml.append("<Placemark> \n"
                + " <LineString>\n"
                + "  <coordinates>\n");
        kml.append(ll[1]).append(",").append(ll[0]).append(",0.0\n");
        kml.append(ll[1]).append(",").append(ll[2]).append(",0.0\n");
        kml.append(ll[3]).append(",").append(ll[2]).append(",0.0\n");
        kml.append(ll[3]).append(",").append(ll[0]).append(",0.0\n");
        kml.append(ll[1]).append(",").append(ll[0]).append(",0.0\n"
                + "  </coordinates>\n"
                + " </LineString>\n"
                + " <Style>\n"
                + "  <LineStyle>\n"
                + "   <color>#");
        kml.append(kmlColor);
        kml.append("</color>\n"
                + "   <width>");
        kml.append(kmlWidth);
        kml.append("</width>\n"
                + "  </LineStyle> \n"
                + " </Style>\n"
                + "</Placemark>");
        return kml.toString();
    }//getKMLLine

    /**
     * Convert a GARS String to a KML Placemark fragment representing the GARS
     * tile as a filled polygon.<p>
     * If the GARS string is invalid then an empty string is returned.
     * <p>
     * KML colors are a string in the format of aabbggrr, where aa=alpha (00 to
     * ff); bb=blue (00 to ff); gg=green (00 to ff); rr=red (00 to ff)<br>
     * Example: blue color with 50 percent opacity = 7fff0000<p>
     *
     * kmlWidth is an integer representing the line width.<p>
     * @param GARS
     * @param kmlColor the color of the line
     * @param kmlFillColor the color of the polygon's fill
     * @param kmlWidth the width of the line
     * @return KML Placemark fragment
     */
    public static String getKMLPolygon(String GARS, String kmlColor, String kmlFillColor, double kmlWidth) {
        StringBuilder kml = new StringBuilder();
        double[] ll = getCornerCoordsArray(GARS);
        if (ll.length < 4) {
            return "";
        }
        kml.append("<Placemark> \n"
                + "<Polygon>\n"
                + "<tessellate>1</tessellate>\n"
                + "<outerBoundaryIs>\n"
                + "<LinearRing>\n"
                + "  <coordinates>\n");
        kml.append(ll[1]).append(",").append(ll[0]).append(",0.0\n");
        kml.append(ll[1]).append(",").append(ll[2]).append(",0.0\n");
        kml.append(ll[3]).append(",").append(ll[2]).append(",0.0\n");
        kml.append(ll[3]).append(",").append(ll[0]).append(",0.0\n");
        kml.append(ll[1]).append(",").append(ll[0]).append(",0.0\n"
                + "  </coordinates>\n"
                + "</LinearRing>\n"
                + "</outerBoundaryIs>\n"
                + "</Polygon>\n"
                + " <Style>\n"
                + "  <LineStyle>\n"
                + "   <color>#");
        kml.append(kmlColor);
        kml.append("</color>\n"
                + "   <width>");
        kml.append(kmlWidth);
        kml.append("</width>\n"
                + "  </LineStyle> \n"
                + "  <PolyStyle>\n"
                + "   <color>#");
        kml.append(kmlFillColor);
        kml.append("</color>\n"
                + "  </PolyStyle> \n"
                + " </Style>\n"
                + "</Placemark>");
        return kml.toString();
    }//getKMLLine

    /**
     * Convert a GARS String to a KML Placemark fragment depicting the GARS
     * string as a label in the center of the GARS tile.<p>
     *
     * KML colors are a string in the format of aabbggrr, where aa=alpha (00 to
     * ff); bb=blue (00 to ff); gg=green (00 to ff); rr=red (00 to ff)<br>
     * Example: blue color with 50 percent opacity = 7fff0000<p>
     * @param GARS
     * @param lblColor the color of the text
     * @param lblSize the size of the text (as a ratio to the normal size of
     * 1.0)
     * @return KML Placemark fragment
     */
    public static String getKMLPoint(String GARS, String lblColor, double lblSize) {
        double[] ll = getCenterCoordsArray(GARS);
        if (ll.length != 2) {
            return "";
        }
        StringBuilder latlon = new StringBuilder();
        latlon.append(ll[1]).append(",").append(ll[0]);

        StringBuilder kml = new StringBuilder();
        kml.append("	<Placemark>\n"
                + "		<name>" + GARS + "</name>\n"
                + "	<Style>\n"
                + "		<IconStyle>\n"
                + "			<scale>0</scale>\n"
                + "		</IconStyle>\n"
                + "		<LabelStyle>\n"
                + "			<color>" + lblColor + "</color>\n"
                + "                     <scale>" + lblSize + "</scale>"
                + "		</LabelStyle>"
                + "	</Style>\n"
                + "		<Point>\n"
                + "\n"
                + "			<coordinates>" + latlon + ",0</coordinates>\n"
                + "		</Point>\n"
                + "	</Placemark>");
        return kml.toString();
    }//getKMLPoint
}
