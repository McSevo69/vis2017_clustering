package at.ac.univie.vis2017.clustering_algorithms;



import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;
import ch.netzwerg.paleo.DataFrame;

import java.util.ArrayList;



public class DBSCAN {

    private int minPts = 0;
    private double eps_value = 0.0;
    ArrayList<Point> points = new ArrayList<>();

    // save all necessary information
    // check if point is already classified



    // prepare Data object for
    private Data dat = null;


    public DBSCAN(int minPts, double eps_value, ArrayList<Point> points) {
        this.minPts = minPts;
        this.eps_value = eps_value;
        this.points = points;
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


}
