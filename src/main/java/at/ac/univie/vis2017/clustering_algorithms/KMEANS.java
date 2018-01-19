package at.ac.univie.vis2017.clustering_algorithms;

import at.ac.univie.vis2017.gui.ClusteringController;
import at.ac.univie.vis2017.util.Algorithm;
import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    // euclidean or manhattan
    private String distanceFunction = "euclidean";
    // lloyd or macqueen
    private String updateFunction = "lloyd";
    // convergence
    private double convergenceThreshold = 1e-3;




    Logger logger = LogManager.getLogger(KMEANS.class);

    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;
        setUpdateFunction("lloyd");
        this.setConvergenceThreshold(1e-3);


        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(this.points.size(), 0));

        // set cluster centers before first iteration
        setClusterCenters("random");
        findClosestClusterCenter(distanceFunction);


        Algorithm algorithm = Algorithm.KMEANS;
        dat = new Data(this.points.size(), numberClusters, algorithm);

    }





    public double getConvergenceThreshold() {
        return convergenceThreshold;
    }

    public void setConvergenceThreshold(double convergenceThreshold) {
        this.convergenceThreshold = convergenceThreshold;
    }

    public String getUpdateFunction() {
        return updateFunction;
    }

    public void setUpdateFunction(String updateFunction) {
        this.updateFunction = updateFunction;
    }

    public String getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(String distanceFunction) {
        this.distanceFunction = distanceFunction;
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

        ClusteringController cc = new ClusteringController();

        // get extent from data
        ArrayList<Double> extent = cc.getExtentFromDataPoints(this.points);

        //System.out.println("Extent");
        //System.out.println("Minx = " + extent.get(0) + "; maxX = " + extent.get(1)  + "; minY = " + extent.get(2)  + "; maxY = " + extent.get(3) );


        // initialize data randomly
        if(strategy.equals("random")) {


            for (int i = 0; i < this.numberClusters; i++) {
                System.out.println(i);
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


    // function assigns each point to closest cluster
    public void findClosestClusterCenter(String distanceType) {

        int numberPoints = this.points.size();
        int numberClusters = this.getNumberClusters();


        //System.out.println(numberClusters);

        // search nearest cluster center for each point
        for (int i = 0; i < numberPoints; i++) {


            double minDistance = Double.MAX_VALUE;
            double actualDistance = 0.0f;

            for (int j = 0; j < numberClusters; j++) {

                actualDistance = Point.getDistanceBetweenPoints(centers.get(j), points.get(i), distanceType);

                if (actualDistance < minDistance) {
                    minDistance = actualDistance;
                    points.get(i).setClusterNumber(j + 1);
                }
            }
        }

    }

    public ArrayList<Point> computeNewCentroids() {

        // create array list for new centroids
        ArrayList<Point> newCentroids = new ArrayList<>();

        int numberClusters = this.getNumberClusters();
        int numberPoints = this.points.size();


        //System.out.println("NumberClusters = " + numberClusters);
        //System.out.println("NumberPoints = " + numberPoints);

        // find for each cluster a new centroid
        for (int i = 0; i < numberClusters; i++) {

            logger.trace("Cluster i = " + i);

            // store values of interest for calculation of the new centers
            double centroidX = 0, centroidY = 0;
            int numberPointsInCluster = 0;

            for (int j = 0; j < numberPoints; j++) {

                if (this.getPoints().get(j).getClusterNumber() == i + 1) {

                    centroidX += this.getPoints().get(j).getX();
                    //System.out.println(centroidX);
                    centroidY += this.getPoints().get(j).getY();
                    numberPointsInCluster++;
                }

            }

            logger.trace("NumberPointsInCluster = " + numberPointsInCluster);
            
            if (numberPointsInCluster > 0) {
                // create new centroid point and add in list
                Point p = new Point(centroidX / numberPointsInCluster, centroidY / numberPointsInCluster);
                p.setCenterPointTrue();
                p.setClusterSize(numberPointsInCluster);
                newCentroids.add(p);
            } else {
                this.setNumberClusters(getNumberClusters()-1);
            }
        }

        return newCentroids;
    }


    // function updates centroids after each iteration
    public void updateCentroids(ArrayList<Point> newCenters) {

        int numberClusters = this.getNumberClusters();

        for (int i = 0; i < numberClusters; i++) {
            logger.trace(i);
            this.centers.get(i).setX(newCenters.get(i).getX());
            this.centers.get(i).setY(newCenters.get(i).getY());
            this.centers.get(i).setClusterSize(newCenters.get(i).getClusterSize());
        }
    }


    public Data clusterData() {

        double convergence = 1e10;
        double old_Convergence = 1e100;
        //double threshold = this.getConvergenceThreshold();

        //System.out.println(this.getCenters());
        int maxIterations = this.getMaxIter();
        int i = 0;


        if(this.getUpdateFunction() == "lloyd") {

            // iterate until maxiter or convergence
            while((i < maxIterations) && (Math.abs(Math.abs(convergence) - Math.abs(old_Convergence)) > this.getConvergenceThreshold())) {

                logger.trace("iteration i: " + i);

                // copy actual cluster centers to compute convergence
                ArrayList<Point> actualCenters;
                actualCenters = this.getCenters();

                System.out.println("Cluster center of iteration " + i  + " = " + actualCenters);


                findClosestClusterCenter(this.getDistanceFunction());
                updateCentroids(computeNewCentroids());

                ArrayList<Point> iterationBuf = new ArrayList<>();
                for (Point p : getPoints())
                    iterationBuf.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));

                ArrayList<Point> centersBuf = new ArrayList<>();
                for (Point p : getCenters()) {
                    Point a = new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber());
                    a.setClusterSize(p.getClusterSize());
                    centersBuf.add(a);
                }


                dat.addIteration(iterationBuf, centersBuf);



                ArrayList<Point> newCenters;
                newCenters = this.getCenters();

                old_Convergence = convergence;
                convergence = 0.0;

                for (int j = 0; j < this.getNumberClusters(); j++){
                    convergence += Math.sqrt(actualCenters.get(j).getX() + newCenters.get(j).getX() * actualCenters.get(j).getY() + newCenters.get(j).getY());
                }

                System.out.println("Convergence = " + convergence);

                logger.trace(this.getCenters());

                i += 1;
            }

        } else if(this.getUpdateFunction() == "macqueen"){
            while((i < maxIterations) && (Math.abs(Math.abs(convergence) - Math.abs(old_Convergence)) > this.getConvergenceThreshold())) {

                logger.trace("iteration i: " + i);

                // copy actual cluster centers to compute convergence
                ArrayList<Point> actualCenters;
                actualCenters = this.getCenters();

                System.out.println("Cluster center of iteration " + i  + " = " + actualCenters);



                // update macqueen








                ArrayList<Point> newCenters;
                newCenters = this.getCenters();

                old_Convergence = convergence;
                convergence = 0.0;

                for (int j = 0; j < this.getNumberClusters(); j++){
                    convergence += Math.sqrt(actualCenters.get(j).getX() + newCenters.get(j).getX() * actualCenters.get(j).getY() + newCenters.get(j).getY());
                }

                System.out.println("Convergence = " + convergence);

                logger.trace(this.getCenters());

                i += 1;

            }
        }





        return this.dat;
    }


}
