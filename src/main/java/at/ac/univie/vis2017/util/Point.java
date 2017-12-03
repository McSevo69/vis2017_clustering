package at.ac.univie.vis2017.util;

public class Point {

    // x and y coordinates
    private double x = NULL;
    private double y = NULL;
    // cluster center (just for kmeans and kmedoids)
    private double centerX = NULL;
    private double centerY = NULL;
    // cluster number
    private int clusterNumber = 0;


    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", clusterNumber=" + clusterNumber +
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
}
