/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.univie.vis2017.visualizer;

import at.ac.univie.vis2017.util.Algorithm;
import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;
import ch.netzwerg.paleo.ColumnIds.CategoryColumnId;
import ch.netzwerg.paleo.ColumnIds.DoubleColumnId;
import ch.netzwerg.paleo.ColumnIds.StringColumnId;
import ch.netzwerg.paleo.ColumnType;
import ch.netzwerg.paleo.DataFrame;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Severin
 * @author phuksz
 */
public class VisualizerFX implements IVisualizer {
    private Canvas canvas;
    
    private int iteration;
    private int pointIterator;
    private int speed;
    private Algorithm algorithm;
    private Mode mode;
    
    private boolean showPaths;
    private boolean showCenters;
    private boolean showData;
    private boolean showVoronoi;
    
    private Data data;
    
    private GraphicsContext gc;
    private double colorValueChunk; // the space of 360 degrees is divided into
                                    // k equally sized chunks, where k is the
                                    // amount of clusters in the data.
    
    private int pSize = 5;
    private int cSize = 5;
    
    Logger logger = LogManager.getLogger(VisualizerFX.class);
    
    public VisualizerFX () {
        this.iteration = 0;
        this.pointIterator = 0;
        this.mode = Mode.MANUAL;
        
        this.data = null;
        this.speed = 0;
        this.algorithm = null;
        
        this.showPaths = false;
        this.showData = true;
        this.showCenters = true;
        this.showVoronoi = false;
        
        this.colorValueChunk = 360;
    }
    
    public VisualizerFX (Data data) {
        this.iteration = 0;
        this.pointIterator = 0;
        this.mode = Mode.MANUAL;

        this.data = data;
        this.speed = data.getN();
        this.algorithm = data.getAlgorithm();

        this.showPaths = false;
        this.showData = true;
        this.showCenters = true;
        this.showVoronoi = false;

        setColorValueChunk();
    }
    
