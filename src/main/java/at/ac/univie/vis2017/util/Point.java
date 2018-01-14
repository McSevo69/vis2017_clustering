package at.ac.univie.vis2017.util;

public class Point {


    // x and y coordinates
    private double x;
    private double y;
    // cluster center (just for kmeans and kmedoids)
    private double centerX;
    private double centerY;
    // cluster number
    private int clusterNumber = 0;
    // true if Point is centerpoint
    private boolean isCenterPoint = false;

    public boolean isCenterPoint() {
        return isCenterPoint;
    }

    public void setCenterPoint(boolean centerPoint) {
        isCenterPoint = centerPoint;
    }

    public void setCenterPointTrue() {
        isCenterPoint = true;
    }

    public Point (double x, double y) {
        this.x = x;
        this.y = y;
        this.centerX = 0;
        this.centerY = 0;
        this.clusterNumber = 0;
    }
    
    public Point(double x, double y, double centerX, double centerY, int clusterNumber) {
        this.x = x;
        this.y = y;
        this.centerX = centerX;
        this.centerY = centerY;
        this.clusterNumber = clusterNumber;
    }

    /*@Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", clusterNumber=" + clusterNumber +
                '}';
    }*/

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", clusterNumber=" + clusterNumber +
                ", isCenterPoint=" + isCenterPoint +
                '}';
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public int getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(int clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    // compute distance between two points
    public static double getDistanceBetweenPoints(Point p1, Point p2, String type) {

        if (type == "euclidean") {

            return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + ((p1.getY() - p2.getY()) * (p1.getY() - p2.getY())));


        } else if (type == "manhattan") {

            return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());

        }

        return 0.0;
    }
}
