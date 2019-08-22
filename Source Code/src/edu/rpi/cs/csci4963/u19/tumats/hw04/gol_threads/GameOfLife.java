package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import java.util.Random;

/**
 * Handles all data related operations for the program. Also stores all tick data for the entirety of the program.
 * TODO
 *
 * @author Srikanth Tumati
 * @version 1.2
 * @since 1.0
 */
public class GameOfLife {

    /** the instance of the GameOfLife that will be used in the program */
    private static GameOfLife instance = new GameOfLife();
    /** board represents the two dimensional array that stores each cell and updates them depending on their neighbors*/
    private Byte[][] board = null;
    /** updateGeneration represents a temporary board that is used when each cell in the board is simultaneously updated*/
    private Byte[][] updateGeneration = null;
    /** The number of rows that the board must have */
    private int boardRows;
    /** The number of columns that the board must have */
    private int boardCols;
    /** The current tick of the program */
    public int currentTick = 0;

    /**
     * Populates the board when the values are initialized as null
     */
    public void populateBoard(){
        if (board == null){
            board = new Byte[boardRows][boardCols];
            updateGeneration = new Byte[boardRows][boardCols];
        }
        Random rand = new Random();
        for (int row = 0; row < boardRows; row++ ){
            for (int col = 0; col < boardCols; col++ ){
                if (board[row][col] == null){
                    if (rand.nextBoolean()){
                        board[row][col] = 1;
                    }
                    else{
                        board[row][col] = 0;
                    }
                }
//                    board[row][col] = 0;
            }
        }
    }

    /**
     * Changes the state of an individual cell within the current board
     * @param row The row for the cell which is being changed
     * @param col The column for the cell which is being changed
     * @param state True if the cell is being changed to 1 and False of the cell is being changed to 0
     */
    public void setState(int row, int col, boolean state){
        if (instance != null && row < board.length && col < board[0].length){
            if (state)
                board[row][col] = 1;
            else
                board[row][col] = 0;
        }
    }

