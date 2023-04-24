package com.example.minesweeper;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.minesweeper.Minesweeper.*;

public class Tile extends StackPane{
    private static int MARGIN = 1;
    private static int HEADER = 50;

    private Rectangle rectangle;
    private Text text;
    private String currentText;
    private Text flag;

    private int x, y;
    boolean isAMine;
    private boolean isClicked = false;
    private boolean isFlagged = false;
    private boolean isDark = false;

    final private static ArrayList<String[]> langMap = new ArrayList<>();

    public Tile(int x, int y, boolean isAMine, boolean isClicked, boolean isFlagged, String conText){
        this.x = x;
        this.y = y;
        this.isAMine = isAMine;
        this.isClicked = isClicked;
        this.isFlagged = isFlagged;
        this.currentText = conText;

        initializeLangMap();

        int RECT_SIZE = 20;
        setTranslateX((x * (RECT_SIZE + MARGIN)) + MARGIN);
        setTranslateY((y * (RECT_SIZE + MARGIN)) + HEADER);
        rectangle = new Rectangle(RECT_SIZE, RECT_SIZE);

        if (!isClicked){
            rectangle.setFill(Color.GREY);
        } else if (isClicked) {
            rectangle.setFill(null);
        }

        text = new Text(" ");
        setText(currentText);

        flag = new Text("깃발");

        if (!isFlagged) {
            flag.setFill(null);
        } else if (isFlagged) {
            flag.setFill(Color.RED);
        }

        setOnMouseClicked(e -> onClick(e)); // Redirect to handle method in class. Double colon acts similar to lambda function

        getChildren().add(text);
        getChildren().add(rectangle);
        getChildren().add(flag);

        /*Text coord = new Text(); // Debug, show coordinates of each tile
        coord.setText(x + ", " + y);
        coord.setFont(Font.font(7));
        getChildren().add(coord);*/

    }

    public void setIsDark(boolean setDark){
        if(!isGenerated){
            isDark = setDark;
            return;
        }

        if (setDark){
            text.setFill(Color.color(.7,.7,.7,1));
        } else {
            text.setFill(Color.color(.204,.204,.204,1));
        }

        isDark = setDark;
    }

    public boolean getDark(){
        return isDark;
    }

    public boolean isAMine(){
        return isAMine;
    }

    public boolean isFlagged() {return isFlagged;}

    public void setFill(boolean fill){
        if(fill) rectangle.setFill(Color.GREY);
        if(!fill) rectangle.setFill(null);
    }

    public void setMine(boolean mine) {isAMine = mine;}

    public void setFlag(boolean Flag){
        isFlagged = Flag;
    }

    public boolean getFlag(){
        return isFlagged;
    }

    public void setClicked(boolean clicked){
        isClicked = clicked;
    }

    public boolean getClicked(){
        return isClicked;
    }

    public void setText(String value){
        if(value.equals("X")) {
            text.setText("X");
            currentText = "X";
            return;
        }
        for(String[] langPair : langMap){
            if(value.equals(langPair[0])){
                text.setText(langPair[1]);
                currentText = langPair[0];
                return;
            }
        }
        text.setText(value);
        currentText = value;
    }

    public String getText(){
        if(text.getText().equals("X")){
            return "X";
        }
        for(String[] langPair : langMap){
            if(text.getText().equals(langPair[1])){
                return langPair[0];
            }
        }
        return text.getText();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    private void reveal(){
        setClicked(true);
        rectangle.setFill(null);
    }

    private void initializeLangMap(){
        langMap.add(new String[]{"0", " "});
        langMap.add(new String[]{"1", "일"});
        langMap.add(new String[]{"2", "이"});
        langMap.add(new String[]{"3", "삼"});
        langMap.add(new String[]{"4", "사"});
        langMap.add(new String[]{"5", "오"});
        langMap.add(new String[]{"6", "육"});
        langMap.add(new String[]{"7", "칠"});
        langMap.add(new String[]{"8", "구"});
    }

    public void onClick(MouseEvent e) {

        try {
            if (e.getButton() == MouseButton.SECONDARY) {
                System.out.println("RIGHT CLICK");
                if (isFlagged && !isClicked) {
                    isFlagged = !isFlagged;
                    flag.setFill(null);

                    currentMines += 1;
                    updateMineCount();

                } else if (!isFlagged && !isClicked) {
                    flag.setFill(Color.RED);
                    isFlagged = !isFlagged;

                    currentMines -= 1;
                    updateMineCount();

                }
                return;
            }
        } catch (NullPointerException n){
            System.out.println("Previous tile was blank!");
        }

        if(isClicked || isFlagged){
            System.out.println("Was a flag: " + isFlagged);
            return;
        }

        if(!isGenerated){
            isGenerated = true;
            text.setText("0");

            generate(x, y);
        }

        System.out.println("clicked!");
        rectangle.setFill(null);
        unclickedTiles.remove(this);

        if(isAMine){
            System.out.println("You lose");

            for (int i = 0; i < 30; i++){
                for (int j = 0; j < 16; j++){
                    if(tileMap[i][j].isAMine) tileMap[i][j].reveal();
                }
            }

        } else if (this.getText().equals("0")){
            System.out.println("BLANK");
            isClicked = true;
            ArrayList<Tile> adjacentTiles = getAdjacentTiles(x, y);

            for(Tile tile : adjacentTiles){
                System.out.println(x + ", " + y);
                tile.onClick(null);
            }

        }
        isClicked = true;

        for (Tile tile : unclickedTiles){
            if (!tile.isAMine()){
                System.out.println("You didnt win yet..");
                return;
            }
        }

        Minesweeper.isWon();

        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                if(tileMap[i][j].isAMine) tileMap[i][j].reveal();
            }
        }
    }

    public void destroyTile(){
        getChildren().remove(rectangle);
        getChildren().remove(text);
        getChildren().remove(flag);
    }

    public SerializedTile convert(){
        SerializedTile newTile;
        newTile = new SerializedTile(x, y, isAMine, isClicked, isFlagged, currentText, isDark);

        return newTile;
    }

    @Override
    public String toString(){
        return x + ", " + y + ". " + "A mine: " + isAMine;
    }

}
