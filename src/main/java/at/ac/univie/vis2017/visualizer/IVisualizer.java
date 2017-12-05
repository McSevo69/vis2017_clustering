package at.ac.univie.vis2017.visualizer;

import at.ac.univie.vis2017.util.*;

/*
 *  data fields:
 *      - algorithm : algorithm enum
 *          - set
 *      - iteration : int
 *          - set/get
 *      - speed     : int
 *          - set
 *      - mode      : bool // auto/manual
 *          - set
 *      - showpaths : bool
 *          - set
 *      - data      : Wrapper
 *          - set
 */

public interface IVisualizer {
    public enum Mode {
        AUTO, MANUAL
    }
    
    public void setAlgorithm (Algorithm algorithm);
    
    public void setIteration (int iteration);
    public int getIteration ();
    
    public void setSpeed (int speed);
    
    public void setMode (Mode mode);
    
    public void setShowPaths (boolean showPaths);
    
    public void setData (Data data);
    
    public void iterate ();
}
