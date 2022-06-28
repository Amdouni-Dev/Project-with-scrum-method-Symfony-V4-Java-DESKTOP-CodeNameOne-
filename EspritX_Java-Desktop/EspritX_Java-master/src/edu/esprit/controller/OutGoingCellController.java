package edu.esprit.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class OutGoingCellController implements Initializable {

    @FXML
    private Label Content;
    public void setContent(String content){
        Content.setText(content);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Content.setMinWidth(Region.USE_PREF_SIZE);
        Content.setMaxWidth(Region.USE_PREF_SIZE);
        Content.textProperty().addListener((ov, prevText, currText) -> {
            // Do this in a Platform.runLater because of Textfield has no padding at first time and so on
            Platform.runLater(() -> {
                Text text = new Text(currText);
                text.setFont(Content.getFont()); // Set the same font, so the size is the same
                double width = text.getLayoutBounds().getWidth() // This big is the Text in the TextField
                        + Content.getPadding().getLeft() + Content.getPadding().getRight() // Add the padding of the TextField
                        + 2d; // Add some spacing
                Content.setPrefWidth(width); // Set the width

            });
        });
    }
}
