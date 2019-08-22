package edu.rpi.cs.csci4963.u19.tumats.hw04.gol_threads;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UpdateCellRunnable implements Runnable{

    private int row;
    private int col;
    private GridPane gridPane;

    private Color defaultDeadColor = Color.rgb(211, 211, 211);
    /** Default color of an alive cell rgb(211, 211, 211) */
    private static Color defaultAliveColor = Color.BLACK;

    public UpdateCellRunnable(int row, int col, GridPane gridPane){
        this.row = row;
        this.col = col;
        this.gridPane = gridPane;
    }

    public void run(){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(1);
        rectangle.setHeight(1);
        rectangle.setFill(defaultAliveColor);
        gridPane.add(rectangle, col, row);
//        System.out.println("row: " + row + " col: " + col);
    }
}
