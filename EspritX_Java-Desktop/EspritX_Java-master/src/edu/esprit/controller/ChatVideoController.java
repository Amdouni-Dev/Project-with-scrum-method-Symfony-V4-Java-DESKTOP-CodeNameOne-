package edu.esprit.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatVideoController implements Initializable {

    @FXML
    private WebView WebView;

    private WebEngine engine;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Process p = Runtime.getRuntime().exec("\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\" \"https://139-162-157-203.ip.linodeusercontent.com/PiDev3A\"");
        } catch (IOException e) {
            e.printStackTrace();
        }

        engine = WebView.getEngine();
        engine.load("https://139-162-157-203.ip.linodeusercontent.com");
        //engine.load("https://google.com");
    }
}
