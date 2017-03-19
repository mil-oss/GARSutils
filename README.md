# GARSutils
Convert latitude,longitude to a GARS (Global Area Reference System) code, and extract coordinates (center, corners) for a given GARS code

Convert latitude,longitude to a GARS code, and extract coordinates (center, corners) for a given GARS code

This Java project converts latitude and longitude (in decimal degrees) to a GARS code. It also converts a GARS string to a latitude,longitude point (decimal degrees) of the center of the GARS tile as well as providing the corner coordinates of the GARS tile. JSON and KML representations of the GARS tile are also supported.

GARS is the Global Area Reference System described here: http://earth-info.nga.mil/GandG/coordsys/grids/gars.html

The Java classes are ports of NGA's geotrans 3.5 cpp code: http://earth-info.nga.mil/GandG/geotrans/

See GARS.java for all usage examples.

Code usage snippets:

    double lat = 48.123;
    double lon = 8.123;
    // Lat, Lon to GARS
    String garsCode = LLtoGARS.getGARS(latitude, longitude); //Returns the default 5x5-minute GARS code 
    garsCode = LLtoGARS.getGARS(latitude, longitude, "5");  //Returns the 5x5-minute GARS code        
    garsCode = LLtoGARS.getGARS(latitude, longitude, "15"); //Returns the 15x15-minute GARS code
    garsCode = LLtoGARS.getGARS(latitude, longitude, "30"); //Returns the 30x30-minute GARS code
    
    // GARS to lat, lon
    garsCode = "427LK2";
    double[] latlon = GARStoLL.getCenterCoordsArray(garsCode); 
            //latlon[0] = latitude, latlon[1] = longitude
    double[] ll = GARStoLL.getCornerCoordsArray(garsCode); 
            //ll[0],ll[1] represents the lat,lon of lower left corner
            //ll[2],ll[3] represents the lat,lon of upper right corner
    String center = GARStoLL.getCenterCoords(garsCode);
    String jsonCenter = GARStoLL.getCenterJSONCoords(garsCode); 
            // JSON object representing the center coordinates
    String jsonCorners = GARStoLL.getCornerJSONCoords(garsCode); 
            // JSON object representing the corner coordinates

    String kmlColor = "ff0000ff";
    double kmlLabelSize = 1.0;
    String kmlCenterPoint = GARStoLL.getKMLPoint(garsCode, kmlColor, kmlLabelSize); 
            // GARS center as kml placemerk
    double kmlLineWidth = 1.5;
    String kmlLineString = GARStoLL.getKMLLine(garsCode, kmlColor, kmlLineWidth); 
            // GARS tile as kml linestring
    String kmlPolygonFillColor = "aa00ff00";
    String kmlPolygon = GARStoLL.getKMLPolygon(garsCode, kmlColor, kmlPolygonFillColor, kmlLineWidth); 
            // gars tile as filled kml polygon
