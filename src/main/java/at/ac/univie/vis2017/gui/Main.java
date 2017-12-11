/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 */
package at.ac.univie.vis2017.gui;

import at.ac.univie.vis2017.clustering_algorithms.DBSCAN;
import at.ac.univie.vis2017.clustering_algorithms.KMEANS;
import at.ac.univie.vis2017.util.Point;
import at.ac.univie.vis2017.gui.ClusteringController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 1366;
    private final double MINIMUM_WINDOW_HEIGHT = 768;
    private final static String LOG4J_DEFAULT_CONFIG_FILE = System.getProperty("user.dir") + 
            System.getProperty("file.separator") + "log4j2.xml";
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Main.class);
  
    /**
     * loads the log4j.xml file, which is located in the path where 
     * the data export tool is located
     */
    public static void loadLogger() {
        // Create logger (API defines log4j2.xml path)
        File logConfFile = new File(LOG4J_DEFAULT_CONFIG_FILE);
        System.out.println("Trying to read logger configuration in file:\n" + LOG4J_DEFAULT_CONFIG_FILE + "\n");
        
        if (!logConfFile.exists()) {
            System.err.println(String.format("The logger config file %s does not exist", LOG4J_DEFAULT_CONFIG_FILE));
        } else if (logConfFile.isDirectory()) {
            System.err.println(String.format("The logger config file %s is a directory!!!", LOG4J_DEFAULT_CONFIG_FILE));
        } else if (!logConfFile.canRead()) {
            System.err.println(String.format("The logger config file %s can not be read", LOG4J_DEFAULT_CONFIG_FILE));
        }

        Configurator.initialize("preprocessor", null, logConfFile.toURI());

        logger = LogManager.getLogger(Main.class);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {



        /*
        #############################################################################################
        #############################################################################################
        Test Clustering Algorithms
        #############################################################################################
        #############################################################################################
         */

        //TODO Mike
        ArrayList<Point> dbscan1points = new ArrayList<>();
        ArrayList<Point> dbscan2points = new ArrayList<>();
        ArrayList<Point> dbscan3points = new ArrayList<>();



        // read data from file
        try {
            dbscan1points = ClusteringController.getDataFromTxt("/Users/michaeltrimmel/IdeaProjects/vis2017_clustering/src/main/data/dbscan1.txt");
            //dbscan2points = ClusteringController.getDataFromTxt("/Users/michaeltrimmel/IdeaProjects/vis2017_clustering/src/main/data/dbscan2.txt");
            //dbscan3points = ClusteringController.getDataFromTxt("/Users/michaeltrimmel/IdeaProjects/vis2017_clustering/src/main/data/dbscan3.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //for (Point p : points)
        //System.out.println(p.toString());



        // get number of points
        int n_dbscan1 = dbscan1points.size();
        //int n_dbscan2 = dbscan2points.size();
        //int n_dbscan3 = dbscan3points.size();
        System.out.println("Number Points = " + n_dbscan1);
        //System.out.println(n_dbscan2);
        //System.out.println(n_dbscan3);

        // set number of target clusters
        int eps_value = 3;
        int minPts = 2;



        // test dbscan
        //DBSCAN dbscanTest = new DBSCAN(minPts, eps_value, dbscan1points);

        //dbscanTest.clusterData();



        KMEANS kmeansTest = new KMEANS(3, 100, dbscan1points);

        kmeansTest.setClusterCenters("random");
        kmeansTest.clusterData();






        loadLogger();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        showMainWindow();
    }
    
    public void showMainWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ClusteringGUI.fxml"));

            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/ClusteringGUI.css");
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMaxHeight(height);
            stage.setMaxWidth(width);
            stage.setTitle("Clustering Visualizer");
            stage.getIcons().add(new Image("images/clustering.png"));          
            stage.setScene(scene);
            stage.show();
            //stage.setResizable(false);
            //stage.maxHeightProperty().bind(stage.widthProperty().multiply(9/16));
            //stage.maxWidthProperty().bind(stage.heightProperty().multiply(16/9));
            //stage.
            stage.setMaximized(true);
        } catch (IOException ex) {
            logger.fatal("Fatal error", ex);
        }
    }

}
