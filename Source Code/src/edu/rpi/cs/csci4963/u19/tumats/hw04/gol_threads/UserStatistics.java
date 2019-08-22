package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Creates the statistics that are displayed to the user for the entire lifetime of the program. Additionally, provides
 * methods for other classes to access to update the values in the statistics
 *
 * @author Srikanth Tumati
 * @version 1.0
 * @since 1.0
 */
public class UserStatistics extends GridPane {

    /** The GridPane that is used to house all the values and present them to the user in an organized manner */
    private GridPane gridPane = new GridPane();
    /** Contains the value for the tick that the program is currently on */
    private Label currentTickVal = new Label("0");
    private Label currentTick = new Label("Current Tick: ");

    /**
     * Constructor for the UserStatistics class
     */
    public UserStatistics(){
        gridPane.add(currentTick, 0,0);
        gridPane.add(currentTickVal, 1, 0);
        gridPane.setHgap(10);
        gridPane.setVgap(200);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Allows access to set the tick that is currently displayed to the user
     * @param val the number that should be displayed as the tick
     */
    public void setCurrentTickVal(int val){
        this.currentTickVal.setText(Integer.toString(val));
    }

    /**
     * Allows access to increment the tick counter by one
     */
    public void incrementTick(){
        currentTickVal.setText(Integer.toString(Integer.parseInt(currentTickVal.getText()) + 1));
    }


    /**
     * Returns the gridPane object that contains all of the statistics that will be displayed to the user
     * @return The populated GridPane
     */
    public GridPane getGridPane(){
        return gridPane;
    }

    /**
     * Resets all the statistics by setting all the values to 0.
     */
    public void resetStatistics(){
        this.currentTickVal.setText("0");
    }

}
