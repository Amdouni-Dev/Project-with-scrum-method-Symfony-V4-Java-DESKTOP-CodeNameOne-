package edu.esprit.controller.Template;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ExampleController extends MainViewController {
    @FXML
    private Label hello;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        super.initialize(url,resourceBundle);
        hello.setLayoutX(150.0);
    }
}
