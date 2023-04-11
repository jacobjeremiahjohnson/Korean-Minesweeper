package com.example.minesweeper;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Minesweeper extends Application {

    public static Tile tileMap[][] = new Tile[30][16]; // Map of all Tile objects

    public static ArrayList<Tile> unclickedTiles = new ArrayList<>(); // List of all unclicked tiles

    static boolean isGenerated = true; // Flag that is set to true after first tile checked
    public static int currentMines = 0; // Current mine count (for GUI)
    public static Text mineCount = new Text();

    private static Pane root;

    private boolean isWon;

    public Minesweeper(){
        FileController fileController = new FileController();

        try { // Try to load tile map from file
            tileMap = fileController.read();

            boolean noClickedTiles = true;

            for (int i = 0; i < 30; i++){
                for (int j = 0; j < 16; j++){
                    if(tileMap[i][j].getClicked()) {
                        noClickedTiles = false;
                    } else {
                        unclickedTiles.add(tileMap[i][j]);
                    }
                    if(tileMap[i][j].isAMine()){
                        currentMines += 1;
                    }
                    if(tileMap[i][j].isFlagged()){
                        currentMines -= 1;
                    }
                }
            }

            if(noClickedTiles) {
                Exception e = new Exception();
                throw e;
            }
        }

        catch (Exception e){ // Load a new game if file can't work or no tiles were selected in save
            System.out.println("Default tile map loaded");

            isGenerated = false;
            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 16; j++) {
                    Tile tile  = new Tile(i, j, false, false, false, "0");
                    tileMap[i][j] = tile;
                }
            }
        }
    }

    private Parent createContent() { // Draw all elements based on tile map

        root = new Pane();
        root.setPrefSize(634, 386);
        mineCount = new Text("Mine Count: " + currentMines);

        mineCount.setTranslateX(100);
        mineCount.setTranslateY(20);
        root.getChildren().add(mineCount);

        Label title = new Label("Korean Minesweeper");
        title.setTranslateX(300);
        title.setTranslateY(20);
        root.getChildren().add(title);

        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                root.getChildren().add(tileMap[i][j]);
            }
        }
        return root;
    }

    public static void updateMineCount(){
        mineCount.setText("Mine Count: " + currentMines);
    }

    public static void generate(int x, int y){ // Generate mines after first tile checked
        ArrayList<Tile> adjacentTilesToStart = getAdjacentTiles(x, y);

        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                if (Math.random() < 0.19){
                    tileMap[i][j].setMine(true);
                    tileMap[i][j].setText("X");

                    currentMines += 1;
                    updateMineCount();
                }
                unclickedTiles.add(tileMap[i][j]);
            }
        }

        tileMap[x][y].setMine(false);
        tileMap[x][y].setText("0");

        unclickedTiles.remove(tileMap[x][y]);

        for (Tile tile : adjacentTilesToStart){
            tile.setMine(false);
            tile.setText("0");

            unclickedTiles.remove(tile);
        }

        numberGenerator();
    }

    private static void numberGenerator(){ // Fill tiles with numbers
        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                if(tileMap[i][j].isAMine()){
                    ArrayList<Tile> adjacentTiles = getAdjacentTiles(i, j);
                    for(Tile tile : adjacentTiles){
                        tile.setText(String.valueOf(Integer.parseInt(tile.getText()) + 1));
                    }
                }
            }
        }
    }

    public static ArrayList<Tile> getAdjacentTiles(int x, int y){ // Fill an ArrayList with each tile that surrounds a given tile
        ArrayList<Tile> adjacentTiles = new ArrayList<>();

        int offsetMap[][] = {
                {-1, -1},
                {-1, 0},
                {-1, 1},
                {0, 1},
                {1, 1},
                {1, 0},
                {1, -1},
                {0, -1}
        };

        for (int i = 0; i < 8; i++){
            Boolean isAMine;
            try{
                isAMine = tileMap[x + offsetMap[i][0]][y + offsetMap[i][1]].isAMine();
            }catch(IndexOutOfBoundsException e){ //Mine touches border
                System.out.println(e);
                continue;
            }

            if (!isAMine){
                adjacentTiles.add(tileMap[x + offsetMap[i][0]][y + offsetMap[i][1]]);
            }
        }
        return adjacentTiles;
    }

    private void onKeyPress(KeyEvent e){
        if (e.getCode() == KeyCode.F2){
            System.out.println("F2 PRESSED");


        }
        if (e.getCode() == KeyCode.F3){ // Reset everything
            System.out.println("F3 PRESSED");

            unclickedTiles = new ArrayList<>();
            isGenerated = false;
            currentMines = 0;

            updateMineCount();

            for (int i = 0; i < 30; i++) {
                for (int j = 0; j < 16; j++) {
                    tileMap[i][j].destroyTile();

                    tileMap[i][j] = null;
                    Tile tile = new Tile(i, j, false, false, false, "0");
                    tile.setFlag(false);
                    tile.setClicked(false);

                    tileMap[i][j] = tile;

                    root.getChildren().add(tile);
                }
            }

        }
    }

    @Override
    public void stop(){
        FileController fileController = new FileController();
        fileController.write(tileMap);
        System.out.println("Save file");
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> onKeyPress(e));
        stage.setTitle("Korean Minesweeper - F2 for Settings - F3 for New Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}