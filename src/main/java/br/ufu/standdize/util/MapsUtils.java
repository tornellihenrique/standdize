package br.ufu.standdize.util;

public class MapsUtils {

    public static String latLonToTileZXY(double lat, double lon, int z) {
        final int MIN_ZOOM_LEVEL = 0;
        final int MAX_ZOOM_LEVEL = 22;
        final double MIN_LAT = -85.051128779807;
        final double MAX_LAT = 85.051128779806;
        final double MIN_LON = -180.0;
        final double MAX_LON = 180.0;

        if ((z < MIN_ZOOM_LEVEL) || (z > MAX_ZOOM_LEVEL)) {
            throw new IllegalArgumentException("Zoom level value is out of range [" +
                    Integer.toString(MIN_ZOOM_LEVEL) + ", " +
                    Integer.toString(MAX_ZOOM_LEVEL) + "]");
        }

        if (!Double.isFinite(lat) || (lat < MIN_LAT) || (lat > MAX_LAT)) {
            throw new IllegalArgumentException("Latitude value is out of range [" +
                    Double.toString(MIN_LAT) + ", " +
                    Double.toString(MAX_LAT) + "]");
        }

        if (!Double.isFinite(lon) || (lon < MIN_LON) || (lon > MAX_LON)) {
            throw new IllegalArgumentException("Longitude value is out of range [" +
                    Double.toString(MIN_LON) + ", " +
                    Double.toString(MAX_LON) + "]");
        }

        int xyTilesCount = (int) Math.pow(2, z);
        int x = (int) Math.floor((lon + 180.0) / 360.0 * xyTilesCount);
        int y = (int) Math.floor((1.0 - Math.log(Math.tan(lat * Math.PI / 180.0) + 1.0 / Math.cos(lat * Math.PI / 180.0)) / Math.PI) / 2.0 * xyTilesCount);

        return Integer.toString(z) + "/" + Integer.toString(x) + "/" + Integer.toString(y);
    }

    public static String tileZXYToLatLon(int z, int x, int y) {
        final int MIN_ZOOM_LEVEL = 0;
        final int MAX_ZOOM_LEVEL = 22;

        if ((z < MIN_ZOOM_LEVEL) || (z > MAX_ZOOM_LEVEL)) {
            throw new IllegalArgumentException("Zoom level value is out of range [" +
                    Integer.toString(MIN_ZOOM_LEVEL) + ", " +
                    Integer.toString(MAX_ZOOM_LEVEL) + "]");
        }

        int minXY = 0;
        int maxXY = (int)(Math.pow(2, z) - 1);
        if ((x < minXY) || (x > maxXY)) {
            throw new IllegalArgumentException("Tile x value is out of range [" +
                    Integer.toString(minXY) + ", " +
                    Integer.toString(maxXY) + "]");
        }

        if ((y < 0) || (y > maxXY)) {
            throw new IllegalArgumentException("Tile y value is out of range [" +
                    Integer.toString(minXY) + ", " +
                    Integer.toString(maxXY) + "]");
        }

        double lon = (double) x / Math.pow(2, z) * 360.0 - 180.0;

        double n = Math.PI - 2.0 * Math.PI * (double) y / Math.pow(2, z);
        double lat = 180.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));

        return Double.toString(lat) + "/" + Double.toString(lon);
    }
}
