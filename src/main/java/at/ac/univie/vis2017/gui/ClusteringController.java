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
import javafx.application.Platform;

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
import javafx.scene.input.MouseDragEvent;
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
    @FXML CheckBox shapesCheckBox;
    @FXML CheckBox shapesCheckBoxMinor;
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
    @FXML Button randomDataButton;
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
    private ArrayList<ArrayList<Point>> hashedPoints;
    private ArrayList<ArrayList<ArrayList<Point>>> hashedCenters;
    private double canvasHeightOnHash;
    private double canvasWidthOnHash;
    private ArrayList<ArrayList<Point>> hashedPointsMinor;
    private ArrayList<ArrayList<ArrayList<Point>>> hashedCentersMinor;
    private double canvasHeightOnHashMinor;
    private double canvasWidthOnHashMinor;
    
    private int maxIterationsMain = 0;
    private int maxIterationsMinor = 0;
    
    private String strategyMinor;
    private String algorithmMinor;
    private String initModeMinor;
    private ArrayList<Point> clusterCentersMinor;

    private int kOfKmeans = 3;
    private int kOfKmeansMinor = 3;
    private boolean isLinked = true;           
    private boolean isComputedMinor = false;
    private boolean isComputed = false;
    private boolean isDisabledComputeButton = true;
    private boolean isDisabledComputeButtonMinor = true;
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
        this.initialStatePointsMinor = new ArrayList<>();
        this.clusterCenters = new ArrayList<>();
        this.clusterCentersMinor = new ArrayList<>();
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
            logger.info("File opening aborted");
        }   
        
        return datasetPath;
    }
    
    public void loadKmeansFile() {
        String kmeansFile = loadFromFile();
        try {
            this.initialStatePoints = getDataFromTxt(kmeansFile);
            
            ArrayList<Point> buffer = new ArrayList<>();
            for (Point p : initialStatePoints)
                buffer.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), -1));
            
            logger.debug("File loaded successfully");
            Data data = new Data(new Data(initialStatePoints.size(), Algorithm.KMEANS, buffer));
            visualizer.setData(data);
            hashedPoints = visualizer.hashPoints(data);
            canvasWidthOnHash = kmeansCanvasMain.getWidth();
            canvasHeightOnHash = kmeansCanvasMain.getHeight();
            visualizer.setSpeed(pointsKmeansSlider.valueProperty().intValue());
        
            iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
            logger.debug("Iteration Spinner updated. New value: " + 0);
            
            visualizer.clearCanvas();
            visualizer.drawIterationData();
            
            //logger.debug("Controls are deactivated.");
            deactivateControls();
            //TODO check if I'll choose is set
            if (!initMode.equals("I'll choose")) fakeActivateComputeButton();
            isComputed = false;
            pointsKmeansSlider.setDisable(false);
            
            if (isLinked) {
                this.initialStatePointsMinor = new ArrayList<Point>();
                
                for (Point p : initialStatePoints)
                    initialStatePointsMinor.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), -1));
                
                Data datCopy = new Data(data);
                visualizerMinor.setData(datCopy);
                hashedPointsMinor = visualizerMinor.hashPoints(data);
                canvasWidthOnHashMinor = kmeansCanvasMinor.getWidth();
                canvasHeightOnHashMinor = kmeansCanvasMinor.getHeight();
                visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());
 
                iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
                logger.debug("IterationSpinnerMinor updated. New value: " + 0);

                visualizerMinor.clearCanvas();
                visualizerMinor.drawIterationData();

                logger.debug("ControlsMinor are deactivated.");
                deactivateControlsMinor();
                pointsKmeansSliderMinor.setDisable(false);
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
                buffer.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), -1));
            
            logger.debug("File loaded successfully");
            Data data = new Data(new Data(initialStatePointsMinor.size(), Algorithm.KMEANS, buffer));
            visualizerMinor.setData(data);
            hashedPointsMinor = visualizerMinor.hashPoints(data);
            canvasWidthOnHashMinor = kmeansCanvasMinor.getWidth();
            canvasHeightOnHashMinor = kmeansCanvasMinor.getHeight();
            visualizerMinor.setSpeed(pointsKmeansSlider.valueProperty().intValue());
        
            iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
            logger.debug("Iteration Spinner updated. New value: " + 0);
            
            visualizerMinor.clearCanvas();
            visualizerMinor.drawIterationData();
            
            logger.debug("Controls are deactivated.");
            deactivateControlsMinor();
            randomDataMinorButton.setDisable(false);
            loadFromFileMinorButton.setDisable(false);
            fakeActivateComputeButtonMinor();
            isComputedMinor = false;
            pointsKmeansSliderMinor.setDisable(false);
            
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
        fakeDeactivateComputeButton();
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
        fakeActivateComputeButton();
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
        fakeDeactivateComputeButtonMinor();
        randomDataMinorButton.setDisable(true);
        loadFromFileMinorButton.setDisable(true);        
        restartAutoModeKmeansImageMinor.setDisable(true);
        restartAutoModeKmeansImageMinor.setOpacity(0.6);
        pauseAutoModeKmeansImageMinor.setDisable(true);
        pauseAutoModeKmeansImageMinor.setOpacity(0.6);
        playAutoModeKmeansImageMinor.setDisable(true);
        playAutoModeKmeansImageMinor.setOpacity(0.6);
        kOfKmeansSpinnerMinor.setDisable(true);
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
            multiples.get(i).setShowIteration(true);
            multiples.get(i).draw();         
        }        
    }
    
    public void activateControlsMinor() {
        skipToStartImageMinor.setDisable(false);
        skipToStartImageMinor.setOpacity(1);
        stepBackImageMinor.setDisable(false);
        stepBackImageMinor.setOpacity(1);
        stepForwardImageMinor.setDisable(false);
        stepForwardImageMinor.setOpacity(1);
        iterationKmeansSpinnerMinor.setDisable(false);
        fakeActivateComputeButtonMinor();
        randomDataMinorButton.setDisable(false);
        loadFromFileMinorButton.setDisable(false);
        restartAutoModeKmeansImageMinor.setDisable(false);
        restartAutoModeKmeansImageMinor.setOpacity(1);
        pauseAutoModeKmeansImageMinor.setDisable(false);
        pauseAutoModeKmeansImageMinor.setOpacity(1);
        playAutoModeKmeansImageMinor.setDisable(false);
        playAutoModeKmeansImageMinor.setOpacity(1);
        kOfKmeansSpinnerMinor.setDisable(false);
        activateFiltersMinor();
    }
    
    public void activateFilters() {
        centroidPathKmeansCheckBox.setDisable(false);
        clusterCentersKmeansCheckBox.setDisable(false);
        dataPointsKmeansCheckBox.setDisable(false);  
        shapesCheckBox.setDisable(false);
    }
                
    public void activateFiltersMinor() {
        centroidPathKmeansCheckBoxMinor.setDisable(false);
        clusterCentersKmeansCheckBoxMinor.setDisable(false);
        dataPointsKmeansCheckBoxMinor.setDisable(false);   
        shapesCheckBoxMinor.setDisable(false);
    }
    
    public void deactivateFilters() {
        centroidPathKmeansCheckBox.setDisable(true);
        clusterCentersKmeansCheckBox.setDisable(true);
        dataPointsKmeansCheckBox.setDisable(true);
        shapesCheckBox.setDisable(true);
    }
                
    public void deactivateFiltersMinor() {
        centroidPathKmeansCheckBoxMinor.setDisable(true);
        clusterCentersKmeansCheckBoxMinor.setDisable(true);
        dataPointsKmeansCheckBoxMinor.setDisable(true);
        shapesCheckBoxMinor.setDisable(true);
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
        
        if (!isDisabledComputeButton) {
            fakeDeactivateComputeButton();
                       
            if (initMode.equals("I'll choose")) {
                this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints, clusterCenters, KMEANS.Initialization.USERCHOICE);
            } else if (initMode.equals("D2-Sampling")) {
                this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints, KMEANS.Initialization.D2);
            } else if (initMode.equals("Random centroids")) {
                this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints);
            } else {
                logger.debug("random partitioning");
                this.kmeansAlgorithm = new KMEANS(kOfKmeans, 100, initialStatePoints, KMEANS.Initialization.RANDOM_PARTITION);
            }
            
            if (algorithm.equals("K-medians")) {
                kmeansAlgorithm.setDistanceFunction("manhattan");
            }

            Data dat = kmeansAlgorithm.clusterData();
            logger.debug("centroids after calculation: " + dat.getIterationCenters(0).size());

            maxIterationsMain = dat.getIterations();
            hashedCenters = visualizer.hashCenters(dat);
            canvasWidthOnHash = kmeansCanvasMain.getWidth();
            canvasHeightOnHash = kmeansCanvasMain.getHeight();

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
            multiples.get(0).setHighlight(true);

            if (isLinked) {                
                this.initialStatePointsMinor = new ArrayList<Point>();
                
                for (Point p : initialStatePoints)
                    initialStatePointsMinor.add(new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber()));
                
                ArrayList<Point> centersBuf = new ArrayList<>();
                for (Point p : dat.getIterationCenters(0)) {
                    Point a = new Point(p.getX(), p.getY(), p.getCenterX(), p.getCenterY(), p.getClusterNumber());
                    a.setClusterSize(p.getClusterSize());
                    centersBuf.add(a);
                }
                clusterCentersMinor = centersBuf; 
                isDisabledComputeButtonMinor = false;
                iterateKmeansMinor();
                isDisabledComputeButtonMinor = true;
            }
            
            fakeActivateComputeButton();
        }
    }
    
    public void iterateKmeansMinor() {
        
        if (!isDisabledComputeButtonMinor) {
        
            if (initModeMinor.equals("Random partitioning")) {
                //void;
            } else if (isLinked && initMode.equals(initModeMinor)) {
                initModeMinor = "I'll choose";
            }

            if (initModeMinor.equals("I'll choose")) {
                this.kmeansAlgorithmMinor = new KMEANS(kOfKmeansMinor, 100, initialStatePointsMinor, clusterCentersMinor, KMEANS.Initialization.USERCHOICE);
                logger.debug(this.kmeansAlgorithmMinor.getInit().toString());
            } else if (initModeMinor.equals("D2-Sampling")) {
                this.kmeansAlgorithmMinor = new KMEANS(kOfKmeansMinor, 100, initialStatePointsMinor, KMEANS.Initialization.D2);
            } else if (initModeMinor.equals("Random centroids")) {
                this.kmeansAlgorithmMinor = new KMEANS(kOfKmeansMinor, 100, initialStatePointsMinor);
            } else {
                this.kmeansAlgorithmMinor = new KMEANS(kOfKmeansMinor, 100, initialStatePointsMinor, KMEANS.Initialization.RANDOM_PARTITION);
            }

            if (algorithmMinor.equals("K-medians")) {
                kmeansAlgorithmMinor.setDistanceFunction("manhattan");
            }

            Data dat = kmeansAlgorithmMinor.clusterData();
            maxIterationsMinor = dat.getIterations();
            visualizerMinor.setData(dat);
            hashedCentersMinor = visualizerMinor.hashCenters(dat);
            canvasWidthOnHashMinor = kmeansCanvasMinor.getWidth();
            canvasHeightOnHashMinor = kmeansCanvasMinor.getHeight();

            visualizerMinor.setAfterComputation();
            visualizerMinor.setSpeed(pointsKmeansSliderMinor.valueProperty().intValue());
            logger.debug("iterateKmeans pressed");
            logger.debug("Controls are activated.");
            if (!isLinked) activateControlsMinor();
            activateFiltersMinor();
            restartManualKmeansMinor();
            iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
            isComputedMinor = true;
            skipToStartImageMinor.setDisable(true);
            skipToStartImageMinor.setOpacity(0.6);
        }
    }
    
    public void stepBackKmeans() {
        stepBackImage.setDisable(true);
        stepBackImage.setOpacity(0.6);
        
        if (visualizer.getIteration() > 0) {
        
            visualizer.stepback();
            logger.debug("stepBackKmeans pressed");
            updateKmeansIteration();
            
            if (visualizer.getIteration() > 0) {
                stepBackImage.setDisable(false);
                stepBackImage.setOpacity(1);
            }
        }
        
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1); 
        
        //if (isLinked) stepBackKmeansMinor();
    }
    
    public void stepBackKmeansMinor() {
        stepBackImageMinor.setDisable(true);
        stepBackImageMinor.setOpacity(0.6);
        
        if (visualizerMinor.getIteration() > 0) {
        
            visualizerMinor.stepback();
            logger.debug("stepBackKmeansMinor pressed");
            updateKmeansIterationMinor();
            
            if (visualizerMinor.getIteration() > 0 && !isLinked) {
                stepBackImageMinor.setDisable(false);
                stepBackImageMinor.setOpacity(1);
            }
        }
        
        if (!isLinked) {
            stepForwardImage.setDisable(false);
            stepForwardImage.setOpacity(1);
        }
    }
    
    public void forwardKmeans() {
        stepForwardImage.setDisable(true);
        stepForwardImage.setOpacity(0.6);
        logger.debug("forwardKmeans pressed");        
                
        stepBackImage.setDisable(false);
        stepBackImage.setOpacity(1);
        skipToStartImage.setDisable(false);
        skipToStartImage.setOpacity(1);
        
        boolean isIterated = false;
        
        if (visualizer.getIteration() < maxIterationsMain-1){
            visualizer.iterate();
            updateKmeansIteration();
            isIterated = true;
        }
        
        if (!isIterated) forwardKmeansMinor();
        
        stepForwardImage.setDisable(false);
        stepForwardImage.setOpacity(1);

        if (visualizer.getIteration()+1 >= maxIterationsMain) {
            stepForwardImage.setDisable(true);
            stepForwardImage.setOpacity(0.6);
            //throw new IndexOutOfBoundsException();
        }
        
        if (isLinked && visualizerMinor.getIteration()+1 < maxIterationsMinor) {
            stepForwardImage.setDisable(false);
            stepForwardImage.setOpacity(1);
            //throw new IndexOutOfBoundsException();
        }
         
    }
    
    public void forwardKmeansMinor() {
        stepForwardImageMinor.setDisable(true);
        stepForwardImageMinor.setOpacity(0.6);
        logger.debug("forwardKmeansMinor pressed");

        if (!isLinked) {
            stepBackImageMinor.setDisable(false);
            stepBackImageMinor.setOpacity(1);
            skipToStartImageMinor.setDisable(false);
            skipToStartImageMinor.setOpacity(1);
        }
        
        if (visualizerMinor.getIteration()+1 < maxIterationsMinor){
            visualizerMinor.iterate();
            updateKmeansIterationMinor();

        }
        
        if (!isLinked) {        
            stepForwardImageMinor.setDisable(false);
            stepForwardImageMinor.setOpacity(1);
        }
         
        if (visualizerMinor.getIteration()+1 >= maxIterationsMinor) {
            stepForwardImageMinor.setDisable(true);
            stepForwardImageMinor.setOpacity(0.6);
            //throw new IndexOutOfBoundsException();
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
                            if (!stepForwardImage.isDisabled()) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        forwardKmeans();
                                    }
                                });                                
                                sleep(autoModeSpeedMain);
                            } else isRunning = false;                            
                        } catch (IndexOutOfBoundsException e) {
                            autoModePauseKmeans();
                            isRunning = false;                        
                        } catch (InterruptedException ex) {
                             logger.info(ex.getMessage());
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
                            if (!stepForwardImageMinor.isDisabled()) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        forwardKmeansMinor();
                                    }
                                }); 
                                sleep(autoModeSpeedMinor);
                            } else isRunning = false;
                        } catch (IndexOutOfBoundsException e) {
                            autoModePauseKmeansMinor();
                            isRunning = false;                        
                        } catch (InterruptedException ex) {
                             logger.info(ex.getMessage());
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
        logger.debug("autoMode restarted");
        updateKmeansIteration();
        autoModePlayKmeans();
        if (isLinked) autoModeRestartKmeansMinor();
    }
    
    public void autoModeRestartKmeansMinor() {
        visualizerMinor.restart();
        logger.debug("autoMode (minor) restarted");
        updateKmeansIterationMinor();
        autoModePlayKmeansMinor();
    }
    
    public void randomDataKmeansPressed() {
        randomDataButton.setDisable(true);
        this.initialStatePoints  = createGaussianRandomData(90);
        logger.debug("Random data generated");
        Data dat = new Data(initialStatePoints.size(), Algorithm.KMEANS, initialStatePoints);
        visualizer.setData(dat);
        hashedPoints = visualizer.hashPoints(dat);
        canvasWidthOnHash = kmeansCanvasMain.getWidth();
        canvasHeightOnHash = kmeansCanvasMain.getHeight();
        
        visualizer.setSpeed(pointsKmeansSlider.valueProperty().intValue());
        
        iterationKmeansSpinner.valueFactoryProperty().get().setValue(0);
        logger.debug("Iteration Spinner updated. New value: " + 0);
        
        visualizer.clearCanvas();
        visualizer.drawIterationData();
        
        logger.debug("Controls are deactivated.");
        deactivateControls();
        if (!initMode.equals("I'll choose")) {
            fakeActivateComputeButton();
        }
        isComputed = false;
        
        clusterCenters.clear();
        
        if (isLinked) {
            isComputedMinor = false;
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
            pointsKmeansSliderMinor.setDisable(false);
        }
        
        pointsKmeansSlider.setDisable(false);
        randomDataButton.setDisable(false);      
    }
    
    public void randomDataKmeansMinorPressed() {
        this.initialStatePointsMinor  = createGaussianRandomData(90);
        logger.debug("Random data generated");
        Data dat = new Data(initialStatePointsMinor.size(), Algorithm.KMEANS, initialStatePointsMinor);
        visualizerMinor.setData(dat);
        hashedPointsMinor = visualizerMinor.hashPoints(dat);
        canvasWidthOnHashMinor = kmeansCanvasMinor.getWidth();
        canvasHeightOnHashMinor = kmeansCanvasMinor.getHeight();
        
        visualizerMinor.setSpeed(pointsKmeansSliderMinor.valueProperty().intValue());
        
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(0);
        logger.debug("Iteration Spinner Minor updated. New value: " + 0);
        
        visualizerMinor.clearCanvas();
        visualizerMinor.drawIterationData();
        
        logger.debug("Controls are deactivated.");
        deactivateControlsMinor();
        deactivateFiltersMinor();
        randomDataMinorButton.setDisable(false);
        loadFromFileMinorButton.setDisable(false);
        pointsKmeansSliderMinor.setDisable(false);
        fakeActivateComputeButtonMinor();
        isComputedMinor = false;
                
    }
    
    public void changeNavigationLinkage() {
        logger.debug("ChangeNavigation called");
        if (isLinked) {
            linkageImage.setImage(new Image("images/Broken_Link_32px.png"));
            initModeMinor = kmeansInitChoiceBoxMinor.selectionModelProperty().get().getSelectedItem();
            activateControlsMinor();
            if (visualizerMinor.getIteration() == 0) {
                skipToStartImageMinor.setDisable(true);
                skipToStartImageMinor.setOpacity(0.6);
                stepBackImageMinor.setDisable(true);
                stepBackImageMinor.setOpacity(0.6);
            }
            
            if (visualizerMinor.getIteration() >= maxIterationsMinor-1) {
                stepForwardImageMinor.setDisable(true);
                stepForwardImageMinor.setOpacity(0.6);
            }
            
            if (!isComputedMinor) { //this could be done in a nicer way
                fakeDeactivateComputeButtonMinor();
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
            kOfKmeansSpinnerMinor.valueFactoryProperty().get().valueProperty().setValue(kOfKmeans);
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
    
    public void fakeDeactivateComputeButton() {
        computeButton.setOpacity(0.4);
        isDisabledComputeButton = true;
    }
    
    public void fakeActivateComputeButton() {
        computeButton.setOpacity(1);
        isDisabledComputeButton = false;
    }
    
    public void fakeDeactivateComputeButtonMinor() {
        computeButtonMinor.setOpacity(0.4);
        isDisabledComputeButtonMinor = true;
    }
    
    public void fakeActivateComputeButtonMinor() {
        computeButtonMinor.setOpacity(1);
        isDisabledComputeButtonMinor = false;
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
            if (!newValue.equals(oldValue) && newValue < maxIterationsMain && newValue >= 0) {
                if (isLinked) iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(newValue);
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
                if (isComputed) visualizer.setIteration(newValue);
                for (VisualizerFX vis : multiples) vis.setHighlight(false);
                multiples.get(newValue).setHighlight(true);
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
            } else {
                iterationKmeansSpinner.valueFactoryProperty().get().setValue(oldValue);
            }
        });
        
        iterationKmeansSpinnerMinor.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && newValue < maxIterationsMinor && newValue >= 0) {
                kmeansCanvasMinor = new Canvas(420, 420);
                kmeansCanvasMinor.setCache(false);
                kmeansCanvasMinor.setCacheHint(CacheHint.SPEED);
                kmeansParentPaneMinor.getChildren().clear();
                kmeansParentPaneMinor.getChildren().add(kmeansCanvasMinor);
                visualizerMinor.bindProperties(kmeansCanvasMinor, kmeansParentPaneMinor);
                if (isComputedMinor) visualizerMinor.setIteration(newValue);
                logger.debug("IterationMinor set to " + newValue);
            } else {
                iterationKmeansSpinnerMinor.valueFactoryProperty().get().setValue(oldValue);
            }
        });
        
        kOfKmeansSpinner.valueFactoryProperty().get().valueProperty().addListener((observable, oldValue, newValue) -> {
            kOfKmeans = newValue;
            if (isLinked) kOfKmeansSpinnerMinor.valueFactoryProperty().get().valueProperty().setValue(newValue);
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
        
        shapesCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show data points: " + newValue.toString());
            visualizer.setColorblindMode(newValue);
        });
        
        shapesCheckBoxMinor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Show data points Minor: " + newValue.toString());
            visualizerMinor.setColorblindMode(newValue);
        });
        
        kmeansUpdateStratChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Update-strat (Main) set to " + newValue);
            this.strategy = newValue;
        });
        
        kmeansInitChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Init-strat (Main) set to " + newValue);
            this.initMode = newValue;
            if (newValue.equals("I'll choose")) {
                fakeDeactivateComputeButton();
                isComputed = false;
            } else {
                clusterCenters.clear();
                if (!initialStatePoints.isEmpty()) fakeActivateComputeButton();
            }
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
            if (newValue.equals("I'll choose")) {
                fakeDeactivateComputeButtonMinor();
                isComputedMinor = false;
            } else {
                clusterCentersMinor.clear();
                if (!initialStatePointsMinor.isEmpty() && !isLinked) fakeActivateComputeButtonMinor();
            }
        });
        
        kmeansAlgorithmChoiceBoxMinor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            //logger.debug("Algorithm (Minor) set to " + newValue);
            this.algorithmMinor = newValue;
        });
                
        kmeansUpdateStratChoiceBox.getSelectionModel().selectFirst();
        kmeansInitChoiceBox.getSelectionModel().selectFirst();
        kmeansAlgorithmChoiceBox.getSelectionModel().selectFirst();
        
        kmeansUpdateStratChoiceBoxMinor.getSelectionModel().selectFirst();
        kmeansInitChoiceBoxMinor.getSelectionModel().selectFirst();
        kmeansAlgorithmChoiceBoxMinor.getSelectionModel().selectFirst();
                       
        visualizer.bindProperties(kmeansCanvasMain, kmeansParentPane);
        visualizerMinor.bindProperties(kmeansCanvasMinor, kmeansParentPaneMinor);
        
        kmeansParentPane.setStyle("-fx-border-color: black");

        deactivateControls();
        deactivateControlsMinor();
        deactivateFiltersMinor();
                
        Tooltip mousePositionToolTip = new Tooltip("");
        Tooltip tooManyCentroidsTooltip = new Tooltip("");
        Tooltip mousePositionToolTipMinor = new Tooltip("");
        Tooltip tooManyCentroidsTooltipMinor = new Tooltip("");

        kmeansParentPane.setOnMouseMoved((MouseEvent event) -> {
            //TODO
            boolean dummy = false;
            int x = (int) event.getX() - 1;
            int y = (int) event.getY() - 1;
            
            x = (int) (x * (canvasWidthOnHash / kmeansCanvasMain.getWidth()));
            y = (int) (y * (canvasHeightOnHash / kmeansCanvasMain.getHeight()));
            
            Point hoveredCenter = null, hoveredPoint = null;
            String msg = "";
            int clusterToHighlight = -1;
            
            try {
                hoveredCenter = hashedCenters.get(visualizer.getIteration()).get(x).get(y);
                dummy = (hoveredCenter != null);
                if (dummy) {
                    msg = "x: " + (int) hoveredCenter.getX() + ", y: " + (int) hoveredCenter.getY()
                                + "\nCluster ID:   " + hoveredCenter.getClusterNumber() 
                                + "\nCluster size: " + hoveredCenter.getClusterSize();
                    clusterToHighlight = hoveredCenter.getClusterNumber();
                }
            } catch (Exception e) {
                //logger.trace(e.getMessage());
            }
            
            try {
                if (!dummy) {
                    hoveredPoint = hashedPoints.get(x).get(y);
                    dummy = (hoveredPoint != null);
                    if (dummy) {
                        msg = "x: " + (int) hoveredPoint.getX() + ", y: " + (int) hoveredPoint.getY();
                        clusterToHighlight = hoveredPoint.getClusterNumber();
                    }
                }
            } catch (Exception e) {
                //logger.trace(e.getMessage());
            }
            
            if (dummy) {
                mousePositionToolTip.setText(msg);
                visualizer.highlightCluster(clusterToHighlight+1); //+1 for some reasons
                
                Node node = (Node) event.getSource();
                mousePositionToolTip.show(node, event.getScreenX() + 50, event.getScreenY());
            } else {
                mousePositionToolTip.hide();
                if (isComputed) visualizer.draw();
            }
        });
        
        kmeansParentPane.setOnMouseExited((MouseEvent event) -> {
            mousePositionToolTip.hide();
            tooManyCentroidsTooltip.hide();
        });        
        
        kmeansParentPane.setOnMouseClicked((MouseEvent event) -> {
            Point p = new Point(event.getX()/kmeansCanvasMain.getWidth() * 150, event.getY()/kmeansCanvasMain.getHeight() * 150);
            p.setCenterPointTrue();
            if (!initMode.equals("I'll choose")) {
                tooManyCentroidsTooltip.setText("Set initialization mode to \"I'll choose\"\n to set centroids manually");
                tooManyCentroidsTooltip.show((Node) event.getSource(), event.getScreenX() + 50, event.getScreenY());
            } else if (clusterCenters.size() >= kOfKmeans) {
                tooManyCentroidsTooltip.setText("Set k higher to set more centroids!");
                tooManyCentroidsTooltip.show((Node) event.getSource(), event.getScreenX() + 50, event.getScreenY());
            } else {
                clusterCenters.add(p);
                visualizer.drawCenter(p);
                
                if (clusterCenters.size() == kOfKmeans) {
                    fakeActivateComputeButton();
                }
            }
        });  
        
        
        kmeansParentPaneMinor.setOnMouseMoved((MouseEvent event) -> {
            //TODO
            boolean dummy = false;
            int x = (int) event.getX() - 1;
            int y = (int) event.getY() - 1;
            
            x = (int) (x * (canvasWidthOnHashMinor / kmeansCanvasMinor.getWidth()));
            y = (int) (y * (canvasHeightOnHashMinor / kmeansCanvasMinor.getHeight()));
            
            Point hoveredCenter = null, hoveredPoint = null;
            String msg = "";
            int clusterToHighlight = -1;
            
            try {
                hoveredCenter = hashedCentersMinor.get(visualizerMinor.getIteration()).get(x).get(y);
                dummy = (hoveredCenter != null);
                if (dummy) {
                    msg = "x: " + (int) hoveredCenter.getX() + ", y: " + (int) hoveredCenter.getY()
                                + "\nCluster ID:   " + hoveredCenter.getClusterNumber() 
                                + "\nCluster size: " + hoveredCenter.getClusterSize();
                    clusterToHighlight = hoveredCenter.getClusterNumber();
                }
            } catch (Exception e) {
                //logger.trace(e.getMessage());
            }
            
            try {
                if (!dummy) {
                    hoveredPoint = hashedPointsMinor.get(x).get(y);
                    dummy = (hoveredPoint != null);
                    if (dummy) {
                        msg = "x: " + (int) hoveredPoint.getX() + ", y: " + (int) hoveredPoint.getY();
                        clusterToHighlight = hoveredPoint.getClusterNumber();
                    }
                }
            } catch (Exception e) {
                //logger.trace(e.getMessage());
            }
            
            if (dummy) {
                mousePositionToolTipMinor.setText(msg);
                visualizerMinor.highlightCluster(clusterToHighlight+1);
                
                Node node = (Node) event.getSource();
                mousePositionToolTipMinor.show(node, event.getScreenX() + 50, event.getScreenY());
            } else {
                mousePositionToolTipMinor.hide();
                if (isComputedMinor) visualizerMinor.draw();
            }
        });
        
        kmeansParentPaneMinor.setOnMouseExited((MouseEvent event) -> {
            mousePositionToolTipMinor.hide();
            tooManyCentroidsTooltipMinor.hide();
        });
        
        kmeansParentPaneMinor.setOnMouseClicked((MouseEvent event) -> {
            Point p = new Point(event.getX()/kmeansCanvasMinor.getWidth() * 150, event.getY()/kmeansCanvasMinor.getHeight() * 150);
            p.setCenterPointTrue();
            if (!initModeMinor.equals("I'll choose")) {
                tooManyCentroidsTooltipMinor.setText("Set initialization mode to \"I'll choose\"\n to set centroids manually");
                tooManyCentroidsTooltipMinor.show(kmeansParentPaneMinor, event.getScreenX() + 50, event.getScreenY());
            } else if (clusterCentersMinor.size() >= kOfKmeansMinor) {
                tooManyCentroidsTooltipMinor.setText("Set k higher to set more centroids!");
                tooManyCentroidsTooltipMinor.show(kmeansParentPaneMinor, event.getScreenX() + 50, event.getScreenY());
            } else {
                clusterCentersMinor.add(p);
                visualizerMinor.drawCenter(p);
                
                if (clusterCentersMinor.size() == kOfKmeansMinor) {
                    fakeActivateComputeButtonMinor();
                }
            }
        }); 
        
        Tooltip recomputeDisabled = new Tooltip("hallo");
        computeButton.setTooltip(recomputeDisabled);
        computeButton.setOnMouseMoved((MouseEvent e) -> {
            if (!isDisabledComputeButton) return;
            recomputeDisabled.setText("Cluster centers set does not match k");
            if (initMode.equals("I'll choose")) recomputeDisabled.show(computeButton, e.getX(), e.getY());
        });

        computeButton.setOnMouseExited((MouseEvent e) -> {
            recomputeDisabled.hide();
        });
        
        Tooltip recomputeDisabledMinor = new Tooltip("hallo");
        computeButtonMinor.setTooltip(recomputeDisabledMinor);
        computeButtonMinor.setOnMouseMoved((MouseEvent e) -> {
            if (!isDisabledComputeButtonMinor) return;
            recomputeDisabledMinor.setText("Cluster centers set does not match k");
            if (!isLinked && initModeMinor.equals("I'll choose")) recomputeDisabledMinor.show(computeButtonMinor, e.getX(), e.getY());
        });

        computeButtonMinor.setOnMouseExited((MouseEvent e) -> {
            recomputeDisabledMinor.hide();
        });
    }    
}
