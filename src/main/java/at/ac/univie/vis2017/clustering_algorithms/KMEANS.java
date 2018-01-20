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
    
    public enum Initialization {RANDOM, RANDOM_PARTITION, FARTHEST, D2, USERCHOICE};
    public enum Update {LLOYD, MACQUEEN};
    
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
    // initialization
    private Initialization init = Initialization.RANDOM;
    // convergence
    private double convergenceThreshold = 1e-3;

    Logger logger = LogManager.getLogger(KMEANS.class);

    //default constructor -> LLOYD, EUCLIDIAN, RANDOM
    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;
        this.updateFunction = "lloyd";
        this.convergenceThreshold = 1e-3;

        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(this.points.size(), 0));

        // set cluster centers before first iteration
        setClusterCenters();

        findClosestClusterCenter(distanceFunction);

        Algorithm algorithm = Algorithm.KMEANS;
        dat = new Data(this.points.size(), numberClusters, algorithm);

    }

    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points, Initialization init) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;
        this.init = init;
        setUpdateFunction("lloyd");
        this.setConvergenceThreshold(1e-3);


        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(this.points.size(), 0));

        // set cluster centers before first iteration
        setClusterCenters();

        if(this.getUpdateFunction() == "lloyd") {
            findClosestClusterCenter(distanceFunction);
        }




        Algorithm algorithm = Algorithm.KMEANS;
        dat = new Data(this.points.size(), numberClusters, algorithm);

    }

    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points, ArrayList<Point> centers) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;
        setUpdateFunction("lloyd");
        this.setConvergenceThreshold(1e-3);


        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(this.points.size(), 0));

        // set cluster centers before first iteration
        this.centers = new ArrayList<>(centers);
        setClusterCenters();

        if(this.getUpdateFunction() == "lloyd") {
            findClosestClusterCenter(distanceFunction);
        }




        Algorithm algorithm = Algorithm.KMEANS;
        dat = new Data(this.points.size(), numberClusters, algorithm);

    }

    public KMEANS(int numberClusters, int maxIter, ArrayList<Point> points, ArrayList<Point> centers, Initialization init) {
        this.numberClusters = numberClusters;
        this.maxIter = maxIter;
        this.points = points;
        this.init = init;
        setUpdateFunction("lloyd");
        this.setConvergenceThreshold(1e-3);


        this.clusterNumber = new ArrayList<Integer>(Collections.nCopies(this.points.size(), 0));

        // set cluster centers before first iteration
        this.centers = new ArrayList<>(centers);
        setClusterCenters();

        if(this.getUpdateFunction() == "lloyd") {
            findClosestClusterCenter(distanceFunction);
        }




        Algorithm algorithm = Algorithm.KMEANS;
        dat = new Data(this.points.size(), numberClusters, algorithm);

    }

    public double getConvergenceThreshold() {
        return convergenceThreshold;
    }

    public void setConvergenceThreshold(double convergenceThreshold) {
        this.convergenceThreshold = convergenceThreshold;
    }
    
    public Initialization getInit () {
        return init;
    }
    
    public void setInitialization (Initialization init) {
        this.init = init;
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
    
    // function finds the extent of the given dataset
    public ArrayList<Double> getExtentFromDataPoints(ArrayList<Point> points) {

        // extent[0]=minX; extent[1]=maxX; extent[2]=minY; extent[3]=maxY;
        ArrayList<Double> extent = new ArrayList<>();


        // get extent of data to randomly set the initial points
        extent.add(Double.MAX_VALUE);      //minX
        extent.add(Double.MIN_NORMAL);     //maxX
        extent.add(Double.MAX_VALUE);      //minY
        extent.add(Double.MIN_VALUE);     //maxY

        // assign min and max x from points
        for(Point p : points) {
            if (p.getX() < extent.get(0))
                extent.set(0, p.getX());
            if (p.getX() > extent.get(1))
                extent.set(1, p.getX());
            if (p.getY() < extent.get(2))
                extent.set(2, p.getY());
            if (p.getY() > extent.get(3))
                extent.set(3, p.getY());
        }

        //System.out.println(extent.size());

        return extent;
    }

    public void setClusterCenters() {
        Random r = new Random();

        // get extent from data
        ArrayList<Double> extent = getExtentFromDataPoints(this.points);

        logger.debug(init.toString());

        if(null != init) // initialize data randomly
        switch (init) {
            case RANDOM:
                for (int i = 0; i < this.numberClusters; i++) {
                    logger.trace(i + " from random initialization");
                    // create random values for centers
                    double randomValueX = extent.get(0)  + (extent.get(1)  - extent.get(0) ) * r.nextDouble();
                    double randomValueY = extent.get(2)  + (extent.get(3)  - extent.get(2) ) * r.nextDouble();
                    
                    //System.out.println("Point: " + i + "x = " + randomValueX + "; y = " + randomValueY);
                    Point p = new Point(randomValueX, randomValueY);
                    p.setCenterPointTrue();
                    p.setClusterNumber(i);
                    
                    centers.add(p);
                } 
                break;
            case RANDOM_PARTITION:
                ArrayList<ArrayList<Point>> clusterSets = new ArrayList<>(this.numberClusters);
                
                for (int i = 0; i < this.numberClusters; ++i) {
                    clusterSets.add(new ArrayList<>());
                }
                
                for (Point p : points) {
                    int c = r.nextInt(numberClusters);
                    p.setClusterNumber(c);
                    clusterSets.get(c).add(new Point(p));
                }
                
                
                for (int i = 0; i < this.numberClusters; ++i) {
                    double sumX = 0;
                    double sumY = 0;

                    for (int j = 0; j < clusterSets.get(i).size(); ++j) {
                        sumX += clusterSets.get(i).get(j).getX();
                        sumY += clusterSets.get(i).get(j).getY();
                    }
                    
                    Point c = new Point(sumX/clusterSets.get(i).size(), sumY/clusterSets.get(i).size());
                    c.setCenterPointTrue();
                    c.setClusterSize(clusterSets.get(i).size());
                    c.setClusterNumber(i);
                    centers.add(c);
                    System.out.println(c);
                }
                break;
            case FARTHEST:
                break;
            case D2:
                int i = 0;
                double randomValueX = extent.get(0)  + (extent.get(1)  - extent.get(0) ) * r.nextDouble();
                double randomValueY = extent.get(2)  + (extent.get(3)  - extent.get(2) ) * r.nextDouble();
                //System.out.println("Point: " + i + "x = " + randomValueX + "; y = " + randomValueY);
                Point c0 = new Point(randomValueX, randomValueY);
                c0.setCenterPointTrue();
                c0.setClusterNumber(i++);
                centers.add(c0);
                for (; i < numberClusters; ++i) {
                    ArrayList<Double> minDistances = new ArrayList<>();
                    double sumDistance = 0;
                    for (Point p : points) {
                        double minDistance = Double.MAX_VALUE;
                        for (Point c : centers) {
                            if (Point.getDistanceBetweenPoints(c, p, distanceFunction) < minDistance) {
                                minDistance = Point.getDistanceBetweenPoints(c, p, distanceFunction);
                            }
                        }
                        
                        minDistances.add(minDistance);
                        sumDistance += minDistance;
                    }
                    
                    while (centers.size() == i) {
                        for (int j = 0; j < points.size(); ++j) {
                            if (r.nextDouble() < minDistances.get(j)/sumDistance) {
                                Point c = new Point(points.get(j));
                                c.setCenterPointTrue();
                                c.setClusterNumber(i);
                                centers.add(c);
                                break;
                            }
                        }
                    }
                }   break;
            case USERCHOICE:
                if (centers.size() != numberClusters) {
                    throw new IllegalArgumentException("cluster list passed by user does not match k");
                }   for (int j = 0; j < numberClusters; ++j) {
                    centers.get(j).setCenterPointTrue();
                    centers.get(j).setClusterNumber(j);
                }   break;
            default:
                break;
        }

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


        if(updateFunction.equals("lloyd")) {

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

        } else if(updateFunction.equals("macqueen")){

            int numberPoints = this.points.size();
            String distanceFunction = this.getDistanceFunction();

            while((i < maxIterations) && (Math.abs(Math.abs(convergence) - Math.abs(old_Convergence)) > this.getConvergenceThreshold())) {

                logger.trace("iteration i: " + i);

                // copy actual cluster centers to compute convergence
                ArrayList<Point> actualCenters;
                actualCenters = this.getCenters();

                System.out.println("Cluster center of iteration " + i  + " = " + actualCenters);

                // compute initial centroids
                updateCentroids(computeNewCentroids());

                // update macqueen style
                for (int k = 0; k < numberPoints; k++) {


                    double newDistance = 0.0;
                    int oldCluster = this.points.get(k).getClusterNumber();
                    double actualDistance = Point.getDistanceBetweenPoints(centers.get(oldCluster), points.get(k), distanceFunction);

                    // check for each point if there is a new nearest cluster center and update if
                    for (int j = 0; j < numberClusters; j++) {

                        newDistance = Point.getDistanceBetweenPoints(centers.get(j), points.get(k), distanceFunction);

                        if (newDistance < actualDistance) {
                            actualDistance = newDistance;
                            points.get(i).setClusterNumber(j + 1);


                            // assign new cluster center if distance is nearer than to actual center
                            int newCluster = j + 1;

                            this.points.get(k).setClusterNumber(newCluster);

                            // recompute centroids for affected clusters
                            for (int l = 0; l < numberPoints; l++) {


                                // compute centroids for points with cluster numbers
                                // TO DO update centroids of clusters


                            }


                        }
                    }

                }
                
                ArrayList<Point> newCenters;
                newCenters = this.getCenters();

                old_Convergence = convergence;
                convergence = 0.0;

                for (int j = 0; j < this.getNumberClusters(); j++){
                    convergence += Math.sqrt(actualCenters.get(j).getX() + newCenters.get(j).getX() * actualCenters.get(j).getY() + newCenters.get(j).getY());
                }

                logger.debug("Convergence = " + convergence);

                logger.trace(this.getCenters());

                i++;

            }
        }


        return this.dat;
    }


}