    /**
     * Returns the String version of a specified board
     * @param printBoard the Board that will be converted into a String
     * @return The String equivalent of the board
     */
    public String boardToString(Byte[][] printBoard){
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < printBoard.length; row++){
            for(int column = 0; column < printBoard[0].length; column++){
                sb.append(printBoard[row][column]).append(" ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Returns the current number of rows in the board
     * @return The number of rows
     */
    public int getNumRows(){
        return boardRows;
    }

    /**
     * Returns the current number of columns in the board
     * @return The number of columns
     */
    public int getNumCols(){
        return boardCols;
    }

    /**
     * Returns the current game tick
     * @return The tick
     */
    public int getCurrentTick(){
        return currentTick;
    }

    /**
     * Resets all data structures and sets the tick to 0. This allows the GameOfLife to essentially 'restart'.
     */
    public void resetData(){
        boardRows = ConfigHandler.defaultRows;
        boardCols = ConfigHandler.defaultCols;
        board = null;
//        tickData.clear();
//        stateData.clear();
        currentTick = 0;
    }


    /**
     * Provides the value of a certain cell in the current board
     * @param row The desired row of the cell
     * @param col The desired column of the cell
     * @return 1 if the cell is alive and 0 otherwise
     */
    public boolean getState(int row, int col){
//        if (row == 9)
//            System.out.println("we here boy");
        try {
            return board[row][col] == 1;
        }
        catch(NullPointerException ex){
            System.out.println("bad row: " + row + " bad col: " + col);
        }
        return true;
    }

    /**
     * Initializes the board by setting it to the proper dimensions
     */
    public void initializeBoard(int row, int col){
        boardRows = row;
        boardCols = col;
        board = new Byte[row][col];
        updateGeneration = new Byte[row][col];
    }

    public Byte[][] getBoard(){
        return this.board;
    }

    /**
     * Ensures that the cell in question is either within bounds of the board or not in bounds
     * @param row The row of the cell in question
     * @param column The column of the cell in question
     * @return true if the cell is within bounds and false otherwise
     */
    public boolean checkBounds(int row, int column){
        return (board.length - 1 >= row && row >= 0 && column >= 0 && board[0].length - 1 >= column);
    }

    /**
     * Sets an entire row of the board with values that are given by rowData[].
     * @param rowData An Integer array containing values (of 1 or 0) for the cells of a certain row
     * @param row The desired row for which the data should be placed in
     */
    public void setRow(Byte[] rowData, int row){
        for (int count = 0; count < rowData.length; count++){
            board[row][count] = rowData[count];
        }
    }

    /**
     * This method performs the appropriate action by checking the current status of the cell and the number of live
     * neighbors surrounding it. It uses the following rules to determine the state of the cell. The cell is kept alive
     * if it has 2 or 3 cells (1). Any cell with fewer than two neighbors or more than three neighbors dies (0). Any cell
     * that is currently dead and has 3 neighboring cells that are alive also becomes alive (1).
     * @param row The specific row of the desired cell in which we are updating
     * @param column The specific column of the desired cell in which we are updating
     * @param liveNeighbors The number of live neighbors that the cell currently has
     */
    public void updateCell(int row, int column, int liveNeighbors){
        if (liveNeighbors == 3 || (liveNeighbors == 2 && board[row][column] == 1))
            updateGeneration[row][column] = 1;
        else
            updateGeneration[row][column] = 0;
    }

    /**
     * Performs all necessary operations needed to update the game tick. Stores updated board with cell data and state data.
     */
    public void updateTick(){
        for (int row = 0; row < boardRows; row++){
            for(int column = 0; column < boardCols; column++){
                board[row][column] = updateGeneration[row][column];
            }
        }
    }

    /**
     * This method confirms that each cell in the board checks 8 neighboring cells and wraps around the board
     * when necessary.
     * @param row The specific row which is being checked
     * @param column The specific column which is being checked
     * @return the number of neighbors to the source cell that are currently alive
     */
    public int checkNeighbors(int row, int column){
        int sum = 0;
        if (checkBounds(row, column + 1))
            sum += board[row][column + 1];
        else{
            sum += board[row][0];
        }

        if (checkBounds(row - 1, column))
            sum += board[row - 1][column];
        else{
            sum += board[board.length - 1][column];
        }

        if (checkBounds(row + 1, column))
            sum += board[row + 1][column];
        else{
            sum += board[0][column];
        }

        if (checkBounds(row, column - 1))
            sum += board[row][column - 1];
        else{
            sum += board[row][board[0].length - 1];
        }

        /** Top 4 are cardinal directions N S W E and should be all good */

        if (checkBounds(row + 1, column + 1))
            sum += board[row + 1][column + 1];
        else{
            /** First case is when point is on bottom row */
            if (board.length - 1 == row){
                /** Check if point is not the bottom right corner*/
                if (board[0].length - 1 > column)
                    sum += board[0][column + 1];
                /** Switch to 0,0 if bottom right corner point*/
                else
                    sum += board[0][0];
            }
            /** Second case is when point is on last column */
            else if (board[0].length - 1 == column){
                sum += board[row + 1][0];
            }
        }


        if (checkBounds(row - 1, column + 1))
            sum += board[row - 1][column + 1];
        else{
            /** First case is when point is on top row*/
            if (row == 0){
                /** Make sure we aren't dealing with top right point */
                if (board[0].length - 1 > column)
                    sum += board[board.length - 1][column + 1];
                else
                    sum += board[board.length - 1][0];
            }

            /** Second case is when we are dealing with last column */

            else if (board[0].length - 1 == column){
                sum += board[row - 1][0];
            }
        }

        /** Top left */
        if (checkBounds(row - 1, column - 1))
            sum += board[row - 1][column - 1];
        else{
            /** First case is when point is on top row*/
            if (row == 0){

                /** make sure its not the top left point */
                if (column != 0){
                    sum += board[board.length - 1][column - 1];
                }
                /** correct point for the top left point*/
                else
                    sum += board[board.length - 1][board[0].length - 1];

            }
            /** Second case is for first column */
            else if (column == 0)
                sum += board[row - 1][board[0].length - 1];

        }
        /** Bottom left */
        if (checkBounds(row + 1, column - 1))
            sum += board[row + 1][column - 1];
        else{
            /** First case is first column*/
            if (column == 0){
                /** confirm not bottom left point */
                if (row != board.length - 1){
                    sum += board[row + 1][board[0].length - 1];
                }
                else
                    sum += board[0][board[0].length - 1];
            }
            /** Second case is last row*/
            else if (board.length - 1 == row)
                sum += board[0][column - 1];

        }
        return sum;
    }



}
