package at.ac.univie.vis2017.visualizer;

import vis2017.util.*;

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
    public void setAlgorithm (Algorithm algorithm);
    
    public void setIteration ();
    public int getIteration ();
    
    public void setSpeed ();
    
    public void setMode ();
    
    public void setShowPaths ();
    
    public void setData ();
}
