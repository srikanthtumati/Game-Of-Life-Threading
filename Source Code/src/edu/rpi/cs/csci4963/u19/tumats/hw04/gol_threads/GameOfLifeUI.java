package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;



import java.util.ArrayList;

/**
 * Main Class for the GUI. It contains all parts of the GUI and also houses 'central' methods that require a number of
 * individual classes.
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class GameOfLifeUI extends Application {

    /** The maximum number of ticks that the program can run */
    private int maxTicks;
    /** configHandler represents the instance of ConfigHandler that handles all operations related to the Configuration File */
    private ConfigHandler configHandler;
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol = new GameOfLife();
    /** borderPane is the main GUI component that connects the various parts of the GUI */
    private BorderPane borderPane;
    /** gridView represents the instance of the GridView which generates the grid viewable by the user for the lifetime of the program */
    private GridView gridView;
    /** userStatistics represents the instance of UserStatistics that is used to display data about the current tick to the user */
    private UserStatistics userStatistics;
    /** primaryStage is the Stage where the GUI is being displayed */
    private Stage primaryStage;
    /** menuBar is the Menu that is displayed to the user at the top of the GUI */
    private UserMenu menuBar;
    /** toolbar is the Toolbar that is displayed to the user at the bottom of the GUI */
    private UserToolbar toolbar;
//    private long startTime;
//    private long endTime;

    private int minDimension;

    /**
     * assignDefaultVals is run to initialize the program and get the maximum number of ticks from the configuration file
     * (if it exists) and sets it to 50 otherwise.
     */
    public void assignDefaultVals(){
        String[] configData = ConfigHandler.readConfigFile();
        maxTicks = Integer.parseInt(configData[2]);
    }

    /**
     * Performs the necessary operations to 'reset' the program and prepare to load values from a data file
     * @param filename the name of the data file (determined by user)
     */
    public void loadFile(String filename){
        gol.resetData();
        ParseData.readFile(filename, gol);
        gridView = new GridView(gol, this, userStatistics);
        ConfigHandler.defaultRows = gol.getNumRows();
        ConfigHandler.defaultCols = gol.getNumCols();
        borderPane.setCenter(gridView.getGridView());
        gridView.generateGrid(minDimension);
        userStatistics.resetStatistics();
    }

    /**
     * Updates the GUI to account for changes in the configuration file
     * @param dimensionChange true if the user changed the default row/column values and false otherwise
     */
    public void updateDefaultVals(boolean dimensionChange){
        this.maxTicks = ConfigHandler.maxTicks;
        if (dimensionChange) {
            resetData();
            resizeWindow();
        }


    }

    /**
     * Ensures that the window is properly sized and also sets a minimum so the user is unable to hide a part of the GUI
     */
    public void resizeWindow(){
        this.primaryStage.sizeToScene();
        this.primaryStage.setWidth(primaryStage.getWidth());
        this.primaryStage.setHeight(primaryStage.getHeight());
        this.primaryStage.setMinWidth(primaryStage.getWidth());
        this.primaryStage.setMinHeight(primaryStage.getHeight());
        this.primaryStage.setMaximized(true);
        borderPane.setRight(userStatistics.getGridPane());
    }

    /**
     * Allows the program to reset and also account for any changes in the dimensions of the grid
     */
    public void resetData(){
        gol.resetData();
        gridView = new GridView(gol, this, userStatistics);
        borderPane.setCenter(gridView.getGridView());
        gol.populateBoard();
        gridView.generateGrid(minDimension);
        userStatistics.resetStatistics();
    }

    /**
     * Performs all necessary operations in order to update the current tick
     */
    public void updateGOLSTick(){
        if (gol.getCurrentTick() < maxTicks){
            if (gol.getCurrentTick() >= menuBar.getStartingTickRange() && gol.getCurrentTick() < menuBar.getEndingTickRange())
                WriteData.writeFile(gol.boardToString(gol.getBoard()), ConfigHandler.filePattern, ConfigHandler.folderPattern  , gol.getCurrentTick());
            gol.currentTick += 1;
            System.out.println(gol.getCurrentTick());
            ArrayList<Thread> updateBoard = new ArrayList<>();
            int rowsPerThread = ConfigHandler.defaultRows / ConfigHandler.defaultThreads;
            int startingRow = 0;
            int endingRow = rowsPerThread;
            for (int i = 0; i < ConfigHandler.defaultThreads; i++) {
                startingRow = i * rowsPerThread;
                endingRow = (i + 1) * rowsPerThread;
                if (i == ConfigHandler.defaultThreads - 1) {
                    endingRow = ConfigHandler.defaultRows;
                }
                UpdateGraphTask updateGraphTask = new UpdateGraphTask(startingRow, endingRow, gol);
                Thread updateGraphThread = new Thread(updateGraphTask);
                updateGraphThread.setDaemon(true);
                updateGraphThread.start();
                updateBoard.add(updateGraphThread);
            }
            for (Thread thread: updateBoard){
                try{
                    thread.join();
                }
                catch(InterruptedException ex){
                    ex.printStackTrace();
                }
            }
            gol.updateTick();
            userStatistics.incrementTick();
            if (gol.getCurrentTick() == ConfigHandler.maxTicks) {
                gridView.generateGrid((int) Math.min(gridView.getGridView().getHeight(), gridView.getGridView().getWidth()));
//                System.out.println(System.nanoTime());
            }
            if (gol.getCurrentTick() >= menuBar.getStartingTickRange() && gol.getCurrentTick() < menuBar.getEndingTickRange())
                WriteData.writeFile(gol.boardToString(gol.getBoard()), ConfigHandler.filePattern, ConfigHandler.folderPattern  , gol.getCurrentTick());
        }
    }

    /**
     * Initializes all GUI components and prepares the GUI for the user
     */
    public void init(){
        userStatistics = new UserStatistics();
        configHandler = new ConfigHandler(this);
        assignDefaultVals();
        menuBar = new UserMenu(gol, this, configHandler);
        gridView = new GridView(gol, this, userStatistics);
        gol.initializeBoard(ConfigHandler.defaultRows, ConfigHandler.defaultCols);
        gol.populateBoard();
        toolbar = new UserToolbar(gol, this, configHandler, userStatistics, gridView);
    }

    @Override
    /**
     * Connects the components of the GUI to the BorderPane and displays the GUI to the user
     */
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        borderPane = new BorderPane();
        primaryStage.setTitle("Conway's Game of Life Simulator");
        borderPane.setTop(menuBar.getMenu());
        borderPane.setCenter(gridView.getGridView());
        borderPane.setBottom(toolbar.getToolBar());
        Scene scene = new Scene(borderPane);
        this.primaryStage.setScene(scene);
        this.primaryStage.sizeToScene();
        this.primaryStage.show();
        this.primaryStage.setMaximized(true);
        this.primaryStage.setMinWidth(this.primaryStage.getWidth());
        this.primaryStage.setMinHeight(this.primaryStage.getHeight());
        minDimension = (int) Math.min(gridView.getGridView().getHeight(), gridView.getGridView().getWidth());
        if (minDimension == 0){
        	minDimension = 947;
        }
        gridView.generateGrid(minDimension);
        borderPane.setRight(userStatistics.getGridPane());
    }

    /**
     * The main method to the program
     */
    public static void main(String[] args) {
        launch(args);
    }
}


