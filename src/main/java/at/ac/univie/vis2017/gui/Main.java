/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 */
package at.ac.univie.vis2017.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;


/**
 * Test Application. This class handles navigation and user session.
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
        
        loadLogger();                               
        launch(args);
        
        System.out.println("I'm awake.");
        
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
            
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setHeight(MINIMUM_WINDOW_HEIGHT);
            stage.setMaxWidth(1920);
            stage.setMaxHeight(1080);
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setTitle("Clustering Visualizer");
            stage.getIcons().add(new Image("images/clustering.png"));          
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            logger.fatal("Fatal error", ex);
        }
    }

}
