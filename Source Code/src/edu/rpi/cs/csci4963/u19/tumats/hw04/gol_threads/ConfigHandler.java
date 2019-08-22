package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.io.*;

/**
 * Handles all operations that are related to the Configuration File. Also contains the default values that are used in the
 * program and consequently placed in a configuration file if one is not found
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class ConfigHandler {

    /** defaultRows is the number of rows that program defaults to (10) */
    protected static int defaultRows = 10;
    /** defaultCols is the number of columns that program defaults to (10) */
    protected static int defaultCols = 10;
    /** maxTicks is the number of maximum ticks that program defaults to (50) */
    protected static int maxTicks = 50;
    /** folderPattern is the folder pattern that program defaults to ("output") */
    protected static String folderPattern = "output";
    /** filePattern is the file pattern that program defaults to ("tick") */
    protected static String filePattern = "tick";
    /** configFile is the configuration filename that program defaults to ("config.txt") */
    private static String configFile = "config.txt";
    /** defaultThreads holds the number of threads that will be used in the program */
    protected static int defaultThreads = 12;
    /** golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods' */
    private GameOfLifeUI golui;

    /**
     * Constructor for the ConfigHandler class
     * @param golui the GameOfLifeUI object that is currently being used in the program
     */
    public ConfigHandler(GameOfLifeUI golui){
        this.golui = golui;
    }

    /**
     * Writes the Configuration File when the program is first run (or not found)
     * @param folderPattern The folder pattern (or folder name) for the data files
     * @param filePattern The file pattern for the data files
     * @param maxNumTicks The maximum number of ticks that can be run
     * @param rows The number of rows that should be in the grid of the program
     * @param cols The number of columns that should be in the grid of the program
     */
    public static void writeConfigFile(String folderPattern, String filePattern, Integer maxNumTicks, Integer rows, Integer cols, Integer threads){
        File tempFile = new File(configFile);
        if (tempFile.exists())
            tempFile.delete();
        StringBuilder sb = new StringBuilder();
        sb.append(folderPattern).append('\n').append(filePattern).append('\n').append(maxNumTicks).append('\n').append(rows).append('\n').append(cols).append('\n').append(threads).append('\n');
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));
            bw.write(sb.toString());
            bw.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Performs checks to ensure that the datafile is valid and can be used by the program
     * @param configData An array with all values that are always within the configuration file
     */
    public static void validateConfig(String[] configData){
        try{
            for (int i = 0; i < configData.length; i++){
                if (configData[i].length() < 1)
                    throw new NumberFormatException("");
            }
            Integer.parseInt(configData[2]);
            Integer.parseInt(configData[3]);
            Integer.parseInt(configData[4]);
            Integer.parseInt(configData[5]);
        }
        catch(NumberFormatException ex){
            System.out.println("Invalid Configuration File Found!\nCreating new Configuration File...\n");
            generateDefaultConfig(configData);
            readConfigFile();
        }
    }

    /**
     * Creates a default configuration file when one is either not found or invalid
     * @param configData An array with all values that are always within the configuration file
     */
    public static void generateDefaultConfig(String[] configData){
        ConfigHandler.writeConfigFile(folderPattern, filePattern, maxTicks, defaultRows, defaultCols, defaultThreads);
        configData[0] = ConfigHandler.folderPattern;
        configData[1] = ConfigHandler.filePattern;
        configData[2] = Integer.toString(ConfigHandler.maxTicks);
        configData[3] = Integer.toString(ConfigHandler.defaultRows);
        configData[4] = Integer.toString(ConfigHandler.defaultCols);
        configData[5] = Integer.toString(ConfigHandler.defaultThreads);
    }

    /**
     * Reads the configuration file and also ensures that it is valid. If one is not found, a new configuration file is created
     * @return An array with all values that are always within the configuration file
     */
    public static String[] readConfigFile(){
        String[] configData = new String[6];
        try{
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            for (int i = 0; i < 6; i++ ){
                configData[i] = br.readLine();
            }
            ConfigHandler.folderPattern = configData[0];
            ConfigHandler.filePattern = configData[1];
            ConfigHandler.maxTicks = Integer.parseInt(configData[2]);
            ConfigHandler.defaultRows = Integer.parseInt(configData[3]);
            ConfigHandler.defaultCols = Integer.parseInt(configData[4]);
            ConfigHandler.defaultThreads = Integer.parseInt(configData[5]);
            validateConfig(configData);
        }
        catch(NumberFormatException ex){
            System.out.println("Configuration file unable to be read/found!\nCreating new Configuration File...\n");
            generateDefaultConfig(configData);
        }
        catch (IOException ex){
            System.out.println("Configuration file unable to be read/found!\nCreating new Configuration File...\n");
            generateDefaultConfig(configData);
        }

        return configData;
    }

    /**
     * Detects whether there is a change from the default values to the values in the configuration file
     * @param inputOutputFolderPattern The folder pattern that is in the configuration file
     * @param inputOutputFilePattern The file pattern that is in the configuration file
     * @param inputMaxNumTicks The max ticks that is in the configuration file
     * @param inputUserRows The number of rows that are in the configuration file
     * @param inputUserCols The number of columns that are in the configuration file
     * @return true if there was a detected change and false otherwise
     */
    public boolean detectConfigChange(TextField inputOutputFolderPattern, TextField inputOutputFilePattern, TextField inputMaxNumTicks, TextField inputUserRows, TextField inputUserCols, TextField inputThreads){
        return (!inputOutputFolderPattern.getText().equals(folderPattern) || !inputOutputFilePattern.getText().equals(filePattern) || !(Integer.parseInt(inputMaxNumTicks.getText()) == maxTicks) || !(Integer.parseInt(inputUserRows.getText()) == defaultRows) || !(Integer.parseInt(inputUserCols.getText()) == defaultCols) || !(Integer.parseInt(inputThreads.getText()) == defaultThreads));
    }

    /**
     * If change is detected in the configuration file, then the program is updated to account for these changes
     * @param folderPattern The folder pattern that is in the configuration file
     * @param filePattern The file pattern that is in the configuration file
     * @param maxTicks The max ticks that is in the configuration file
     * @param userRows The number of rows that are in the configuration file
     * @param userCols The number of columns that are in the configuration file
     */
    public void updateConfigChange(TextField folderPattern, TextField filePattern, TextField maxTicks, TextField userRows, TextField userCols, TextField userThreads){
        writeConfigFile(folderPattern.getText(), filePattern.getText(), Integer.parseInt(maxTicks.getText()), Integer.parseInt(userRows.getText()), Integer.parseInt(userCols.getText()), Integer.parseInt(userThreads.getText()));
        boolean dimensionChange = false;
        if (defaultRows != Integer.parseInt(userRows.getText()) || defaultCols != Integer.parseInt(userCols.getText()))
            dimensionChange = true;
        ConfigHandler.folderPattern = folderPattern.getText();
        ConfigHandler.filePattern = filePattern.getText();
        ConfigHandler.maxTicks = Integer.parseInt(maxTicks.getText());
        ConfigHandler.defaultRows = Integer.parseInt(userRows.getText());
        ConfigHandler.defaultCols = Integer.parseInt(userCols.getText());
        ConfigHandler.defaultThreads = Integer.parseInt(userThreads.getText());
        golui.updateDefaultVals(dimensionChange);
    }

    /**
     * Generates the Configuration Panel where the user is able to change various aspects of the program (number of rows/columns,
     * maximum number of ticks, folder pattern, and file pattern)
     */
    public void generateConfigDialog(){
        GridPane gridPane = new GridPane();
        Label userOutputFolderPattern = new Label("Output Folder Pattern: ");
        Label userOutputFilePattern = new Label("Output File Pattern: ");
        Label userMaxNumTicks = new Label("Maximum Number of Ticks: ");
        Label userRows = new Label("Number of Rows: ");
        Label userCols = new Label("Number of Columns: ");
        Label userThreads = new Label("Number of Threads: ");
        TextField inputThreads = new TextField(Integer.toString(defaultThreads));
        inputThreads.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    inputThreads.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        TextField inputOutputFolderPattern = new TextField(folderPattern);
        TextField inputOutputFilePattern = new TextField(filePattern);
        TextField inputMaxNumTicks = new TextField(Integer.toString(maxTicks));
        inputMaxNumTicks.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    inputMaxNumTicks.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        TextField inputUserCols = new TextField(Integer.toString(defaultCols));
        TextField inputUserRows = new TextField(Integer.toString(defaultRows));
        inputUserRows.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    inputUserRows.setText(t1.replaceAll("[^\\d]", ""));
                }
                inputUserCols.setText(t1);
            }
        });
        inputUserCols.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    inputUserCols.setText(t1.replaceAll("[^\\d]", ""));
                }
                inputUserRows.setText(t1);
            }
        });
        Label displayCores = new Label("Number of Cores Available: ");
        Label numOfCores = new Label("   " + Integer.toString(Runtime.getRuntime().availableProcessors()));
        gridPane.add(userRows, 0, 0);
        gridPane.add(userCols, 0, 1);
        gridPane.add(userMaxNumTicks, 0, 2);
        gridPane.add(userOutputFilePattern, 0 ,3);
        gridPane.add(userOutputFolderPattern, 0, 4);
        gridPane.add(userThreads, 0, 5);
        gridPane.add(inputUserRows, 1, 0);
        gridPane.add(inputUserCols, 1, 1);
        gridPane.add(inputMaxNumTicks, 1, 2);
        gridPane.add(inputOutputFilePattern, 1,3);
        gridPane.add(inputOutputFolderPattern, 1, 4);
        gridPane.add(inputThreads, 1, 5);
        gridPane.add(displayCores, 0, 6);
        gridPane.add(numOfCores, 1, 6);
        Dialog<String[]> dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setTitle("Configuration Panel");
        dialog.getDialogPane().setContent(gridPane);
        dialog.showAndWait();
        if (detectConfigChange(inputOutputFolderPattern, inputOutputFilePattern, inputMaxNumTicks, inputUserRows, inputUserCols, inputThreads)){
            updateConfigChange(inputOutputFolderPattern, inputOutputFilePattern, inputMaxNumTicks, inputUserRows, inputUserCols, inputThreads);
        }
    }
}
