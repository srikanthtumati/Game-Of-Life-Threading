package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.concurrent.Task;

/**
 * Updates the board based on the cells that the thread is responsible for
 */
public class UpdateGraphTask extends Task<Void> {

    /** the starting row that the thread begins working on */
    private int startingRow;
    /** the ending row that the thread begins working on */
    private int endingRow;
    /** a copy of the GameOfLife object used to collect cell state */
    private GameOfLife gol;

    /**
     * Constructor for the UpdateGraphTask class
     * @param startingRow the starting row that the thread begins working on
     * @param endingRow the ending row that the thread begins working on
     * @param gol a copy of the GameOfLife object used to collect cell state
     */
    public UpdateGraphTask(int startingRow, int endingRow, GameOfLife gol){
        this.endingRow = endingRow;
        this.startingRow = startingRow;
        this.gol = gol;
    }

    /**
     * The "run" method performed by the thread
     * @return always null
     */
    public Void call(){
        int liveNeighbors;
        for (int row = startingRow; row < endingRow; row++ ){
            for (int col = 0; col < ConfigHandler.defaultCols; col++ ){
                liveNeighbors = gol.checkNeighbors(row, col);
                gol.updateCell(row, col, liveNeighbors);
            }
        }
        return null;


    }
}
