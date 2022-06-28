package edu.esprit.gui;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class InitControls {
    public InitControls(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png"))));
        stage.setMaximized(true);
        stage.setTitle("EspritX");
    }
}
