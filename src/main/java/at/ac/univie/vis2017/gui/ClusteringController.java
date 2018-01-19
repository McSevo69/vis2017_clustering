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
import static java.lang.Thread.sleep;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
    @FXML Canvas kmeansCanvasMinor;
    @FXML TitledPane kmeansDataGenPane;
    @FXML Pane kmeansParentPane;
    @FXML Pane kmeansParentPaneMinor;
    @FXML AnchorPane anchorPaneMain;
    @FXML GridPane gridPaneControl;
    @FXML GridPane gridPaneTimeView;
    @FXML ImageView restartAutoModeKmeansImage;
    @FXML ImageView pauseAutoModeKmeansImage;
    @FXML ImageView playAutoModeKmeansImage;
    @FXML ImageView skipToStartImage;
    @FXML ImageView stepBackImage;
    @FXML ImageView stepForwardImage;
    @FXML ImageView restartAutoModeKmeansImageMinor;
    @FXML ImageView pauseAutoModeKmeansImageMinor;
    @FXML ImageView playAutoModeKmeansImageMinor;
    @FXML ImageView skipToStartImageMinor;
    @FXML ImageView stepBackImageMinor;
    @FXML ImageView stepForwardImageMinor;
    @FXML ImageView linkageImage;
    @FXML Spinner<Integer> iterationKmeansSpinner;
    @FXML Spinner<Integer> iterationKmeansSpinnerMinor;
    @FXML Spinner<Integer> kOfKmeansSpinner;
    @FXML Spinner<Integer> kOfKmeansSpinnerMinor;
    @FXML Slider speedKmeansSlider;
    @FXML Slider speedKmeansSliderMinor;
    @FXML Slider pointsKmeansSlider;
    @FXML Slider pointsKmeansSliderMinor;
    @FXML CheckBox centroidPathKmeansCheckBox;
    @FXML CheckBox clusterCentersKmeansCheckBox;
    @FXML CheckBox dataPointsKmeansCheckBox;
    @FXML CheckBox centroidPathKmeansCheckBoxMinor;
    @FXML CheckBox clusterCentersKmeansCheckBoxMinor;
    @FXML CheckBox dataPointsKmeansCheckBoxMinor;
    @FXML ChoiceBox<String> kmeansUpdateStratChoiceBox;
    @FXML ChoiceBox<String> kmeansInitChoiceBox;
    @FXML ChoiceBox<String> kmeansAlgorithmChoiceBox;
    @FXML ChoiceBox<String> kmeansUpdateStratChoiceBoxMinor;
    @FXML ChoiceBox<String> kmeansInitChoiceBoxMinor;
    @FXML ChoiceBox<String> kmeansAlgorithmChoiceBoxMinor;
    @FXML Button computeButton;
    @FXML Button computeButtonMinor;
    @FXML Button loadFromFileMinorButton;
    @FXML Button randomDataMinorButton;
    @FXML GridPane smallMultiplesGridPane;
    @FXML Label stepTwoLabel;

    private Main application;
    private ArrayList<Point> initialStatePoints;
    private ArrayList<Point> initialStatePointsMinor;
    private VisualizerFX visualizer;
    private VisualizerFX visualizerMinor;
    private KMEANS kmeansAlgorithm;
    private KMEANS kmeansAlgorithmMinor;
    
    private String strategy;
    private String algorithm;
    private String initMode;
    private ArrayList<Point> clusterCenters;
    private ArrayList<ArrayList<String>> hashedPoints;
    private ArrayList<ArrayList<ArrayList<String>>> hashedCenters;
    
    private int maxIterationsMain;
    private int maxIterationsMinor;
    
    private String strategyMinor;
    private String algorithmMinor;
    private String initModeMinor;
    private ArrayList<Point> clusterCentersMinor;

    private int kOfKmeans = 3;
    private int kOfKmeansMinor = 3;
    private boolean isLinked = true;           
    private boolean isComputedMinor = false;
    private boolean isComputed = false;
    private String distanceFunction;
    private int autoModeSpeedMain = 300;
    private int autoModeSpeedMinor = 300;
    private Thread autoModeMainThread;
    private Thread autoModeMinorThread;
    private final int MAX_DURATION = 2500;
    private List<VisualizerFX> multiples;
    private String labelTwoText = "while convergence == false\n" +
                            "    for p : points\n" +
                            "        for c : centroids\n" +
                            "            if d(p,c) minimal\n" +
                            "                set_c.add(p)\n" +
                            "            endif\n" +
                            "        endfor\n" +
                            "    endfor\n" +
                            "    \n" +
                            "    for c : centroids\n" +
                            "        c.position = mean (set_c)\n" +
                            "    endfor\n" +
                            "    \n" +
                            "    if set_c == set_c0 for all c\n" +
                            "        convergence = true\n" +
                            "    endif\n" +
                            "endwhile";

    Logger logger = LogManager.getLogger(ClusteringController.class);

    public ClusteringController() {
        this.visualizer = new VisualizerFX();
        this.visualizerMinor = new VisualizerFX();
        this.initialStatePoints = new ArrayList<>();
        this.clusterCenters = new ArrayList<>();
        this.autoModeMainThread = new Thread();
        this.autoModeMinorThread = new Thread();
        this.multiples = new ArrayList<>();
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
            Data data = new Data(new Data(initialStatePoints.size(), Algorithm.KMEANS, buffer));
            visualizer.setData(data);
            visualizer.setSpeed(pointsKmeansSlider.valueProperty().intValue());
            //visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
        
            iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
            logger.debug("Iteration Spinner updated. New value: " + 0);
            
            visualizer.clearCanvas();
            visualizer.drawIterationData();
            
            logger.debug("Controls are deactivated.");
            deactivateControls();
            computeButton.setDisable(false);
            isComputed = false;
            
            if (isLinked) {
                this.initialStatePointsMinor = new ArrayList<Point>();
                
                for (Point p : initialStatePoints)
                    initialStatePointsMinor.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));
                
                Data datCopy = new Data(data);
                visualizerMinor.setData(datCopy);
                visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());
 
                iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
                logger.debug("IterationSpinnerMinor updated. New value: " + 0);

                visualizerMinor.clearCanvas();
                visualizerMinor.drawIterationData();

                logger.debug("ControlsMinor are deactivated.");
                deactivateControlsMinor();
            }
            
        } catch (IOException ex) {
            logger.error("Cannot load file!\n" + ex);
        }
        
    }
    
    public void loadKmeansFileMinor() {
        String kmeansFile = loadFromFile();
        try {
            this.initialStatePointsMinor = getDataFromTxt(kmeansFile);
            
            ArrayList<Point> buffer = new ArrayList<>();
            for (Point p : initialStatePointsMinor)
                buffer.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));
            
            logger.debug("File loaded successfully");
            Data data = new Data(new Data(initialStatePointsMinor.size(), Algorithm.KMEANS, buffer));
            visualizerMinor.setData(data);
            visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());
            //visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
        
            iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
            logger.debug("Iteration Spinner updated. New value: " + 0);
            
            visualizerMinor.clearCanvas();
            visualizerMinor.drawIterationData();
            
            logger.debug("Controls are deactivated.");
            deactivateControlsMinor();
            randomDataMinorButton.setDisable(false);
            loadFromFileMinorButton.setDisable(false);
            computeButtonMinor.setDisable(false);
            isComputedMinor = false;
            
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
        computeButton.setDisable(true);
        restartAutoModeKmeansImage.setDisable(true);
        restartAutoModeKmeansImage.setOpacity(0.6);
        pauseAutoModeKmeansImage.setDisable(true);
        pauseAutoModeKmeansImage.setOpacity(0.6);
        playAutoModeKmeansImage.setDisable(true);
        playAutoModeKmeansImage.setOpacity(0.6);
        deactivateFilters();
    }
    
    public void activateControls() {
        //skipToStartImage.setDisable(false);
        //skipToStartImage.setOpacity(1);
        //stepBackImage.setDisable(false);
        //stepBackImage.setOpacity(1);
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1);
        iterationKmeansSpinner.setDisable(false);
        computeButton.setDisable(false);
        restartAutoModeKmeansImage.setDisable(false);
        restartAutoModeKmeansImage.setOpacity(1);
        pauseAutoModeKmeansImage.setDisable(false);
        pauseAutoModeKmeansImage.setOpacity(1);
        playAutoModeKmeansImage.setDisable(false);
        playAutoModeKmeansImage.setOpacity(1);
        activateFilters();
    }
    
    public void deactivateControlsMinor() {
        skipToStartImageMinor.setDisable(true);
        skipToStartImageMinor.setOpacity(0.6);
        stepBackImageMinor.setDisable(true);
        stepBackImageMinor.setOpacity(0.6);
        stepForwardImageMinor.setDisable(true);
        stepForwardImageMinor.setOpacity(0.6);
        iterationKmeansSpinnerMinor.setDisable(true);
        computeButtonMinor.setDisable(true);
        randomDataMinorButton.setDisable(true);
        loadFromFileMinorButton.setDisable(true);        
        restartAutoModeKmeansImageMinor.setDisable(true);
        restartAutoModeKmeansImageMinor.setOpacity(0.6);
        pauseAutoModeKmeansImageMinor.setDisable(true);
        pauseAutoModeKmeansImageMinor.setOpacity(0.6);
        playAutoModeKmeansImageMinor.setDisable(true);
        playAutoModeKmeansImageMinor.setOpacity(0.6);
    }
    
    public void clearSmallMultiples() {
        multiples.clear();
        smallMultiplesGridPane.getChildren().clear();
    }
    
    
    public void updateSmallMultiples(Data dat) {
        
        clearSmallMultiples();
        
        int maxIterations = dat.getIterations();
        
        int columnsPerRow = 4;
        int fullRows = maxIterations/columnsPerRow;
        int res = maxIterations%columnsPerRow;
        
        for (int i=0; i<fullRows; i++) {
            for (int j=0; j<columnsPerRow; j++) {
                multiples.add(new VisualizerFX());
                Pane newPane = new Pane();
                newPane.getChildren().add(new Canvas());
                smallMultiplesGridPane.add(newPane, j, i);
                smallMultiplesGridPane.setMargin(newPane, new Insets(5,5,5,5));
                smallMultiplesGridPane.setMinHeight(180);
                Node children = smallMultiplesGridPane.getChildren().get(i*columnsPerRow+j);
                children.setOnMouseClicked(evt -> {
                    if (isComputed) {
                        int rowIndex = smallMultiplesGridPane.getRowIndex(children);
                        int columnIndex = smallMultiplesGridPane.getColumnIndex(children);
                        iterationKmeansSpinner.valueFactoryProperty().get().setValue(rowIndex*columnsPerRow+columnIndex);
                    }
                });
                Pane parent = (Pane) smallMultiplesGridPane.getChildren().get(i*columnsPerRow+j);
                multiples.get(i*columnsPerRow+j).bindProperties( (Canvas) newPane.getChildren().get(0), parent);
            }
        }
        
        for (int i=0; i<res; i++) {
            multiples.add(new VisualizerFX());
            Pane newPane = new Pane();
            newPane.getChildren().add(new Canvas());
            smallMultiplesGridPane.add(newPane, i, fullRows);
            smallMultiplesGridPane.setMargin(newPane, new Insets(5,5,5,5));
            smallMultiplesGridPane.setMinHeight(180);
            Node children = smallMultiplesGridPane.getChildren().get(columnsPerRow*fullRows+i);
            children.setOnMouseClicked(evt -> {
                if (isComputed) {
                    int rowIndex = smallMultiplesGridPane.getRowIndex(children);
                    int columnIndex = smallMultiplesGridPane.getColumnIndex(children);
                    iterationKmeansSpinner.valueFactoryProperty().get().setValue(rowIndex*columnsPerRow+columnIndex);
                }
            });
            Pane parent = (Pane) smallMultiplesGridPane.getChildren().get(columnsPerRow*fullRows+i);
            multiples.get(columnsPerRow*fullRows+i).bindProperties( (Canvas) newPane.getChildren().get(0), parent);
        }
                        
        for (int i=0; i<maxIterations; i++) {
            multiples.get(i).setData(dat);
            multiples.get(i).setIteration(i);
            multiples.get(i).setAfterComputation();
            multiples.get(i).setShowData(dataPointsKmeansCheckBox.selectedProperty().get());
            multiples.get(i).setShowCenters(clusterCentersKmeansCheckBox.selectedProperty().get());
            multiples.get(i).setShowPaths(centroidPathKmeansCheckBox.selectedProperty().get());
            multiples.get(i).draw();         
        }
        
    }
    
    public void activateControlsMinor() {
        skipToStartImageMinor.setDisable(false);
        skipToStartImageMinor.setOpacity(1);
        stepBackImageMinor.setDisable(false);
        stepBackImageMinor.setOpacity(1);
        //stepForwardImageMinor.setDisable(false);
        //stepForwardImageMinor.setOpacity(1);
        iterationKmeansSpinnerMinor.setDisable(false);
        computeButtonMinor.setDisable(false);
        randomDataMinorButton.setDisable(false);
        loadFromFileMinorButton.setDisable(false);
        restartAutoModeKmeansImageMinor.setDisable(false);
        restartAutoModeKmeansImageMinor.setOpacity(1);
        pauseAutoModeKmeansImageMinor.setDisable(false);
        pauseAutoModeKmeansImageMinor.setOpacity(1);
        playAutoModeKmeansImageMinor.setDisable(false);
        playAutoModeKmeansImageMinor.setOpacity(1);
        activateFiltersMinor();
    }
    
    public void activateFilters() {
        centroidPathKmeansCheckBox.setDisable(false);
        clusterCentersKmeansCheckBox.setDisable(false);
        dataPointsKmeansCheckBox.setDisable(false);  
    }
                
    public void activateFiltersMinor() {
        centroidPathKmeansCheckBoxMinor.setDisable(false);
        clusterCentersKmeansCheckBoxMinor.setDisable(false);
        dataPointsKmeansCheckBoxMinor.setDisable(false);     
    }
    
    public void deactivateFilters() {
        centroidPathKmeansCheckBox.setDisable(true);
        clusterCentersKmeansCheckBox.setDisable(true);
        dataPointsKmeansCheckBox.setDisable(true);  
    }
                
    public void deactivateFiltersMinor() {
        centroidPathKmeansCheckBoxMinor.setDisable(true);
        clusterCentersKmeansCheckBoxMinor.setDisable(true);
        dataPointsKmeansCheckBoxMinor.setDisable(true);     
    }
    
    public void restartManualKmeans() {
        visualizer.restart();
        logger.debug("restartKmeans pressed");
        updateKmeansIteration();
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1);
        stepBackImage.setDisable(true);
        stepBackImage.setOpacity(0.6);
        skipToStartImage.setDisable(true);
        skipToStartImage.setOpacity(0.6);
        if (isLinked) restartManualKmeansMinor();
    }
    
    public void restartManualKmeansMinor() {
        visualizerMinor.restart();
        logger.debug("restartKmeansMinor pressed");
        updateKmeansIterationMinor();
        if (!isLinked) {
            stepForwardImageMinor.setDisable(false);
            stepForwardImageMinor.setOpacity(1);
        }        
        stepBackImageMinor.setDisable(true);
        stepBackImageMinor.setOpacity(0.6);
        skipToStartImageMinor.setDisable(true);
        skipToStartImageMinor.setOpacity(0.6);
    }
    
    public void updateKmeansIteration(){
        iterationKmeansSpinner.valueFactoryProperty().get().setValue(visualizer.getIteration());
        logger.debug("Iteration Spinner updated. New value: " + visualizer.getIteration());
    }
    
    public void updateKmeansIterationMinor(){
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(visualizerMinor.getIteration());
        logger.debug("Iteration Spinner for Minor updated. New value: " + visualizerMinor.getIteration());
    }
    
    public void iterateKmeans() {
/*
        if (initMode.equals("I'll choose")) {
            if (clusterCenters.size() == kOfKmeans) {
                this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
            }
        } else if (initMode.equals("Farthest")) {
            this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
        } else if (initMode.equals("Random")) {
            this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
        } else {
            
        }
*/
        this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
        Data dat = kmeansAlgorithm.clusterData();

        maxIterationsMain = dat.getIterations();
        hashedCenters = visualizer.hashCenters(dat);

        visualizer.setData(dat);
        visualizer.setAfterComputation();
        visualizer.setSpeed(pointsKmeansSlider.valueProperty().intValue());
        logger.debug("iterateKmeans pressed");
        logger.debug("Controls are activated.");
        logger.debug("Iteration Spinner updated. New value: " + 0);
        activateControls();
        restartManualKmeans();
        activateFilters();
        iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
        
        isComputed = true;
        updateSmallMultiples(dat);
        skipToStartImage.setDisable(true);
        skipToStartImage.setOpacity(0.6);
        
        if (isLinked) {
            Data datCopy = new Data(dat);
            maxIterationsMinor = datCopy.getIterations();
            visualizerMinor.setData(datCopy);
            visualizerMinor.setAfterComputation();
            visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());
            iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
            logger.debug("IterationSpinnerMinor updated. New value: " + 0);
            restartManualKmeansMinor();
            activateFiltersMinor();
            isComputedMinor = true;
        }
    }
    
    public void iterateKmeansMinor() {
        this.kmeansAlgorithmMinor = new KMEANS(kOfKmeansMinor, 100, initialStatePointsMinor);
        Data dat = kmeansAlgorithmMinor.clusterData();
        maxIterationsMinor = dat.getIterations();
        visualizerMinor.setData(dat);
        visualizerMinor.setAfterComputation();
        visualizerMinor.setSpeed(pointsKmeansSliderMinor.valueProperty().intValue());
        logger.debug("iterateKmeans pressed");
        logger.debug("Controls are activated.");
        logger.debug("Iteration Spinner updated. New value: " + 0);
        if (!isLinked) activateControlsMinor();
        restartManualKmeansMinor();
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
        isComputedMinor = true;
        skipToStartImageMinor.setDisable(true);
        skipToStartImageMinor.setOpacity(0.6);
    }
    
    public void stepBackKmeans() {
        visualizer.stepback();
        logger.debug("stepBackKmeans pressed");
        updateKmeansIteration();
               
        if (isLinked) stepBackKmeansMinor();
        
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1);
        
        if (visualizer.getIteration() == 0) {
            stepBackImage.setDisable(true);
            stepBackImage.setOpacity(0.6);
        }
    }
    
    public void stepBackKmeansMinor() {
        visualizerMinor.stepback();
        logger.debug("stepBackKmeansMinor pressed");
        updateKmeansIterationMinor();
        stepForwardImageMinor.setDisable(false);
        stepForwardImageMinor.setOpacity(1);
        
        if (visualizerMinor.getIteration() == 0) {
            stepBackImageMinor.setDisable(true);
            stepBackImageMinor.setOpacity(0.6);
        }
    }
    
    public void forwardKmeans() throws IndexOutOfBoundsException {
        logger.debug("forwardKmeans pressed");
                
        stepBackImage.setDisable(false);
        stepBackImage.setOpacity(1);
        skipToStartImage.setDisable(false);
        skipToStartImage.setOpacity(1);
        
        if (visualizer.getIteration() < maxIterationsMain-1){
            visualizer.iterate();
            updateKmeansIteration();
        }
        
        if (isLinked) forwardKmeansMinor();

        if (visualizer.getIteration()+1 >= maxIterationsMain) {
            stepForwardImage.setDisable(true);
            stepForwardImage.setOpacity(0.6);
            throw new IndexOutOfBoundsException();
        }       
        
    }
    
    public void forwardKmeansMinor() throws IndexOutOfBoundsException {
        logger.debug("forwardKmeansMinor pressed");
        if (!isLinked) {
            stepBackImageMinor.setDisable(false);
            stepBackImageMinor.setOpacity(1);
            skipToStartImageMinor.setDisable(false);
            skipToStartImageMinor.setOpacity(1);
        }
        
        if (visualizerMinor.getIteration() < maxIterationsMinor-1){
            visualizerMinor.iterate();
            updateKmeansIterationMinor();
        }
         
        if (visualizerMinor.getIteration()+1 >= maxIterationsMinor) {
            stepForwardImageMinor.setDisable(true);
            stepForwardImageMinor.setOpacity(0.6);
            throw new IndexOutOfBoundsException();
        }
    }
    
    public void startAutoModeMainThread(){
        autoModeMainThread = new Thread(){
            @Override
            public void run(){
                logger.trace("in autothread run");
                deactivateStartButtonAutoModeMain();
                boolean isRunning = true;
                while (isRunning) {
                    if (visualizer.getMode() == Mode.AUTO) {
                        try {
                            forwardKmeans();
                            sleep(autoModeSpeedMain);
                        } catch (IndexOutOfBoundsException e) {
                            autoModePauseKmeans();
                            isRunning = false;                        
                        } catch (InterruptedException ex) {
                             logger.debug(ex.getMessage());
                             isRunning = false; 
                        }
                    } else {
                        isRunning = false;
                    }                
                }
                activateStartButtonAutoModeMain();
            }
        };
        autoModeMainThread.start();
    }
    
    public void startAutoModeMinorThread(){
        autoModeMinorThread = new Thread(){
            @Override
            public void run(){
                logger.trace("in autothread run");
                deactivateStartButtonAutoModeMinor();
                boolean isRunning = true;
                while (isRunning) {
                    if (visualizerMinor.getMode() == Mode.AUTO) {
                        try {
                            forwardKmeansMinor();
                            sleep(autoModeSpeedMinor);
                        } catch (IndexOutOfBoundsException e) {
                            autoModePauseKmeansMinor();
                            isRunning = false;                        
                        } catch (InterruptedException ex) {
                             logger.debug(ex.getMessage());
                             isRunning = false; 
                        }
                    } else {
                        isRunning = false;
                    }                 
                } 
                activateStartButtonAutoModeMinor();
            }
        };
        autoModeMinorThread.start();
    }
    
    public void activateStartButtonAutoModeMain(){
        playAutoModeKmeansImage.setOpacity(1);
        playAutoModeKmeansImage.setDisable(false);
    }
    
    public void deactivateStartButtonAutoModeMain(){
        playAutoModeKmeansImage.setOpacity(0.5);
        playAutoModeKmeansImage.setDisable(true);
    }
    
    public void activateStartButtonAutoModeMinor(){
        playAutoModeKmeansImageMinor.setOpacity(1);
        playAutoModeKmeansImageMinor.setDisable(false);
    }
    
    public void deactivateStartButtonAutoModeMinor(){
        playAutoModeKmeansImageMinor.setOpacity(0.5);
        playAutoModeKmeansImageMinor.setDisable(true);
    }
    
    public void autoModePlayKmeans() {
        visualizer.setMode(Mode.AUTO);
        logger.debug("setMode.AUTO pressed");
        startAutoModeMainThread();      
    }
    
    public void autoModePlayKmeansMinor() {
        visualizerMinor.setMode(Mode.AUTO);
        logger.debug("setMode.AUTO pressed");
        startAutoModeMinorThread();        
    }
    
    public void autoModePauseKmeans() {
        visualizer.setMode(Mode.MANUAL);
        logger.debug("setMode.MANUAL pressed");
        if (isLinked) autoModePauseKmeansMinor();
    }
    
    public void autoModePauseKmeansMinor() {
        visualizerMinor.setMode(Mode.MANUAL);
        logger.debug("setMode.MANUAL pressed");
    }
    
    public void autoModeRestartKmeans() {
        visualizer.restart();
        visualizer.setMode(Mode.AUTO);
        logger.debug("autoMode restarted");
        updateKmeansIteration();
        autoModePlayKmeans();
        if (isLinked) autoModeRestartKmeansMinor();
    }
    
    public void autoModeRestartKmeansMinor() {
        visualizerMinor.restart();
        visualizerMinor.setMode(Mode.AUTO);
        logger.debug("autoMode (minor) restarted");
        updateKmeansIterationMinor();
        autoModePlayKmeansMinor();
    }
    
    public void randomDataKmeansPressed() {
        //this.initialStatePoints  = createRandomData(500, 140.0, 140.0);
        this.initialStatePoints  = createGaussianRandomData(90);
        logger.debug("Random data generated");
        Data dat = new Data(initialStatePoints.size(), Algorithm.KMEANS, initialStatePoints);
        visualizer.setData(dat);
        hashedPoints = visualizer.hashPoints(dat);
        visualizer.setSpeed(pointsKmeansSlider.valueProperty().intValue());
        //visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
        
        iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
        logger.debug("Iteration Spinner updated. New value: " + 0);
        
        visualizer.clearCanvas();
        visualizer.drawIterationData();
        
        logger.debug("Controls are deactivated.");
        deactivateControls();
        computeButton.setDisable(false);
        isComputed = false;
        
        clusterCenters.clear();
        
        if (isLinked) {
            this.initialStatePointsMinor = new ArrayList<Point>();
                
            for (Point p : initialStatePoints)
                initialStatePointsMinor.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));
                
            Data datCopy = new Data(dat);
            visualizerMinor.setData(datCopy);
            visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());

            iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
            logger.debug("IterationSpinnerMinor updated. New value: " + 0);

            visualizerMinor.clearCanvas();
            visualizerMinor.drawIterationData();

            logger.debug("ControlsMinor are deactivated.");
            deactivateControlsMinor();
        }
        
    }
    
    public void randomDataKmeansMinorPressed() {
        //this.initialStatePoints  = createRandomData(500, 140.0, 140.0);
        this.initialStatePointsMinor  = createGaussianRandomData(90);
        logger.debug("Random data generated");
        Data dat = new Data(initialStatePointsMinor.size(), Algorithm.KMEANS, initialStatePointsMinor);
        visualizerMinor.setData(dat);
        visualizerMinor.setSpeed(pointsKmeansSliderMinor.valueProperty().intValue());
        //visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
        
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
        logger.debug("Iteration Spinner Minor updated. New value: " + 0);
        
        visualizerMinor.clearCanvas();
        visualizerMinor.drawIterationData();
        
        logger.debug("Controls are deactivated.");
        deactivateControlsMinor();
        deactivateFiltersMinor();
        randomDataMinorButton.setDisable(false);
        loadFromFileMinorButton.setDisable(false);
        computeButtonMinor.setDisable(false);
        isComputedMinor = false;
                
    }
    
    public void changeNavigationLinkage() {
        logger.debug("ChangeNavigation called");
        if (isLinked) {
            linkageImage.setImage(new Image("images/Broken_Link_32px.png"));
            activateControlsMinor();
            if (!isComputedMinor) { //this could be done in a nicer way
                computeButtonMinor.setDisable(true);
                skipToStartImageMinor.setDisable(true);
                skipToStartImageMinor.setOpacity(0.6);
                stepBackImageMinor.setDisable(true);
                stepBackImageMinor.setOpacity(0.6);
                stepForwardImageMinor.setDisable(true);
                stepForwardImageMinor.setOpacity(0.6);
                iterationKmeansSpinnerMinor.setDisable(true);
                restartAutoModeKmeansImageMinor.setDisable(true);
                restartAutoModeKmeansImageMinor.setOpacity(0.6);
                pauseAutoModeKmeansImageMinor.setDisable(true);
                pauseAutoModeKmeansImageMinor.setOpacity(0.6);
                playAutoModeKmeansImageMinor.setDisable(true);
                playAutoModeKmeansImageMinor.setOpacity(0.6);
                deactivateFiltersMinor();                
            }
            isLinked = false;            
        } else {
            linkageImage.setImage(new Image("images/Link_32px.png"));
            deactivateControlsMinor();
            isLinked = true;            
        }        
    }
    
    public int invertValues(int speed, int min, int max) {
        //inverting values. 100 -> 1, 1 -> 100
        int span = max+min;
        return (span-speed)%span;
    }
    
    public void updateAutoSpeedMain(int speed) {
        int invertedSpeed = invertValues(speed, 10, 80);
        autoModeSpeedMain = MAX_DURATION*invertedSpeed/100;         
    }
    
    public void updateAutoSpeedMinor(int speed) {
        int invertedSpeed = invertValues(speed, 10, 80);       
        autoModeSpeedMinor = MAX_DURATION*invertedSpeed/100;  
    }
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                
        restartAutoModeKmeansImage.setImage(new Image("images/Restart_32px.png"));
        pauseAutoModeKmeansImage.setImage(new Image("images/Pause_32px.png"));
        playAutoModeKmeansImage.setImage(new Image("images/Play_32px.png"));
        skipToStartImage.setImage(new Image("images/Skip to Start_32px.png"));
        stepBackImage.setImage(new Image("images/Back_32px.png"));
        stepForwardImage.setImage(new Image("images/Forward_32px.png"));
        linkageImage.setImage(new Image("images/Link_32px.png"));
        
        stepTwoLabel.setText(labelTwoText);
        
        iterationKmeansSpinner.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && newValue < maxIterationsMain) {
                kmeansCanvasMain = new Canvas(420, 420);
                kmeansCanvasMain.setCache(false);
                kmeansCanvasMain.setCacheHint(CacheHint.SPEED);
                kmeansParentPane.getChildren().clear();
                kmeansParentPane.getChildren().add(kmeansCanvasMain);
                visualizer.bindProperties(kmeansCanvasMain, kmeansParentPane);
                skipToStartImage.setDisable(false);
                skipToStartImage.setOpacity(1);
                stepBackImage.setDisable(false);
                stepBackImage.setOpacity(1);
                stepForwardImage.setDisable(false);
                stepForwardImage.setOpacity(1);
                visualizer.setIteration(newValue);
                logger.debug("Iteration set to " + newValue);        
                if (visualizer.getIteration()+1 >= maxIterationsMain) {
                    stepForwardImage.setDisable(true);
                    stepForwardImage.setOpacity(0.6);
                } else if (visualizer.getIteration() == 0) {
                    stepBackImage.setDisable(true);
                    stepBackImage.setOpacity(0.6);
                    skipToStartImage.setDisable(true);
                    skipToStartImage.setOpacity(0.6);
                }
            }
        });
        
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && newValue < maxIterationsMinor) {
                kmeansCanvasMinor = new Canvas(420, 420);
                kmeansCanvasMinor.setCache(false);
                kmeansCanvasMinor.setCacheHint(CacheHint.SPEED);
                kmeansParentPaneMinor.getChildren().clear();
                kmeansParentPaneMinor.getChildren().add(kmeansCanvasMinor);
                visualizerMinor.bindProperties(kmeansCanvasMinor, kmeansParentPaneMinor);
                visualizerMinor.setIteration(newValue);
                logger.debug("IterationMinor set to " + newValue);
            }
        });
        
        kOfKmeansSpinner.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            kOfKmeans = newValue;
            logger.debug("kOfKmeans set to " + newValue);
        });
        
        kOfKmeansSpinnerMinor.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            kOfKmeansMinor = newValue;
            logger.debug("kOfKmeansMinor set to " + newValue);
        });
                
        pointsKmeansSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            visualizer.setSpeed(newValue.intValue());
            logger.debug("new speed set: " + newValue.intValue());
            if (isLinked) pointsKmeansSliderMinor.valueProperty().setValue(newValue.intValue());
        });
        
        pointsKmeansSliderMinor.valueProperty().addListener((observable, oldValue, newValue) -> {
            visualizerMinor.setSpeed(newValue.intValue());
            logger.debug("Minor: new speed set: " + newValue.intValue());
        });
        
        speedKmeansSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateAutoSpeedMain(newValue.intValue());
            if (isLinked) speedKmeansSliderMinor.valueProperty().setValue(newValue.intValue());
        });
        
        speedKmeansSliderMinor.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateAutoSpeedMinor(newValue.intValue());
        });
        
        
        //Checkboxes
        centroidPathKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show centroid paths: " + newValue.toString());
            visualizer.setShowPaths(newValue);
            for (VisualizerFX vis : multiples) vis.setShowPaths(newValue);
        });
        
        centroidPathKmeansCheckBoxMinor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show centroid paths Minor: " + newValue.toString());
            visualizerMinor.setShowPaths(newValue);
        });
        
        clusterCentersKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show cluster centers: " + newValue.toString());
            visualizer.setShowCenters(newValue);
            for (VisualizerFX vis : multiples) vis.setShowCenters(newValue);
        });
        
        clusterCentersKmeansCheckBoxMinor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show cluster centers Minor " + newValue.toString());
            visualizerMinor.setShowCenters(newValue);
        });
        
        dataPointsKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show data points: " + newValue.toString());
            visualizer.setShowData(newValue);
            for (VisualizerFX vis : multiples) vis.setShowData(newValue);
        });
        
        dataPointsKmeansCheckBoxMinor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show data points Minor: " + newValue.toString());
            visualizerMinor.setShowData(newValue);
        });
        
        kmeansUpdateStratChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Update-strat (Main) set to " + newValue);
            this.strategy = newValue;
        });
        
        kmeansInitChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Init-strat (Main) set to " + newValue);
            this.initMode = newValue;
        });
        
        kmeansAlgorithmChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Algorithm (Main) set to " + newValue);
            this.algorithm = newValue;
        });
        
        kmeansUpdateStratChoiceBoxMinor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Update-strat (Minor) set to " + newValue);
            this.strategyMinor = newValue;
        });
        
        kmeansInitChoiceBoxMinor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Init-strat (Minor) set to " + newValue);
            this.initModeMinor = newValue;
        });
        
        kmeansAlgorithmChoiceBoxMinor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Algorithm (Minor) set to " + newValue);
            this.algorithmMinor = newValue;
        });
                
        kmeansUpdateStratChoiceBox.getSelectionModel().selectFirst();
        kmeansInitChoiceBox.getSelectionModel().selectFirst();
        kmeansAlgorithmChoiceBox.getSelectionModel().selectFirst();
        
        kmeansUpdateStratChoiceBoxMinor.getSelectionModel().selectFirst();
        kmeansInitChoiceBoxMinor.getSelectionModel().selectFirst();
        kmeansAlgorithmChoiceBoxMinor.getSelectionModel().selectFirst();
                       
        //visualizer.drawBorder(gc);
        visualizer.bindProperties(kmeansCanvasMain, kmeansParentPane);
        visualizerMinor.bindProperties(kmeansCanvasMinor, kmeansParentPaneMinor);
