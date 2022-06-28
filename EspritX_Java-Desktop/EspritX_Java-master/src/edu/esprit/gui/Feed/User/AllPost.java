package edu.esprit.gui.Feed.User;

import edu.esprit.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class AllPost extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/Template/Feed/User/AllPost.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Welcome User!");
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.resizableProperty().set(false);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Template/Images/icon.png"))));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - scene.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - scene.getHeight()) / 2);
        //stage.centerOnScreen();
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }}
