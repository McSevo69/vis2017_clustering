package vis2017.visualizer;

import java.util.*;

import processing.core.*;

import vis2017.util.*;

public class Visualizer extends PApplet implements IVisualizer
{
    private int iteration;
    
    public static void main (String[] args) {
        PApplet.main(args);
    }

    public void setAlgorithm (Algorithm algorithm) {}
    
    public void setIteration () {}
    public int getIteration () {
        return iteration;
    }
    
    public void setSpeed () {}
    
    public void setMode () {}
    
    public void setShowPaths () {}
    
    public void setData () {}

    public void settings () {
        size (256, 256);
    }
    
    public void setup () {
        
    }
    
    public void draw () {
        // ellipse (x-coordinate, y-coordinate, x-radius, y-radius);
        ellipse (10, 10, 10, 10);
    }
}
