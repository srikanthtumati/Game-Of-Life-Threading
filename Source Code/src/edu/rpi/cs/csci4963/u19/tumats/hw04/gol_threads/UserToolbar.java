package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Pair;

/**
 * Generates the ToolBar that houses commonly used buttons to the user. Additionally creates event handlers for these
 * buttons
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class UserToolbar extends HBox{

    /** The Horizontal Box that contains the buttons to be used in the toolbar */
    private HBox hBox;
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol;
    /** golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods' */
    private GameOfLifeUI golui;
    /** configHandler represents the instance of ConfigHandler that handles all operations related to the Configuration File */
    private ConfigHandler configHandler;
    /** userStatistics represents the instance of UserStatistics that is used to display data about the current tick to the user */
    private UserStatistics userStatistics;
    /** gridView represents the instance of the GridView which generates the grid viewable by the user for the lifetime of the program */
    private GridView gridView;

    /**
     * Constructor for the UserToolbar class
     * @param gol The GameOfLife object that is currently being used in the program
     * @param golui The GameOfLifeUI object that is currently being used in the program
     * @param configHandler The ConfigHandler object that is currently being used in the program
     * @param userStatistics the UserStatistics object that is currently being used in the program
     * @param gridView the GridView object that is currently being used in the program
     */
    public UserToolbar(GameOfLife gol, GameOfLifeUI golui, ConfigHandler configHandler, UserStatistics userStatistics, GridView gridView){
        this.gol = gol;
        this.golui = golui;
        this.configHandler = configHandler;
        this.userStatistics = userStatistics;
        this.gridView = gridView;
        hBox = new HBox();
        generateToolbar();
    }

    /**
     * Returns the Horizontal Box that is populated with the buttons that are in the program toolbar
     * @return HBox that is populated
     */
    public HBox getToolBar(){
        return hBox;
    }

    /**
     * Creates the toolbar that will be added to the bottom of the GUI. Additionally, the actions that are performed by
     * the buttons are also added.
     */
    private void generateToolbar(){
        Button autoPlay = new Button("Auto Play");
        autoPlay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                System.out.println(System.nanoTime());
                while (gol.getCurrentTick() < ConfigHandler.maxTicks){
                    golui.updateGOLSTick();
                }
            }
        });
        Button configurationPanel = new Button("Configuration Panel");
        configurationPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                configHandler.generateConfigDialog();
            }
        });
        autoPlay.setMaxWidth(Double.MAX_VALUE);
        configurationPanel.setMaxWidth(Double.MAX_VALUE);
        hBox.getChildren().addAll(autoPlay, configurationPanel);
        HBox.setHgrow(autoPlay, Priority.ALWAYS);
        HBox.setHgrow(configurationPanel, Priority.ALWAYS);
    }



}
