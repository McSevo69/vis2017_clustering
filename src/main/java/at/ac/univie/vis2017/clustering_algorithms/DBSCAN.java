package at.ac.univie.vis2017.clustering_algorithms;

import ch.netzwerg.paleo.DataFrame;

public class DBSCAN {

    private int minPts = 0;
    private double eps_value = 0.0;
    // save all necessary informations
    private DataFrame data = null;

    public DBSCAN(int minPts, double eps_value, DataFrame data) {
        this.minPts = minPts;
        this.eps_value = eps_value;
        this.data = data;
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

    public DataFrame getData() {
        return data;
    }

    public void setData(DataFrame data) {
        this.data = data;
    }
}
