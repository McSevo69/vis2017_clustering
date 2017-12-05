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

import at.ac.univie.vis2017.visualizer.VisualizerFX;
import ch.netzwerg.paleo.DataFrame;
import ch.netzwerg.paleo.io.Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javax.swing.JComponent;
import javax.swing.JPanel;
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
    @FXML Canvas kmeansCanvas;
    @FXML Accordion kmeansAccordion;
    @FXML TitledPane kmeansDataGenPane;
    @FXML Pane kmeansParentPane;

    private Main application;
    private DataFrame kmeansDataFrame;
    private VisualizerFX visualizer;
    
    Logger logger = LogManager.getLogger(ClusteringController.class);

    public ClusteringController() {
        this.visualizer = new VisualizerFX();
    }
    
    public void setApp(Main application){
        this.application = application;
    }
    
    // read data from txt-file
    // http://people.cs.nctu.edu.tw/~rsliang/dbscan/testdatagen.html
    public static DataFrame getDataFromTxt(String filePath) throws IOException {

        // create dataframe where txt file is saved
        DataFrame data = null;

        // create buffered reader
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        String line;
        String datasetString = "";

        try {
            while ((line = br.readLine()) != null) {
                datasetString+=line+"\n";
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("ERROR: unable to read file " + filePath);
            e.printStackTrace();
        }
        
        //System.out.println(datasetString);
        
        data = Parser.tsv(new StringReader(datasetString));

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
            kmeansDataFrame = getDataFromTxt(kmeansFile);
            System.out.println("Test");
            visualizer.drawDataFrame(kmeansCanvas.getGraphicsContext2D(), kmeansDataFrame);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ClusteringController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        kmeansAccordion.expandedPaneProperty().set(kmeansDataGenPane);
                
        GraphicsContext gc = kmeansCanvas.getGraphicsContext2D();
        
        //visualizer.drawBorder(gc);
        visualizer.bindProperties(kmeansCanvas, kmeansParentPane, gc);
        visualizer.drawShapes(gc);
        
        kmeansParentPane.setStyle("-fx-border-color: black");
    }
    
}
