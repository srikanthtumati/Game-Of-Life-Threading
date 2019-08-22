package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.Optional;

/**
 * Creates the menu for the GUI and also handles all dialog boxes that can be produced from the menu. Additionally, it
 * performs various checks to ensure that the user input is valid and also attempts to reject incorrect input
 * (such as characters or negative numbers for the row/column size)
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class UserMenu extends MenuBar{

    /** configHandler represents the instance of ConfigHandler that handles all operations related to the Configuration File */
    private ConfigHandler configHandler;
    /** gol is the GameOfLife object where all data related operations occur */
    private GameOfLife gol;
    /** golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods' */
    private GameOfLifeUI golui;
    /** menuBar is an instance of UserMenu */
    private MenuBar menuBar;
    private TextField startingTickRange = new TextField("0");
    private TextField endingTickRange = new TextField("0");

    /**
     * Constructor for the UserMenu class
     * @param gol The GameOfLife object that is currently being used in the program
     * @param golui The GameOfLifeUI object that is currently being used in the program
     * @param configHandler The ConfigHandler object that is currently being used in the program
     */
    public UserMenu(GameOfLife gol, GameOfLifeUI golui, ConfigHandler configHandler) {
        this.gol = gol;
        this.golui = golui;
        this.configHandler = configHandler;
        menuBar = new MenuBar();
        Menu menuTools = generateMenuTools();
        Menu menuFile = generateMenuFiles();
        menuBar.getMenus().add(menuFile);
        menuBar.getMenus().add(menuTools);
    }

    /**
     * Returns the UserMenu after being generated and properly formatted
     * @return The created UserMenu to be used in the GUI
     */
    public MenuBar getMenu(){
        return this.menuBar;
    }

    public int getStartingTickRange(){
        return Integer.parseInt(startingTickRange.getText());
    }


    public int getEndingTickRange(){
        return Integer.parseInt(endingTickRange.getText());
    }
    /**
     * Generates the 'Tools' portion of the menu and all connected submenus with proper event handlers.
     * @return The 'Tools' part of the menu
     */
    private Menu generateMenuTools() {
        Menu menuTools = new Menu("Tools");
        MenuItem menuReset = new MenuItem("Reset");
        MenuItem menuConfig = new MenuItem("Configuration Panel");
        menuReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure you want to reset ALL tick data?");
                alert.setTitle("Reset Tick Data");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    golui.resetData();
                }
            }
        });
        menuConfig.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                configHandler.generateConfigDialog();
            }
        });
        menuTools.getItems().addAll(menuReset, menuConfig);
        return menuTools;
    }

    /**
     * Generates the 'Files' portion of the menu and all connected submenus with proper event handlers.
     * @return The 'Files' part of the menu
     */
    private Menu generateMenuFiles() {
        Menu menuFile = new Menu("File");
        MenuItem menuLoadFile = new MenuItem("Load File");
        MenuItem menuSaveRange = new MenuItem("Save a Range of Ticks");
        menuLoadFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open data file");
                File userFile = fileChooser.showOpenDialog(null);
                if (userFile != null){
                    if (gol.getCurrentTick() != 0){
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Data detected for current run!");
                        alert.setHeaderText("Are you sure you want to reset ALL current tick data?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() != ButtonType.OK){
                            return;
                        }
                    }
                    golui.loadFile(userFile.getName());
                }
            }
        });

        menuSaveRange.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                generateUserTickRangeDialog();
            }
        });
        menuFile.getItems().addAll(menuSaveRange, menuLoadFile);
        return menuFile;
    }


    /**
     * Generates the dialog box for the user to input the desired range of ticks for output and collects the output
     * @return The userinput in a Pair object with the starting tick (inclusive) in the key and the ending tick (inclusive) in the value
     */
    public void generateUserTickRangeDialog(){
        GridPane gridPane = new GridPane();

        startingTickRange.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    startingTickRange.setText(t1.replaceAll("[^\\d]", ""));
                }
                else if (t1.matches("")){
                    startingTickRange.setText("0");
                }
            }
        });
        endingTickRange.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    endingTickRange.setText(t1.replaceAll("[^\\d]", ""));
                }
                else if (t1.matches("")){
                    endingTickRange.setText("0");
                }
            }
        });
        Label startingTick = new Label("Starting tick: ");
        Label endingTick = new Label("Ending tick: ");
        gridPane.add(startingTick, 0, 0);
        gridPane.add(startingTickRange, 1, 0);
        gridPane.add(endingTick, 0, 1);
        gridPane.add(endingTickRange, 1 ,1);
        Dialog<Pair> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Tick Range");
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(userButton -> {
            if (userButton == ButtonType.OK){

                if (Integer.parseInt(endingTickRange.getText()) < Integer.parseInt(startingTickRange.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Range!");
                    alert.setContentText("Starting Tick must be less than or equal to Ending Tick!");
                    alert.showAndWait();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

}

