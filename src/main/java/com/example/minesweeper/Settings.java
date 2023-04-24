package com.example.minesweeper;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Settings {

    private static RadioButton darkModeButton = new RadioButton();

    public Settings() {
        darkModeButton.setOnAction(e -> {
            Minesweeper.setIsDark(darkModeButton.isSelected());
        });
    }

    private static Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(200, 200);

        HBox hbox = new HBox();

        Label darkModeLabel = new Label();

        darkModeLabel.setText("Dark Mode: ");
        darkModeButton.setSelected(Minesweeper.isDark);

        hbox.getChildren().add(darkModeLabel);
        hbox.getChildren().add(darkModeButton);

        root.getChildren().add(hbox);
        return root;
    }

    public static void display(){
        Stage stage = new Stage();
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.setResizable(false);

        stage.show();
    }
}
