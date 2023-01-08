package system.auxmethods;

public class Haversine {

    public static double shorterDistanceKm(double iniLat, double iniLong, double finLat, double finLong) {
        final int EARTH_RADIUS = 6371;
        double diffLong = Math.toRadians(finLong - iniLong);
        double diffLat = Math.toRadians(finLat - iniLat);
        
        double startLatRadius = Math.toRadians(iniLat);
        double endLatRadius = Math.toRadians(finLat);
        
        double a = Math.pow(Math.sin(diffLat / 2), 2) + Math.pow(Math.sin(diffLong / 2), 2) * Math.cos(startLatRadius) * Math.cos(endLatRadius);
        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS * c;
    }

}
