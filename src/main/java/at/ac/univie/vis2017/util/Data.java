/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.univie.vis2017.util;

import at.ac.univie.vis2017.gui.ClusteringController;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import java.lang.IllegalArgumentException;

/**
 *
 * @author phuksz
 */
public class Data {

    //number of data points
    private int n;
    // number of clusters
    private int k;
    // algorithm
    private Algorithm algorithm;
    // original data points
    private ArrayList<ArrayList<Point>> data = new ArrayList<>();
    // centers for kmeans, kmedians or kedoids
    private ArrayList<ArrayList<Point>> centers = new ArrayList<>();
    // number of cluster each poiunt is assigned to
    private ArrayList<Integer> clusterNumber = new ArrayList<>();
    
    private double maxX;
    private double maxY;
    
    Logger logger = LogManager.getLogger(Data.class);
    
    public Data() {
        this.n = 0;
        this.k = 0;
        this.algorithm = null;
        this.data = null;
        this.centers = null;
        this.clusterNumber = null;
    }
    
    public Data(int n, int k, Algorithm algo, ArrayList<ArrayList<Point>> data,
            ArrayList<ArrayList<Point>> centers, ArrayList<Integer> clusterNum) {

        this.n = n;
        this.k = k;
        this.algorithm = algo;
        this.data = data;
        this.centers = centers;
        this.clusterNumber = clusterNum;      
        
    }
    
    public Data(Data datCopy) {
        this(datCopy.getN(), datCopy.getK(), datCopy.getAlgorithm(), datCopy.getData(),
                datCopy.getCenters(), datCopy.getClusterNumber());
    }
    
    public Data (int n, Algorithm algorithm) {
        this.n = n;
        this.algorithm = algorithm;
    }

    public Data(int n, Algorithm algorithm, ArrayList<Point> initData) {
        this.n = n;
        this.algorithm = algorithm;
        this.data.add(initData);
    }

    public Data (int n, int k, Algorithm algorithm) {
        this.n = n;
        this.k = k;
        this.algorithm = algorithm;
    }
    
    public Data (int n, int k, Algorithm algorithm, ArrayList<Point> initData) {
        this.n = n;
        this.k = k;
        this.algorithm = algorithm;

        this.data.add(initData);
    }
    
    public boolean checkData () {
        logger.debug("checkData called");
        for (ArrayList<Point> a : data) {
            if (a.size() != n) {
                return false;
            }
        }
        return true;
    }

    public void addIteration (ArrayList<Point> newData) {
        logger.debug("addIteration called");
        data.add(newData);
    }

    public void addCenters (ArrayList<Point> newCenters) {
        logger.debug("addCenters called");
        data.add(newCenters);
    }
    
    public void addIteration (ArrayList<Point> newData, ArrayList<Point> newCenters) {
        logger.debug("addIteration including centers called");
        data.add(newData);
        centers.add(newCenters);
    }

    public void setIteration (int iteration, ArrayList<Point> newData) {
        logger.debug("setIteration called");
        data.set(iteration, newData);
    }
    
    public void setIteration (int iteration, ArrayList<Point> newData, ArrayList<Point> newCenters) {
        logger.debug("setIteration including centers called");
        data.set(iteration, newData);
        centers.set(iteration, newCenters);
    }

    public ArrayList<ArrayList<Point>> getIteration (int iteration) {
        logger.debug("getIteration called");
        ArrayList<ArrayList<Point>> buf = new ArrayList<ArrayList<Point>>();
        buf.add(data.get(iteration));
        buf.add(centers.get(iteration));
        return buf;
    }
    
    public ArrayList<Point> getIterationData (int iteration) {
        logger.debug("getIterationData called");
        return data.get(iteration);
    }

    public ArrayList<Point> getIterationCenters (int iteration) {
        logger.debug("getIterationCenters called");
        return centers.get(iteration);
    }

    public int getIterations () {
        logger.debug("getIterations = " + data.size());
        return data.size();
    }
    
    public Algorithm getAlgorithm () {
        logger.debug("geAlgorithm = " + algorithm);
        return algorithm;
    }
    
    public int getN () {
        logger.debug("getN = " + n);
        return n;
    }
    
    public int getK () {
        logger.debug("getK = " + k);
        return k;
    }
    
    public ArrayList<ArrayList<Point>> getData() {
        logger.debug("into getData");
        return data;
    }
    
    public ArrayList<ArrayList<Point>> getCenters() {
        logger.debug("into getCenters");
        return centers;
    }
    
    public ArrayList<Integer> getClusterNumber() {
        logger.debug("into getClusterNumber");
        return clusterNumber;
    }
    
    public void setMaxX (double maxX) {
        logger.debug("setMaxX = " + maxX);
        this.maxX = maxX;
    }
    
    public double getMaxX () {
        logger.debug("getMaxX = " + maxX);
        return this.maxX;
    }
    
    public void setMaxY (double maxY) {
        logger.debug("setMaxY = " + maxY);
        this.maxY = maxY;
    }

    public double getMaxY () {
        logger.debug("getMaxY = " + maxY);
        return this.maxY;
    }
    
    public static Data getTestData () {
        int n = 5;
        int k = 4;
        Algorithm algorithm = Algorithm.KMEANS;

        
        Data data = new Data(n, k, algorithm);

        ArrayList<Point> points = new ArrayList<Point>();
        points.add(new Point(20, 45, 1, 1, 1));
        points.add(new Point(22, 40, 1, 1, 1));
        points.add(new Point(25, 43, 1, 1, 1));
        points.add(new Point(130, 150, 1, 1, 1));
        points.add(new Point(132, 145, 1, 1, 1));
        
        ArrayList<Point> centers = new ArrayList<Point>();
        centers.add(new Point(120, 50, 1, 1, 1));
        centers.add(new Point(200, 150, 1, 1, 2));
        centers.add(new Point(150, 200, 1, 1, 3));
        centers.add(new Point(300, 300, 1, 1, 4));

        data.addIteration(points, centers);

        
        points = new ArrayList<Point>();
        points.add(new Point(20, 45, 1, 1, 1));
        points.add(new Point(22, 40, 1, 1, 1));
        points.add(new Point(25, 43, 1, 1, 1));
        points.add(new Point(130, 150, 1, 1, 2));
        points.add(new Point(132, 145, 1, 1, 2));
        
        centers = new ArrayList<Point>();
        centers.add(new Point(22.3, 42.6, 1, 1, 1));
        centers.add(new Point(131, 147.5, 1, 1, 2));
        centers.add(new Point(150, 200, 1, 1, 3));
        centers.add(new Point(300, 300, 1, 1, 4));
        
        data.addIteration(points, centers);
        
        return data;
    }
}
