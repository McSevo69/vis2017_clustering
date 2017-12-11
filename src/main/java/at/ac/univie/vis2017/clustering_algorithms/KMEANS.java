package at.ac.univie.vis2017.clustering_algorithms;

import at.ac.univie.vis2017.gui.ClusteringController;
import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class KMEANS {

    // number of clusters (minimum is 2)
    private int numberClusters = 2;
    // maximum number of iterations
    private int maxIter = 4;
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

    public ArrayList<Point> getCenters() {
        return centers;
    }

    public void setCenters(ArrayList<Point> centers) {
        this.centers = centers;
    }



    public void setClusterCenters(String strategy) {

        // get extent from data
        ArrayList<Double> extent = ClusteringController.getExtentFromDataPoints(this.points);

        System.out.println("Extent");
        System.out.println("Minx = " + extent.get(0) + "; maxX = " + extent.get(1)  + "; minY = " + extent.get(2)  + "; maxY = " + extent.get(3) );

        System.out.println("ClusterCenters");
        // initialize data randomly
        if(strategy.equals("random")) {


            for (int i = 0; i < this.numberClusters; i++) {

                // create random values for centers
                Random r = new Random();
                double randomValueX = extent.get(0)  + (extent.get(1)  - extent.get(0) ) * r.nextDouble();
                double randomValueY = extent.get(2)  + (extent.get(3)  - extent.get(2) ) * r.nextDouble();

                //System.out.println("Point: " + i + "x = " + randomValueX + "; y = " + randomValueY);
                Point p = new Point(randomValueX, randomValueY);
                p.setCenterPointTrue();
                p.setClusterNumber(i);

                centers.add(p);

            }
        }
/*

        System.out.println("Number CenterPoints = " + centers.size());

        for(Point p : centers) {
            System.out.println(p);
        }*/
    }


    // function updates centroids after each iteration
    public void updateCentroids(ArrayList<Point> newCenters) {

        for (int i = 0; i < this.centers.size(); i++) {
            this.centers.get(i).setX(newCenters.get(i).getX());
            this.centers.get(i).setY(newCenters.get(i).getY());
        }

    }


    // function assigns each point to closest cluster
    public void findClosestClusterCenter() {

        int numberPoints = this.points.size();
        int numberClusters = this.getNumberClusters();


        //System.out.println(numberClusters);

        // search nearest cluster center for each point
        for (int i = 0; i < numberPoints; i++) {

            //
            double minDistance = Double.MAX_VALUE;
            double actualDistance = 0.0f;

            for (int j = 0; j < numberClusters; j++) {

                actualDistance = Point.getDistanceBetweenPoints(centers.get(j), points.get(i));

                if (actualDistance < minDistance) {
                    minDistance = actualDistance;
                    points.get(i).setClusterNumber(j + 1);
                }
            }
        }

    }





    public void clusterData() {


        // assign maxiter to local variable to save performance
        int maxIterations = this.getMaxIter();

        // iterate until maximum iteration or convergence (convergence not implemented yet)
        for (int i = 0; i < 1; i++) {
            System.out.println("iteration i: " + i);
            findClosestClusterCenter();
        }

        // check if class labels are assigned
        for (int i = 0; i < this.points.size(); i++) {
            System.out.println(this.points.get(i));
        }

    }





}
