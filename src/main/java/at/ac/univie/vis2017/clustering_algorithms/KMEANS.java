package at.ac.univie.vis2017.clustering_algorithms;

import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class KMEANS {

    // number of clusters (minimum is 2)
    private int numberClusters = 2;
    // maximum number of iterations
    private int maxIter = 100;
    // initial data points
    ArrayList<Point> points = new ArrayList<>();
    // actual data centers for each iteration
     ArrayList<Point> centers = new ArrayList<>();
    // actual cluster numbers
    ArrayList<Integer> clusterNumber;
    // prepare Data object for
    private Data dat;







    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;

        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(points.size(), 0));
        dat = new Data();
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





    public void setClusterCenters(String strategy) {


        // get extent of data to randomly set the initial points
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

        System.out.println("Extent");
        System.out.println("Minx = " + minX + "; maxX = " + maxX + "; minY = " + minY + "; maxY = " + maxY);

        System.out.println("ClusterCenters");
        // initialize data randomly
        if(strategy.equals("random")) {


            for (int i = 0; i < this.numberClusters; i++) {
                Random r = new Random();
                double randomValueX = minX + (maxX - minX) * r.nextDouble();
                double randomValueY = minY + (maxY - minY) * r.nextDouble();

                System.out.println("Point " + i + "=" + randomValueX + "-" + randomValueY);
            }
        }



    }



    public void clusterData() {

    }





}
