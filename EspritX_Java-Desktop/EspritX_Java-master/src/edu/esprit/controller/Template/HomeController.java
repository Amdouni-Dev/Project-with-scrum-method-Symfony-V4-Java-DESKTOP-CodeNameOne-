package edu.esprit.controller.Template;

import edu.esprit.gui.Example;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @javafx.fxml.FXML
    private Button User;
    @javafx.fxml.FXML
    private Button Guest;

    @FXML
    public void UserView() throws IOException {
        Stage stage = new Stage();
        new Example().start(stage);
        User.getScene().getWindow().hide();
    }
}
