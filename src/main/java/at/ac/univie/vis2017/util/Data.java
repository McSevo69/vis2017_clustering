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
    private Algorithm algorithm;
    private ArrayList<ArrayList<Point>> data;
    
    
    public Data (int n, Algorithm algorithm) {
        this.n = n;
        this.algorithm = algorithm;
    }
    
    public Data (int n, Algorithm algorithm, ArrayList<Point> initData) {
        this.n = n;
        this.algorithm = algorithm;
/*        
        if (initData.size() < n) {
            throw new IllegalArgumentException(
                "the passed array list is of wrong size " + initData.size() + 
                " (expected n=" + n + ")"
            );
        }
*/        
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
        this.data.add(newData);
    }
    
    public void setIteration (int iteration, ArrayList<Point> newData) {
        this.data.set(iteration, newData);
    }
    
    public ArrayList<Point> getIteration (int iteration) {
        return this.data.get(iteration);
    }
    
    public int getIterations () {
        return this.data.size();
    }
    
    public Algorithm getAlgortihm () {
        return this.algorithm;
    }
}