    public void setAlgorithm (Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public void setIteration (int iteration) {
        this.pointIterator = 0;
        
        if (iteration < data.getIterations()) {
            this.iteration = iteration;
        }
        
        draw();
    }
    
    public int getIteration () {
        return iteration;
    }
    
    public void setSpeed (int speed) {
        this.speed = (int) ((speed/100.0) * data.getN());
    }
    
    public void setMode (Mode mode) {
        this.mode = mode;
        draw();
    }
    
    public void setShowPaths (boolean showPaths) {
        this.showPaths = showPaths;
        draw();
    }
    
    public void setShowCenters (boolean showCenters) {
        this.showCenters = showCenters;
        draw();
    }
    
    public void setShowData (boolean showData) {
        this.showData = showData;
        draw();
    }

    public void setShowVornoi (boolean showVoronoi) {
        this.showVoronoi = showVoronoi;
        draw();
    }

    public void setData (Data data) {
        this.data = data;
        this.speed = data.getN();
        this.algorithm = data.getAlgorithm();
        setColorValueChunk();
    }
    
    public void setGraphicsContext (GraphicsContext gc) {
        this.gc = gc;
        setCanvas(gc.getCanvas());
    }
    
    public void setCanvas (Canvas canvas) {
        this.canvas = canvas;
    }
    
    public void iterate () {
        if (iteration < data.getIterations() - 1) {
            if (pointIterator+speed < data.getN()) {
                pointIterator += speed;
            } else {
                iteration++;
                pointIterator = 0;
            }
        }
        
        draw();
    }
    
    public void stepback () {
        if (iteration > 0) {
            if (pointIterator-speed > 0) {
                pointIterator -= speed;
            } else {
                iteration--;
                pointIterator = data.getN() - speed;
            }
        }
        
        draw();
    }
    
    public void restart () {
        iteration = 0;
        draw();
    }
    
    private void setColorValueChunk () {
        colorValueChunk = 360.0 / data.getK();
    }
    
    public void drawInitialState(GraphicsContext gc, ArrayList<Point> is) {
        
        double height = gc.getCanvas().getHeight();
        double width = gc.getCanvas().getWidth();
        double canvasHeight = 500;
        double canvasWidth = 500;
        double normalize = 140.0;
        
        gc.setFill(Color.BLACK);
                
        for (Point point : is) {
            gc.setStroke(Color.WHITE);
            gc.fillOval((point.getX()/normalize)*width, (point.getY()/normalize)*height, 8*width/canvasWidth, 8*height/canvasHeight);
        }
        
    }
    
    
    public void drawPoint(Point p) {
        gc.setFill(Color.hsb(p.getClusterNumber()*colorValueChunk,1,1));
        gc.fillOval(p.getX(), p.getY(), pSize, pSize);
    }
    
    public void drawCenter(Point p) {
        gc.setFill(Color.hsb(p.getClusterNumber()*colorValueChunk,1,1));
        gc.setStroke(Color.BLACK);

        gc.fillRect(p.getX(), p.getY(), cSize, cSize);
        gc.strokeRect(p.getX(), p.getY(), cSize, cSize);
    }
    
    
    public void drawIterationData() {
        if (data.getIterationData(0) == null) return;
        for (Point p : data.getIterationData(iteration)) {
            drawPoint(p);
        }
    }

    public void drawIterationCenters () {
        if (data.getIterationCenters(0) == null) return;
        for (Point p : data.getIterationCenters(iteration)) {
            drawCenter(p);
        }
    }
    
    public void drawIterationPaths () {
        if (data.getIterationCenters(0) == null) return;
        for (int i = 0; i < data.getIterationCenters(iteration).size(); ++i) {
            for (int j = 1; j <= iteration; ++j) {
                Point curr = data.getIterationCenters(j).get(i);
                Point past = data.getIterationCenters(j-1).get(i);
//                gc.setStroke(Color.hsb(curr.getClusterNumber()*colorValueChunk,1,1));
                gc.setStroke(Color.BLACK);
                gc.strokeLine(past.getX()+cSize/2, past.getY()+cSize/2, curr.getX()+cSize/2, curr.getY()+cSize/2);
            }
        }
    }
    
    public void drawIterationVoronoi () {
        if (data.getIterationCenters(0) == null) return;

        ArrayList<Point> voronoiPoints = new ArrayList<Point>();

        for (int i = 0; i < data.getK(); ++i) {
            Point c1 = data.getIterationCenters(iteration).get(i);

            for (int j = i+1; j < data.getK(); ++j) {
                Point c2 = data.getIterationCenters(iteration).get(j);
                
                voronoiPoints.add(
                        new Point(
                                (c1.getX()+c2.getX())/2, 
                                (c1.getY()+c2.getY())/2
                        )
                );
            }
        }
        
        for (Point p : voronoiPoints) {
            gc.setFill(Color.BLACK);
            gc.strokeOval(p.getX(), p.getY(), pSize, pSize);
        }
    }

    /*
    phuksz has TODO what phuksz has TODO:
    - add auto functionality
    - add snapshot-function for the snapshot-panels
    - complete voronoi-visualization (http://765.blogspot.co.at/2009/09/how-to-draw-voronoi-diagram.html)
    
    longterm:
    - add visualizations for other algorithms
    
    */
    public void draw () {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        pSize = (int) ((canvas.getWidth() + canvas.getHeight())/2)/80;
        cSize = (int) ((canvas.getWidth() + canvas.getHeight())/2)/80;
        /*
        logger.debug("showPaths:   " + showPaths);
        logger.debug("showData:    " + showData);
        logger.debug("showCenters: " + showCenters);
        logger.debug("showVoronoi: " + showVoronoi);

        System.out.println("showPaths:   " + showPaths);
        System.out.println("showData:    " + showData);
        System.out.println("showCenters: " + showCenters);
        System.out.println("showVoronoi: " + showVoronoi);
        */
        if (data != null) {
            if (data.getAlgorithm() == Algorithm.KMEANS) {
                if (showPaths) {
                    drawIterationPaths();
                }

                if (showData) {
                    drawIterationData();
                }

                if (showCenters) {
                    drawIterationCenters();
                }

                if (showVoronoi) {
                    drawIterationVoronoi();
                }
            } else if (data.getAlgorithm() == Algorithm.DBSCAN) {

            } else if (data.getAlgorithm() == Algorithm.OPTICS) {

            } else if (data.getAlgorithm() == Algorithm.KMEDIANS) {

            } else if (data.getAlgorithm() == Algorithm.KMEDOIDS) {

            } else {
                throw new IllegalArgumentException("not a known algorithm");
            }
        }
    }

    public void drawShapes(GraphicsContext gc) {
        //System.out.println(canvas.getWidth() + ":" + canvas.getHeight());        
        
        //if (data != null) {
          //  gc.setFill(Color.GREEN);

           // for (Point p : data.getIteration(iteration)) {
           //     drawPoint (gc, p);
           // }
        //}
        
        //gc.setFill(Color.GREEN);
        //gc.fillOval(10, 60, 30, 30);
        
/*
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                       new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                         new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                          new double[]{210, 210, 240, 240}, 4);
*/
    }
    
    /*public void drawBorder(GraphicsContext g) {
        final double canvasWidth = g.getCanvas().getWidth();
        final double canvasHeight = g.getCanvas().getHeight();

        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeRect(0, 0, canvasWidth, canvasHeight);

    }*/
    
    public void bindProperties(Canvas canvas, Pane parent, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());
    }
}
