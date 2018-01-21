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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
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

  private Canvas canvas = null;
    
    private int iteration;
    private int oldIteration;           
    private int pointIterator;          // iterates over the points during a 
                                        // a single 
    private int lastStep;               // size of the last step taken before
                                        // we pass into new iteration
    private int speed;                  // until now a value between 0 and 100. 
                                        //better: value between 0 and data.size

    private Algorithm algorithm;
    private Mode mode;
    private boolean beforeComputation;
    
    private boolean showPaths;
    private boolean showCenters;
    private boolean showData;
    private boolean showVoronoi;
    private boolean showIteration;
    
    private Data data;
    
    private GraphicsContext gc;
    private Pane parentPane;
    private double colorValueChunk; // the space of 360 degrees is divided into
                                    // k equally sized chunks, where k is the
                                    // amount of clusters in the data.
    private double colorLower = 20;
    private double colorUpper = 200;
    
    private boolean colorblindMode = false;
    
    private int pSize = 5;
    private int cSize = 5;
    
    private double maxX = 150;
    private double maxY = 150;
    
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
        this.showIteration = false;
        
        this.colorValueChunk = 360;
        
        this.beforeComputation = true;
//        installTooltips ();
    }
    
    public VisualizerFX (Data data) {
        this.iteration = 0;
        this.pointIterator = 0;
        this.mode = Mode.MANUAL;
        
        setData(data);

        this.showPaths = false;
        this.showData = true;
        this.showCenters = true;
        this.showVoronoi = false;
        this.showIteration = false;

        this.colorValueChunk = 360;
        
        this.beforeComputation = true;
//        installTooltips ();
    }
