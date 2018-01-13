/*
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package at.ac.univie.vis2017.gui;

import at.ac.univie.vis2017.clustering_algorithms.KMEANS;
import at.ac.univie.vis2017.util.Algorithm;
import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;
import at.ac.univie.vis2017.util.RandomGaussian;
import at.ac.univie.vis2017.visualizer.IVisualizer.Mode;
import at.ac.univie.vis2017.visualizer.VisualizerFX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClusteringController
 */
public class ClusteringController extends AnchorPane implements Initializable {
    
    //@FXML TabPane tabPaneMenu;    
    //@FXML Tab kmeansTab;       
    //@FXML Tab dbscanTab;     
    //@FXML Tab opticsTab;  
    @FXML SwingNode kmeansNode;
    @FXML Canvas kmeansCanvasMain;
    @FXML Canvas kmeansCanvasStart;
    @FXML Canvas kmeansCanvasMiddle;
    @FXML Canvas kmeansCanvasEnd;
    @FXML TitledPane kmeansDataGenPane;
    @FXML Pane kmeansParentPane;
    @FXML AnchorPane anchorPaneMain;
    @FXML GridPane gridPaneControl;
    @FXML GridPane gridPaneTimeView;
    @FXML ImageView restartAutoModeKmeansImage;
    @FXML ImageView pauseAutoModeKmeansImage;
    @FXML ImageView playAutoModeKmeansImage;
    @FXML ImageView skipToStartImage;
    @FXML ImageView stepBackImage;
    @FXML ImageView stepForwardImage;
    //@FXML ImageView restartManualModeKMeansImage;
    //@FXML ImageView stepBackManualModeKMeansImage;
    //@FXML ImageView iterateManualModeKMeansImage;
    @FXML Spinner<Integer> iterationKmeansSpinner;
    @FXML Spinner<Integer> kOfKmeansSpinner;
    @FXML Slider speedKmeansSlider;
    @FXML CheckBox centroidPathKmeansCheckBox;
    @FXML CheckBox clusterCentersKmeansCheckBox;
    @FXML CheckBox dataPointsKmeansCheckBox;
    @FXML CheckBox voronoiLinesKmeansCheckBox;
    @FXML ChoiceBox kmeansUpdateStratChoiceBox;
    @FXML ChoiceBox kmeansInitChoiceBox;

    private Main application;
    private ArrayList<Point> initialStatePoints;
    private VisualizerFX visualizer;
    private KMEANS kmeansAlgorithm;
    private int kOfKmeans = 3;
    
    Logger logger = LogManager.getLogger(ClusteringController.class);

    public ClusteringController() {
        this.visualizer = new VisualizerFX();
        this.initialStatePoints = new ArrayList<>();
    }
    
    public void setApp(Main application){
        this.application = application;
    }
    
    public ArrayList<Point> createGaussianRandomData(int nrObservations) {
        ArrayList<Point> data = new ArrayList<>();
        
        double meanX, meanY; 
        double variance;        
        RandomGaussian rg = new RandomGaussian();
        Random r = new Random();
        
        int nrClusters = (int) (3 + (8 - 3) * r.nextDouble());
        for (int i = 0; i < nrClusters; i++) {
            r = new Random();
            meanX = 30 + (120 - 30) * r.nextDouble();
            meanY = 30 + (120 - 30) * r.nextDouble();
            variance = 6 + (10 - 6) * r.nextDouble();
            for (int j = 0; j < nrObservations; j++) {
                double randomValueX = rg.getGaussian(meanX, variance);
                double randomValueY = rg.getGaussian(meanY, variance);
                data.add(new Point(randomValueX, randomValueY));
            }
        }
        
        return data;
    }

