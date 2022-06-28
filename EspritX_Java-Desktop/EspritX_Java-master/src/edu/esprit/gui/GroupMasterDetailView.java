package edu.esprit.gui;

import edu.esprit.controller.GroupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GroupMasterDetailView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GroupMasterDetailView.class.getResource("/edu/esprit/group/master-detail-view.fxml"));
        fxmlLoader.setController(new GroupController());
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("ESPRITX DESKTOP CLIENT.");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}