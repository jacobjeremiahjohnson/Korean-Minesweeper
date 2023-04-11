package com.example.minesweeper;

import java.io.Serializable;

public class SerializedTile implements Serializable {
    private int x, y;
    boolean isAMine;
    private boolean isClicked;
    private boolean isFlagged;
    private String text;

    public SerializedTile(int x, int y, boolean isAMine, boolean isClicked, boolean isFlagged, String text){
        this.x = x;
        this.y = y;
        this.isAMine = isAMine;
        this.isClicked = isClicked;
        this.isFlagged = isFlagged;
        this.text = text;
    }

    public Tile convert() {
        Tile newTile;
        newTile = new Tile(x, y, isAMine, isClicked, isFlagged, text);

        return newTile;
    }

}