    public ArrayList<Point> createRandomData(int nrObservations, double boundsX, double boundsY) {

        // array list for data points
        ArrayList<Point> data = new ArrayList<>();

        // extent[0]=minX; extent[1]=maxX; extent[2]=minY; extent[3]=maxY;
        ArrayList<Double> extent = new ArrayList<>();


        // get extent of data to randomly set the initial points
//        extent.add(-1.0 * boundsX);         //minX
        extent.add(0.0);                    //minX
        extent.add(boundsX);                //maxX
//        extent.add(-1.0 * boundsY);         //minY
        extent.add(0.0);                    //minY
        extent.add(boundsY);                //maxY

        for (int i = 0; i < nrObservations; i++) {
            Random r = new Random();

            // create random values within given bounds
            double randomValueX = extent.get(0)  + (extent.get(1)  - extent.get(0) ) * r.nextDouble();
            double randomValueY = extent.get(2)  + (extent.get(3)  - extent.get(2) ) * r.nextDouble();

            // add new point to existing data set
            data.add(new Point(randomValueX, randomValueY));

        }

        return data;
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
    
    // read data from txt-file
    // http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    public ArrayList<Point> getDataFromTxt(String filePath) throws IOException {

        // create ArrayList where Points are saved
        ArrayList<Point> data = new ArrayList<>();

        // create buffered reader
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(" ");
                data.add(new Point(Double.parseDouble(row[1]), Double.parseDouble(row[2])));
            }
            br.close();
        }
        catch (IOException e) {
            //logger.error("ERROR: unable to read file " + filePath);
            e.printStackTrace();
        }

        return data;
    }
    
    public String loadFromFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load dataset from file");
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        chooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Dataset", "*.txt"));
        
        Stage stageOpen = new Stage();
        stageOpen.setScene(new Scene(new Group(), 500, 400));
        File openedDatasetFile = chooser.showOpenDialog(stageOpen);
        String datasetPath = "";
        
        try {
            if (openedDatasetFile.length() > 0) {
                datasetPath = openedDatasetFile.getPath();
            } else {
                datasetPath = "";
                logger.error("Empty file selected");
            }
        } catch (NullPointerException ex) {
            logger.debug("File opening aborted");
        }   
        
        return datasetPath;
    }
    
    public void loadKmeansFile() {
        String kmeansFile = loadFromFile();
        try {
            this.initialStatePoints = getDataFromTxt(kmeansFile);
            
            ArrayList<Point> buffer = new ArrayList<>();
            for (Point p : initialStatePoints)
                buffer.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));
            
            logger.debug("File loaded successfully");
            visualizer.setData(new Data(initialStatePoints.size(), Algorithm.KMEANS, buffer));
            visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasStart.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasMiddle.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasEnd.getGraphicsContext2D(), initialStatePoints);            
                        
            iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
            logger.debug("Iteration Spinner updated. New value: " + 0);
            
            logger.debug("Controls are deactivated.");
            deactivateControls();
            
        } catch (IOException ex) {
            logger.error("Cannot load file!\n" + ex);
        }
        
    }
       
    public void deactivateControls() {
        skipToStartImage.setDisable(true);
        skipToStartImage.setOpacity(0.6);
        stepBackImage.setDisable(true);
        stepBackImage.setOpacity(0.6);
        stepForwardImage.setDisable(true);
        stepForwardImage.setOpacity(0.6);
        iterationKmeansSpinner.setDisable(true);
    }
    
    public void activateControls() {
        skipToStartImage.setDisable(false);
        skipToStartImage.setOpacity(1);
        stepBackImage.setDisable(false);
        stepBackImage.setOpacity(1);
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1);
        iterationKmeansSpinner.setDisable(false);
    }
    
    public void restartManualKmeans() {
        visualizer.restart();
        logger.debug("restartKmeans pressed");
        updateKmeansIteration();
    }
    
    public void updateKmeansIteration(){
        iterationKmeansSpinner.valueFactoryProperty().get().setValue(visualizer.getIteration());
        logger.debug("Iteration Spinner updated. New value: " + visualizer.getIteration());
    }
    
    public void iterateKmeans() {
        this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
        Data dat = kmeansAlgorithm.clusterData();
        visualizer.setData(dat);
        logger.debug("iterateKmeans pressed");
        logger.debug("Controls are activated.");
        activateControls();
        restartManualKmeans();
    }
    
    public void stepBackKmeans() {
        visualizer.stepback();
        logger.debug("stepBackKmeans pressed");
        updateKmeansIteration();
    }
    
    public void forwardKmeans() {
        visualizer.iterate();
        logger.debug("forwardKmeans pressed");
        updateKmeansIteration();
    }
    
    public void autoModePlayKmeans() {
        this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
        Data dat = kmeansAlgorithm.clusterData();
        visualizer.setData(dat);
        visualizer.setMode(Mode.AUTO);
        logger.debug("setMode.AUTO pressed");
    }
    
    public void autoModePauseKmeans() {
        visualizer.setMode(Mode.MANUAL);
        logger.debug("setMode.MANUAL pressed");
    }
    
    public void autoModeRestartKmeans() {
        visualizer.restart();
        visualizer.setMode(Mode.AUTO);
        logger.debug("autoMode restarted");
        updateKmeansIteration();      
    }
    
    public void randomDataKmeansPressed() {
        //this.initialStatePoints  = createRandomData(500, 140.0, 140.0);
        this.initialStatePoints  = createGaussianRandomData(90);
        logger.debug("Random data generated");
        visualizer.setData(new Data(initialStatePoints.size(), Algorithm.KMEANS, initialStatePoints));
        visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);

        iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
        logger.debug("Iteration Spinner updated. New value: " + 0);
        
        logger.debug("Controls are deactivated.");
        deactivateControls();
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                
        restartAutoModeKmeansImage.setImage(new Image("images/Restart_32px.png"));
        pauseAutoModeKmeansImage.setImage(new Image("images/Pause_32px.png"));
        playAutoModeKmeansImage.setImage(new Image("images/Play_32px.png"));
        skipToStartImage.setImage(new Image("images/Skip to Start_32px.png"));
        stepBackImage.setImage(new Image("images/Back_32px.png"));
        stepForwardImage.setImage(new Image("images/Forward_32px.png"));
        
        iterationKmeansSpinner.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                visualizer.setIteration(newValue);
                logger.debug("Iteration set to " + newValue);
            }
        });
        
        kOfKmeansSpinner.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            kOfKmeans = newValue;
            logger.debug("kOfKmeans set to " + newValue);
        });
                
        speedKmeansSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            visualizer.setSpeed(newValue.intValue());
            logger.debug("new speed set: " + newValue.intValue());
        });
        
        
        //Checkboxes
        centroidPathKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show centroid paths: " + newValue.toString());
            visualizer.setShowPaths(newValue);
        });
        
        clusterCentersKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show cluster centers: " + newValue.toString());
            visualizer.setShowCenters(newValue);
        });
        
        dataPointsKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show data points: " + newValue.toString());
            visualizer.setShowData(newValue);
        });
        
        voronoiLinesKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show voronoi: " + newValue.toString());
            visualizer.setShowVornoi(newValue);
        });
        
        kmeansUpdateStratChoiceBox.getSelectionModel().selectFirst();
        kmeansInitChoiceBox.getSelectionModel().selectFirst();
               
        GraphicsContext gc = kmeansCanvasMain.getGraphicsContext2D();
        
        //visualizer.drawBorder(gc);
        visualizer.bindProperties(kmeansCanvasMain, kmeansParentPane, gc);
//        visualizer.drawShapes(gc);

        visualizer.setData(Data.getTestData());
        visualizer.draw();

        //visualizer.drawShapes(kmeansCanvasStart.getGraphicsContext2D());
        //visualizer.drawShapes(kmeansCanvasMiddle.getGraphicsContext2D());
        //visualizer.drawShapes(kmeansCanvasEnd.getGraphicsContext2D());
        
        kmeansParentPane.setStyle("-fx-border-color: black");
        //kmeansParentPane.minWidthProperty().bind(kmeansParentPane.heightProperty());
        //kmeansParentPane.maxWidthProperty().bind(kmeansParentPane.heightProperty());
        //kmeansParentPane.setPrefWidth(kmeansParentPane.getMaxHeight());
        //kmeansParentPane.widthProperty().
        //gridPaneControl.minWidthProperty().bind(kmeansParentPane.heightProperty());

        deactivateControls();

    }
    
}
