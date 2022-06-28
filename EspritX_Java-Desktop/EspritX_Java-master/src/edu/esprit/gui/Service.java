package edu.esprit.gui;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import edu.esprit.controller.ServiceController;
import edu.esprit.controller.Template.ExampleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Service extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("Service.pebble.fxml");

        Map<String, Object> context = new HashMap<>();

        File file = File.createTempFile("views/Index", "fxml");
        file.deleteOnExit();

        PrintWriter writer = new PrintWriter(file);
        compiledTemplate.evaluate(writer, context);
        writer.close();

        FXMLLoader loader = new FXMLLoader();

        loader.setController(new ServiceController());
        loader.setLocation(file.toURI().toURL());

        AnchorPane pane = loader.load();
        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);

        new InitControls(primaryStage);
        primaryStage.show();

    }
}