//        visualizer.drawShapes(gc);


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
        deactivateControlsMinor();
        deactivateFiltersMinor();
                
        Tooltip mousePositionToolTip = new Tooltip("");
        Tooltip tooManyCentroidsTooltip = new Tooltip("");

        kmeansParentPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //TODO
                boolean dummy = false;
                int x = (int) event.getX() - 1;
                int y = (int) event.getY() - 1;
                String msg = "";
                
                logger.debug("mouse on: " + x + ":" + y);
                
                try {
                    msg = hashedCenters.get(visualizer.getIteration()).get(x).get(y);
                    logger.debug(msg);
                    dummy = (msg != null);
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
                
                try {
                    if (!dummy) {
                        msg = hashedPoints.get(x).get(y);
                        dummy = (msg != null);
                    }
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
                
                if (dummy) {
                    mousePositionToolTip.setText(msg);

                    Node node = (Node) event.getSource();
                    mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
                } else {
                    mousePositionToolTip.hide();
                }
            }

        });
        
        kmeansParentPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePositionToolTip.hide();
                tooManyCentroidsTooltip.hide();
            }
        });        
        
        kmeansParentPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point p = new Point(event.getX(), event.getY());
                p.setCenterPointTrue();
                if (!initMode.equals("I'll choose")) {
                    tooManyCentroidsTooltip.setText("Set initialization mode to \"I'll choose\"\n to set centroids manually");
                    tooManyCentroidsTooltip.show((Node) event.getSource(), event.getScreenX() + 50, event.getScreenY());
                } else if (clusterCenters.size() >= kOfKmeans) {
                    tooManyCentroidsTooltip.setText("Set k higher to set more centroids!");
                    tooManyCentroidsTooltip.show((Node) event.getSource(), event.getScreenX() + 50, event.getScreenY());
                } else {
                    clusterCenters.add(p);

                    kmeansCanvasMain.getGraphicsContext2D().setFill(Color.LIGHTGRAY);
                    kmeansCanvasMain.getGraphicsContext2D().fillRect(p.getX(), p.getY(), 9, 9);
                    
                    kmeansCanvasMain.getGraphicsContext2D().setStroke(Color.BLACK);
                    kmeansCanvasMain.getGraphicsContext2D().strokeRect(p.getX(), p.getY(), 9, 9);
                }

//                visualizer.drawPoint(p, 1);
            }
        });        
    }    
}
