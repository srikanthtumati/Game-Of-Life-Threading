package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * Handles all operations relating to the Grid that is visible to the user. Additionally, event handlers are added
 * to the grid to make it responsive.
 *
 * @author Srikanth Tumati
 * @version 1.2
 * @since 1.0
 */
public class GridView extends GridPane {

//    private int threadID;
    /**
     * gridPane is the GridPane where all cells are added
     */
    private GridPane gridPane = new GridPane();
    /**
     * gol is the GameOfLife object where all data related operations occur
     */
    private GameOfLife gol;
    /**
     * golui is an instance of the GameOfLifeUI and allows the UserMenu to call various 'central methods'
     */
    private GameOfLifeUI golui;
    /**
     * userStatistics represents the instance of UserStatistics that is used to display data about the current tick to the user
     */
    private UserStatistics userStatistics;

    /** gridData is a HashMap that stores all Rectangles that are present in the grid */
//    private HashMap<Pair<Integer, Integer>, Rectangle> gridData = new HashMap<>();

    /**
     * Default color of a dead cell rgb(255, 255, 255)
     */
    private static Color defaultDeadColor = Color.rgb(211, 211, 211);
    /**
     * Default color of an alive cell rgb(211, 211, 211)
     */
    private static Color defaultAliveColor = Color.BLACK;
    /** stores the "scale" factor when the grid is large enough */
    private int y;
    /** stores the amount of pixels for each side of the cell */
    private double pixelsPerCell;
    /** the number of rows in the grid visible to the user */
    private int gridRows = ConfigHandler.defaultRows;
    /** the number of columns visible to the user */
    private int gridCols = ConfigHandler.defaultCols;

    /**
     * Constructor for the GridView class
     *
     * @param gol            The GameOfLife object that is currently being used in the program
     * @param golui          The GameOfLifeUI object that is currently being used in the program
     * @param userStatistics the UserStatistics object that is currently being used in the program
     */
    public GridView(GameOfLife gol, GameOfLifeUI golui, UserStatistics userStatistics) {
        this.gol = gol;
        this.golui = golui;
        this.userStatistics = userStatistics;
    }

    /**
     * Retrieves the GridPane (does not have to be populated)
     *
     * @return The GridPane to be displayed in the GUI
     */
    public GridPane getGridView() {
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    public void generateGrid(int minDimension) {
        y = 1;
        pixelsPerCell = (double) minDimension / ConfigHandler.defaultRows;
        if (pixelsPerCell < 1) {
            for (int temp = minDimension; temp > 1; temp--){
                if (ConfigHandler.defaultRows%temp == 0){
                    y = ConfigHandler.defaultRows/temp;
                    break;
                }
            }
            gridRows = ConfigHandler.defaultRows / y;
            gridCols = ConfigHandler.defaultCols / y;
            pixelsPerCell = 1;
        } else {
            gridRows = ConfigHandler.defaultRows;
            gridCols = ConfigHandler.defaultCols;
        }
        gridPane.getChildren().clear();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int aliveCount;
                int deadCount;
                for (int row = 0; row < gridRows; row ++) {
                    for (int col = 0; col < gridCols; col ++) {
                        Rectangle rectangle = new Rectangle();
                        rectangle.setWidth(pixelsPerCell);
                        rectangle.setHeight(pixelsPerCell);
                        aliveCount = 0;
                        deadCount = 0;
                        for (int horizontal = 0; horizontal < y; horizontal++) {
                            for (int vertical = 0; vertical < y; vertical++) {
                                if (gol.getState(row + horizontal, col + vertical)) {
                                    aliveCount += 1;
                                } else {
                                    deadCount += 1;
                                }
                            }
                        }
                        if (aliveCount >= deadCount) {
                            rectangle.setFill(defaultAliveColor);

                        } else {
                            rectangle.setFill(defaultDeadColor);

                        }
                        gridPane.add(rectangle, col, row);
                    }
                }
            }
        });
    }

}

