package edu.esprit;

import edu.esprit.controller.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserStubApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/edu/esprit/group/master-detail-view.fxml"));
        fxmlLoader.setController(new UserController());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("ESPRITX DESKTOP CLIENT.");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}