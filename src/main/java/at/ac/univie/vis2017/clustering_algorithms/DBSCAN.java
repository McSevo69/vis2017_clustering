package at.ac.univie.vis2017.clustering_algorithms;



import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;
import ch.netzwerg.paleo.DataFrame;

import java.util.ArrayList;
import java.util.Collections;


public class DBSCAN {

    private int minPts = 0;
    private double eps_value = 0.0;
    ArrayList<Point> points = new ArrayList<>();
    ArrayList<Integer> clusterNumber;
    // prepare Data object for
    private Data dat;

    public DBSCAN(int minPts, double eps_value) {
        this.minPts = minPts;
        this.eps_value = eps_value;
        dat = new Data();


    }




    public DBSCAN(int minPts, double eps_value, ArrayList<Point> points) {
        this.minPts = minPts;
        this.eps_value = eps_value;
        this.points = points;

        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(points.size(), 0));

        dat = new Data();
    }

    public int getMinPts() {
        return minPts;
    }

    public void setMinPts(int minPts) {
        this.minPts = minPts;
    }

    public double getEps_value() {
        return eps_value;
    }

    public void setEps_value(double eps_value) {
        this.eps_value = eps_value;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Point> points) {
        this.points = points;
    }

    public ArrayList<Integer> getClusterNumber() {
        return clusterNumber;
    }

    public void setClusterNumber(ArrayList<Integer> clusterNumber) {
        this.clusterNumber = clusterNumber;
    }

    public Data getDat() {
        return dat;
    }

    public void setDat(Data dat) {
        this.dat = dat;
    }


    public void clusterData() {

        // get extent of data
        double minX = 1000000;
        double maxX = -1000000;
        double minY = 1000000;
        double maxY = -1000000;

        // assign min and max x from points
        for(Point p : points) {
            if (p.getX() > maxX)
                maxX = p.getX();
            if (p.getX() < minX)
                minX = p.getX();
            if (p.getY() > maxY)
                maxY = p.getY();
            if (p.getY() < minY)
                minY = p.getY();
        }






    }

}
