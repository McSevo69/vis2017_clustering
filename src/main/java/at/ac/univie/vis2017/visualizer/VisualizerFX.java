/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.univie.vis2017.visualizer;

import at.ac.univie.vis2017.util.Algorithm;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 *
 * @author Severin
 */
public class VisualizerFX implements IVisualizer {
    private int iteration;
    
    public void setAlgorithm (Algorithm algorithm) {
        
    }
    
    public void setIteration () {
        
    }
    
    public int getIteration () {
        return iteration;
    }
    
    public void setSpeed () {
        
    }
    
    public void setMode () {
        
    }
    
    public void setShowPaths () {
        
    }
    
    public void setData () {
        
    }
    
    public void iterate () {
        
    }

    public void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(5);
        
        gc.strokeLine(40, 10, 10, 40);
        
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(10, 60, 30, 30);
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

        canvas.widthProperty().bind(parent.widthProperty());
        canvas.heightProperty().bind(parent.heightProperty());
        canvas.widthProperty().addListener(evt -> drawShapes(gc));
        canvas.heightProperty().addListener(evt -> drawShapes(gc));

    }
}
