package edu.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CellController implements Initializable {

    @FXML
    private Label Name;

    @FXML
    private Label TextPlaceholder;

    @FXML
    private Label IdLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IdLabel.setVisible(false);
    }
    public String valuelabel(){
       return Name.getText();
    }
    public void initData(String init,Integer id) {
        Name.setText(init);
        IdLabel.setText(id.toString());
    }
    public String GetConversation() {
       return IdLabel.getText();
    }

}

