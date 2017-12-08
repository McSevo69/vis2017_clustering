/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.univie.vis2017.util;

import java.util.ArrayList;
//import java.lang.IllegalArgumentException;

/**
 *
 * @author phuksz
 */
public class Data {
    private int n;
    private int k;
    private Algorithm algorithm;
    private ArrayList<ArrayList<Point>> data = new ArrayList<>();
    private ArrayList<ArrayList<Point>> centers = new ArrayList<>();
    
    public Data (int n, Algorithm algorithm) {
        this.n = n;
        this.algorithm = algorithm;
    }
    
    public Data() {
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
        for (ArrayList<Point> a : data) {
            if (a.size() != n) {
                return false;
            }
        }
        
        return true;
    }

    public void addIteration (ArrayList<Point> newData) {
        data.add(newData);
    }
    
    public void addIteration (ArrayList<Point> newData, ArrayList<Point> newCenters) {
        data.add(newData);
        centers.add(newCenters);
    }

    public void setIteration (int iteration, ArrayList<Point> newData) {
        data.set(iteration, newData);
    }
    
    public void setIteration (int iteration, ArrayList<Point> newData, ArrayList<Point> newCenters) {
        data.set(iteration, newData);
        centers.set(iteration, newCenters);
    }

    public ArrayList<ArrayList<Point>> getIteration (int iteration) {
        ArrayList<ArrayList<Point>> buf = new ArrayList<ArrayList<Point>>();
        buf.add(data.get(iteration));
        buf.add(centers.get(iteration));
        return buf;
    }
    
    public ArrayList<Point> getIterationData (int iteration) {
        return data.get(iteration);
    }

    public ArrayList<Point> getIterationCenters (int iteration) {
        return centers.get(iteration);
    }

    public int getIterations () {
        return data.size();
    }
    
    public Algorithm getAlgorithm () {
        return algorithm;
    }
    
    public int getN () {
        return n;
    }
    
    public int getK () {
        return k;
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