/*    
    public void installTooltips () {
        Tooltip mousePositionToolTip = new Tooltip("");
        parentPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg = "(x: " + event.getX() + ", y: " + event.getY() + ")\n(sceneX: "
                        + event.getSceneX() + ", sceneY: " + event.getSceneY() + ")\n(screenX: "
                        + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";
                mousePositionToolTip.setText(msg);

                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            }

        });
    }
*/    
    public void setAfterComputation () {
        beforeComputation = false;
    }
    
    public void setColorblindMode (boolean colorblindMode) {
        this.colorblindMode = colorblindMode;
    }
    
    public boolean getColorblindMode () {
        return colorblindMode;
    }
    
    public void setAlgorithm (Algorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    public void setIteration (int iteration) {
//        this.pointIterator = 0;
        
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
        /*
        //logger.debug("  " + data.getN() + " - " + "((int) " + data.getN() + "/" + this.speed + ") * " + this.speed);
        //logger.debug("= " + data.getN() + " - " + "((int) " + data.getN()/this.speed + ") * " + this.speed);
        //logger.debug("= " + data.getN() + " - " + (int) data.getN()/this.speed + " * " + this.speed);
        //logger.debug("= " + data.getN() + " - " + ((int) data.getN()/this.speed) * this.speed);
        //logger.debug("= " + (data.getN() - ((int) data.getN()/this.speed) * this.speed));
        */
        lastStep = data.getN() - (((int) (data.getN()/this.speed)) * this.speed);
    }
    
    public void setMode (Mode mode) {
        this.mode = mode;
        //logger.debug("mode is set to: " + mode.toString());
        draw();
    }
    
    public Mode getMode () {
        return mode;
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
    
    public void setShowIteration (boolean showIteration) {
        this.showIteration = showIteration;
    }

    public void setData (Data data) {
        this.beforeComputation = true;
        this.data = data;
        this.speed = data.getN();
        this.algorithm = data.getAlgorithm();
        setColorValueChunk();
    }
    
    public Data getData () {
        return data;
    }
    
    public void setGraphicsContext (GraphicsContext gc) {
        this.gc = gc;
        setCanvas(gc.getCanvas());
    }
    
    public void setCanvas (Canvas canvas) {
        this.canvas = canvas;
    }
    
    public void iterate () {
        if (iteration > data.getIterations()) throw new IndexOutOfBoundsException(iteration + " is out of bounds " + data.getIterations());

        if (beforeComputation) {
            beforeComputation = false;
        }
        
/*
        if (iteration <= data.getIterations() - 1) {
            if (pointIterator+speed < data.getN()) {
                pointIterator += speed;
            } else {
                oldIteration = iteration++;
                lastStep = data.getN() - pointIterator;
                pointIterator = 0;
            }
        }
*/        
        
        if (speed == data.getN()) { // if s == x, no pointiterator
            oldIteration = iteration++;
            pointIterator = 0;
//            lastStep = speed;
        } else if (speed > 0 && speed < data.getN()) {
            if (pointIterator == data.getN()) { // from (i, x) to (i+1, 0)
                oldIteration = iteration++;
                pointIterator = 0;
            } else if (pointIterator+speed >= data.getN()) { // from (i, x-a) to (i, x)
//                lastStep = data.getN() - pointIterator;
                pointIterator = data.getN();
            } else { // from (i, x-b) to (i, x-a)
                pointIterator += speed;
            }
        } else {
            
        }
/*        
        //logger.debug(
                "Iteration, speed, pointIterator: " + 
                iteration + ", " + 
                speed + ", " + 
                pointIterator
        );
*/        
        draw();
    }
    
    public void stepback () {
        if (iteration < 0) throw new IndexOutOfBoundsException(iteration + " is out of bounds.");
        
        if (speed == data.getN()) { // if s == x, no pointiterator
            oldIteration = iteration--;
            pointIterator = 0;
        } else if (speed > 0 && speed < data.getN()) {
            if (pointIterator == 0) { // from (i, 0) to (i-1, x)
                oldIteration = iteration--;
                pointIterator = data.getN();
            } else if (pointIterator == data.getN()) { // from (i, x) to (i, x-s)
                pointIterator -= lastStep;
            } else if (pointIterator-speed >= 0) { // from (i, x-a) to (i, x-b)
                pointIterator -= speed;
            } else { // from (i, x-b) to (i, x-a)
                pointIterator = 0;
            }
        } else {
            
        }
/*
        if (iteration > 0) {
            //logger.debug("pointIterator=" + pointIterator + ", speed=" + speed + ", pointIterator-speed=" + (pointIterator-speed)+ ", >0?", (pointIterator-speed > 0));
            if (pointIterator-speed > 0) {
                //logger.debug("pointIterator: " + pointIterator);
                //logger.debug("speed: " + speed);
                pointIterator -= speed;
            } else {
                //logger.debug("pointIterator: " + pointIterator);
                //logger.debug("speed: " + speed);
                oldIteration = iteration--;
                pointIterator = data.getN();
            }
        } else if (pointIterator > 0) {
            if (pointIterator-speed > 0) {
                //logger.debug("line 188");
                pointIterator -= speed;
            } else {
                //logger.debug("line 191");
                pointIterator = 0;
            }
        } else {
            //logger.debug("line 195");
        }
*/        
/*
        //logger.debug(
                "Iteration, speed, pointIterator: " + 
                iteration + ", " + 
                speed + ", " + 
                pointIterator
        );
*/
        
        draw();
    }
    
    public void restart () {
        iteration = 0;
        pointIterator = 0;
        draw();
    }
    
    private void setColorValueChunk () {
        colorValueChunk = 2 * (colorUpper - colorLower) / data.getK();
    }
    
    private double getHueByID (int id) {
        return id * colorValueChunk + colorLower;
    }
    
    private double normalizeX (double x) {
        //System.out.println(x + "/" + maxX + " * " + canvas.getWidth());
        return (x/maxX) * canvas.getWidth();
    }
    
    private double normalizeY (double y) {
        return (y/maxY) * canvas.getHeight();
    }

    /*public void drawInitialState(GraphicsContext gc, ArrayList<Point> is) {
        
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        double height = gc.getCanvas().getHeight();
        double width = gc.getCanvas().getWidth();
        //double canvasHeight = 500;
        //double canvasWidth = 500;
        double normalize = 150.0;
        
        gc.setFill(Color.BLACK);
                
        for (Point point : is) {
            gc.setStroke(Color.WHITE);
            gc.fillOval((point.getX()/normalize)*width, (point.getY()/normalize)*height, pSize, pSize);
        }
        
    }*/
    
    
    public void drawPoint(Point p, double opaque) {
        if (beforeComputation || p.getClusterNumber() < 0) {
            gc.setFill(Color.BLACK);
        } else {
//            gc.setFill(Color.hsb(p.getClusterNumber()*colorValueChunk,opaque,1));
            gc.setFill(Color.hsb(getHueByID(p.getClusterNumber()),opaque,1));
        }

//        System.out.println("(" + (int)normalizeX(p.getX()) + "," + (int)normalizeY(p.getY()) + ") -> " + p.getClusterNumber());
        if (true) {
            switch (p.getClusterNumber()) {
                case 0:
                    drawKaro(p);
                    break;
                case 1:
                    drawCross(p);
                    break;
                case 2:
                    drawX(p);
                    break;
                case 3:
                    drawTriangle(p);
                    break;
                case 4:
                    drawHashtag(p);
                    break;
                case 5:
                    gc.fillOval(normalizeX(p.getX()), normalizeY(p.getY()), pSize, pSize);
                    break;
                case 6:
                    drawPentagon(p);
                    break;
                case 7:
                    drawStar(p);
                    break;
                case -1:
                    gc.fillOval(normalizeX(p.getX()), normalizeY(p.getY()), pSize, pSize);
                    break;
                default:
            }
        } else {
            gc.fillOval(normalizeX(p.getX()), normalizeY(p.getY()), pSize, pSize);
        }
    }
    
    public void drawCenter(Point p) {
        //logger.debug("drawCenter called");
        if (iteration < 0) return;
        //logger.debug("  not returned");
        
        if (beforeComputation || p.getClusterNumber() < 0) {
//            gc.setFill(Color.hsb((p.getClusterNumber()+1)*colorValueChunk,0,0.5)); // THAT'S DIRTY!!!
            gc.setFill(Color.hsb(getHueByID(p.getClusterNumber() + 1),0,0.5));
        } else {
//            gc.setFill(Color.hsb((p.getClusterNumber()+1)*colorValueChunk,1,1)); // THAT'S DIRTY!!!
            gc.setFill(Color.hsb(getHueByID(p.getClusterNumber() + 1),1,1));
        }

        gc.setStroke(Color.BLACK);
        
//        System.out.println("(" + (int)normalizeX(p.getX()) + "," + (int)normalizeY(p.getY()) + ") -> " + p.getClusterNumber());
        gc.fillRect(normalizeX(p.getX()), normalizeY(p.getY()), cSize, cSize);
        gc.strokeRect(normalizeX(p.getX()), normalizeY(p.getY()), cSize, cSize);
    }
    
    public void highlightCluster(int clusterID) {
        if (data.getIterationData(0) == null) return;
        for (Point p : data.getIterationData(iteration)) {
            if (p.getClusterNumber() == clusterID) {
                drawPoint (p, 0.9);
            } 
        }
    }
    
    
    public void drawIterationData() {
        //logger.debug("executing drawIterationData");
        if (data.getIterationData(0) == null) return;
        for (Point p : data.getIterationData(iteration)) {
            if (pointIterator == 0) {
                drawPoint (p, 0.6);
            } else {
                drawPoint(p, 0.1);
            }
        }
        
        if (iteration<0) return;
        
        try {        
            if (data.getIterationData(iteration+1) == null) {
                //logger.debug("no " + iteration + "+1 found!");
                return;
            }
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //logger.debug("no " + iteration + "+1 found!");
            return;   
        }
        ArrayList<Point> points = data.getIterationData(iteration+1);
        for (int i = 0; i < pointIterator && i < points.size(); ++i) {
            drawPoint(points.get(i), 0.6);

            if (pointIterator == data.getN()) {
                if (i > data.getN() - lastStep) {
                    gc.setFill(Color.BLACK);
                    gc.strokeOval(normalizeX(points.get(i).getX()), normalizeY(points.get(i).getY()), pSize, pSize);
                }
            } else if (i > pointIterator-speed) {
                gc.setFill(Color.BLACK);
                gc.strokeOval(normalizeX(points.get(i).getX()), normalizeY(points.get(i).getY()), pSize, pSize);
            }
        }

    }

    public void drawIterationCenters () {
        //logger.debug("executing drawIterationCenters");
        try {
            if (data.getIterationCenters(0) == null) return;
        } catch (java.lang.IndexOutOfBoundsException ex) {
            return;
        }
        
        for (Point p : data.getIterationCenters(iteration)) {
            drawCenter(p);
        }
    }
    
    public void drawIterationPaths () {
        //logger.debug("executing drawIterationPaths");
        if (data.getIterationCenters(0) == null) return;
        for (int i = 0; i < data.getIterationCenters(iteration).size(); ++i) {
            for (int j = 1; j <= iteration; ++j) {
                Point curr = data.getIterationCenters(j).get(i);
                Point past = data.getIterationCenters(j-1).get(i);
//                gc.setStroke(Color.hsb(curr.getClusterNumber()*colorValueChunk,1,1));
                gc.setStroke(Color.BLACK);
                gc.strokeLine(
                        normalizeX(past.getX())+cSize/2, 
                        normalizeY(past.getY())+cSize/2, 
                        normalizeX(curr.getX())+cSize/2, 
                        normalizeY(curr.getY())+cSize/2);
            }
        }
    }
    
    public void drawIterationVoronoi () {
        //logger.debug("executing drawIterationVoronoi");
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
            gc.strokeOval(normalizeX(p.getX()), normalizeY(p.getY()), pSize, pSize);
        }
    }
    
    public void clearCanvas() {
        gc.setFill(Color.WHITE);
        //gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //canvas = new Canvas();
        //gc = canvas.getGraphicsContext2D();
        //bindProperties(canvas, parentPane);
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
        if (canvas == null) {
            throw new RuntimeException("no canvas set!");
        }
        
        clearCanvas();
        
        pSize = (int) ((canvas.getWidth() + canvas.getHeight())/2)/70;
        cSize = (int) ((canvas.getWidth() + canvas.getHeight())/2)/70;
        
        //logger.debug("showPaths is set to:   " + showPaths);
        //logger.debug("showData is set to:    " + showData);
        //logger.debug("showCenters is set to: " + showCenters);
        //logger.debug("showVoronoi is set to: " + showVoronoi);
        /*
        System.out.println("showPaths:   " + showPaths);
        System.out.println("showData:    " + showData);
        System.out.println("showCenters: " + showCenters);
        System.out.println("showVoronoi: " + showVoronoi);
        */
        if (data != null) {
            if (data.getAlgorithm() == Algorithm.KMEANS) {
                
                
                if (showData) {
                    drawIterationData();
                }
                
                if (showCenters) {
                    drawIterationCenters();
                }
                
                if (showPaths) {
                    drawIterationPaths();
                }           

                if (showVoronoi) {
                    drawIterationVoronoi();
                }
                
                if (showIteration) {
                    gc.setFill(Color.BLACK);
                    gc.fillText(iteration + "", canvas.getWidth()/20, canvas.getHeight()/10);
                } else if (iteration == data.getIterations()-1) {
                    gc.setFill(Color.BLACK);
                    gc.fillText("converged", canvas.getWidth()/20, canvas.getHeight()/20);
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
    
    public void bindProperties(Canvas canvas, Pane parent) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.parentPane = parent;
        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());
    }
    
    public ArrayList<ArrayList<ArrayList<Point>>> hashCenters (Data data) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        ArrayList<ArrayList<ArrayList<Point>>> centers = new ArrayList<>();
        
        for (int k = 0; k < data.getCenters().size(); ++k) {
            centers.add(new ArrayList<>());
            for (int i = 0; i < width; ++i) {
                centers.get(k).add(new ArrayList<>());
                for (int j = 0; j < height; ++j) {
                    centers.get(k).get(i).add(null);
                }
            }
        }
        
        for (int i = 0; i < data.getCenters().size(); ++i) {
            ArrayList<Point> list = data.getCenters().get(i);
            for (Point p : list) {
                for (int j = 0; j < cSize; ++j) {
                    for (int k = 0; k < cSize; ++k) {
                        //String msg = "x: " + (int) normalizeX(p.getX()) + ", y: " + (int) normalizeY(p.getY())
                          //      + "\nCluster ID:   " + p.getClusterNumber() 
                            //    + "\nCluster size: " + p.getClusterSize();
                        centers.get(i).get((int) normalizeX(p.getX()) + j).set((int) normalizeY(p.getY()) + k, new Point(p));
                    }
                }
            }
        }
        /*
        for (int i = 0; i < centers.size(); ++i) {
            //logger.debug("in iteration: " + i + ":");
            for (int j = 0; j < centers.get(i).size(); ++j) {
                for (int k = 0; k < centers.get(i).get(j).size(); ++k) {
                    if (centers.get(i).get(j).get(k) != null) {
                        //logger.debug(j + ":" + k);
                        //logger.debug(centers.get(i).get(j).get(k));
                    }
                }
            }
        }
        */
        return centers;
    }
    
    public ArrayList<ArrayList<Point>> hashPoints (Data data) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        ArrayList<ArrayList<Point>> points = new ArrayList<>();
        
        for (int i = 0; i < width; ++i) {
            points.add(new ArrayList<>());
            for (int j = 0; j < height; ++j) {
                points.get(i).add(null);
            }
        }
        
        for (Point p : data.getIterationData(0)) {
            for (int i = 0; i < pSize; ++i) {
                for (int j = 0; j < pSize; ++j) {
                    //String msg = "x: " + (int) normalizeX(p.getX()) + ", y: " + (int) normalizeY(p.getY());
                    points.get((int) normalizeX(p.getX()) + i).set((int) normalizeY(p.getY()) + j, new Point(p));
                }
            }
        }
        
        return points;
    }
    
    public void drawKaro (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 4;
        double[] x = new double [] {pSize/2+pX, pSize+pX, pSize/2+pX, 0+pX};
        double[] y = new double [] {0+pY, pSize/2+pY, pSize+pY, pSize/2+pY};
        gc.fillPolygon(x, y, n);
    }
    
    public void drawCross (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 12;
        double[] x = new double [] {0+pX, pSize/2+pX, pSize/2+pX, pSize/2+pX+2, pSize/2+pX+2, pSize+pX+2, pSize+pX+2, pSize/2+pX+2, pSize/2+pX+2, pSize/2+pX, pSize/2+pX, 0+pX};
        double[] y = new double [] {pSize/2+pY, pSize/2+pY, 0+pY, 0+pY, pSize/2+pY, pSize/2+pY, pSize/2+pY+2, pSize/2+pY+2, pSize+pY+2, pSize+pY+2, pSize/2+pY+2, pSize/2+pY+2};
        gc.fillPolygon(x, y, n);
    }
    
    public void drawTriangle (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 3;
        double[] x = new double [] {0+pX, pSize/2+pX, pSize+pX};
        double[] y = new double [] {pSize+pY, 0+pY, pSize+pY};
        gc.fillPolygon(x, y, n);
    }

    public void drawStar (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 10;
        double[] x = new double [] {0+pX, pSize/4+pX, pSize/2+pX, 3*pSize/4+pX, pSize+pX, 2*pSize/3+pX, 4*pSize/5+pX, pSize/2+pX, pSize/5+pX, pSize/3+pX};
        double[] y = new double [] {pSize/3+pY, pSize/3+pY, 0+pY, pSize/3+pY, pSize/3+pY, 3*pSize/5+pY, pSize+pY, 3*pSize/5+pY, pSize+pY, 3*pSize/5+pY};
        gc.fillPolygon(x, y, n);
    }
    
    public void drawX (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 8;
        double[] x = new double [] {0+pX, pSize/2+pX,   pSize+pX, 3*pSize/4+pX, pSize+pX, pSize/2+pX,   0+pX,     pSize/4+pX};
        double[] y = new double [] {0+pY, pSize/4+pY, 0+pY,     pSize/2+pY,   pSize+pY, 3*pSize/4+pY, pSize+pY, pSize/2+pY};
        gc.fillPolygon(x, y, n);
    }
    
    public void drawPentagon (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 5;
        double[] x = new double [] {0+pX, pSize/2+pX, pSize+pX, 4*pSize/5+pX, pSize/5+pX};
        double[] y = new double [] {2*pSize/5+pY, 0+pY, 2*pSize/5+pY, pSize+pY, pSize+pY};
        gc.fillPolygon(x, y, n);
    }
    
    public void drawHashtag (Point p) {
        double pX = normalizeX(p.getX());
        double pY = normalizeY(p.getY());
        int n = 4;
        double[] x = new double [] {pSize/5+pX, 2*pSize/5+pX, 2*pSize/5+pX, pSize/5+pX};
        double[] y = new double [] {0+pY, 0+pY, pSize+pY, pSize+pY};
        gc.fillPolygon(x, y, n);
        x = new double [] {0+pX, 0+pX, pSize+pX, pSize+pX};
        y = new double [] {pSize/5+pY, 2*pSize/5+pY, 2*pSize/5+pY, pSize/5+pY};
        gc.fillPolygon(x, y, n);
        x = new double [] {3*pSize/5+pX, 4*pSize/5+pX, 4*pSize/5+pX, 3*pSize/5+pX};
        y = new double [] {0+pY, 0+pY, pSize+pY, pSize+pY};
        gc.fillPolygon(x, y, n);
        x = new double [] {0+pX, 0+pX, pSize+pX, pSize+pX};
        y = new double [] {3*pSize/5+pY, 4*pSize/5+pY, 4*pSize/5+pY, 3*pSize/5+pY};
        gc.fillPolygon(x, y, n);
    }
}
