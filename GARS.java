/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gars;

/**
 *
 * @author marty
 */
public class GARS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("================================================");
        StringBuilder gars = new StringBuilder();
        double latitude = 0.0;
        double longitude = 0.0;
        String GARString = "";
        gars.append("\nTest lat,lon GARS for ").append(latitude).append(",").append(longitude).append(" ");
        gars.append(LLtoGARS.getLongitudeBand(longitude)).append(LLtoGARS.getLatitudeBand(latitude));
        gars.append(LLtoGARS.getQudarant(latitude, longitude));
        gars.append(LLtoGARS.getKeyPad(latitude, longitude));
        System.out.println(gars.toString());
        System.out.print("30x30min " + LLtoGARS.getGARS(latitude, longitude, "30"));
        System.out.print(" 15x15min " + LLtoGARS.getGARS(latitude, longitude, "15"));
        GARString = LLtoGARS.getGARS(latitude, longitude);
        System.out.println(" 5x5min " + GARString);
        System.out.println("Reverse GARS2LL: " + GARStoLL.getCenterJSONCoords(GARString));
        latitude = 33.3333;
        longitude = 33.3333;
        gars = new StringBuilder();
        gars.append("\nTest lat,lon GARS for ").append(latitude).append(",").append(longitude).append(" ");
        gars.append(LLtoGARS.getLongitudeBand(longitude)).append(LLtoGARS.getLatitudeBand(latitude));
        gars.append(LLtoGARS.getQudarant(latitude, longitude));
        gars.append(LLtoGARS.getKeyPad(latitude, longitude));
        System.out.println(gars.toString());
        System.out.print("30x30min " + LLtoGARS.getGARS(latitude, longitude, "30"));
        System.out.print(" 15x15min " + LLtoGARS.getGARS(latitude, longitude, "15"));
        GARString = LLtoGARS.getGARS(latitude, longitude);
        System.out.println(" 5x5min " + GARString);
        System.out.println("Reverse GARS2LL: " + GARStoLL.getCenterJSONCoords(GARString));
        latitude = 12.123;
        longitude = 34.123;
        gars = new StringBuilder();
        gars.append("\nTest lat,lon GARS for ").append(latitude).append(",").append(longitude).append(" ");
        gars.append(LLtoGARS.getLongitudeBand(longitude)).append(LLtoGARS.getLatitudeBand(latitude));
        gars.append(LLtoGARS.getQudarant(latitude, longitude));
        gars.append(LLtoGARS.getKeyPad(latitude, longitude));
        System.out.println(gars.toString());
        System.out.print("30x30min " + LLtoGARS.getGARS(latitude, longitude, "30"));
        System.out.print(" 15x15min " + LLtoGARS.getGARS(latitude, longitude, "15"));
        GARString = LLtoGARS.getGARS(latitude, longitude);
        System.out.println(" 5x5min " + GARString);
        System.out.println("Reverse GARS2LL: " + GARStoLL.getCenterJSONCoords(GARString));
        latitude = 34.9006462097168;
        longitude = 33.43534469604492 ;
        gars = new StringBuilder();
        gars.append("\nTest lat,lon GARS for ").append(latitude).append(",").append(longitude).append(" ");
        gars.append(LLtoGARS.getLongitudeBand(longitude)).append(LLtoGARS.getLatitudeBand(latitude));
        gars.append(LLtoGARS.getQudarant(latitude, longitude));
        gars.append(LLtoGARS.getKeyPad(latitude, longitude));
        System.out.println(gars.toString());
        System.out.print("30x30min " + LLtoGARS.getGARS(latitude, longitude, "30"));
        System.out.print(" 15x15min " + LLtoGARS.getGARS(latitude, longitude, "15"));
        GARString = LLtoGARS.getGARS(latitude, longitude);
        System.out.println(" 5x5min " + GARString);
        System.out.println("Reverse GARS2LL (Center as lat,lon String): " + GARStoLL.getCenterCoords(GARString));
        System.out.println("Reverse GARS2LL (Center as KML String): " + GARStoLL.getCenterKMLCoords(GARString));
        System.out.println("Reverse GARS2LL (Center as JSON): " + GARStoLL.getCenterJSONCoords(GARString));
        System.out.println("Reverse GARS2LL (Corners as JSON): " + GARStoLL.getCornerJSONCoords(GARString));
        System.out.println("Reverse GARS2LL (Corners as KML line): \n" + GARStoLL.getKMLLine(LLtoGARS.getGARS(latitude, longitude, "30"), "ff0000ff", 2.5));
        System.out.println("Reverse GARS2LL (Corners as KML Polygon): \n" + GARStoLL.getKMLPolygon(LLtoGARS.getGARS(latitude, longitude, "15"), "ff0000ff", "7fff0000", 1.5));
        System.out.println("Reverse GARS2LL (Center as KML label): \n" + GARStoLL.getKMLPoint(LLtoGARS.getGARS(latitude, longitude, "30"), "ff0000ff", 0.8));

        String badGARS = "xxx";
        System.out.println("\nTest error handling with invalid GARS string: " + badGARS);
        System.out.println("Reverse GARS2LL (Center as lat,lon String): " + GARStoLL.getCenterCoords(badGARS));
        System.out.println("Reverse GARS2LL (Center as KML String): " + GARStoLL.getCenterKMLCoords(badGARS));
        System.out.println("Reverse GARS2LL (Center as JSON): " + GARStoLL.getCenterJSONCoords(badGARS));
        System.out.println("Reverse GARS2LL (Corners as JSON): " + GARStoLL.getCornerJSONCoords(badGARS));
        System.out.println("Reverse GARS2LL (Corners as KML line): " + GARStoLL.getKMLLine(badGARS, "ff0000ff", 2));
    }

}
