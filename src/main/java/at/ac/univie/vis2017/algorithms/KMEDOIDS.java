package at.ac.univie.vis2017.algorithms;

import ch.netzwerg.paleo.DataFrame;

public class KMEDOIDS {

    // number of clusters (minimum is 2)
    private int numberClusters = 2;
    // maximum number of iterations
    private int maxIter = 100;
    // dataframe for points to cluster
    DataFrame data = null;


    public KMEDOIDS(int numberClusters, int maxIter, DataFrame data) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.data = data;
    }

    public int getNumberClusters() {
        return numberClusters;
    }

    public void setNumberClusters(int numberClusters) {
        this.numberClusters = numberClusters;
    }

    public int getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(int maxIter) {
        this.maxIter = maxIter;
    }

    public DataFrame getData() {
        return data;
    }

    public void setData(DataFrame data) {
        this.data = data;
    }
}
