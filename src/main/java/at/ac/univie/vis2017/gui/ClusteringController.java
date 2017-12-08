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

import at.ac.univie.vis2017.util.Data;
import at.ac.univie.vis2017.util.Point;
import at.ac.univie.vis2017.visualizer.IVisualizer.Mode;
import at.ac.univie.vis2017.visualizer.VisualizerFX;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


import java.net.URL;
import java.util.ArrayList;
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
    //@FXML ImageView restartManualModeKMeansImage;
    //@FXML ImageView stepBackManualModeKMeansImage;
    //@FXML ImageView iterateManualModeKMeansImage;
    @FXML Spinner<Integer> iterationKmeansSpinner;
    @FXML Slider speedKmeansSlider;
    @FXML CheckBox centroidPathKmeansCheckBox;
    @FXML CheckBox clusterCentersKmeansCheckBox;
    @FXML CheckBox dataPointsKmeansCheckBox;
    @FXML CheckBox voronoiLinesKmeansCheckBox;

    private Main application;
    private ArrayList<Point> initialStatePoints;
    private VisualizerFX visualizer;
    
    Logger logger = LogManager.getLogger(ClusteringController.class);

    public ClusteringController() {
        this.visualizer = new VisualizerFX();
        this.initialStatePoints = new ArrayList<>();
    }
    
    public void setApp(Main application){
        this.application = application;
    }
    
    // read data from txt-file
    // http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    public static ArrayList<Point> getDataFromTxt(String filePath) throws IOException {

        // create ArrayList where Points are saved
        ArrayList<Point> data = new ArrayList<>();

        // create buffered reader
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String line;

        try {
            while ((line = br.readLine()) != null) {
                String[] row = line.split(" ");
                data.add(new Point(Double.parseDouble(row[1]), Double.parseDouble(row[2]), 0.0, 0.0, 0));
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: unable to read file " + filePath);
            e.printStackTrace();
        }

        return data;
    }
    
    public String loadFromFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load dataset from file");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
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
                //
            }
        } catch (NullPointerException ex) {
            logger.debug("File opening aborted");
        }   
        
        return datasetPath;
    }
    
    public void loadKmeansFile() {
        String kmeansFile = loadFromFile();
        try {
            initialStatePoints = getDataFromTxt(kmeansFile);
            System.out.println("Test");
            visualizer.drawInitialState(kmeansCanvasMain.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasStart.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasMiddle.getGraphicsContext2D(), initialStatePoints);
            visualizer.drawInitialState(kmeansCanvasEnd.getGraphicsContext2D(), initialStatePoints);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ClusteringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void restartManualKmeans() {
        visualizer.restart();
        //iterationKmeansSpinner.valueFactoryProperty().
        System.out.println("restartKmeans pressed");
    }
    
    public void iterateKmeans() {
        visualizer.iterate();
        //iterationKmeansSpinner.valueFactoryProperty().set((visualizer.getIteration()));
        System.out.println("ierateKmeans pressed");
    }
    
    public void stepBackKmeans() {
        visualizer.stepback();
        //iterationKmeansSpinner.valueFactoryProperty().set((visualizer.getIteration()));
        System.out.println("stepBackKmeans pressed");
    }
    
    public void autoModePlayKmeans() {
        visualizer.setMode(Mode.AUTO);
        System.out.println("setMode.AUTO pressed");
    }
    
    public void autoModePauseKmeans() {
        visualizer.setMode(Mode.MANUAL);
        System.out.println("setMode.MANUAL pressed");
    }
    
    public void autoModeRestartKmeans() {
        visualizer.restart();
        visualizer.setMode(Mode.AUTO);
        System.out.println("autoMode restarted");
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
                
        restartAutoModeKmeansImage.setImage(new Image("images/Restart_32px.png"));
        pauseAutoModeKmeansImage.setImage(new Image("images/Pause_32px.png"));
        playAutoModeKmeansImage.setImage(new Image("images/Play_32px.png"));
        //restartManualModeKMeansImage.setImage(new Image("images/Skip_to_Start_32px.png"));
        //stepBackManualModeKMeansImage.setImage(new Image("images/Back_32px.png"));
        //iterateManualModeKMeansImage.setImage(new Image("images/Forward_32px.png"));
        
        //iterationKmeansSpinner.valueProperty().s
        
        speedKmeansSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            visualizer.setSpeed(newValue.intValue());
            System.out.println("new speed set");
        });
        
        
        //Checkboxes
        centroidPathKmeansCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Show centroid paths: " + newValue.toString());
            visualizer.setShowPaths(newValue);
        });
        //clusterCentersKmeansCheckBox
        //@FXML CheckBox dataPointsKmeansCheckBox;
        //@FXML CheckBox voronoiLinesKmeansCheckBox;
               
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
        
        
    }
    
}
