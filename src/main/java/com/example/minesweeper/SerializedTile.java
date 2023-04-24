package com.example.minesweeper;

import java.io.Serializable;

public class SerializedTile implements Serializable { // Representation of Tile that can be represented in byte format
    private int x, y;
    boolean isAMine;
    private boolean isClicked;
    private boolean isFlagged;
    private String text;
    private boolean isDark;

    public SerializedTile(int x, int y, boolean isAMine, boolean isClicked, boolean isFlagged, String text, boolean isDark){
        this.x = x;
        this.y = y;
        this.isAMine = isAMine;
        this.isClicked = isClicked;
        this.isFlagged = isFlagged;
        this.text = text;
        this.isDark = isDark;
    }

    public Tile convert() { // Convert to usable Tile object
        Tile newTile;
        newTile = new Tile(x, y, isAMine, isClicked, isFlagged, text);
        newTile.setIsDark(isDark);

        return newTile;
    }

}
