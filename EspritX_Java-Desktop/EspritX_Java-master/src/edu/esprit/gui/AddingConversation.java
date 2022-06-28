package edu.esprit.gui;

import edu.esprit.entities.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AddingConversation extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainView.class.getResource("/Chat/AddConver.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EspritX");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png"))));
        stage.setWidth(608);
        stage.setHeight(410);
        stage.show();
    }
}
